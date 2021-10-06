package com.nitya.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.ResultList;
import com.nitya.accounter.mobile.commands.AbstractCommand;
import com.nitya.accounter.mobile.requirements.ChangeListner;
import com.nitya.accounter.mobile.requirements.DateRequirement;
import com.nitya.accounter.mobile.requirements.StringListRequirement;
import com.nitya.accounter.mobile.utils.CommandUtils;
import com.nitya.accounter.web.client.core.ClientFinanceDate;

public abstract class NewAbstractReportCommand<T> extends AbstractCommand {
	protected static final int REPORTS_TO_SHOW = 20;
	protected static final int VATAGENCIES_TO_SHOW = 20;
	protected static final int CUSTOMERS_TO_SHOW = 20;
	protected static final String DATE_RANGE = "Date Range";
	protected static final String FROM_DATE = "From Date";
	protected static final String TO_DATE = "To Date";
	protected static final String TAX_AGENCY = "Tax Agency";
	protected static final String STATUS = "Status";
	protected static final String EXPENSE_TYPE = "Expense Type";
	protected static final String VENDOR_PREPAYMENT = "Supplier Prepayment";
	protected static final String VAT_AGENCIES = "Tax Agencies";
	protected static final String RECORDS = "records";
	protected static final String ENDING_DATE = "Ending Date";
	protected static final String CUSTOMER = "Customer";
	protected static final String PAY_HEAD = "Payhead";
	protected static final String EMPLOYEE = "Employee";
	private String previousSelectedRange;

	protected List<ResultList> categories = new ArrayList<ResultList>();
	protected ClientFinanceDate startDate;
	protected ClientFinanceDate endDate;

	/**
	 * Adds Date range,from date and to date requirements to existing
	 * requirements.
	 * 
	 * @param list
	 */
	protected void addDateRangeFromToDateRequirements(List<Requirement> list) {
		list.add(addDateRangeRequirement());
		addFromToDateRequirements(list);
	}

	protected void addFromToDateRequirements(List<Requirement> list) {
		list.add(getFromDateRequirement());
		list.add(getToDateRequirement());
	}

	private Requirement getToDateRequirement() {
		return new DateRequirement(TO_DATE, getMessages().pleaseEnter(
				getMessages().endDate()), getMessages().endDate(), true, true) {
			@Override
			public void setValue(Object value) {
				endDate = (ClientFinanceDate) value;
				super.setValue(value);
			}
		};
	}

	private Requirement getFromDateRequirement() {
		return new DateRequirement(FROM_DATE, getMessages().pleaseEnter(
				getMessages().startDate()), getMessages().startDate(), true,
				true) {
			@Override
			public void setValue(Object value) {
				startDate = (ClientFinanceDate) value;
				super.setValue(value);
			}
		};
	}

	protected void addDateRangeFromDateRequirements(List<Requirement> list) {
		list.add(addDateRangeRequirement());
		list.add(getFromDateRequirement());
	}

	protected void addDateRangeToDateRequirements(List<Requirement> list) {
		list.add(addDateRangeRequirement());
		list.add(getToDateRequirement());
	}

	private Requirement addDateRangeRequirement() {
		return new StringListRequirement(DATE_RANGE, getMessages().pleaseEnter(
				getMessages().dateRange()), getMessages().dateRange(), true,
				true, new ChangeListner<String>() {

					@Override
					public void onSelection(String value) {
						dateRangeChanged(value);
					}
				}) {

			@Override
			protected String getSetMessage() {
				return null;
			}

			@Override
			protected String getSelectString() {
				return null;
			}

			@Override
			protected List<String> getLists(Context context) {
				String[] dateRangeArray = { getMessages().all(),
						getMessages().thisWeek(), getMessages().thisMonth(),
						getMessages().lastWeek(), getMessages().lastMonth(),
						getMessages().thisFinancialYear(),
						getMessages().lastFinancialYear(),
						getMessages().thisFinancialQuarter(),
						getMessages().lastFinancialQuarter(),
						getMessages().financialYearToDate(),
						getMessages().custom() };
				return Arrays.asList(dateRangeArray);
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		};
	}

	protected void dateRangeChanged(String dateRange) {
		List<ClientFinanceDate> minimumAndMaximumDates = CommandUtils
				.getMinimumAndMaximumTransactionDate(getCompanyId());
		if (minimumAndMaximumDates.isEmpty()) {
			return;
		}
		Map<String, Object> dateRangeChanged = CommandUtils.dateRangeChanged(
				getMessages(), dateRange, getPreferences(), startDate, endDate,
				minimumAndMaximumDates.get(0));
		this.startDate = (ClientFinanceDate) dateRangeChanged.get("startDate");
		if (get(FROM_DATE) != null) {
			get(FROM_DATE).setValue(startDate);
		}
		this.endDate = (ClientFinanceDate) dateRangeChanged.get("endDate");
		if (get(TO_DATE) != null) {
			get(TO_DATE).setValue(endDate);
		}
		FinanceDate[] financeDates = CommandUtils.getMinimumAndMaximumDates(
				startDate, endDate, getCompanyId());
		setStartEndDates(financeDates);
		previousSelectedRange = dateRange;
		get(DATE_RANGE).setValue(previousSelectedRange);
	}

	@Override
	public String getId() {
		return null;
	}

	/**
	 * to get last date of the month.
	 **/
	public FinanceDate getLastMonth(FinanceDate date) {
		int month = date.getMonth() - 1;
		int year = date.getYear();

		int lastDay;
		switch (month) {
		case 0:
		case 2:
		case 4:
		case 6:
		case 7:
		case 9:
		case 11:
			lastDay = 31;
			break;
		case 1:
			if (year % 4 == 0 && year % 100 == 0)
				lastDay = 29;
			else
				lastDay = 28;
			break;

		default:
			lastDay = 30;
			break;
		}
		return new FinanceDate(date.getYear(), date.getMonth() - 1, lastDay);
		// return lastDay;
	}

	protected FinanceDate getStartDate() {
		return new FinanceDate(startDate);
	}

	protected FinanceDate getEndDate() {
		return new FinanceDate(endDate);
	}

	@Override
	protected void setDefaultValues(Context context) {
		startDate = CommandUtils.getCurrentFiscalYearStartDate(context
				.getPreferences());
		endDate = CommandUtils.getCurrentFiscalYearEndDate(context
				.getPreferences());
		if (get(FROM_DATE) != null) {
			get(FROM_DATE).setValue(startDate);
		}
		if (get(TO_DATE) != null) {
			get(TO_DATE).setValue(endDate);
		}
		if (get(DATE_RANGE) != null) {
			get(DATE_RANGE).setValue(getMessages().all());
		}
	}

	@Override
	protected String getWelcomeMessage() {
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		return null;
	}

	@Override
	public String getSuccessMessage() {
		return null;
	}

	protected void setStartEndDates(FinanceDate[] minimumAndMaximumDates) {
		this.startDate = minimumAndMaximumDates[0].toClientFinanceDate();
		Requirement fromDateReq = get(FROM_DATE);
		if (fromDateReq != null) {
			fromDateReq.setValue(startDate);
		}
		this.endDate = minimumAndMaximumDates[1].toClientFinanceDate();
		Requirement toDateReq = get(TO_DATE);
		if (toDateReq != null) {
			toDateReq.setValue(endDate);
		}

	}

}
