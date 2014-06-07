(ns eliza.responder.nonsense
  (:require [eliza.register :refer [register-responder!]]
            [clojure.string :as string]))

(def responses
  ["Now you're just talking nonsense!"
   "Have some sense, man!"])

(defn vowels
  [phrase]
  (string/replace phrase #"[^aeiouAEIOU]" ""))

(defn nonsense?
  [phrase]
  (zero? (count (vowels phrase))))

(defn nonsense-responder
  [{input :input :as input-map}]
  (if (nonsense? input)
    {:output (rand-nth responses)}))

(register-responder! :nonsense
  #(if (nonsense? (:input %)) 0.90 0.20)
  (fn [input-map] {:output (rand-nth responses)}))
