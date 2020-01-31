(ns datasim-ui.views.layout
  (:require [re-mdl.core           :as mdl]
            [re-frame.core         :refer [dispatch subscribe]]
            [datasim-ui.views.form :as form]))

(defn content
  []
  [mdl/grid
   :children
   [[mdl/cell
     :col 12
     :children
     [[:div.header-parent
       [:input#import-input.hidden-button
        {:type     "file"
         :name     "import-input"
         :onChange (fn [e]
                     (.preventDefault e)
                     (.stopPropagation e)
                     (.then (js/Promise.resolve
                             (.text (aget (.. e -currentTarget -files) 0)))
                            #(dispatch [:input/import %])))}]
       [mdl/button
        :raised?        true
        :colored?       true
        :ripple-effect? true
        :child          "Import"
        :on-click       (fn [e]
                          (.preventDefault e)
                          (.stopPropagation e)
                          (.click (js/document.getElementById "import-input")))]
       [mdl/button
        :raised?        true
        :colored?       true
        :ripple-effect? true
        :child          "Export"
        :on-click       (fn [e]
                          (.preventDefault e)
                          (.stopPropagation e)
                          (let [profiles   (js/JSON.parse @(subscribe [:input/profiles]))
                                personae   (js/JSON.parse @(subscribe [:input/personae]))
                                alignments (js/JSON.parse @(subscribe [:input/alignments]))
                                parameters (js/JSON.parse @(subscribe [:input/parameters]))
                                json       #js {"profiles"   profiles
                                                "personae"   personae
                                                "alignments" alignments
                                                "parameters" parameters}
                                blob       (js/Blob. [(js/JSON.stringify json)]
                                                     clj->js {:type "application/json"})
                                link       (js/document.createElement "a")]
                            (set! (.-download link) "input.json")
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
                            (.click link)))]
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
       :input/profiles]]]
    [mdl/cell
     :col 6
     :children
     [[form/text-field
       "personae"
       :input/personae]]]
    [mdl/cell
     :col 6
     :children
     [[form/text-field
       "alignments"
       :input/alignments]]]
    [mdl/cell
     :col 6
     :children
     [[form/text-field
       "parameters"
       :input/parameters]]]]])

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
