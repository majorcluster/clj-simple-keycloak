(defproject org.clojars.majorcluster/clj-simple-keycloak "0.1.0"
  :description "A clojure library to simply resolve authentication and authorization on an externally maintained keycloak server"
  :url "https://github.com/mtsbarbosa/clj-simple-keycloak"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [http-kit "2.7.0-alpha1"]
                 [org.clojure/data.json "0.2.6"]
                 [nubank/matcher-combinators "1.2.1"]]
  :repl-options {:init-ns clj-simple-keycloak.core})
