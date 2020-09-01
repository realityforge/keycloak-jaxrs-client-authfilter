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
  private static final int DEFAULT_CONNECT_TIMEOUT_IN_MILLIS = 5000;
  private static final int DEFAULT_READ_TIMEOUT_IN_MILLIS = 5000;
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
  private final int _connectTimeoutInMillis;
  private final int _readTimeoutInMillis;

  @Nonnull
  public static KeycloakConfig createPasswordConfig( @Nonnull final String serverUrl,
                                                     @Nonnull final String realm,
                                                     @Nonnull final String clientID,
                                                     @Nonnull final String username,
                                                     @Nonnull final String password )
  {
    return createPasswordConfig( serverUrl, realm, clientID, username, password, null );
  }

  @Nonnull
  public static KeycloakConfig createPasswordConfig( @Nonnull final String serverUrl,
                                                     @Nonnull final String realm,
                                                     @Nonnull final String clientID,
                                                     @Nonnull final String username,
                                                     @Nonnull final String password,
                                                     @Nullable final String clientSecret )
  {
    return createPasswordConfig( serverUrl,
                                 realm,
                                 clientID,
                                 username,
                                 password,
                                 clientSecret,
                                 DEFAULT_CONNECT_TIMEOUT_IN_MILLIS,
                                 DEFAULT_READ_TIMEOUT_IN_MILLIS );
  }

  @Nonnull
  public static KeycloakConfig createPasswordConfig( @Nonnull final String serverUrl,
                                                     @Nonnull final String realm,
                                                     @Nonnull final String clientID,
                                                     @Nonnull final String username,
                                                     @Nonnull final String password,
                                                     @Nullable final String clientSecret,
                                                     final int connectTimeoutInMillis,
                                                     final int readTimeoutInMillis )
  {
    return new KeycloakConfig( serverUrl,
                               realm,
                               PASSWORD,
                               clientID,
                               username,
                               password,
                               clientSecret,
                               connectTimeoutInMillis,
                               readTimeoutInMillis );
  }

  private KeycloakConfig( @Nonnull final String serverUrl,
                          @Nonnull final String realm,
                          @Nonnull final String grantType,
                          @Nonnull final String clientID,
                          @Nullable final String username,
                          @Nullable final String password,
                          @Nullable final String clientSecret,
                          final int connectTimeoutInMillis,
                          final int readTimeoutInMillis )
  {
    assert connectTimeoutInMillis >= 0;
    assert readTimeoutInMillis >= 0;
    _serverUrl = Objects.requireNonNull( serverUrl );
    _realm = Objects.requireNonNull( realm );
    _grantType = Objects.requireNonNull( grantType );
    _clientID = Objects.requireNonNull( clientID );
    _connectTimeoutInMillis = connectTimeoutInMillis;
    _readTimeoutInMillis = readTimeoutInMillis;
    if ( !PASSWORD.equals( grantType ) && !CLIENT_CREDENTIALS.equals( grantType ) )
    {
      final String message =
        "Unsupported grantType: " + grantType + " Valid grant types: " + PASSWORD + " and " + CLIENT_CREDENTIALS;
      throw new IllegalArgumentException( message );
    }

    if ( PASSWORD.equals( grantType ) )
    {
      _username = Objects.requireNonNull( username );
      _password = Objects.requireNonNull( password );
      _clientSecret = clientSecret;
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

  public int getConnectTimeoutInMillis()
  {
    return _connectTimeoutInMillis;
  }

  public int getReadTimeoutInMillis()
  {
    return _readTimeoutInMillis;
  }
}
