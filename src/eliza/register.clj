(ns eliza.register)

(def responders (atom {}))

(defn wrap-confidence-responder [confidence-fn response-fn]
  (fn [input-map]
    {:response-ref (future (response-fn input-map))
     :confidence   (confidence-fn input-map)}))

(defn register-responder!
  [k confidence-fn response-fn]
  (let [responder (wrap-confidence-responder confidence-fn response-fn)]
    (swap! responders #(assoc % k responder))))
