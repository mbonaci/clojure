(defn xors [max-x max-y]
  (for [x (range max-x) y (range max-y)] [x y (bit-xor x y)])

(xors 2 2)