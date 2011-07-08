(ns eliza.core
  (:use eliza.register)
  (:require [clojure.string :as str]
            eliza.responders.simple))

;; pairs of entry/response
(def *history* (atom []))

(letfn [(tokenize [s]
          (re-seq #"\w+" (str/lower-case s)))]
  (defn wrap-tokenizing [responder]
    (fn [input-hash]
      (let [{:keys [input]} input-hash
            parsed (tokenize input)]
        (responder (assoc input-hash
                     :tokens parsed))))))

(defn chat [input-map]
  (let [response-map (some #(% input-map) (vals @*all-responders*))]
    (swap! *history* conj [input-map response-map])
    response-map))

(defn chat-loop []
  (loop []
    (print "eliza> ")
    (when-let [input (not-empty (read-line))]
      (println (:output (chat {:input input})))
      (recur))))

(defn user-has-said? [s]
  (some #(= (first %) s) @*history*))

(defn eliza-has-said? [s]
  (some #(= (second %) s) @*history*))
