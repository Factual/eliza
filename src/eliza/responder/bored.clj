(ns eliza.responder.bored
  (:require [eliza.history :refer [history]]))

(def bored-responses
  ["BOOOORING"
   "I feel like I've heard that before"
   "Now where have I heard that before?"])

(defn heard-it-before?
  [input]
  (some #(= input (:input (first %))) @history))

(defn bored-responder
  [{:keys [input] :as input-map}]
  (and (heard-it-before? input)
       {:output (rand-nth bored-responses)}))
