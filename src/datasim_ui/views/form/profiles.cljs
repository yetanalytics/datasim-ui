(ns datasim-ui.views.form.profiles
  (:require [re-frame.core               :refer [subscribe dispatch]]
            [re-codemirror.core          :as cm]
            [datasim-ui.functions        :as fns]
            [datasim-ui.views.form       :as form]))

(defmethod form/edit-form [:input/profiles :profile-tabs] [key mode]
  [:div
   [:div.advanced
    {:key (str "profile-editor-" (name (:mode mode)))}
    [cm/codemirror
     {:mode              "application/json"
      :theme             "default"
      :lineNumbers       true
      :lineWrapping      true
      :matchBrackets     true
      :autoCloseBrackets true
      :gutters           ["CodeMirror-link-markers"]
      :lint              true}
     {:name   (name (:mode mode))
      :value  @(subscribe (into [] (concat
                                    [:input/get-value-json key]
                                    (:address mode))))
      :events {"change" (fn [this [cm obj]]
                          (dispatch
                           (into []
                                 (concat
                                  [:input/set-value-json key (.getValue cm)]
                                  (:address mode)))))}}]]])
