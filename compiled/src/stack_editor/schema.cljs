
(ns stack-editor.schema)

(def store
 {:router {:name :analyzer, :data :definitions},
  :notifications [],
  :writer
  {:pointer 0,
   :clipboard [],
   :kind :definitions,
   :stack [],
   :focus []},
  :collection
  {:definitions {},
   :namespaces {},
   :main-definition nil,
   :procedures {}}})
