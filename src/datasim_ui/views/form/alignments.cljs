(ns datasim-ui.views.form.alignments
  (:require [re-frame.core               :refer [subscribe dispatch]]
            [re-codemirror.core          :as cm]
            [datasim-ui.functions        :as fns]
            [datasim-ui.views.form       :as form]
            [datasim-ui.views.textfield  :as textfield]
            [datasim-ui.views.dropdown   :as dropdown]
            [clojure.pprint              :refer [pprint]]))

(defmethod form/edit-form [:input/alignments :advanced] [key mode]
  [:div
   [:div.advanced
    [form/textarea key]]])

(defmethod form/edit-form [:input/alignments :basic] [key mode]
  (form/if-valid
      key
      [:div.edit-basic
       {:key (str "alignment" key)}
     (let [agent-options (into [{:value "" :display "Select Actor"}]
                               (mapv (fn [actor]
                                       {:value actor
                                        :display actor})
                                     @(subscribe [:input/get-actor-ifis {}])))
           group-options (into [{:value "" :display "Select Group"}]
                               (mapv (fn [actor]
                                       {:value actor
                                        :display actor})
                                     @(subscribe [:input/get-actor-groups {}])))
           role-options (into [{:value "" :display "Select Role"}]
                               (mapv (fn [actor]
                                       {:value actor
                                        :display actor})
                                     @(subscribe [:input/get-actor-roles {}])))
           profile-options  (let [iris @(subscribe [:input/get-profile-iris {}])]
                              (reduce (fn [all key]
                                        (into (conj all
                                                    {:display
                                                     (str "##" (name key)
                                                          "##")
                                                     :value (name key)})
                                              (mapv
                                               (fn [iri] {:display iri
                                                          :value iri})
                                               (key iris))
                                              ))
                                      [{:value "" :display "Select Component"}]
                                      (keys iris)))]
       [:div.cardlist-container
        (for [a-index (range (count @(subscribe [:input/get-parsed-data key])))]
          [:div.mdc-card.mdc-card--outlined
           {:key (str "alignment-card-" a-index)}
           [dropdown/dropdown
            :id        (str "input.alignments." a-index ".id")
            :label     "Alignment Type"
            :value     @(subscribe [:input/get-value key a-index :type])
            :options   (cond-> [{:value "Agent"
                                 :display "Agent"}
                                {:value "Group"
                                 :display "Group"}
                                {:value "Role"
                                 :display "Role"}])
            :on-change (fn [e]
                         (fns/ps-event e)
                         (dispatch [:input/set-value key (.. e -target -value)
                                    a-index :type]))]
           (let [alignment-type @(subscribe [:input/get-value key a-index :type])]
             [dropdown/dropdown
              :id        (str "input.alignments." a-index ".id")
              :label     alignment-type
              :value     @(subscribe [:input/get-value key a-index :id])
              :options   (case alignment-type
                           "Agent" agent-options
                           "Group" group-options
                           "Role"  role-options)
              :on-change (fn [e]
                           (fns/ps-event e)
                           (dispatch [:input/set-value key (.. e -target -value)
                                      a-index :id]))])
           [:div.cardlist-container
            (for [c-index (range (count @(subscribe [:input/get-value key a-index
                                                     :alignments])))]
              [:div.mdc-card.mdc-card--outlined
               {:key (str "alignment-" a-index "-component-" c-index)}
               [dropdown/dropdown
                :id        (str "input.alignments."a-index".alignments." c-index
                                ".component")
                :label     "Component"
                :value     @(subscribe [:input/get-value key a-index :alignments
                                        c-index :component])
                :options   profile-options
                :on-change (fn [e]
                             (fns/ps-event e)
                             (dispatch [:input/set-value key (.. e -target -value)
                                        a-index :alignments c-index :component]))]
               [textfield/numeric
                :id        (str "input.alignments."a-index".alignments." c-index
                                ".weight")
                :label     "Weight"
                :step      "0.01"
                :max       "1.00"
                :min       "-1.00"
                :value     @(subscribe [:input/get-value key a-index :alignments
                                        c-index :weight])
                :on-change (fn [e]
                             (fns/ps-event e)
                             (dispatch [:input/set-value key (js/parseFloat (.. e -target -value))
                                        a-index :alignments c-index :weight]))]
               [:span.element-button.clickable
                {:on-click (fn [e]
                             (fns/ps-event e)
                             (dispatch [:input/remove-element key c-index
                                        a-index :alignments]))}
                [:span.mdc-tab__icon.material-icons
                 "remove_circle"]
                "Remove Component"]])
            [:span.element-button.clickable
             {:on-click (fn [e]
                          (fns/ps-event e)
                          (dispatch [:input/add-element key {}
                                     a-index :alignments]))}
             [:span.mdc-tab__icon.material-icons
              "add_box"]
             "Add Component"]]
           [:span.element-button.clickable
            {:on-click (fn [e]
                         (fns/ps-event e)
                         (dispatch [:input/remove-element key a-index]))}
            [:span.mdc-tab__icon.material-icons
             "remove_circle"]
            "Remove Alignment"]
           ])
        [:span.element-button.clickable
         {:on-click (fn [e]
                      (fns/ps-event e)
                      (dispatch [:input/add-element key {:type "Agent"}]))}
         [:span.mdc-tab__icon.material-icons
          "add_box"]
         "Add Alignment"]])]))
