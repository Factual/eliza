(ns eliza.server
  (:use [ring.middleware params keyword-params resource]
        [compojure.core])
  (:require [compojure.route :as route]
            [eliza.core      :as eliza]))

(defn exception-str
  [e]
  (with-out-str (.printStackTrace e (java.io.PrintWriter. *out*))))

(defn say-something
  [{:keys [params] :as req}]
  (try
    {:status 200
     :body   (str (eliza/query (:input params)))}
    (catch Exception e
      {:status 500
       :body   (exception-str e)})))

(defroutes eliza-handlers
  (POST "/say" [] say-something)
  (route/not-found "No such thing."))

(def app
  (-> eliza-handlers
      (wrap-resource "public")
      wrap-keyword-params
      wrap-params))



