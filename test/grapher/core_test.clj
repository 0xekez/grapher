(ns grapher.core-test
  (:require [clojure.test :refer :all]
            [grapher.core :refer :all]))

(deftest test-get-name-and-val
  (testing "get-name-and-val"
    (is (= () (get-name-and-val "/hello")))
    (is (= () (get-name-and-val "/")))
    (is (= () (get-name-and-val "/hello/2/3")))
    (is (= '("hello" 2) (get-name-and-val "/hello/2")))))

(deftest test-get-response
  (testing "getting a response"
    (is (= "that seems wrong" (get-response {:uri "hello"})))))
