(ns datasim-ui.util
  (:require [goog.object :as gobject]
            [clojure.string :as s]))

(def inputs
  {:input/profiles   "Profiles"
   :input/personae   "Personae"
   :input/alignments "Alignments"
   :input/parameters "Parameters"})

(def options
  {:options/endpoint       "LRS Endpoint"
   :options/api-key        "API Key"
   :options/api-secret-key "API Secret Key"})

(def basic-auth
  {:options/username "Username"
   :options/password "Password"})

(defn label
  [key]
  (-> (get (merge inputs options basic-auth)
           key)))

(defn input-name
  [key]
  (-> key
      label
      (s/replace " " "-")
      s/lower-case))

(declare clj-to-json)

(defn to-json-key
  ([k] (to-json-key k clj-to-json))
  ([k primitive-fn]
   (cond
     (satisfies? IEncodeJS k) (-clj->js k)
     (or (string? k)
         (number? k)
         (keyword? k)
         (symbol? k)) (primitive-fn k)
     :default (pr-str k))))


(defn clj-to-json
  [x & {:keys [keyword-fn]
        :or   {keyword-fn name}
        :as options}]
  (letfn [(keyfn [k] (to-json-key k thisfn))
          (thisfn [x] (cond
                        (nil? x) nil
                        (satisfies? IEncodeJS x) (-clj->js x)
                        (keyword? x) (keyword-fn x)
                        (symbol? x) (str x)
                        (map? x) (let [m (js-obj)]
                                   (doseq [[k v] x]
                                     (gobject/set m (keyfn k) (thisfn v)))
                                   m)
                        (coll? x) (let [arr (array)]
                                    (doseq [x (map thisfn x)]
                                      (.push arr x))
                                    arr)
                        :else x))]
    (thisfn x)))


(defn to-clj-key
  [k]
  (keyword nil
           (if (= "@context" k)
             "_context"
             k)))

(defn json-to-clj
  ([x] (json-to-clj x :keywordize-keys false))
  ([x & opts]
   (let [{:keys [keywordize-keys]} opts
         keyfn (if keywordize-keys to-clj-key str)
         f (fn thisfn [x]
             (cond
               (satisfies? IEncodeClojure x)
               (-js->clj x (apply array-map opts))

               (seq? x)
               (doall (map thisfn x))

               (map-entry? x)
               (MapEntry. (thisfn (key x)) (thisfn (val x)) nil)

               (coll? x)
               (into (empty x) (map thisfn) x)

               (array? x)
               (persistent!
                (reduce #(conj! %1 (thisfn %2))
                        (transient []) x))

               (identical? (type x) js/Object)
               (persistent!
                (reduce (fn [r k] (assoc! r (keyfn k) (thisfn (gobject/get x k))))
                        (transient {}) (js-keys x)))
               :else x))]
     (f x))))
