(ns yritysten.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [yritysten.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[yritysten started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[yritysten has shut down successfully]=-"))
   :middleware wrap-dev})
