# keycloak-jaxrs-client-authfilter

[![Build Status](https://secure.travis-ci.org/realityforge/keycloak-jaxrs-client-authfilter.svg?branch=master)](http://travis-ci.org/realityforge/keycloak-jaxrs-client-authfilter)
[<img src="https://img.shields.io/maven-central/v/org.realityforge.keycloak.client.authfilter/keycloak-jaxrs-client-authfilter.svg?label=latest%20release"/>](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.realityforge.keycloak.client.authfilter%22%20a%3A%22keycloak-jaxrs-client-authfilter%22)

This library provides a basic Jaxrs client interceptor that makes accessing keycloak secured
services easy.

## Quick Start

The simplest way to use the library is to add the following dependency
into the build system. i.e.

```xml
<dependency>
    <groupId>org.realityforge.keycloak.client.authfilter</groupId>
    <artifactId>keycloak-jaxrs-client-authfilter</artifactId>
    <version>1.01</version>
</dependency>
```

Then you set up an instance of `Keycloak` that will be used to authenticate with keycloak. Make sure that
the client configured is one that allows log in via password (and not a "bearer-only" client).

```java
final KeycloakConfig config =
  KeycloakConfig.createPasswordConfig( "https://id.example.com", "MyRealm", "MyClient", "MyUser", "MyPass" );
final Keycloak keycloak = new Keycloak( config );
```

Then ensure that a `BearerAuthFilter` is attached to jaxrs client setup when accessing secured services. i.e.

```java
final Client client =
  ClientBuilder.newClient().register( new BearerAuthFilter( keycloak ) );
...
```

## Credit

This code was inspired by a [post](http://lists.jboss.org/pipermail/keycloak-user/2017-May/010740.html) by
Thomas Darimont and draws heavily on the [admin-client](https://github.com/keycloak/keycloak/tree/master/integration/admin-client/src/main/java/org/keycloak/admin/client)
code from the main keycloak project.
