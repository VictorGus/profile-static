(ns profile-site.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.handler.dump :refer [handle-dump]]
            [compojure.route :as route]
            [compojure.core :refer :all]
            [garden.core :as gc]
            [hiccup.core :as hc]
            [profile-site.views :as psv]
            [clojure.java.io :as io]))


(defn home-page [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (psv/layout "Home" [:h1 "Home page"])})

(defn get-data [path]
  (slurp (io/resource path)))


(get-data "")
(defroutes app
  (GET "/" [] #'home-page)
  (route/resources "/assets/")
  (route/not-found "This page doesn't exist"))

(defn -main []
  (jetty/run-jetty (wrap-reload app) {:port 3000}))
