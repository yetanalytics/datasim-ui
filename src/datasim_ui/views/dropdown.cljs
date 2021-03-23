(ns datasim-ui.views.dropdown
  (:require [reagent.core          :as r]
            ["@material/select"    :refer [MDCSelect]]
            ["@material/textfield" :refer [MDCTextField]]
            [clojure.pprint :refer [pprint]]))

(defn dropdown
  [_]
  (r/create-class
   {:component-did-mount
    (fn [c]
      (MDCSelect. (r/dom-node c)))
    :reagent-render
    (fn [& {:keys [on-change
                   label
                   value
                   full-width?
                   options
                   name
                   id]
            :or {full-width? true
                 id          (str "mdc-text-field-" (random-uuid))}}]
      [:div.mdc-select
       [:select.mdc-select__native-control
        {:name name
         :id id
         :on-change on-change
         :value (or value
                    (:value (first
                             (filter #(:default %) options)))
                    (:value (first options)))}
        (for [option options]
          [:option
           {:value (:value option)
            :key (str name "-" (:value option))}
           (:display option)])]
       [:label.mdc-floating-label
        label]
       [:div.mdc-line-ripple]])}))
