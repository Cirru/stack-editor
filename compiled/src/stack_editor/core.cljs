
(ns stack-editor.core
  (:require [respo.core :refer [render! clear-cache!]]
            [stack-editor.schema :as schema]
            [stack-editor.comp.container :refer [comp-container]]
            [cljs.reader :refer [read-string]]
            [stack-editor.updater.core :refer [updater]]
            [stack-editor.util.time :refer [now]]
            [stack-editor.actions :refer [load-collection!]]))

(defonce store-ref (atom schema/store))

(defonce states-ref (atom {}))

(defn dispatch! [op op-data]
  (comment println "dispatch!" op op-data)
  (let [new-store (updater @store-ref op op-data (now))]
    (reset! store-ref new-store)))

(defn render-app! []
  (let [target (.querySelector js/document "#app")]
    (render! (comp-container @store-ref) target dispatch! states-ref)))

(defn -main []
  (enable-console-print!)
  (render-app!)
  (add-watch store-ref :changes render-app!)
  (add-watch states-ref :changes render-app!)
  (println "app started!")
  (load-collection! dispatch!)
  (let [configEl (.querySelector js/document "#config")
        config (read-string (.-innerHTML configEl))]
    (if (and (some? navigator.serviceWorker) (:build? config))
      (-> navigator.serviceWorker
       (.register "./sw.js")
       (.then
         (fn [registration]
           (println "resigtered:" registration.scope)))
       (.catch (fn [error] (println "failed:" error)))))))

(set! js/window.onload -main)

(defn on-jsload []
  (clear-cache!)
  (render-app!)
  (println "code updated."))
