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

(defn analyzers [m]
  (-> m
      tokenize
      is-question?))

(def timeout-period 200)

(defn query [input]
  (let [input-map (analyzers {:input input})
        sorted-responders (sort-by (comp - :confidence)
                                   (map #(% input-map)
                                        (vals @responders)))
        output-map (some #(deref (:response-ref %)
                                 timeout-period
                                 nil)
                       sorted-responders)]
    (add-to-history! [input-map output-map])
    (:output output-map)))

(defn chat-loop []
  (loop []
    (print "Eliza> ")
    (flush)
    (when-let [input (not-empty (read-line))]
      (println (query (str/trim input)))
      (recur))))
