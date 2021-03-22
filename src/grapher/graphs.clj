(ns grapher.graphs
  (:use [hiccup.core])
  (:require
   [grapher.collector :as collector]))

(def header
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
   [:title "grapher"]
   [:link {:rel "stylesheet" :href "style.css"}]
   [:script {:src "https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.min.js"}]])

(defn to-js-array [data]
  (let [v (into [] data)
        joined (clojure.string/join ", " v)]
    (str "[" joined "]")))

(defn make-line-chart [name data]
  [:div {:class "graph" :id (format "graph_%s" name)}
   [:canvas {:id name :width "100%" :height "100px"}]
   [:script (let [seq (range 1 (+ 1 (count data)))
                  jslabels (to-js-array seq)
                  jsdata (to-js-array data)]
              (format "
new Chart(document.getElementById('%s').getContext('2d'), {
  type: 'line',
  data: {
    labels: %s,
    datasets: [{
      label: '%s',
      data: %s,
      fill: false,
      pointRadius: 1,
      borderColor: 'blue',
      borderWidth: 1,
    }]
  },
  options: {
    title: {
      display: true,
      text: '%s'
    }
  }
});
" name jslabels name jsdata name))]])

(defn make-homepage []
  (let [data (collector/get-data-map)]
    (html
     header
     [:body
      [:h1 "grapher"]
      (for [[n d] data] (make-line-chart n d))])))
