(ns datasim-ui.views.layout
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [re-mdl.core             :as mdl]
            [re-frame.core           :refer [subscribe dispatch]]
            [cljs-http.client        :as http]
            [cljs.core.async         :refer [<!]]
            [datasim-ui.functions    :as fns]
            [datasim-ui.views.form   :as form]
            [datasim-ui.views.editor :as editor]))

(defn content
  []
  (let [url "https://raw.githubusercontent.com/yetanalytics/datasim/master/dev-resources/personae/simple.json"]
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
               [mdl/button
                :raised?        true
                :colored?       true
                :ripple-effect? true
                :child          "Import from URL"
                :on-click       (fn [e]
                                  (.preventDefault e)
                                  (.stopPropagation e)
                                  (dispatch [:dialog/open])
                                  #_(go (let [response (<! (http/get "http://localhost:9090/api/v1/download-url"
                                                                   {:with-credentials? false
                                                                    :headers           {"Access-Control-Allow-Origin" "*"}
                                                                    :query-params      {"url" (js/encodeURIComponent url)}}))]
                                        (println (:body response)))))]
               [mdl/button
                :raised?        true
                :colored?       true
                :ripple-effect? true
                :child          "Import"
                :on-click       (fn [e]
                                  (fns/click-input e "import-input"))]
               [mdl/button
                :raised?        true
                :colored?       true
                :ripple-effect? true
                :child          "Export"
                :on-click       (fn [e]
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
                                                   "input.json"))]
               [:div.spacer]
               [mdl/button
                :icon?    true
                :child    [:i.material-icons "settings"]
                :on-click (fn [e]
                            (fns/ps-event e)
                            (dispatch [:options/toggle]))]
               [mdl/button
                :attr           {:type "submit"}
                :raised?        true
                :accent?        true
                :ripple-effect? true
                :child          "Run"]]]]
            [form/options]]
           (if-let [sub-key @(subscribe [:db/focus])]
             [[editor/editor-max sub-key]]
             [[editor/editor-min :input/profiles]
              [editor/editor-min :input/personae]
              [editor/editor-min :input/alignments]
              [editor/editor-min :input/parameters]]))]))

(defn dialog
  []
  [mdl/dialog
   :children
   [[mdl/dialog-title
     :child "Import from URL"]
    [mdl/dialog-content
     :children
     [[:p "hi"]]]
    [mdl/dialog-actions
     :children
     [[mdl/button
       :child    "Cancel"
       :on-click #(dispatch [:dialog/close])]
      [mdl/button
       :child    "Import"
       :on-click (fn [e]
                   (.preventDefault e)
                   (.stopPropagation e)
                   (dispatch [:dialog/close]))]]]]])

(defn layout
  []
  [mdl/layout
   :fixed-header? true
   :children
   [[mdl/layout-header
     :children
     [[mdl/layout-header-row
       :children
       [[mdl/layout-title
         :label "Datasim"]
        [mdl/layout-spacer]]]]]
    [mdl/layout-drawer
     :children
     [[mdl/layout-title
       :label "Datasim"]
      [mdl/layout-nav
       :children
       [[mdl/layout-nav-link
         :content "Demo 1"]]]]]
    [mdl/layout-content
     :children
     [[form/form
       [content]]
      (when @(subscribe [:dialog/open])
        [dialog])]]]])
