package oauth.jwt;

import common.models.AbstractModel;

public class JsonWebTokenHeader extends AbstractModel {
    private String alg;
    private String type;

    public JsonWebTokenHeader() {}

    public JsonWebTokenHeader(String alg, String type) {
        this.alg = alg;
        this.type = type;
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
