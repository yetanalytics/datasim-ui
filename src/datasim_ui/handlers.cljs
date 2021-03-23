(ns datasim-ui.handlers
  (:require [re-frame.core             :as re-frame]
            [datasim-ui.views.snackbar :refer [snackbar!]]
            [datasim-ui.db             :as db :refer [check-spec-interceptor]]
            [clojure.pprint            :refer [pprint]]
            [datasim-ui.util           :as util]))

(def global-interceptors
  [check-spec-interceptor])

(re-frame/reg-event-db
 :db/initialize
 global-interceptors
 (fn [db _]
   (-> db
       (assoc ::db/options
              {:options/visible     false
               :options/send-to-lrs false})
       (assoc ::db/dialog
              {:dialog/open false})
       (assoc ::db/validation
              {:validation/visible false})
       (assoc ::db/input
              {:input/parameters
               {:input-modes [{:mode     :basic
                               :display  "Basic"
                               :icon     "build"
                               :selected false}
                              {:mode     :advanced
                               :display  "Advanced"
                               :icon     "code"
                               :selected true}]}
               :input/alignments
               {:input-modes [{:mode     :basic
                               :display  "Basic"
                               :icon     "build"
                               :selected false}
                              {:mode     :advanced
                               :display  "Advanced"
                               :icon     "code"
                               :selected true}]
                :input-data  "[]"}
               :input/personae
               {:input-modes [{:mode     :basic
                               :display  "Basic"
                               :icon     "build"
                               :selected false}
                              {:mode     :advanced
                               :display  "Advanced"
                               :icon     "code"
                               :selected true}]
                :input-data "[]"}
               :input/profiles
               {:input-modes [{:mode          :default
                               :mode-type     :profile-tabs
                               :display       "Untitled 1"
                               :icon          "code"
                               :selected      true
                               :index         0}]
                :input-data  ["{}"]}}))))

(re-frame/reg-event-db
 :input/set-modes
 global-interceptors
 (fn [db [_ input-key modes]]
   (assoc-in db [::db/input input-key :input-modes]
             modes)))

(re-frame/reg-event-db
 :input/set-selected-mode
 global-interceptors
 (fn [db [_ input-key mode-key]]
   (let [modes (get-in db [::db/input input-key :input-modes])]
     (assoc-in db [::db/input input-key :input-modes]
               (map (fn [mode]
                      (assoc-in mode [:selected] (= (:mode mode) mode-key)))
                    modes)))))



(re-frame/reg-event-fx
 :input/set-data
 global-interceptors
 (fn [{:keys [event db]} [_ input-key data]]
   ;; If profiles, also trigger header update event
   (cond->
       {:db (assoc-in db [::db/input input-key :input-data] data)}
        (= input-key :input/profiles)
        (conj {:dispatch [:input/update-profile-headers]}))))

(re-frame/reg-event-fx
 :input/set-all
 (fn [{:keys [event db]} [_ data]]
   (try
     (let [json       (js/JSON.parse data)
           profiles   (js/JSON.stringify (.. json -profiles))
           personae   (js/JSON.stringify (.. json -personae))
           alignments (js/JSON.stringify (.. json -alignments))
           parameters (js/JSON.stringify (.. json -parameters))]
       {:dispatch-n [[:input/import-vector :input/profiles profiles]
                     [:input/set-data :input/personae personae]
                     [:input/set-data :input/alignments alignments]
                     [:input/set-data :input/parameters parameters]]})
     (catch js/Error. e
       (do
         (snackbar! "Error parsing JSON Input")
         db)))))

(re-frame/reg-event-fx
 :input/set-value
 (fn [{:keys [event db]} [_ input-key value & address]]
   (try
     (let [input-json (get-in db [::db/input input-key :input-data])
           data       (or (util/json-to-clj (js/JSON.parse input-json)
                                            :keywordize-keys true)
                          {})
           new-data   (assoc-in data address value)
           new-json   (js/JSON.stringify (util/clj-to-json new-data) nil 2)]
       {:dispatch [:input/set-data input-key new-json]})
     (catch js/Error. e
       (do
         (pprint ["Parse Problem!" e])
         db)))))

