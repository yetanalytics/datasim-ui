(ns datasim-ui.handlers
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-event-db
 :input/import
 (fn [db [_ json-input]]
   (let [profiles   (js/JSON.stringify (.. json-input -profiles))
         personae   (js/JSON.stringify (.. json-input -personae))
         alignments (js/JSON.stringify (.. json-input -alignments))
         parameters (js/JSON.stringify (.. json-input -parameters))]
     (assoc db :datasim-ui.db/input
            {:input/profiles   profiles
             :input/personae   personae
             :input/alignments alignments
             :input/parameters parameters}))))
