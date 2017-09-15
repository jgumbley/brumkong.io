(ns guidemap.prod
  (:require [guidemap.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
