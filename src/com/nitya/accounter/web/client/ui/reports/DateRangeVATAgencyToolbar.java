package com.nitya.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientTAXAgency;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.combo.TAXAgencyCombo;
import com.nitya.accounter.web.client.ui.forms.DateItem;

public class DateRangeVATAgencyToolbar extends ReportToolbar {

	private DateItem fromItem;
	private DateItem toItem;

	private SelectCombo reportBasisItem, dateRangeItem;
	private TAXAgencyCombo vatAgencyCombo;
	protected String selectedEndDate;
	protected String selectedStartDate;
	private Button updateButton;
	private List<String> dateRangeList;

	public DateRangeVATAgencyToolbar() {
		createControls();
		// selectedDateRange = FinanceApplication.constants().all();
	}

	private void createControls() {

		String[] dateRangeArray = { messages.all(), messages.thisWeek(),
				messages.thisMonth(), messages.lastWeek(),
				messages.lastMonth(), messages.thisFinancialYear(),
				messages.lastFinancialYear(), messages.thisFinancialQuarter(),
				messages.lastFinancialQuarter(),
				messages.financialYearToDate(),
				// FinanceApplication.constants().today(),
				// FinanceApplication.constants().endThisWeek(),
				// FinanceApplication.constants().endThisWeekToDate(),
				// FinanceApplication.constants().endThisMonth(),
				// FinanceApplication.constants().endThisMonthToDate(),
				// FinanceApplication.constants().endThisFiscalQuarter(),
				// FinanceApplication.constants()
				// .endThisFiscalQuarterToDate(),
				// FinanceApplication.constants()
				// .endThisCalanderQuarter(),
				// FinanceApplication.constants()
				// .endThisCalanderQuarterToDate(),
				// FinanceApplication.constants().endThisFiscalYear(),
				// FinanceApplication.constants()
				// .endThisFiscalYearToDate(),
				// FinanceApplication.constants().endThisCalanderYear(),
				// FinanceApplication.constants()
				// .endThisCalanderYearToDate(),
				// FinanceApplication.constants().endYesterday(),
				// FinanceApplication.constants()
				// .endPreviousFiscalQuarter(),
				// FinanceApplication.constants()
				// .endLastCalenderQuarter(),
				// FinanceApplication.constants()
				// .previousFiscalYearSameDates(),
				// FinanceApplication.constants().lastCalenderYear(),
				// FinanceApplication.constants().previousCalenderYear(),
				messages.custom() };

		vatAgencyCombo = new TAXAgencyCombo(messages.chooseVATAgency(), false);
		vatAgencyCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXAgency>() {

					@Override
					public void selectedComboBoxItem(ClientTAXAgency selectItem) {
						if (selectItem != null) {

							ClientFinanceDate startDate = fromItem.getDate();
							ClientFinanceDate endDate = toItem.getDate();

							reportview.makeReportRequest(selectItem.getID(),
									startDate, endDate);
						}

					}
				});
		List<ClientTAXAgency> vatAgencies = vatAgencyCombo.getComboItems();
		for (ClientTAXAgency vatAgency : vatAgencies) {
			if (vatAgency.getName().equals(messages.hmRevenueCustomsVAT())) {
				vatAgencyCombo.setComboItem(vatAgency);
				break;
			}
		}

