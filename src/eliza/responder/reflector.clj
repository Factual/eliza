(ns eliza.responder.reflector
  (:require [eliza.register :refer [register-responder!]]
            [clojure.string :as str]))

(def reflect-input
  (let [swap-me-you {"me" "you"
                     "i" "you"
                     "i'm" "you're"
                     "am" "are"
                     "you" "I"
                     "you're" "I'm"
                     "are" "am"}]
    (fn [{tokens :tokens input :input}]
      (let [swapped-tokens (map #(or (swap-me-you %) %)
                                tokens)
            body (str/replace (str/join " " swapped-tokens)
                              #"I$" "me")
            response-word (if (= \? (last input)) "ask" "say")]
        (str "Why do you " response-word " " body "?")))))

(defn reflector-responder [{input :input :as input-map}]
  (when (and (re-find #"I|[mM]e|[yY]ou" input))
    {:output (reflect-input input-map)}))

(register-responder! :reflector
  (fn [{input :input :as input-map}]
    (if (re-find #"I|[mM]e|[yY]ou" input) 0.80 0))
  (fn [input-map] {:output (reflect-input input-map)}))
