(ns eliza.responder.sleeper
  (:require [eliza.register :refer [register-responder!]]))

(def sleeping? (atom false))

(def tired-counter (atom 0))

(defn tired?
  []
  (swap! tired-counter inc)
  (and (> @tired-counter 10)
       (< (rand) 0.05)))

(defn go-to-sleep?
  [tokens]
  (or (= ["go" "to" "sleep"] (take 3 tokens))
      (tired?)))

(defn wake-up?
  [tokens]
  (= ["wake" "up"] (take 2 tokens)))

(defn wake-up!
  []
  (swap! tired-counter (constantly 0))
  (swap! sleeping? (constantly false)))

(defn sleeper-responder
  [{:keys [input tokens] :as input-map}]
  (cond
    (go-to-sleep? tokens) (swap! sleeping? (constantly true))
    (wake-up?     tokens) (wake-up!))
  (if @sleeping?
    {:output "ZZZZZZzzzZZZZZ..."}))

(register-responder! :sleeper
  (fn [{:keys [input tokens] :as input-map}]
    (cond (go-to-sleep? tokens)
            (do (swap! sleeping? (constantly true))
                0.99)
          (wake-up?     tokens)
            (do (wake-up!)
                0)
          :else 0))
  (fn [input-map] "ZZZZZZzzzZZZZZ..."))
