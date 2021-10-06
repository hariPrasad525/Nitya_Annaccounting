package com.nitya.accounter.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

/**
 * Type Security
 * 
 * @author Rajesh Akkineni
 * @version 1.0
 */
public class Security {
	/**
	 * Call this when an identity gets an Invitation to space. Public part
	 * should be sent along with the invitation acceptence message. Or When we
	 * create a space. Public part of this pair will be saved in IMember for all
	 * members of a space For us (not sure where to save) we should save both
	 * parts.
	 * 
	 * @param spaceid
	 * @param signKey
	 * @param encKey
	 * @return
	 */
	private static Logger LOG = Logger.getLogger(Security.class);

	public static KeyPair createDHKeyPairForSpace(String spaceid, Key signKey,
			Key encKey, DHParameterSpec spec) {
		try {
			KeyPairGenerator keygen = KeyPairGenerator.getInstance("DH", "BC"); //$NON-NLS-1$ //$NON-NLS-2$
			byte[] bsp = spaceid.getBytes();
			byte[] bsign = signKey.getEncoded();
			byte[] benc = encKey.getEncoded();

			byte b[] = new byte[bsp.length + bsign.length + benc.length];

			System.arraycopy(bsp, 0, b, 0, bsp.length);
			System.arraycopy(bsign, 0, b, bsp.length, bsign.length);

			System.arraycopy(benc, 0, b, (bsp.length) + (bsign.length),
					benc.length);

			System.arraycopy(benc, 0, b, bsp.length + bsign.length, benc.length);

			SecureRandom ran = new SecureRandom(b);
			if (spec != null) {
				keygen.initialize(spec);
			} else {
				keygen.initialize(256, ran);
			}
			return keygen.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		} catch (NoSuchProviderException e) {
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		} catch (InvalidAlgorithmParameterException e) {
			//
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		}
		return null;
	}

	public static byte[] getBytes(SecretKey key) {
		return key.getEncoded();
	}

	/**
	 * Method createEncryptionKeyPair Should be called when the identity is
	 * created. The keypair should be saved along with it
	 * 
	 * @param bits
	 * @return KeyPair
	 */
	public static KeyPair createEncryptionKeyPair() {
		try {
			KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA", "BC"); //$NON-NLS-1$ //$NON-NLS-2$
			SecureRandom ran = new SecureRandom();
			keygen.initialize(2048, ran);
			return keygen.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		} catch (NoSuchProviderException e) {
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		}
		return null;
	}

	/**
	 * Should be used as password for database Orginal password should never be
	 * used anyware
	 * 
	 * @param password
	 * @return
	 */
	// public static SecretKey createPasswordKey(String accId, String password)
	// {
	// KeyGenerator pKCS5_PBKDF2 = null;
	// SecureRandom ran = new SecureRandom();
	// byte[] b = accId.getBytes();
	// try {
	// pKCS5_PBKDF2 = KeyGenerator.getInstance("PBKDF2", "BC");
	//
	// PBEKeyAndParameterSpec parameterSpec;
	// parameterSpec = new PBEKeyAndParameterSpec(password.getBytes(), b,
	// 192, 192);
	// pKCS5_PBKDF2.init(parameterSpec, ran);
	// } catch (InvalidAlgorithmParameterException e) {
	// LOG.error("Exception: ", e);
	// return null;
	// } catch (Exception e) {
	// LOG.error("Exception: ", e);
	// return null;
	// }
	// password = "";
	// return pKCS5_PBKDF2.generateKey();
	// }
	/**
	 * Method createSigningKeyPair Should be called when the identity is
	 * created. The keypair should be saved along with it
	 * 
	 * @param bits
	 * @return KeyPair
	 */
	public static KeyPair createSigningKeyPair() {
		try {
			KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA", "BC"); //$NON-NLS-1$ //$NON-NLS-2$
			SecureRandom ran = new SecureRandom();
			keygen.initialize(2048, ran);
			return keygen.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		} catch (NoSuchProviderException e) {
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		}
		return null;
	}

