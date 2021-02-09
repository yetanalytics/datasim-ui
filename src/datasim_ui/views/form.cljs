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
                   (.append form-data "profiles" @(subscribe [:input/get-data :input/profiles]))
                   (.append form-data "personae" @(subscribe [:input/get-data :input/personae]))
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
     "The JSON for this input is not valid and must be fixed or removed before using this edit mode."])
  )

(defmulti edit-form (fn [key mode]
                      [key (:mode mode)]))

;; Default to error message
(defmethod edit-form :default [_ _]
  [:p
   "No Editor For This Mode"])

(defmethod edit-form [:input/parameters :advanced] [key mode]
  [:div
   [:div.advanced
    [textarea key]]])

(defmethod edit-form [:input/personae :advanced] [key mode]
  [:div
   [:div.advanced
    [textarea key]]])

(defmethod edit-form [:input/alignments :advanced] [key mode]
  [:div
   [:div.advanced
    [textarea key]]])

(defmethod edit-form [:input/profiles :advanced] [key mode]
  [:div
   [:div.advanced
    [textarea key]]])

(defmethod edit-form [:input/parameters :basic] [key mode]
  [:div.edit-basic
   [textfield/textfield
    :id        "input.parameters.start"
    :label     "Start Time"
    :value     @(subscribe [:input/get-value :input/parameters :start])
    :on-change (fn [e]
                 (fns/ps-event e)
                 (dispatch [:input/set-value key (.. e -target -value) :start]))]
   [textfield/textfield
    :id        "input.parameters.end"
    :label     "End Time"
    :value     @(subscribe [:input/get-value :input/parameters :end])
    :on-change (fn [e]
                 (fns/ps-event e)
                 (dispatch [:input/set-value key (.. e -target -value) :end]))]
   [textfield/textfield
    :id        "input.parameters.timezone"
    :label     "Timezone"
    :value     @(subscribe [:input/get-value :input/parameters :timezone])
    :on-change (fn [e]
                 (fns/ps-event e)
                 (dispatch [:input/set-value key (.. e -target -value) :timezone]))]
   [textfield/textfield
    :id        "input.parameters.seed"
    :label     "Simulation Seed"
    :value     @(subscribe [:input/get-value :input/parameters :seed])
    :on-change (fn [e]
                 (fns/ps-event e)
                 (dispatch [:input/set-value key (.. e -target -value) :seed]))]])

