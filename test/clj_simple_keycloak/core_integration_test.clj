(ns clj-simple-keycloak.core-integration-test
  (:require
   [clj-simple-keycloak.core :refer [authn!! authz!! new-client-config]]
   [clojure.test :refer :all]
   [matcher-combinators.test])
  (:import
   (clojure.lang ExceptionInfo)))

(def kc-client-config
  (new-client-config "http://anything" "anyrealm" "anyclient-id" "secret"))

(defn stub-port
  [response]
  {:kc-client-http-port/post-token (fn [_ _]
                                     response)})

(def body
  "{\"token\":\"xyz\"}")

(deftest authn-test
  (testing "non-200-response returns exception"
    (is (thrown-match?
         ExceptionInfo {:type :simple-keycloak/unauthorized}
         (authn!! (stub-port (ref {:status 403})) kc-client-config "usr1" "pwd")))
    (is (thrown-match?
         ExceptionInfo {:type :simple-keycloak/unauthorized}
         (authn!! (stub-port (ref {:status 500})) kc-client-config "usr1" "pwd")))
    (is (thrown-match?
         ExceptionInfo {:type :simple-keycloak/unauthorized}
         (authn!! (stub-port (ref {:status 400})) kc-client-config "usr1" "pwd"))))
  (testing "200 response should be returned with its deserialized body as response"
    (is (= {"token" "xyz"}
           (authn!! (stub-port (ref {:status 200 :body body})) kc-client-config "usr1" "pwd")))))

(deftest authz-test
  (testing "non-200-response returns exception"
    (is (thrown-match?
         ExceptionInfo {:type :simple-keycloak/unauthorized}
         (authz!! (stub-port (ref {:status 403})) kc-client-config {} "perm")))
    (is (thrown-match?
         ExceptionInfo {:type :simple-keycloak/unauthorized}
         (authz!! (stub-port (ref {:status 500})) kc-client-config {} "perm")))
    (is (thrown-match?
         ExceptionInfo {:type :simple-keycloak/unauthorized}
         (authz!! (stub-port (ref {:status 400})) kc-client-config {} "perm")))
    (testing "200 response should be returned with its body as response"
      (is (= body
             (authz!! (stub-port (ref {:status 200 :body body})) kc-client-config {} "perm"))))))
