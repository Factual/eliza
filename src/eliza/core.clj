(ns eliza.core
  (:require [clojure.string :as str]))

;; pairs of entry/response
(def *history* (atom []))

(def *simple-question-responses*
  ["Why do you ask?"
   "I have no idea."
   "I wish I knew."
   "I haven't got a clue."
   ])

(def *simple-default-responses*
  ["How does that make you feel?"
   "What would your mother say about that?"
   "Are you sure?"
   ])

(def *swap-me-you-pairs*
  [["me" "y%u"]
   ["I" "y%u"]
   ["am" "a%e"]
   ["you" "I"]
   ["are" "am"]
   ["y%u" "you"]
   ["a%e" "are"]])

(defn swap-me-you [line]
  (reduce #(apply str/replace %1 %2)
          line *swap-me-you-pairs*))

(defn reflect-input [line]
  (let [match (re-find #"(.*)([!.?])$" line)
        body (swap-me-you
              (str/trim
               (match 1)))
        response-word (if (= "?" (match 2)) "ask" "say")]
    (str "Why do you " response-word " '" body "'?")))

(defn simple-responder [map]
  (let [line (:input map)
        response (cond (and (re-find #"[?]$" line)
                   (< 0.5 (rand)))
              (rand-nth *simple-question-responses*)

              (and (re-find #"I|me|you" line)
                   (< 0.5 (rand)))
              (reflect-input line)
              
              :default
              (rand-nth *simple-default-responses*)
              )]
    {:output response})
  )

(def *all-responders*
  (atom {}))

(defn register-responder [key responder]
  (swap! *all-responders* assoc key responder)) 

(register-responder "simple" simple-responder)

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

