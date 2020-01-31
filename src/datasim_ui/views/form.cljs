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
  (let [id (str input-name "-input")]
    [:div
     [:div.text-field--header
      [:input
       {:id       id
        :type     "file"
        :class    "hidden-button"
        :onChange (fn [e]
                    (fns/import-file e (keyword input-name "import")))}]
      [:span (clojure.string/capitalize input-name)]
      [:div.spacer]
      [mdl/button
       :class          "mini-button"
       :raised?        true
       :colored?       true
       :ripple-effect? true
       :child          "Import"
       :on-click       (fn [e]
                         (fns/click-input e id))]
      [mdl/button
       :class          "mini-button"
       :raised?        true
       :colored?       true
       :ripple-effect? true
       :child          "Export"
       :on-click       (fn [e]
                         (fns/export-file e
                                          (js/Blob. [@(subscribe [sub-key])]
                                                    clj->js {:type "application/json"})
                                          (str input-name ".json")))]]
     [mdl/textfield
      :input-attr {:name input-name}
      :class      "editor"
      :type       :textarea
      :rows       15
      :model      @(subscribe [sub-key])
      :handler-fn #(dispatch [(keyword input-name "import") %])]]))
