package com.nitya.accounter.web.client.ui.reports;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.forms.ComboBoxItem;
import com.nitya.accounter.web.client.ui.forms.DateItem;

public class CheckDetailReportToolbar extends ReportToolbar {
	private DateItem fromItem;
	private DateItem toItem;
	protected ComboBoxItem checkDetailCombo;
	private ComboBoxItem dateRangeItem;
	private Button updateButton;

	public CheckDetailReportToolbar() {
		createControls();
		// selectedDateRange = FinanceApplication.constants().all();
	}

	private void createControls() {
		String[] statusArray = { messages.cash(),
				messages.check(),
				messages.creditCard(),
				messages.directDebit(),
				messages.masterCard(),
				messages.onlineBanking(),
				messages.standingOrder(),
				messages.switchMaestro() };

		String[] dateRangeArray = { messages.all(),
				messages.thisWeek(),
				messages.thisMonth(),
				messages.lastWeek(),
				messages.lastMonth(),
				messages.thisFinancialYear(),
				messages.lastFinancialYear(),
				messages.thisFinancialQuarter(),
				messages.lastFinancialQuarter(),
				messages.financialYearToDate(), messages.custom() };

		checkDetailCombo = new ComboBoxItem(messages.paymentMethod(),"checkDetailCombo");
		checkDetailCombo.setValueMap(statusArray);
		checkDetailCombo.setDefaultValue(statusArray[0]);
		checkDetailCombo.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				ClientFinanceDate startDate = fromItem.getDate();
				ClientFinanceDate endDate = toItem.getDate();
				reportview.makeReportRequest(
						Long.parseLong(checkDetailCombo.getValue()), startDate,
						endDate);

			}
		});

		dateRangeItem = new ComboBoxItem(messages.dateRange(),"dateRangeItem");
		dateRangeItem.setValueMap(dateRangeArray);
		dateRangeItem.setDefaultValue(dateRangeArray[0]);

		dateRangeItem.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {

				dateRangeChanged(dateRangeItem.getValue().toString());

			}
		});

		fromItem = new DateItem(messages.from(),"fromItem");
		fromItem.setDatethanFireEvent(Accounter.getStartDate());

		toItem = new DateItem(messages.to(),"toItem");
		ClientFinanceDate date = Accounter.getCompany()
				.getCurrentFiscalYearEndDate();
		// .getLastandOpenedFiscalYearEndDate();

		if (date != null)
			toItem.setDatethanFireEvent(date);
		else
			toItem.setDatethanFireEvent(new ClientFinanceDate());

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
				dateRangeItem.setDefaultValue(messages.custom());
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

//		if (UIUtils.isMSIEBrowser()) {
//			dateRangeItem.setWidth("170px");
//			checkDetailCombo.setWidth("90px");
//		}
		addItems(checkDetailCombo, dateRangeItem, fromItem, toItem);
		add(updateButton);
	}

	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		fromItem.setValue(startDate);
		toItem.setValue(endDate);
		reportview
				.makeReportRequest(Long.parseLong(checkDetailCombo.getValue()),
						startDate, endDate);

		// itemSelectionHandler.onItemSelectionChanged(TYPE_ACCRUAL, startDate,
		// endDate);
	}

	@Override
	public void setDateRanageOptions(String... dateRanages) {
		dateRangeItem.setValueMap(dateRanages);
	}

	@Override
	public void setDefaultDateRange(String defaultDateRange) {
		dateRangeItem.setDefaultValue(defaultDateRange);
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
}
