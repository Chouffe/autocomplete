(ns autocomplete.core
    (:require [reagent.core :as reagent]
              [trie.core :as trie]
              [re-frame.core :as re-frame]
              [autocomplete.handlers]
              [autocomplete.subs]
              [autocomplete.views :as views]
              [autocomplete.config :as config]))

(when config/debug?
  (println "dev mode"))

(defn mount-root []
  (reagent/render [views/demo]
                  (.getElementById js/document "autocomplete-app")))

(defn ^:export init []
  (re-frame/dispatch-sync [:initialize-db])
  (mount-root))
