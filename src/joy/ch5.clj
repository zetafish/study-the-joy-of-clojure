(ns joy.ch5
  "Composite Data Types"
  (:require [clojure.set :as set]))

(vec (range 5))

(let [v [:a :b]]
  (into v (range 5)))

(replace {2 :a, 4 :b} [1 2 3 2 3 4])
(replace {2 :a, 4 :b} '(1 2 3 2 3 4))

(def matrix
  [[1 2 3]
   [4 5 6]
   [7 8 9]])

(get-in matrix [1 2])

(assoc-in matrix [1 2] :x)

(update-in matrix [1 2] * 100)

(defn neighbors
  ([size yx] (neighbors [[-1 0] [1 0] [0 -1] [0 1]] size yx))
  ([deltas size yx]
   (filter (fn [new-yx]
             (every? #(< -1 % size) new-yx))
           (map #(map + yx %) deltas))))

(map #(get-in matrix %) (neighbors 3 [0 0]))


(def y (apply list (range 100000)))
(time (last y))
(time (peek y))


;; vector as stack: conj peek pop
;; peek is faster than last
(def my-stack [1 2 3])
(peek my-stack)
(pop my-stack)
(conj my-stack 10)

;; subvector
(def a-to-j (vec "abcdefghij"))
(subvec a-to-j 3 6)

(doseq [[dimension amount] {:width 10, :height 20, :depth 15}]
  (println (str (name dimension) ":") amount "inches"))

;; Caveats:
;; - vectors aren't sparse
;; - vectors aren't queues
;; - vectors aren't sets

;; Lists

(counted? (range 10))
(class (range 10))
(class (cons 80 (range 10)))

;; persistent queues
(def q clojure.lang.PersistentQueue/EMPTY)

(defmethod print-method clojure.lang.PersistentQueue
  [q, w]
  (print-method '<- w) (print-method (seq q) w) (print-method '-< w))

;; pop/conj returns queue
;; rest/next returns a seq
(println q)
(-> q
    (conj 1)
    (conj 2)
    (conj 3)
    (conj 4)
    (pop)
    (conj 5))

(def schedule (conj q :wake-up :shower :brush-teeth))

(peek schedule)
(pop schedule)
(rest schedule)
(next schedule)

;; persistent sets
(#{:a :b :c :d} :c)
(#{:a :b :c :d} :e)
(set [[] ()])
(set [[] () #{} {}])

;; sorted set
(sorted-set :b :c :a)
(sorted-set [3 4] [1 2])
(sorted-set :b 2 :c :a 3 1)

(def my-set (sorted-set :a :b))
(conj my-set "a")
(println my-set)


;; contains?
(contains? #{1 2 4 3} 4)  ;; compares elems of set
(contains? [1 2 4 3] 4)   ;; compares indices of vec

;; clojure.set
(set/intersection #{1 2 3} #{3 4 5})
(set/union #{1 2 3} #{3 4 5})
(set/difference #{1 2 3} #{3 4 5}) ;; relative complement (not opposite of union)

;; hash-maps
(hash-map :a 1, :b 2, :c 3, :d 4, :e 5)

;; heterogenous
(let [m {:a 1, 1 :b, [1 2 3] "4 5 6"}]
  [(get m :a) (get m [1 2 3])])

(seq {:a 1, :b 2})
(into {} (seq {:a 1, :b 2}))
(into {} (map vec '[(:a 1) (:b 2)]))
(apply hash-map [:a 1 :b 2])

(zipmap [:a :b] [1 2])

;; sorted map
(sorted-map :thx 1234 :r2d 2)
(sorted-map "bac" 2 "abc" 9)
(sorted-map-by #(compare (subs %1 1) (subs %2 1))
               "bac" 2 "abc" 9)

(sorted-map :a 1 "b" 2)
(sorted-map-by #(compare (name %1) (name %2)) :a 1 "b" 2)

;; With sorted map (and sorted set) you can jump efficiently to a key
;; and then walk back and forth with `subseq` and `rsubseq`.
(subseq (sorted-map :a 1 :b 2 :c 3) > :a)
(subseq (sorted-map :a 1 :b 2 :c 3 :d 4 :e 4) >= :b <= :d)

(assoc {1 :int} 1.0 :float)
(assoc (sorted-map 1 :int) 1.0 :float)

;; keeping insertion order with array-map
(seq (hash-map :a 1, :b 2, :c 3))
(seq (array-map :a 1, :b 2, :c 3))

;; Putting it together - finding the position of items in a sequence.

(defn pos
  [e coll]
  (loop [s coll idx 0]
    (cond
      (not (seq s)) idx
      (map? coll) (if (= e (second (first s)))
                    (ffirst s)
                    (recur (rest s) (inc idx)))
      :else (if (= e (first s))
              idx
              (recur (rest s) (inc idx))))))

(defn index
  [coll]
  (cond
    (map? coll) (seq coll)
    (set? coll) (map vector coll coll)
    :else (map vector (iterate inc 0) coll)))

(defn pos
  [e coll]
  (map first
       (filter #(= e (second %)) (index coll))))

(defn pos
  [pred coll]
  (for [[i v] (index coll)
        :when (pred v)] i))

(pos #(= 3 %) [:a 1 :b 2 :c 3 :d 4])
(pos #(= 3 %) {:a 1 :b 2 :c 3 :d 4})
(pos #(= 3 %) '(:a 1 :b 2 :c 3 :d 4))
(pos #(= \space %) ":a 1 :b 2 :c 3 :d 4 3 3")
