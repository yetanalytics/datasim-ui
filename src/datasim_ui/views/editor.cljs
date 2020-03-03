(ns datasim-ui.views.editor
  (:require [re-mdl.core           :as mdl]
            [re-frame.core         :refer [dispatch subscribe]]
            [datasim-ui.views.form :as form]
            [datasim-ui.functions  :as fns]
            [datasim-ui.util       :refer [inputs]]))

(defn editor-tab
  [[k v]]
  [:button.minorbutton
   {:on-click (fn [e]
                (fns/ps-event e)
                (dispatch [:focus/set k]))}
   v])

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
        [:button.minorbutton
         {:on-click (fn [e]
                      (fns/import-url e key))}
         "Import from URL"]
        [:button.minorbutton
         {:on-click (fn [e]
                      (fns/click-input e id))}
         "Import File"]
        [:button.minorbutton
         {:on-click (fn [e]
                      (fns/export-file e
                                       (js/Blob. [@(subscribe [key])]
                                                 clj->js {:type "application/json"})
                                       (str input-name ".json")))}
         "Export File"]
        [:button.mdc-icon-button.material-icons
         {:on-click (fn [e]
                      (fns/ps-event e)
                      (dispatch [(if (= :min size)
                                   :focus/set
                                   :focus/clear)
                                 key]))}
         (if (= :min size)
           "fullscreen"
           "fullscreen_exit")]]
       [form/textarea key]]]]))

(defn editor-min
  [sub-key]
  [editor sub-key :min])

(defn editor-max
  [sub-key]
  [editor sub-key :max])
