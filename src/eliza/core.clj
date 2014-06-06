(ns eliza.core
  (:use eliza.middleware
        [eliza.responder reflector delegator sleeper])
  (:require [eliza.responder.nonsense :refer [nonsense-responder]]
            [clojure.string :as str]))


;; pairs of entry/response maps
(def history (atom []))

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

(def app
  (-> default-responder
      (wrap-responder reflector-responder)
      (wrap-responder delegator-responder)
      (wrap-responder nonsense-responder)
      (wrap-responder sleeper-responder)
      wrap-tokenizing
      wrap-is-question?))

(defn query [input]
  (let [input-map {:input input}
        output-map (app input-map)]
    (swap! history conj [input-map output-map])
    (:output output-map)))

(defn chat-loop []
  (loop []
    (print "Eliza> ")
    (flush)
    (when-let [input (not-empty (read-line))]
      (println (query (str/trim input)))
      (recur))))
