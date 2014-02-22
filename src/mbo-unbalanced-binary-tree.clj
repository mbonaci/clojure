(ns mbo-unbalanced-binary-tree)

(defn xseq [t]
  (when t
    (concat (xseq (:L t)) [(:val t)] (xseq (:R t)))))

(defn xconj [t v]
  (cond
    (nil? t) {:val v, :L nil, :R nil}
    (< v (:val t))
          {:val (:val t),
           :L (xconj (:L t) v),
           :R (:R t)}
    :else {:val (:val t)
           :L (:L t)
           :R (xconj (:R t) v)}
   ))

(def f1 (xconj nil 50))

(def f2 (xconj f1 40))

(def f3 (xconj f2 60))

(def f4 (xconj f3 10))

(def f5 (xconj f4 30))

(def f6 (xconj f5 20))

(def f7 (xconj f6 35))


(xseq f7)
