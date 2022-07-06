(ns datasim-ui.views.layout
  (:require [re-frame.core             :refer [subscribe dispatch]]
            [datasim-ui.functions      :as fns]
            [datasim-ui.views.form     :as form]
            [datasim-ui.views.editor   :as editor]
            [datasim-ui.views.dialog   :as dialog]
            [datasim-ui.views.snackbar :refer [snackbar]]
            [datasim-ui.util           :as util]))

(defn content
  []
  [:div.grid
   (into [:div.grid-inner
          [:div.cell-12
           [:div.flex-container
            [:input#import-input.hidden-button
             {:type     "file"
              :onChange (fn [e]
                          (fns/import-file e :input/all))}]            
            [:button.minorbutton
             {:on-click (fn [e]
                          (fns/click-input e "import-input"))}
             "Import File"]
            [:button.minorbutton
             {:on-click (fn [e]
                          (fns/export-file e
                                           (let [profiles   (js/JSON.parse @(subscribe [:input/get-data-vector :input/profiles]))
                                                 personae   (js/JSON.parse @(subscribe [:input/get-data :input/personae]))
                                                 alignments (js/JSON.parse @(subscribe [:input/get-data :input/alignments]))
                                                 parameters (js/JSON.parse @(subscribe [:input/get-data :input/parameters]))
                                                 json       #js {"profiles"       profiles
                                                                 "personae-array" personae
                                                                 "alignments"     alignments
                                                                 "parameters"     parameters}]
                                             (js/Blob. [(js/JSON.stringify json)]
                                                       util/clj-to-json {:type "application/json"}))
                                           "input.json"))}
             "Export File"]
            [:div.spacer]
            [:button.mdc-icon-button.material-icons.icon
             {:on-click (fn [e]
                          (fns/ps-event e)
                          (dispatch [:options/toggle]))}
             "settings"]
            [:button.majorbutton
             {:type "submit"}
             "Run"]]]
          [form/options]
          [form/validation]]
         [[editor/editor :input/profiles]
          [editor/editor :input/personae]
          [editor/editor :input/alignments]
          [editor/editor :input/parameters]])])

(defn top-menu
  []
  [:div.top-menu
   [:a {:href "#/"}
    [:img {:src "img/datasim_logo.png"}]]
   [:a {:href "https://github.com/yetanalytics/datasim"} "Contribute on GitHub"]])

(defn page
  []
  [:div "HI"])

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
