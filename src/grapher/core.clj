(ns grapher.core
  (:require
   [grapher.app :refer [app write-name-and-value]]
   [ring.adapter.jetty :refer [run-jetty]]))

(defn timed-app [request]
  "Handles a request using the regular app function but also plots the
  amount of time it took to handle the request."
  (let [start (System/currentTimeMillis)
        res (app request)
        elapsed (- (System/currentTimeMillis) start)]
    (write-name-and-value "response_speed_ms" elapsed)
    res))

(defn -main []
  (run-jetty timed-app {:port  80
                        :join? false}))
