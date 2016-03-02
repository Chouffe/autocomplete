(ns autocomplete.handlers
    (:require [re-frame.core :as re-frame]
              [trie.core :refer [trie]]
              [clojure.string :as s]
              [autocomplete.db :as db]))

(re-frame/register-handler :initialize-db (fn  [_ _] db/default-db))

(re-frame/register-handler
   :add-to-lexicon
   (fn [db [_ word]]
      (js/console.log "Logging=> " word)
      (js/console.log "Set" (str (:lexicon-set db)))
      (if (s/blank? word)
         db
         (update db :lexicon-set conj (s/lower-case word)))))

(re-frame/register-handler
   :save-prefix-string
   (fn [db [_ prefix-string]]
      (assoc db :prefix-string prefix-string)))

(re-frame/register-handler
   :load-lexicon
   (fn [db [_ lexicon-kw]]
      (assoc db :lexicon-set
             (case lexicon-kw
                :languages db/languages
                :countries db/countries
                :small-alphabet db/small-alphabet
                db/empty-set))))
