(ns profile-site.data
  (:require [profile-site.views :as psv]
            [clojure.java.io :as io]
            [hiccup.core :as hc]
            [clj-yaml.core :as yaml]
            [markdown-to-hiccup.core :as m]))

(defn get-data [path]
  (slurp (io/resource path)))

(def patient-profile (yaml/parse-string (get-data "Patient.yaml")))

(def organization-profile (yaml/parse-string (get-data "Organization.yaml")))

(defn md-summary->hc [markdown-text]
  (assoc-in (->> markdown-text
                 (m/md->hiccup)
                 (m/component)) [1 :class] "body-header"))

(defn into-file [path cnt type]
  (->> cnt
       hc/html
       (spit (io/file (str path type ".html")))))

(into-file "./resources/" (md-summary->hc (get-data "summary.md")) "summary")

(into-file "./resources/" (psv/profile patient-profile) "patient")
(into-file "./resources/" (psv/profile organization-profile) "organization")
