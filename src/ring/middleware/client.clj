(ns ring.middleware.client
  "Functions that help integrate clj-http in ring applications."
  (:require [clj-http.client :as http]))

(defn wrap-client-middleware-builder
  "A ring middleware that builds and applies a clj-http middleware.

  `f` is a function that takes a request and returns a clj-http middleware. This
  is useful in scenarios where the middleware must be dynamic based on data from
  the request (e.g., header propagation).

  Note that this relies on thread-local bindings, so you cannot use async
  handlers if you use this middleware.

  If you need to apply a middleware that is not dynamic, use
  [[ring.middleware.client/wrap-client-middleware]] instead."
  [handler f]
  (fn [request]
    (http/with-additional-middleware [(f request)]
      (handler request))))

(defn wrap-client-middleware
  "A ring middleware that wraps clj-http calls with the specified middleware.

  Note that this relies on thread-local bindings, so you cannot use async
  handlers if you use this middleware.

  If you need to dynamically create a middleware that is based on information
  held in the request, use
  [[ring.middleware.client/wrap-client-middleware-builder]] instead."
  [handler middleware]
  (wrap-client-middleware-builder handler (constantly middleware)))
