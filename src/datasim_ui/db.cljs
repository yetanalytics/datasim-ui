(ns datasim-ui.db
  (:require [cljs.spec.alpha :as s :include-macros true]
            [re-frame.core   :as re-frame]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Spec to define the db shape
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;



(s/def :editor-mode/mode keyword?)
(s/def :editor-mode/mode-type keyword?)
(s/def :editor-mode/display string?)
(s/def :editor-mode/icon string?)
(s/def :editor-mode/selected boolean?)
(s/def :editor-mode/index int?)
(s/def ::editor-mode
  (s/keys :req-un [:editor-mode/display
                   :editor-mode/mode
                   :editor-mode/selected]
          :opt    [:editor-mode/mode-type
                   :editor-mode/icon
                   :editor-mode/index]))

(s/def ::input-modes
  (s/every ::editor-mode))

(s/def ::input-data
  string?)

(s/def ::input-data-vec
  vector?)

(s/def ::input-map
  (s/keys :opt [::input-modes
                ::input-data]))

(s/def ::input-map-vec
  (s/keys :opt [::input-modes
                ::input-data-vec]))

(s/def :input/profiles ::input-map-vec)
(s/def :input/personae ::input-map-vec)
(s/def :input/alignments ::input-map)
(s/def :input/parameters ::input-map)
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

(s/def :error/visible boolean?)
(s/def :error/id uuid?)
(s/def :error/text string?)
(s/def :error/path (s/every any?))
(s/def :error/visible boolean?)
(s/def ::error (s/keys :req [:error/id
                             :error/text
                             :error/path
                             :error/visible]))
(s/def :validation/data
  (s/every ::error))

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
