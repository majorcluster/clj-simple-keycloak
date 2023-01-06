(defproject org.clojars.majorcluster/clj-simple-keycloak "0.2.0"
  :description "A clojure library to simply resolve authentication and authorization on an externally maintained keycloak server"
  :url "https://github.com/mtsbarbosa/clj-simple-keycloak"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [http-kit "2.7.0-alpha1"]
                 [org.clojure/data.json "2.4.0"]
                 [nubank/matcher-combinators "3.7.2"]]
  :repl-options {:init-ns clj-simple-keycloak.core})
