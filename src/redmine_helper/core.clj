(ns redmine-helper.core
  (:use redmine_helper.report_processor)
  (:gen-class))

(defn -main
  [& args]
  (run-processor))
