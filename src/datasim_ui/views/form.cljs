(ns datasim-ui.views.form
  (:require [re-mdl.core          :as mdl]
            [re-frame.core        :refer [subscribe dispatch]]
            [datasim-ui.functions :as fns]
            [datasim-ui.util      :as util]))

(defn form
  [body]
  [:div
   [:form#generate-form
    {:action  "http://127.0.0.1:9000/api/v1/generate"
     :method  "post"
     :encType "multipart/form-data"}
    body]])

(defn textarea
  [key]    
  [mdl/textfield
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
  (if @(subscribe [:options/visible])
    [:div
     [:h6  "Run Options"]
     [textfield :options/endpoint]
     [textfield :options/api-key]
     [textfield :options/api-secret-key]]
    [:div ""]))
