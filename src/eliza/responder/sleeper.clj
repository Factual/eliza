(ns eliza.responder.sleeper
  (:require [eliza.register :refer [register-responder!]]
            [clojure.string :as string]))

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
  [{:keys [input tokens] :as input-map}]
  (or (= ["wake" "up"] (take 2 tokens))
      (.endsWith input "!!")
      (= input (string/upper-case input))))

(defn wake-up!
  []
  (swap! tired-counter (constantly 0))
  (swap! sleeping? (constantly false)))

(defn sleeper-responder
  [{:keys [input tokens] :as input-map}]
  (cond
    (go-to-sleep? tokens)    (swap! sleeping? (constantly true))
    (wake-up?     input-map) (wake-up!))
  (if @sleeping?
    {:output "ZZZZZZzzzZZZZZ..."}))

(register-responder! :sleeper
  (fn [{:keys [input tokens] :as input-map}]
    (cond (go-to-sleep? tokens)
            (do (swap! sleeping? (constantly true))
                0.99)
          (wake-up?     input-map)
            (do (wake-up!)
                0)
          @sleeping? 0.99
          :else 0))
  (fn [input-map] {:output "ZZZZZZzzzZZZZZ..."}))
