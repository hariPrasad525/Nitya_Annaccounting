package com.vimukti.accounter.web.client.ui.core;

public class DecimalUtil {

	public static boolean isEquals(double val1, double val2) {
		val1 = round(val1);
		val2 = round(val2);

		long thisBits = doubleToLongBits(val1);
		long anotherBits = doubleToLongBits(val2);

		if (thisBits != anotherBits) {
			return false;
		}

		int val1Index = String.valueOf(val1).indexOf('.');
		int val2Index = String.valueOf(val2).indexOf('.');

		return val1Index == val2Index;

	}

	private static long doubleToLongBits(double val) {

		String dbl = String.valueOf(val);
		int decimpoints = dbl.indexOf(".") > -1 ? dbl.split("\\.")[1].length()
				: 1;
		long factor = (long) Math.pow(10, decimpoints);
		val = val * factor;

		return Math.round(val);
	}

	public static boolean isGreaterThan(double val1, double val2) {
		val1 = round(val1);
		val2 = round(val2);
		return val1 > val2;
	}

	public static boolean isLessThan(double val1, double val2) {
		val1 = round(val1);
		val2 = round(val2);
		return val1 < val2;
	}

	public static int compare(double val1, double val2) {
		if (isLessThan(val1, val2))
			return -1;
		if (isGreaterThan(val1, val2))
			return 1;

		val1 = round(val1);
		val2 = round(val2);

		long thisBits = doubleToLongBits(val1);
		long anotherBits = doubleToLongBits(val2);

		return (thisBits == anotherBits ? 0 : // Values are equal
				(thisBits < anotherBits ? -1 : // (-0.0, 0.0) or (!NaN, NaN)
						1));
	}

	public static double round(double amount) {
		double tmp1 = amount;
		long factor = (long) Math.pow(10, 2);
		tmp1 = tmp1 * factor;
		long tmp = Math.round(Math.abs(tmp1));
		amount = ((amount < 0 && tmp != 0) ? -1 : 1) * ((double) tmp / factor);
		String[] strArr = String.valueOf(amount).split("\\.");
		//
		// amount = Double.parseDouble(strArr[0] + "."
		// + (strArr.length > 1 ? strArr[1].substring(0, 2) : "00"));
		return amount;
	}

}
