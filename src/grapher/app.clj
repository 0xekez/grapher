(ns grapher.app
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]
   [grapher.graphs :as graphs]
   [ring.middleware.content-type :refer [wrap-content-type]]
   [ring.middleware.not-modified :refer [wrap-not-modified]]
   [ring.middleware.resource :refer [wrap-resource]]
   [ring.util.response :as util]))

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

(defn handle-update [request]
  (let [uri (:uri request)
        name-and-val (get-name-and-val uri)]
    (cond
      (= (count name-and-val) 2) (do
                                   (apply write-name-and-value name-and-val)
                                   '("success!" 200))
      :else '("something about this request seems off" 400))))

(defn handler [request]
  (cond
    (.contains ["index.html" "/"] (:uri request)) (util/content-type (util/response (graphs/make-homepage)) "text/html")
    :else (let [response (handle-update request)
                status (second response)
                body (first response)]
            (util/content-type (case status
                                 200 (util/response body)
                                 400 (util/bad-request body)) "text/html"))))

(def app
  (->
   handler
   (wrap-resource "www")
   (wrap-content-type)
   (wrap-not-modified)))
