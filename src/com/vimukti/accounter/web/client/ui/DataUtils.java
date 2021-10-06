package com.vimukti.accounter.web.client.ui;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientUnit;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.forms.RegExpValidator;
import com.vimukti.accounter.web.client.ui.forms.Validator;

public class DataUtils {

	protected static AccounterMessages messages = Global.get().messages();

	public static Validator phoneValidator() {
		Validator custValidator = new Validator() {

			@Override
			public boolean validate(FormItem formItem) {
				if (formItem.getValue() == null) {
					return true;
				}
				String val = formItem.getValue().toString();
				return val.matches("\\d{10}");

			}
		};
		return custValidator;
	}

	public static Validator faxValidator() {
		Validator custValidator = new Validator() {

			@Override
			public boolean validate(FormItem formItem) {
				if (formItem.getValue() == null) {
					return true;
				}
				String val = formItem.getValue().toString();
				return val.matches("\\d{10}");
			}
		};
		return custValidator;
	}

	public static Validator emailValidator() {
		RegExpValidator validator = new RegExpValidator();
		validator.setExpression(messages.emailFormatExpr());
		return validator;
	}

	public static Validator webValidator() {
		RegExpValidator validator = new RegExpValidator();
		validator.setExpression(messages.webFormatExpr());
		return validator;
	}

	public static Double getBalance(String balanceGiven) {
		try {
			return getAmountStringAsDouble(balanceGiven);
		} catch (Exception e) {
			return 0.00D;
		}
	}

	public final static native String removeSpaces(String s)/*-{
		var tokens = s.split(" ");
		var resultingString = "";

		for (i = 0; i < tokens.length; i++) {
			if (!tokens[i] == " ")
				resultingString = resultingString + tokens[i] + " ";
		}
		return resultingString;
	}-*/;

	/*
	 * /*-{ var tokens=s.split(" "); var resultingString=""; $wnd.alert(s);
	 * 
	 * varcurrentSymbol=this.@com.vimukti.accounter.web.client.ui.UIUtils::
	 * getCurrencySymbol();
	 * 
	 * for(i = 0; i < tokens.length; i++){ if(!(tokens[i]==currentSymbol)){
	 * resultingString=resultingString + tokens[i]; } }
	 * $wnd.alert(resultingString); return resultingString; }-
	 */

	public static boolean isValidAmount(String string) {
		if (string == null || string.equals(""))
			return true;

		boolean valid = true;
		for (int i = 0; i < string.length(); i++) {
			char ch = string.charAt(i);
			if ((Character.isDigit(ch)) || (ch == '.') || (ch == '-'))
				continue;
			else {
				valid = false;
				break;
			}

		}
		return valid;
	}

	public static Validator zipValidator() {
		Validator custValidator = new Validator() {

			@Override
			public boolean validate(FormItem formItem) {
				if (formItem.getValue() == null) {
					return true;
				}
				String val = formItem.getValue().toString();
				return val.matches("\\d{5}");
			}
		};
		return custValidator;
	}

	/*
	 * This method removes white spaces,currency symbol,comma symbol from the
	 * amount string
	 * 
	 * @return amount as double
	 */
	public static double getReformatedAmount(String amount) {
		double amountAsDouble = 0.0;
		if (amount != null) {
			amount = amount.replaceAll("\\" + UIUtils.getCurrencySymbol(), "");
			amount = amount.replaceAll("\\" + "%", "");
			amount = amount.trim();
			amount = amount.replaceAll(" ", "");
			amount = amount.replaceAll(",", "");
			try {
				// amountAsDouble = Double.parseDouble(amount);
				amountAsDouble = getAmountStringAsDouble(amount);
			} catch (Exception e) {
			}
		}
		return amountAsDouble;
	}

