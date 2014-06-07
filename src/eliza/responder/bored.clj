(ns eliza.responder.bored
  (:require [eliza.register :refer [register-responder!]]
            [eliza.history  :refer [history]]))

(def bored-responses
  ["BOOOORING"
   "I feel like I've heard that before"
   "Now where have I heard that before?"])

(defn heard-it-before?
  [{:keys [input chat-session-id] :as input-map}]
  (some #(= input (:input (first %))) (history chat-session-id)))

(defn bored-responder
  [{:keys [input] :as input-map}]
  (and (heard-it-before? input)
       {:output (rand-nth bored-responses)}))

(register-responder! :bored
  #(if (heard-it-before? %) 0.80 0)
  (fn [input-map] {:output (rand-nth bored-responses)}))
