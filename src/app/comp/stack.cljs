
(ns app.comp.stack
  (:require [hsl.core :refer [hsl]]
            [respo.core :refer [defcomp div list-> <> span input]]
            [clojure.string :as string]
            [respo-ui.core :as ui]))

(defn on-click [pointer]
  (fn [e dispatch!]
    (let [event (:original-event e)
          command? (or (.-ctrlKey event) (.-metaKey event))
          shift? (.-shiftKey event)]
      (cond
        command? (dispatch! :stack/collapse pointer)
        shift? (do (.preventDefault event) (dispatch! :stack/shift pointer))
        :else (do (dispatch! :stack/point-to pointer))))))

(def style-bar
  {:padding "4px 8px",
   :cursor "pointer",
   :color (hsl 0 0 60),
   :font-family "Source Code Pro,Menlo,monospace",
   :font-size 13,
   :line-height 1.4,
   :white-space "nowrap"})

(def style-bright {:color (hsl 0 0 90)})

(def style-container
  {:overflow "auto", :padding "16px 0 160px 0", :user-select :nonworkspacee})

(def style-ns
  {:font-size "11px", :line-height 1.4, :color (hsl 0 0 50), :font-family "Hind"})

(def style-ns-main
  {:padding "0 8px",
   :line-height "36px",
   :cursor "pointer",
   :color (hsl 0 0 60),
   :font-family "Hind",
   :font-size "13px",
   :white-space "nowrap"})

(defcomp
 comp-stack
 (stack pointer)
 (list->
  {:style (merge ui/flex style-container)}
  (->> stack
       (map-indexed
        (fn [idx item]
          [idx
           (let [{ns-part :ns, kind :kind, extra-name :extra} item]
             (if (= kind :defs)
               (div
                {:style style-bar, :on-click (on-click idx)}
                (div {:style (if (= idx pointer) style-bright)} (<> span extra-name nil))
                (div {:style style-ns} (<> span ns-part nil)))
               (div
                {:style (merge style-ns-main (if (= idx pointer) style-bright)),
                 :on-click (on-click idx)}
                (<> span ns-part nil))))])))))
