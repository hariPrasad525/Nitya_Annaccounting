package com.vimukti.accounter.developer.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.nitya.accounter.main.ServerConfiguration;

public class PublicKeyGenerator {

	/**
	 * @param args
	 * @throws NoSuchProviderException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws IOException
	 * @throws CertificateException
	 * @throws KeyStoreException
	 * @throws UnrecoverableEntryException
	 * @throws URISyntaxException
	 */
	public static void main(String[] args) throws KeyStoreException,
			NoSuchAlgorithmException, CertificateException, IOException,
			UnrecoverableEntryException {
		FileInputStream is = new FileInputStream(
				ServerConfiguration.getConfig() + File.separator
						+ "license.keystore");
		KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		String password = "***REMOVED***";
		keystore.load(is, password.toCharArray());
		PrivateKeyEntry entry = (PrivateKeyEntry) keystore.getEntry("vimukti",
				new PasswordProtection(ServerConfiguration
						.getLicenseKeystorePWD().toCharArray()));
		Key key = entry.getPrivateKey();
		System.out.println((PrivateKey) key);
		PublicKey publicKey = entry.getCertificate().getPublicKey();
		System.out.println(publicKey);

		byte[] encoded = publicKey.getEncoded();
		byte[] encodeBase64 = Base64.encodeBase64(encoded);
		System.out.println("Public Key:" + new String(encodeBase64));

	}

	private static void generate() throws NoSuchAlgorithmException,
			NoSuchProviderException, InvalidKeySpecException,
			KeyStoreException, CertificateException, IOException,
			URISyntaxException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		random.setSeed("VimTech".getBytes("UTF-8"));
		keyGen.initialize(1024, random);

		KeyPair pair = keyGen.generateKeyPair();
		PrivateKey priv = pair.getPrivate();
		PublicKey pub = pair.getPublic();
		System.out.println(priv);
		System.out.println(pub);

		byte[] encoded = pub.getEncoded();
		byte[] encodeBase64 = Base64.encodeBase64(encoded);
		System.out.println("Public Key:" + new String(encodeBase64));

		byte[] encodedPrv = priv.getEncoded();
		byte[] encodeBase64Prv = Base64.encodeBase64(encodedPrv);
		System.out.println("Private Key:" + new String(encodeBase64Prv));

		byte[] decodeBase64 = Base64.decodeBase64(encodeBase64);
		X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(decodeBase64);
		KeyFactory keyFactory = KeyFactory.getInstance("DSA");

		System.out.println(keyFactory.generatePublic(pubKeySpec).equals(pub));
	}

}
