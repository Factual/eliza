(ns eliza.history)

;; pairs of entry/response maps
(def history-map (atom {}))

(defn add-to-history!
  [id data]
  (swap! history-map
    #(update-in % [id] (fn [hist] (conj (or hist []) data)))))

(defn history
  [id]
  (@history-map id []))
