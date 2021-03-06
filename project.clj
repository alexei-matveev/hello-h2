(defproject hello-h2 "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 ;; https://github.com/clojure/java.jdbc
                 [org.clojure/java.jdbc "0.7.11"]
                 ;; https://github.com/h2database/h2database --- JAR
                 ;; file size is 2.2M
                 [com.h2database/h2 "1.4.200"]]
  :main hello-h2.core
  :repl-options {:init-ns hello-h2.core})
