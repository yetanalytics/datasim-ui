(ns datasim-ui.views.form
  (:require [re-frame.core               :refer [subscribe dispatch]]
            [re-codemirror.core          :as cm]
            [datasim-ui.functions        :as fns]
            [datasim-ui.util             :as util]
            [datasim-ui.views.textfield  :as textfield]
            [datasim-ui.views.checkbox   :as checkbox]
            [cljsjs.codemirror.mode.javascript]
            [cljsjs.codemirror.addon.lint.javascript-lint]
            [cljsjs.codemirror.addon.lint.json-lint]
            [cljsjs.codemirror.addon.edit.matchbrackets]
            [cljsjs.codemirror.addon.edit.closebrackets]))

(defn form
  [body]
  [:div
   [:form#generate-form
    {:action  "http://127.0.0.1:9090/api/v1/generate"
     :method  "post"
     :encType "multipart/form-data"}
    body]])

(defn textarea
  [key]
  [cm/codemirror
   {:mode              "application/json"
    :theme             "default"
    :lineNumbers       true
    :lineWrapping      true
    :matchBrackets     true
    :autoCloseBrackets true
    :gutters           ["CodeMirror-link-markers"]
    :lint              true}
   {:name   (util/input-name key)
    :value  @(subscribe [key])
    :events {"change" (fn [this [cm obj]]
                        (dispatch [key (.getValue cm)]))}}])

(defn textfield
  [key]
  [textfield/textfield
   :name      (util/input-name key)
   :label     (util/label key)
   :value     @(subscribe [key])
   :on-change (fn [e]
                (fns/ps-event e)
                (dispatch [key (.. e -target -value)]))])

(defn options
  []
  [:div.cell-12
   [:div
    {:class (cond-> "options"
              @(subscribe [:options/visible])
              (str " visible"))}
    [:h6  "Run Options"]
    [textfield :options/endpoint]
    [textfield :options/api-key]
    [textfield :options/api-secret-key]
    [checkbox/checkbox
     :name  "send-to-lrs"
     :label "Send Statements to LRS"]]])