	// public static String getAmountAsString(Double amount) {
	//
	// if (amount == null)
	// amount = 0.00;
	//
	// StringBuffer buffer = null;
	//
	// boolean isNagative = false;
	//
	// if (amount >= 0)
	// buffer = new StringBuffer("" + UIUtils.getCurrencySymbol() + " ");
	// else {
	// isNagative=true;
	// amount = amount * (-1);
	// buffer = new StringBuffer("-" + UIUtils.getCurrencySymbol());
	//
	// }
	//
	// /*
	// * It will format the 'amount' value to precission of 2
	// */
	// long factor = (long) Math.pow(10, 2);
	// amount = amount * factor;
	// long tmp = Math.round(amount);
	// amount = (double) tmp / factor;
	//
	// // double value = amount;
	// // value = value * 100;
	// // value = (double) ((int) value);
	// // amount = value / 100;
	//
	// // buffer.append(com.google.gwt.i18n.client.NumberFormat
	// // .getDecimalFormat().format(amount));
	// buffer.append(amount);
	//
	// /*
	// * It will append '0' if the precision is 1 i.e, 200.0 will be formated
	// * to 200.00
	// */
	//
	// String string = buffer.toString();
	// if (!string.contains("."))
	// buffer.append(".00");
	// else {
	// int index = string.indexOf(".");
	// String sub = string.substring(index);
	// if (sub.length() == 2)
	// buffer.append("0");
	// }
	//
	// // if (isNagative)
	// // buffer.append(")");
	//
	// return buffer.toString();
	// }

	public static String getdoubleScallingValue(Double value) {
		String convertedvalue = (com.google.gwt.i18n.client.NumberFormat
				.getDecimalFormat().format(value));
		return convertedvalue;
	}

	public static String getAmountAsStrings(Double amount) {

		if (amount == null)
			amount = 0.00D;

		StringBuffer buffer = null;

		boolean isNagative = false;

		if (!DecimalUtil.isLessThan(amount, 0))
			buffer = new StringBuffer("" + UIUtils.getCurrencySymbol() + " ");
		else {
			isNagative = true;
			amount = amount * (-1);
			buffer = new StringBuffer("" + UIUtils.getCurrencySymbol() + " ");

		}

		buffer.append(com.google.gwt.i18n.client.NumberFormat
				.getDecimalFormat().format(amount));

		return buffer.toString();
	}

	public static String getAmountAsStringInPrimaryCurrency(Double amount) {
		if (amount == null)
			amount = 0.00;
		return getAmountAsStringInCurrency(amount, Global.get().preferences()
				.getPrimaryCurrency().getSymbol());

		// String decimalCharacter = Global.get().preferences()
		// .getDecimalCharacter();
		// StringBuffer buffer = new StringBuffer();
		//
		// /*
		// * It will format the 'amount' value to precission of 2
		// */
		//
		// double tmp1 = amount;
		// long factor = (long) Math.pow(10, 2);
		// tmp1 = tmp1 * factor;
		// long tmp = Math.round(Math.abs(tmp1));
		//
		// amount = ((DecimalUtil.isLessThan(amount, 0) && tmp != 0) ? -1 : 1)
		// * ((double) tmp / factor);
		//
		// buffer.append(amount);
		//
		// /*
		// * It will append '0' if the precision is 1 i.e, 200.0 will be
		// formated
		// * to 200.00
		// */
		//
		// String string = buffer.toString();
		// if (!string.contains(decimalCharacter))
		// buffer.append(".00");
		// else {
		// int index = string.indexOf(decimalCharacter);
		// String sub = string.substring(index);
		// if (sub.length() == 2)
		// buffer.append("0");
		// }
		// String nextStr = buffer.substring(buffer.indexOf(decimalCharacter));
		// if (buffer.charAt(0) == '-') {
		// String sign = "-";
		// String valueWithSign = sign
		// + insertCommas(buffer.substring(1,
		// buffer.indexOf(decimalCharacter))) + nextStr;
		// return valueWithSign;
		// }
		// String value = insertCommas(buffer.substring(0,
		// buffer.indexOf(decimalCharacter)))
		// + nextStr;
		//
		// return value;
	}

	private static String insertCommas(String str) {

		if (str.length() < 4) {
			return str;
		}
		return insertCommas(str.substring(0, str.length() - 3)) + ","
				+ str.substring(str.length() - 3, str.length());
	}

	public static String getNumberAsPercentString(String value) {
		double number = getReformatedAmount(value);
		if (value == null)
			value = "0.0";
		StringBuffer buffer = null;
		if (!DecimalUtil.isLessThan(number, 0)) {
			buffer = new StringBuffer("");
			buffer.append(com.google.gwt.i18n.client.NumberFormat
					.getDecimalFormat().format(number));
			buffer.append(" %");
		} else {
			number = number * (-1);
			buffer = new StringBuffer("(");

			buffer.append(buffer.append(com.google.gwt.i18n.client.NumberFormat
					.getDecimalFormat().format(number)));
			buffer.append(" %)");
		}

		return buffer.toString();

	}

