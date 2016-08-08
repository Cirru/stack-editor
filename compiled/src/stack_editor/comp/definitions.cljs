
(ns stack-editor.comp.definitions
  (:require [hsl.core :refer [hsl]]
            [clojure.string :as string]
            [respo.alias :refer (create-comp div input)]
            [respo.comp.space :refer [comp-space]]
            [respo.comp.text :refer [comp-text]]
            [respo.comp.debug :refer [comp-debug]]
            [respo-ui.style :as ui]
            [stack-editor.util.time :refer [now]]
            [stack-editor.comp.main-def :refer [comp-main-def]]
            [respo-border.transform.space :refer [interpose-spaces]]
            [stack-editor.style.widget :as widget]
            [cirru-editor.util.dom :refer [focus!]]))

(defn init-state [& args] "")

(defn update-state [state new-text] new-text)

(defn on-input [mutate!] (fn [e dispatch!] (mutate! (:value e))))

(defn on-add-definition [state mutate!]
  (fn [e dispatch!]
    (let [content state]
      (if (re-find (re-pattern "^.+/.+$") content)
        (do
          (dispatch! :collection/add-definition content)
          (mutate! ""))
        (dispatch!
          :notification/add-one
          [(now) (str "\"" content "\" is not valid!")])))))

(defn on-edit-definition [definition-path]
  (fn [e dispatch!]
    (dispatch! :collection/edit-definition definition-path)
    (focus!)))

(defn render [definitions]
  (fn [state mutate!]
    (div
      {:style (merge ui/flex ui/card {})}
      (div
        {}
        (input
          {:style (merge ui/input {:width "320px"}),
           :event {:input (on-input mutate!)},
           :attrs {:placeholder "namespace/definition", :value state}})
        (comp-space "8px" nil)
        (div
          {:style
           (merge ui/button {:line-height 2.2, :padding "0 8px"}),
           :event {:click (on-add-definition state mutate!)}}
          (comp-text "add definition"))
        (comp-space "40px" nil))
      (comp-space nil "16px")
      (interpose-spaces
        (div
          {}
          (->>
            definitions
            (map-indexed
              (fn [idx entry] [idx
                               (let 
                                 [[ns-part var-part]
                                  (string/split (first entry) "/")]
                                 (div
                                   {:style
                                    (merge
                                      widget/entry
                                      {:padding "4px 8px"}),
                                    :event
                                    {:click
                                     (on-edit-definition
                                       (first entry))}}
                                   (div
                                     {:style {:line-height 1.4}}
                                     (comp-text var-part nil))
                                   (div
                                     {:style
                                      {:line-height 1.4,
                                       :color (hsl 0 0 70),
                                       :font-size "11px"}}
                                     (comp-text ns-part nil))))]))))
        {:width "8px", :display "inline-block"}))))

(def comp-definitions
 (create-comp :definitions init-state update-state render))
