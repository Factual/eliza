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

(defn canon-str [s]
  (str/replace (str/upper-case s) #"[^A-Z]" ""))

(defn canon-match? [s1 s2]
  (= (canon-str s1) (canon-str s2)))

(defn user-has-said? [s]
  (some #(canon-match? s (first %)) @*history*))

(defn eliza-has-said? [s]
  (some #(canon-match? s (second %)) @*history*))

