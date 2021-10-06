package com.vimukti.accounter.web.client.ui.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public abstract class TransactionsListView<T> extends BaseListView<T> {
	protected ClientFinanceDate startDate;
	protected ClientFinanceDate endDate;
	public static final int VIEW_ALL = 0;
	public static final int VIEW_OPEN = 1;
	public static final int VIEW_OVERDUE = 2;
	public static final int VIEW_VOIDED = 3;
	public static final int VIEW_DRAFT = 4;
	public static final int VIEW_PAID = 5;
	public static final int TYPE_ALL = 1000;

	public DateItem fromItem;
	public DateItem toItem;
	public Button updateButton;
	protected SelectCombo viewSelect, dateRangeSelector;

	private String viewType;

	public TransactionsListView() {
		super();
		this.getElement().setId("TransactionsListView");
		this.startDate = Accounter.getStartDate();
		this.endDate = getCompany().getCurrentFiscalYearEndDate();
	}

	public TransactionsListView(String viewType) {
		super();
		this.getElement().setId("TransactionsListView");
		this.startDate = Accounter.getStartDate();
		this.endDate = getCompany().getCurrentFiscalYearEndDate();

		this.setViewType(viewType);
		this.setDateRange(messages.all());
	}

	@Override
	public void initData() {
		if (status == STATUS_NEW) {
			initListCallback();
		}
	}

	@Override
	protected void createListForm(DynamicForm form) {
		super.createListForm(form);
		dateRangeSelector = getDateRangeSelectItem();

		if (dateRangeSelector == null) {
			dateRangeSelector = new SelectCombo(messages.date());
			// dateRangeSelector.setWidth("150px");
			List<String> typeList = new ArrayList<String>();
			typeList.add(messages.active());
			typeList.add(messages.inActive());
			dateRangeSelector.initCombo(typeList);
			dateRangeSelector.addChangeHandler(new ChangeHandler() {

				@Override
				public void onChange(ChangeEvent event) {
					if (dateRangeSelector.getSelectedValue() != null) {

					}

				}
			});

		}

		fromItem = new DateItem(messages.from(), "fromItem");
		if (Accounter.getStartDate() != null) {
			fromItem.setDatethanFireEvent(Accounter.getStartDate());
		} else {
			fromItem.setDatethanFireEvent(new ClientFinanceDate());
		}
		toItem = new DateItem(messages.to(), "toItem");
		toItem.setDatethanFireEvent(Accounter.getCompany()
				.getCurrentFiscalYearEndDate());
		// .getLastandOpenedFiscalYearEndDate());
		fromItem.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				dateRangeSelector.addComboItem(messages.custom());
				dateRangeSelector.setComboItem(messages.custom());
				updateButton.setEnabled(true);
				setDateRange(messages.custom());
				setStartDate(fromItem.getValue());
			}
		});
		toItem.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				dateRangeSelector.addComboItem(messages.custom());
				dateRangeSelector.setComboItem(messages.custom());
				updateButton.setEnabled(true);
				setDateRange(messages.custom());
				setEndDate(toItem.getValue());
			}
		});
		updateButton = new Button(messages.update());
		updateButton.setEnabled(false);
		updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (dateRangeSelector.getSelectedValue().equals(
						messages.custom())) {
					customManage();
				}
			}
		});
		form.add(dateRangeSelector, fromItem, toItem);
		form.add(updateButton);
	}

	@Override
	protected boolean isTransactionListView() {
		return true;
	}

	@Override
	protected SelectCombo getSelectItem() {
		viewSelect = new SelectCombo(messages.currentView());
		// listOfTypes.add(DRAFT);
		List<String> viewSelectTypes = getViewSelectTypes();
		if (!Accounter.hasPermission(Features.DRAFTS)) {
			if (viewSelectTypes != null && !viewSelectTypes.isEmpty()) {
				viewSelectTypes.remove(messages.drafts());
			}
		}
		viewSelect.initCombo(viewSelectTypes);

		// if (UIUtils.isMSIEBrowser())
		// viewSelect.setWidth("105px");

		viewSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (viewSelect.getSelectedValue() != null) {
							grid.setViewType(viewSelect.getSelectedValue());
							setViewType(selectItem);
							filterList(viewSelect.getSelectedValue());
						}
					}
				});
		viewSelect.addStyleName("invoiceListCombo");
		viewSelect.setComboItem(getViewType());
		return viewSelect;
	}

	@Override
	protected SelectCombo getDateRangeSelectItem() {
		dateRangeSelector = new SelectCombo(messages.date());
		String[] dateRangeArray = { messages.all(), messages.thisWeek(),
				messages.thisMonth(), messages.lastWeek(),
				messages.lastMonth(), messages.thisFinancialYear(),
				messages.lastFinancialYear(), messages.thisFinancialQuarter(),
				messages.lastFinancialQuarter(), messages.financialYearToDate() };
		final List<String> dateRanges = new ArrayList<String>();
		for (int i = 0; i < dateRangeArray.length; i++) {
			dateRanges.add(dateRangeArray[i]);
		}
		dateRangeSelector.initCombo(dateRanges);

		dateRangeSelector.setComboItem(messages.all());
		setDateRange(messages.all());
		// if (UIUtils.isMSIEBrowser())
		// dateRangeSelector.setWidth("105px");

		dateRangeSelector
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {
					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (selectItem != null
								&& !selectItem.equals(messages.custom())) {
							dateRangeChanged(dateRangeSelector
									.getSelectedValue());
							setDateRange(dateRangeSelector.getSelectedValue());
							grid.setViewType(dateRangeSelector
									.getSelectedValue());
							dateRangeSelector.removeComboItem(messages.custom());
							updateButton.setEnabled(false);
						}
					}
				});
		dateRangeSelector.addStyleName("invoiceListCombo");

		return dateRangeSelector;
	}

	public void dateRangeChanged(String dateRange) {
		ClientFinanceDate date = new ClientFinanceDate();
		/** If company don't have any transactions.. **/
		startDate = getCompany().getTransactionStartDate() == null ? date
				: getCompany().getTransactionStartDate();
		endDate = getCompany().getTransactionEndDate() == null ? date
				: getCompany().getTransactionEndDate();
		// getLastandOpenedFiscalYearEndDate();
		if (dateRange.equals(messages.thisWeek())) {
			startDate = getWeekStartDate();
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(new ClientFinanceDate().getDateAsObject());
			endCal.add(Calendar.DAY_OF_MONTH, 2);
			endDate.setDay(startDate.getDay() + 6);
			endDate.setMonth(startDate.getMonth());
			endDate.setYear(startDate.getYear());
		}
		if (dateRange.equals(messages.thisMonth())) {
			startDate = new ClientFinanceDate(date.getYear(), date.getMonth(),
					1);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(new ClientFinanceDate().getDateAsObject());
			endCal.set(Calendar.DAY_OF_MONTH,
					endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
			endDate = new ClientFinanceDate(endCal.getTime());
		}
		if (dateRange.equals(messages.lastWeek())) {
			endDate = getWeekStartDate();
			endDate.setDay(endDate.getDay() - 1);
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(endDate.getDateAsObject());
			startCal.set(Calendar.DAY_OF_MONTH,
					startCal.get(Calendar.DAY_OF_MONTH) - 6);
			startDate = new ClientFinanceDate(startCal.getTime());

		}
		if (dateRange.equals(messages.lastMonth())) {
			int day;
			if (date.getMonth() == 0) {
				day = getMonthLastDate(11, date.getYear() - 1);
				startDate = new ClientFinanceDate(date.getYear() - 1, 11, 1);
				endDate = new ClientFinanceDate(date.getYear() - 1, 11, day);
			} else {
				Date dateAsObject = date.getDateAsObject();
				int month = dateAsObject.getMonth();
				day = getMonthLastDate(month - 1, date.getYear());
				startDate = new ClientFinanceDate(date.getYear(),
						date.getMonth() - 1, 1);
				endDate = new ClientFinanceDate(date.getYear(),
						date.getMonth() - 1, day);
			}
		}
		if (dateRange.equals(messages.thisFinancialYear())) {
			startDate = getCompany().getCurrentFiscalYearStartDate();
			endDate = getCompany().getCurrentFiscalYearEndDate();
		}
		if (dateRange.equals(messages.lastFinancialYear())) {
			startDate = Accounter.getCompany().getCurrentFiscalYearStartDate();
			startDate.setYear(startDate.getYear() - 1);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(Accounter.getCompany().getCurrentFiscalYearEndDate()
					.getDateAsObject());
			endCal.set(Calendar.DAY_OF_MONTH,
					endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
			endDate = new ClientFinanceDate(endCal.getTime());
			endDate.setYear(endDate.getYear() - 1);
		}
		if (dateRange.equals(messages.thisFinancialQuarter())) {
			startDate = new ClientFinanceDate();
			endDate = getCompany().getCurrentFiscalYearEndDate();
			// getLastandOpenedFiscalYearEndDate();
			getCurrentFiscalYearQuarter();
		}
		if (dateRange.equals(messages.lastFinancialQuarter())) {
			// getLastandOpenedFiscalYearEndDate();
			getCurrentFiscalYearQuarter();
			Calendar startDateCal = Calendar.getInstance();
			startDateCal.setTime(startDate.getDateAsObject());
			startDateCal.set(Calendar.MONTH,
					startDateCal.get(Calendar.MONTH) - 3);
			Calendar endDateCal = Calendar.getInstance();
			endDateCal.setTime(endDate.getDateAsObject());
			endDateCal.set(Calendar.MONTH, endDateCal.get(Calendar.MONTH) - 3);
			startDate = new ClientFinanceDate(startDateCal.getTime());
			endDate = new ClientFinanceDate(endDateCal.getTime());
		}
		if (dateRange.equals(messages.financialYearToDate())) {
			startDate = getCompany().getCurrentFiscalYearStartDate();
			endDate = new ClientFinanceDate();
		}
		setStartDate(startDate);
		setEndDate(endDate);
		changeDates(startDate, endDate);
	}

	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		fromItem.setValue(startDate);
		toItem.setValue(endDate);
		initListCallback();
	}

	public ClientFinanceDate getStartDate() {
		return startDate == null ? ClientFinanceDate.emptyDate() : startDate;
	}

	public void setStartDate(ClientFinanceDate startDate) {
		this.startDate = startDate;
	}

	public ClientFinanceDate getEndDate() {
		return UIUtils.toDate(endDate);
	}

	public void setEndDate(ClientFinanceDate endDate) {
		this.endDate = endDate;
	}

	public ClientFinanceDate getWeekStartDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new ClientFinanceDate().getDateAsObject());
		int i = calendar.get(Calendar.DAY_OF_WEEK);
		calendar.add(Calendar.DAY_OF_MONTH, 1 - i);
		ClientFinanceDate financeDate = new ClientFinanceDate(
				calendar.getTime());
		return financeDate;
	}

	// public native double getWeekStartDate()/*-{
	// var date= new Date();
	// var day=date.getDay();
	// var newDate=new Date();
	// newDate.setDate(date.getDate()-day);
	// var tmp=newDate.getTime();
	// return tmp;
	// }-*/;

	private int getMonthLastDate(int month, int year) {
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
		return lastDay;
	}

	private void getCurrentFiscalYearQuarter() {

		ClientFinanceDate date = new ClientFinanceDate();
		ClientFinanceDate start = Accounter.getCompany()
				.getCurrentFiscalYearStartDate();
		ClientFinanceDate end = Accounter.getCompany()
				.getCurrentFiscalYearEndDate();

		int currentQuarter = 0;
		ClientFinanceDate quarterStart = Accounter.getCompany()
				.getCurrentFiscalYearStartDate();
		ClientFinanceDate quarterEnd;

		Calendar quarterEndCal = Calendar.getInstance();
		quarterEndCal.setTime(quarterStart.getDateAsObject());
		quarterEndCal
				.set(Calendar.MONTH, quarterEndCal.get(Calendar.MONTH) + 3);
		quarterEndCal.set(Calendar.DAY_OF_MONTH, quarterStart.getDay() - 1);

		quarterEnd = new ClientFinanceDate(quarterEndCal.getTime());

		if (!date.before(quarterStart) && !date.after(quarterEnd)) {
			currentQuarter = 1;
		}
		quarterStart.setMonth(quarterStart.getMonth() + 3);
		quarterEndCal.setTime(quarterStart.getDateAsObject());
		quarterEndCal
				.set(Calendar.MONTH, quarterEndCal.get(Calendar.MONTH) + 3);
		quarterEndCal.set(Calendar.DAY_OF_MONTH, quarterStart.getDay() - 1);

		quarterEnd = new ClientFinanceDate(quarterEndCal.getTime());
		if (!date.before(quarterStart) && !date.after(quarterEnd)) {
			currentQuarter = 2;
		}
		quarterStart.setMonth(quarterStart.getMonth() + 3);
		quarterEndCal.setTime(quarterStart.getDateAsObject());
		quarterEndCal
				.set(Calendar.MONTH, quarterEndCal.get(Calendar.MONTH) + 3);
		quarterEndCal.set(Calendar.DAY_OF_MONTH, quarterStart.getDay() - 1);

		quarterEnd = new ClientFinanceDate(quarterEndCal.getTime());
		if (!date.before(quarterStart) && !date.after(quarterEnd)) {
			currentQuarter = 3;
		}
		quarterStart.setMonth(quarterStart.getMonth() + 3);
		quarterEndCal.setTime(quarterStart.getDateAsObject());
		quarterEndCal
				.set(Calendar.MONTH, quarterEndCal.get(Calendar.MONTH) + 3);
		quarterEndCal.set(Calendar.DAY_OF_MONTH, quarterStart.getDay() - 1);

		quarterEnd = new ClientFinanceDate(quarterEndCal.getTime());
		if (!date.before(quarterStart) && !date.after(quarterEnd)) {
			currentQuarter = 4;
		}
		switch (currentQuarter) {
		case 1:
			startDate = start;
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(start.getDateAsObject());
			endCal.set(Calendar.MONTH, endCal.get(Calendar.MONTH) + 3);
			endCal.set(Calendar.DAY_OF_MONTH, startDate.getDay() - 1);
			endDate = new ClientFinanceDate(endCal.getTime());
			break;

		case 2:
			startDate = start;
			startDate.setMonth(start.getMonth() + 3);
			Calendar endCal2 = Calendar.getInstance();
			endCal2.setTime(startDate.getDateAsObject());
			endCal2.set(Calendar.MONTH, endCal2.get(Calendar.MONTH) + 3);
			endCal2.set(Calendar.DAY_OF_MONTH, startDate.getDay() - 1);
			endDate = new ClientFinanceDate(endCal2.getTime());
			break;

		case 3:
			startDate = start;
			startDate.setMonth(start.getMonth() + 6);
			Calendar endCal3 = Calendar.getInstance();
			endCal3.setTime(startDate.getDateAsObject());
			endCal3.set(Calendar.MONTH, endCal3.get(Calendar.MONTH) + 3);
			endCal3.set(Calendar.DAY_OF_MONTH, startDate.getDay() - 1);
			endDate = new ClientFinanceDate(endCal3.getTime());
			break;
		default:
			startDate = start;
			startDate.setMonth(start.getMonth() + 9);
			endDate = end;
			break;
		}
	}

	protected void filterList(String selectedValue) {

	}

	@Override
	public HashMap<String, Object> saveView() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (viewSelect != null) {
			map.put("currentView", viewSelect.getValue().toString());
		}
		if (dateRangeSelector != null) {
			map.put("dateRange", dateRangeSelector.getValue().toString());
		}
		if (fromItem != null) {
			map.put("startDate", fromItem.getValue());
		}
		if (toItem != null) {
			map.put("endDate", toItem.getValue());
		}
		map.put("start", start);
		return map;
	}

	@Override
	public void restoreView(HashMap<String, Object> map) {
		if (map == null || map.isEmpty()) {
			return;
		}
		String currentView = null;
		if (viewSelect != null) {
			currentView = (String) map.get("currentView");
			viewSelect.setComboItem(currentView);
			this.setViewType(currentView);
		}
		String dateRange = (String) map.get("dateRange");
		if (dateRangeSelector != null) {
			dateRangeSelector.setComboItem(dateRange);
		}
		// dateRangeChanged(dateRange1);
		ClientFinanceDate startDate = (ClientFinanceDate) map.get("startDate");
		setStartDate(startDate);
		ClientFinanceDate endDate = (ClientFinanceDate) map.get("endDate");
		setEndDate(endDate);
		start = (Integer) map.get("start");
		restoreView(currentView, dateRange);
	}

	@Override
	public void customManage() {
		setStartDate(fromItem.getDate());
		setEndDate(toItem.getDate());
		changeDates(startDate, endDate);
	}

	public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
		if (viewSelect != null) {
			viewSelect.setComboItem(viewType);
		}
	}

	@Override
	public String getDateRange() {
		return super.getDateRange();
	}

	@Override
	public void setDateRange(String selectedValue) {
		super.setDateRange(selectedValue);
	}

	@Override
	public void restoreView(String currentView, String dateRange) {
		if (dateRange != null && dateRangeSelector != null) {
			dateRangeSelector.setComboItem(dateRange);
			setDateRange(dateRange);
		}
		if (fromItem != null && !getStartDate().isEmpty()) {
			fromItem.setEnteredDate(getStartDate());
		}
		if (toItem != null && !getEndDate().isEmpty()) {
			toItem.setEnteredDate(getEndDate());
		}
		filterList(currentView);
		if (grid != null) {
			showLoading();
		}
	}
}
