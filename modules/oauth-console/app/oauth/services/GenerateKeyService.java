package oauth.services;

import oauth.models.OAuthClient;
import org.bouncycastle.operator.OperatorCreationException;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public interface GenerateKeyService {
    void generateKey(OAuthClient client) throws CertificateException, NoSuchAlgorithmException, OperatorCreationException, KeyStoreException, SignatureException, NoSuchProviderException, InvalidKeyException, IOException;
    String getSecretKeyPath(String accessorId);
}