	public static Double getAmountStringAsDouble(String amountAsString)
			throws Exception {

		return NumberFormat2.getInstance().parse(amountAsString);

	}

	public static String getDiscountString(Double discount) {
		if (discount == null)
			discount = 0.0D;

		StringBuffer buffer = new StringBuffer(discount.toString());

		buffer.append(" %");

		return buffer.toString();
	}

	public static String amountAsStringWithCurrency(double amount,
			long currencyID) {
		ClientCurrency currency = Accounter.getCompany()
				.getCurrency(currencyID);
		if (currency == null) {
			currency = Accounter.getCompany().getPrimaryCurrency();
		}
		return amountAsStringWithCurrency(amount, currency);
	}

	public static String amountAsStringWithCurrency(Double amount,
			ClientCurrency currency) {
		return getAmountAsStringInCurrency(amount, currency.getSymbol());
	}

	public static String getAmountAsStringInCurrency(double amount,
			String currencySymbol) {
		return Global.get().toCurrencyFormat(amount, currencySymbol);
	}

	/**
	 * It will subtract qty2 from qty1
	 * 
	 * @param qty1
	 * @param qty2
	 * @return
	 */
	public static ClientQuantity subtractQuantities(ClientQuantity qty1,
			ClientQuantity qty2) {
		ClientQuantity qty = new ClientQuantity();
		qty.setValue(-qty2.getValue());
		qty.setUnit(qty2.getUnit());
		return addQuantities(qty1, qty);
	}

	public static ClientQuantity addQuantities(ClientQuantity qty1,
			ClientQuantity qty2) {
		ClientCompany company = Accounter.getCompany();
		ClientUnit unit1 = company.getUnitById(qty1.getUnit());
		ClientUnit unit2 = company.getUnitById(qty2.getUnit());

		if (unit1 == null ^ unit2 == null) {
			// one unit is null and another one is not null.
			throw new IllegalArgumentException(
					"Can't able to add, null type mismatch");
		}

		if (unit1 != null && unit2.getMeasurement() != unit1.getMeasurement()) {
			// unit available, but the both are not belongs to same Measurement.
			throw new IllegalArgumentException(
					"Can't able to add different Unit types");
		}

		/*
		 * convert the quantities to default measure
		 */
		ClientQuantity thisQuantity = qty1.convertToDefaultUnit(unit1);
		ClientQuantity otherQuantity = qty2.convertToDefaultUnit(unit2);

		/*
		 * add the default quantities to make result quantity.
		 */
		ClientQuantity resultQuantity = new ClientQuantity();
		if (unit1 != null) {
			resultQuantity.setUnit(qty1.getDefaultUnitId(unit1));
		}
		resultQuantity.setValue(thisQuantity.getValue()
				+ otherQuantity.getValue());

		return resultQuantity;
	}

	public static String getQuantityAsString(ClientQuantity quantity) {
		ClientCompanyPreferences preferences = Global.get().preferences();
		ClientCompany company = Accounter.getCompany();
		StringBuffer data = new StringBuffer();
		data.append(String.valueOf(quantity.getValue()));

		// if (preferences.isUnitsEnabled()) {
		// ClientUnit unit = Accounter.getCompany().getUnitById(
		// quantity.getUnit());
		// data.append(" ");
		// if (unit != null) {
		// data.append(unit.getType());
		// }
		// }
		return data.toString();
	}

}

class NumberFormat2 {
	public static final String COMMA = ",";
	public static final String PERIOD = Global.get().preferences()
			.getDecimalCharacter();
	public static final char DASH = '-';
	public static final char LEFT_PAREN = '(';
	public static final char RIGHT_PAREN = ')';

	// k/m/b Shortcuts
	public static final String THOUSAND = "k";
	public static final String MILLION = "m";
	public static final String BILLION = "b";

	// currency position constants
	public static final int CUR_POS_LEFT_OUTSIDE = 0;
	public static final int CUR_POS_LEFT_INSIDE = 1;
	public static final int CUR_POS_RIGHT_INSIDE = 2;
	public static final int CUR_POS_RIGHT_OUTSIDE = 3;

	// negative format constants
	public static final int NEG_LEFT_DASH = 0;
	public static final int NEG_RIGHT_DASH = 1;
	public static final int NEG_PARENTHESIS = 2;

	// constant to signal that fixed precision is not to be used
	public static final int ARBITRARY_PRECISION = -1;

