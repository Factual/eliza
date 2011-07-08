(ns eliza.register)

(def *all-responders*
  (atom {}))

(defn register-responder [key responder]
  (swap! *all-responders* assoc key responder)) 
