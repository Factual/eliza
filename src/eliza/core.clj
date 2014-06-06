(ns eliza.core
  (:require [eliza.register :refer [responders]]
            [eliza.responder.nonsense]
            [eliza.responder.sleeper]
            [eliza.responder.bored]
            [eliza.responder.reflector]
            [eliza.responder.delegator]
            [eliza.history    :refer [add-to-history!]]
            [eliza.middleware :refer [tokenize is-question?]]
            [clojure.string :as str]))

(defn default-responder [input-map]
  (let [question-responses  ["I have no idea."
                             "I wish I knew."
                             "I haven't got a clue."]
        statement-responses ["I don't know much about that."
                             "I'm no expert on that."
                             "I don't have much to say about that."]]
    {:output (rand-nth (if (:question? input-map)
                         question-responses
                         statement-responses))}))

(defn wrap-responder [default-responder other-responder]
  (fn [input-map]
    (if-let [responder-response (other-responder input-map)]
      responder-response
      (default-responder input-map))))

(defn wrap-confidence-responder [confidence-fn response-fn]
  (fn [input-map]
    {:confidence (confidence-fn input-map)
     :response-ref (future (response-fn input-map))}))

(defn analyzers [m]
  (-> m
      tokenize
      is-question?))

(def timeout-period 200)

(defn query [input]
  (let [input-map (analyzers {:input input})
        sorted-responders (sort-by :confidence 
                                   (map #(% input-map)
                                        (vals @responders)))
        output-map
        (first (filter #(deref (:response-ref %)
                               timeout-period
                               nil)
                       sorted-responders))]
    (add-to-history! [input-map output-map])
    (:output output-map)))

(defn chat-loop []
  (loop []
    (print "Eliza> ")
    (flush)
    (when-let [input (not-empty (read-line))]
      (println (query (str/trim input)))
      (recur))))
