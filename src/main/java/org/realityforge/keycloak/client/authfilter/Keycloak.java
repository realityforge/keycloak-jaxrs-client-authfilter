/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.realityforge.keycloak.client.authfilter;

import java.util.Collections;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.keycloak.OAuth2Constants;
import org.keycloak.common.util.Time;
import org.keycloak.representations.AccessTokenResponse;

public final class Keycloak
{
  private static final long DEFAULT_MIN_VALIDITY = 30;

  @Nonnull
  private final KeycloakConfig _config;
  @Nullable
  private AccessTokenResponse _currentToken;
  private long _expirationTime;
  private long _minTokenValidity = DEFAULT_MIN_VALIDITY;

  public Keycloak( @Nonnull final KeycloakConfig config )
  {
    _config = Objects.requireNonNull( config );
  }

  public synchronized void setMinTokenValidity( final long minTokenValidity )
  {
    _minTokenValidity = minTokenValidity;
  }

  public long getMinTokenValidity()
  {
    return _minTokenValidity;
  }

  @Nullable
  public String getAccessTokenString()
  {
    final AccessTokenResponse token = getAccessToken();
    return null == token ? null : token.getToken();
  }

  @Nullable
  public synchronized AccessTokenResponse getAccessToken()
  {
    if ( null == _currentToken )
    {
      return grantToken();
    }
    else if ( tokenExpired() )
    {
      return refreshToken();
    }
    else
    {
      return _currentToken;
    }
  }

  /**
   * Invalidate token if it still patches current token.
   */
  public void invalidate( @Nonnull final String token )
  {
    if ( null != _currentToken && _currentToken.getToken().equals( token ) )
    {
      expireToken();
    }
  }

  /**
   * Call token service to refresh token, and then attempt to grant token if the refresh fails.
   */
  @Nullable
  private AccessTokenResponse refreshToken()
  {
    assert null != _currentToken;
    getAccessToken( refreshTokenParameters() );
    return null != _currentToken ? _currentToken : grantToken();
  }

  /**
   * Call token service to grant token.
   */
  @Nullable
  private AccessTokenResponse grantToken()
  {
    return getAccessToken( grantTokenParameters() );
  }

  /**
   * Retrieve and cache token, calculating expiration time.
   * If request fails then ensure current token is expired and return null.
   */
  @Nullable
  private synchronized AccessTokenResponse getAccessToken( @Nonnull final MultivaluedMap<String, String> parameters )
  {
    try
    {
      final int requestTime = Time.currentTime();
      _currentToken = callTokenService( parameters );
      if ( null == _currentToken )
      {
        _expirationTime = 0;
      }
      else
      {
        _expirationTime = requestTime + _currentToken.getExpiresIn();
        /*
         * Manually expire token if passed back and is invalid.
         * In theory this should never happen but some versions of keycloak will return invalid token. Possibly
         * due to temporary skews in clocks or bugs in either keycloak or this code. This code avoids the scenario
         * where an invalid refresh token is used to attempt to reconnect and it gets into a terminal failure loop.
         */
        if ( tokenExpired() || null == _currentToken.getToken() )
        {
          expireToken();
        }
      }
      return _currentToken;
    }
    catch ( final BadRequestException ignored )
    {
      expireToken();
      return null;
    }
  }

  /**
   * Expire the token.
   */
  private void expireToken()
  {
    _currentToken = null;
    _expirationTime = -1;
  }

  /**
   * Invoke the token web service with specified parameters.
   */
  @Nullable
  private AccessTokenResponse callTokenService( @Nonnull final MultivaluedMap<String, String> parameters )
  {
    final ClientBuilder builder =
      ClientBuilder.newBuilder().register( JacksonFeature.class );
    final String clientSecret = _config.getClientSecret();
    if ( null != clientSecret )
    {
      builder.register( new BasicAuthFilter( _config.getClientID(), clientSecret ) );
    }
    final Client client = builder.build();
    try
    {
      final WebTarget target = client.
        target( _config.getServerUrl() ).
        path( "/realms/" ).path( _config.getRealm() ).path( "/protocol/openid-connect/token" );
      final Response response = target.
        request( MediaType.APPLICATION_FORM_URLENCODED ).
        accept( MediaType.APPLICATION_JSON ).
        post( Entity.form( parameters ) );
      if ( Response.Status.Family.SUCCESSFUL == response.getStatusInfo().getFamily() )
      {
        return response.readEntity( AccessTokenResponse.class );
      }
      else
      {
        return null;
      }
    }
    finally
    {
      client.close();
    }
  }

  /**
   * Generate parameters required for initial token grant.
   */
  @Nonnull
  private MultivaluedMap<String, String> grantTokenParameters()
  {
    final MultivaluedHashMap<String, String> results = initParameters();
    results.put( OAuth2Constants.GRANT_TYPE, Collections.singletonList( _config.getGrantType() ) );
    results.put( "username", Collections.singletonList( _config.getUsername() ) );
    results.put( "password", Collections.singletonList( _config.getPassword() ) );
    return results;
  }

  /**
   * Generate parameters required for token refresh request.
   */
  @Nonnull
  private MultivaluedMap<String, String> refreshTokenParameters()
  {
    assert null != _currentToken;
    final MultivaluedHashMap<String, String> results = initParameters();
    results.put( OAuth2Constants.GRANT_TYPE, Collections.singletonList( OAuth2Constants.REFRESH_TOKEN ) );
    results.put( OAuth2Constants.REFRESH_TOKEN, Collections.singletonList( _currentToken.getRefreshToken() ) );
    return results;
  }

  /**
   * Initialize parameters, ensuring clientID is set if required.
   */
  @Nonnull
  private MultivaluedHashMap<String, String> initParameters()
  {
    final MultivaluedHashMap<String, String> results = new MultivaluedHashMap<>();
    if ( _config.isPublicClient() )
    {
      results.put( OAuth2Constants.CLIENT_ID, Collections.singletonList( _config.getClientID() ) );
    }
    return results;
  }

  /**
   * Return true if the token has expired.
   */

  private synchronized boolean tokenExpired()
  {
    return ( Time.currentTime() + _minTokenValidity ) >= _expirationTime;
  }
}
