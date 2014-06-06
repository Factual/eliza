(ns eliza.responder.delegator
  (:require [eliza.register :refer [register-responder!]]
            [clojure.string :as str]))

(def delegator-responder
  (let [people ["Will" "Aaron" "Avram" "Evan" "Daniel" "Guru"]]
    (fn [{[first second & more] :tokens}]
      (when (= ["what" "is"] [first second])
        {:output (str/join " "
                           (list* (rand-nth people)
                                  "probably knows more about"
                                  more))}))))

(register-responder! :delegator
  (constantly 0)
  (constantly nil))
