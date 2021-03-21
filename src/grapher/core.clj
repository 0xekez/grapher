(ns grapher.core
  (:require [ring.adapter.jetty :as ring])
  (:require [clojure.string :as str])
  (:require [clojure.java.io :as io]))

(defn get-name-and-val [uri]
  (let [parts (str/split uri #"/")
        tuple (rest parts)]
    (cond
      (= (count parts) 3) (list (first tuple) (read-string (second tuple)))
      :else ())))

(defn get-target-file [name value]
  (str "data/" name))

(defn write-name-and-value [name value]
  (with-open [wrtr (io/writer (get-target-file name value) :append true :create true)]
    (.write wrtr (str value "\n"))))

(defn make-reponse [body status]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body body})

(defn get-response [request]
  (let [uri (:uri request)
        name-and-val (get-name-and-val uri)]
        (cond
          (= (count name-and-val) 2) (do
                                       (apply write-name-and-value name-and-val)
                                       '("success!" 200))
          (.contains ["/" "/index.html"] uri) '("grapher homepage" 200)
          :else '("something about this request seems off" 404))))

(defn handler [request]
  (apply make-reponse (get-response request)))

(defn -main []
  (ring/run-jetty handler {:port  3000
                           :join? false}))