	private String inputDecimalSeparator = PERIOD; // decimal character used on
	// the original string

	private boolean showGrouping = true;
	private String groupingSeparator = COMMA; // thousands grouping character
	private String decimalSeparator = PERIOD; // decimal point character

	private boolean showCurrencySymbol = false;
	private String currencySymbol = "" + UIUtils.getCurrencySymbol() + "";
	private int currencySymbolPosition = CUR_POS_LEFT_OUTSIDE;

	private int negativeFormat = NEG_LEFT_DASH;
	private boolean isNegativeRed = false; // wrap the output in html that will
	// display red?

	private int decimalPrecision = 0;
	private boolean useFixedPrecision = false;
	private boolean truncate = false; // truncate to decimalPrecision rather
	// than rounding?

	private boolean isPercentage = false; // should the result be displayed as a

	// percentage?

	private NumberFormat2() {
	}

	/**
	 * returns the default instance of NumberFormat
	 * 
	 * @return
	 */
	public static NumberFormat2 getInstance() {
		NumberFormat2 instance = new NumberFormat2();
		instance.setDecimalSeparator(Global.get().preferences()
				.getDecimalCharacter());
		instance.setInputDecimalSeparator(Global.get().preferences()
				.getDecimalCharacter());
		instance.setGroupingSeparator(Global.get().preferences()
				.getDigitGroupCharacter());
		return instance;
	}

	/**
	 * Returns a currency instance of number format
	 * 
	 * @return
	 */
	public static NumberFormat2 getCurrencyInstance() {
		return getCurrencyInstance("" + UIUtils.getCurrencySymbol() + "", true);
	}

	/**
	 * Returns a currency instance of number format that uses curSymbol as the
	 * currency symbol
	 * 
	 * @param curSymbol
	 * @return
	 */
	public static NumberFormat2 getCurrencyInstance(String curSymbol) {
		return getCurrencyInstance(curSymbol, true);
	}

	/**
	 * Returns a currency instance of number format that uses curSymbol as the
	 * currency symbol and either commas or periods as the thousands separator.
	 * 
	 * @param curSymbol
	 *            Currency Symbol
	 * @param useCommas
	 *            true, uses commas as the thousands separator, false uses
	 *            periods
	 * @return
	 */
	public static NumberFormat2 getCurrencyInstance(String curSymbol,
			boolean useCommas) {
		NumberFormat2 nf = new NumberFormat2();
		nf.isCurrency(true);
		nf.setCurrencySymbol(curSymbol);
		if (!useCommas) {
			nf.setInputDecimalSeparator(Global.get().preferences()
					.getDecimalCharacter());
			nf.setDecimalSeparator(Global.get().preferences()
					.getDecimalCharacter());
			nf.setGroupingSeparator(Global.get().preferences()
					.getDigitGroupCharacter());
		}
		nf.setFixedPrecision(2);
		return nf;
	}

	/**
	 * Returns an instance that formats numbers as integers.
	 * 
	 * @return
	 */
	public static NumberFormat2 getIntegerInstance() {
		NumberFormat2 nf = new NumberFormat2();
		nf.setShowGrouping(false);
		nf.setFixedPrecision(0);
		nf.setInputDecimalSeparator(Global.get().preferences()
				.getDecimalCharacter());
		nf.setDecimalSeparator(Global.get().preferences().getDecimalCharacter());
		nf.setGroupingSeparator(Global.get().preferences()
				.getDigitGroupCharacter());
		return nf;
	}

	public static NumberFormat2 getPercentInstance() {
		NumberFormat2 nf = new NumberFormat2();
		nf.isPercentage(true);
		nf.setFixedPrecision(2);
		nf.setShowGrouping(false);
		nf.setInputDecimalSeparator(Global.get().preferences()
				.getDecimalCharacter());
		nf.setDecimalSeparator(Global.get().preferences().getDecimalCharacter());
		nf.setGroupingSeparator(Global.get().preferences()
				.getDigitGroupCharacter());
		return nf;
	}

	public String format(String num) {
		return toFormatted(parse(num));
	}

	public double parse(String num) {
		return asNumber(num, inputDecimalSeparator);
	}

	/**
	 * Static routine that attempts to create a double out of the supplied text.
	 * This routine is a bit smarter than Double.parseDouble()
	 * 
	 * @param num
	 * @return
	 */
	public static double parseDouble(String num, String decimalChar) {
		return asNumber(num, decimalChar);
	}

