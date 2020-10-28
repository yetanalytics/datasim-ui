(ns datasim-ui.handlers
  (:require [re-frame.core             :as re-frame]
            [datasim-ui.views.snackbar :refer [snackbar!]]
            [datasim-ui.db             :as db :refer [check-spec-interceptor]]))

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
              {:validation/visible false}))))

(re-frame/reg-event-db
 :input/all
 global-interceptors
 (fn [db [_ input]]
   (try
     (let [json       (js/JSON.parse input)
           profiles   (js/JSON.stringify (.. json -profiles))
           personae   (js/JSON.stringify (.. json -personae))
           alignments (js/JSON.stringify (.. json -alignments))
           parameters (js/JSON.stringify (.. json -parameters))]
       (assoc db ::db/input
              {:input/profiles   profiles
               :input/personae   personae
               :input/alignments alignments
               :input/parameters parameters}))
     (catch js/Error. e
       (do
         (snackbar! "Error parsing JSON Input")
         db)))))

(re-frame/reg-event-db
 :input/profiles
 global-interceptors
 (fn [db [_ profiles]]
   (assoc-in db [::db/input :input/profiles]
             profiles)))

(re-frame/reg-event-db
 :input/personae
 global-interceptors
 (fn [db [_ personae]]
   (assoc-in db [::db/input :input/personae]
             personae)))

(re-frame/reg-event-db
 :input/alignments
 global-interceptors
 (fn [db [_ alignments]]
   (assoc-in db [::db/input :input/alignments]
             alignments)))

(re-frame/reg-event-db
 :input/parameters
 global-interceptors
 (fn [db [_ parameters]]
   (assoc-in db [::db/input :input/parameters]
             parameters)))

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
 (fn [db [_ data]]
   (assoc-in db [::db/validation :validation/data]
             data)))


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
