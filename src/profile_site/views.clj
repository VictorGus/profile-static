(ns profile-site.views
  (:require [garden.core :as gc]
            [hiccup.core :as hc]
            [profile-site.style :as pss]))

(defn layout [title & content]
  (hc/html [:html
            [:head
             [:meta {:charset "utf-8"}]
             [:title title]]
            [:body content]]))

(defn attr-required? [attr profile]
  (get-in profile (vec (cons :attrs (-> attr
                                        (concat [:isRequired])
                                        (vec))))))

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
    :else nil))

(defn get-type [attr profile]
  (get-in profile (vec (cons :attrs (-> attr
                                        (concat [:type])
                                        (vec))))))

(defn get-icon [attr profile]
  (cond
    (or (= (last attr) :address) (= (last attr) :identifier) (= (last attr) :name))
    "/assets/icon_datatype.gif"
    (-> profile
        (get-in attr)
        (get :attrs))
    "/assets/icon_element.gif"
    :else "/assets/icon_primitive.png"))

(defn set-last-item-img [items]
  (let [last-item (last items)
        outer-item (nth (last items) 1)]
    (-> items
        (assoc-in [(.indexOf items last-item)] (->> last-item
                                                    (filter #(if (= (get-in % [1 1 :class]) "line-inner-item") %))
                                                    (map #(assoc-in % [1 3 1 :src] "/assets/tbl_blank.png"))
                                                    (concat [:tbody outer-item])
                                                    (vec)))
        (assoc-in [(.indexOf items last-item) 1 1 3 1 :src] "/assets/tbl_vjoin_end.png")
        (assoc-in [(.indexOf items last-item) 1 1 1 :style] (when (> (count (rest last-item)) 1)
                                                              "background-image: url(/assets/tbl_bck010.png)")))))

(defn set-last-inner-item-img [items]
  (when (> (count items) 0)
    (assoc-in items [(.indexOf items (last items)) 1 4 1 :src] "/assets/tbl_vjoin_end.png")))

(defn profile-page [{resourceType :resourceType :as resource}]
  (layout resourceType [:style (pss/style pss/profile-style)]
              (-> [:table
                   [:tbody
                    [:tr {:style "border: 1px #F0F0F0 solid;
                                  font-size: 11px;
                                  font-family: verdana;
                                  vertical-align: top;"}
                     [:th "Имя"]
                     [:th "Флаги"]
                     [:th "Кард."]
                     [:th "Тип"]
                     [:th "Описание и ограничения"]]
                    [:tr
                     [:td {:class "line-item-resource-type"}
                      [:img {:src "/assets/icon_element.gif"
                             :style "vertical-align: top"}]
                      (get resource :resourceType)]
                     [:td {:class "line-item"} ""]
                     [:td {:class "line-item"
                           :style "opacity: 0.4"} "0..*"]]]]
                  (concat (set-last-item-img (vec (for [item (vec (keys (:attrs resource)))]
                                                    (vec (-> [:tbody [:tr
                                                                      [:td (assoc {:class "line-item"} :style (when (->> item
                                                                                                                         (get (:attrs resource))
                                                                                                                         (:attrs))
                                                                                                                "background-image: url(/assets/tbl_bck11.png)"))
                                                                       [:img {:src "/assets/tbl_spacer.png"
                                                                              :style "vertical-align: top;
                                                                                      background-color: white;"}]
                                                                       [:img {:src "/assets/tbl_vjoin.png"
                                                                              :style "vertical-align: top"}]
                                                                       [:img {:src (get-icon [:attrs item] resource)
                                                                              :class "table-icon"}]
                                                                       [:a item]]
                                                                      [:td {:class "line-item"}
                                                                       [:span {:class "flag-item"}
                                                                        "S"]]
                                                                      (let [card (get-cardinality [item] resource)]
                                                                        (if (or (= card "1..1") (= card "1..*"))
                                                                          [:td {:class "line-item"}
                                                                           card]
                                                                          [:td {:class "line-item"
                                                                                :style "opacity: 0.4"}
                                                                           card]))
                                                                      [:td {:class "line-item"
                                                                            :style "opacity: 0.4"}
                                                                       (get-type [item] resource)]
                                                                      [:td {:class "line-item"} [:a (-> resource
                                                                                                        (:attrs)
                                                                                                        (get item)
                                                                                                        (get :desc))]]]]
                                                             (concat (set-last-inner-item-img (vec (for [inner (->> item
                                                                                                                    (get (:attrs resource))
                                                                                                                    (:attrs)
                                                                                                                    (keys)
                                                                                                                    (vec))]
                                                                                                     [:tr
                                                                                                      [:td {:class "line-inner-item"}
                                                                                                       [:img {:src "/assets/tbl_spacer.png"
                                                                                                              :style "vertical-align: top"}]
                                                                                                       [:img {:src "/assets/tbl_vline.png"
                                                                                                              :style "vertical-align: top"}]
                                                                                                       [:img {:src "/assets/tbl_vjoin.png"
                                                                                                              :style "vertical-align: top"}]
                                                                                                       [:img {:src (get-icon [:attrs item inner] resource)
                                                                                                              :class "table-icon"}]
                                                                                                       inner]
                                                                                                      [:td {:class "line-item"}
                                                                                                       [:span {:class "flag-item"}
                                                                                                        "S"]]
                                                                                                      (let [card (get-cardinality [item :attrs inner] resource)]
                                                                                                        (if (or (= card "1..1") (= card "1..*"))
                                                                                                          [:td {:class "line-item"}
                                                                                                           card]
                                                                                                          [:td {:class "line-item"
                                                                                                                :style "opacity: 0.4"}
                                                                                                           card]))
                                                                                                      [:td {:class "line-item"
                                                                                                            :style "opacity: 0.4"}
                                                                                                       (get-type [item :attrs inner] resource)]
                                                                                                      [:td {:class "line-item"} [:a (-> resource
                                                                                                                                        (:attrs)
                                                                                                                                        (get item)
                                                                                                                                        (:attrs)
                                                                                                                                        (get inner)
                                                                                                                                        (get :desc))]]]))))))))))
                  (vec))))