	public static double parseDouble(String num) {
		return parseDouble(num, PERIOD);
	}

	public void setInputDecimalSeparator(String val) {
		inputDecimalSeparator = val == null ? PERIOD : val;
	}

	public void setNegativeFormat(int format) {
		negativeFormat = format;
	}

	public void setNegativeRed(boolean isRed) {
		isNegativeRed = isRed;
	}

	public void setShowGrouping(boolean show) {
		showGrouping = show;
	}

	public void setDecimalSeparator(String separator) {
		decimalSeparator = separator;
	}

	public void setGroupingSeparator(String separator) {
		groupingSeparator = separator;
	}

	public void isCurrency(boolean isC) {
		showCurrencySymbol = isC;
	}

	public void setCurrencySymbol(String symbol) {
		currencySymbol = symbol;
	}

	public void setCurrencyPosition(int cp) {
		currencySymbolPosition = cp;
	}

	public void isPercentage(boolean pct) {
		isPercentage = pct;
	}

	/**
	 * Sets the number of fixed precision decimal places should be displayed. To
	 * use arbitrary precision,
	 * setFixedPrecision(NumberFormat.ARBITRARY_PRECISION)
	 * 
	 * @param places
	 */
	public void setFixedPrecision(int places) {
		useFixedPrecision = places != ARBITRARY_PRECISION;
		this.decimalPrecision = places < 0 ? 0 : places;
	}

	/**
	 * Causes the number to be truncated rather than rounded to its fixed
	 * precision.
	 * 
	 * @param trunc
	 */
	public void setTruncate(boolean trunc) {
		truncate = trunc;
	}

	/**
	 * 
	 * @param preSep
	 *            raw number as text
	 * @param PERIOD
	 *            incoming decimal point
	 * @param decimalSeparator
	 *            outgoing decimal point
	 * @param groupingSeparator
	 *            thousands separator
	 * @return
	 */
	private String addSeparators(String preSep) {
		String nStr = preSep;
		int dpos = nStr.indexOf(PERIOD);
		String nStrEnd = "";
		if (dpos != -1) {
			nStrEnd = decimalSeparator
					+ nStr.substring(dpos + 1, nStr.length());
			nStr = nStr.substring(0, dpos);
		}
		int l = nStr.length();
		for (int i = l; i > 0; i--) {
			nStrEnd = nStr.charAt(i - 1) + nStrEnd;
			if (i != 1 && ((l - i + 1) % 3) == 0)
				nStrEnd = groupingSeparator + nStrEnd;
		}
		return nStrEnd;
	}

	protected String toFormatted(double num) {
		String nStr;

		if (isPercentage)
			num = num * 100;

		nStr = useFixedPrecision ? toFixed(Math.abs(getRounded(num)),
				decimalPrecision) : Double.toString(num);

		nStr = showGrouping ? addSeparators(nStr) : nStr.replaceAll("\\"
				+ PERIOD, decimalSeparator);

		String c0 = "";
		String n0 = "";
		String c1 = "";
		String n1 = "";
		String n2 = "";
		String c2 = "";
		String n3 = "";
		String c3 = "";

		String negSignL = ""
				+ ((negativeFormat == NEG_PARENTHESIS) ? LEFT_PAREN : DASH);
		String negSignR = ""
				+ ((negativeFormat == NEG_PARENTHESIS) ? RIGHT_PAREN : DASH);

		if (currencySymbolPosition == CUR_POS_LEFT_OUTSIDE) {
			if (DecimalUtil.isLessThan(num, 0)) {
				if (negativeFormat == NEG_LEFT_DASH
						|| negativeFormat == NEG_PARENTHESIS)
					n1 = negSignL;
				if (negativeFormat == NEG_RIGHT_DASH
						|| negativeFormat == NEG_PARENTHESIS)
					n2 = negSignR;
			}
			if (showCurrencySymbol)
				c0 = currencySymbol;
		} else if (currencySymbolPosition == CUR_POS_LEFT_INSIDE) {
			if (DecimalUtil.isLessThan(num, 0)) {
				if (negativeFormat == NEG_LEFT_DASH
						|| negativeFormat == NEG_PARENTHESIS)
					n0 = negSignL;
				if (negativeFormat == NEG_RIGHT_DASH
						|| negativeFormat == NEG_PARENTHESIS)
					n3 = negSignR;
			}
			if (showCurrencySymbol)
				c1 = currencySymbol;
		} else if (currencySymbolPosition == CUR_POS_RIGHT_INSIDE) {
			if (DecimalUtil.isLessThan(num, 0)) {
				if (negativeFormat == NEG_LEFT_DASH
						|| negativeFormat == NEG_PARENTHESIS)
					n0 = negSignL;
				if (negativeFormat == NEG_RIGHT_DASH
						|| negativeFormat == NEG_PARENTHESIS)
					n3 = negSignR;
			}
			if (showCurrencySymbol)
				c2 = currencySymbol;
		} else if (currencySymbolPosition == CUR_POS_RIGHT_OUTSIDE) {
			if (DecimalUtil.isLessThan(num, 0)) {
				if (negativeFormat == NEG_LEFT_DASH
						|| negativeFormat == NEG_PARENTHESIS)
					n1 = negSignL;
				if (negativeFormat == NEG_RIGHT_DASH
						|| negativeFormat == NEG_PARENTHESIS)
					n2 = negSignR;
			}
			if (showCurrencySymbol)
				c3 = currencySymbol;
		}
		nStr = c0 + n0 + c1 + n1 + nStr + n2 + c2 + n3 + c3
				+ (isPercentage ? "%" : "");

		if (isNegativeRed && DecimalUtil.isLessThan(num, 0)) {
			nStr = "<font color='red'>" + nStr + "</font>";
		}

		return nStr;
	}

