/**
 * 
 */
package com.nitya.accounter.mobile.utils;

import java.text.ParseException;

import com.ibm.icu.text.SimpleDateFormat;

public class StringUtils {

	public static boolean isInteger(String string) {
		boolean matches = string.matches("[-+]?\\d+(\\d+)?");
		if (matches) {
			try {
				Integer.parseInt(string);
			} catch (NumberFormatException e) {
				return false;
			}
		}
		return matches;
	}

	public static boolean isDouble(String string) {
		boolean matches = string.matches("[-+]?\\d+(\\d+)?")
				|| string.matches("[-+]?\\d+(\\.\\d+)?");
		if (matches) {
			try {
				Double.parseDouble(string);
			} catch (NumberFormatException e) {
				return false;
			}
		}
		return matches;
	}

	public static boolean isDate(String string) {
		SimpleDateFormat d = new SimpleDateFormat("dd/MM/yyyy");
		try {
			d.parse(string);
			return true;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return false;
		}

	}

	public static String[] getSubStrings(String input) {
		// TODO
		return new String[0];
	}

}
