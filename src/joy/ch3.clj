(ns joy.ch3
  "Dipping our toes in the pool")

(if true :yes :no)
(if [] :yes :no)
(if nil :yes :no)
(if false :yes :no)

;; nil vs false
(when (nil? nil)
  "Actually nil not false")

;; nil punning
(seq [1 2 3])
(seq nil)

(defn print-seq [s]
  (when (seq s)
    (prn (first s))
    (recur (rest s))))

(print-seq [1 2 3])

;; destructuring
(def guys-whole-name ["Guy" "Lewis" "Steele"])

(let [[f m l] guys-whole-name]
  (str l ", " f " " m))

;; positional destructuring with regex
(let [[a b]])

;; destrucuture a map
(def guys-name-map {:f-name "Guy"
                    :m-name "Lewis"
                    :l-name "Steele"})
(let [{f :f-name m :m-name l :l-name} guys-name-map]
  (str l ", " f " " m))

(let [{:keys [f-name m-name l-name]} guys-name-map]
  (str l-name ", " f-name " " m-name))

;; using `:as` in map destructure
(let [{f-name :f-name, :as whole-name} guys-name-map]
  whole-name)

;; using `:or` in map destructure
(let [{:keys [title f-name m-name l-name] :or {title "Mr."}} guys-name-map]
  (println title f-name m-name l-name))

;; using `:syms`
(let [{:syms [a b c d]} {'a 1 'b 2 'c 3}]
  [a b c d])

;; using `:strs`
(let [{:strs [a b c]} {"a" 1 "b" 2}]
  [a b c])

;; also vector is associative
(let [{a 0 b 1 c 5} [10 20 30 40 50 60]]
  [a b c])

;; destructure function params
(defn print-last-name [{:keys [l-name]}]
  (println l-name))

(print-last-name guys-name-map)

;; some experiment
(range 5)
(for [x (range 2) y (range 3)] [x y])
(bit-xor 1 2)
(for [x (range 2) y (range 2)] [x y (bit-xor x y)])

(defn xors
  [max-x max-y]
  (for [x (range max-x) y (range max-y)]
    [x y (rem (bit-xor x y) 256)]))

;; some graphics
(def frame (java.awt.Frame.))
(do frame)

(for [m (seq (.getMethods java.awt.Frame))
      :let [method-name (.getName m)]
      :when (re-find #"dis" method-name)]
  method-name)

(.setVisible frame true)
(.setSize frame (java.awt.Dimension. 200 200))
(.isVisible frame)

(def gfx (.getGraphics frame))

(.fillRect gfx 100 100 50 75)
(.setColor gfx (java.awt.Color. 255 128 0))
(.fillRect gfx 100 150 75 50)

(defn clear [g] (.clearRect g 0 0 200 200))

(doseq [[x y xor] (xors 500 500)]
  (.setColor gfx (java.awt.Color. xor xor xor))
  (.fillRect gfx x y 1 1))

(clear gfx)

;; What whould other functions do
(defn f-values
  [f xs ys]
  (for [x (range xs) y (range ys)]
    [x y (rem (f x y) 256)]))

(defn f-draw
  [f xs ys]
  (clear gfx)
  (doseq [[x y v] (f-values f xs ys)]
    (.setColor gfx (java.awt.Color. v v v))
    (.fillRect gfx x y 1 1)))

(f-draw bit-xor 200 200)
(f-draw bit-and 200 200)
(f-draw bit-or 200 200)
(f-draw + 200 200)
(f-draw * 200 200)
