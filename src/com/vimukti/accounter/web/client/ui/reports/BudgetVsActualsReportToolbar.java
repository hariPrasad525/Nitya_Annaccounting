package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientBudget;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class BudgetVsActualsReportToolbar extends ReportToolbar {

	protected SelectCombo monthSelectCombo, quaterlySelectCombo, budgetName,
			dateRangeSelect, accountCombo;
	protected List<String> statusList, dateRangeList, yearRangeList;
	List<String> budgetArray = new ArrayList<String>();
	List<Long> idArray = new ArrayList<Long>();
	List<Integer> startYearArray = new ArrayList<Integer>();
	List<String> financialMonthArray = new ArrayList<String>();
	List<Integer> endYearArray = new ArrayList<Integer>();

	long budgetId = 999L;

	ClientFinanceDate reportStartDate;
	ClientFinanceDate reportEndDate;
	int reportType = 0;

	public BudgetVsActualsReportToolbar() {
		createData();
	}

	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		reportview.makeReportRequest(budgetId, startDate, endDate, reportType);
	}

	@Override
	public void setStartAndEndDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		setStartDate(startDate);
		setEndDate(endDate);
	}

	@Override
	public void setDefaultDateRange(String defaultDateRange) {
		dateRangeChanged(defaultDateRange);
	}

	public void createControls() {

		budgetName = new SelectCombo(messages.budget());
		statusList = new ArrayList<String>();
		for (String str : budgetArray) {
			statusList.add(str);
		}
		if (budgetArray.size() < 1) {
			statusList.add("");
		}

		budgetName.initCombo(statusList);
		budgetName.setSelected(statusList.get(0));
		budgetName
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						budgetId = idArray.get(budgetName.getSelectedIndex());

						accountCombo.show();
						dateRangeSelect.show();
						reportview.makeReportRequest(budgetId, reportStartDate,
								reportEndDate, reportType);
						// monthSelectCombo.show();
						// quaterlySelectCombo.show();
					}
				});

		dateRangeSelect = new SelectCombo(messages.dateRange());
		dateRangeSelect.initCombo(getDateRangeList());
		dateRangeSelect.setSelectedItem(2);
		dateRangeSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (selectItem.equals(messages.accountsVsMonths())) {
							monthSelectCombo.show();
							quaterlySelectCombo.hide();
						} else if (selectItem.equals(messages
								.accountsVsQuaters())) {
							quaterlySelectCombo.show();
							monthSelectCombo.hide();
						} else {
							quaterlySelectCombo.hide();
							monthSelectCombo.hide();
							createDates();
							changeDates(reportStartDate, reportEndDate);
						}
					}
				});

		monthSelectCombo = new SelectCombo(messages.month());
		monthSelectCombo.initCombo(getMonthNames());
		monthSelectCombo.setSelectedItem(0);
		monthSelectCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (selectItem.equals(getMonthNames().get(0))) {
							createStartDate(getMonthNames().get(0));
						} else if (selectItem.equals(getMonthNames().get(1))) {
							createStartDate(getMonthNames().get(1));
						} else if (selectItem.equals(getMonthNames().get(2))) {
							createStartDate(getMonthNames().get(2));
						} else if (selectItem.equals(getMonthNames().get(3))) {
							createStartDate(getMonthNames().get(3));
						} else if (selectItem.equals(getMonthNames().get(4))) {
							createStartDate(getMonthNames().get(4));
						} else if (selectItem.equals(getMonthNames().get(5))) {
							createStartDate(getMonthNames().get(5));
						} else if (selectItem.equals(getMonthNames().get(6))) {
							createStartDate(getMonthNames().get(6));
						} else if (selectItem.equals(getMonthNames().get(7))) {
							createStartDate(getMonthNames().get(7));
						} else if (selectItem.equals(getMonthNames().get(8))) {
							createStartDate(getMonthNames().get(8));
						} else if (selectItem.equals(getMonthNames().get(9))) {
							createStartDate(getMonthNames().get(9));
						} else if (selectItem.equals(getMonthNames().get(10))) {
							createStartDate(getMonthNames().get(10));
						} else if (selectItem.equals(getMonthNames().get(11))) {
							createStartDate(getMonthNames().get(11));
						} else if (selectItem.equals(getMonthNames().get(12))) {
							createStartDate(getMonthNames().get(12));
						}
						changeDates(reportStartDate, reportEndDate);
					}
				});

		accountCombo = new SelectCombo(messages.showAccounts());
		accountCombo.initCombo(getAccountsList());
		accountCombo.setSelectedItem(0);
		accountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (selectItem.equals(getAccountsList().get(0))) {
							reportType = 0;
						} else {
							reportType = 1;
						}

						changeDates(reportStartDate, reportEndDate);
					}
				});

		quaterlySelectCombo = new SelectCombo(messages.quarterly());
		quaterlySelectCombo.initCombo(getQuatersList());
		quaterlySelectCombo.setSelectedItem(0);
		quaterlySelectCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (selectItem.equals(getQuatersList().get(0))) {
							createStartDateForQuater(1);
						} else if (selectItem.equals(getQuatersList().get(1))) {
							createStartDateForQuater(2);
						} else if (selectItem.equals(getQuatersList().get(2))) {
							createStartDateForQuater(3);
						} else if (selectItem.equals(getQuatersList().get(3))) {
							createStartDateForQuater(4);
						}
						changeDates(reportStartDate, reportEndDate);
					}
				});

		addItems(budgetName, accountCombo);
		// addItems(budgetName, accountCombo, dateRangeSelect, monthSelectCombo,
		// quaterlySelectCombo);

		monthSelectCombo.hide();
		quaterlySelectCombo.hide();
		// accountCombo.hide();
		// dateRangeSelect.hide();

	}

	protected void createDates() {

		int selectedIndex = budgetName.getSelectedIndex();
		String string = financialMonthArray.get(selectedIndex);

		int mnth = 0;

		if (string.equals(DayAndMonthUtil.jan())) {
			mnth = 1;
		} else if (string.equals(DayAndMonthUtil.feb())) {
			mnth = 2;
		} else if (string.equals(DayAndMonthUtil.mar())) {
			mnth = 3;
		} else if (string.equals(DayAndMonthUtil.apr())) {
			mnth = 4;
		} else if (string.equals(DayAndMonthUtil.mayS())) {
			mnth = 5;
		} else if (string.equals(DayAndMonthUtil.jun())) {
			mnth = 6;
		} else if (string.equals(DayAndMonthUtil.jul())) {
			mnth = 7;
		} else if (string.equals(DayAndMonthUtil.aug())) {
			mnth = 8;
		} else if (string.equals(DayAndMonthUtil.sep())) {
			mnth = 9;
		} else if (string.equals(DayAndMonthUtil.oct())) {
			mnth = 10;
		} else if (string.equals(DayAndMonthUtil.nov())) {
			mnth = 11;
		} else if (string.equals(DayAndMonthUtil.dec())) {
			mnth = 12;
		}

		ClientFinanceDate strtDate = new ClientFinanceDate(
				startYearArray.get(selectedIndex), mnth, 1);
		reportStartDate = strtDate;

		ClientFinanceDate endDate = new ClientFinanceDate(
				endYearArray.get(selectedIndex) - 1, mnth + 11, 30);
		reportEndDate = endDate;

	}

	protected void createStartDateForQuater(int i) {

		int selectedIndex = budgetName.getSelectedIndex();
		String string = financialMonthArray.get(selectedIndex);

		int strtMn = 0, endMn = 0;
		if (i == 1) {
			strtMn = 4;
			endMn = 6;
			ClientFinanceDate strtDate = new ClientFinanceDate(
					startYearArray.get(selectedIndex), strtMn, 1);
			reportStartDate = strtDate;
			ClientFinanceDate endDate = new ClientFinanceDate(
					startYearArray.get(selectedIndex), endMn, 30);
			reportEndDate = endDate;
		} else if (i == 2) {
			strtMn = 7;
			endMn = 9;
			ClientFinanceDate strtDate = new ClientFinanceDate(
					startYearArray.get(selectedIndex), strtMn, 1);
			reportStartDate = strtDate;
			ClientFinanceDate endDate = new ClientFinanceDate(
					startYearArray.get(selectedIndex), endMn, 30);
			reportEndDate = endDate;
		} else if (i == 3) {
			strtMn = 10;
			endMn = 12;
			ClientFinanceDate strtDate = new ClientFinanceDate(
					startYearArray.get(selectedIndex), strtMn, 1);
			reportStartDate = strtDate;
			ClientFinanceDate endDate = new ClientFinanceDate(
					startYearArray.get(selectedIndex), endMn, 30);
			reportEndDate = endDate;
		} else if (i == 4) {
			strtMn = 1;
			endMn = 3;
			ClientFinanceDate strtDate = new ClientFinanceDate(
					endYearArray.get(selectedIndex), strtMn, 1);
			reportStartDate = strtDate;
			ClientFinanceDate endDate = new ClientFinanceDate(
					endYearArray.get(selectedIndex), endMn, 30);
			reportEndDate = endDate;
		}

	}

	protected void createStartDate(String monthName) {

		int selectedIndex = budgetName.getSelectedIndex();
		String string = monthName;

		int mnth = 0;

		if (string.equals(DayAndMonthUtil.january())) {
			mnth = 1;
		} else if (string.equals(DayAndMonthUtil.february())) {
			mnth = 2;
		} else if (string.equals(DayAndMonthUtil.march())) {
			mnth = 3;
		} else if (string.equals(DayAndMonthUtil.april())) {
			mnth = 4;
		} else if (string.equals(DayAndMonthUtil.may_full())) {
			mnth = 5;
		} else if (string.equals(DayAndMonthUtil.june())) {
			mnth = 6;
		} else if (string.equals(DayAndMonthUtil.july())) {
			mnth = 7;
		} else if (string.equals(DayAndMonthUtil.august())) {
			mnth = 8;
		} else if (string.equals(DayAndMonthUtil.september())) {
			mnth = 9;
		} else if (string.equals(DayAndMonthUtil.october())) {
			mnth = 10;
		} else if (string.equals(DayAndMonthUtil.november())) {
			mnth = 11;
		} else if (string.equals(DayAndMonthUtil.december())) {
			mnth = 12;
		}

		ClientFinanceDate strtDate = new ClientFinanceDate(
				startYearArray.get(selectedIndex), mnth, 1);
		reportStartDate = strtDate;

		ClientFinanceDate endDate = new ClientFinanceDate(
				startYearArray.get(selectedIndex), mnth, 30);
		reportEndDate = endDate;

	}

	private List<String> getDateRangeList() {
		ArrayList<String> datesList = new ArrayList<String>();

		datesList.add(messages.accountsVsMonths());
		datesList.add(messages.accountsVsQuaters());
		datesList.add(messages.accountsVsTotal());

		return datesList;
	}

	private List<String> getQuatersList() {

		ArrayList<String> datesList = new ArrayList<String>();

		datesList.add("Q1" + " " + DayAndMonthUtil.apr() + " - "
				+ DayAndMonthUtil.jun());
		datesList.add("Q2" + " " + DayAndMonthUtil.jul() + " - "
				+ DayAndMonthUtil.sep());
		datesList.add("Q3" + " " + DayAndMonthUtil.oct() + " - "
				+ DayAndMonthUtil.dec());
		datesList.add("Q4" + " " + DayAndMonthUtil.jan() + " - "
				+ DayAndMonthUtil.mar());

		return datesList;
	}

	private List<String> getMonthNames() {
		ArrayList<String> datesList = new ArrayList<String>();
		datesList.add(DayAndMonthUtil.january());
		datesList.add(DayAndMonthUtil.february());
		datesList.add(DayAndMonthUtil.march());
		datesList.add(DayAndMonthUtil.april());
		datesList.add(DayAndMonthUtil.may_full());
		datesList.add(DayAndMonthUtil.june());
		datesList.add(DayAndMonthUtil.july());
		datesList.add(DayAndMonthUtil.august());
		datesList.add(DayAndMonthUtil.september());
		datesList.add(DayAndMonthUtil.october());
		datesList.add(DayAndMonthUtil.november());
		datesList.add(DayAndMonthUtil.december());
		return datesList;
	}

	private List<String> getAccountsList() {
		ArrayList<String> list = new ArrayList<String>();

		list.add(messages.allAccounts());
		list.add(messages.onlyBudgetAccounts());

		return list;
	}

	public void createData() {

		Accounter.createHomeService().getBudgetList(
				new AsyncCallback<PaginationList<ClientBudget>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(
							PaginationList<ClientBudget> budgetList) {
						if (budgetList == null) {
							budgetList = new PaginationList<ClientBudget>();
						}
						for (ClientBudget budget : budgetList) {
							budgetArray.add(budget.getBudgetName());

							String finyear = budget.getFinancialYear();
							String split = finyear.substring(7);
							String split2 = (String) split.subSequence(0,
									split.length() - 1);
							String[] split3 = split2.split(" - ");

							String string = split3[0];
							String startYear = string.substring(3);
							startYearArray.add(Integer.parseInt(startYear));

							financialMonthArray.add((String) string
									.subSequence(0, 3));

							string = split3[1];
							String endYear = string.substring(3);
							endYearArray.add(Integer.parseInt(endYear));

							idArray.add(budget.getID());
						}
						createControls();

						if (idArray.size() > 0) {
							budgetId = idArray.get(0);
						} else {
							budgetId = 999L;
						}
						changeDates(new ClientFinanceDate(),
								new ClientFinanceDate());

					}
				});

	}
}
