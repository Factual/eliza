(ns eliza.server
  (:use [ring.middleware params keyword-params resource session]
        [compojure.core])
  (:require [compojure.route :as route]
            [eliza.core      :as eliza]))

(defn exception-str
  [e]
  (with-out-str (.printStackTrace e (java.io.PrintWriter. *out*))))

(defn chat-session-id
  [req]
  (:chat-session-id (:session req)))

(defn say-something
  [{:keys [params] :as req}]
  (try
    {:status 200
     :body   (str (eliza/query (:input params) (chat-session-id req)))}
    (catch Exception e
      {:status 500
       :body   (exception-str e)})))

(defroutes eliza-handlers
  (POST "/say" [] say-something)
  (route/not-found "No such thing."))

(defn wrap-dir-index [handler]
  (fn [req]
    (handler
     (update-in req [:uri]
                #(if (= "/" %) "/chat.html" %)))))

(defn new-chat-session
  []
  (.toString (java.util.UUID/randomUUID)))

(defn wrap-make-session-if-none
  [handler]
  (fn [req]
    (let [response (handler req)]
      (if (:session/key req)
        response
        (assoc response :session {:chat-session-id (new-chat-session)})))))

(def app
  (-> eliza-handlers
      (wrap-resource "public")
      wrap-make-session-if-none
      wrap-session
      wrap-dir-index
      wrap-keyword-params
      wrap-params))
