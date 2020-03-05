(ns datasim-ui.views.editor
  (:require [re-frame.core         :refer [dispatch subscribe]]
            [datasim-ui.views.form :as form]
            [datasim-ui.functions  :as fns]
            [datasim-ui.util       :refer [inputs]]))

(defn editor-tab
  [[k v] disabled?]
  [:button.minorbutton
   (cond-> {:on-click (fn [e]
                        (fns/ps-event e)
                        (dispatch [:focus/set k]))}
     disabled?
     (assoc :disabled true))
   v])

(defn editor-tab-bar
  [sub-key]
  (into [:span]
        (for [input inputs]
          [editor-tab input (= sub-key (first input))])))

(defn editor*
  [key size]
  (let [input-name (get inputs key)
        id         (str input-name "-input")]
    [:div
     {:class (if (= :min size)
               "cell-6"
               "cell-12")}
     [:div
      [:div.editor--header
       [:input
        {:id       id
         :type     "file"
         :class    "hidden-button"
         :onChange (fn [e]
                     (fns/import-file e key))}]
       [:span
        (if (= :max size)
          [editor-tab-bar key]
          [:span.editor-title input-name])]
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
      [form/textarea key]]]))

(defn editor
  [key]
  (if-let [?focus @(subscribe [:db/focus])]
    (if (= key ?focus)
      [editor* key :max])
    [editor* key :min]))
