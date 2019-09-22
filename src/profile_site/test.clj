(let [items (for [item (vec (keys (:attrs resource)))] (-> [[:tr
                                                             [:td {:class "line-item"}
                                                              [:img {:src "/assets/tbl_vjoin.png"
                                                                     :style "vertical-align: top"}]
                                                              [:a item]]
                                                             [:td {:class "line-item"} "?"]
                                                             [:td {:class "line-item"}  "?"]
                                                             [:td {:class "line-item"} [:a (-> resource
                                                                                               (:attrs)
                                                                                               (get item)
                                                                                               (get :desc))]]]]
                                                           (concat (for [inner (->> item
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
                                                                       inner]
                                                                      [:td {:class "line-item"} "?"]
                                                                      [:td {:class "line-item"} "?"]
                                                                      [:td {:class "line-item"} [:a (-> resource
                                                                                                        (:attrs)
                                                                                                        (get item)
                                                                                                        (:attrs)
                                                                                                        (get inner)
                                                                                                        (get :desc))]]]))))])
