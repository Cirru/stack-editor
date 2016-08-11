
(ns stack-editor.updater.router)

(defn route [store op-data]
  (let [router op-data] (assoc store :router router)))

(defn toggle-palette [store op-data op-id]
  (update-in store [:router :show-palette?] not))
