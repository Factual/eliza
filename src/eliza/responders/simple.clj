(ns eliza.responders.simple
  (:use eliza.register)
  (:require [clojure.string :as str]))

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
  (let [match (re-find #"(.*)([!.?])?$" line)
        body (swap-me-you
              (str/trim
               (match 1)))
        response-word (if (= "?" (match 2)) "ask" "say")]
    (str "Why do you " response-word " '" body "'?")))

(defn simple-responder [{input :input}]
  (when (and (re-find #"I|me|you" input)
             (< 0.5 (rand)))
    {:output (reflect-input input)}))

(register-responder ::simple simple-responder)
