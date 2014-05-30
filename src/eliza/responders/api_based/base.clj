(ns eliza.responders.api-based.base
  (:require [eliza.register :refer [register-responder]]))


;; some helpers
(defn get-input-string [input-object]
  (if (string? input-object)
    input-object
    (:input input-object)))

(defn make-dataset-responder [input-semparser api-caller api-response-renderer]
  (fn [input-object]
    (when-let [structured-request (input-semparser input-object)]
      (when-let [api-response (api-caller structured-request)]
        (api-response-renderer api-response)))))

(defn default-api-response-renderer
  "the real output should have output renderer"
  [resp]
  resp)