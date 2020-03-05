(ns datasim-ui.views.snackbar
  (:require ["@material/snackbar" :refer [MDCSnackbar]]))

(defonce sb
  (delay (MDCSnackbar.
          (.querySelector js/document ".mdc-snackbar"))))

(defn snackbar!
  [message]
  (.show @sb (clj->js {:message message})))

(defn snackbar
  []
  [:div.mdc-snackbar.datasim-snackbar
   {:aria-live "assertive"
    :aria-atomic "true"
    :aria-hidden "true"}
   [:div.mdc-snackbar__text]
   [:div.mdc-snackbar__action-wrapper
    [:button.mdc-snackbar__action-button
     {:type "button"}]]])
