(ns yritysten.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [yritysten.core-test]))

(doo-tests 'yritysten.core-test)

