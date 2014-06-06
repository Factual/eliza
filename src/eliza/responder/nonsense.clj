(ns eliza.responder.nonsense
  (:require [clojure.string :as string]))

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
