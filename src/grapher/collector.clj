(ns grapher.collector)

(defn collect-files [root]
  (let [dir (clojure.java.io/file root)]
    (into {}
          (map #(vector (.getName %) (.getPath %))
               (filter #(not (.isDirectory %)) (file-seq dir))))))

(defn collect-contents [filemap]
  (into {} (for [[k v] filemap] [k (#(slurp %) v)])))

(defn extract-data [contents]
  (let [lines (clojure.string/split contents #"\n")]
    (map read-string lines)))

(defn collect-data [filemap]
  (into {} (for [[k v] filemap] [k (extract-data v)])))

(defn get-data-map []
  (->
   (collect-files "data")
   collect-contents
   collect-data))
