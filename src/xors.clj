(defn xors [xs ys] 
  (for [x (range xs) y (range ys)]
    [x y (rem (bit-xor x y) 256)]))


(defn f-values [f xs ys] 
  (for [x (range xs) y (range ys)]
    [x y (rem (f x y) 256)]))


(defn clear [g] (.clearRect g 0 0 200 200))


(defn draw-values [f xs ys]
  (clear gfx)
  (.setSize frame (java.awt.Dimension. xs ys))
  (doseq [[x y v] (f-values f xs ys)]
    (.setColor gfx (java.awt.Color. v v v))
    (.fillRect gfx x y 1 1)))


(for [method (seq (.getMethods java.awt.Frame))
    :let [method-name (.getName method)]
    :when (re-find #"." method-name)]
method-name)