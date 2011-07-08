(ns eliza.responders.delegator
  (:use [eliza.register :only [register-responder]])
  (:require [clojure.string :as s]))

(letfn [(tokenize [s]
          (re-seq #"\w+" (s/lower-case s)))]
  (defn wrap-tokenizing [responder]
    (fn [input-hash]
      (let [{:keys [input]} input-hash
            parsed (tokenize input)]
        (responder (assoc input-hash
                     :tokens parsed))))))

(def delegator-responder
  (wrap-tokenizing
   (let [people ["Lance" "Alan" "Tim" "Evan" "Don"]]
     (fn [{[first second & more] :tokens :as input}]
       (println "Called with" input)
       (when (= ["what" "is"] [first second])
         {:output (s/join " "
                          (list* (rand-nth people)
                                 "probably knows more about"
                                 more))})))))

(register-responder ::delegator delegator-responder)