	/**
	 * javascript only rounds to whole numbers, so we need to shift our decimal
	 * right, then round, then shift the decimal back left.
	 * 
	 * @param val
	 * @return
	 */
	private double getRounded(double val) {
		double exp = Math.pow(10, decimalPrecision);
		double rounded = val * exp;
		if (truncate)
			rounded = !DecimalUtil.isLessThan(rounded, 0) ? Math.floor(rounded)
					: Math.ceil(rounded);
		else
			rounded = Math.round(rounded);
		return rounded / exp;
	}

	private static native String toFixed(double val, int places) /*-{
		return val.toFixed(places);
	}-*/;

	private static double asNumber(String val, String inputDecimalValue) {
		if (val == null)
			return 0.0d;
		String newVal = val;
		boolean isPercentage = false;
		// remove % if there is one

		if (newVal.indexOf('%') != -1) {
			newVal = newVal.replaceAll("\\%", "");
			isPercentage = true;
		}

		// convert abbreviations for thousand, million and billion
		newVal = newVal.toLowerCase().replaceAll(BILLION, "000000000");
		newVal = newVal.replaceAll(MILLION, "000000");
		newVal = newVal.replaceAll(THOUSAND, "000");

		// remove any characters that are not digit, decimal separator, +, -, (,
		// ), e, or E
		String re = "[^\\" + inputDecimalValue + "\\d\\-\\+\\(\\)eE]";
		newVal = newVal.replaceAll(" ", "");

		// ensure that the first decimal separator is a . and remove the rest.
		int index = inputDecimalValue == null || inputDecimalValue.isEmpty() ? -1
				: newVal.indexOf(inputDecimalValue);
		if (index != -1) {
			newVal = newVal.replaceAll(re, "");
			newVal = newVal.substring(0, newVal.indexOf(inputDecimalValue))
					+ PERIOD
					+ (newVal.substring(newVal.indexOf(inputDecimalValue)
							+ inputDecimalValue.length()).replaceAll("\\"
							+ inputDecimalValue, ""));
		}

		// convert right dash and paren negatives to left dash negative
		if (!newVal.equals("") && newVal.charAt(newVal.length() - 1) == DASH) {
			newVal = newVal.substring(0, newVal.length() - 1);
			newVal = DASH + newVal;
		} else if (!newVal.equals("") && newVal.charAt(0) == LEFT_PAREN
				&& newVal.charAt(newVal.length() - 1) == RIGHT_PAREN) {
			newVal = newVal.substring(1, newVal.length() - 1);
			newVal = DASH + newVal;
		}

		Double parsed;
		try {
			parsed = new Double(newVal);
			if (parsed.isInfinite() || parsed.isNaN())
				parsed = new Double(0);
		} catch (NumberFormatException e) {
			parsed = new Double(0);
		}

		return isPercentage ? parsed.doubleValue() / 100 : parsed.doubleValue();
	}

}
