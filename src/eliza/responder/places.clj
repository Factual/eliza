(ns eliza.responder.places
  (:require [funnyplaces.api :as places])
  (:require [clojure.contrib.pprint :as pprint])
  (:use [clojure.string :only (join)]))

(declare *locality*)

(defn obs [c]
  (let [i (int c)]
    (cond
      (or (and (>= i (int \a)) (<= i (int \m)))
          (and (>= i (int \A)) (<= i (int \M))))
        (char (+ i 13))
      (or (and (>= i (int \n)) (<= i (int \z)))
          (and (>= i (int \N)) (<= i (int \Z))))
        (char (- i 13))
        :else c)))

(defn obsr [s]
  (apply str (map obs s)))

(def K "djfOH2eEEepFwi4xtdoNUwoNJC2BiL7OZozvfKbv")
(def S "sISvvr2QGyh2YlJEVfrMpwyFvhTA06kQ6u80QIwc")

(places/factual! (obsr K) (obsr S))

(defn im-in
  "Determines whether locality is known. If known, updates internal *locality* and
   replies with a locality related response. If not known, replies with a negative
   response."
  [locality]
  (let [valid (first (places/fetch :places :limit 1 :filters {:locality locality}))]
    (if valid
      (do
        (def *locality* locality)    
        {:output (rand-nth [(str "How is the weather in " locality "?")
                            (str "How do you like " locality "?")
                            (str "How long have you been in " locality "?")])})
      {:output (str "I've never heard of " locality)})))

(defn where-is
  "Tries to find a place that matches term, in current *locality*. If a place is
   found, responds with some information about it. Otherwise, replies with a
   negative response."
  [term]
  (let [hits (places/fetch :places :limit 1 :q term :filters {:locality *locality* :name {:$search term}})
        hit (first hits)]
    (if (empty? hits)
      {:output "I don't know of such a place. Sorry."}
      {:output (str "Well, I know of '" (:name hit) "' at " (:address hit))})))

(defn where-am []
  {:output (if *locality*
             (str "You said you're in " *locality*)
             ("I don't know, where are you?"))})

(defn places-responder
  "Attempts to handle locality messages like 'i'm in ...' and 'where is ...?'"
  [input-map]
  (let [[tok1 tok2 & more] (:tokens input-map)]
    (condp = [tok1 tok2]
        ["i'm" "in"] (im-in (join " " more))
        ["where" "is"] (where-is (apply str more))
        ["where" "am"] (where-am))))