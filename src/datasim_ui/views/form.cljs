(ns datasim-ui.views.form
  (:require [goog.crypt.base64           :refer [encodeString]]
            [re-frame.core               :refer [subscribe dispatch]]
            [re-codemirror.core          :as cm]
            [datasim-ui.env              :refer [api]]
            [datasim-ui.functions        :as fns]
            [datasim-ui.util             :as util]
            [datasim-ui.views.textfield  :as textfield]
            [datasim-ui.views.checkbox   :as checkbox]
            [datasim-ui.views.snackbar   :refer [snackbar!]]
            [cljsjs.codemirror.mode.javascript]
            [cljsjs.codemirror.addon.lint.javascript-lint]
            [cljsjs.codemirror.addon.lint.json-lint]
            [cljsjs.codemirror.addon.edit.matchbrackets]
            [cljsjs.codemirror.addon.edit.closebrackets]))

(defn form
  [body]
  [:div
   [:form#generate-form
    ;; to properly send a form with basic auth, a regular form cant be used.
    ;; Use an XHR instead so headers can be set.
    {:encType  "multipart/form-data"
     :onSubmit (fn [e]
                 (fns/ps-event e)
                 ;; Generate form data directly from the form itself
                 (let [form-data (js/FormData.)
                       xhr       (js/XMLHttpRequest.)
                       username  @(subscribe [:options/username])
                       password  @(subscribe [:options/password])]
                   (.append form-data "profiles" @(subscribe [:input/profiles]))
                   (.append form-data "personae" @(subscribe [:input/personae]))
                   (.append form-data "alignments" @(subscribe [:input/alignments]))
                   (.append form-data "parameters" @(subscribe [:input/parameters]))
                   (.append form-data "lrs-endpoint" @(subscribe [:options/endpoint]))
                   (.append form-data "api-key" @(subscribe [:options/api-key]))
                   (.append form-data "api-secret-key" @(subscribe [:options/api-secret-key]))
                   (.append form-data "send-to-lrs" @(subscribe [:options/send-to-lrs]))
                   (.open xhr "POST" (str api "/generate"))
                   ;; Only attach auth info if provided
                   (when-not (or (clojure.string/blank? username)
                                 (clojure.string/blank? password))
                     (let [auth (encodeString (str username ":" password))]
                       (.setRequestHeader xhr "Authorization" (str "Basic " auth))))
                   (.setRequestHeader xhr "Access-Control-Allow-Origin" "*")
                   ;; XHR needs to be a blob to be able to download
                   (set! (.-responseType xhr) "blob")
                   (set! (.-onload xhr) (fn [e]
                                          ;; Handle possible auth errors,
                                          ;; or just download the blob result
                                          (condp = (.. e -target -status)
                                            401 (do
                                                  (dispatch [:options/show])
                                                  (snackbar! "No Datasim API Credentials Provided"))
                                            403 (do
                                                  (dispatch [:options/show])
                                                  (snackbar! "Invalid Datasim API Credentials"))
                                            200 (fns/export-file e
                                                                 (.. e -target -response)
                                                                 "simulation.json"))))
                   (.send xhr form-data)))}
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
  [key & {:keys [form?]
          :or   {form? false}}]
  (cond-> [textfield/textfield
           :id        (util/input-name key)
           :label     (util/label key)
           :value     @(subscribe [key])
           :on-change (fn [e]
                        (fns/ps-event e)
                        (dispatch [key (.. e -target -value)]))]
    form? (conj :name (util/input-name key))))

(defn options
  []
  [:div.cell-12
   [:div
    {:class (cond-> "options"
              @(subscribe [:options/visible])
              (str " visible"))}
    [:div.grid-inner
     [:div.cell-6
      [:h4 "Credentials"]
      [textfield :options/username :form? false]
      [textfield :options/password :form? false]]
     [:div.cell-6
      [:h4 "Run Options"]
      [textfield :options/endpoint]
      [textfield :options/api-key]
      [textfield :options/api-secret-key]

      [checkbox/checkbox
       :name  "send-to-lrs"
       :label "Send Statements to LRS"]]]]])
