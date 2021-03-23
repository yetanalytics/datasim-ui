(ns datasim-ui.views.textfield
  (:require [reagent.core          :as r]
            ["@material/textfield" :refer [MDCTextField]]
            [clojure.pprint :refer [pprint]]))

(defn textfield
  [_]
  (r/create-class
   {:component-did-mount
    (fn [c]
      (MDCTextField. (r/dom-node c)))
    :reagent-render
    (fn [& {:keys [on-change
                   label
                   value
                   full-width?
                   name
                   id]
            :or {full-width? true
                 id          (str "mdc-text-field-" (random-uuid))}}]
      [:div.mdc-text-field
       {:class (when full-width?
                 "mdc-text-field--fullwidth")}
       [:input.mdc-text-field__input
        (cond-> {:type      "text"
                 :id        id
                 :value     value
                 :on-change on-change}
          name (assoc :name name))]
       [:label.mdc-floating-label
        {:for id}
        label]
       [:div.mdc-line-ripple]])}))

(defn numeric
  [_]
  (r/create-class
   {:component-did-mount
    (fn [c]
      (MDCTextField. (r/dom-node c)))
    :reagent-render
    (fn [& {:keys [on-change
                   label
                   value
                   step
                   max
                   min
                   full-width?
                   name
                   id]
            :or {full-width? true
                 id          (str "mdc-text-field-" (random-uuid))}}]
      [:div.mdc-text-field
       {:class (when full-width?
                 "mdc-text-field--fullwidth")}
       [:input.mdc-text-field__input
        (cond-> {:type      "number"
                 :id        id
                 :value     value
                 :on-change on-change
                 :step      step
                 :max       max
                 :min       min}
          name (assoc :name name))]
       [:label.mdc-floating-label
        {:for id}
        label]
       [:div.mdc-line-ripple]])}))

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
