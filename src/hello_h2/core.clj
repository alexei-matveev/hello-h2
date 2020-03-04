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

(defn -main []
  (jdbc/with-db-connection [db db]
    (jdbc/db-do-commands db
                         (jdbc/create-table-ddl :kvtable
                                                [[:key "varchar(256)"]
                                                 [:value :integer]]))
    (jdbc/insert-multi! db
                        :kvtable
                        [:key :value]
                        (for [x (range 100)]
                          [(rand-nth ["a" "b"]) (rand-int 10)]))
    (println
     (jdbc/query db ["select KeY as k, VaLuE as V from kvtable"]))

    ;; Numeric literals "0.0" and "0.0e0"  in H2 SQL are decimals. The
    ;; expression "value + 0.0" would  also be a decimal. You probably
    ;; want doubles:
    (println
     (jdbc/query db ["select key, avg(cast(value as double)) as avg from kvtable group by key"]))
    (jdbc/db-do-commands db (jdbc/drop-table-ddl :kvtable))))