	/**
	 * Call this when an identity gets an Invitation to space. Public part
	 * should be sent along with the invitation acceptence message. Or When we
	 * create a space. Public part of this pair will be saved in IMember for all
	 * members of a space For us (not sure where to save) we should save both
	 * parts.
	 * 
	 * @return
	 */
	public static KeyPair createSignPairForSpace() {
		try {
			KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA", "BC"); //$NON-NLS-1$ //$NON-NLS-2$
			SecureRandom ran = new SecureRandom();
			keygen.initialize(2048, ran);
			return keygen.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		} catch (NoSuchProviderException e) {
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		}
		return null;
	}

	/**
	 * Call this in Space constructor And save it in database
	 * 
	 * @return
	 */
	public static SecretKey createSpaceEncKey() {
		KeyGenerator key_gen = null;
		try {
			key_gen = KeyGenerator.getInstance("AES", "BC"); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (Exception e) {
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
			return null;
		}

		return key_gen.generateKey();

	}

	/**
	 * Call this in Space constructor And save it in database
	 * 
	 * @return
	 */
	public static SecretKey createSpaceMacKey() {
		KeyGenerator key_gen = null;
		try {
			key_gen = KeyGenerator.getInstance("HmacSHA1", "BC"); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (Exception e) {
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
			return null;
		}
		return key_gen.generateKey();
	}

	/**
	 * Called when we get delta(info) that some new member is added to space.
	 * Save in IMemeber
	 * 
	 * @param dhPriKey
	 *            our Private DH key for this space
	 * @param dhPubKey
	 *            new members public DH key for this space
	 * @return
	 */
	public static SecretKey createSpaceMemberEncKey(PrivateKey dhPriKey,
			PublicKey dhPubKey) {
		try {
			KeyAgreement keya = KeyAgreement.getInstance("DH", "BC"); //$NON-NLS-1$ //$NON-NLS-2$
			keya.init(dhPriKey);
			keya.doPhase(dhPubKey, true);
			SecureRandom sr = new SecureRandom(keya.generateSecret());
			KeyGenerator key_gen = KeyGenerator.getInstance("AES", "BC"); //$NON-NLS-1$ //$NON-NLS-2$
			key_gen.init(sr);
			return key_gen.generateKey();
		} catch (NoSuchAlgorithmException e) {
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		} catch (InvalidKeyException e) {
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		} catch (IllegalStateException e) {
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		} catch (NoSuchProviderException e) {
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		}
		return null;
	}

	/**
	 * Called when we get delta(info) that some new member is added to space.
	 * Saved in IMemeber
	 * 
	 * @param dhPriKey
	 * @param dhPubKey
	 * @return
	 */
	public static SecretKey createSpaceMemberMacKey(PrivateKey dhPriKey,
			PublicKey dhPubKey) {
		try {
			KeyAgreement keya = KeyAgreement.getInstance("DH", "BC"); //$NON-NLS-1$ //$NON-NLS-2$
			keya.init(dhPriKey);
			keya.doPhase(dhPubKey, true);
			SecureRandom sr = new SecureRandom(keya.generateSecret());
			KeyGenerator key_gen = KeyGenerator.getInstance("HmacSHA1", "BC"); //$NON-NLS-1$ //$NON-NLS-2$
			key_gen.init(sr);
			return key_gen.generateKey();
		} catch (NoSuchAlgorithmException e) {
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		} catch (InvalidKeyException e) {
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		} catch (IllegalStateException e) {
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		} catch (NoSuchProviderException e) {
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		}
		return null;
	}

	public static byte[] getBytes(PrivateKey privateKey) {
		try {
			if (privateKey instanceof RSAPrivateKey) {
				RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) privateKey;
				ByteArrayOutputStream bout = new ByteArrayOutputStream(256);
				ObjectOutputStream oout = new ObjectOutputStream(bout);
				oout.writeObject(rsaPrivateKey.getModulus());
				oout.writeObject(rsaPrivateKey.getPrivateExponent());
				return bout.toByteArray();
			} else {
				DHPrivateKey dhPrivateKey = (DHPrivateKey) privateKey;
				ByteArrayOutputStream bout = new ByteArrayOutputStream(256);
				ObjectOutputStream oout = new ObjectOutputStream(bout);
				oout.writeObject(dhPrivateKey.getX());
				oout.writeObject(dhPrivateKey.getParams().getP());
				oout.writeObject(dhPrivateKey.getParams().getG());
				return bout.toByteArray();
			}
		} catch (IOException e) {
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		}
		return null;
	}

	public static byte[] getBytes(PublicKey publicKey) {
		try {
			if (publicKey instanceof RSAPublicKey) {
				RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
				ByteArrayOutputStream bout = new ByteArrayOutputStream(256);
				ObjectOutputStream oout = new ObjectOutputStream(bout);
				oout.writeObject(rsaPublicKey.getModulus());
				oout.writeObject(rsaPublicKey.getPublicExponent());
				return bout.toByteArray();

			} else {
				DHPublicKey dhPublicKey = (DHPublicKey) publicKey;
				ByteArrayOutputStream bout = new ByteArrayOutputStream(256);
				ObjectOutputStream oout = new ObjectOutputStream(bout);
				oout.writeObject(dhPublicKey.getY());
				oout.writeObject(dhPublicKey.getParams().getP());
				oout.writeObject(dhPublicKey.getParams().getG());
				return bout.toByteArray();
			}
		} catch (IOException e) {
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		}
		return null;
	}

	/*
	 * public static byte[] getBytes(PublicKey publicKey) { DHPublicKey dhKey =
	 * null; RSAPublicKey rsaKey = null; if (publicKey instanceof DHPublicKey) {
	 * dhKey = (DHPublicKey) publicKey;
	 * 
	 * BigInteger publicValue = dhKey.getY(); DHParameterSpec params =
	 * dhKey.getParams(); BigInteger g = params.getG(); BigInteger p =
	 * params.getP(); byte[] pvBytes = publicValue.toByteArray(); byte[] gBytes
	 * = g.toByteArray(); byte[] pBytes = p.toByteArray(); short pvLength =
	 * (short) pvBytes.length; short gLength = (short) gBytes.length; short
	 * pLength = (short) pBytes.length;
	 * 
	 * byte[] b = new byte[6 + gLength + pLength + pvLength]; int i = 0; b[i++]
	 * = (byte) ((pvLength >>> 8) & 0xFF); b[i++] = (byte) ((pvLength >>> 0) &
	 * 0xFF);
	 * 
	 * for (int j = 0; j < pvLength; j++) { b[i++] = pvBytes[j]; }
	 * 
	 * b[i++] = (byte) ((gLength >>> 8) & 0xFF); b[i++] = (byte) ((gLength >>>
	 * 0) & 0xFF);
	 * 
	 * for (int j = 0; j < gLength; j++) { b[i++] = gBytes[j]; } b[i++] = (byte)
	 * ((pLength >>> 8) & 0xFF); b[i++] = (byte) ((pLength >>> 0) & 0xFF);
	 * 
	 * for (int j = 0; j < pLength; j++) { b[i++] = pBytes[j]; } return b; } if
	 * (publicKey instanceof RSAPublicKey) { rsaKey = (RSAPublicKey) publicKey;
	 * BigInteger expo = rsaKey.getPublicExponent(); BigInteger mod =
	 * rsaKey.getModulus(); byte[] expoBytes = expo.toByteArray(); byte[]
	 * modBytes = mod.toByteArray(); short expoLength = (short)
	 * expoBytes.length; short modLength = (short) modBytes.length; byte[] b =
	 * new byte[expoLength + modLength + 4]; int i = 0; b[i++] = (byte)
	 * ((expoLength >>> 8) & 0xFF); b[i++] = (byte) ((expoLength >>> 0) & 0xFF);
	 * 
	 * for (int j = 0; j < expoLength; j++) { b[i++] = expoBytes[j]; } b[i++] =
	 * (byte) ((modLength >>> 8) & 0xFF); b[i++] = (byte) ((modLength >>> 0) &
	 * 0xFF); for (int j = 0; j < modLength; j++) { b[i++] = modBytes[j]; }
	 * return b; } return null; }
	 * 
	 * public static byte[] getBytes(PrivateKey privateKey) { DHPrivateKey
	 * dhKey; RSAPrivateKey rsaKey; if (privateKey instanceof DHPrivateKey) {
	 * dhKey = (DHPrivateKey) privateKey;
	 * 
	 * BigInteger publicValue = dhKey.getX(); DHParameterSpec params =
	 * dhKey.getParams(); BigInteger g = params.getG(); BigInteger p =
	 * params.getP(); byte[] pvBytes = publicValue.toByteArray(); byte[] gBytes
	 * = g.toByteArray(); byte[] pBytes = p.toByteArray(); short pvLength =
	 * (short) pvBytes.length; short gLength = (short) gBytes.length; short
	 * pLength = (short) pBytes.length;
	 * 
	 * byte[] b = new byte[6 + gLength + pLength + pvLength]; int i = 0; b[i++]
	 * = (byte) ((pvLength >>> 8) & 0xFF); b[i++] = (byte) ((pvLength >>> 0) &
	 * 0xFF);
	 * 
	 * for (int j = 0; j < pvLength; j++) { b[i++] = pvBytes[j]; }
	 * 
	 * b[i++] = (byte) ((gLength >>> 8) & 0xFF); b[i++] = (byte) ((gLength >>>
	 * 0) & 0xFF);
	 * 
	 * for (int j = 0; j < gLength; j++) { b[i++] = gBytes[j]; } b[i++] = (byte)
	 * ((pLength >>> 8) & 0xFF); b[i++] = (byte) ((pLength >>> 0) & 0xFF);
	 * 
	 * for (int j = 0; j < pLength; j++) { b[i++] = pBytes[j]; } return b; } if
	 * (privateKey instanceof RSAPrivateKey) { rsaKey = (RSAPrivateKey)
	 * privateKey; BigInteger expo = rsaKey.getPrivateExponent(); BigInteger mod
	 * = rsaKey.getModulus(); byte[] expoBytes = expo.toByteArray(); byte[]
	 * modBytes = mod.toByteArray(); short expoLength = (short)
	 * expoBytes.length; short modLength = (short) modBytes.length; byte[] b =
	 * new byte[expoLength + modLength + 4]; int i = 0; b[i++] = (byte)
	 * ((expoLength >>> 8) & 0xFF); b[i++] = (byte) ((expoLength >>> 0) & 0xFF);
	 * 
	 * for (int j = 0; j < expoLength; j++) { b[i++] = expoBytes[j]; } b[i++] =
	 * (byte) ((modLength >>> 8) & 0xFF); b[i++] = (byte) ((modLength >>> 0) &
	 * 0xFF); for (int j = 0; j < modLength; j++) { b[i++] = modBytes[j]; }
	 * return b; } return null; }
	 */

	/**
	 * Should be called to return the fingerprint
	 * 
	 * @param pEncKey
	 * @param psignKey
	 * @return
	 */
	public static byte[] getFingerPrint(Key pEncKey, Key psignKey) {
		try {
			MessageDigest sha = MessageDigest.getInstance("SHA", "BC"); //$NON-NLS-1$ //$NON-NLS-2$
			sha.update(pEncKey.getEncoded());
			sha.update(psignKey.getEncoded());
			return sha.digest();
		} catch (NoSuchAlgorithmException e) {
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		} catch (NoSuchProviderException e) {
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		}

		return null;
	}

	/**
	 * Should be used as password for database Orginal password should never be
	 * used anyware
	 * 
	 * @param password
	 * @return
	 */
	public static SecretKey createPasswordKey(String password) {
		KeyGenerator key_gen = null;
		try {
			key_gen = KeyGenerator.getInstance("AES", "BC"); //$NON-NLS-1$ //$NON-NLS-2$
			key_gen.init(new SecureRandom(password.getBytes()));
		} catch (Exception e) {
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
			return null;
		}

		return key_gen.generateKey();

	}

	public static CipherOutputStream createCipherOutputStream(String password,
			OutputStream fos) {
		SecretKey key = createPasswordKey(password);
		return createCipherOutputStream(key, fos);
	}

	public static CipherInputStream createCipherInputStream(String password,
			InputStream fis) {
		SecretKey key = createPasswordKey(password);
		return createCipherInputStream(key, fis);
	}

	public static CipherOutputStream createCipherOutputStream(SecretKey key,
			OutputStream fos) {
		Cipher cip = null;
		try {
			cip = Cipher.getInstance("AES", "BC"); //$NON-NLS-1$ //$NON-NLS-2$
			SecureRandom ran = new SecureRandom();
			cip.init(Cipher.ENCRYPT_MODE, key, ran);
			return new CipherOutputStream(fos, cip);
		} catch (NoSuchAlgorithmException e) {
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		} catch (NoSuchProviderException e) {
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		} catch (NoSuchPaddingException e) {
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		} catch (InvalidKeyException e) {
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		}

		return null;
	}

	public static CipherInputStream createCipherInputStream(SecretKey key,
			InputStream fis) {
		Cipher cip = null;

		try {
			cip = Cipher.getInstance("AES", "BC"); //$NON-NLS-1$ //$NON-NLS-2$
			SecureRandom ran = new SecureRandom();
			cip.init(Cipher.DECRYPT_MODE, key, ran);
			return new CipherInputStream(fis, cip);
		} catch (NoSuchAlgorithmException e) {
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		} catch (NoSuchProviderException e) {
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		} catch (NoSuchPaddingException e) {
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		} catch (InvalidKeyException e) {
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		}

		return null;
	}

	public static Signature CreateSignature() {
		try {
			return Signature.getInstance("SHA1withRSA", "BC"); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (NoSuchAlgorithmException e) {
			//
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		} catch (NoSuchProviderException e) {
			//
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		}
		;
		return null;
	}

	public static byte[] decrypt(PrivateKey private1, byte[] encKey) {
		Cipher cip;
		try {
			cip = Cipher.getInstance("RSA/NONE/PKCS1Padding", "BC"); //$NON-NLS-1$ //$NON-NLS-2$
			SecureRandom ran = new SecureRandom();
			cip.init(Cipher.DECRYPT_MODE, private1, ran);
			return cip.doFinal(encKey);
		} catch (NoSuchAlgorithmException e) {
			//
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		} catch (NoSuchProviderException e) {
			//
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		} catch (NoSuchPaddingException e) {
			//
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		} catch (InvalidKeyException e) {
			//
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		} catch (IllegalBlockSizeException e) {
			//
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		} catch (BadPaddingException e) {
			//
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		}
		return null;

	}

	public static Mac CreateMac() {
		try {
			return Mac.getInstance("HMACSHA1", "BC"); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (NoSuchAlgorithmException e) {
			//
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		} catch (NoSuchProviderException e) {
			//
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		}
		return null;
	}

	public static SecretKey createSecretKey(byte[] symectric) {
		return new SecretKeySpec(symectric, "AES"); //$NON-NLS-1$
	}

	public static byte[] encode(byte[] encoded, PublicKey encKey) {
		Cipher cip;
		try {
			cip = Cipher.getInstance("RSA/NONE/PKCS1Padding", "BC"); //$NON-NLS-1$ //$NON-NLS-2$
			SecureRandom ran = new SecureRandom();
			cip.init(Cipher.ENCRYPT_MODE, encKey, ran);
			return cip.doFinal(encoded);
		} catch (NoSuchAlgorithmException e) {
			//
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		} catch (NoSuchProviderException e) {
			//
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		} catch (NoSuchPaddingException e) {
			//
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		} catch (InvalidKeyException e) {
			//
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		} catch (IllegalBlockSizeException e) {
			//
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		} catch (BadPaddingException e) {
			//
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		}

		return null;
	}

	public static DHParameterSpec getDHParamSpec(byte[] bs) {

		try {
			ObjectInputStream oin = new ObjectInputStream(
					new ByteArrayInputStream(bs));
			return new DHParameterSpec((BigInteger) oin.readObject(),
					(BigInteger) oin.readObject());
		} catch (IOException e) {
			//
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		} catch (ClassNotFoundException e) {
			//
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		}
		return null;
	}

	public static byte[] getBytes(DHParameterSpec dhParamSpec) {
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream(256);
			ObjectOutputStream oout = new ObjectOutputStream(bout);
			oout.writeObject(dhParamSpec.getP());
			oout.writeObject(dhParamSpec.getG());
			return bout.toByteArray();
		} catch (IOException e) {
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
			return null;
		}
	}

	public static String decryptPassword(String password) {
		return password;
	}

	public static String encryptPassword(String password) {
		return password;
	}

	public static byte[] makeHash(String data) {
		try {
			MessageDigest sha = MessageDigest.getInstance("SHA"); //$NON-NLS-1$ //$NON-NLS-2$
			sha.update(data.getBytes());
			return sha.digest();
		} catch (NoSuchAlgorithmException e) {
			LOG.error("Exception: ", e);
			LOG.error("Exception", e);
		}

		return null;
	}
}
