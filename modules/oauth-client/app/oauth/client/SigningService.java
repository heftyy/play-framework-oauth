package oauth.client;

import oauth.jwt.JsonWebToken;

public interface SigningService {
    String signJwt(JsonWebToken jwt) throws Exception;
}
