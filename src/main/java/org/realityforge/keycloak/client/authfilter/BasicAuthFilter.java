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

import java.util.Base64;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;

@SuppressWarnings( "WeakerAccess" )
public final class BasicAuthFilter
  implements ClientRequestFilter
{
  private final String _username;
  private final String _password;

  public BasicAuthFilter( @Nonnull final String username, @Nonnull final String password )
  {
    _username = Objects.requireNonNull( username );
    _password = Objects.requireNonNull( password );
  }

  @Override
  public void filter( final ClientRequestContext requestContext )
  {
    final String pair = _username + ":" + _password;
    final String header = "Basic " + Base64.getEncoder().encodeToString( pair.getBytes() );
    requestContext.getHeaders().add( HttpHeaders.AUTHORIZATION, header );
  }
}
