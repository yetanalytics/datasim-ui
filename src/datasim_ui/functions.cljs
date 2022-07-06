(ns datasim-ui.functions
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [re-frame.core             :refer [subscribe dispatch]]
            [cljs-http.client          :as http]
            [cljs.core.async           :refer [<!]]
            [datasim-ui.env            :refer [api]]
            [datasim-ui.views.snackbar :refer [snackbar!]]))

(defn ps-event
  "Helper function that will prevent default action
   and stop propagation for an event, a common task."
  [e]
  (.preventDefault e)
  (.stopPropagation e))

(defn click-input
  "Given an id for an element, click that input."
  [e id]
  (ps-event e)
  (.click (js/document.getElementById id)))

(defn import-file
  "Read in the text from an input file, resolve the promise, and dispatch."
  [e k]
  (ps-event e)
  (let [target (.. e -currentTarget)]
    ;; Resolve the promise for the text file, should always be the 0th item.
    (.then (js/Promise.resolve
            (.text (aget (.. target -files) 0)))
           #(do
              (dispatch (case k
                          :input/all      [:input/set-all %]
                          :input/profiles [:input/import-vector k %]
                          [:input/set-data k %]))
              ;; clear out the temp file holding the input, so it can be reused.
              (set! (.. target -value) "")))))

(defn export-file
  "Taking in a file blob and a file name, this will create a new anchor element.
   It will use this to download the object."
  [e blob file-name]
  (ps-event e)
  (let [link (js/document.createElement "a")]
    (set! (.-download link) file-name)
    ;; webkit does not need the object to be added to the page
    (if js/window.webkitURL
      (set! (.-href link)
            (.createObjectURL js/window.webkitURL
                              blob))
      (do
        ;; for non webkit browsers, add the element to the page
        (set! (.-href link)
              (.createObjectURL js/window.URL
                                blob))
        (set! (.-onclick link)
              (fn [e]
                (.preventDefault e)
                (.stopPropagation e)
                (.remove (.. e -currentTarget))))
        (set! (.. link -style -display)
              "none")
        (.append js/document.body
                 link)))
    (.click link)))
