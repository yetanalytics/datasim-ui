(ns datasim-ui.core
  (:require [reagent.core     :as r]
            [re-frame.core    :refer [dispatch-sync]]
            [datasim-ui.handlers]
            [datasim-ui.subs]
            [datasim-ui.views :refer [app]]))

(enable-console-print!)

(defn ^:export run
  []
  (dispatch-sync [:db/initialize])
  (r/render [app]
            (js/document.getElementById "app")))

(run)
