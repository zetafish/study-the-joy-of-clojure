(ns joy.ch1
  (:require [clojure.string :as str]))

;; 1.1. The Clojure Way

;; 1.2. Why another Lisp?

;; - Beauty
;; - Flexibility
;; - Code is data

;; 1.3. Functional Programming

;; pass functions around
(defn f [n] (str "hi " n))
(defn g [f1] [(f1 0) (f1 1)])
(g f)

;; 1.3.2 Implications

;; OO sees the world as nouns (classes)
;; FP sees the world as verbs (functions)

;; 1.4. Why Clojure isn't especially object oriented

;; 1.4.1. Terms

;; 1.4.3. Most of what OOP gives, Clojure provides

;; expression problem

;; polymorphism

(defprotocol Concatenable
  (cat [this other]))

(extend-type String
  Concatenable
  (cat [this other]
    (.concat this other)))

(cat "House" " of Leaves")

(extend-type java.util.List
  Concatenable
  (cat [this other]
    (concat this other)))

(cat [1 2] [3 4])

;; subtyping and interface-oriented programming

;; encapsulation

;; namespaces

(defn initial-board []
  [\r \n \b \q \k \b \n \r
   \p \p \p \p \p \p \p \p
   \- \- \- \- \- \- \- \-
   \- \- \- \- \- \- \- \-
   \- \- \- \- \- \- \- \-
   \- \- \- \- \- \- \- \-
   \P \P \P \P \P \P \P \P
   \R \N \B \Q \K \B \N \R])

(letfn [(index [file rank]
          (let [f (- (int file) (int \a))
                r (* 8 (- 8 (- (int rank) (int \0))))]
            (+ f r)))]
  (defn lookup [board pos]
    (let [[file rank] pos]
      (board (index file rank)))))

(lookup (initial-board) "e1")

(defn lookup2 [board pos]
  (let [[file rank] (map int pos)
        [fc rc]     (map int [\a \0])
        f (- file fc)
        r (* 8 (- 8 (- rank rc)))
        index (+ f r)]
    (board index)))

(lookup2 (initial-board) "e4")
