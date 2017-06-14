
(ns app.comp.file
  (:require-macros (respo.macros :refer (defcomp)))
  (:require [clojure.string :as string]
            [respo.alias :refer [div input]]
            [respo.cursor :refer [with-cursor]]
            [respo.comp.text :refer [comp-text]]
            [respo.comp.space :refer [comp-space]]
            [respo.comp.debug :refer [comp-debug]]
            [respo-ui.style :as ui]
            [app.style.widget :as widget]
            [app.comp.define :refer [comp-define]]
            [cirru-editor.util.dom :refer [focus!]]))

(def style-file {:display :inline-block, :vertical-align :top, :width 240, :margin-top 16})

(def style-list {:max-height 240, :overflow "auto"})

(defn on-edit-definition [ns-name definition-path]
  (fn [e dispatch!]
    (dispatch! :collection/edit {:ns ns-name, :kind :defs, :extra definition-path, :focus []})
    (focus!)))

(defn by-var-part [code-entry]
  (let [path (first code-entry)] (last (string/split path "/"))))

(defn with-query [state]
  (fn [x] (if (string/blank? state) true (string/includes? (first x) state))))

(defcomp
 comp-file
 (states def-codes ns-name)
 (let [state (or (:data states) "")]
   (div
    {:style style-file}
    (comp-define state ns-name cursor)
    (div
     {:style style-list}
     (->> def-codes
          (filter (with-query state))
          (sort-by by-var-part)
          (map
           (fn [code-entry]
             (let [path (first code-entry)
                   var-part (last (string/split path (re-pattern "/")))]
               [var-part
                (div
                 {:style widget/var-entry,
                  :event {:click (on-edit-definition ns-name path)},
                  :attrs {:inner-text var-part}})]))))))))