(defmethod edit-form [:input/personae :basic] [key mode]
  (if-valid
      key
    [:div.edit-basic
     [textfield/textfield
      :id        "input.personae.name"
      :label     "Name"
      :value     @(subscribe [:input/get-value key :name])
      :on-change (fn [e]
                   (fns/ps-event e)
                   (dispatch [:input/set-value key (.. e -target -value) :name]))]
     [dropdown/dropdown
      :id        "input.personae.typedrop"
      :label     "Type"
      :value     @(subscribe [:input/get-value key :objectType])
      :options   [{:value "Group"
                   :display "Group"}]
      :on-change (fn [e]
                   (fns/ps-event e)
                   (dispatch [:input/set-value key (.. e -target -value)
                              :objectType]))]
     [:h5
      "Members"]
     [:div.cardlist-container
      (for [member-index (range (count @(subscribe [:input/get-value key :member])))]
        (let [[ifi-key ifi-value] @(subscribe [:input/get-ifi key :member member-index])]
          [:div.mdc-card.mdc-card--outlined
           {:key (str "personae-member-" member-index)}
           [textfield/textfield
            :id        (str "input.personae.member." member-index ".name")
            :label     "Member Name"
            :value     @(subscribe [:input/get-value key :member member-index :name])
            :on-change (fn [e]
                         (fns/ps-event e)
                         (dispatch [:input/set-value key (.. e -target -value)
                                    :member member-index :name]))]
           [textfield/textfield
            :id        (str "input.personae.member." member-index ".role")
            :label     "Role (optional)"
            :value     @(subscribe [:input/get-value key :member member-index :role])
            :on-change (fn [e]
                         (fns/ps-event e)
                         (dispatch [:input/set-value key (.. e -target -value)
                                    :member member-index :role]))]
           [dropdown/dropdown
            :id        "input.personae.member." member-index ".ifi.key"
            :label     "Identifier Type"
            :value     (name ifi-key)
            :options   [{:value "mbox"
                         :display "MBox"}
                        {:value "mbox_sha1sum"
                         :display "MBox Checksum"}
                        {:value "openid"
                         :display "OpenID"}
                        {:value "account"
                         :display "Account"}]
            :on-change (fn [e]
                         (fns/ps-event e)
                         (let [val (keyword (.. e -target -value))]
                           (dispatch [:input/set-ifi key [val
                                                          (if (not= val ifi-key)
                                                            (if (= val :account)
                                                              {}
                                                              "")
                                                            ifi-value)]
                                      :member member-index])))]
           (if (= ifi-key :account)
             [:div
              [textfield/textfield
               :id        (str "input.personae.member." member-index ".ifi.account.homePage")
               :label     "Account Homepage"
               :value     (:homePage ifi-value)
               :on-change (fn [e]
                            (fns/ps-event e)
                            (dispatch [:input/set-ifi key [ifi-key {:homePage (.. e -target -value)
                                                                    :name (:name ifi-value)}]
                                       :member member-index]))]
              [textfield/textfield
               :id        (str "input.personae.member." member-index ".ifi.account.name")
               :label     "Account Name"
               :value     (:name ifi-value)
               :on-change (fn [e]
                            (fns/ps-event e)
                            (dispatch [:input/set-ifi key [ifi-key {:homePage (:homePage ifi-value)
                                                                    :name (.. e -target -value)}]
                                       :member member-index]))]]
             [textfield/textfield
              :id        (str "input.personae.member." member-index ".ifi.value")
              :label     (name ifi-key)
              :value     ifi-value
              :on-change (fn [e]
                           (fns/ps-event e)
                           (dispatch [:input/set-ifi key [ifi-key (.. e -target -value)]
                                      :member member-index]))])
           [:span.element-button
            [:span.mdc-tab__icon.material-icons.clickable
             {:on-click (fn [e]
                          (fns/ps-event e)
                          (dispatch [:input/remove-element key member-index :member]))}
             "remove_circle"]
            "Remove Member"]]))
      [:span.element-button
       [:span.mdc-tab__icon.material-icons.clickable
        {:on-click (fn [e]
                     (fns/ps-event e)
                     (dispatch [:input/add-element key {:mbox ""} :member]))}
        "add_box"]
       "Add Member"]]]))


