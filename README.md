# ring-client

[![Clojars Project](https://img.shields.io/clojars/v/tessellator/ring-client.svg)](https://clojars.org/tessellator/ring-client)

Ring middlewares that help integrate with
[clj-http](https://github.com/dakrone/clj-http) in ring applications.

NOTE: Because the internals depend on thread-local bindings, these middlewares
cannot be used with async handlers.

## Usage

Assuming some clj-http middleware named `client-middleware`, the following will
wrap any HTTP calls from clj-http with the specified client middleware.

```clojure
(require '[ring.middleware.client :refer [wrap-client-middleware]])

(def handler
  (wrap-client-middleware my-routes client-middleware))
```

If you need a more dynamic solution, you may use the
`wrap-client-middleware-builder` function. Instead of accepting a middleware
directly, it accepts a function that receives a ring request and returns a
clj-http middleware. The resulting middleware is applied in the same manner
as demonstrated above.

## License

Copyright © 2019 Thomas C. Taylor

Distributed under the Eclipse Public License version 2.0.
