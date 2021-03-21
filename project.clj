(defproject grapher "0.1.0-SNAPSHOT"
  :description "simple grapher"
  :url "https://github.com/ZekeMedley/grapher"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [ring "1.9.2"]]
  :repl-options {:init-ns grapher.core}
  :main grapher.core)
