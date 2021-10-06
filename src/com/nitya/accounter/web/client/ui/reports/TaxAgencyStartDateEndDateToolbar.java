package com.nitya.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientTAXAgency;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.combo.TAXAgencyCombo;
import com.nitya.accounter.web.client.ui.forms.DateItem;
import com.nitya.accounter.web.client.ui.widgets.DateValueChangeHandler;

public class TaxAgencyStartDateEndDateToolbar extends ReportToolbar {

	public DateItem fromItem;
	public DateItem toItem;

	public SelectCombo dateRangeItem;
	public TAXAgencyCombo taxAgencyCombo;
	ClientTAXAgency selectedAgency;
	public Button updateButton;
	private boolean isVATPriorReport;
	private List<String> dateRangeList;

	public TaxAgencyStartDateEndDateToolbar(boolean isVATPriorReport) {
		this.isVATPriorReport = isVATPriorReport;
		createControls();
	}

	private void createControls() {
		
		String[] dateRangeArray = { messages.all(), messages.thisWeek(),
				messages.thisMonth(), messages.lastWeek(),
				messages.lastMonth(), messages.thisFinancialYear(),
				messages.lastFinancialYear(), messages.thisFinancialQuarter(),
				messages.lastFinancialQuarter(),
				messages.financialYearToDate(),
				messages.custom() };

		String[] reportBasisArray = { messages.cash(), messages.accrual() };

		taxAgencyCombo = new TAXAgencyCombo(messages.selectTAXAgency(), false);
		taxAgencyCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXAgency>() {

					@Override
					public void selectedComboBoxItem(ClientTAXAgency selectItem) {
						if (selectItem != null) {
							selectedAgency = selectItem;
							reportRequest();
						}

					}
				});
		taxAgencyCombo.setSelectedItem(0);
		selectedAgency = taxAgencyCombo.getSelectedValue();
		
		
		dateRangeItem = new SelectCombo(messages.dateRange());
		dateRangeItem.setValueMap(dateRangeArray);
		dateRangeItem.setDefaultValue(dateRangeArray[0]);
		dateRangeList = new ArrayList<String>();
		for (int i = 0; i < dateRangeArray.length; i++) {
			dateRangeList.add(dateRangeArray[i]);
		}
		dateRangeItem.initCombo(dateRangeList);
		dateRangeItem.setComboItem(messages.financialYearToDate());
		dateRangeItem
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (!dateRangeItem.getSelectedValue().equals(
								messages.custom())) {
							dateRangeChanged(dateRangeItem.getSelectedValue());
							
						}
					}
				});
		
		
		
		fromItem = new DateItem(messages.from(), "fromItem");
		fromItem.setEnteredDate(new ClientFinanceDate());

		fromItem.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate date) {
				startDate = date;

				itemSelectionHandler.onItemSelectionChanged(TYPE_ACCRUAL,
						startDate, endDate);

			}
		});

		toItem = new DateItem(messages.to(), "toItem");
		toItem.setEnteredDate(new ClientFinanceDate());
		ClientFinanceDate date = Accounter.getCompany()
				.getCurrentFiscalYearEndDate();

		toItem.setTitle(messages.to());
		toItem.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate date) {
				endDate = date;

				itemSelectionHandler.onItemSelectionChanged(TYPE_ACCRUAL,
						startDate, endDate);

			}
		});
		updateButton = new Button(messages.update());
		updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				setStartDate(fromItem.getDate());
				setEndDate(toItem.getDate());

				itemSelectionHandler.onItemSelectionChanged(TYPE_ACCRUAL,
						fromItem.getDate(), toItem.getDate());

				reportRequest();

				setSelectedDateRange(messages.custom());
				
			}

		});

		Button printButton = new Button(messages.print());

		printButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

			}

		});

		addItems(taxAgencyCombo,dateRangeItem, fromItem, toItem);

		add(updateButton);

		List<ClientTAXAgency> vatAgencies = Accounter.getCompany()
				.getActiveTAXAgencies();
		for (ClientTAXAgency vatAgency : vatAgencies) {
			if (vatAgency.getName().equals(messages.hmRevenueCustomsVAT()))
				taxAgencyCombo.addItemThenfireEvent(vatAgency);
		}

	}

	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
		fromItem.setValue(startDate);
		toItem.setValue(endDate);
		itemSelectionHandler.onItemSelectionChanged(TYPE_ACCRUAL, startDate,
				endDate);
		if (selectedAgency != null && this.reportview != null) {
			this.reportview.makeReportRequest(this.selectedAgency.getID(),
					startDate, endDate);
		}
	}

	@Override
	public void setStartAndEndDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		fromItem.setValue(startDate);
		toItem.setValue(endDate);
	}

	@Override
	public void setDefaultDateRange(String defaultDateRange) {
		dateRangeItem.setDefaultValue(defaultDateRange);
		dateRangeItem.setComboItem(defaultDateRange);
		dateRangeChanged(defaultDateRange);
	}

	private void reportRequest() {
		if (this.reportview != null)
			this.reportview.makeReportRequest(this.selectedAgency.getID(),
					startDate, endDate);

	}
@Override
public void setDateRanageOptions(String... dateRanages) {
	List<String> dateRangesList = new ArrayList<String>();
	for (int i = 0; i < dateRanages.length; i++) {
		dateRangesList.add(dateRanages[i]);
	}

	dateRangeItem.initCombo(dateRangesList);
}
	public void setFromDate(ClientFinanceDate date) {
		fromItem.setDateWithNoEvent(date);
		startDate = date;
	}

	public void setToDate(ClientFinanceDate date) {
		toItem.setDateWithNoEvent(date);
		endDate = date;
	}

	@Override
	protected void payeeData() {

		if (getPayeeId() != 0) {
			ClientTAXAgency selectItem = Accounter.getCompany().getTaxAgency(
					getPayeeId());
			if (selectItem != null) {
				selectedAgency = selectItem;
				ClientFinanceDate startDate = fromItem.getDate();
				ClientFinanceDate endDate = toItem.getDate();
				reportview.makeReportRequest(selectedAgency.getID(), startDate,
						endDate);
				reportview.removeEmptyStyle();
				taxAgencyCombo.setSelected(selectedAgency.getName());
			}
		}
	}
}
