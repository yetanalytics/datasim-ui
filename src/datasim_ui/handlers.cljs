(ns datasim-ui.handlers
  (:require [re-frame.core             :as re-frame]
            [datasim-ui.views.snackbar :refer [snackbar!]]
            [datasim-ui.db             :as db :refer [check-spec-interceptor]]
            [clojure.pprint            :refer [pprint]]))

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
                               :selected true}]}
               :input/personae
               {:input-modes [{:mode     :basic
                               :display  "Basic"
                               :icon     "build"
                               :selected false}
                              {:mode     :advanced
                               :display  "Advanced"
                               :icon     "code"
                               :selected true}]}
               :input/profiles
               {:input-modes [{:mode     :advanced
                               :display  "Profile"
                               :icon     "code"
                               :selected true}]}}))))

(re-frame/reg-event-db
 :input/set-data
 global-interceptors
 (fn [db [_ input-key data]]
   (if (= input-key :input/all)
     (try
       (let [json       (js/JSON.parse data)
             profiles   (js/JSON.stringify (.. json -profiles))
             personae   (js/JSON.stringify (.. json -personae))
             alignments (js/JSON.stringify (.. json -alignments))
             parameters (js/JSON.stringify (.. json -parameters))]
         (-> db
             (assoc-in [::db/input :input/profiles :input-data] profiles)
             (assoc-in [::db/input :input/parameters :input-data] parameters)
             (assoc-in [::db/input :input/alignments :input-data] alignments)
             (assoc-in [::db/input :input/personae :input-data] personae)))
       (catch js/Error. e
         (do
           (snackbar! "Error parsing JSON Input")
           db)))
     (assoc-in db [::db/input input-key :input-data]
               data))))

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

(re-frame/reg-event-db
 :input/set-value
 global-interceptors
 (fn [db [_ input-key value & address]]
   (pprint "got here")
   (let [input-data (get-in db [::db/input input-key :input-data])
         debug0     (pprint "input data, value, address")
         json       (js/JSON.parse input-data)
         data       (js->clj json :keywordize-keys true)
         new-data   (assoc-in data address value)
         new-json   (js/JSON.stringify (clj->js new-data))
         debug1     (pprint [data new-data new-json])]
     (assoc-in db [::db/input input-key :input-data]
               new-json))))

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
