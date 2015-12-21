package oauth.client;

import oauth.jwt.JsonWebToken;
import org.apache.commons.net.util.Base64;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public class PrivateKeySigningService implements SigningService {
    private String keyFile;
    private String password;

    public PrivateKeySigningService(String keyFile, String password) {
        this.keyFile = keyFile;
        this.password = password;
    }

    @Override
    public String signJwt(JsonWebToken jwt) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyStoreException, SignatureException, InvalidKeyException {
        StringBuilder token = new StringBuilder();

        token.append(Base64.encodeBase64URLSafeString(jwt.getHeader().toString().getBytes("UTF-8")));
        token.append(".");
        token.append(Base64.encodeBase64URLSafeString(jwt.getClaim().toString().getBytes("UTF-8")));

        KeyStore keystore = KeyStore.getInstance("PKCS12");
        keystore.load(new FileInputStream(keyFile), password.toCharArray());

        PrivateKey privateKey = (PrivateKey) keystore.getKey("oauth", password.toCharArray());
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(token.toString().getBytes("UTF-8"));
        String signedPayload = Base64.encodeBase64URLSafeString(signature.sign());
        token.append(".");
        token.append(signedPayload);

        return token.toString();
    }
}
