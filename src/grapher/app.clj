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
  "Gets the name of the target graph and the requested value from
  URI. Returns () on error."
  (let [parts (str/split uri #"/")
        tuple (rest parts)]
    (cond (= (count parts) 3) (let [[name data] tuple]
                                (try
                                  (let [val (Integer. data)]
                                    (list name val))
                                  ;; Catch failure to parse the
                                  ;; integer. This likely means that
                                  ;; we were given a path like foo/bar
                                  ;; instead of foo/1
                                  (catch Exception e ())))
          :else ())))

(defn get-target-file [name]
  "Gets the file to store data in given the NAME of the graph."
  (str "data/" name))

(defn write-name-and-value [name value]
  "Stores VALUE in the data store for graph NAME."
  (with-open [wrtr (io/writer (get-target-file name) :append true :create true)]
    (.write wrtr (str value "\n"))))

(defn handle-update [request]
  "Handles a REQUEST to update data for a graph. Returns a tuple
  containing the http status for REQUEST and the body of the http
  response."
  (let [uri (:uri request)
        name-and-val (get-name-and-val uri)]
    (cond
      (= (count name-and-val) 2) (do
                                   (apply write-name-and-value name-and-val)
                                   '("success!" 200))
      :else '("something about this request seems off" 400))))

(defn handler [request]
  "Handles an incoming REQUEST returning a ring http response."
  (cond
    (.contains ["index.html" "/"] (:uri request)) (util/content-type (util/response (graphs/make-homepage)) "text/html")
    :else (let [response (handle-update request)
                status (second response)
                body (first response)]
            (util/content-type (cond
                                 (= status 200) (util/response body)
                                 (= status 400) (util/bad-request body)
                                 :else {:status status
                                        :headers []
                                        :body body}) "text/html"))))
(def app
  (->
   handler
   (wrap-resource "www")
   (wrap-content-type)
   (wrap-not-modified)))
