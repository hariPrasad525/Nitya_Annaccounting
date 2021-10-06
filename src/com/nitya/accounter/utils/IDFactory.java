package com.nitya.accounter.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Type IDFactory
 * 
 * @author Rajesh Akkineni
 * @version 1.0
 */
public class IDFactory {

	private static SecureRandom prng;

	/**
	 * Method createID
	 * 
	 * @return String
	 */
	public synchronized static String createID() {
		return createID(40);
	}

	public synchronized static String createID(int numOfChars) {
		if (prng == null) {
			try {
				// Initialize SecureRandom
				// This is a lengthy operation, to be done only upon
				// initialization of the application
				prng = SecureRandom.getInstance("SHA1PRNG"); //$NON-NLS-1$				
			} catch (NoSuchAlgorithmException ex) {
				System.err.println(ex);
			}
		}
		String code = "abcdefghijklmnopqrstuvwxyz0123456789";
		StringBuffer sb = new StringBuffer(40);
		for (int x = 0; x < numOfChars; x++) {
			sb.append(code.charAt(prng.nextInt(36)));
		}

		return sb.toString();
	}

	public synchronized static String createNumberID(int numOfChars) {
		if (prng == null) {
			try {
				// Initialize SecureRandom
				// This is a lengthy operation, to be done only upon
				// initialization of the application
				prng = SecureRandom.getInstance("SHA1PRNG"); //$NON-NLS-1$				
			} catch (NoSuchAlgorithmException ex) {
				System.err.println(ex);
			}
		}
		String code = "0123456789";
		StringBuffer sb = new StringBuffer(40);
		for (int x = 0; x < numOfChars; x++) {
			sb.append(code.charAt(prng.nextInt(9)));
		}

		return sb.toString();
	}

	/**
	 * The byte[] returned by MessageDigest does not have a nice textual
	 * representation, so some form of encoding is usually performed.
	 * 
	 * This implementation follows the example of David Flanagan's book "Java In
	 * A Nutshell", and converts a byte array into a String of hex characters.
	 * 
	 * Another popular alternative is to use a "Base64" encoding.
	 * 
	 * @param aInput
	 * @return String
	 */
	// private static String hexEncode(byte[] aInput) {
	// StringBuffer result = new StringBuffer();
	// char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
	// '1', '2', '3', '4', '5', '6' };
	// for (int idx = 0; idx < aInput.length; ++idx) {
	// byte b = aInput[idx];
	// result.append(digits[(b & 0xf0) >> 4]);
	// result.append(digits[b & 0x0f]);
	// }
	// return result.toString();
	// }

	public static String createID(byte[] bytes) {
		SecureRandom rand = null;

		try {
			// Initialize SecureRandom
			// This is a lengthy operation, to be done only upon
			// initialization of the application
			rand = SecureRandom.getInstance("SHA1PRNG"); //$NON-NLS-1$				
		} catch (NoSuchAlgorithmException ex) {
			System.err.println(ex);
		}

		rand.setSeed(bytes);
		String code = "abcdefghijklmnopqrstuvwxyz0123456789";
		StringBuffer sb = new StringBuffer(40);
		for (int x = 0; x < 40; x++) {
			sb.append(code.charAt(rand.nextInt(36)));
		}

		return sb.toString();
	}
}