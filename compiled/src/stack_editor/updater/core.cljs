
(ns stack-editor.updater.core
  (:require [stack-editor.updater.router :as router]
            [stack-editor.updater.collection :as collection]
            [stack-editor.updater.notification :as notification]
            [stack-editor.updater.stack :as stack]))

(defn default-handler [store op-data] store)

(defn updater [store op op-data op-id]
  (let [handler (case
                  op
                  :router/route
                  router/route
                  :collection/add-definition
                  collection/add-definition
                  :collection/add-procedure
                  collection/add-procedure
                  :collection/add-namespace
                  collection/add-namespace
                  :collection/set-main
                  collection/set-main
                  :collection/edit-definition
                  collection/edit-definition
                  :collection/write
                  collection/write-code
                  :collection/edit-procedure
                  collection/edit-procedure
                  :collection/edit-namespace
                  collection/edit-namespace
                  :collection/load
                  collection/load-remote
                  :notification/add-one
                  notification/add-one
                  :notification/remove-one
                  notification/remove-one
                  :stack/goto-definition
                  stack/goto-definition
                  :stack/go-back
                  stack/go-back
                  :stack/point-to
                  stack/point-to
                  default-handler)]
    (handler store op-data op-id)))
