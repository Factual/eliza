(ns eliza.core
  (:use [eliza.register :only [*all-responders* register-responder]])
  (:require [clojure.string :as str]
            (eliza.responders simple
                              delegator)))

;; pairs of entry/response maps
(def *history* (atom []))

(def default-responder
  (let [question-responses
        ["I have no idea."
         "I wish I knew."
         "I haven't got a clue."
         ]
        statement-responses
        ["I don't know much about that."
         "I'm no expert on that."
         "I don't have much to say about that."
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
    (print "Eliza> ")
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
