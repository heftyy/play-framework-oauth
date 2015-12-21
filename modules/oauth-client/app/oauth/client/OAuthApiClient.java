package oauth.client;

import play.libs.ws.WSResponse;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public interface OAuthApiClient {
    WSResponse doPost(String scopeUrl, String args) throws Exception;
    WSResponse doGet(String scopeUrl) throws Exception;
}
