
(ns stack-editor.util.detect (:require [clojure.string :as string]))

(defn fuzzy-search [pieces queries]
  (every? (fn [query] (some (fn [piece] (string/includes? piece query)) pieces)) queries))

(defn strip-atom [token]
  (-> token (string/replace (re-pattern "^@") "") (string/replace (re-pattern "/@") "/")))
