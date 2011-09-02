(ns eliza.responder.places
  (:require [funnyplaces.api :as places])
  (:require [clojure.contrib.pprint :as pprint])
  (:use [clojure.string :only (join)]))

;;(def FACTUAL-KEY "qwsBU2rRRrcSjv4kgqbAHjbAWP2OvY7BMbmisXoi")
;;(def FACTUAL-SECRET "fVFiie2DTlu2LyWRIseZcjlSiuGN06xD6h80DVjp")

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

;;(places/factual! FACTUAL-KEY FACTUAL-SECRET)
(places/factual! (obsr K) (obsr S))

(defn demo [term]
  (pprint/pprint (places/fetch :places :limit 3 :filters {:name {:$eq term}})))

(defn im-in [locality]
  (let [valid (first (places/fetch :places :limit 1 :filters {:locality locality}))]
    (if valid
      (do
        (def *locality* locality)    
        {:output (rand-nth [(str "How is the weather in " locality "?")
                            (str "How do you like " locality "?")
                            (str "How long have you been in " locality "?")])})
      {:output (str "I've never heard of " locality)})))

(defn where-is [term]
  (let [hits (places/fetch :places :limit 1 :q term :filters {:locality *locality* :name {:$search term}})
        hit (first hits)]
    (if (empty? hits)
      {:output "I don't know of such a place. Sorry."}
      {:output (str "Well, I know of '" (:name hit) "' at " (:address hit))})))

(defn places-responder [input-map]
  (let [[tok1 tok2 & more] (:tokens input-map)]
    (if (= ["where" "is"] [tok1 tok2])
      (where-is (apply str more))
      (if (= ["i'm" "in"] [tok1 tok2])
        (im-in (join " " more))))))