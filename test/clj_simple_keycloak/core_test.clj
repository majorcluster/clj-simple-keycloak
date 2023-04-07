(ns clj-simple-keycloak.core-test
  (:require
   [clj-simple-keycloak.core :refer [build-token-auth-header extract-token!!
                                     new-client-config]]
   [clojure.test :refer :all]
   [matcher-combinators.test])
  (:import
   (clojure.lang ExceptionInfo)))

(deftest extract-token-test
  (testing "When not having token error is thrown"
    (is (thrown-match?
         ExceptionInfo {:type :simple-keycloak/missing-token}
         (extract-token!! {})))
    (is (thrown-match?
         ExceptionInfo {:type :simple-keycloak/missing-token}
         (extract-token!! {"Content-Type" "whatever"})))
    (is (thrown-match?
         ExceptionInfo {:type :simple-keycloak/missing-token}
         (extract-token!! nil)))
    (is (thrown-match?
         ExceptionInfo {:type :simple-keycloak/missing-token}
         (extract-token!! {"Authorization" ""})))
    (is (thrown-match?
         ExceptionInfo {:type :simple-keycloak/missing-token}
         (extract-token!! {"Authorization" nil})))
    (is (thrown-match?
         ExceptionInfo {:type :simple-keycloak/missing-token}
         (extract-token!! {"Authorization" " "})))
    (is (thrown-match?
         ExceptionInfo {:type :simple-keycloak/missing-token}
         (extract-token!! {"Authorization" "Bearer "})))
    (is (thrown-match?
         ExceptionInfo {:type :simple-keycloak/missing-token}
         (extract-token!! {"Authorization" "Bearer  "})))
    (is (thrown-match?
         ExceptionInfo {:type :simple-keycloak/missing-token}
         (extract-token!! {"Authorization" "fdfdfdfd"}))))
  (testing "having a token it is extracted"
    (is (= "kdkdkdkdkd"
           (extract-token!! {"Authorization" "Bearer kdkdkdkdkd"})))))

(deftest build-token-auth-header-test
  (testing "token is added to the header"
    (is (= {"Authorization" "Bearer xyz"}
           (build-token-auth-header "xyz"))))
  (testing "blank token is not added"
    (is (= {}
           (build-token-auth-header ""))))
  (testing "blank token is not added"
    (is (= {}
           (build-token-auth-header " "))))
  (testing "blank token is not added"
    (is (= {}
           (build-token-auth-header nil)))))

(deftest new-client-config-test
  (testing "host and realm are used to build token url"
    (is (= "http://anything/realms/anyrealm/protocol/openid-connect/token"
           (:kc-client-config/token-url (new-client-config "http://anything" "anyrealm" "anyclient-id" "secret"))))))
