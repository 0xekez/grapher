(ns grapher.collector-test
  (:require
   [clojure.test :refer :all]
   [grapher.collector :refer :all]))

(deftest test-get-name-and-val
  (testing "extract data"
    (is (= '(1 2) (extract-data "1\n2\n")))))
