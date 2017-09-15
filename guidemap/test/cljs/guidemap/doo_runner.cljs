(ns guidemap.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [guidemap.core-test]))

(doo-tests 'guidemap.core-test)
