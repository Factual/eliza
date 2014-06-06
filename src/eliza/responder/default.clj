(ns eliza.responder.default
  (:require [eliza.register :refer [register-responder!]]))

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

(register-responder! :default
  rand
  default-responder)