		dateRangeItem = new SelectCombo(messages.dateRange());
		dateRangeItem.setValueMap(dateRangeArray);
		dateRangeItem.setDefaultValue(dateRangeArray[0]);
		dateRangeList = new ArrayList<String>();
		for (int i = 0; i < dateRangeArray.length; i++) {
			dateRangeList.add(dateRangeArray[i]);
		}
		dateRangeItem.setComboItem(messages.financialYearToDate());
		dateRangeItem
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (!dateRangeItem.getSelectedValue().equals(
								messages.custom())) {
							dateRangeChanged(dateRangeItem.getSelectedValue());
							// fromItem.setDisabled(true);
							// toItem.setDisabled(true);
							// updateButton.setEnabled(false);
							// } else {
							// fromItem.setDisabled(false);
							// toItem.setDisabled(false);
							// updateButton.setEnabled(true);
							// }
						}
					}
				});

		fromItem = new DateItem(messages.from(), "fromItem");
		toItem = new DateItem(messages.to(), "toItem");

		toItem.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {

				ClientFinanceDate startDate = fromItem.getDate();
				ClientFinanceDate endDate = toItem.getDate();
				ClientTAXAgency vatAgency = vatAgencyCombo.getSelectedValue();
				if (vatAgency != null)
					reportview.makeReportRequest(vatAgency.getID(), startDate,
							endDate);

				// itemSelectionHandler.onItemSelectionChanged(TYPE_ACCRUAL,
				// fromItem.getDate(), toItem.getDate());

			}
		});

		updateButton = new Button(messages.update());
		updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				setStartDate(fromItem.getDate());
				setEndDate(toItem.getDate());

				ClientFinanceDate start = fromItem.getDate();
				ClientFinanceDate end = toItem.getDate();
				ClientTAXAgency vatAgency = vatAgencyCombo.getSelectedValue();
				if (vatAgency != null)
					reportview.makeReportRequest(vatAgency.getID(), start, end);

				// This will update the dates in the date range layout
				itemSelectionHandler.onItemSelectionChanged(TYPE_ACCRUAL,
						startDate, endDate);
				dateRangeItem.setDefaultValue(messages.custom());
				dateRangeItem.setComboItem(messages.custom());
				setSelectedDateRange(messages.custom());
			}
		});

		// fromItem.setDisabled(true);
		// toItem.setDisabled(true);
		// updateButton.setEnabled(false);

		// set the Date Range to End this Calendar quarter to date
		// setDefaultDateRange(dateRangeArray);

		Button printButton = new Button(messages.print());
		printButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

			}

		});

		// if (UIUtils.isMSIEBrowser()) {
		// dateRangeItem.setWidth("200px");
		// vatAgencyCombo.setWidth("200px");
		// }

		addItems(vatAgencyCombo, dateRangeItem, fromItem, toItem);
		add(updateButton);
	}

	// set the Default Date Range to End this Calendar quarter to date

	private void setDefaultDateRange(String[] dateRangeArray) {

		dateRangeItem.setDefaultValue(dateRangeArray[9]);
		ClientFinanceDate date = new ClientFinanceDate();
		int month = (date.getMonth()) % 3;
		int startMonth = date.getMonth() - month;
		ClientFinanceDate startDate = new ClientFinanceDate(date.getYear(),
				startMonth, 1);
		fromItem.setDatethanFireEvent(startDate);
		toItem.setDatethanFireEvent(date);
		setStartDate(startDate);
		setEndDate(date);
		// dateRangeChanged(dateRangeArray[9]);

	}

	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {

		fromItem.setValue(startDate);
		toItem.setValue(endDate);

		ClientFinanceDate start = startDate;
		ClientFinanceDate end = endDate;
		ClientTAXAgency vatAgency = vatAgencyCombo.getSelectedValue();
		if (vatAgency != null)
			reportview.makeReportRequest(vatAgency.getID(), start, end);

		itemSelectionHandler.onItemSelectionChanged(TYPE_ACCRUAL, startDate,
				endDate);

	}

	@Override
	public void setDateRanageOptions(String... dateRanages) {
		List<String> dateRangesList = new ArrayList<String>();
		for (int i = 0; i < dateRanages.length; i++) {
			dateRangesList.add(dateRanages[i]);
		}

		dateRangeItem.initCombo(dateRangesList);
	}

	@Override
	public void setDefaultDateRange(String defaultDateRange) {
		dateRangeItem.setDefaultValue(defaultDateRange);
		dateRangeItem.setComboItem(defaultDateRange);
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
