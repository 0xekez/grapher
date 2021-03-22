(ns grapher.core
  (:require
   [grapher.app :refer [app]]
   [ring.adapter.jetty :refer [run-jetty]]))

(defn -main []
  (run-jetty app {:port  80
                  :join? false}))
