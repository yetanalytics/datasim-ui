(ns datasim-ui.subs
  (:require [re-frame.core :refer [reg-sub subscribe]]
            [datasim-ui.db :as db]))

(reg-sub
 :db/input
 (fn [db _]
   (::db/input db)))

(reg-sub
 :input/profiles
 (fn [_ _]
   (subscribe [:db/input]))
 (fn [input _]
   (:input/profiles input)))

(reg-sub
 :input/personae
 (fn [_ _]
   (subscribe [:db/input]))
 (fn [input _]
   (:input/personae input)))

(reg-sub
 :input/alignments
 (fn [_ _]
   (subscribe [:db/input]))
 (fn [input _]
   (:input/alignments input)))

(reg-sub
 :input/parameters
 (fn [_ _]
   (subscribe [:db/input]))
 (fn [input _]
   (:input/parameters input)))

(reg-sub
 :db/focus
 (fn [db _]
   (::db/focus db)))

(reg-sub
 :db/options
 (fn [db _]
   (::db/options db)))

(reg-sub
 :options/visible
 (fn [_ _]
   (subscribe [:db/options]))
 (fn [options _]
   (:options/visible options)))

(reg-sub
 :options/endpoint
 (fn [_ _]
   (subscribe [:db/options]))
 (fn [options _]
   (:options/endpoint options)))

(reg-sub
 :options/api-key
 (fn [_ _]
   (subscribe [:db/options]))
 (fn [options _]
   (:options/api-key options)))

(reg-sub
 :options/api-secret-key
 (fn [_ _]
   (subscribe [:db/options]))
 (fn [options _]
   (:options/api-secret-key options)))

(reg-sub
 :options/send-to-lrs
 (fn [_ _]
   (subscribe [:db/options]))
 (fn [options _]
   (:options/send-to-lrs options)))
