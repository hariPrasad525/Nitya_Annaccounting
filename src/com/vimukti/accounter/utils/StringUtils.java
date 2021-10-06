package com.vimukti.accounter.utils;

import java.util.Collection;

import com.vimukti.accounter.servlets.Facebook.StringConverter;

/**
 * This class is a util class with different String operations.
 * 
 * @author Administrator
 */
public class StringUtils {

	// private static Logger LOG = Logger.getLogger(StringUtils.class);

	/**
	 * Method is useful for checking the given string is empty or not if it is
	 * empty then return true otherwise return false.
	 * 
	 * @param tmp
	 * @return boolean
	 */

	public static boolean isEmpty(String str) {
		if (str == null)
			return true;
		if (str.trim().length() == 0) {
			return true;
		}
		return false;
	}

	public static String sizeToString(long val, boolean isAbbreviated) {
		String unity = ""; //$NON-NLS-1$
		double size = val;

		if (isAbbreviated)
			unity = "b";
		else
			unity = "bytes";

		if (size > 1024.) {
			size /= 1024.;
			if (isAbbreviated)
				unity = "Kb";
			else
				unity = "Kilo-Bytes";
		}
		if (size > 1024.) {
			size /= 1024.;
			if (isAbbreviated)
				unity = "Mb";
			else
				unity = "Mega-Bytes";
		}
		if (size > 1024.) {
			size /= 1024.;
			if (isAbbreviated)
				unity = "Gb";
			else
				unity = "Giga-Bytes";

		}
		size = ((double) (long) (size * 10)) / 10d;
		return size + unity;
	}

	/****
	 * Returns the comma seprated string example:-a@b.com,c@d.com,d@e.com
	 * 
	 * @param coll
	 * @param delimiter
	 * @return String
	 */
	public static String join(Collection<String> coll, String delimiter) {
		if (coll.isEmpty())
			return "";

		StringBuilder sb = new StringBuilder();

		for (String x : coll)
			sb.append(x + delimiter);

		sb.delete(sb.length() - delimiter.length(), sb.length());

		return sb.toString();
	}

	public static boolean hasLength(String username) {
		if (username.length() != 0) {
			return true;
		}
		return false;
	}

	public static String stringAppenderForIndex(String... strings) {

		if (strings == null)
			return "";
		StringBuffer buffer = new StringBuffer();
		for (String string : strings) {
			if (!(string == null))
				buffer.append(" " + string + " ");
		}
		return buffer.toString();

	}

	public static <T> String delimitObjectsToString(String delim, T... objects) {
		return delimitObjectsToString(delim, delim, objects);
	}

	public static <T> String delimitObjectsToString(String delim,
			String lastDelim, T... objects) {
		return delimitObjectsToString(delim, lastDelim, false, objects);
	}

	public static <T> String delimitObjectsToString(String delim,
			String lastDelim, boolean skipNulls, T... objects) {
		return delimitObjectsToString(delim, lastDelim, skipNulls,
				new StringConverter<T>() {
					public String toString(T object) {
						return (object == null ? "null" : object.toString());
					}
				}, objects);
	}

	public static <T> String delimitObjectsToString(String delim,
			String lastDelim, boolean skipNulls, StringConverter<T> conv,
			T... objects) {
		if (objects == null || objects.length == 0) {
			return null;
		}
		if (objects.length == 1
				&& Collection.class.isAssignableFrom(objects[0].getClass())) {
			objects = (T[]) ((Collection) objects[0]).toArray();
		}
		StringBuffer sb = new StringBuffer();
		boolean firstDone = false;
		for (int n = 0; n < objects.length; n++) {
			if (firstDone) {
				if (n == objects.length - 1) {
					sb.append(nvl(lastDelim, delim));
				} else {
					sb.append(delim);
				}
			}
			if (objects[n] != null || !skipNulls) {
				sb.append(conv.toString(objects[n]));
				firstDone = true;
			}
		}
		return sb.toString();
	}

	/**
	 * Allows a list of values to be provided, the first non-null value in the
	 * list is returned as the result.
	 * 
	 * @param <T>
	 * @param mainValue
	 * @param fallbackValues
	 * @return
	 */
	public static <T> T nvl(T mainValue, T... fallbackValues) {
		T result = mainValue;
		int idx = 0;
		while (result == null && idx < fallbackValues.length) {
			result = fallbackValues[idx++];
		}
		return result;
	}

	/**
	 * @param str
	 *            string to inspect
	 * @return true if string is not null or empty
	 */
	public static boolean isNotEmptyStr(String str) {
		return !(str == null || str.isEmpty());
	}

	public static boolean isNotBlankStr(String str) {
		return !(str == null || str.trim().isEmpty());
	}

	public static String replaceAll(String str, String oldPattern,
			String newPattern) {
		if (str == null)
			return null;
		if ((oldPattern == null) || (oldPattern.equals("")))
			return str;
		String remainder = str;
		StringBuffer buf = new StringBuffer(str.length() * 2);
		while (true) {
			int i = remainder.indexOf(oldPattern);
			if (i != -1) {
				buf.append(remainder.substring(0, i));
				buf.append(newPattern);
				remainder = remainder.substring(i + oldPattern.length());
			} else {
				buf.append(remainder);
				break;
			}
		}
		return buf.toString();
	}

	public static String trimLeadingWhitespace(String str) {
		if (str.length() == 0) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		while ((buf.length() > 0) && (Character.isWhitespace(buf.charAt(0)))) {
			buf.deleteCharAt(0);
		}
		return buf.toString();
	}

	public static String trimTrailingWhitespace(String str) {
		if (str.length() == 0) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		while ((buf.length() > 0)
				&& (Character.isWhitespace(buf.charAt(buf.length() - 1)))) {
			buf.deleteCharAt(buf.length() - 1);
		}
		return buf.toString();
	}

	public static String removeWhiteSpaces(String licenseData) {
		if ((licenseData == null) || (licenseData.length() == 0)) {
			return licenseData;
		}

		char[] chars = licenseData.toCharArray();
		StringBuffer buf = new StringBuffer(chars.length);
		for (int i = 0; i < chars.length; i++) {
			if (Character.isWhitespace(chars[i]))
				continue;
			buf.append(chars[i]);
		}

		return buf.toString();
	}
}
