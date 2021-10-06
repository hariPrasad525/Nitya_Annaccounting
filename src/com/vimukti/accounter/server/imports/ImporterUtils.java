package com.vimukti.accounter.server.imports;

import java.util.Set;

import com.vimukti.accounter.core.Measurement;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

/**
 * 
 * @author Devaraju.k
 * 
 */
public class ImporterUtils {

	public static Measurement getMeasurementByID(Set<Measurement> list,
			long measurmentId) {
		for (Measurement measurement : list) {
			if (measurement.getID() == measurmentId) {
				return measurement;
			}
		}
		return null;
	}

	public static boolean isValidEmail(String email) {
		email = email.toLowerCase();
		return (email
				.matches("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$"));
	}

	public static boolean isNegativeAmount(Double amt) {
		if (DecimalUtil.isLessThan(amt, 0.00)) {
			return true;
		}
		return false;

	}

	public static boolean isLessThan(double val1, double val2) {
		val1 = round(val1);
		val2 = round(val2);
		return val1 < val2;
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