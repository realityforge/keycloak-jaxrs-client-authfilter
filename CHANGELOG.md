## Unreleased

* Decouple from `org.keycloak` libraries and use a simplified mechanism for representing tokens.
* Allow the specification of `connectTimeout` and `readTimeout` used when attempting to connect to keycloak server.

## 0.2:

* If an invalid token is returned from token service then manually expire the token.
* Add graceful handling of token service invocations that return non-successful responses.

## 0.1:

* Initial release.
