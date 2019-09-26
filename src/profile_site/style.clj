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
                      :background-color "white"}]]
                      
              ;;Menu styles
              
    				[:body
                     {:display "flex"
                      :flex-direction "column"
                      :font-family "'Roboto', sans-serif"
                      :margin "0"
                      :color "#212529"
                      :font-size "1rem"
                      :font-weight "400"
                      :line-height "1.5"
                      :text-align "left"
                      :background-color "#fff"
                      }]

                    [:div#menu :div#logo :img
                     {:height "30px"}]

                    [:div#menu
                     {:padding "10px 40px"}]

                    [:div#body
                     {:margin "30px 88px"}]

                    [:div#wrap
                     {:flex-direction "row"
                      :display "flex"
                      :box-sizing "border-box"}]

                    [:div#nav
                     {:margin "1px"
                      :padding "20px 30px"
                      :display "flex"
                      :min-width "298px"
                      :background "#F5F7F9"
                      :padding-left "12";;"calc((100% - 1448px) / 2)"
                      :width "calc((100% - 1448px) / 2 + 298px)"
                      :border-right "1px solid #e6ecf1"}]
                    ]                  
                      
                      
    )
