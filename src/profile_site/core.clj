(ns profile-site.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.handler.dump :refer [handle-dump]]
            [compojure.route :as route]
            [compojure.core :refer :all]
            [garden.core :as gc]
            [hiccup.core :as hc]
            [profile-site.views :as psv]
            [clojure.java.io :as io]
            [clj-yaml.core :as yaml]))

(defn get-data [path]
  (slurp (io/resource path)))

(def patient-profile (yaml/parse-string (get-data "Patient.yaml")))

(defn attr-required? [attr profile]
  (get-in profile (vec (cons :attrs (-> attr
                                          (concat [:isRequired])
                                          (vec))))))

(attr-required? [:name :attrs :family] patient-profile)

(defn get-cardinality [attr profile]
  (cond
    (or (= (last attr) :identifier) (= (last attr) :name) (= (last attr) :address)
        (= (last attr) :given))
    (if (attr-required? attr profile)
      "1..*"
      "0..*")
    (or (= (last attr) :gender) (= (last attr) :system) (= (last attr) :value)
        (= (last attr) :birthDate) (= (last attr) :use) (= (last attr) :kladr)
        (= (last attr) :district) (= (last attr) :city) (= (last attr) :line)
        (= (last attr) :active) (= (last attr) :family) (= (last attr) :telecom)
        (= (last attr) :seria))
    (if (attr-required? attr profile)
      "1..1"
      "0..1")
    :else "nil"))

(get-cardinality [:name :attrs :given] patient-profile)

(defn patient-page [request]
  {:status 200
   :headers {"Content-type" "text/html"}
   :body (psv/layout "Patient" (-> [:table
                                    [:tbody
                                     [:tr
                                      [:th "Имя"]
                                      [:th "Флаги"]
                                      [:th "Кард."]
                                      [:th "Тип"]
                                      [:th "Описание и ограничения"]]
                                     [:tr
                                      [:td (get patient-profile :resourceType)]
                                      [:td "?"]
                                      [:td "0..*"]]]]
                                   (concat (for [item (vec (keys (:attrs patient-profile)))] (-> [[:tr {:class "item"}
                                                                                                   [:td
                                                                                                    [:img {:src "/assets/tbl_vjoin.png"
                                                                                                           :style "vertical-align: top"}]
                                                                                                    item]
                                                                                                   [:td "?"]
                                                                                                   [:td (get-cardinality [item] patient-profile)]]]
                                                                                                 (concat (for [inner (->> item
                                                                                                                          (get (:attrs patient-profile))
                                                                                                                          (:attrs)
                                                                                                                          (keys)
                                                                                                                          (vec))]
                                                                                                          [:tr {:class "inner-item"}
                                                                                                           [:td
                                                                                                            [:img {:src "/assets/tbl_vline.png"
                                                                                                                   :style "vertical-align: top"}]
                                                                                                            [:img {:src "/assets/tbl_vjoin.png"
                                                                                                                   :style "vertical-align: top"}]
                                                                                                            inner]
                                                                                                           [:td "?"]
                                                                                                           [:td (get-cardinality [item :attrs inner] patient-profile)]])))))
                                   (vec)))})

(defroutes app
  (GET "/" [] #'patient-page)
  (route/resources "/assets/")
  (route/not-found "This page doesn't exist"))

(defn -main []
  (jetty/run-jetty (wrap-reload app) {:port 3000}))
