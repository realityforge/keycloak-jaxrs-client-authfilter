package org.realityforge.keycloak.client.authfilter;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.json.JsonObject;

@SuppressWarnings( { "WeakerAccess", "unused" } )
public final class AccessTokenResponse
{
  @Nonnull
  private final JsonObject _object;

  public AccessTokenResponse( @Nonnull final JsonObject object )
  {
    _object = Objects.requireNonNull( object );
  }

  @Nonnull
  public String getTokenType()
  {
    return _object.getString( "token_type" );
  }

  @Nonnull
  public String getAccessToken()
  {
    return _object.getString( "access_token" );
  }

  public long getExpiresIn()
  {
    return _object.isNull( "expires_in" ) ? 0L : _object.getJsonNumber( "expires_in" ).longValue();
  }

  @Nullable
  public String getRefreshToken()
  {
    return _object.getString( "refresh_token", null );
  }

  public long getRefreshExpiresIn()
  {
    return _object.isNull( "refresh_expires_in" ) ? 0L : _object.getJsonNumber( "refresh_expires_in" ).longValue();
  }

  @Nonnull
  public JsonObject getObject()
  {
    return _object;
  }
}
