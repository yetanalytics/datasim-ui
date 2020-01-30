(ns datasim-ui.core
  (:require [reagent.core     :as r]
            [datasim-ui.handlers]
            [datasim-ui.subs]
            [datasim-ui.views :refer [app]]))

(enable-console-print!)

(defn ^:export run
  []
  (r/render [app]
            (js/document.getElementById "app")))

(run)
