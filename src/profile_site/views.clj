(ns profile-site.views
  (:require [garden.core :as gc]
            [hiccup.core :as hc]
            [profile-site.style :as pss]))

(defn layout [title & content]
  (hc/html [:html
            [:head
             [:meta {:charset "utf-8"}]
             [:title title]]
            content]))

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

(defn get-icon [attr profile]
  (cond
    (or (= (last attr) :address) (= (last attr) :identifier))
    "/assets/icon_datatype.gif"
    (-> profile
        (get-in attr)
        (get :attrs))
    "/assets/icon_element.gif"
    :else "/assets/icon_primitive.png"))

(defn set-last-item-img [items]
  (for [item items] (println item))
  (assoc-in items [(.indexOf items (last items)) 1 1 2 1 :src] "/assets/tbl_vjoin_end.png"))

(defn set-last-inner-item-img [items]
  (when (> (count items) 0)
    (assoc-in items [(.indexOf items (last items)) 1 3 1 :src] "/assets/tbl_vjoin_end.png")))

(defn profile-page [{resourceType :resourceType :as resource}]
  (layout resourceType [:style (pss/style pss/profile-style)]
              (-> [:table
                   [:tbody
                    [:tr
                     [:th "Имя"]
                     [:th "Флаги"]
                     [:th "Кард."]
                     [:th "Тип"]
                     [:th "Описание и ограничения"]]
                    [:tr
                     [:td {:class "line-item"}
                      [:img {:src "/assets/icon_element.gif"
                             :style "vertical-align: top"}]
                      (get resource :resourceType)]
                     [:td {:class "line-item"} "?"]
                     [:td {:class "line-item"} "0..*"]]]]
                  (concat (set-last-item-img (vec (for [item (vec (keys (:attrs resource)))]  (vec (-> [:tbody [:tr
                                                                                                               [:td {:class "line-item"}
                                                                                                                [:img {:src "/assets/tbl_vjoin.png"
                                                                                                                       :style "vertical-align: top"}]
                                                                                                                [:img {:src (get-icon [:attrs item] resource)
                                                                                                                       :class "table-icon"}]
                                                                                                                [:a item]]
                                                                                                                [:td {:class "line-item"}
                                                                                                                 "?"]
                                                                                                                [:td {:class "line-item"}
                                                                                                                 (get-cardinality [item] resource)]
                                                                                                                [:td {:class "line-item"}
                                                                                                                 "?"]
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
                                                                                                                                               [:td {:class "line-item"}
                                                                                                                                                [:img {:src "/assets/tbl_vline.png"
                                                                                                                                                       :style "vertical-align: top"}]
                                                                                                                                                [:img {:src "/assets/tbl_vjoin.png"
                                                                                                                                                       :style "vertical-align: top"}]
                                                                                                                                                [:img {:src (get-icon [:attrs item inner] resource)
                                                                                                                                                       :class "table-icon"}]
                                                                                                                                                inner]
                                                                                                                                               [:td {:class "line-item"}
                                                                                                                                                "?"]
                                                                                                                                               [:td {:class "line-item"}
                                                                                                                                                (get-cardinality [item :attrs inner] resource)]
                                                                                                                                               [:td {:class "line-item"}
                                                                                                                                                "?"]
                                                                                                                                               [:td {:class "line-item"} [:a (-> resource
                                                                                                                                                                                 (:attrs)
                                                                                                                                                                                 (get item)
                                                                                                                                                                                 (:attrs)
                                                                                                                                                                                 (get inner)
                                                                                                                                                                                 (get :desc))]]]))))))))))
                  (vec))))
