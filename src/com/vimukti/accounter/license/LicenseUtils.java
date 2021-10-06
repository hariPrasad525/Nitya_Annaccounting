package com.vimukti.accounter.license;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.log4j.Logger;

public class LicenseUtils {
	private static Logger log = Logger.getLogger(LicenseUtils.class);
	public static final long POST_LICENSE_EVAL_PERIOD = 2592000000L;
	public static final long UPDATE_ALLOWED_PERIOD = 31622400000L;
	public static final long ALMOST_EXPIRED_PERIOD = 3628800000L;
	public static final String PARTNER_NOT_MATCHING_BUILD = "partner not matching build partner name";
	public static final String LICENSE_NO_PARTNER = "License does not contain a partner name";

	public static String getString(byte[] byteArray) {
		char[] charByte = new char[byteArray.length * 2];
		for (int i = 0; i < byteArray.length; i++) {
			byte aByte = byteArray[i];
			if ((-128 <= aByte) && (aByte < -64)) {
				charByte[i] = rndChar(0);
				charByte[(byteArray.length + i)] = getCharInRange(aByte + 128);
			} else if ((-64 <= aByte) && (aByte < 0)) {
				charByte[i] = rndChar(1);
				charByte[(byteArray.length + i)] = getCharInRange(aByte + 64);
			} else if ((0 <= aByte) && (aByte < 64)) {
				charByte[i] = rndChar(2);
				charByte[(byteArray.length + i)] = getCharInRange(aByte);
			} else if ((64 <= aByte) && (aByte < 128)) {
				charByte[i] = rndChar(3);
				charByte[(byteArray.length + i)] = getCharInRange(aByte - 64);
			} else {
				log.debug("Invalid Char in stream " + aByte);
			}
		}
		String str = new String(charByte);
		return str;
	}

	private static char rndChar(int i) {
		int c = i * 6 + (int) (Math.random() * 6.0D);
		boolean u = (int) (Math.random() * 2.0D) < 1;
		return (char) (c + (u ? 97 : 65));
	}

	private static char getCharInRange(int c1) {
		if ((0 <= c1) && (c1 <= 9)) {
			return (char) (c1 + 48);
		}
		if ((10 <= c1) && (c1 <= 35)) {
			return (char) (c1 - 10 + 65);
		}
		if ((36 <= c1) && (c1 <= 61)) {
			return (char) (c1 - 36 + 97);
		}
		if (c1 == 62) {
			return '<';
		}
		if (c1 == 63) {
			return '>';
		}

		log.debug("Invalid int in stream " + c1);
		return '\000';
	}

	private static byte getByteInRange(char c1) {
		if (('0' <= c1) && (c1 <= '9')) {
			return (byte) (c1 - '0');
		}
		if (('A' <= c1) && (c1 <= 'Z')) {
			return (byte) (c1 - 'A' + 10);
		}
		if (('a' <= c1) && (c1 <= 'z')) {
			return (byte) (c1 - 'a' + 36);
		}
		if (c1 == '<') {
			return 62;
		}
		if (c1 == '>') {
			return 63;
		}

		log.debug("Incorrect character in stream " + c1);
		return 2;
	}

	public static byte[] getBytes(String string) {
		char[] charArray = string.toCharArray();

		byte[] bytes = new byte[charArray.length / 2];
		for (int i = 0; i < bytes.length; i++) {
			if (Character.toLowerCase(charArray[i]) < 'g') {
				bytes[i] = (byte) (getByteInRange(charArray[(bytes.length + i)]) - 128);
			} else if (Character.toLowerCase(charArray[i]) < 'm') {
				bytes[i] = (byte) (getByteInRange(charArray[(bytes.length + i)]) - 64);
			} else if (Character.toLowerCase(charArray[i]) < 's') {
				bytes[i] = getByteInRange(charArray[(bytes.length + i)]);
			} else if (Character.toLowerCase(charArray[i]) < 'y') {
				bytes[i] = (byte) (getByteInRange(charArray[(bytes.length + i)]) + 64);
			} else {
				log.debug("Invalid character in byte stream " + charArray[i]);
			}
		}
		return bytes;
	}

	// public static long getSupportPeriodEnd(License license) {
	// return license.getDateCreated().getTime() + 31622400000L;
	// }
	//
	// public static boolean isLicenseTooOldForBuild(License license,
	// Date buildDate) {
	// return getSupportPeriodEnd(license) < buildDate.getTime();
	// }

	public static boolean confirmExtendLicenseExpired(Date dateConfirmed) {
		return new Date().getTime() > getNewBuildWithOldLicenseExpiryDate(dateConfirmed);
	}

	public static boolean confirmExtendLicenseExpired(String dateConfirmed)
			throws NumberFormatException {
		return confirmExtendLicenseExpired(new Date(
				Long.parseLong(dateConfirmed)));
	}

	private static long getNewBuildWithOldLicenseExpiryDate(Date dateConfirmed) {
		return dateConfirmed.getTime() + 2592000000L;
	}

	public static long getNewBuildWithOldLicenseExpiryDate(String dateConfirmed) {
		return getNewBuildWithOldLicenseExpiryDate(new Date(
				Long.parseLong(dateConfirmed)));
	}

	// public static long getSupportPeriodAlmostExpiredDate(License license) {
	// return getSupportPeriodEnd(license) - 3628800000L;
	// }

	// public static String isPartnerDetailsValid(License license,
	// String buildPartnerName) {
	// String licensePartnerName = license.getPartnerName();
	//
	// if ((licensePartnerName != null) && (!licensePartnerName.equals(""))
	// && (!licensePartnerName.equals(buildPartnerName))) {
	// return "partner not matching build partner name";
	// }
	// if (((licensePartnerName == null) || (licensePartnerName.equals("")))
	// && (buildPartnerName != null) && (!buildPartnerName.equals(""))) {
	// return "License does not contain a partner name";
	// }
	// return "";
	// }

	public static byte[] readKey(InputStream is) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		int len = 0;
		byte[] bytes = new byte[512];
		while ((len = is.read(bytes)) > -1) {
			bout.write(bytes, 0, len);
		}

		return bout.toByteArray();
	}
}
