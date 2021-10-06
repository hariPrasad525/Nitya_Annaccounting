/**
 * 
 */
package com.nitya.accounter.utils;

import java.io.File;

import org.apache.log4j.Logger;

/**
 * @author Administrator
 * 
 * @version 1.0
 */

public class SecureUtils {
	private Logger log = Logger.getLogger(SecureUtils.class);

	/**
	 * Create a 32 character length random filename with no extention using
	 * alpha numeric chars
	 * 
	 * @return
	 */
	public static String createRandomFileName() {
		return IDFactory.createID();

	}

	/**
	 * Create a random 32 character length id
	 * 
	 * @return
	 */
	public static String createID() {
		return IDFactory.createID();

	}

	public static String createID(int numberOfChars) {
		return IDFactory.createID(numberOfChars);

	}

	public static String createNumberID(int numberOfChars) {
		return IDFactory.createNumberID(numberOfChars);

	}

	public static String createID(String str1, String str2) {
		return IDFactory.createID((str1 + str2).getBytes());
	}

	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		// The directory is now empty so now it can be smoked
		return dir.delete();
	}
}