(defmethod edit-form [:input/alignments :basic] [key mode]
  (if-valid
      key
    [:div.edit-basic
     [:h5
      "Alignments"]
     (let [agent-options (into [{:value "" :display "Select Actor"}]
                               (mapv (fn [actor]
                                       {:value actor
                                        :display actor})
                                     @(subscribe [:input/get-actor-ifis {}])))
           group-options (into [{:value "" :display "Select Group"}]
                               (mapv (fn [actor]
                                       {:value actor
                                        :display actor})
                                     @(subscribe [:input/get-actor-groups {}])))
           role-options (into [{:value "" :display "Select Role"}]
                               (mapv (fn [actor]
                                       {:value actor
                                        :display actor})
                                     @(subscribe [:input/get-actor-roles {}])))
           profile-options (into [{:value "" :display "Select Component"}]
                                 (mapv (fn [comp]
                                         {:value comp
                                          :display comp})
                                       @(subscribe [:input/get-profile-iris {}])))]
       [:div.cardlist-container
        (for [a-index (range (count @(subscribe [:input/get-parsed-data key])))]
          [:div.mdc-card.mdc-card--outlined
           {:key (str "alignment-card-" a-index)}
           [dropdown/dropdown
            :id        (str "input.alignments." a-index ".id")
            :label     "Alignment Type"
            :value     @(subscribe [:input/get-value key a-index :type])
            :options   (cond-> [{:value "Agent"
                                 :display "Agent"}
                                {:value "Group"
                                 :display "Group"}
                                {:value "Role"
                                 :display "Role"}])
            :on-change (fn [e]
                         (fns/ps-event e)
                         (dispatch [:input/set-value key (.. e -target -value)
                                    a-index :type]))]
           (case @(subscribe [:input/get-value key a-index :type])
             "Agent"
             [dropdown/dropdown
              :id        (str "input.alignments." a-index ".id")
              :label     "Agent"
              :value     @(subscribe [:input/get-value key a-index :id])
              :options   agent-options
              :on-change (fn [e]
                           (fns/ps-event e)
                           (dispatch [:input/set-value key (.. e -target -value)
                                      a-index :id]))]
             "Group"
             [dropdown/dropdown
              :id        (str "input.alignments." a-index ".id")
              :label     "Group"
              :value     @(subscribe [:input/get-value key a-index :id])
              :options   group-options
              :on-change (fn [e]
                           (fns/ps-event e)
                           (dispatch [:input/set-value key (.. e -target -value)
                                      a-index :id]))]
             "Role"
             [dropdown/dropdown
              :id        (str "input.alignments." a-index ".id")
              :label     "Role"
              :value     @(subscribe [:input/get-value key a-index :id])
              :options   role-options
              :on-change (fn [e]
                           (fns/ps-event e)
                           (dispatch [:input/set-value key (.. e -target -value)
                                      a-index :id]))])
           [:div.cardlist-container
            (for [c-index (range (count @(subscribe [:input/get-value key a-index
                                                     :alignments])))]
              [:div.mdc-card.mdc-card--outlined
               {:key (str "alignment-" a-index "-component-" c-index)}
               [dropdown/dropdown
                :id        (str "input.alignments."a-index".alignments." c-index
                                ".component")
                :label     "Component"
                :value     @(subscribe [:input/get-value key a-index :alignments
                                        c-index :component])
                :options   profile-options
                :on-change (fn [e]
                             (fns/ps-event e)
                             (dispatch [:input/set-value key (.. e -target -value)
                                        a-index :alignments c-index :component]))]
               [textfield/numeric
                :id        (str "input.alignments."a-index".alignments." c-index
                                ".weight")
                :label     "Weight"
                :step      "0.01"
                :max       "1.00"
                :min       "-1.00"
                :value     @(subscribe [:input/get-value key a-index :alignments
                                        c-index :weight])
                :on-change (fn [e]
                             (fns/ps-event e)
                             (dispatch [:input/set-value key (js/parseFloat (.. e -target -value))
                                        a-index :alignments c-index :weight]))]
               [:span.element-button
                [:span.mdc-tab__icon.material-icons.clickable
                 {:on-click (fn [e]
                              (fns/ps-event e)
                              (dispatch [:input/remove-element key c-index
                                         a-index :alignments]))}
                 "remove_circle"]
                "Remove Component"]])
            [:span.element-button
             [:span.mdc-tab__icon.material-icons.clickable
              {:on-click (fn [e]
                           (fns/ps-event e)
                           (dispatch [:input/add-element key {}
                                      a-index :alignments]))}
              "add_box"]
             "Add Component"]]
           [:span.element-button
            [:span.mdc-tab__icon.material-icons.clickable
             {:on-click (fn [e]
                          (fns/ps-event e)
                          (dispatch [:input/remove-element key a-index]))}
             "remove_circle"]
            "Remove Alignment"]
           ])
        [:span.element-button
         [:span.mdc-tab__icon.material-icons.clickable
          {:on-click (fn [e]
                       (fns/ps-event e)
                       (dispatch [:input/add-element key {:type "Agent"}]))}
          "add_box"]
         "Add Alignment"]])]))
