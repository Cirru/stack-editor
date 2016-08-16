
(ns stack-editor.comp.define
  (:require [hsl.core :refer [hsl]]
            [respo.alias :refer [create-comp div input]]
            [respo.comp.text :refer [comp-text]]
            [respo.comp.space :refer [comp-space]]
            [respo.comp.debug :refer [comp-debug]]
            [respo-ui.style :as ui]
            [stack-editor.style.widget :as widget]))

(defn init-state [& args] "")

(defn update-state [state text] text)

(defn on-input [mutate!] (fn [e dispatch!] (mutate! (:value e))))

(defn on-add [text mutate! ns-name]
  (fn [e dispatch!]
    (if (> (count text) 0)
      (do
        (mutate! "")
        (dispatch!
          :collection/add-definition
          (str ns-name "/" text))))))

(defn render [ns-name]
  (fn [state mutate!]
    (div
      {}
      (comp-text ns-name {:font-family "Menlo,monospace"})
      (comp-space "8px" nil)
      (input
        {:style (merge widget/input),
         :event {:input (on-input mutate!)},
         :attrs {:placeholder "...", :value state}})
      (comp-space "8px" nil)
      (div
        {:style widget/button,
         :event {:click (on-add state mutate! ns-name)}}
        (comp-text "add" nil)))))

(def comp-define (create-comp :define init-state update-state render))
