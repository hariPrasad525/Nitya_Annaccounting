package com.vimukti.accounter.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.google.gdata.util.common.util.Base64;
import com.vimukti.accounter.main.CipherThreadLocal;
import com.vimukti.accounter.main.ServerConfiguration;

public class EU {
	private static Map<String, ByteArrayWrapper> keys = new HashMap<String, EU.ByteArrayWrapper>();
	private static PublicKey publicKey;

	public synchronized static String d(byte[] in) {
		CipherCouple cipherCouple = CipherThreadLocal.get();
		if (cipherCouple == null) {
			return toUTF8String(in);
		}
		Cipher cipher = cipherCouple.dCipher;
		try {
			return toUTF16String(cipher.doFinal(in));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String toUTF16String(byte[] value) {
		try {
			String string = new String(value, "UTF-16");
			return string;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String toUTF8String(byte[] value) {
		try {
			String string = new String(value, "UTF-8");
			return string;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public synchronized static byte[] e(String in) {
		CipherCouple cipherCouple = CipherThreadLocal.get();
		if (cipherCouple == null) {
			return fromUTF8String(in);
		}
		Cipher cipher = cipherCouple.eCipher;
		try {
			return cipher.doFinal(fromUTF16String(in));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static byte[] fromUTF8String(String value) {
		try {
			byte[] bytes = value.getBytes("UTF-8");
			return bytes;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static byte[] fromUTF16String(String value) {
		try {
			byte[] bytes = value.getBytes("UTF-16");
			return bytes;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] generatePBS(String password) {
		byte[] salt = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7 };
		SecretKeyFactory factory;
		try {
			factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 1024,
					128);
			SecretKey tmp = factory.generateSecret(spec);
			return tmp.getEncoded();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] generateSymetric() throws Exception {
		KeyGenerator generator = KeyGenerator.getInstance("AES");
		generator.init(128);
		byte[] encoded = generator.generateKey().getEncoded();
		return encoded;
	}

	public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		Cipher cipher = Cipher.getInstance("AES");
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] bytes = cipher.doFinal(data);
		return bytes;
	}

	public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
		Cipher cipher = Cipher.getInstance("AES");
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] bytes = cipher.doFinal(data);
		return bytes;
	}

	public static class ByteArrayWrapper {
		byte[] data;

		public ByteArrayWrapper(byte[] s2) {
			data = s2;
		}
	}

	public static class CipherCouple {
		Cipher eCipher;
		Cipher dCipher;
	}

	public static void storeKey(byte[] s2, String sessionId) {
		ByteArrayWrapper byteArrayWrapper = new ByteArrayWrapper(s2);
		keys.put(sessionId, byteArrayWrapper);
	}

	public static byte[] generateD2(String password, String emailId,
			String sessionId) throws Exception {
		byte[] s1 = EU.generatePBS(password);
		byte[] s2 = getKey(sessionId);
		if (s2 == null) {
			s2 = EU.generateSymetric();
			EU.storeKey(s2, sessionId);
		}
		byte[] d2 = EU.encrypt(s1, s2);
		return d2;
	}

	/**
	 * s2
	 * 
	 * @param emailID
	 * @return
	 */
	public static byte[] getKey(String sessionId) {
		ByteArrayWrapper byteArrayWrapper = keys.get(sessionId);
		if (byteArrayWrapper == null) {
			return null;
		}
		return byteArrayWrapper.data;
	}

	public static void removeCipher() {
		CipherThreadLocal.set(null);
	}

	public static void createCipher(byte[] userSecret, byte[] d2,
			String sessionId) throws Exception {
		byte[] s2 = getKey(sessionId);
		if (s2 == null) {
			return;
		}
		byte[] s1 = decrypt(d2, s2);
		byte[] key = decrypt(userSecret, s1);

		createCipher(key);
	}

	public static void createCipher(byte[] companySecret, byte[] pbs)
			throws Exception {
		byte[] key = decrypt(companySecret, pbs);
		createCipher(key);
	}

	private static void createCipher(byte[] key) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
		Cipher eCipher = Cipher.getInstance("AES");
		eCipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		Cipher dCipher = Cipher.getInstance("AES");
		dCipher.init(Cipher.DECRYPT_MODE, skeySpec);
		CipherCouple cipherCouple = new CipherCouple();
		cipherCouple.dCipher = dCipher;
		cipherCouple.eCipher = eCipher;
		CipherThreadLocal.set(cipherCouple);
	}

	public static String decryptAccounter(String parameter) {
		byte[] generatePBS = generatePBS(ServerConfiguration.getAdminPassword());
		try {
			byte[] bytes = Base64.decodeWebSafe(parameter);
			byte[] encrypt = decrypt(bytes, generatePBS);
			return new String(encrypt);
		} catch (Exception e) {
		}
		return null;
	}

	public static String encryptAccounter(String emailId) {
		byte[] generatePBS = generatePBS(ServerConfiguration.getAdminPassword());
		byte[] bytes = emailId.getBytes();
		try {
			byte[] encrypt = encrypt(bytes, generatePBS);
			String encode = Base64.encodeWebSafe(encrypt, true);
			return encode;
		} catch (Exception e) {
		}
		return null;
	}

	public static void removeKey(String sessionId) {
		keys.remove(sessionId);
	}

	public static byte[] encryptPassword(String password) {
		try {
			PublicKey pubKey = readPublicKeyFromFile();
			if (pubKey == null) {
				return null;
			}
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			byte[] cipherData = cipher.doFinal(password.getBytes());
			return cipherData;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String decryptPassword(byte[] password,
			String keyStorePassword) {
		try {
			PrivateKey pubKey = readPrivateKeyFromFile(keyStorePassword);
			if (pubKey == null) {
				return null;
			}
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, pubKey);
			byte[] cipherData = cipher.doFinal(password);
			return new String(cipherData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static PrivateKey readPrivateKeyFromFile(String keyStorePassword)
			throws Exception {
		FileInputStream is = new FileInputStream(
				ServerConfiguration.getConfig() + File.separator + ".keystore");
		KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		String password = "***REMOVED***";
		keystore.load(is, password.toCharArray());
		Key key = keystore.getKey(ServerConfiguration.getCertificateAlias(),
				keyStorePassword.toCharArray());
		return (PrivateKey) key;
	}

	private static PublicKey readPublicKeyFromFile() throws Exception {
		if (publicKey != null) {
			return publicKey;
		}
		FileInputStream is = new FileInputStream(
				ServerConfiguration.getConfig() + File.separator + ".keystore");
		KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		String password = "publicKeyPassword";
		keystore.load(is, password.toCharArray());
		java.security.cert.Certificate cert = keystore
				.getCertificate(ServerConfiguration.getCertificateAlias());
		publicKey = cert.getPublicKey();
		return publicKey;
	}

	public static boolean hasChiper() {
		return CipherThreadLocal.get() != null;
	}

}
