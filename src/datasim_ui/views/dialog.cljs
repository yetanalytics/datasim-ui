(ns datasim-ui.views.dialog
  (:require [reagent.core               :as r]
            [re-frame.core              :refer [subscribe dispatch]]
            ["@material/dialog"         :refer [MDCDialog]]
            [datasim-ui.functions       :as fns]
            [datasim-ui.views.textfield :as textfield]))

(defmulti dialog-form
  (fn [[_ type]]
    type))

(defmethod dialog-form :text
  [[id _]]
  [textfield/textfield
   :label     @(subscribe [:dialog.form/label id])
   :value     @(subscribe [:dialog.form/text id])
   :on-change (fn [e]
                (fns/ps-event e)
                (dispatch [:dialog.form.text/set id (.. e -target -value)]))])

(defmethod dialog-form :default
  [[_ type]]
  (println type " not implemented"))

(defn dialog
  "A modal dialog."
  [_]
  (let [title-id   (str "dialog-title-" (random-uuid))
        content-id (str "dialog-content-" (random-uuid))
        dialog-ref (atom nil)]
    (r/create-class
     {:reagent-render
      (fn [{:keys [title
                   content
                   actions
                   full-width?
                   full-height?]}]
        [:div.mdc-dialog.dave-dialog
         (cond-> {:role             "alertdialog"
                  :aria-modal       true
                  :aria-labelledby  title-id
                  :aria-describedby content-id}
           full-width?  (update :class str " fullwidth")
           full-height? (update :class str " fullheight"))
         [:div.mdc-dialog__container
          [:div.mdc-dialog__surface
           [:h2.mdc-dialog__title
            {:id title-id}
            title]
           (into [:div.mdc-dialog__content
                  {:id content-id}]
                 content)
           (into [:footer.wizardfooter.mdc-dialog__actions
                  [:button.mdc-button.mdc-dialog__button
                   {:data-mdc-dialog-action "cancel"
                    ;; TODO: on-click that cancels the dialog action in a
                    ;; re-framey way.
                    :tab-index 0}
                  "Cancel"]
                  [:button.mdc-button.mdc-dialog__button
                   {:on-click @(subscribe [:dialog/save])}
                   "OK"]]
                 #_(for [[idx {:keys [label
                                    mdc-dialog-action
                                    on-click
                                    disabled?]}] (map-indexed vector actions)]
                   [:button.mdc-button.mdc-dialog__button
                    (cond-> {:on-click  on-click
                             :tab-index (inc idx)}
                      disabled?
                      (assoc :disabled true)
                      mdc-dialog-action
                      (assoc :data-mdc-dialog-action
                             mdc-dialog-action))
                    label]))]
          [:div.mdc-dialog__scrim]]])
      :component-did-mount
      (fn [c]
        (let [d (MDCDialog. (r/dom-node c))]
          (.listen d "MDCDialog:closed"
                   (fn [e]
                     ;; If the dialog closes or is cancelled, we should
                     ;; also reflect this back to app state
                     (when (contains? #{"close"
                                        "cancel"}
                                      (-> e .-detail .-action))
                       (dispatch [:dialog/close]))))
          (.open (reset! dialog-ref d))))
      :component-will-unmount
      (fn [c]
        (.close @dialog-ref))})))

(defn dialog-container
  "Parent component that shows/hides the dialog"
  []
  (cond-> [:div.datasim-dialog-container]
    @(subscribe [:dialog/open])
    (conj [dialog {:title   "DIALOG"
                   :content (into []
                                  (for [form @(subscribe [:dialog.form/ids])]
                                    [dialog-form form]))
                   :actions []}])))
