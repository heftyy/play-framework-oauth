package oauth.client;

import play.libs.ws.WSResponse;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public interface OAuthApiClient {
    WSResponse doPost(String scopeUrl, String args) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyStoreException, SignatureException, InvalidKeyException;
    WSResponse doGet(String scopeUrl) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyStoreException, SignatureException, InvalidKeyException;
}
