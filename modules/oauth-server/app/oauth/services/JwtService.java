package oauth.services;

import oauth.jwt.JsonWebToken;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

public interface JwtService {
    JsonWebToken getWebToken(String assertion) throws UnsupportedEncodingException;
    boolean validateJWT(JsonWebToken jwt) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException;
}
