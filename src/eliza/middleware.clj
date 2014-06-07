(ns eliza.middleware
  (:require [clojure.string :as s]))

(defn tokenize [m]
  (if-let [s (:input m)]
    (assoc m :tokens (re-seq #"(?:\w|')+" (s/lower-case s)))
    m))

(defn is-question?
  "set the :question? in the request based on ending in a question mark"
  [m]
  (assoc m :question? (when (.endsWith (:input m) "?") true)))
