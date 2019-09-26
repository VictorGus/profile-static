(ns profile-site.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.handler.dump :refer [handle-dump]]
            [compojure.route :as route]
            [compojure.core :refer :all]
            [garden.core :as gc]
            [hiccup.core :as hc]
            [profile-site.views :as psv]
            [profile-site.style :as pss]
            [clojure.java.io :as io]
            [clj-yaml.core :as yaml]))

(defn get-data [path]
  (slurp (io/resource path)))

(def patient-profile (yaml/parse-string (get-data "Patient.yaml")))

(defn patient-page [request]
  {:status 200
   :headers {"Content-type" "text/html"}
   :body (psv/layout (meta (psv/profile-page patient-profile)) (psv/profile-page patient-profile))})

(defn home-page [request]
  {:status 200
   :headers {"Content-type" "text/html"}
   :body (psv/layout (meta (psv/home-page)) (psv/home-page))})

(defroutes app
  (GET "/" [] #'home-page)
  (GET "/pat" [] #'patient-page)
  (route/resources "/assets/")
  (route/not-found "This page doesn't exist"))

(defn -main []
  (jetty/run-jetty (wrap-reload app) {:port 3000}))
