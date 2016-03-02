(ns autocomplete.db)

(def empty-set (sorted-set))
(def small-alphabet (sorted-set "to" "tea" "ted" "ten" "A" "in" "inn"))
(def countries (sorted-set "France" "Belgium" "Germany" "Spain"))
(def languages (sorted-set "Clojure" "Haskell" "Scheme" "Erlang"))

(def default-db
  {:lexicon-set small-alphabet
   :autocomplete-list []
   :prefix-string ""})