(re-frame/reg-event-fx
 :input/set-ifi
 (fn [{:keys [event db]} [_ input-key ifi & address]]
   (try
     (let [input-json (get-in db [::db/input input-key :input-data])
           data       (or (util/json-to-clj (js/JSON.parse input-json)
                                            :keywordize-keys true)
                          {})
           new-member (assoc (dissoc (get-in data address)
                                     :mbox :mbox_sha1sum :openid :account)
                             (first ifi) (second ifi))
           new-data   (assoc-in data address new-member)
           new-json   (js/JSON.stringify (util/clj-to-json new-data) nil 2)]
       {:dispatch [:input/set-data input-key new-json]})
     (catch js/Error. e
       (do
         (pprint ["Parse Problem!" e])
         db)))))

(re-frame/reg-event-fx
 :input/add-element
 (fn [{:keys [event db]} [_ input-key value & address]]
   (try
     (let [input-json (get-in db [::db/input input-key :input-data])
           data       (or (util/json-to-clj (js/JSON.parse input-json)
                                            :keywordize-keys true)
                          {})
           new-data   (if (= nil address)
                        (conj data value)
                        (assoc-in data address
                                  (conj (get-in data address) value)))
           new-json   (js/JSON.stringify (util/clj-to-json new-data) nil 2)]
       {:dispatch [:input/set-data input-key new-json]})
     (catch js/Error. e
       (do
         (pprint ["Parse Problem!" e])
         db)))))

(re-frame/reg-event-fx
 :input/remove-element
 (fn [{:keys [event db]} [_ input-key index & address]]
   (try
     (let [input-json (get-in db [::db/input input-key :input-data])
           data       (or (util/json-to-clj (js/JSON.parse input-json)
                                            :keywordize-keys true)
                          {})
           new-data   (if (= nil address)
                        (vec (concat (subvec data 0 index)
                                     (subvec data (inc index))))
                        (let [coll (get-in data address)]
                          (assoc-in data address
                                    (vec (concat (subvec coll 0 index)
                                                 (subvec coll (inc index)))))))
           new-json   (js/JSON.stringify (util/clj-to-json new-data) nil 2)]
       {:dispatch [:input/set-data input-key new-json]})
     (catch js/Error. e
       (do
         (pprint ["Parse Problem!" e])
         db)))))

;; Profile Related

(re-frame/reg-event-fx
 :input/set-value-vector
 (fn [{:keys [event db]} [_ input-key value index]]
   (let [data   (get-in db [::db/input input-key :input-data])]
     {:dispatch [:input/set-data input-key (assoc-in data [index] value)]})))

(re-frame/reg-event-fx
 :input/remove-element-vector
 (fn [{:keys [event db]} [_ input-key index]]
   (let [data (get-in db [::db/input input-key :input-data])]
     {:dispatch [:input/set-data input-key
                 (into [] (concat (subvec data 0 index)
                                  (subvec data (inc index))))]})))

(re-frame/reg-event-fx
 :input/add-element-vector
 (fn [{:keys [event db]} [_ input-key value]]
   (let [data (get-in db [::db/input input-key :input-data])]
     {:dispatch [:input/set-data input-key (conj data value)]})))


(re-frame/reg-event-db
 :input/update-profile-headers
 global-interceptors
 (fn [db _]
   (let [;;get currently selected tab
         selected
         (->
          (filter (fn [mode] (:selected mode))
                  (get-in db [::db/input :input/profiles :input-modes]))
          first
          :mode)
         ;;parse new tabs from data
         modes
         (into []
               (map-indexed
                (fn [idx profile]
                  (let [mode-key (keyword (str "profile-" idx))
                        profile-data (try
                                       (util/json-to-clj (js/JSON.parse profile)
                                                         :keywordize-keys true)
                                       (catch js/Error. e
                                         {}))
                        display  (if (contains? profile-data :prefLabel)
                                   (-> (vals (:prefLabel profile-data))
                                       shuffle
                                       first)
                                   (str "Untitled " (+ idx 1)))]
                    {:mode      mode-key
                     :mode-type :profile-tabs
                     :display   display
                     :selected  (= selected mode-key)
                     :icon      "code"
                     :index     idx}))
                (get-in db [::db/input :input/profiles :input-data])))]
     (assoc-in db [::db/input :input/profiles :input-modes]
               ;;if the old selected doesnt exist, just select first element
               (if (empty? (filter (fn [mode] (:selected mode)) modes))
                 (assoc-in modes [0 :selected] true)
                 modes)))))

