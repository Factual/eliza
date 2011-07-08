(ns eliza.utils
  (:require [clojure.string :as s]))

(defn tokenize [s]
  (re-seq #"\w+" (s/lower-case s)))

(defn wrap-tokenizing [responder]
  (fn [input-hash]
    (let [{:keys [input]} input-hash
          parsed (tokenize input)]
      (responder (assoc input-hash
                   :tokens parsed)))))