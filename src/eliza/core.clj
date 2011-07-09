(ns eliza.core
  (:use eliza.register)
  (:require [clojure.string :as str]
            (eliza.responders simple
                              delegator)))

;; pairs of entry/response maps
(def *history* (atom []))

(def default-responder
  (let [question-responses
        ["Why do you ask?"
         "I have no idea."
         "I wish I knew."
         "I haven't got a clue."
         ]
        statement-responses
        ["How does that make you feel?"
         "What would your mother say about that?"
         "Are you sure?"
         ]]
    (fn [{input :input}]
      {:output (rand-nth (if (= \? (last input))
                           question-responses
                           statement-responses))})))

(defn chat [input-map]
  (let [output-map (or (some #(% input-map) (vals @*all-responders*))
                       (default-responder input-map))]
    (swap! *history* conj [input-map output-map])
    output-map))

(defn chat-loop []
  (loop []
    (print "eliza> ")
    (flush)
    (when-let [input (not-empty (read-line))]
      (println (:output (chat {:input (str/trim input)})))
      (recur))))

(defn canon-str [s]
  (str/replace (str/upper-case s) #"[^A-Z]" ""))

(defn canon-match? [s1 s2]
  (= (canon-str s1) (canon-str s2)))

(defn user-has-said? [s]
  (some #(canon-match? s (first %)) @*history*))

(defn eliza-has-said? [s]
  (some #(canon-match? s (second %)) @*history*))
