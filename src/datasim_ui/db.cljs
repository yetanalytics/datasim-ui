(ns datasim-ui.db
  (:require [cljs.spec.alpha :as s :include-macros true]
            [re-frame.core   :as re-frame]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Spec to define the db shape
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(s/def :input/profiles string?)
(s/def :input/personae string?)
(s/def :input/alignments string?)
(s/def :input/parameters string?)
(s/def ::input (s/keys :opt [:input/profiles
                             :input/personae
                             :input/alignments
                             :input/parameters]))

(s/def ::focus (s/nilable keyword?))

(s/def :options/visible boolean?)
(s/def :options/endpoint string?)
(s/def :options/api-key string?)
(s/def :options/api-secret-key string?)
(s/def :options/send-to-lrs boolean?)
(s/def :options/download boolean?)
(s/def :options/username string?)
(s/def :options/password string?)
(s/def ::options (s/keys :req [:options/visible]
                         :opt [:options/endpoint
                               :options/api-key
                               :options/api-secret-key
                               :options/send-to-lrs
                               :options/username
                               :options/password]))

(s/def :validation/visible boolean?)
(s/def :validation/data some?)
(s/def ::validation (s/keys :req [:validation/visible]
                            :opt [:validation/data]))

(s/def :dialog/open boolean?)
(s/def :dialog/title string?)
(s/def :form/id keyword?)
(s/def :form/type keyword?)
(s/def :form/label string?)
(s/def :form/text string?)
(s/def ::form (s/keys :req-un [:form/type
                               :form/text]
                      :opt-un [:form/label]))
(s/def :dialog/form (s/map-of :form/id ::form))
(s/def :dialog/save fn?)
(s/def ::dialog (s/keys :req [:dialog/open]
                        :opt [:dialog/title
                              :dialog/form
                              :dialog/save]))

(s/def ::db (s/keys :req [::options
                          ::dialog
                          ::validation]
                    :opt [::input
                          ::focus]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; VALIDATOR
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn check-and-throw
  "Throw an exception if the app db does not match the spec."
  [a-spec db]
  (when-not (s/valid? a-spec db)
    (throw (ex-info (str "Spec check failed in: " (s/explain-str a-spec db)) {}))))

(def check-spec-interceptor
  (re-frame/after
   (partial check-and-throw ::db)))