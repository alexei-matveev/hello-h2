;;
;; Clojure + H2 example at [1] uses outdated version of
;; clojure.java.jdbc [2].
;;
;; [1] http://makble.com/using-h2-in-memory-database-in-clojure
;; [2] http://clojure-doc.org/articles/ecosystem/java_jdbc/home.html
;;
(ns hello-h2.core
  (:require [clojure.java.jdbc :as jdbc]))

;; Starting with c.j.jdbc 0.7.6 spect for  in memory H2 DB can be like
;; this [2]. FIXME: what are the defaults for username/password?
(def db {:dbtype "h2:mem", :dbname "demo"})

(defn -main []
  (jdbc/with-db-connection [db db]
    (jdbc/db-do-commands db
                         (jdbc/create-table-ddl :filetable
                                                [[:name "varchar(3200)"]
                                                 [:path "varchar(3200)"]
                                                 [:origname "varchar(3200)"]]))
    (jdbc/insert! db :filetable {:name "file-name"
                                 :path "file/path"
                                 :origname "original-name"})
    (println
     (jdbc/query db ["select * from filetable"]))
    (jdbc/db-do-commands db (jdbc/drop-table-ddl :filetable))))
