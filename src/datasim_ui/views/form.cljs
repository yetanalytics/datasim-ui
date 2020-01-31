(ns datasim-ui.views.form
  (:require [re-mdl.core          :as mdl]
            [re-frame.core        :refer [subscribe dispatch]]
            [datasim-ui.functions :as fns]))

(defn form
  [body]
  [:div
   [:form#generate-form
    {:action  "http://127.0.0.1:9000/api/v1/generate"
     :method  "post"
     :encType "multipart/form-data"}
    body]])

(defn text-field
  [input-name sub-key]    
  [mdl/textfield
   :input-attr {:name input-name}
   :class      "editor"
   :type       :textarea
   :rows       15
   :model      @(subscribe [sub-key])
   :handler-fn #(dispatch [(keyword input-name "import") %])])
