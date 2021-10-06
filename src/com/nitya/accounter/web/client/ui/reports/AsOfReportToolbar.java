package com.nitya.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.forms.DateItem;
import com.nitya.accounter.web.client.ui.forms.LabelItem;
import com.nitya.accounter.web.client.ui.widgets.DateUtills;

public class AsOfReportToolbar extends ReportToolbar {

	protected SelectCombo reportBasisCombo, dateRangeCombo;
	protected List<String> statusList, dateRangeList;
	private DateItem customDate;
	private Button updateButton;

	public AsOfReportToolbar() {
		createControls();
		// selectedDateRange = FinanceApplication.constants().all();
	}

	private void createControls() {

		String[] reportBasisArray = { messages.cash(), messages.accrual() };
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

		LabelItem report = new LabelItem(messages.reportBasisAccrual(),
				"report");

		// reportBasisItem = new ComboBoxItem();
		// reportBasisItem.setTitle(FinanceApplication.constants()
		// .reportBasis());
		// reportBasisItem.setValueMap(reportBasisArray);
		// reportBasisItem.setDefaultValue(reportBasisArray[1]);
		// // report basic is not yet implemented, so disable the feature.
		// reportBasisItem.setDisabled(true);

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

						// if (!dateRangeItem.getValue().toString().equals(
						// FinanceApplication.constants().custom())) {
						dateRangeChanged(dateRangeCombo.getSelectedValue());
						// customDate.setDisabled(true);
						// updateButton.setEnabled(false);
						// } else {
						// customDate.setDisabled(false);
						// updateButton.setEnabled(true);
						// }

					}
				});
		customDate = new DateItem("", "customDate");
		// customDate.setUseTextField(true);
		// customDate.setWidth(100);
		customDate.setShowTitle(false);
		// customDate.setDisabled(true);
		customDate.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				ClientFinanceDate date = (customDate.getValue());

				if (date != null) {
					if (!date.after(startDate))
						Accounter.showError(messages
								.pleaseSelectDateAfterCompanyStartDate()
								+ DateUtills.getDateAsString(startDate));
					else
						changeDates(startDate, date);
				} else {
					Accounter.showError(messages.pleaseSelectDate());
				}

			}

		});

		ClientFinanceDate date = Accounter.getCompany()
				.getCurrentFiscalYearEndDate();
		// .getLastandOpenedFiscalYearEndDate();
		if (date != null)
			customDate.setValue(date);
		else
			customDate.setValue(new ClientFinanceDate());

		updateButton = new Button(messages.update());
		// updateButton.setEnabled(false);
		updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				// setStartDate(fromItem.getDate());
				setEndDate(customDate.getDate());

				itemSelectionHandler.onItemSelectionChanged(TYPE_ACCRUAL,
						startDate, customDate.getDate());
				dateRangeCombo.setDefaultValue(messages.custom());
				dateRangeCombo.setComboItem(messages.custom());
				setSelectedDateRange(messages.custom());

			}
		});

		// if (UIUtils.isMSIEBrowser()) {
		// dateRangeCombo.setWidth("200px");
		// }
		addItems(report, dateRangeCombo, customDate);
		add(updateButton);
		// this.setCellVerticalAlignment(updateButton,
		// HasVerticalAlignment.ALIGN_MIDDLE);
	}

	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		customDate.setValue(endDate);
		itemSelectionHandler.onItemSelectionChanged(TYPE_ACCRUAL, startDate,
				customDate.getDate());
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
		if (endDate != null) {
			customDate.setEnteredDate(endDate);
			setStartDate(startDate);
			setEndDate(endDate);
		}

	}
}
