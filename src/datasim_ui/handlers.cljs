(ns datasim-ui.handlers
  (:require [re-frame.core :as re-frame]
            [datasim-ui.db :as db :refer [check-spec-interceptor]]))

(def global-interceptors
  [check-spec-interceptor])

(re-frame/reg-event-db
 :db/initialize
 global-interceptors
 (fn [db _]
   (assoc db ::db/options
          {:options/visible     false
           :options/send-to-lrs false})))

(re-frame/reg-event-db
 :input/all
 global-interceptors
 (fn [db [_ input]]
   (let [json       (js/JSON.parse input)
         profiles   (js/JSON.stringify (.. json -profiles))
         personae   (js/JSON.stringify (.. json -personae))
         alignments (js/JSON.stringify (.. json -alignments))
         parameters (js/JSON.stringify (.. json -parameters))]
     (assoc db ::db/input
            {:input/profiles   profiles
             :input/personae   personae
             :input/alignments alignments
             :input/parameters parameters}))))

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
