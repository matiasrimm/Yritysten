(ns user
  (:require [mount.core :as mount]
            [yritysten.figwheel :refer [start-fw stop-fw cljs]]
            yritysten.core))

(defn start []
  (mount/start-without #'yritysten.core/repl-server))

(defn stop []
  (mount/stop-except #'yritysten.core/repl-server))

(defn restart []
  (stop)
  (start))


