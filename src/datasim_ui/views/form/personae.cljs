(ns datasim-ui.views.form.personae
  (:require [re-frame.core               :refer [subscribe dispatch]]
            [re-codemirror.core          :as cm]
            [datasim-ui.functions        :as fns]
            [datasim-ui.views.form       :as form]
            [datasim-ui.views.textfield  :as textfield]
            [datasim-ui.views.dropdown   :as dropdown]))

(defmethod form/edit-form [:input/personae :advanced] [key mode]
  [:div
   [:div.advanced
    [form/textarea key]]])


(defmethod form/edit-form [:input/personae :basic] [key mode]
  (form/if-valid
   key
   [:div.edit-basic
    {:key (str "personae-" key)}
    [:div.cardlist-container
     (for [p-index (range (count @(subscribe [:input/get-parsed-data key])))]
       [:div.mdc-card.mdc-card--outlined
        {:key (str "personae-group-" p-index)}
        [textfield/textfield
         :id        (str "input.personae-group-" p-index ".name")
         :label     "Name"
         :value     @(subscribe [:input/get-value key p-index :name])
         :on-change (fn [e]
                      (fns/ps-event e)
                      (dispatch [:input/set-value key (.. e -target -value) p-index :name]))]
        [dropdown/dropdown
         :id        (str "input.personae-group-" p-index ".name")
         :label     "Type"
         :value     @(subscribe [:input/get-value key p-index :objectType])
         :options   [{:value "Group"
                      :display "Group"}]
         :on-change (fn [e]
                      (fns/ps-event e)
                      (dispatch [:input/set-value key (.. e -target -value)
                                 p-index :objectType]))]
        [:h5
         "Members"]
        [:div.cardlist-container
         (for [member-index (range (count @(subscribe [:input/get-value key p-index :member])))]
           (let [[ifi-key ifi-value] @(subscribe [:input/get-ifi key p-index :member member-index])]
             [:div.mdc-card.mdc-card--outlined
              {:key (str "personae-group-" p-index "-member-" member-index)}
              [textfield/textfield
               :id        (str "input.personae-group-" p-index ".member." member-index ".name")
               :label     "Member Name"
               :value     @(subscribe [:input/get-value key p-index :member member-index :name])
               :on-change (fn [e]
                            (fns/ps-event e)
                            (dispatch [:input/set-value key (.. e -target -value)
                                       p-index :member member-index :name]))]
              [textfield/textfield
               :id        (str "input.personae-group-" p-index ".member." member-index ".role")
               :label     "Role (optional)"
               :value     @(subscribe [:input/get-value key p-index :member member-index :role])
               :on-change (fn [e]
                            (fns/ps-event e)
                            (dispatch [:input/set-value key (.. e -target -value)
                                       p-index :member member-index :role]))]
              [dropdown/dropdown
               :id        (str "input.personae-group-" p-index ".member." member-index ".ifi.key")
               :label     "Identifier Type"
               :value     (name ifi-key)
               :options   [{:value "mbox"
                            :display "MBox"}
                           {:value "mbox_sha1sum"
                            :display "MBox Checksum"}
                           {:value "openid"
                            :display "OpenID"}
                           {:value "account"
                            :display "Account"}]
               :on-change (fn [e]
                            (fns/ps-event e)
                            (let [val (keyword (.. e -target -value))]
                              (dispatch [:input/set-ifi key [val
                                                             (if (not= val ifi-key)
                                                               (if (= val :account)
                                                                 {}
                                                                 "")
                                                               ifi-value)]
                                         p-index :member member-index])))]
              (if (= ifi-key :account)
                [:div
                 [textfield/textfield
                  :id        (str "input.personae-group-" p-index ".member." member-index ".ifi.account.homePage")
                  :label     "Account Homepage"
                  :value     (:homePage ifi-value)
                  :on-change (fn [e]
                               (fns/ps-event e)
                               (dispatch [:input/set-ifi key [ifi-key {:homePage (.. e -target -value)
                                                                       :name (:name ifi-value)}]
                                          p-index :member member-index]))]
                 [textfield/textfield
                  :id        (str "input.personae-group-" p-index ".member." member-index ".ifi.account.name")
                  :label     "Account Name"
                  :value     (:name ifi-value)
                  :on-change (fn [e]
                               (fns/ps-event e)
                               (dispatch [:input/set-ifi key [ifi-key {:homePage (:homePage ifi-value)
                                                                       :name (.. e -target -value)}]
                                          p-index :member member-index]))]]
                [textfield/textfield
                 :id        (str "input.personae-group-" p-index ".member." member-index ".ifi.value")
                 :label     (name ifi-key)
                 :value     ifi-value
                 :on-change (fn [e]
                              (fns/ps-event e)
                              (dispatch [:input/set-ifi key [ifi-key (.. e -target -value)]
                                         p-index :member member-index]))])
              [:span.element-button.clickable
               {:on-click (fn [e]
                            (fns/ps-event e)
                            (dispatch [:input/remove-element key member-index p-index :member]))}
               [:span.mdc-tab__icon.material-icons
                "remove_circle"]
               "Remove Member"]]))
         [:span.element-button.clickable
          {:on-click (fn [e]
                       (fns/ps-event e)
                       (dispatch [:input/add-element key {:mbox ""} p-index :member]))}
          [:span.mdc-tab__icon.material-icons
           "add_box"]
          "Add Member"]]
        [:span.element-button.clickable
         {:on-click (fn [e]
                      (fns/ps-event e)
                      (dispatch [:input/remove-element key p-index]))}
         [:span.mdc-tab__icon.material-icons
          "remove_circle"]
         "Remove Group"]
        ])
     [:span.element-button.clickable
      {:on-click (fn [e]
                   (fns/ps-event e)
                   (dispatch [:input/add-element key {}]))}
      [:span.mdc-tab__icon.material-icons
       "add_box"]
      "Add Group"]]]))
