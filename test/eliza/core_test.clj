(ns eliza.core-test
  (:require [clojure.test :refer :all]
            [eliza.core   :refer :all]))

(deftest smoke-test
  (is (query "Hello, how are you?")))