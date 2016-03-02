(ns autocomplete.views
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame]
            [cljs.reader :as r]
            [autocomplete.utils :refer [prefix?]]
            [autocomplete.api :as api]
            [trie.core :refer [trie]]))

(defn lexicon-input []
  [:div.lexicon-input-form
   [:form
    {:on-submit (fn [e] (let [input-field (.getElementById js/document "lexicon-input")]
                          (re-frame/dispatch [:add-to-lexicon (.-value input-field)]))
                  (.preventDefault e))}
    [:input.form-control {:type "text"
                          :id "lexicon-input"
                          :placeholder "Add to autocomplete list"}]]
   [:div.btn-group.load-group {:role "group"}
    [:a.btn.btn-default {:on-click (api/load-lexicon! :reset)}
     "Reset"]
    [:a.btn.btn-default {:on-click (api/load-lexicon! :small-alphabet)}
     "Small Alphabet"]
    [:a.btn.btn-default {:on-click (api/load-lexicon! :countries)}
     "Countries"]
    [:a.btn.btn-default {:on-click (api/load-lexicon! :languages)}
     "Languages"]]])

(declare trie-visualization)

(defn ^:private trie-li-class [depth value prefix-string key]
  (str "trie-color-" (mod depth 2) " "
       (if (and (prefix? value prefix-string) key)
         "trie-match"
         "trie-not-match")))

(defn trie-li [depth prefix-string {:keys [key children value] :as trie}]
  [:li.list-group-item {:class (trie-li-class depth value prefix-string key)}
   value
   (if-not children
     [:span]
     (into [:ul.list-group]
           (mapv (partial trie-visualization (inc depth) prefix-string)
                 (vals children))))])

(defn trie-visualization [depth prefix-string {:keys [key children value] :as trie}]
  (cond
    (= value "") (into [:ul.list-group.list-group-collapse]
                       (mapv (partial trie-visualization (inc depth)  prefix-string)
                             (vals children)))
    (not trie)   [:span]
    :else        (trie-li depth prefix-string trie)))

(defn autocomplete-input []
  (let [autocomplete-list (re-frame/subscribe [:autocomplete-list])
        prefix-string (re-frame/subscribe [:prefix-string])]
    (fn []
      [:div
       [:input.form-control
        {:type "text"
         :placeholder "Type something..."
         :value @prefix-string
         :on-change (fn [e]
                      (re-frame/dispatch
                        [:save-prefix-string (-> e .-target .-value)]))}]])))

(defn trie-component [autocompletor]
  (let [prefix-string (re-frame/subscribe [:prefix-string])]
    (fn []
      [:div.trie-component
       (->> @autocompletor
            str
            r/read-string
            (trie-visualization 0 @prefix-string))])))

(defn autocomplete-list [autocompletor]
  (let [prefix-string (re-frame/subscribe [:prefix-string])]
    (fn []
      (if-not (seq @prefix-string)
        [:div]
        [:div.autocomplete-list
         (into [:ul.list-group]
               (mapv (partial conj [:li.list-group-item]) (@autocompletor @prefix-string)))]))))

(defn demo []
  (let [lexicon-set (re-frame/subscribe [:lexicon-set])
        autocompletor (reaction (trie @lexicon-set))]
    [:div
     [:div.row
      [:div.col-xs-6
       [lexicon-input]]
      [:div.col-xs-6
       [autocomplete-input]]]
     [:div.row
      [:div.col-xs-6
       [trie-component autocompletor]]
      [:div.col-xs-6
       [autocomplete-list autocompletor]]]]))
