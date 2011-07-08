(ns eliza.responders.delegator
  (:use [eliza.core :only [wrap-tokenizing]])
  (:require [clojure.string :as s]))

(def delegator-responder
  (wrap-tokenizing
   (let [people ["Lance" "Alan" "Tim" "Evan" "Don"]]
     (fn [{[first second & more] :tokens}]
       (when (= ["what" "is"] [first second])
         {:response (s/join " "
                            (list* (rand-nth people)
                                   "probably knows more about"
                                   more))})))))