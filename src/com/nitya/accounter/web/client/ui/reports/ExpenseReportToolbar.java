package com.nitya.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.forms.DateItem;

public class ExpenseReportToolbar extends ReportToolbar {
	private DateItem fromItem;
	private DateItem toItem;
	protected SelectCombo expenseCombo, dateRangeCombo;
	protected List<String> statusList, dateRangeList;
	private Button updateButton;
	public static int EMPLOYEE = 1;
	public static int CASH = 2;
	public static int CREDITCARD = 3;
	private int expenseType;

	public ExpenseReportToolbar() {
		createControls();
		// selectedDateRange = FinanceApplication.constants().all();
	}

	private void createControls() {
		String[] statusArray;
		if (Global.get().preferences().isHaveEpmloyees()
				&& Global.get().preferences().isTrackEmployeeExpenses()) {
			statusArray = new String[] { messages.allExpenses(),
					messages.cash(), messages.creditCard(), messages.employee() };
		} else {
			statusArray = new String[] { messages.allExpenses(),
					messages.cash(), messages.creditCard() };
		}

		String[] dateRangeArray = { messages.all(), messages.thisWeek(),
				messages.thisMonth(), messages.lastWeek(),
				messages.lastMonth(), messages.thisFinancialYear(),
				messages.lastFinancialYear(), messages.thisFinancialQuarter(),
				messages.lastFinancialQuarter(),
				messages.financialYearToDate(), messages.custom() };

		expenseCombo = new SelectCombo(messages.expenseRealtedTo());
		statusList = new ArrayList<String>();
		for (int i = 0; i < statusArray.length; i++) {
			statusList.add(statusArray[i]);
		}
		expenseCombo.initCombo(statusList);
		expenseCombo.setComboItem(statusArray[0]);
		expenseCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (selectItem.toString()
								.equals(messages.allExpenses())) {
							/*
							 * status 0 used to get all expenses like Cash,
							 * Credit Card
							 */

							expenseType = 0;
						} else if (selectItem.toString()
								.equals(messages.cash())) {
							expenseType = ClientTransaction.TYPE_CASH_EXPENSE;
						} else if (selectItem.toString().equals(
								messages.creditCard())) {
							expenseType = ClientTransaction.TYPE_CREDIT_CARD_EXPENSE;
						} else if (selectItem.toString().equals(
								messages.employee())) {
							expenseType = ClientTransaction.TYPE_EMPLOYEE_EXPENSE;
						}

						ClientFinanceDate startDate = fromItem.getDate();
						ClientFinanceDate endDate = toItem.getDate();
						reportview.makeReportRequest(expenseType, startDate,
								endDate);

					}
				});

		dateRangeCombo = new SelectCombo(messages.dateRange());
		dateRangeList = new ArrayList<String>();
		for (int i = 0; i < dateRangeArray.length; i++) {
			dateRangeList.add(dateRangeArray[i]);
		}
		dateRangeCombo.initCombo(dateRangeList);
		dateRangeCombo.setDefaultValue(dateRangeArray[0]);
		dateRangeCombo.setComboItem(messages.financialYearToDate());
		dateRangeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

						dateRangeChanged(dateRangeCombo.getSelectedValue());

					}
				});

		fromItem = new DateItem(messages.from(), "fromItem");
		fromItem.setDatethanFireEvent(Accounter.getStartDate());
		toItem = new DateItem(messages.to(), "toItem");

		ClientFinanceDate date = Accounter.getCompany()
				.getCurrentFiscalYearEndDate();
		// .getLastandOpenedFiscalYearEndDate();

		if (date != null)
			toItem.setDatethanFireEvent(date);
		else
			toItem.setDatethanFireEvent(new ClientFinanceDate());

		toItem.setTitle(messages.to());
		toItem.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				startDate = (ClientFinanceDate) fromItem.getValue();
				endDate = (ClientFinanceDate) toItem.getValue();
			}
		});
		updateButton = new Button(messages.update());
		updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				setStartDate(fromItem.getDate());
				setEndDate(toItem.getDate());

				changeDates(fromItem.getDate(), toItem.getDate());
				dateRangeCombo.setDefaultValue(messages.custom());
				dateRangeCombo.setComboItem(messages.custom());
				setSelectedDateRange(messages.custom());

			}
		});

		// fromItem.setDisabled(true);
		// toItem.setDisabled(true);
		// updateButton.setEnabled(false);

		Button printButton = new Button(messages.print());
		// printButton.setTop(2);
		// printButton.setWidth(40);
		printButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

			}

		});

		// if (UIUtils.isMSIEBrowser()) {
		// dateRangeCombo.setWidth("170px");
		// expenseCombo.setWidth("90px");
		// }
		addItems(expenseCombo, dateRangeCombo, fromItem, toItem);
		add(updateButton);
	}

	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		fromItem.setValue(startDate);
		toItem.setValue(endDate);
		itemSelectionHandler.onItemSelectionChanged(TYPE_ACCRUAL, startDate,
				endDate);
		reportview.makeReportRequest(expenseType, startDate, endDate);
	}

	@Override
	public void setDateRanageOptions(String... dateRanages) {
		dateRangeCombo.setValueMap(dateRanages);
	}

	@Override
	public void setDefaultDateRange(String defaultDateRange) {
		dateRangeCombo.setDefaultValue(defaultDateRange);
		dateRangeCombo.setComboItem(defaultDateRange);
		dateRangeChanged(defaultDateRange);
	}

	@Override
	public void setStartAndEndDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		if (startDate != null && endDate != null) {
			fromItem.setEnteredDate(startDate);
			toItem.setEnteredDate(endDate);
			setStartDate(startDate);
			setEndDate(endDate);
		}
	}

	public void setSelectedType(int type) {
		switch (type) {
		case 0:
			expenseCombo.select(messages.allExpenses());
			break;
		case ClientTransaction.TYPE_CASH_EXPENSE:
			expenseCombo.select(messages.cash());
			break;
		case ClientTransaction.TYPE_CREDIT_CARD_EXPENSE:
			expenseCombo.select(messages.creditCard());
			break;
		case ClientTransaction.TYPE_EMPLOYEE_EXPENSE:
			expenseCombo.select(messages.employee());
		}
		this.expenseType = type;
	}
}
