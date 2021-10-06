package com.vimukti.accounter.license;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.vimukti.accounter.core.License;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.StringUtils;
import com.vimukti.accounter.utils.Zip;

public class LicenseManager {

	private static Logger log = Logger.getLogger(LicenseManager.class);

	public static final int ENCODED_LICENSE_LENGTH_BASE = 31;

	private static final int ENCODED_LICENSE_LINE_LENGTH = 76;

	private static final char SEPARATOR = 'X';

	private static final int VERSION_LENGTH = 3;

	private static final PublicKey PUBLIC_KEY;

	public boolean canDecode(String licenseString) {
		licenseString = StringUtils.removeWhiteSpaces(licenseString);

		int pos = licenseString.lastIndexOf(SEPARATOR);
		if ((pos == -1) || (pos + VERSION_LENGTH >= licenseString.length())) {
			return false;
		}

		try {
			int version = Integer.parseInt(licenseString.substring(pos + 1, pos
					+ VERSION_LENGTH));
			if ((version != 1) && (version != 2)) {
				return false;
			}

			String lengthStr = licenseString.substring(pos + VERSION_LENGTH);
			int encodedLicenseLength = Integer.valueOf(lengthStr,
					ENCODED_LICENSE_LENGTH_BASE).intValue();

			return pos == encodedLicenseLength;
		} catch (NumberFormatException e) {
		}

		return false;
	}

	public License doDecode(String licenseString) {
		if (!canDecode(licenseString)) {
			throw new LicenseException("Invalid License");
		}
		String encodedLicenseTextAndHash = getLicenseContent(StringUtils
				.removeWhiteSpaces(licenseString));
		byte[] zippedLicenseBytes = checkAndGetLicenseText(encodedLicenseTextAndHash);
		Reader licenseText = unzipText(zippedLicenseBytes);

		License license = loadLicenseConfiguration(licenseText);
		license.setLicenseText(licenseString);
		return license;
	}

	private Reader unzipText(byte[] licenseText) {
		ByteArrayInputStream in = new ByteArrayInputStream(licenseText);
		InflaterInputStream zipIn = new InflaterInputStream(in, new Inflater());
		try {
			return new InputStreamReader(zipIn, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new LicenseException(e);
		}

	}

	private String getLicenseContent(String licenseString) {
		String lengthStr = licenseString.substring(licenseString
				.lastIndexOf(SEPARATOR) + VERSION_LENGTH);
		try {
			int encodedLicenseLength = Integer.valueOf(lengthStr,
					ENCODED_LICENSE_LENGTH_BASE).intValue();
			return licenseString.substring(0, encodedLicenseLength);
		} catch (NumberFormatException e) {
			throw new LicenseException("Could NOT decode license length <"
					+ lengthStr + ">", e);
		}

	}

	private byte[] checkAndGetLicenseText(String licenseContent) {
		byte[] licenseText;
		try {
			byte[] decodedBytes = Base64
					.decodeBase64(licenseContent.getBytes());
			ByteArrayInputStream in = new ByteArrayInputStream(decodedBytes);
			DataInputStream dIn = new DataInputStream(in);
			int textLength = dIn.readInt();
			licenseText = new byte[textLength];
			dIn.read(licenseText);
			byte[] hash = new byte[dIn.available()];
			dIn.read(hash);
			try {
				Signature signature = Signature.getInstance("SHA1withDSA");
				signature.initVerify(PUBLIC_KEY);
				signature.update(licenseText);
				if (!signature.verify(hash)) {
					throw new LicenseException("Failed to verify the license.");
				}

			} catch (InvalidKeyException e) {
				throw new LicenseException(e);
			} catch (SignatureException e) {
				throw new LicenseException(e);
			} catch (NoSuchAlgorithmException e) {
				throw new LicenseException(e);
			}

		} catch (IOException e) {
			throw new LicenseException(e);
		}

		return licenseText;
	}

	private License loadLicenseConfiguration(Reader text) {
		try {
			return new PropertiesPersister().load(text);
		} catch (IOException e) {
			throw new LicenseException("Could NOT load properties from reader",
					e);
		}

	}

	public LicensePair doEncode(License license) {

		byte[] licenseText = null;
		byte[] hash;
		try {
			licenseText = Zip.compressBytes(new PropertiesPersister()
					.getLicenseAsString(license));
		} catch (UnsupportedEncodingException e) {
			throw new LicenseException(e);
		} catch (IOException e) {
			throw new LicenseException(e);
		}

		try {
			Signature signature = Signature.getInstance("SHA1withDSA");
			signature.initSign(getPrivateKey());
			signature.update(licenseText);
			hash = signature.sign();
		} catch (InvalidKeyException e) {
			throw new LicenseException(e);
		} catch (SignatureException e) {
			throw new LicenseException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new LicenseException(e);
		}

		String packLicense = packLicense(licenseText, hash);

		return new LicensePair(licenseText, hash, packLicense);
	}

	public static String packLicense(byte[] text, byte[] hash)
			throws LicenseException {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			DataOutputStream dOut = new DataOutputStream(out);
			dOut.writeInt(text.length);
			dOut.write(text);
			dOut.write(hash);

			byte[] allData = out.toByteArray();
			String result = new String(Base64.encodeBase64(allData)).trim();

			result = result
					+ SEPARATOR
					+ "0"
					+ 1
					+ Integer.toString(result.length(),
							ENCODED_LICENSE_LENGTH_BASE);
			result = split(result);
			return result;
		} catch (IOException e) {
			throw new LicenseException(e);
		}

	}

