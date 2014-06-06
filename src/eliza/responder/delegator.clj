(ns eliza.responder.delegator
  (:require [eliza.register :refer [register-responder!]]
            [clojure.string :as str]))

(def people ["Will" "Aaron" "Avram" "Evan" "Daniel" "Guru"])

(defn delegator-responder
  [{[first second & more] :tokens}]
  (when (= ["what" "is"] [first second])
    {:output (str/join " "
                       (list* (rand-nth people)
                              "probably knows more about"
                              more))}))

(register-responder! :delegator
  (fn [{[first second & more] :tokens}]
    (if (= ["what" "is"] [first second])
      0.75
      0.25))
  (fn [{[first second & more] :tokens}]
    {:output (format "%s probably know more about %s"
                     (rand-nth people)
                     (or more "that"))}))
