(ns ring.middleware.client-test
  (:require [clj-http.client :as http]
            [clj-http.fake :as fake]
            [clojure.test :refer [deftest is]]
            [ring.middleware.client :refer [wrap-client-middleware-builder
                                            wrap-client-middleware]]
            [ring.mock.request :as mock]))

(def fake-routes
  {"http://example.com/"
   (fn [req] {:status 200
              :headers {}
              :body (get-in req [:headers "x-test-header"])})})

(defn wrap-client-intercept [val]
  (fn [client]
    (fn [request]
      (client (update request :headers assoc "x-test-header" val)))))

(defn call-client-handler [_]
  {:status 200
   :headers {}
   :body (:body (http/get "http://example.com/"))})

(deftest test-wrap-client-middleware-builder
  (fake/with-fake-routes-in-isolation fake-routes
    (let [builder (fn [req] (wrap-client-intercept (slurp (:body req))))
          handler (wrap-client-middleware-builder call-client-handler builder)
          request (-> (mock/request :get "/some/path")
                      (mock/body "intercepted value"))
          response (handler request)]
      (is (= "intercepted value" (:body response))))))

(deftest test-wrap-client-middleware
  (fake/with-fake-routes-in-isolation fake-routes
    (let [middleware (wrap-client-intercept "my value")
          handler (wrap-client-middleware call-client-handler middleware)
          request (mock/request :get "/some/path")
          response (handler request)]
      (is (= "my value" (:body response))))))
