
(ns stack-editor.comp.container
  (:require [hsl.core :refer [hsl]]
            [respo.alias :refer [create-comp div span]]
            [respo-ui.style :as ui]
            [stack-editor.comp.loading :refer [comp-loading]]))

(defn render [store]
  (fn [state mutate!] (div {:style (merge ui/global)} (comp-loading))))

(def comp-container (create-comp :container render))
