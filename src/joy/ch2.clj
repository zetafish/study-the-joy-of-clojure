(ns joy.ch2
  "Drinking from the Clojure firehose.")

;; scalars

(do 1)

(do 1.2)

(do 1/3)

(do 'something)

(do "hello")

(do :kw)

(do \a)

;; collections

'(yankee foxtrot hotel)
'(1 2 3)

[1 2 :a :b]

{1 :one 2 :two}

#{1 2 3 4 :apple :horse}

;; functions
(+ 1 2 3)

((fn mk-set [x y] #{x y})
 1 :canon)

;; multi arity
(fn
  ([x] #{x})
  ([x y] #{x y}))

((fn
   ([x] #{x})
   ([x y] #{x y})) 1)

((fn
   ([x] #{x})
   ([x y] #{x y})) 1 2)

;; more args
((fn [x y & more] [x y more]) 1 2)
((fn [x y & more] [x y more]) 1 2 3)
((fn [x y & more] [x y more]) 1 2 3 4)

;; named functions
(def make-set
  (fn
    ([x] #{x})
    ([x y] #{x y})))

(make-set 1)
(make-set 2 3)

(defn make-a-set
  ([x] #{x})
  ([x y] #{x y}))

(make-a-set 1)
(make-a-set 1 2)

;; in place functions with `#()`
(def make-a-list_  #(list %))
(def make-a-list1  #(list %1))
(def make-a-list2  #(list %1 %2))
(def make-a-list3  #(list %1 %2 %3))
(def make-a-list3+ #(list %1 %2 %3 %&))

(make-a-list2 1 2)
(make-a-list3+ 1 2 3 4 5 6)

;; vars
(def x 42)
(.start (Thread. #(println "Answer:" x)))

(def y)

;; blocks
(do
  6
  (+ 1 2)
  90)

;; locals
(let [pi 3.14
      r 5
      r-squared (* r r)]
  {:area (* pi r-squared)
   :circumference (* 2 pi r)})

;; loops
(loop [n 10]
  (when (pos? n)
    (println n)
    (recur (dec n))))

(loop [acc 0 n 10]
  (if (pos? n)
    (recur (+ acc n) (dec n))
    acc))

;; quoting
(cons 1 [2 3])
(quote (cons 1 (2 3)))
(cons 1 '(2 3))

(quote tena)
(def tena 9)

;; syntax quote
`(1 2 3)

;; syntax quote will auto-qualify symbols
`map
`Integer
`what-is-this

;; unquote
`(+ 10 (* 3 2))
`(+ 10 ~(* 3 2))

;; unquote splice
`(+ 88 ~@(range 10))
(let [x '(2 3)] `(1 ~@x))

;; auto-gen symbols
`potion#

;; java interop
java.util.Locale/JAPAN
(Math/sqrt 9)

;; creating a java class instance
(new java.util.HashMap {1 :x 2 "hello"})
(java.util.HashMap. {1 :y 2 "world"})

;; accessing a java instance member
(.x (java.awt.Point. 3 4))
(.divide (java.math.BigDecimal. "42") 2M)

;; setting java instance props
(let [origin (java.awt.Point. 0 0)]
  (set! (.x origin) 15)
  (str origin))

;; the .. macro
(.. (java.util.Date.)
    toString
    (endsWith "2018"))

;; the doto macro
(doto (java.util.HashMap.)
  (.put 1 :x)
  (.put 2 "hello"))

;; defining classes (reify and deftype)

;; try/catch
(throw (Exception. "X"))

(defn throw-catch [f]
  [(try
     (f)
     (catch ArithmeticException e "No dividing by zero")
     (catch Exception e (str "You are so bad " (.getMessage e)))
     (finally (println "returning...")))])

(throw-catch #(/ 1 0))
(throw-catch #(throw (Exception. "X")))

;; namespaces
(ns joy.endy)
