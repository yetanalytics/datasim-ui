(ns datasim-ui.util)

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
      (clojure.string/replace " " "-")
      clojure.string/lower-case))