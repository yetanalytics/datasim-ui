(ns datasim-ui.views.dialog
  (:require [re-mdl.core   :as mdl]
            [re-frame.core :refer [subscribe dispatch]]))

(defmulti dialog-form
  (fn [[_ type]]
    type))

(defmethod dialog-form :text
  [[id _]]
  [mdl/textfield
   :label           @(subscribe [:dialog.form/label id])
   :model           @(subscribe [:dialog.form/text id])
   :floating-label? true
   :handler-fn      (fn [e]
                      (dispatch [:dialog.form.text/set id e]))])

(defmethod dialog-form :default
  [[_ type]]
  (println type " not implemented"))

(defn dialog
  []
  [mdl/dialog
   :children
   [[mdl/dialog-title
     :child @(subscribe [:dialog/title])]
    [mdl/dialog-content
     :children
     (into []
           (for [form @(subscribe [:dialog.form/ids])]
             [dialog-form form]))]
    [mdl/dialog-actions
     :children
     [[mdl/button
       :child    "OK"
       :on-click (fn [e]
                   (.preventDefault e)
                   (.stopPropagation e)
                   (@(subscribe [:dialog/save])))]
      [mdl/button
       :child    "Cancel"
       :on-click #(dispatch [:dialog/close])]]]]])
