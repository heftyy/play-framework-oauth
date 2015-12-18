package oauth.jwt;

import common.models.AbstractModel;

public class JsonWebToken extends AbstractModel {
    JsonWebTokenHeader header;
    JsonWebTokenClaim claim;
    String signedJwt;

    public JsonWebToken(JsonWebTokenHeader header, JsonWebTokenClaim claim) {
        this.header = header;
        this.claim = claim;
    }

    public JsonWebToken(JsonWebTokenHeader header, JsonWebTokenClaim claim, String signedJwt) {
        this(header, claim);
        this.signedJwt = signedJwt;
    }

    public JsonWebToken(String alg, String type, String accessorId, String domain, String scope, String authUrl, Long time) {
        this.header = new JsonWebTokenHeader(alg, type);
        this.claim = new JsonWebTokenClaim(accessorId, domain, scope, authUrl, time);
    }

    public JsonWebTokenHeader getHeader() {
        return header;
    }

    public void setHeader(JsonWebTokenHeader header) {
        this.header = header;
    }

    public JsonWebTokenClaim getClaim() {
        return claim;
    }

    public void setClaim(JsonWebTokenClaim claim) {
        this.claim = claim;
    }

    public String getSignedJwt() {
        return signedJwt;
    }

    public void setSignedJwt(String signedJwt) {
        this.signedJwt = signedJwt;
    }
}
