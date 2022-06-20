(ns clj-simple-keycloak.core
  (:require [clojure.string :as str]
            [clj-simple-keycloak.ports.http.core :as http]
            [clojure.data.json :as json]))

(defn- get-or-throw
  [s ex-msg ext-type]
  (cond (str/blank? s) (throw (ex-info ex-msg {:type ext-type}))
        :else s))

(defn print-n-continue
  [m]
  (println m)
  m)

(defn- split-opt
  [s pattern]
  (let [not-blank? (not (str/blank? s))
        valid? (and not-blank?
                    (str/includes? s "Bearer"))]
    (if valid? (str/split s pattern)
               [])))

(defn extract-token!!
  "Extract token from headers"
  [headers]
  (-> (get headers "Authorization" " ")
      (print-n-continue)
      (split-opt #"Bearer\s")
      (last)
      (get-or-throw "Missing token" :simple-keycloak/missing-token)))

(defn build-token-auth-header
  "Build token header based on a token received"
  [token]
  (cond (str/blank? token) {}
        :else {"Authorization" (str "Bearer " token)}))

(defn new-client-config
  "Builds a client config to authn!! and authz!!"
  [host realm client-id client-secret]
  {:kc-client-config/host host
   :kc-client-config/realm realm
   :kc-client-config/client-id client-id
   :kc-client-config/client-secret client-secret
   :kc-client-config/token-url (format "%s/realms/%s/protocol/openid-connect/token" host realm)})

(defn authn!!
  "receives a client config created from `new-client-config`, username and password and returns authentication tokens
  in case of error an ex-info w/ `:type :simple-keycloak/unauthorized` is thrown"
  ([http-port kc-client-config username password]
    (let [opts {:timeout 2000
                :form-params {:username username
                              :password password
                              :grant_type "password"
                              :client_id (:kc-client-config/client-id kc-client-config)
                              :client_secret (:kc-client-config/client-secret kc-client-config)}
                :headers {"Content-Type" "application/x-www-form-urlencoded"
                          "Accept" "*/*"}}
          resp @((:kc-client-http-port/post-token http-port) kc-client-config opts)]
      (cond (= 200 (:status resp)) (-> (:body resp)
                                       (json/read-str))
            :else (throw (ex-info "Unauthorized" {:type :simple-keycloak/unauthorized})))))
  ([kc-client-config username password]
   (authn!! http/port kc-client-config username password)))

(defn authz!!
  "receives a client config created from `new-client-config`,
  headers having `Authorization: Bearer <token from authn!!>`,
  permission which auth user must have rights
  in case of error or not authorized an ex-info w/ `:type :simple-keycloak/unauthorized` is thrown"
  ([http-port kc-client-config headers permission]
    (let [opts {:timeout 2000
                :form-params {:grant_type "urn:ietf:params:oauth:grant-type:uma-ticket"
                              :audience (:kc-client-config/client-id kc-client-config)
                              :permission permission}
                :headers (merge headers {"Content-Type" "application/x-www-form-urlencoded"
                                          "Accept" "*/*"})}
          resp @((:kc-client-http-port/post-token http-port) kc-client-config opts)]
      (cond (= 200 (:status resp)) (:body resp)
            :else (throw (ex-info "Unauthorized" {:type :simple-keycloak/unauthorized})))))
  ([kc-client-config headers permission]
   (authz!! http/port kc-client-config headers permission)))
