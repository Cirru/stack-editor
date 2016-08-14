
(ns stack-editor.actions
  (:require [cljs.reader :refer [read-string]]
            [ajax.core :refer [GET POST json-request-format]]
            [stack-editor.util.querystring :refer [parse-query]]))

(def options
 (merge {"port" "7010"} (parse-query (.-search js/location))))

(defn load-collection! [dispatch!]
  (println (pr-str options))
  (GET
    (str "http://localhost:" (get options "port"))
    {:error-handler
     (fn [error]
       (println error)
       (dispatch! :notification/add-one "failed to load collection")),
     :handler
     (fn [response]
       (println "response...")
       (dispatch! :collection/load (read-string response)))}))

(defn submit-collection! [collection dispatch!]
  (POST
    (str "http://localhost:" (get options "port"))
    {:format (json-request-format),
     :error-handler
     (fn [error]
       (println error)
       (dispatch! :notification/add-one "failed to post collection")),
     :body (pr-str collection),
     :handler
     (fn [response]
       (println response)
       (dispatch! :notification/add-one "saved"))}))
