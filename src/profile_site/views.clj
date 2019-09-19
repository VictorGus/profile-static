(ns profile-site.views
  (:require [garden.core :as gc]
            [hiccup.core :as hc]))

(defn layout [title & content]
  (hc/html [:html
            [:head
             [:meta {:charset "utf-8"}]
             [:title title]]
            content]))


(layout "test" [:h1 "Hello"])
