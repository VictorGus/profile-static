(ns profile-site.views
  (:require [garden.core :as gc]
            [hiccup.core :as hc]
            [profile-site.style :as pss]
            [profile-site.utils :refer :all]))

(defn layout [title & content]
  (hc/html [:html
            [:head
             [:style (pss/style (pss/set-page-style pss/profile-style pss/navigation-menu-style))]
             [:meta {:charset "utf-8"}]
             [:title title]]
            [:body content]]))

(defn get-cardinality [attr]
  (cond
    (or (= (keyword (*get attr :attr)) :identifier) (= (keyword (*get attr :attr)) :name) (= (keyword (*get attr :attr)) :address)
        (= (keyword (*get attr :attr)) :given))
    (if (*get attr :isRequired)
      "1..*"
      "0..*")
    (or (= (keyword (*get attr :attr)) :gender) (= (keyword (*get attr :attr)) :system) (= (keyword (*get attr :attr)) :value)
        (= (keyword (*get attr :attr)) :birthDate) (= (keyword (*get attr :attr)) :use) (= (keyword (*get attr :attr)) :kladr)
        (= (keyword (*get attr :attr)) :district) (= (keyword (*get attr :attr)) :city) (= (keyword (*get attr :attr)) :line)
        (= (keyword (*get attr :attr)) :active) (= (keyword (*get attr :attr)) :family) (= (keyword (*get attr :attr)) :telecom)
        (= (keyword (*get attr :attr)) :seria))
    (if (*get attr :isRequired)
      "1..1"
      "0..1")
    :else nil))

(defn get-type [attr profile]
  (get-in profile (vec (cons :attrs (-> attr
                                        (concat [:type])
                                        (vec))))))

(defn get-icon [attr]
  (cond
    (or (= (keyword (*get-in attr [0 :attr])) :address) (= (keyword (*get-in attr [0 :attr])) :identifier) (= (keyword (*get-in attr [0 :attr])) :name))
    "/assets/icon_datatype.gif"
    (> (count (*get attr 1)) 0)
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
        (assoc-in (conj ((comp vec cons) (.indexOf items last-item) (vector-first-path #(= % {:src "/assets/tbl_vjoin.png",
                                                                                               :style "vertical-align: top; background-color: white;"}) last-item)) :src)
                   "/assets/tbl_vjoin_end.png")
        (assoc-in [(.indexOf items last-item) 1 1 1 :style] (when (> (count (rest last-item)) 1)
                                                              "background-image: url(/assets/tbl_bck010.png)")))))

(defn set-last-inner-item-img [items]
  (when (> (count items) 0)
    (-> items
        (assoc-in [(.indexOf items (last items)) 1 4 1 :src] "/assets/tbl_vjoin_end.png")
        (assoc-in [(.indexOf items (last items)) 1 1 :style] "background-color: white; background-image: url(/assets/tbl_bck100.png)"))))

(def menu
  [:div
   [:div {:class "menu"}
    [:div {:class "logo"}
     [:img {:class "img-logo"
            :src "http://www.hl7.org/fhir/us/core/assets/images/fhir-logo-www.png"}]]]
   [:div {:class "wrap"}
    [:div {:class "nav"}
     [:ul {:class "ulmenu"}
      [:li
       [:a {:id "list-item"
            :href "/index.html"} "Главная"]]
      [:li
       [:a {:id "list-item"
            :href "/profiles/index.html"} "Профили Ресурсов"]
       [:ul {:class "resource-list"}
        [:li
         [:a {:id "list-item"
              :href "/profiles/Patient"} "Пациент (Patient)"]]
        [:li
         [:a {:id "list-item"
              :href "/profiles/Organization"} "Organization"]]
        [:li
         [:a {:id "list-item"
              :href "/profiles/Practitioner"} "Practitioner"]]]]
      [:li
       [:a {:id "list-item"
            :href "/valuesets"} "Терминологии"]
       [:ul {:class "terminology-list"}
        [:li
         [:a {:id "list-item"
              :href "/valuesets/Patient-identifiers"} "Идентификаторы Пациента"]]]]]]
    [:div {:class "profile"}]]])

