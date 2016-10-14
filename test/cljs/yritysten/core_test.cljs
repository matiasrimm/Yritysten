(ns yritysten.core-test
  (:require [cljs.test :refer-macros [is are deftest testing use-fixtures]]
            [reagent.core :as reagent :refer [atom]]
            [yritysten.core :as rc]))

(deftest test-home
  (is (= true true)))

