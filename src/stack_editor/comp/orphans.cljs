
(ns stack-editor.comp.orphans
  (:require [hsl.core :refer [hsl]]
            [respo.alias :refer [create-comp div]]))

(defn render [store] (fn [state mutate!] (div {})))

(def comp-orphans (create-comp :orphans render))