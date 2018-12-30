(ns joy.ch4
  "On Scalars")

;; truncation
@(def full-precision 3.14159265358979323846264338327950288419716939937M)

@(def trunc-precision 3.14159265358979323846264338327950288419716939937)

;; promotion
(def clueless 9)
(class clueless)

(class (+ clueless 9000000000000000))
(class (+ clueless 90000000000000000000))
(class (+ clueless 1.0))

;; overflow
(+ Long/MAX_VALUE Long/MAX_VALUE)
(+ Long/MAX_VALUE 1)

(unchecked-add Long/MAX_VALUE 1)
(unchecked-add Long/MAX_VALUE Long/MAX_VALUE)

;; underflow
(float 0.0000000000000000000000000000000000000000000001)
(float 1E-430)

(+ 0.1M 0.1M 0.1M 0.1M 0.1M 0.1M 0.1M 0.1M 0.1M 0.1M 0.1M 0.1M)
(+ 0.1M 0.1M 0.1M 0.1M 0.1M 0.1M 0.1M 0.1M 0.1M 0.1M 0.1M 0.1)

;; floating point operations are *not* assiciative nor distributive
(def a  1.0e50)
(def b -1.0e50)
(def c 17.0e00)

;; not assiciative
(+ (+ a b) c)
(+ a (+ b c))

;; not distributive
(let [a (float 0.001)
      b (float 0.002)
      c (float 0.003)]
  [(* a (+ b c))
   (+ (* a b) (* a c))])


;; rationals are always perfectly accurate
(def a (rationalize 1.0e50))
(def b (rationalize -1.0e50))
(def c (rationalize 17.0e00))

(+ (+ a b) c)
(+ a (+ b c))

;; keyword vs. symbol

::bye
:haunted/house

(defn do-blowfish [directive]
  (case directive
    :aquarium/blowfish (println "feed the fish")
    :crypto/blowfish   (println "encode the message")
    :blowfish          (println "not sure what to do")))

(do-blowfish :aquarium/blowfish)
(do-blowfish :crypto/blowfish)
(do-blowfish :blowfish)

(ns crypto)
(ns aquarium)
(joy.ch4/do-blowfish ::blowfish)

(identical? :spoon :spoon)

(identical? 'spoon 'spoon)
(= 'spoon 'spoon)

;; symbol can hold metadata
(let [x (with-meta 'goat {:ornery true})
      y (with-meta 'goat {:ornery false})]
  [(= x y)
   (identical? x y)
   (meta x)
   (meta y)])

;; Clojure is a LISP-1

;; regex
#"some example"
(class #"some")
(class #"\d")

(seq (.split #"," "one,two,three"))
(re-seq #"\w+" "I am the king")
(re-seq #"\w*(\w)" "one-two/three")

;; beware of mutable matchers
