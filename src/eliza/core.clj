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

(defn simple-responder [line]
  (cond (and (re-find #"[?]$" line)
             (< 0.5 (rand)))
        (rand-nth *simple-question-responses*)

        (and (re-find #"I|me|you" line)
             (< 0.5 (rand)))
        (reflect-input line)

        :default
        (rand-nth *simple-default-responses*)
        )
  )

(letfn [(tokenize [s]
          (re-seq #"\w+" (str/lower-case s)))]
  (defn wrap-tokenizing [responder]
    (fn [input-hash]
      (let [{:keys [input]} input-hash
            parsed (tokenize input)]
        (responder (assoc input-hash
                     :tokens parsed))))))

(def *all-responders*
  [simple-responder])

(defn chat [line]
  (let [responder (rand-nth *all-responders*)
        response (responder line)]
    (swap! *history* conj [line response])
    response))

(defn chat-loop []
  (loop []
    (print "eliza> ")
    (when-let [input (not-empty (read-line))]
      (println (chat input))
      (recur))))

(defn user-has-said? [s]
  (some #(= (first %) s) @*history*))

(defn eliza-has-said? [s]
  (some #(= (second %) s) @*history*))
