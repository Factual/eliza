(ns eliza.utils
  (:require [clojure.string :as str]
            [eliza.history :refer [history]]))

(defn canon-str [s]
  (str/replace (str/upper-case s) #"[^A-Z]" ""))

(defn canon-match? [s1 s2]
  (= (canon-str s1) (canon-str s2)))

(defn user-has-said? [s]
  (some #(canon-match? s (first %)) @history))

(defn eliza-has-said? [s]
  (some #(canon-match? s (second %)) @history))
