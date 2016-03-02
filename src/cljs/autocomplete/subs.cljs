(ns autocomplete.subs
   (:require-macros [reagent.ratom :refer [reaction]])
   (:require [re-frame.core :as re-frame]))

(re-frame/register-sub
   :name
   (fn [db]
      (reaction (:name @db))))

(re-frame/register-sub
   :autocomplete-list
   (fn [db]
      (reaction (:autocomplete-list @db))))

(re-frame/register-sub
   :lexicon-set
   (fn [db]
      (reaction (:lexicon-set @db))))

(re-frame/register-sub
   :prefix-string
   (fn [db]
      (reaction (:prefix-string @db))))
