# Change Log

### [v1.00](https://github.com/realityforge/keycloak-jaxrs-client-authfilter/tree/v1.00) (2019-08-30)
[Full Changelog](https://github.com/realityforge/keycloak-jaxrs-client-authfilter/compare/v0.2...v1.00)

* Decouple from `org.keycloak` libraries and use a simplified mechanism for representing tokens.
* Allow the specification of `connectTimeout` and `readTimeout` used when attempting to connect to keycloak server.

### [v0.02](https://github.com/realityforge/keycloak-jaxrs-client-authfilter/tree/v0.02) (2017-06-20)
[Full Changelog](https://github.com/realityforge/keycloak-jaxrs-client-authfilter/compare/v0.01...v0.02)

* If an invalid token is returned from token service then manually expire the token.
* Add graceful handling of token service invocations that return non-successful responses.

### [v0.01](https://github.com/realityforge/keycloak-jaxrs-client-authfilter/tree/v0.01) (2017-05-23)
[Full Changelog](https://github.com/realityforge/keycloak-jaxrs-client-authfilter/compare/2f4de506384500d535a86ba282d7e082db59936b...v0.01)

* Initial release.