(re-frame/reg-event-fx
 :input/import-vector
 global-interceptors
 (fn [_ [_ input-key data]]
   ;; If importing to a vector type like profiles, check if single or array
   (let [parsed (util/json-to-clj (js/JSON.parse data)
                                  :keywordize-keys true)]
     {:dispatch (if (vector? parsed)
                  ;;if vector convert contents to vector of strings and save
                  [:input/set-data input-key
                   (mapv (fn [profile]
                           (try
                             (js/JSON.stringify (util/clj-to-json profile)
                                                nil 2)
                             (catch js/Error. e
                               "{}")))
                         parsed)]
                  ;;if not just add contents to the end of existing proile vec
                  [:input/add-element-vector input-key data])})))

(re-frame/reg-event-db
 :focus/set
 global-interceptors
 (fn [db [_ tab]]
   (assoc db ::db/focus tab)))

(re-frame/reg-event-db
 :focus/clear
 global-interceptors
 (fn [db _]
   (dissoc db ::db/focus)))

(re-frame/reg-event-db
 :options/toggle
 global-interceptors
 (fn [db _]
   (update-in db [::db/options :options/visible]
              not)))

(re-frame/reg-event-db
  :validation/show
  global-interceptors
  (fn [db _]
    (assoc-in db [::db/validation :validation/visible]
              true)))

(re-frame/reg-event-db
 :validation/hide
 global-interceptors
 (fn [db _]
   (assoc-in db [::db/validation :validation/visible]
             false)))

(re-frame/reg-event-db
 :validation/data
 global-interceptors
 (fn [db [_ errors]]
   (assoc-in db [::db/validation :validation/data]
             (map (fn [error]
                    {:error/visible false
                     :error/id (random-uuid)
                     :error/path (:path error)
                     :error/text (:text error)})
                  errors))))

(re-frame/reg-event-db
 :validation/toggle-error
 global-interceptors
 (fn [db [_ error-id]]
   (update-in db [::db/validation :validation/data]
              (fn [errors]
                (map (fn [error]
                       (if (= error-id (:error/id error))
                         (merge error {:error/visible
                                       (not (:error/visible error))})
                         error))
                     errors)))))

(re-frame/reg-event-db
 :options/show
 global-interceptors
 (fn [db _]
   (assoc-in db [::db/options :options/visible]
             true)))

(re-frame/reg-event-db
 :options/hide
 global-interceptors
 (fn [db _]
   (assoc-in db [::db/options :options/visible]
             false)))

(re-frame/reg-event-db
 :options/endpoint
 global-interceptors
 (fn [db [_ endpoint]]
   (assoc-in db [::db/options :options/endpoint]
             endpoint)))

(re-frame/reg-event-db
 :options/api-key
 global-interceptors
 (fn [db [_ api-key]]
   (assoc-in db [::db/options :options/api-key]
             api-key)))

(re-frame/reg-event-db
 :options/api-secret-key
 global-interceptors
 (fn [db [_ api-secret-key]]
   (assoc-in db [::db/options :options/api-secret-key]
             api-secret-key)))

(re-frame/reg-event-db
 :options/send-to-lrs
 global-interceptors
 (fn [db _]
   (update-in db [::db/options :options/send-to-lrs]
              not)))

(re-frame/reg-event-db
 :options/username
 global-interceptors
 (fn [db [_ username]]
   (assoc-in db [::db/options :options/username]
             username)))

(re-frame/reg-event-db
 :options/password
 global-interceptors
 (fn [db [_ password]]
   (assoc-in db [::db/options :options/password]
             password)))

(re-frame/reg-event-db
 :dialog/open
 global-interceptors
 (fn [db [_ {:keys [title form save]}]]
   (assoc db ::db/dialog
          (cond-> {:dialog/open  true
                   :dialog/title title}
            form
            (assoc :dialog/form form)
            save
            (assoc :dialog/save save)))))

(re-frame/reg-event-db
 :dialog/close
 global-interceptors
 (fn [db _]
   (assoc-in db [::db/dialog :dialog/open] false)))

(re-frame/reg-event-db
 :dialog.form.text/set
 global-interceptors
 (fn [db [_ id text]]
   (assoc-in db [::db/dialog
                 :dialog/form
                 id
                 :text]
             text)))
