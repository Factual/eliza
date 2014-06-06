(ns eliza.history)

;; pairs of entry/response maps
(def history (atom []))

(defn add-to-history!
  [data]
  (swap! history conj data))
