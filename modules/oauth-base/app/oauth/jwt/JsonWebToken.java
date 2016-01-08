package oauth.jwt;

import common.models.JsonSerializable;

import java.util.Objects;

public class JsonWebToken extends JsonSerializable {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonWebToken webToken = (JsonWebToken) o;
        return Objects.equals(header, webToken.header) &&
                Objects.equals(claim, webToken.claim);
    }

    @Override
    public int hashCode() {
        return Objects.hash(header, claim);
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
