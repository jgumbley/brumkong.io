(ns ^:figwheel-no-load guidemap.dev
  (:require
    [guidemap.core :as core]
    [devtools.core :as devtools]))

(devtools/install!)

(enable-console-print!)

(core/init!)
