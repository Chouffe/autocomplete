(ns autocomplete.api
  (:require [re-frame.core :as re-frame]))

(defn load-lexicon! [lexicon-kw]
  (fn [_]
    (re-frame/dispatch [:load-lexicon lexicon-kw])))

