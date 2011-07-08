(ns eliza.responders.delegator
  (:use [eliza.register :only [register-responder]]
        [eliza.utils :only [wrap-tokenizing]])
  (:require [clojure.string :as s]))

(def delegator-responder
  (wrap-tokenizing
   (let [people ["Lance" "Alan" "Tim" "Evan" "Don"]]
     (fn [{[first second & more] :tokens :as input}]
       (when (= ["what" "is"] [first second])
         {:output (s/join " "
                          (list* (rand-nth people)
                                 "probably knows more about"
                                 more))})))))

(register-responder ::delegator delegator-responder)
