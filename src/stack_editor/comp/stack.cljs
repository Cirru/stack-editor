
(ns stack-editor.comp.stack
  (:require [hsl.core :refer [hsl]]
            [clojure.string :as string]
            [respo-ui.style :as ui]
            [cirru-editor.util.dom :refer [focus!]]
            [respo.alias :refer [create-comp div]]
            [respo.comp.text :refer [comp-text]]
            [respo.comp.space :refer [comp-space]]
            [respo.comp.debug :refer [comp-debug]]))

(defn on-click [pointer]
  (fn [e dispatch!]
    (let [event (:original-event e)
          command? (or (.-ctrlKey event) (.-metaKey event))]
      (if command?
        (dispatch! :stack/collapse pointer)
        (do (dispatch! :stack/point-to pointer) (focus!))))))

(defn render [stack pointer]
  (fn [state mutate!]
    (div
      {:style
       (merge ui/flex {:overflow "auto", :padding "40px 0 200px 0"})}
      (->>
        stack
        (map-indexed
          (fn [idx item] [idx
                          (if (string/includes? (last item) "/")
                            (let [[ns-part var-part]
                                  (string/split (last item) "/")]
                              (div
                                {:style
                                 {:color (hsl 0 0 60),
                                  :cursor "pointer",
                                  :padding "8px 16px",
                                  :font-family "Menlo,monospace"},
                                 :event {:click (on-click idx)}}
                                (div
                                  {:style
                                   (merge
                                     {:line-height 1.4}
                                     (if
                                       (= idx pointer)
                                       {:color (hsl 0 0 90)}))}
                                  (comp-text var-part nil))
                                (div
                                  {:style
                                   {:line-height 1.4,
                                    :color (hsl 0 0 50),
                                    :font-size "11px"}}
                                  (comp-text ns-part nil))))
                            (div
                              {:style
                               (merge
                                 {:line-height 3,
                                  :color (hsl 0 0 60),
                                  :cursor "pointer",
                                  :padding "0 16px",
                                  :font-family "Menlo,monospace"}
                                 (if
                                   (= idx pointer)
                                   {:color (hsl 0 0 90)})),
                               :event {:click (on-click idx)}}
                              (comp-text (last item) nil)))]))))))

(def comp-stack (create-comp :stack render))