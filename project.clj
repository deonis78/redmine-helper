(defproject redmine-helper "0.1.0-SNAPSHOT"
  :description "redmine helper for productivity calculation"
  :dependencies [[org.clojure/clojure "1.6.0"] [org.clojure/data.csv "0.1.2"]]
  :main redmine-helper.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
