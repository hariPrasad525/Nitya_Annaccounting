package com.vimukti.accounter.web.client.ui.reports;

import java.util.HashMap;
import java.util.Map;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;

public abstract class PortletToolBar extends ReportToolbar {

	public Map<String, String> portletConfigData = new HashMap<String, String>();
	public static final int THIS_MONTH = 0;
	public static final int LAST_MONTH = 1;
	public static final int THIS_FINANCIAL_YEAR = 2;
	public static final int LAST_FINANCIAL_YEAR = 3;
	public static final int THIS_QUARTER = 4;
	public static final int LAST_QUARTER = 5;
	protected String[] allDateRangeArray = { messages.all(),
			messages.thisWeek(), messages.thisMonth(), messages.lastWeek(),
			messages.lastMonth(), messages.thisFinancialYear(),
			messages.lastFinancialYear(), messages.thisFinancialQuarter(),
			messages.lastFinancialQuarter(), messages.financialYearToDate() };
	protected String[] monthDateRangeArray = { messages.thisMonth(),
			messages.lastMonth(), messages.thisFinancialYear(),
			messages.lastFinancialYear(), messages.thisFinancialQuarter(),
			messages.lastFinancialQuarter() };

	public static int getDateRangeType(String dateRange) {
		if (dateRange.equals(messages.thisMonth())) {
			return THIS_MONTH;
		} else if (dateRange.equals(messages.lastMonth())) {
			return LAST_MONTH;
		} else if (dateRange.equals(messages.thisFinancialYear())) {
			return THIS_FINANCIAL_YEAR;
		} else if (dateRange.equals(messages.lastFinancialYear())) {
			return LAST_FINANCIAL_YEAR;
		} else if (dateRange.equals(messages.thisFinancialQuarter())) {
			return THIS_QUARTER;
		} else if (dateRange.equals(messages.lastFinancialQuarter())) {
			return LAST_QUARTER;
		}
		return THIS_MONTH;
	}

	/**
	 * get default date range for date range combo
	 */
	protected abstract void initOrSetConfigDataToPortletConfig();

	protected abstract void refreshPortletData();

	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setStartAndEndDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		// TODO Auto-generated method stub

	}

}
