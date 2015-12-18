package oauth.services;

import oauth.models.OAuthClient;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.joda.time.DateTime;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

public class GeneratePkcsKeyService implements GenerateKeyService {
    private static final String ALIAS = "oauth";
    private static final String keyExtension = ".p12";
    private static final String BC = org.bouncycastle.jce.provider.BouncyCastleProvider.PROVIDER_NAME;

    private String PASSWORD_STRING;
    private String accessorId;

	public void generateKey(OAuthClient client) throws CertificateException, NoSuchAlgorithmException, OperatorCreationException, KeyStoreException, SignatureException, NoSuchProviderException, InvalidKeyException, IOException {
		generateAccessorId();
		createPassword();
		createCertificateAndSecret(client);
	}

	public String getSecretKeyPath(String accessorId) {
		return "files/keys/" + accessorId + keyExtension;
	}

	/**
	 * Generates a secret key in PKCS12 format (.p12) with bouncycastle library.
	 * Uses SHA256WithRSAEncryption algorithm. After creating the key opens it
	 * to test if it works and then saves a new client to the database using the
	 * name and ip given in the constructor.
	 * 
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws OperatorCreationException
	 * @throws CertificateException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 * @throws KeyStoreException
	 * @throws IOException
	 */
	private void createCertificateAndSecret(OAuthClient client) throws NoSuchAlgorithmException, NoSuchProviderException, OperatorCreationException, CertificateException,
			InvalidKeyException, SignatureException, KeyStoreException, IOException {

		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

		keyPairGenerator.initialize(2048);
		KeyPair kPair = keyPairGenerator.generateKeyPair();

		KeyPairGenerator kpGen = KeyPairGenerator.getInstance("RSA", "BC");
		kpGen.initialize(2048, new SecureRandom());
		KeyPair pair = kpGen.generateKeyPair();

		// Generate self-signed certificate
		X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
		builder.addRDN(BCStyle.OU, "play-oauth");
		builder.addRDN(BCStyle.O, "play-oauth");
		builder.addRDN(BCStyle.CN, "play-oauth");

		Date notBefore = new Date(System.currentTimeMillis() - (3600 * 24 * 365 * 1000));
		Date notAfter = new Date(System.currentTimeMillis() + (3600 * 24 * 365 * 1000));
		BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());

		X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(
				builder.build(),
				serial,
				notBefore,
				notAfter,
				builder.build(),
				pair.getPublic()
		);

		ContentSigner sigGen = new JcaContentSignerBuilder("SHA256WithRSAEncryption").setProvider(BC).build(pair.getPrivate());
		X509Certificate cert = new JcaX509CertificateConverter().setProvider(BC).getCertificate(certGen.build(sigGen));
		cert.checkValidity(new Date());
		cert.verify(cert.getPublicKey());

        File f = new File("files/keys");
        Files.createDirectories(f.toPath());

		KeyStore keystore = KeyStore.getInstance("PKCS12");
		keystore.load(null, null);
		keystore.setKeyEntry(ALIAS, kPair.getPrivate(), PASSWORD_STRING.toCharArray(), new X509Certificate[] { cert });
		keystore.store(new FileOutputStream(getSecretKeyPath(accessorId)), PASSWORD_STRING.toCharArray());

        byte[] publicKeyBytes = kPair.getPublic().getEncoded();
        String base64PublicKey = Base64.encodeBase64URLSafeString(publicKeyBytes);

		// Open the new key and if it works save to db.
		try {
			client.setCreationTime(DateTime.now());
			client.setPublicKey(base64PublicKey);
			client.setAccessorId(accessorId);
			client.setPassword(PASSWORD_STRING);
		} catch (Exception e) {
			System.err.println("Failed to load the keystore after creation: " + e.getLocalizedMessage());
		}
	}

	/**
	 * Creates the Clients UUID using randomUUID() from java.
	 */
	private void generateAccessorId() {
		accessorId = java.util.UUID.randomUUID().toString();
	}

	/**
	 * Creates a random password that will be used to open the secret key
	 * string.
	 */
	private void createPassword() {
		PASSWORD_STRING = RandomStringUtils.randomAlphanumeric(20).toUpperCase();
	}
}