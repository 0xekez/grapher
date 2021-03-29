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
  "Converts a clojure vec DATA into a javascript list literal's string
  representation."
  (let [v (into [] data)
        joined (clojure.string/join ", " v)]
    (str "[" joined "]")))

(defn make-line-chart [name data]
  "Makes the javascript and html for graph with name NAME and data
  DATA."
  [:div {:class "graph" :id (format "graph_%s" name)}
   [:canvas {:id name :width "400px" :height "400px"}]
   [:script (let [seq (range 1 (+ 1 (count data)))
                  jslabels (to-js-array seq)
                  jsdata (to-js-array data)]
              (format "
Chart.defaults.global.defaultFontColor = 'rgb(0,0,0)'
Chart.defaults.global.defaultFontFamily = 'sans-serif'
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
  "Returns a string containing the html for the grapher homepage."
  (let [data (collector/get-data-map)]
    (html
     header
     [:body
      [:header
       [:h1 "grapher"]
       [:h3 "to add a new data point visit negativefour.com/graph/value"]]
      [:main
       (for [[n d] data] (make-line-chart n d))]])))
