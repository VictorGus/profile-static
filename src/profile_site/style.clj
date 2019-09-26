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

                    [:a
                     {:text-decoration "none"}]

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
                      :background-color "white"}]

                    [:body
                     {:display "flex"
                      :flex-direction "column"
                      :font-family "'Roboto', sans-serif"
                      :margin "0"
                      :color "#212529"
                      :line-height "1.5"
                      :text-align "left"}]

                    [:.logo :.img-logo
                     {:height "30px"}]

                    [:.resource-list
                     {:padding-left "10px"
                      :margin-left "0px"}]

                    [:.terminology-list
                     {:padding-left "10px"
                      :margin-left "0px"}]

                    [:.menu
                     {:padding "10px 40px"
                      :list-style-position "inside"}]

                    [:.profile
                     {:margin "30px 88px"}]

                    [:.wrap
                     {:flex-direction "row"
                      :display "flex"
                      :box-sizing "border-box"}]

                    [:.nav
                     {:margin "1px"
                      :padding "20px 30px"
                      :display "flex"
                      :min-width "298px"
                      :background "#f1f1f1"
                      :padding-left "12";;"calc((100% - 1448px) / 2)"
                      :width "calc((100% - 1448px) / 2 + 298px)"
                      :border-right "1px solid #e6ecf1"}]])
