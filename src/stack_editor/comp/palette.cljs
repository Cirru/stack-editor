
(ns stack-editor.comp.palette
  (:require [clojure.string :as string]
            [hsl.core :refer [hsl]]
            [respo.alias :refer [create-comp div input]]
            [respo-ui.style :as ui]
            [respo.comp.text :refer [comp-text]]
            [cirru-editor.util.dom :refer [focus!]]
            [stack-editor.comp.command :refer [comp-command]]
            [stack-editor.util.keycode :as keycode]
            [stack-editor.actions :refer [submit-collection!]]
            [stack-editor.style.widget :as widget]
            [stack-editor.util.detect :refer [fuzzy-search]]))

(defn on-input [mutate!]
  (fn [e dispatch!] (mutate! {:cursor 0, :text (:value e)})))

(defn handle-command [cursor commands collection dispatch!]
  (let [command (get (into [] commands) cursor)]
    (dispatch! :router/toggle-palette nil)
    (case
      (first command)
      "load"
      (println "load")
      "save"
      (submit-collection! collection dispatch!)
      "definition"
      (do
        (dispatch! :collection/edit [:definitions (last command)])
        (focus!))
      "namespace"
      (do
        (dispatch! :collection/edit [:namespaces (last command)])
        (focus!))
      "procedure"
      (do
        (dispatch! :collection/edit [:procedures (last command)])
        (focus!))
      nil)))

(def update-state merge)

(defn on-select [cursor commands collection]
  (fn [dispatch!]
    (handle-command cursor commands collection dispatch!)))

(defn init-state [& args] {:cursor 0, :text ""})

(def basic-commands [["save"] ["load"]])

(defn on-keydown [mutate! commands cursor collection]
  (fn [e dispatch!]
    (let [code (:key-code e) total (count commands)]
      (cond
        (= code keycode/key-esc) (do
                                   (mutate! {:text ""})
                                   (dispatch!
                                     :router/toggle-palette
                                     nil)
                                   (focus!))
        (= code keycode/key-down) (if
                                    (< cursor (dec total))
                                    (mutate! {:cursor (inc cursor)}))
        (= code keycode/key-up) (if
                                  (> cursor 0)
                                  (mutate! {:cursor (dec cursor)}))
        (= code keycode/key-enter) (do
                                     (mutate! {:text ""})
                                     (handle-command
                                       cursor
                                       commands
                                       collection
                                       dispatch!))
        :else nil))))

(defn render [collection]
  (fn [state mutate!]
    (let [def-paths (->>
                      (keys (:definitions collection))
                      (map (fn [path] ["definition" path])))
          ns-names (->>
                     (keys (:namespaces collection))
                     (map (fn [ns-name] ["namespace" ns-name])))
          procedure-names (->>
                            (keys (:procedures collection))
                            (map
                              (fn [procedure-name] ["procedure"
                                                    procedure-name])))
          queries (string/split (:text state) " ")
          commands (->>
                     (concat
                       basic-commands
                       def-paths
                       ns-names
                       procedure-names)
                     (filter
                       (fn [command] (fuzzy-search command queries))))]
      (div
        {:style
         (merge
           ui/fullscreen
           ui/row
           {:background-color (hsl 200 40 10 0.8),
            :justify-content "center",
            :position "fixed"})}
        (div
          {:style
           (merge
             ui/column
             {:background-color (hsl 0 0 0 0.8), :width "800px"})}
          (input
            {:style
             (merge widget/input {:line-height "40px", :width "100%"}),
             :event
             {:keydown
              (on-keydown mutate! commands (:cursor state) collection),
              :input (on-input mutate!)},
             :attrs
             {:placeholder "write command...",
              :value (:text state),
              :id "command-palette"}})
          (div
            {:style ui/flex}
            (->>
              commands
              (map-indexed
                (fn [idx command] [idx
                                   (comp-command
                                     command
                                     (= idx (:cursor state))
                                     (on-select
                                       idx
                                       commands
                                       collection))])))))))))

(def comp-palette (create-comp :palette init-state update-state render))