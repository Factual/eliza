(ns eliza.middleware
  (:require [clojure.string :as s]))

(defn wrap-tokenizing [responder]
  (letfn [(tokenize [s] (re-seq #"(?:\w|')+" (s/lower-case s)))]
    (fn [input-hash]
      (let [{:keys [input]} input-hash
            parsed (tokenize input)]
        (responder (assoc input-hash
                     :tokens parsed))))))

(defn wrap-is-question?
  "set the :question? in the request based on ending in a question mark"
  [responder]
  (fn [request]
    (responder (assoc request :question? (when (.endsWith (:input request) "?") true)))))
