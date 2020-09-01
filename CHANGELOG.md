# Change Log

### Unreleased

* Upgrade the `org.realityforge.javax.annotation` artifact to version `1.0.1`.
* Change `BearerAuthFilter` to abort the client request with an `IOException` if unable to acquire access token.
* Reduce the access level of `BasicAuthFilter`  as it is not expected to be accessed outside package.

### [v1.02](https://github.com/realityforge/keycloak-jaxrs-client-authfilter/tree/v1.02) (2019-09-09) · [Full Changelog](https://github.com/realityforge/keycloak-jaxrs-client-authfilter/compare/v1.01...v1.02)

* Upgrade the version of the `javax:javaee-api` artifact to `8.0`.
* Rename `AccessTokenResponse` to `TokenResponse`.
* Rename `Keycloak.getAccessToken()` to `Keycloak.getToken()`.
* Rename `Keycloak.getAccessTokenString()` to `Keycloak.getAccessToken()`.

### [v1.01](https://github.com/realityforge/keycloak-jaxrs-client-authfilter/tree/v1.01) (2019-08-30) · [Full Changelog](https://github.com/realityforge/keycloak-jaxrs-client-authfilter/compare/v1.00...v1.01)

* The `connectTimeout` and `readTimeout` properties were specified as values of type `long` and while this worked for older versions of jersey, it no longer works with the version of jersey shipped with Payara 5. The values have been changed to be of type `int` that works across jersey versions.

### [v1.00](https://github.com/realityforge/keycloak-jaxrs-client-authfilter/tree/v1.00) (2019-08-30) · [Full Changelog](https://github.com/realityforge/keycloak-jaxrs-client-authfilter/compare/v0.2...v1.00)

* Decouple from `org.keycloak` libraries and use a simplified mechanism for representing tokens.
* Allow the specification of `connectTimeout` and `readTimeout` used when attempting to connect to keycloak server.

### [v0.02](https://github.com/realityforge/keycloak-jaxrs-client-authfilter/tree/v0.02) (2017-06-20) · [Full Changelog](https://github.com/realityforge/keycloak-jaxrs-client-authfilter/compare/v0.01...v0.02)

* If an invalid token is returned from token service then manually expire the token.
* Add graceful handling of token service invocations that return non-successful responses.

### [v0.01](https://github.com/realityforge/keycloak-jaxrs-client-authfilter/tree/v0.01) (2017-05-23) · [Full Changelog](https://github.com/realityforge/keycloak-jaxrs-client-authfilter/compare/2f4de506384500d535a86ba282d7e082db59936b...v0.01)

* Initial release.
