(ns autocomplete.utils)

(defn prefix? [string prefix]
  (= 0 (.indexOf string prefix)))
