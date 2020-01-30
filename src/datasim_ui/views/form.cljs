(ns datasim-ui.views.form
  (:require [re-mdl.core :as mdl]))

(defn form
  [body]
  [:div
   [:form#generate-form
    {:action  "http://127.0.0.1:9000/api/v1/generate"
     :method  "post"
     :encType "multipart/form-data"}
    body]])

(defn text-field
  [text input-name]
  [:div
   [:div.text-field--header
    [:span input-name]
    [:div.spacer]
    [mdl/button
     :class          "mini-button"
     :raised?        true
     :colored?       true
     :ripple-effect? true
     :child          "Import"]
    [mdl/button
     :class          "mini-button"
     :raised?        true
     :colored?       true
     :ripple-effect? true
     :child          "Export"]]
   [:textarea
    {:id   (str "textarea-" input-name)
     :name input-name
     :rows 20
     :cols 100}]])
