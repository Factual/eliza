(ns eliza.responders.simple
  (:use [eliza.register :only [register-responder]]
        [eliza.utils :only [wrap-tokenizing]])
  (:require [clojure.string :as str]))

(def reflect-input
  (let [swap-me-you {"me" "you"
                     "i" "you"
                     "i'm" "you're"
                     "am" "are"
                     "you" "I"
                     "you're" "I'm"
                     "are" "am"}]
    (wrap-tokenizing
     (fn [{tokens :tokens input :input}]
       (let [swapped-tokens (map #(or (swap-me-you %) %)
                                 tokens)
             body (str/replace (str/join " " swapped-tokens)
                               #"I$" "me")
             response-word (if (= \? (last input)) "ask" "say")]
         (str "Why do you " response-word " " body "?"))
       ))))

(defn simple-responder [{input :input :as input-map}]
  (when (and (re-find #"I|[mM]e|[yY]ou" input)
             (< 0.5 (rand)))
    {:output (reflect-input input-map)}))

(register-responder ::simple simple-responder)
