(ns grapher.collector)

(defn collect-files [root]
  "Collects all of the non directory files in folder ROOT. Returns a
  map between file names and their relative paths."
  (let [dir (clojure.java.io/file root)]
    (into {}
          (map #(vector (.getName %) (.getPath %))
               (filter #(not (.isDirectory %)) (file-seq dir))))))

(defn collect-contents [filemap]
  "Collects the contents of each file in FILEMAP and returns a map
  between file names and their contents."
  (into {} (for [[k v] filemap] [k (#(slurp %) v)])))

(defn extract-data [contents]
  "Extracts the graph data from CONTENTS."
  (let [lines (clojure.string/split contents #"\n")]
    (map read-string lines)))

(defn collect-data [filemap]
  "Collects all the data in each file in FILEMAP and returns a map
  between file names and their data."
  (into {} (for [[k v] filemap] [k (extract-data v)])))

(defn get-data-map []
  "Gets a map between graph names and their data."
  (->
   (collect-files "data")
   collect-contents
   collect-data))