(defn home-page []
  (let [hm [:h1 "Home page"]]
    (with-meta (assoc-in menu (vector-first-path #(= {:class "profile"} %) menu) hm) {:title "Home"})))

(defn home-page->html []
  (let [page-title (meta (home-page))
        hm-page (home-page)]
    (layout (:title page-title) hm-page)))

(defn outter-attrs-into-hc [profile]
  (set-last-item-img (vec (map (fn [itm]
                                [:tbody [:tr
                                         [:td (assoc {:class "line-item"} :style (when (> (count itm) 2)
                                                                                   "background-image: url(/assets/tbl_bck11.png)"))
                                          [:img {:src "/assets/tbl_spacer.png"
                                                 :style "vertical-align: top; background-color: white;"}]
                                          [:img {:src "/assets/tbl_vjoin.png"
                                                 :style "vertical-align: top; background-color: white;"}]
                                          [:img {:src (get-icon itm)
                                                 :class "table-icon"}]
                                          [:a (*get-in itm [0 :attr])]]
                                         [:td {:class "line-item"}
                                          [:span {:class "flag-item"}
                                           "S"]]
                                         (let [card (get-cardinality (*get itm 0))]
                                           (if (or (= card "1..1") (= card "1..*"))
                                             [:td {:class "line-item"}
                                              card]
                                             [:td {:class "line-item"
                                                   :style "opacity: 0.4"}
                                              card]))
                                         [:td {:class "line-item"
                                               :style "opacity: 0.4"}
                                          (*get-in itm [0 :type])]
                                         [:td {:class "line-item"} [:a (*get-in itm [0 :desc])]]]]) (get-profile-attrs profile)))))

(defn inner-items-into-hc [itm]
  (loop [attr itm
         res []]
    (if(> (count attr) 0) (recur (rest attr)
                                    (concat res [:tr
                                                [:td {:class "line-inner-item"}
                                                 [:img {:src "/assets/tbl_spacer.png"
                                                        :style "vertical-align: top"}]
                                                 [:img {:src "/assets/tbl_vline.png"
                                                        :style "vertical-align: top; background-color: white"}]
                                                 [:img {:src "/assets/tbl_vjoin.png"
                                                        :style "vertical-align: top; background-color: white"}]
                                                 [:img {:src (get-icon attr)
                                                        :class "table-icon"}]
                                                 attr]
                                                [:td {:class "line-item"}
                                                 [:span {:class "flag-item"}
                                                  "S"]]
                                                (let [card (get-cardinality (*get attr 0))]
                                                  (if (or (= card "1..1") (= card "1..*"))
                                                    [:td {:class "line-item"}
                                                     card]
                                                    [:td {:class "line-item"
                                                          :style "opacity: 0.4"}
                                                     card]))
                                                [:td {:class "line-item"
                                                      :style "opacity: 0.4"}
                                                 (*get-in attr [0 :type])]
                                                 [:td {:class "line-item"} [:a (*get-in attr [0 :desc])]]]))
       res)))

(defn profile [{resourceType :resourceType :as resource}]
  (let [prl ^:title (-> [:table
                         [:tbody
                          [:tr {:style "border: 1px #F0F0F0 solid;
                                  font-size: 11px;
                                  font-family: verdana;
                                  vertical-align: top;"}
                           [:th
                            [:a "Имя"]]
                           [:th
                            [:a "Флаги"]]
                           [:th
                            [:a "Кард."]]
                           [:th
                            [:a "Тип"]]
                           [:th
                            [:a "Описание и ограничения"]]]
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
                                                                                    :style "vertical-align: top; background-color: white;"}]
                                                                             [:img {:src "/assets/tbl_vjoin.png"
                                                                                    :style "vertical-align: top; background-color: white;"}]
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
                                                                                                                    :style "vertical-align: top; background-color: white"}]
                                                                                                             [:img {:src "/assets/tbl_vjoin.png"
                                                                                                                    :style "vertical-align: top; background-color: white"}]
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
                        (vec))]
    (with-meta prl {:title resourceType})
    prl))

(defn profile-page [resource menu]
  (let [prl (profile resource)]
    (-> menu
        (assoc-in [(.indexOf menu (last menu)) (.indexOf (last menu) [:div {:class "profile"}]) 2] prl)
        (with-meta {:title (get resource :resourceType)}))))

(defn profile-page->html [resource]
  (let [page-title (meta (profile-page resource menu))
        pt-page (profile-page resource menu)]
    (layout (:title page-title) pt-page)))



(comment


  (require '[profile-site.core :refer :all])


(defn iner-items-into-hc [attrs]
  (map (fn [attr] (if (sequential? attr)
                    (iner-items-into-hc attr)
                   [:tr
                    [:td {:class "line-inner-item"}
                     [:img {:src "/assets/tbl_spacer.png"
                            :style "vertical-align: top"}]
                     [:img {:src "/assets/tbl_vline.png"
                            :style "vertical-align: top; background-color: white"}]
                     [:img {:src "/assets/tbl_vjoin.png"
                            :style "vertical-align: top; background-color: white"}]
                     [:img {:src (get-icon attr)
                            :class "table-icon"}]
                     attr]
                    [:td {:class "line-item"}
                     [:span {:class "flag-item"}
                      "S"]]
                    (let [card (get-cardinality (*get attr 0))]
                      (if (or (= card "1..1") (= card "1..*"))
                        [:td {:class "line-item"}
                         card]
                        [:td {:class "line-item"
                              :style "opacity: 0.4"}
                         card]))
                    [:td {:class "line-item"
                          :style "opacity: 0.4"}
                     (*get-in attr [0 :type])]
                    [:td {:class "line-item"} [:a (*get-in attr [0 :desc])]]])) (*get attrs 1)))

(iner-items-into-hc (*get-in (get-profile-attrs patient-profile) [2]))

(*get-in (get-profile-attrs patient-profile) [2])

  (defn dot-shit [arr]
    (map #(if (vector? %)
            (dot-shit %)
            (inc %)) arr))

  (dot-shit [1 [2 [10 11 12 [40 50 56 [100000 [2 3 4 5 6 7 [8 9 10]]]]]] 3])

  )
