(ns datasim-ui.views.form
  (:require [re-mdl.core   :as mdl]
            [re-frame.core :refer [subscribe dispatch]]))

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
                    (.preventDefault e)
                    (.stopPropagation e)
                    (.then (js/Promise.resolve
                            (.text (aget (.. e -currentTarget -files) 0)))
                           #(dispatch [(keyword input-name "import") %])))}]
      [:span input-name]
      [:div.spacer]
      [mdl/button
       :class          "mini-button"
       :raised?        true
       :colored?       true
       :ripple-effect? true
       :child          "Import"
       :on-click       (fn [e]
                         (.preventDefault e)
                         (.stopPropagation e)
                         (.click (js/document.getElementById id)))]
      [mdl/button
       :class          "mini-button"
       :raised?        true
       :colored?       true
       :ripple-effect? true
       :child          "Export"
       :on-click       (fn [e]
                         (.preventDefault e)
                         (.stopPropagation e)
                         (let [blob (js/Blob. [@(subscribe [sub-key])]
                                              clj->js {:type "application/json"})
                               link (js/document.createElement "a")]
                           (set! (.-download link) (str input-name ".json"))
                           (if js/window.webkitURL
                             (set! (.-href link)
                                   (.createObjectURL js/window.webkitURL
                                                     blob))
                             (do
                               (set! (.-href link)
                                     (.createObjectURL js/window.URL
                                                       blob))
                               (set! (.-onclick link)
                                     (fn [e]
                                       (.preventDefault e)
                                       (.stopPropagation e)
                                       (.remove (.. e -currentTarget))))
                               (set! (.. link -style -display)
                                     "none")
                               (.append js/document.body
                                        link)))
                           (.click link)))]]
     [mdl/textfield
      :input-attr {:name input-name}
      :class      "editor"
      :type       :textarea
      :rows       15
      :model      @(subscribe [sub-key])
      :handler-fn #(dispatch [(keyword input-name "import") %])]]))
