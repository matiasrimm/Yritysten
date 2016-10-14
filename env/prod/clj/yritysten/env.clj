(ns yritysten.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[yritysten started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[yritysten has shut down successfully]=-"))
   :middleware identity})
