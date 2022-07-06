(ns datasim-ui.views.editor
  (:require [re-frame.core         :refer [dispatch subscribe]]
            [datasim-ui.views.form :as form]
            [datasim-ui.views.form.alignments]
            [datasim-ui.views.form.parameters]
            [datasim-ui.views.form.personae]
            [datasim-ui.views.form.profiles]
            [datasim-ui.functions  :as fns]
            [datasim-ui.util       :refer [inputs]]
            [clojure.pprint        :refer [pprint]]))

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
        id         (str input-name "-input")
        data       (if (= key :input/profiles)
                     @(subscribe [:input/get-data-vector key])
                     @(subscribe [:input/get-data key]))
        modes      @(subscribe [:input/get-modes key])]
    [:div.editor
     {:class (if (= :min size)
               "cell-6"
               "cell-12 max")}
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
                    (fns/click-input e id))}
       "Import File"]
      [:button.minorbutton
       {:on-click (fn [e]
                    (fns/export-file e
                                     (js/Blob. [data]
                                               clj->js {:type "application/json"})
                                     (str input-name ".json")))}
       "Export File"]
      [:button.mdc-icon-button.material-icons.icon-mini
       {:on-click (fn [e]
                    (fns/ps-event e)
                    (dispatch [(if (= :min size)
                                 :focus/set
                                 :focus/clear)
                               key]))}
       (if (= :min size)
         "fullscreen"
         "fullscreen_exit")]]
     [:div
      (if (seq modes)
        [:div.mdc-tab-bar
         {:role "tablist"}
         [:div.mdc-tab-scroller
          [:div.mdc-tab-scroller__scroll-area
           [:div.mdc-tab-scroller__scroll-content
            (for [mode modes]
              [:button
               {:class (cond-> "mdc-tab"
                         (:selected mode) (str " mdc-tab--active"))
                :role "tab"
                :aria-selected (:selected mode)
                :tabIndex "0"
                :key (str (name key) "-" (name (:mode mode)) "-tab-button")
                :on-click (fn [e]
                            (fns/ps-event e)
                            (dispatch [:input/set-selected-mode
                                       key
                                       (:mode mode)]))}
               [:span.mdc-tab__content
                (if (:icon mode)
                  [:span.mdc-tab__icon.material-icons
                   {:aria-hidden "true"}
                   (:icon mode)])
                [:span.mdc-tab__text-label
                 (if (:display mode)
                   (subs (:display mode) 0 12)
                   (:mode mode))]]
               (if (= (:mode-type mode) :profile-tabs)
                 [:span.mdc-tab__icon.material-icons.remove-profile.clickable
                  {:aria-hidden "true"
                   :on-click (fn [e]
                               (dispatch [:input/remove-element-vector key
                                          (:index mode)]))}
                  "clear"])
               [:span
                {:class (cond-> "mdc-tab-indicator"
                          (:selected mode) (str " mdc-tab-indicator--active"))}
                [:span.mdc-tab-indicator__content.mdc-tab-indicator__content--underline]]])
            (if (= :profile-tabs (-> modes
                                     first
                                     :mode-type))
              [:span.mdc-tab__icon.material-icons.add-profile.clickable
               {:aria-hidden "true"
                :on-click (fn [e]
                            (dispatch [:input/add-element-vector key "{}"]))}
               "add"])]]]])
      (let [mode @(subscribe [:input/get-selected-mode key])]
        [form/edit-form key mode])]]))

(defn editor
  [key]
  (if-let [?focus @(subscribe [:db/focus])]
    (if (= key ?focus)
      [editor* key :max])
    [editor* key :min]))
