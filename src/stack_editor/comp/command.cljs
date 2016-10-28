
(ns stack-editor.comp.command
  (:require [clojure.string :as string]
            [hsl.core :refer [hsl]]
            [respo.alias :refer [create-comp div]]
            [respo.comp.text :refer [comp-text]]))

(defn on-click [on-select] (fn [e dispatch!] (on-select dispatch!)))

(defn render [command selected? on-select]
  (fn [state mutate!]
    (div
     {:style (merge
              {:line-height 2.4,
               :cursor "pointer",
               :backgroud-color (hsl 0 0 0),
               :padding "0 8px",
               :font-family "Menlo,monospace"}
              (if selected? {:background-color (hsl 0 0 30)})),
      :event {:click (on-click on-select)}}
     (comp-text (string/join " " command) nil))))

(def comp-command (create-comp :command render))
