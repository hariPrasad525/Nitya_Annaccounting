package com.nitya.accounter.web.client.ui.widgets;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.util.DayAndMonthUtil;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class DateUtills {

	public boolean isMonth = false;

	public static String getCurrentDateAsString(String dateFormat) {

		DateTimeFormat dateFormatter = DateTimeFormat.getFormat(dateFormat);
		return dateFormatter.format(new Date(System.currentTimeMillis()));

	}

	public static String getDateAsString(Date date) {
		DateTimeFormat dateFormatter = null;
		dateFormatter = DateTimeFormat.getFormat(Global.get().preferences()
				.getDateFormat());
		return dateFormatter.format(date);

	}

	public static String getDateAsString(ClientFinanceDate date) {
		return getDateAsString(date.getDateAsObject());
	}

	public static String getDateAsString(long date) {
		return getDateAsString(new ClientFinanceDate(date).getDateAsObject());
	}

	public int evaluateDate(String dateFormat) {
		try {
			return Integer.parseInt(dateFormat);
		} catch (Exception e) {
			isMonth = true;
			if (dateFormat.equalsIgnoreCase(DayAndMonthUtil.jan())
					|| dateFormat.equalsIgnoreCase(DayAndMonthUtil.january())) {
				return 1;
			} else if (dateFormat.equalsIgnoreCase(DayAndMonthUtil.feb())
					|| dateFormat.equalsIgnoreCase(DayAndMonthUtil.february())) {
				return 2;
			} else if (dateFormat.equalsIgnoreCase(DayAndMonthUtil.mar())
					|| dateFormat.equalsIgnoreCase(DayAndMonthUtil.march())) {
				return 3;
			} else if (dateFormat.equalsIgnoreCase(DayAndMonthUtil.apr())
					|| dateFormat.equalsIgnoreCase(DayAndMonthUtil.april())) {
				return 4;
			} else if (dateFormat.equalsIgnoreCase(DayAndMonthUtil.may_full())
					|| dateFormat.equalsIgnoreCase(DayAndMonthUtil.mayS())) {
				return 5;
			} else if (dateFormat.equalsIgnoreCase(DayAndMonthUtil.jun())
					|| dateFormat.equalsIgnoreCase(DayAndMonthUtil.june())) {
				return 6;
			} else if (dateFormat.equalsIgnoreCase(DayAndMonthUtil.jul())
					|| dateFormat.equalsIgnoreCase(DayAndMonthUtil.july())) {
				return 7;
			} else if (dateFormat.equalsIgnoreCase(DayAndMonthUtil.aug())
					|| dateFormat.equalsIgnoreCase(DayAndMonthUtil.august())) {
				return 8;
			} else if (dateFormat.equalsIgnoreCase(DayAndMonthUtil.sep())
					|| dateFormat.equalsIgnoreCase(DayAndMonthUtil.september())) {
				return 9;
			} else if (dateFormat.equalsIgnoreCase(DayAndMonthUtil.oct())
					|| dateFormat.equalsIgnoreCase(DayAndMonthUtil.october())) {
				return 10;
			} else if (dateFormat.equalsIgnoreCase(DayAndMonthUtil.nov())
					|| dateFormat.equalsIgnoreCase(DayAndMonthUtil.november())) {
				return 11;
			} else if (dateFormat.equalsIgnoreCase(DayAndMonthUtil.dec())
					|| dateFormat.equalsIgnoreCase(DayAndMonthUtil.december())) {
				return 12;
			} else {
				System.out.println("Please enter Valid Month Format DDMMYYYY");
			}
		}
		return 0;
	}

	public int evaluateMonthFormat(String monthFormat) {
		try {
			return Integer.parseInt(monthFormat);
		} catch (Exception e) {
			if (monthFormat.equalsIgnoreCase(DayAndMonthUtil.jan())
					|| monthFormat.equalsIgnoreCase(DayAndMonthUtil.january())) {
				return 1;
			} else if (monthFormat.equalsIgnoreCase(DayAndMonthUtil.feb())
					|| monthFormat.equalsIgnoreCase(DayAndMonthUtil.february())) {
				return 2;
			} else if (monthFormat.equalsIgnoreCase(DayAndMonthUtil.mar())
					|| monthFormat.equalsIgnoreCase(DayAndMonthUtil.march())) {
				return 3;
			} else if (monthFormat.equalsIgnoreCase(DayAndMonthUtil.apr())
					|| monthFormat.equalsIgnoreCase(DayAndMonthUtil.april())) {
				return 4;
			} else if (monthFormat.equalsIgnoreCase(DayAndMonthUtil.may_full())
					|| monthFormat.equalsIgnoreCase(DayAndMonthUtil.mayS())) {
				return 5;
			} else if (monthFormat.equalsIgnoreCase(DayAndMonthUtil.jun())
					|| monthFormat.equalsIgnoreCase(DayAndMonthUtil.june())) {
				return 6;
			} else if (monthFormat.equalsIgnoreCase(DayAndMonthUtil.jul())
					|| monthFormat.equalsIgnoreCase(DayAndMonthUtil.july())) {
				return 7;
			} else if (monthFormat.equalsIgnoreCase(DayAndMonthUtil.aug())
					|| monthFormat.equalsIgnoreCase(DayAndMonthUtil.august())) {
				return 8;
			} else if (monthFormat.equalsIgnoreCase(DayAndMonthUtil.sep())
					|| monthFormat
							.equalsIgnoreCase(DayAndMonthUtil.september())) {
				return 9;
			} else if (monthFormat.equalsIgnoreCase(DayAndMonthUtil.oct())
					|| monthFormat.equalsIgnoreCase(DayAndMonthUtil.october())) {
				return 10;
			} else if (monthFormat.equalsIgnoreCase(DayAndMonthUtil.nov())
					|| monthFormat.equalsIgnoreCase(DayAndMonthUtil.november())) {
				return 11;
			} else if (monthFormat.equalsIgnoreCase(DayAndMonthUtil.dec())
					|| monthFormat.equalsIgnoreCase(DayAndMonthUtil.december())) {
				return 12;
			} else {
				System.out.println("Please enter Valid Month Format DDMMYYYY");
			}
		}
		return 0;
	}

	public static String getMonthNameByNumber(int month) {

		switch (month) {
		case 1:
			return (DayAndMonthUtil.jan());
		case 2:
			return (DayAndMonthUtil.feb());
		case 3:
			return (DayAndMonthUtil.mar());
		case 4:
			return (DayAndMonthUtil.apr());
		case 5:
			return (DayAndMonthUtil.mayS());
		case 6:
			return (DayAndMonthUtil.jun());
		case 7:
			return (DayAndMonthUtil.july());
		case 8:
			return (DayAndMonthUtil.aug());
		case 9:
			return (DayAndMonthUtil.sep());
		case 10:
			return (DayAndMonthUtil.oct());
		case 11:
			return (DayAndMonthUtil.nov());
		case 12:
			return (DayAndMonthUtil.dec());
		default:
			break;

		}
		return "";

	}

	public static ClientFinanceDate getDateFromString(String value) {
		return new ClientFinanceDate(parseDate(value));

	}

	public static Date parseDate(String value) {
		if (value == null) {
			return null;
		}
		value = value.trim();
		if (value.isEmpty()) {
			return null;
		}
		DateTimeFormat dateFormatter = DateTimeFormat.getFormat(Global.get()
				.preferences().getDateFormat());
		return dateFormatter.parse(value);
	}

	public static ClientFinanceDate getSelectedFormatDate(String value,
			String selectedformat) {
		if (value == null) {
			return null;
		}
		value = value.trim();
		if (value.isEmpty()) {
			return null;
		}
		DateTimeFormat dateFormatter = DateTimeFormat.getFormat(selectedformat);
		try {
			Date parse = dateFormatter.parse(value);
			return new ClientFinanceDate(parse);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

}
