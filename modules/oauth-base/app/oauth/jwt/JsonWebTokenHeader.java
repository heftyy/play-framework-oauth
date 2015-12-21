package oauth.jwt;

import common.models.AbstractModel;

import java.util.Objects;

public class JsonWebTokenHeader extends AbstractModel {
    private String alg;
    private String type;

    public JsonWebTokenHeader() {}

    public JsonWebTokenHeader(String alg, String type) {
        this.alg = alg;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonWebTokenHeader that = (JsonWebTokenHeader) o;
        return Objects.equals(alg, that.alg) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alg, type);
    }

    public String getAlg() {
        return alg;
    }

    public void setAlg(String alg) {
        this.alg = alg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
