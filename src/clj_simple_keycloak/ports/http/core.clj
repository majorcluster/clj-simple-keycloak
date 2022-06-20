(ns clj-simple-keycloak.ports.http.core
  (:require [org.httpkit.client :as http]))

(defn- post-token
  [kc-client-config opts]
  (http/post (:kc-client-config/token-url kc-client-config) opts))

(def port
  {:kc-client-http-port/post-token post-token})
