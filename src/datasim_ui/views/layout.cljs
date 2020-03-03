(ns datasim-ui.views.layout
  (:require [re-mdl.core             :as mdl]
            [re-frame.core           :refer [subscribe dispatch]]
            [datasim-ui.functions    :as fns]
            [datasim-ui.views.form   :as form]
            [datasim-ui.views.editor :as editor]
            [datasim-ui.views.dialog :as dialog]))

(defn content
  []
  [mdl/grid
   :children
   (into [[mdl/cell
           :class "header-container"
           :col   12
           :children
           [[:div
             [:input#import-input.hidden-button
              {:type     "file"
               :onChange (fn [e]
                           (fns/import-file e :input/all))}]
             [:button.minorbutton
              {:on-click (fn [e]
                           (fns/import-url e :input/all))}
              "Import from URL"]
             [:button.minorbutton
              {:on-click (fn [e]
                           (fns/click-input e "import-input"))}
              "Import File"]
             [:button.minorbutton
              {:on-click (fn [e]
                           (fns/export-file e
                                            (let [profiles   (js/JSON.parse @(subscribe [:input/profiles]))
                                                  personae   (js/JSON.parse @(subscribe [:input/personae]))
                                                  alignments (js/JSON.parse @(subscribe [:input/alignments]))
                                                  parameters (js/JSON.parse @(subscribe [:input/parameters]))
                                                  json       #js {"profiles"   profiles
                                                                  "personae"   personae
                                                                  "alignments" alignments
                                                                  "parameters" parameters}]
                                              (js/Blob. [(js/JSON.stringify json)]
                                                        clj->js {:type "application/json"}))
                                            "input.json"))}
              "Export File"]
             [:div.spacer]
             [:button.mdc-icon-button.material-icons
              {:on-click (fn [e]
                           (fns/ps-event e)
                           (dispatch [:options/toggle]))}
              "settings"]
             [:button.majorbutton
              {:type "submit"}
              "Run"]]]]
          [form/options]]
         (if-let [sub-key @(subscribe [:db/focus])]
           [[editor/editor-max sub-key]]
           [[editor/editor-min :input/profiles]
            [editor/editor-min :input/personae]
            [editor/editor-min :input/alignments]
            [editor/editor-min :input/parameters]]))])

(defn top-menu
  []
  [:div.top-menu
   [:a {:href "#/"}  
    [:img {:src "img/datasim_logo.png"}]]
   [:a {:href "https://github.com/yetanalytics/datasim"} "Contribute on GitHub"]])

(defn page
  []
  [:div "HI"])

(defn snackbar
  []
  [:div.mdc-snackbar.datasim-snackbar
   {:aria-live "assertive"
    :aria-atomic "true"
    :aria-hidden "true"}
   [:div.mdc-snackbar__text]
   [:div.mdc-snackbar__action-wrapper
    [:button.mdc-snackbar__action-button
     {:type "button"}]]])

(defn footer
  "The footer at the bottom of the app."
  []
  [:footer
   [:a {:href "#"} [:img {:src "img/datasim_logo_sml.png"}]]
   [:div.spacer]
   [:a#license {:href "https://github.com/yetanalytics/datasim/blob/master/LICENSE"}
    "License"]])

(defn layout
  []
  [:div.datasim-app
   [:div.datasim-app-body
    [top-menu] 
    [form/form
     [content]]
    [dialog/dialog-container]]
   [footer]
   [snackbar]])
