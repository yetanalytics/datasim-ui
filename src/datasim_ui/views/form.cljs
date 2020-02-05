(ns datasim-ui.views.form
  (:require [re-mdl.core                 :as mdl]
            [re-frame.core               :refer [subscribe dispatch]]
            [datasim-ui.views.codemirror :as cm]
            [datasim-ui.functions        :as fns]
            [datasim-ui.util             :as util]))

(defn form
  [body]
  [:div
   [:form#generate-form
    {:action  "http://127.0.0.1:9090/api/v1/generate"
     :method  "post"
     :encType "multipart/form-data"}
    body]])

(defn textarea
  [key]    
  [cm/codemirror
   :name      (util/input-name key)
   :value     @(subscribe [key])
   :update-fn #(dispatch [key %])]
  #_[mdl/textfield
   :input-attr {:name (util/input-name key)}
   :class      "editor"
   :type       :textarea
   :rows       15
   :model      @(subscribe [key])
   :handler-fn #(dispatch [key %])])

(defn textfield
  [key]
  [mdl/textfield
   :input-attr      {:name (util/input-name key)}
   :floating-label? true
   :label           (util/label key)
   :model           @(subscribe [key])
   :handler-fn      #(dispatch [key %])])

(defn options
  []
  [mdl/cell
   :class "options-container"
   :col   12
   :children
   [[:div
     {:class (cond-> "options"
               @(subscribe [:options/visible])
               (str " visible"))}
     [:h6  "Run Options"]
     [textfield :options/endpoint]
     [textfield :options/api-key]
     [textfield :options/api-secret-key]
     [mdl/toggle-checkbox
      :input-attr     {:name "send-to-lrs"}
      :model          @(subscribe [:options/send-to-lrs])
      :label          "Send Statements to LRS"'
      :ripple-effect? true
      :handler-fn     #(dispatch [:options/send-to-lrs])]]]])
