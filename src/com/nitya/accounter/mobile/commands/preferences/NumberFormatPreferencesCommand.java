package com.nitya.accounter.mobile.commands.preferences;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.requirements.BooleanRequirement;
import com.nitya.accounter.mobile.requirements.StringListRequirement;
import com.nitya.accounter.web.client.core.ClientCompanyPreferences;
import com.nitya.accounter.web.client.ui.CoreUtils;

public class NumberFormatPreferencesCommand extends
		AbstractCompanyPreferencesCommand {

	private static final String DATE_FORMAT = "dateFormat";
	private static final String USE_CUSTOMER_NUMBERS = "usecustomernumbers";
	private static final String USE_VENDOR_NUMBERS = "usevendornumbers";
	private static final String USE_ACCOUNT_NUMBERS = "useaccountnumbers";
	private static final String TIMEZONE = "timezone";
	private static final String FISCAL_MONTH = "fiscalamonth";
	private static final String ACCOUNT_NUMBERS_RANGE = "accountNumberRangeChecking";

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		ClientCompanyPreferences preferences = context.getPreferences();
		get(DATE_FORMAT).setValue(preferences.getDateFormat());
		get(USE_CUSTOMER_NUMBERS).setValue(preferences.getUseCustomerId());
		get(USE_VENDOR_NUMBERS).setValue(preferences.getUseVendorId());
		get(USE_ACCOUNT_NUMBERS).setValue(preferences.getUseAccountNumbers());
		get(ACCOUNT_NUMBERS_RANGE).setValue(
				preferences.isAccountnumberRangeCheckEnable());
		get(TIMEZONE).setValue(preferences.getTimezone());
		// get(FISCAL_MONTH).setValue(
		// getFiscalYearMonths()
		// .get(preferences.getFiscalYearFirstMonth()));
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new StringListRequirement(DATE_FORMAT, getMessages()
				.pleaseSelect("Date Format"), "Date Format", true, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().pleaseSelect("Date Format");
			}

			@Override
			protected String getSelectString() {
				return getMessages().hasSelected("Date Format");
			}

			@Override
			protected List<String> getLists(Context context) {
				List<String> dateFormates = new ArrayList<String>();
				dateFormates.add("ddMMyy");
				dateFormates.add("MM/dd/yy");
				dateFormates.add("dd/MM/yy");
				dateFormates.add("ddMMyyyy");
				dateFormates.add("MMddyyyy");
				dateFormates.add("MMM-dd-yy");
				dateFormates.add("MMMddyyyy");
				dateFormates.add("dd/MM/yyyy");
				dateFormates.add("MM/dd/yyyy");
				dateFormates.add("dd/MMMM/yyyy");
				dateFormates.add("MMMMddyyyy");
				dateFormates.add("dd-MM-yyyy");
				dateFormates.add("MM-dd-yyyy");
				dateFormates.add("dd/MMM/yyyy");
				dateFormates.add("MMM/dd/yyyy");
				return dateFormates;
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});
		list.add(new BooleanRequirement(USE_CUSTOMER_NUMBERS, true) {

			@Override
			protected String getTrueString() {
				return "Use Customer Numbers";
			}

			@Override
			protected String getFalseString() {
				return "Do not use customer numbers";
			}
		});

		list.add(new BooleanRequirement(USE_VENDOR_NUMBERS, true) {

			@Override
			protected String getTrueString() {
				return "Use Vendor Numbers";
			}

			@Override
			protected String getFalseString() {
				return "Do not use vendor numbers";
			}
		});

		list.add(new BooleanRequirement(USE_ACCOUNT_NUMBERS, true) {

			@Override
			protected String getTrueString() {
				return "Use Account Numbers";
			}

			@Override
			protected String getFalseString() {
				return "Do not use account numbers";
			}
		});

		// list.add(new StringListRequirement(FISCAL_MONTH, getMessages()
		// .pleaseSelect(getMessages().FirstFiscalMonth()), getMessages()
		// .FirstFiscalMonth(), true, true, null) {
		//
		// @Override
		// protected String getSelectString() {
		// return getMessages().pleaseSelect(
		// getMessages().FirstFiscalMonth());
		// }
		//
		// @Override
		// protected List<String> getLists(Context context) {
		// return getFiscalYearMonths();
		// }
		//
		// @Override
		// protected String getSetMessage() {
		// return getMessages().hasSelected(
		// getMessages().FirstFiscalMonth());
		// }
		//
		// @Override
		// protected String getEmptyString() {
		// return null;
		// }
		// });

		list.add(new BooleanRequirement(ACCOUNT_NUMBERS_RANGE, true) {

			@Override
			protected String getTrueString() {
				return "Enabled account numbers range checking";
			}

			@Override
			protected String getFalseString() {
				return "Disabled account numbers range checking";
			}
		});

		list.add(new StringListRequirement(TIMEZONE, getMessages()
				.pleaseSelect(getMessages().timezone()), getMessages()
				.timezone(), true, true, null) {

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getMessages().timezone());
			}

			@Override
			protected List<String> getLists(Context context) {
				return CoreUtils.getTimeZonesAsList();
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().timezone());
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});
	}

	protected List<String> getNegativeNumFormats() {
		List<String> negativeNumberFormats = new ArrayList<String>();
		negativeNumberFormats.add("Normal Format. Example: (-200)");
		negativeNumberFormats
				.add("Negative number with in the parathesis.Example: (200)");
		negativeNumberFormats
				.add("Negative number minus with in the parathesis.Example: (-)200");
		negativeNumberFormats.add("Number with trailing minus.Example: 200-");
		return negativeNumberFormats;

	}

	private List<String> getFiscalYearMonths() {

		String[] names = new String[] { /*
										 * DayAndMonthUtil.january(),
										 * DayAndMonthUtil.february(),
										 * DayAndMonthUtil.march(),
										 * DayAndMonthUtil.april(),
										 * DayAndMonthUtil.may_full(),
										 * DayAndMonthUtil.june(),
										 * DayAndMonthUtil.july(),
										 * DayAndMonthUtil.august(),
										 * DayAndMonthUtil.september(),
										 * DayAndMonthUtil.october(),
										 * DayAndMonthUtil.november(),
										 * DayAndMonthUtil.december()
										 */};
		List<String> fiscalYearMonths = new ArrayList<String>();
		for (int i = 0; i < names.length; i++) {
			fiscalYearMonths.add(names[i]);
		}
		return fiscalYearMonths;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientCompanyPreferences preferences = context.getPreferences();
		String dateFormat = get(DATE_FORMAT).getValue();
		Boolean useCustomerNos = get(USE_CUSTOMER_NUMBERS).getValue();
		Boolean useVendorNos = get(USE_VENDOR_NUMBERS).getValue();
		Boolean useAccountNos = get(USE_ACCOUNT_NUMBERS).getValue();
		boolean isAccountNumbersRangeChecking = get(ACCOUNT_NUMBERS_RANGE)
				.getValue();
		String timeZone = get(TIMEZONE).getValue();
		// String fiscalMonth = get(FISCAL_MONTH).getValue();
		preferences.setDateFormat(dateFormat);
		preferences.setUseCustomerId(useCustomerNos);
		preferences.setUseVendorId(useVendorNos);
		preferences.setUseAccountNumbers(useAccountNos);
		preferences.setIsAccountNumberRangeCheck(isAccountNumbersRangeChecking);
		// preferences.setFiscalYearFirstMonth(getFiscalYearMonths().indexOf(
		// fiscalMonth));
		preferences.setTimezone(timeZone);
		savePreferences(context, preferences);
		return null;
	}
}
