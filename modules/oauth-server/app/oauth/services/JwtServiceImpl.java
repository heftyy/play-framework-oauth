package oauth.services;

import common.repository.Repository;
import oauth.jwt.JsonWebToken;
import oauth.jwt.JsonWebTokenClaim;
import oauth.jwt.JsonWebTokenHeader;
import oauth.models.OAuthClient;
import org.apache.commons.codec.binary.Base64;
import play.libs.Json;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.*;
import java.security.spec.*;

public class JwtServiceImpl implements JwtService {

    private final Repository<OAuthClient> clientRepository;

    @Inject
    public JwtServiceImpl(Repository<OAuthClient> clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public JsonWebToken getWebToken(String assertion)
            throws UnsupportedEncodingException {
        String claimBase64 = assertion.split("\\.")[1];

        String decodedClaimBytes = new String(Base64.decodeBase64(claimBase64.getBytes("UTF-8")));

        JsonWebTokenHeader header = new JsonWebTokenHeader("RSA256", "alg");
        JsonWebTokenClaim claim = Json.fromJson(Json.parse(decodedClaimBytes), JsonWebTokenClaim.class);

        return new JsonWebToken(header, claim, getSignedJwt(assertion));
    }

    @Override
    public boolean validateJWT(JsonWebToken webToken)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException {

        String jwt =
                Base64.encodeBase64URLSafeString(webToken.getHeader().toString().getBytes("UTF-8")) +
                "." +
                Base64.encodeBase64URLSafeString(webToken.getClaim().toString().getBytes("UTF-8"));

        PublicKey publicKey = getPublicKey(webToken.getClaim().getAccessorId());

        if (publicKey == null) {
            play.Logger.error("Didn't find the key for user with id " + webToken.getClaim().getAccessorId());
            return false;
        }

        return verifySignature(jwt, webToken.getSignedJwt(), publicKey);
    }

    private String getSignedJwt(String assertion) throws UnsupportedEncodingException {
        return assertion.split("\\.")[2];
    }

    private boolean verifySignature(String jwt, String signedJwt, PublicKey publicKey)
            throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, SignatureException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(jwt.getBytes("UTF-8"));

        final byte[] bytes = Base64.decodeBase64(URLDecoder.decode(signedJwt, "UTF-8"));

        return signature.verify(bytes);
    }

    private PublicKey getPublicKey(String accessorId)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        OAuthClient oAuthClient = clientRepository.findByField("accessorId", accessorId);

        byte[] publicKeyBytes;
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        publicKeyBytes = Base64.decodeBase64(oAuthClient.getPublicKey());
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        return keyFactory.generatePublic(publicKeySpec);
    }
}
