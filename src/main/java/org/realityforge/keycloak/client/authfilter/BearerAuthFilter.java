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

import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

/**
 * Jaxrs client filter that sets the Bearer header when request made
 * and invalidates the token if http request is unauthorized.
 */
public final class BearerAuthFilter
  implements ClientRequestFilter, ClientResponseFilter
{
  private static final String AUTH_HEADER_PREFIX = "Bearer ";

  @Nonnull
  private final Keycloak _keycloak;

  public BearerAuthFilter( @Nonnull final Keycloak keycloak )
  {
    _keycloak = Objects.requireNonNull( keycloak );
  }

  @Override
  public void filter( final ClientRequestContext requestContext )
  {
    final String accessToken = _keycloak.getAccessTokenString();
    if ( null != accessToken )
    {
      requestContext.getHeaders().add( HttpHeaders.AUTHORIZATION, AUTH_HEADER_PREFIX + accessToken );
    }
  }

  @Override
  public void filter( final ClientRequestContext requestContext, final ClientResponseContext responseContext )
  {
    if ( Response.Status.UNAUTHORIZED.getStatusCode() == responseContext.getStatus() )
    {
      final List<Object> headers = requestContext.getHeaders().get( HttpHeaders.AUTHORIZATION );
      if ( null == headers )
      {
        return;
      }
      for ( final Object header : headers )
      {
        if ( header instanceof String )
        {
          final String headerValue = (String) header;
          if ( headerValue.startsWith( AUTH_HEADER_PREFIX ) )
          {
            final String token = headerValue.substring( AUTH_HEADER_PREFIX.length() );
            _keycloak.invalidate( token );
          }
        }
      }
    }
  }
}
