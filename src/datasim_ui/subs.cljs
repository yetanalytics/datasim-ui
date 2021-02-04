(ns datasim-ui.subs
  (:require [re-frame.core :refer [reg-sub subscribe]]
            [datasim-ui.db :as db]
            [clojure.pprint :refer [pprint]]))

(reg-sub
 :db/input
 (fn [db _]
   (::db/input db)))

(reg-sub
 :input/get-data
 (fn [_ _]
   (subscribe [:db/input]))
 (fn [input [_ input-key]]
   (get-in input [input-key :input-data])))

(reg-sub
 :input/get-parsed-data
 (fn [[_ input-key]]
   (subscribe [:input/get-data input-key]))
 (fn [input-data [_ _]]
   (try
     (js->clj (js/JSON.parse input-data)
              :keywordize-keys true)
     (catch js/Error. e
       (do
         (pprint ["Parse Problem!" e])
         nil)))))

(reg-sub
 :input/get-valid
 (fn [[_ input-key]]
   (subscribe [:input/get-data input-key]))
 (fn [input-data [_ _]]
   (try
     (let [parsed (js->clj (js/JSON.parse input-data)
                           :keywordize-keys true)]
       true)
     (catch js/Error. e
       (do
         false)))))

(reg-sub
 :input/get-value
 (fn [[_ input-key]]
   (subscribe [:input/get-parsed-data input-key]))
 (fn [data [_ input-key & address]]
   (get-in data address)))

(reg-sub
 :input/get-actor-ifis
 (fn [_ _]
   (subscribe [:input/get-parsed-data :input/personae]))
 (fn [data _]
   (if (= nil data)
     []
     (mapv (fn [member]
             (or
              (:mbox member)
              (:mbox_sha1 member)
              (:openid member)
              (if (:account member)
                (str (get-in member [:account :name]) " / "
                     (get-in member [:account :homePage]))
                nil)))
           (:member data)))))


(reg-sub
 :input/get-modes
 (fn [_ _]
   (subscribe [:db/input]))
 (fn [input [_ input-key]]
   (get-in input [input-key :input-modes])))

(reg-sub
 :input/get-selected-mode
 (fn [_ _]
   (subscribe [:db/input]))
 (fn [input [_ input-key]]
   (let [modes (get-in input [input-key :input-modes])]
     (first
      (filter (fn [mode]
                (:selected mode)) modes)))))

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
 :db/validation
 (fn [db _]
   (::db/validation db)))

(reg-sub
 :validation/visible
 (fn [_ _]
   (subscribe [:db/validation]))
 (fn [validation _]
   (:validation/visible validation)))

(reg-sub
 :validation/data
 (fn [_ _]
   (subscribe [:db/validation]))
 (fn [validation _]
   (:validation/data validation)))

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

(reg-sub
 :options/username
 (fn [_ _]
   (subscribe [:db/options]))
 (fn [options _]
   (:options/username options)))

(reg-sub
 :options/password
 (fn [_ _]
   (subscribe [:db/options]))
 (fn [options _]
   (:options/password options)))

(reg-sub
 :db/dialog
 (fn [db _]
   (::db/dialog db)))

(reg-sub
 :dialog/open
 (fn [_ _]
   (subscribe [:db/dialog]))
 (fn [dialog _]
   (:dialog/open dialog)))

(reg-sub
 :dialog/title
 (fn [_ _]
   (subscribe [:db/dialog]))
 (fn [dialog _]
   (:dialog/title dialog)))

(reg-sub
 :dialog/form
 (fn [_ _]
   (subscribe [:db/dialog]))
 (fn [dialog _]
   (:dialog/form dialog)))

(reg-sub
 :dialog.form/ids
 (fn [_ _]
   (subscribe [:dialog/form]))
 (fn [form _]
   (mapv (fn [[id {:keys [type]}]]
           [id type])
         form)))

(reg-sub
 :dialog.form/label
 (fn [_ _]
   (subscribe [:dialog/form]))
 (fn [form [_ id]]
   (-> form
       (get id)
       :label)))

(reg-sub
 :dialog.form/text
 (fn [_ _]
   (subscribe [:dialog/form]))
 (fn [form [_ id]]
   (-> form
       (get id)
       :text)))

(reg-sub
 :dialog/save
 (fn [_ _]
   (subscribe [:db/dialog]))
 (fn [dialog _]
   (:dialog/save dialog)))
