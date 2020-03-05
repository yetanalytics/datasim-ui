(ns datasim-ui.views.checkbox)

(defn checkbox
  [& {:keys [id label name]
      :or   {id (random-uuid)}}]
  [:div.mdc-form-field
   [:div.mdc-checkbox
    [:input.mdc-checkbox__native-control
     (cond-> {:id   id
              :type "checkbox"}
       name (assoc :name name))]
    [:div.mdc-checkbox__background
     [:svg.mdc-checkbox__checkmark
      {:viewBox "0 0 24 24"}
      [:path.mdc-checkbox__checkmark-path
       {:fill "none"
        :d    "M1.73,12.91 8.1,19.28 22.79,4.59"}]]
     [:div.mdc-checkbox__mixedmark]]
    [:div.mdc-checkbox__ripple]]
   [:label
    {:for id}
    label]])
