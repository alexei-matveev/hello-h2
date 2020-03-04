;;
;; Clojure + H2 example at [1] uses outdated version of
;; clojure.java.jdbc.
;;
;; [1] http://makble.com/using-h2-in-memory-database-in-clojure
;;
(ns hello-h2.core
  (:require [clojure.java.jdbc :as jdbc]))

(def db
  {:classname "org.h2.Driver"
   :subprotocol "h2:mem"
   :subname "demo;DB_CLOSE_DELAY=-1"
   :user "sa"
   :password ""})

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
