(ns eliza.core
  (:use eliza.middleware
        [eliza.responder reflector delegator])
  (:require [eliza.responder.nonsense :refer [nonsense-responder]]
            [eliza.responder.sleeper  :refer [sleeper-responder]]
            [eliza.history            :refer [add-to-history!]]
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
    (add-to-history! [input-map output-map])
    (:output output-map)))

(defn chat-loop []
  (loop []
    (print "Eliza> ")
    (flush)
    (when-let [input (not-empty (read-line))]
      (println (query (str/trim input)))
      (recur))))
