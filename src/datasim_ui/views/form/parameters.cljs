(ns datasim-ui.views.form.parameters
  (:require [re-frame.core               :refer [subscribe dispatch]]
            [re-codemirror.core          :as cm]
            [datasim-ui.functions        :as fns]
            [datasim-ui.views.form       :as form]
            [datasim-ui.views.textfield  :as textfield]
            [datasim-ui.views.dropdown   :as dropdown]
            [datasim-ui.views.picker     :as picker]
            [datasim-ui.timezone         :as tz]))

(defmethod form/edit-form [:input/parameters :advanced] [key mode]
  [:div
   [:div.advanced
    [form/textarea key]]])


(defmethod form/edit-form [:input/parameters :basic] [key mode]
  (form/if-valid
   key
   [:div.edit-basic
    [:p
     "Start Date"]
    [picker/date-time-picker
     :close-fn  (fn [date]
                  (dispatch [:input/set-value key date :start]))
     :date      @(subscribe [:input/get-value key :start])
     :?max-date nil]

    [:p
     "End Date"]
    [picker/date-time-picker
     :close-fn  (fn [date]
                  (dispatch [:input/set-value key date :end]))
     :date      @(subscribe [:input/get-value key :end])
     :?max-date nil]
    [dropdown/dropdown
     :id        "input.parameters.timezone"
     :label     "Timezone"
     :value     @(subscribe [:input/get-value :input/parameters :timezone])
     :options   (mapv (fn [zone]
                        {:value (:zone zone)
                         :display (str (:zone zone)
                                       " (" (:offset zone) ")")})
                      tz/timezones)
     :on-change (fn [e]
                  (fns/ps-event e)
                  (dispatch [:input/set-value key (.. e -target -value) :timezone]))]
    [textfield/textfield
     :id        "input.parameters.seed"
     :label     "Simulation Seed"
     :value     @(subscribe [:input/get-value :input/parameters :seed])
     :on-change (fn [e]
                  (fns/ps-event e)
                  (dispatch [:input/set-value key (js/parseInt (.. e -target -value)) :seed]))]

    [textfield/numeric
     :id        (str "input.parameters.max")
     :label     "Max Statements"
     :step      "1"
     :min       "0"
     :value     @(subscribe [:input/get-value :input/parameters :max])
     :on-change (fn [e]
                  (fns/ps-event e)
                  (dispatch [:input/set-value key (js/parseInt (.. e -target -value)) :max]))]]))
