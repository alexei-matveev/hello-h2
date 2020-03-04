;;
;; Clojure   +  H2   example   at  [1]   uses   outdated  version   of
;; clojure.java.jdbc [2].
;;
;; [1] http://makble.com/using-h2-in-memory-database-in-clojure
;; [2] http://clojure-doc.org/articles/ecosystem/java_jdbc/home.html
;;
(ns hello-h2.core
  (:require [clojure.java.jdbc :as jdbc]))

;; Starting with c.j.jdbc  0.7.6 specs for the in memory  DB can be as
;; simple  as here  [2].   What are  the  defaults for  user/password?
;; FWIW, it is not "sa"/"".
(def db {:dbtype "h2:mem", :dbname "demo"})

;; For a million rows it takes about 10s  + n * 1s to execute. Of that
;; 10s is  spent populating  the table  and about 1s  for each  of the
;; aggregate funcitons.
(defn -main []
  (jdbc/with-db-connection [db db]
    (jdbc/db-do-commands db
                         (jdbc/create-table-ddl :kvtable
                                                [[:key "varchar(256)"]
                                                 [:value :double]]))
    (try
      (do
        (time
         (jdbc/insert-multi! db
                             :kvtable
                             [:key :value]
                             (for [x (range (* 1000 1000))]
                               [(rand-nth ["a" "b"]) (rand 10)])))
        (println
         (jdbc/query db ["select KeY as k, VaLuE as V from kvtable limit 5"]))

        ;; Numeric literals "0.0" and "0.0e0"  in H2 SQL are decimals. The
        ;; expression "i + 0.0" with integer  "i" would also be a decimal.
        ;; You  probably  want  doubles.    Use  "cast(i  as  double)"  to
        ;; aggregate.  Aggregate functions  median() and percentile_cont()
        ;; apparently always return a decimal.
        (println
         (time
          (jdbc/query db
                      [(str "select key"
                            ", avg(value) as avg"
                            ", median(value) as p50"
                            ", percentile_cont(0.95) within group (order by value) as p95"
                            "  from kvtable group by key")]))))
      ;; Without try &  finally, if any of the above  fails, the table
      ;; is   not  deleted.    In  Cider   you  would   need  to   M-x
      ;; sesmon-restart:
      (finally
        (jdbc/db-do-commands db (jdbc/drop-table-ddl :kvtable))))))
