(ns datasim-ui.views.codemirror
  (:require [reagent.core :as r]
            [cljsjs.codemirror]))

(defn opts
  "Grab the options componenets from the object."
  [this]
  (-> this
      r/argv
      rest
      first))

(defn conf
  "Grab the configuration componenets from the object."
  [this]
  (-> this
      r/argv
      rest
      second))

(defn change
  "Function to catch the change event from CodeMirror.
   It will retrieve the update function handler from the passed config.

   The entire value from code mirror will be read into here.
   It will also pass the change object for extra control."
  [this cm obj]
  (let [{:keys [update-fn]} (conf this)]
    (update-fn (.getValue cm) obj)))

(defn value
  "Function used to take updates to CodeMirror and reflect them in
   a state manager."
  [this cm _]
  (let [{:keys [value]} (conf this)]
    (when-not (= value (.getValue cm))
      (.setValue cm value))))

(defn codemirror
  []
  (r/create-class
   {:reagent-render      (fn [_ {:keys [name value]}]
                           ;; Render a textarea element and accept a name
                           ;; to use it as a form
                           [:textarea
                            {:name         name
                             :defaultValue value}])
    :component-did-mount (fn [this]
                           (let [opts (opts this)
                                 conf (conf this)
                                 ;; create a CodeMirror object from the textarea
                                 ;; merge in default options with passed opts
                                 cm   (-> this
                                          r/dom-node
                                          (js/CodeMirror.fromTextArea
                                           (clj->js (merge {:mode         "application/json"
                                                            :theme        "default"
                                                            :lineWrapping true}
                                                           opts))))]
                             ;; add the CM object to the React component so it can be accessed
                             (r/set-state this {:cm cm})
                             ;; attach an option update function callback
                             ;; this is used when codemirror value changes
                             (doseq [[event-name event-fn]
                                     (:events conf)]
                               (.on cm
                                    event-name
                                    (fn [& args]
                                      (event-fn this args))))
                             #_(when (:update-fn conf)
                               (.on cm
                                    "change"
                                    (fn [doc obj]
                                      (change this doc obj))))))
    :component-did-update (fn [this old-argv]
                            ;; provide a way for CodeMirror to be updated by external state changes
                            (let [cm (-> this
                                         r/state
                                         :cm)]
                              (value this cm old-argv)))}))
