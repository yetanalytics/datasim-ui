(ns datasim-ui.views.form
  (:require [goog.crypt.base64           :refer [encodeString]]
            [re-frame.core               :refer [subscribe dispatch]]
            [re-codemirror.core          :as cm]
            [datasim-ui.env              :refer [api]]
            [clojure.edn                 :as edn]
            [datasim-ui.functions        :as fns]
            [datasim-ui.util             :as util]
            [datasim-ui.views.textfield  :as textfield]
            [datasim-ui.views.dropdown   :as dropdown]
            [datasim-ui.views.checkbox   :as checkbox]
            [datasim-ui.views.snackbar   :refer [snackbar!]]
            [cljsjs.codemirror.mode.javascript]
            [cljsjs.codemirror.addon.lint.javascript-lint]
            [cljsjs.codemirror.addon.lint.json-lint]
            [cljsjs.codemirror.addon.edit.matchbrackets]
            [cljsjs.codemirror.addon.edit.closebrackets]
            [clojure.pprint              :refer [pprint]]))

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
                   (.append form-data "profiles" @(subscribe [:input/get-data-vector :input/profiles]))
                   (.append form-data "personae-array" @(subscribe [:input/get-data :input/personae]))
                   (.append form-data "alignments" @(subscribe [:input/get-data :input/alignments]))
                   (.append form-data "parameters" @(subscribe [:input/get-data :input/parameters]))
                   (.append form-data "lrs-endpoint" @(subscribe [:options/endpoint]))
                   (.append form-data "api-key" @(subscribe [:options/api-key]))
                   (.append form-data "api-secret-key" @(subscribe [:options/api-secret-key]))
                   (.append form-data "send-to-lrs" (.-checked (aget (js/document.getElementsByName "send-to-lrs")
                                                                     0)))
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
                                            400 (do
                                                  (dispatch [:validation/show])
                                                  (dispatch [:options/hide])
                                                  (-> (js/Promise.resolve
                                                       (.text (.. e -target -response)))
                                                      (.then #(dispatch [:validation/data
                                                                         (edn/read-string %)])))
                                                  (snackbar! "Validation Errors Detected, see Validation Results"))
                                            401 (do
                                                  (dispatch [:options/show])
                                                  (snackbar! "No Datasim API Credentials Provided"))
                                            403 (do
                                                  (dispatch [:options/show])
                                                  (snackbar! "Invalid Datasim API Credentials"))
                                            200 (do
                                                  (dispatch [:validation/hide])
                                                  (fns/export-file e
                                                                   (.. e -target -response)
                                                                   "simulation.json")))))
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
    :value  @(subscribe [:input/get-data key])
    :events {"change" (fn [this [cm obj]]
                        (dispatch [:input/set-data key (.getValue cm)]))}}])

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

(defn validation
  []
  [:div.cell-12
   [:div
    {:class (cond-> "validation"
              @(subscribe [:validation/visible])
              (str " visible"))}
    [:div.grid-inner
     [:div.cell-12
      [:h3 "Validation Results"]
      (for [error @(subscribe [:validation/data])]
        [:div {:class "validation-result" :key (str "error-result-"
                                                    (:error/id error))}
         [:span
          [:a {:on-click #(dispatch [:validation/toggle-error (:error/id error)])}
           (str (if (:error/visible error) "^" ">") " Error Detected in: "
                (:error/path error))]]
         [:div {:class (cond-> "validation-details"
                          (:error/visible error) (str " visible"))}
          (str (:error/text error))]])]]]])


(defn if-valid
  [key form]
  (if @(subscribe [:input/get-valid key])
    form
    [:p
     "The JSON for this input is not valid and must be fixed or removed before using this edit mode."]))

(defmulti edit-form (fn [key mode]
                      [key (or
                            (:mode-type mode)
                            (:mode mode))]))

;; Default to error message
(defmethod edit-form :default [_ _]
  [:p
   "No Editor For This Mode"])
