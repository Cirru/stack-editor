
(ns stack-editor.util.analyze
  (:require [clojure.string :as string]))

(defn locate-ns [short-form this-namespace namespaces]
  (if (nil? this-namespace)
    nil
    (if (contains? namespaces this-namespace)
      (let [namespace-data (get namespaces this-namespace)]
        (if (nil? namespace-data)
          nil
          (let [required (get namespace-data 1)
                rules (subvec required 1)
                matched-rule (first
                               (filter
                                 (fn 
                                   [rule]
                                   (and
                                     (= ":as" (get rule 1))
                                     (= short-form (get rule 2))))
                                 rules))]
            (if (some? matched-rule) (first matched-rule) nil))))
      nil)))

(defn locate-ns-by-var [short-form this-namespace namespaces]
  (if (nil? this-namespace)
    nil
    (if (contains? namespaces this-namespace)
      (let [namespace-data (get namespaces this-namespace)]
        (if (nil? namespace-data)
          nil
          (let [required (get namespace-data 1)
                rules (subvec required 1)
                matched-rule (first
                               (filter
                                 (fn 
                                   [rule]
                                   (and
                                     (= ":refer" (get rule 1))
                                     (some
                                       (fn 
                                         [definition]
                                         (= definition short-form))
                                       (get rule 2))))
                                 rules))]
            (if (some? matched-rule) (first matched-rule) nil))))
      nil)))

(defn find-path [piece current-def namespaces definitions]
  (let [current-ns (first (string/split current-def "/"))
        this-namespace (get namespaces current-ns)]
    (if (string/includes? piece "/")
      (let [that-ns (first (string/split piece "/"))
            that-var (last (string/split piece "/"))
            maybe-namespace (locate-ns
                              that-ns
                              this-namespace
                              namespaces)]
        (if (some? maybe-namespace)
          (let [maybe-that-def (str maybe-namespace "/" that-var)]
            (if (contains? definitions maybe-that-def)
              {:ok true, :data maybe-that-def}
              (let [maybe-that-ns (locate-ns-by-var
                                    that-ns
                                    this-namespace
                                    namespaces)]
                (if (some? maybe-that-ns)
                  (str maybe-that-ns "/" that-var)
                  {:ok false, :data nil}))))
          {:ok false, :data nil}))
      (let [maybe-this-def (str current-ns "/" piece)]
        (if (contains? definitions maybe-this-def)
          {:ok true, :data maybe-this-def}
          {:ok false, :data nil})))))