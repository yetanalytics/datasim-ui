(ns datasim-ui.handlers
  (:require [re-frame.core :as re-frame]
            [datasim-ui.db :refer [check-spec-interceptor]]))

(def global-interceptors
  [check-spec-interceptor])

(re-frame/reg-event-db
 :input/import
 global-interceptors
 (fn [db [_ input]]
   (let [json       (js/JSON.parse input)
         profiles   (js/JSON.stringify (.. json -profiles))
         personae   (js/JSON.stringify (.. json -personae))
         alignments (js/JSON.stringify (.. json -alignments))
         parameters (js/JSON.stringify (.. json -parameters))]
     (assoc db :datasim-ui.db/input
            {:input/profiles   profiles
             :input/personae   personae
             :input/alignments alignments
             :input/parameters parameters}))))

(re-frame/reg-event-db
 :profiles/import
 global-interceptors
 (fn [db [_ profiles]]
   (assoc-in db [:datasim-ui.db/input :input/profiles]
             profiles)))

(re-frame/reg-event-db
 :personae/import
 global-interceptors
 (fn [db [_ personae]]
   (assoc-in db [:datasim-ui.db/input :input/personae]
             personae)))

(re-frame/reg-event-db
 :alignments/import
 global-interceptors
 (fn [db [_ alignments]]
   (assoc-in db [:datasim-ui.db/input :input/alignments]
             alignments)))

(re-frame/reg-event-db
 :parameters/import
 global-interceptors
 (fn [db [_ parameters]]
   (assoc-in db [:datasim-ui.db/input :input/parameters]
             parameters)))
