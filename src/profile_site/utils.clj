(ns profile-site.utils)

(defn vector-first-path [is? tree]
  (loop [[tree i path fk] [tree 0 [] nil]]
    (cond
      (>= i (count tree))
      (if (nil? fk) nil (recur fk))
      (vector? (tree i))
      (recur [(tree i) 0 (conj path i) [tree (inc i) path fk]])
      (is? (tree i))
      (conj path i)
      :else
      (recur [tree (inc i) path fk]))))
