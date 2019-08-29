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

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This class is the immutable configuration required to access keycloak server.
 */
@SuppressWarnings( { "WeakerAccess", "unused" } )
public final class KeycloakConfig
{
  @Nonnull
  private static final String PASSWORD = "password";
  @Nonnull
  private static final String CLIENT_CREDENTIALS = "client_credentials";
  @Nonnull
  private final String _serverUrl;
  @Nonnull
  private final String _realm;
  @Nonnull
  private final String _grantType;
  @Nullable
  private final String _username;
  @Nullable
  private final String _password;
  @Nonnull
  private final String _clientID;
  @Nullable
  private final String _clientSecret;

  @Nonnull
  public static KeycloakConfig createPasswordConfig( @Nonnull final String serverUrl,
                                                     @Nonnull final String realm,
                                                     @Nonnull final String clientID,
                                                     @Nonnull final String username,
                                                     @Nonnull final String password )
  {
    return new KeycloakConfig( serverUrl, realm, PASSWORD, clientID, username, password, null );
  }

  private KeycloakConfig( @Nonnull final String serverUrl,
                          @Nonnull final String realm,
                          @Nonnull final String grantType,
                          @Nonnull final String clientID,
                          @Nullable final String username,
                          @Nullable final String password,
                          @Nullable final String clientSecret )
  {
    _serverUrl = Objects.requireNonNull( serverUrl );
    _realm = Objects.requireNonNull( realm );
    _grantType = Objects.requireNonNull( grantType );
    _clientID = Objects.requireNonNull( clientID );
    if ( !PASSWORD.equals( grantType ) && !CLIENT_CREDENTIALS.equals( grantType ) )
    {
      final String message =
        "Unsupported grantType: " + grantType + " Valid grant types: " + PASSWORD + " and " + CLIENT_CREDENTIALS;
      throw new IllegalArgumentException( message );
    }

    if ( PASSWORD.equals( grantType ) )
    {
      assert null == clientSecret;
      _username = Objects.requireNonNull( username );
      _password = Objects.requireNonNull( password );
      _clientSecret = null;
    }
    else
    {
      assert null == username;
      assert null == password;
      _username = null;
      _password = null;
      _clientSecret = Objects.requireNonNull( clientSecret );
    }
  }

  @Nonnull
  public String getServerUrl()
  {
    return _serverUrl;
  }

  @Nonnull
  public String getRealm()
  {
    return _realm;
  }

  @Nonnull
  public String getGrantType()
  {
    return _grantType;
  }

  @Nonnull
  public String getClientID()
  {
    return _clientID;
  }

  @Nullable
  public String getUsername()
  {
    return _username;
  }

  @Nullable
  public String getPassword()
  {
    return _password;
  }

  @Nullable
  public String getClientSecret()
  {
    return _clientSecret;
  }

  public boolean isPublicClient()
  {
    return _clientSecret == null;
  }
}
