(ns datasim-ui.views.layout
  (:require [re-mdl.core           :as mdl]
            [datasim-ui.views.form :as form]))

(defn content
  []
  [mdl/grid
   :children
   [[mdl/cell
     :col 12
     :children
     [[:div.header-parent
       [:input#import-input
        {:type     "file"
         :name     "import-input"
         :onChange (fn [e]
                     (.preventDefault e)
                     (.stopPropagation e)
                     (let [text (.text (aget (.. e -currentTarget -files) 0))]
                       (.then (js/Promise.resolve text)
                              (fn [content]
                                (let [json       (js/JSON.parse content)
                                      profiles   (js/document.getElementById "textarea-profiles")
                                      personae   (js/document.getElementById "textarea-personae")
                                      alignments (js/document.getElementById "textarea-alignments")
                                      parameters (js/document.getElementById "textarea-parameters")]
                                  (set! (.-value profiles) (js/JSON.stringify (.. json -profiles)))
                                  (set! (.-value personae) (js/JSON.stringify (.. json -personae)))
                                  (set! (.-value alignments) (js/JSON.stringify (.. json -alignments)))
                                  (set! (.-value parameters) (js/JSON.stringify (.. json -parameters))))))))}]
       [mdl/button
        :raised?        true
        :colored?       true
        :ripple-effect? true
        :child          "Import"
        :on-click       (fn [e]
                          (.preventDefault e)
                          (.stopPropagation e))]
       [mdl/button
        :raised?        true
        :colored?       true
        :ripple-effect? true
        :child          "Export"
        :on-click       (fn [e]
                          (.preventDefault e)
                          (.stopPropagation e))]
       [:div.spacer]
       [mdl/button
        :icon? true
        :child [:i.material-icons "settings"]]
       [mdl/button
        :attr           {:type "submit"}
        :raised?        true
        :accent?        true
        :ripple-effect? true
        :child          "Run"]]]]
    [mdl/cell
     :col 6
     :children
     [[form/text-field 
       "profiles"
       "profiles"]]]
    [mdl/cell
     :col 6
     :children
     [[form/text-field
       "personae"
       "personae"]]]
    [mdl/cell
     :col 6
     :children
     [[form/text-field
       "alignments"
       "alignments"]]]
    [mdl/cell
     :col 6
     :children
     [[form/text-field
       "parameters"
       "parameters"]]]]])

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
       [content]]]]]])