	private static String split(String licenseData) {
		if ((licenseData == null) || (licenseData.length() == 0)) {
			return licenseData;
		}

		char[] chars = licenseData.toCharArray();
		StringBuffer buf = new StringBuffer(chars.length + chars.length
				/ ENCODED_LICENSE_LINE_LENGTH);
		for (int i = 0; i < chars.length; i++) {
			buf.append(chars[i]);
			if ((i <= 0) || (i % ENCODED_LICENSE_LINE_LENGTH != 0))
				continue;
			buf.append('\n');
		}

		return buf.toString();
	}

	public String generateLicense(License license) {
		LicensePair licensePair = doEncode(license);
		return licensePair.originalLicenseString;
	}

	static {
		try {
			String pubKeyEncoded = "MIIBtzCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoDgYQAAoGAFtEZn3RJcxouqNq+dvDL8wyGC3AozpTDyZTgZcAb5QahAjXnMIgtrn0N6tt054R+EDOAj7UfkqsoDLhCzclLJtBNECMKq7OoYF6AfkwLUMY9F4eoLjJ6b+WmzYU8YTPl3q9hlfk17KkfHRR1hfezffll4aZQiCSiL/pJSESc3kM=";

			KeyFactory keyFactory = KeyFactory.getInstance("DSA");
			PUBLIC_KEY = keyFactory.generatePublic(new X509EncodedKeySpec(
					Base64.decodeBase64(pubKeyEncoded.getBytes())));
		} catch (NoSuchAlgorithmException e) {
			throw new Error(e);
		} catch (InvalidKeySpecException e) {
			throw new Error(e);
		}
	}

	private PrivateKey getPrivateKey() {
		Key key = null;
		try {
			FileInputStream is = new FileInputStream(
					ServerConfiguration.getConfig() + File.separator
							+ "license.keystore");
			KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
			String password = ServerConfiguration.getLicenseKeystorePWD();
			keystore.load(is, password.toCharArray());

			key = keystore.getKey(ServerConfiguration.getLicenseAlias(),
					password.toCharArray());

		} catch (NoSuchAlgorithmException e) {
			throw new LicenseException(e);
		} catch (CertificateException e) {
			throw new LicenseException(e);
		} catch (IOException e) {
			throw new LicenseException(e);
		} catch (UnrecoverableKeyException e) {
			throw new LicenseException(e);
		} catch (KeyStoreException e) {
			throw new LicenseException(e);
		}
		return (PrivateKey) key;
	}
}
