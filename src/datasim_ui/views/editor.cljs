(ns datasim-ui.views.editor
  (:require [re-mdl.core           :as mdl]
            [re-frame.core         :refer [dispatch subscribe]]
            [datasim-ui.views.form :as form]
            [datasim-ui.functions  :as fns]
            [datasim-ui.util       :refer [inputs]]))

(defn editor-tab
  [[k v]]
  [mdl/button
   :class          "mini-button"
   :raised?        true
   :colored?       true
   :ripple-effect? true
   :child          v
   :on-click       (fn [e]
                     (fns/ps-event e)
                     (dispatch [:focus/set k]))])

(defn editor-tab-bar
  [sub-key]
  (into [:span]
        (for [input (dissoc inputs sub-key)]
          [editor-tab input])))

(defn editor
  [key size]
  (let [input-name (get inputs key)
        id         (str input-name "-input")]
    [mdl/cell
     :col (if (= :min size)
            6
            12)
     :children
     [[:div
       [:div.editor--header
        [:input
         {:id       id
          :type     "file"
          :class    "hidden-button"
          :onChange (fn [e]
                      (fns/import-file e key))}]
        [:span
         [:span.editor-title input-name]
         (when (= :max size)
           [editor-tab-bar key])]
        [:div.spacer]
        [mdl/button
         :class          "mini-button"
         :raised?        true
         :colored?       true
         :ripple-effect? true
         :child          "Import from URL"
         :on-click       (fn [e]
                           (fns/import-url e key))]
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
                                            (js/Blob. [@(subscribe [key])]
                                                      clj->js {:type "application/json"})
                                            (str input-name ".json")))]
        [mdl/button
         :class          "mini-button-icon"
         :icon?          true
         :ripple-effect? true
         :child          [:i.material-icons (if (= :min size)
                                              "fullscreen"
                                              "fullscreen_exit")]
         :on-click       (fn [e]
                           (fns/ps-event e)
                           (dispatch [(if (= :min size)
                                        :focus/set
                                        :focus/clear)
                                      key]))]]
       [form/textarea key]]]]))

(defn editor-min
  [sub-key]
  [editor sub-key :min])

(defn editor-max
  [sub-key]
  [editor sub-key :max])
