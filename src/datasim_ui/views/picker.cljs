(ns datasim-ui.views.picker
  (:require [reagent.core :as r]
            [cljsjs.flatpickr]
            [clojure.pprint :refer [pprint]]))


(defn date-time-picker
  "React wrapper around the Flatpickr date time picker"
  [& {:keys [close-fn date
             ?min-date ?max-date]
      :or   {close-fn identity}}]
  (let [;; need a parent element so the alt format can be appended somewhere
        picker (.createElement js/document "div")
        el     (.createElement js/document "input")
        _      (set! (.-placeholder el) "Select Date...")
        _      (.appendChild picker el)
        fp     (js/flatpickr el
                         (clj->js (cond->
                                      {:static        true
                                       :altInput      true
                                       :altFormat     "Z"
                                       :dateFormat    "Z"
                                       :defaultDate   date
                                       :enableTime    true
                                       :enableSeconds true
                                       :onClose       (fn [selected-date formatted-date]
                                                        ;; pass the selected date back to the callback
                                                        (close-fn formatted-date))}
                                    ?min-date
                                    (assoc :minDate ?min-date)
                                    ?max-date
                                    (assoc :maxDate ?max-date))))]
    (r/create-class
     {:reagent-render        (fn [_ _]
                               [:div])
      :component-did-mount   (fn [this]
                               (-> this
                                   r/dom-node
                                   (.appendChild picker)))
      :component-will-update (fn [_ new-argv]
                               (let [args (apply hash-map (rest new-argv))]
                                 ;; dynamically update the date, and min and max dates
                                 (.setDate fp (:date args))
                                 (.set fp "minDate" (:?min-date args))
                                 (.set fp "maxDate" (:?max-date args))
                                 (.redraw fp)))})))
