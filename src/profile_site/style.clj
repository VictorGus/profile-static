(ns profile-site.style
  (:require [garden.core :as gc]))

(defn style [data]
  (gc/css data))

(def profile-style [[:.line-item
                     {:vertical-align "top"
                      :text-align "left"
                      :padding "0px 4px 0px 4px"
                      :background-color "white"}]

                    [:.line-item-resource-type
                     {:vertical-align "top"
                      :text-align "left"
                      :padding "0px 4px 0px 4px"}]

                    [:.line-inner-item
                     {:vertical-align "top"
                      :text-align "left"
                      :padding "0px 4px 0px 4px"}]

                    [:table
                     {:font-size "11px"
                      :font-family "verdana"
                      :max-width "100%"
                      :border-spacing "0px"
                      :border-collapse "collapse"}]

                    [:tr>td
                     {:border "0px"
                      :vertical-align "top"
                      :padding-bottom "10px"}]

                    [:td
                     {:line-height "1em"}]

                    [:tbody
                     {:display "table-row-group"}]

                    [:.flag-item
                     {:padding-left "3px"
                      :padding-right "3px"
                      :color "white"
                      :background-color "red"}]

                    [:th
                     {:vertical-align "top"
                      :text-align "left"
                      :background-color "white"
                      :border "0px #F0F0F0 solid"
                      :padding "0px 4px 0px 4px"}]

                    [:.table-icon
                     {:vertical-align "top"
                      :margin "0px 2px 0px 0px"
                      :background-color "white"}]])
