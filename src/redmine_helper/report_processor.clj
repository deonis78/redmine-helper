(ns redmine_helper.report_processor
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as su]))

(def coefficients {"Simple" 0.5, "Normal" 1, "Hard" 1.5, "Very Hard" 2})

(defn folder-fn [acc el]
  (let [rec (su/split el #"\t+")
        complexities #{"Simple" "Normal" "Hard" "Very Hard"}]
    (if (contains? complexities (first rec))
      (let [current (assoc-in (last acc) [:complexity (first rec)] (second rec))]
        (conj (vec (butlast acc)) current))
      (conj acc {:name (first rec), :total (second rec)}))))

(defn parse-double [val]
  (java.lang.Double/parseDouble val))

(defn calculate-complexity [complexities]
  (reduce + 0
    (for [[k v] complexities :when (contains? coefficients k)] (* (get coefficients k) (parse-double v)))))

(defn create-log-map [time-log el]
  (assoc time-log (first el) (parse-double(second el))))

(defn work-time [time-log name]
  (if-let [time (get time-log name)] time 0))

(defn run-processor []
(with-open [reader (io/reader "spent_time.txt") w (io/writer "result.csv")
            time-log-reader (io/reader "time_log.csv")]
  (let [rows (line-seq reader)
        result (reduce folder-fn [] rows)
        time-log (reduce create-log-map {} (csv/read-csv time-log-reader :separator \,))]
    (doseq [line result] (.write w
      (format "\"%s\";%.2f;%.2f\n"
        (:name line)
        (calculate-complexity (:complexity line))
        (/ (work-time time-log (:name line)) (parse-double (:total line)))))))))
