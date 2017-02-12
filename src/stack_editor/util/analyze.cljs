
(ns stack-editor.util.analyze
  (:require [clojure.string :as string] [stack-editor.util.detect :refer [contains-def?]]))

(defn locate-ns [short-form current-ns files]
  (if (nil? current-ns)
    nil
    (if (contains? files current-ns)
      (let [ns-data (get-in files [current-ns :ns])]
        (if (or (nil? ns-data) (<= (count ns-data) 2))
          nil
          (let [required (get ns-data 2)
                rules (subvec required 1)
                matched-rule (->> rules
                                  (filter
                                   (fn [rule]
                                     (println "rule" rule short-form)
                                     (and (= ":as" (get rule 2))
                                          (= short-form (get rule 3)))))
                                  (first))]
            (println "matched-rule" matched-rule)
            (if (some? matched-rule) (get matched-rule 1) nil))))
      nil)))

(defn locate-ns-by-var [short-form current-ns files]
  (println "Searching:" short-form current-ns files)
  (if (nil? current-ns)
    nil
    (if (contains? files current-ns)
      (let [ns-data (get-in files [:current-ns :ns])]
        (println "ns-data:" ns-data)
        (if (and (nil? ns-data) (<= (count ns-data) 2))
          nil
          (if (>= (count ns-data) 3)
            (let [required (get ns-data 2)
                  rules (subvec required 1)
                  matched-rule (->> rules
                                    (filter
                                     (fn [rule]
                                       (println
                                        "search rrule:"
                                        rule
                                        short-form
                                        (= ":refer" (get rule 1)))
                                       (and (= ":refer" (get rule 2))
                                            (some
                                             (fn [definition] (= definition short-form))
                                             (get rule 3)))))
                                    (first))]
              (println "rules" matched-rule)
              (if (some? matched-rule) (get matched-rule 1) nil))
            nil)))
      nil)))

(defn compute-ns [piece current-ns files]
  (if (string/includes? piece "/")
    (let [[that-ns that-value] (string/split piece "/")] (locate-ns that-ns current-ns files))
    (if (contains-def? files current-ns piece)
      current-ns
      (locate-ns-by-var piece current-ns files))))
