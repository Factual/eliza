(ns eliza.responder.delegator
  (:require [clojure.string :as str]))

(def delegator-responder
  (let [people ["Lance" "Alan" "Tim" "Evan" "Don"]]
    (fn [{[first second & more] :tokens}]
      (when (= ["what" "is"] [first second])
        {:output (str/join " "
                           (list* (rand-nth people)
                                  "probably knows more about"
                                  more))}))))