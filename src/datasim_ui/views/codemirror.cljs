(ns datasim-ui.views.codemirror
  (:require [reagent.core :as r]
            [cljsjs.codemirror]
            [cljsjs.codemirror.mode.javascript]
            [cljsjs.codemirror.addon.lint.lint]
            [cljsjs.codemirror.addon.lint.javascript-lint]
            [cljsjs.codemirror.addon.lint.json-lint]))

(defn change
  [this cm _]
  (let [{:keys [update-fn]} (-> this
                                r/argv
                                rest
                                seq)]
    (update-fn (.getValue cm))))

(defn value
  [this cm _]
  (let [{:keys [value]} (-> this
                            r/argv
                            rest
                            seq)]
    (when-not (= value (.getValue cm))
      (.setValue cm value))))

(defn codemirror
  []
  (r/create-class
   {:reagent-render      (fn [& {:keys [name value]}]
                           [:textarea
                            {:name         name
                             :defaultValue value}])
    :component-did-mount (fn [this]
                           (let [cm (-> this
                                        r/dom-node
                                        (js/CodeMirror.fromTextArea
                                         (clj->js {:mode         "application/json"
                                                   :lineNumbers  true
                                                   :lineWrapping true
                                                   :gutters      ["CodeMirror-lint-markers"]
                                                   :lint         true})))]
                             (r/set-state this {:cm cm})
                             (.on cm
                                  "change"
                                  (fn [doc obj]
                                    (change this doc obj)))))
    :component-did-update (fn [this old-argv]
                            (let [cm (-> this
                                         r/state
                                         :cm)]
                              (value this cm old-argv)))}))
