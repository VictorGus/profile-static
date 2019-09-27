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

(def organization-profile (yaml/parse-string (get-data "Organization.yaml")))

(defn patient-page [request]
  {:status 200
   :headers {"Content-type" "text/html"}
   :body (psv/profile-page->html patient-profile)})

(defn organization-page [request]
  {:status 200
   :headers {"Content-type" "text/html"}
   :body (psv/profile-page->html organization-profile)})

(defn home-page [request]
  {:status 200
   :headers {"Content-type" "text/html"}
   :body (let [hm-page (psv/home-page)]
           (psv/layout (meta hm-page) hm-page))})

(defroutes app
  (GET "/" [] #'home-page)
  (GET "/profiles/Patient" [] #'patient-page)
  (GET "/profiles/Organization" [] #'organization-page)
  (route/resources "/assets/")
  (route/not-found "This page doesn't exist"))

(defn -main []
  (jetty/run-jetty (wrap-reload app) {:port 3000}))
