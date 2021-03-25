(ns grapher.app-test
  (:require
   [clojure.test :refer :all]
   [grapher.app :refer :all]
   [grapher.collector :refer :all]))

(deftest test-get-name-and-val
  (testing "get-name-and-val"
    (is (= () (get-name-and-val "/hello")))
    (is (= () (get-name-and-val "/")))
    (is (= () (get-name-and-val "/hello/2/3")))
    (is (= () (get-name-and-val "foo/bar")))
    (is (= () (get-name-and-val "foo/bar/baz")))
    (is (= '("hello" 2) (get-name-and-val "/hello/2")))))

(deftest test-get-response
  (testing "getting a response"
    (is (= '("something about this request seems off" 400) (handle-update {:uri "hello"})))))
