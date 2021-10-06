package com.vimukti.accounter.text.commands.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.vimukti.accounter.core.FinanceDate;

public class CommandUtils {
	public final static String[] MONTHS = new String[] { "january", "february",
			"march", "april", "may", "june", "july", "august", "september",
			"october", "november", "december" };

	/**
	 * Get the Month by Name
	 * 
	 * @param monthName
	 * @return
	 */
	public static int getMonthByName(String monthName) {
		// fiscalStartsList = null;
		for (int i = 0; i < MONTHS.length; i++) {
			if (monthName.toLowerCase().equals(MONTHS[i])) {
				return i;
			}
		}
		return 0;
	}

	public static FinanceDate getFinaceDate(String dateVal, FinanceDate defVal) {

		Date date = null;
		// MMddyyyy
		try {
			SimpleDateFormat format = new SimpleDateFormat("MMddyyyy");
			date = format.parse(dateVal);
		} catch (ParseException e) {
			// FAILED
		}
		// ddMMyyyy
		try {
			SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");
			date = format.parse(dateVal);
		} catch (ParseException e) {
			// THIS ALSO FAILED
		}
		// dd/MM/yyyy
		try {
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			date = format.parse(dateVal);
		} catch (ParseException e) {
			// THIS ALSO FAILED
		}
		// dd/MM/yy
		try {
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
			date = format.parse(dateVal);
		} catch (ParseException e) {
			// THIS ALSO FAILED
		}
		if (date == null) {
			return defVal;
		}
		return new FinanceDate(date);

	}
}
