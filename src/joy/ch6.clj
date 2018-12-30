(ns joy.ch6
  "Being lazy and set in your ways.")

;; Topics
;; - immutability
;; - designing a persistent toy
;; - laziness
;; - putting it together: a lazy quicksort

;; Immutability brings simplification of:
;; - invariants: only needed at construction
;; - reasoning:
;; - equality has meaning: when things can change how can objects be equal?
;; - sharing is cheap
;; - flattening levels of indirection
;; - fosters concurrent programming

;; Desigining a persistent toy
(def baselist (list :barnabas :adam))
(def lst1 (cons :willie baselist))
(def lst2 (cons :phoenix baselist))

(= (next lst1) (next lst2))
(identical? (next lst1) (next lst2))

;; simple tree
{:val 5 :L nil :R nil}

(defn xconj [t v]
  (cond
    (nil? t)       {:val v :L nil :R nil}
    (< v (:val t)) {:val (:val t)
                    :L (xconj (:L t) v)
                    :R (:R t)}
    :else          {:val (:val t)
                    :L (:L t)
                    :R (xconj (:R t) v)}))

(defn xseq [t]
  (when t
    (concat (xseq (:L t)) [(:val t)] (xseq (:R t)))))

(-> nil
    (xconj 5)
    (xconj 3)
    (xconj 8)
    (xconj 100)
    (xconj 20)
    xseq)

(def tree1 (-> nil
               (xconj 5)
               (xconj 3)
               (xconj 2)))

;; Laziness

;; lazy-seq
(defn steps
  [coll]
  (if (seq coll)
    [(first coll) (steps (rest coll))]
    []))

(steps [1 2 3 4])
(steps (range 10000))

(defn lz-steps
  [coll]
  (lazy-seq
    (if (seq coll)
      [(first coll) (lz-steps (rest coll))]
      [])))

(lz-steps [1 2 3 4])
(dorun (lz-steps (range 10000000)))

;; holding on to head
(let [r (range 1e9)] [(first r) (last r)])
(let [r (range 1e9)] [(last r) (first r)])

(iterate (fn [n] (/ n 2)) 1)

(defn triangle [n]
  (/ (* n (+ n 1)) 2))

(triangle 10)

(map triangle (range 1 11))
(def tri-nums (map triangle (iterate inc 1)))
(take 10 tri-nums)
(take 10 (filter even? tri-nums))
(nth tri-nums 99)

(double (reduce + (take 1000 (map / tri-nums))))

(take 2 (drop-while #(< % 10000) tri-nums))

;; delay and force
(defn stall-expensive [cheap expensive]
  (if-let [good-enough (force cheap)]
    good-enough
    (force expensive)))

(stall-expensive (delay false)
                 (delay (do (Thread/sleep 5000) :expensive)))

(defn inf-triangles [n]
  {:head (triangle n)
   :tail (delay (inf-triangles (inc n)))})

(defn head  [l]   (:head l))

(defn tail  [l]   (force (:tail l)))

(def tri (inf-triangles 1))
(head (tail (tail (tail tri))))


(defn taker
  [n coll]
  (loop [t n src coll ret []]
    (if (zero? t)
      ret
      (recur (dec t) (tail src) (conj ret (head src))))))

(defn nthr
  [coll n]
  (if (zero? n)
    (head coll)
    (recur (tail coll) (dec n))))

(taker 10 tri)

(nthr tri 99)

;; And now putting it together: lazy quicksort

(defn nom [n] (take n (repeatedly #(rand-int n))))

(defn sort-parts
  "Lazy, tail recursive implementation of quicksort. Works against and
  creates partitions based on the pivot, defined as `work`."
  [work]
  (lazy-seq
    (loop [[part & parts :as pp] work]
      (if-let [[pivot & xs] (seq part)]
        (let [smaller? #(< % pivot)]
          (recur (list*
                   (filter smaller? xs)
                   pivot
                   (remove smaller? xs)
                   parts)))
        (when-let [[x & parts] parts]
          (cons x (sort-parts parts)))))))

(defn qsort [xs]
  (sort-parts (list xs)))

(def xs (nom 100000))

(time (count (take 10 (sort xs))))
(time (count (take 10 (qsort xs))))
