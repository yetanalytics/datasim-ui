(ns datasim-ui.views.editor
  (:require [re-mdl.core           :as mdl]
            [re-frame.core         :refer [dispatch subscribe]]
            [datasim-ui.views.form :as form]
            [datasim-ui.functions  :as fns]))

(def inputs
  {:input/profiles   "Profiles"
   :input/personae   "Personae"
   :input/alignments "Alignments"
   :input/parameters "Parameters"})

(defn editor-split-pane
  []
  [:div 
   {:on-click #(dispatch [:focus/clear])}
   "Return"])

(defn editor-tab
  [[k v]]
  [:div 
   {:on-click #(dispatch [:focus/set k])}
   v])

(defn editor-tab-bar
  [sub-key]
  [mdl/cell
   :col 12
   :children
   (conj
    (into []
          (for [input (dissoc inputs sub-key)]
            [editor-tab input]))
    [editor-split-pane])])

(defn editor
  [sub-key size]
  (let [input-name (get inputs sub-key)
        id         (str input-name "-input")]
    [mdl/cell
     :col (if (= :min size)
            6
            12)
     :children
     [[:div
       [:div.text-field--header
        [:input
         {:id       id
          :type     "file"
          :class    "hidden-button"
          :onChange (fn [e]
                      (fns/import-file e (keyword input-name "import")))}]
        [:span input-name]
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
                                            (str input-name ".json")))]
        (when (= :min size)
          [mdl/button
           :icon?    true
           :child    [:i.material-icons "fullscreen"]
           :on-click (fn [e]
                       (fns/ps-event e)
                       (dispatch [:focus/set sub-key]))])]
       [form/text-field input-name sub-key]]]]))

(defn editor-min
  [sub-key]
  [editor sub-key :min])

(defn editor-max
  [sub-key]
  [editor sub-key :max])
