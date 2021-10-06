/**
 * 
 */
package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXReturn;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXAgencyCombo;
import com.vimukti.accounter.web.client.ui.widgets.DateUtills;

/**
 * @author Murali.A
 * 
 */
public class PriorVATReturnToolBar extends ReportToolbar {
	private TAXAgencyCombo vatAgencyCombo;
	private SelectCombo endingDateCombo;
	private ClientTAXAgency selectedVATAgency = null;
	private ClientFinanceDate selectedEndDate;
	private SelectCombo dateRangeCombo;
	private List<String> dateRangeList;

	public PriorVATReturnToolBar() {
		createControls();
	}

	public void createControls() {

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
		// vatAgencyCombo.setWidth(40);
		vatAgencyCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXAgency>() {

					@Override
					public void selectedComboBoxItem(ClientTAXAgency selectItem) {
						if (selectItem != null) {
							// isToolBarComponentChanged = true;
							selectedVATAgency = selectItem;
							fillEndingDatesCombo(selectItem);
						}

					}
				});

		dateRangeCombo = new SelectCombo(messages.dateRange());
		dateRangeList = new ArrayList<String>();
		for (int i = 0; i < dateRangeArray.length; i++) {
			dateRangeList.add(dateRangeArray[i]);
		}
		dateRangeCombo.initCombo(dateRangeList);
		dateRangeCombo.setComboItem(messages.financialYearToDate());
		dateRangeCombo.setTitle(messages.dateRange());
		dateRangeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (!dateRangeCombo.getSelectedValue().equals(
								messages.custom())) {
							dateRangeChanged(dateRangeCombo.getSelectedValue());
						}
					}
				});

		endingDateCombo = new SelectCombo(messages.chooseEndingDate());
		endingDateCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						reportRequest();
						// selectedEndDate =
						// endingDateCombo.getValue().toString();
						// if (selectedEndDate == null)
						// return;
						//
						// if (selectedEndDate.length() == 0)
						// return;

						// String endDate = UIUtils
						// .dateToString(new Date(selectedEndDate));
						// reportview.makeReportRequest(selectedVATAgency.getID(),
						// selectedEndDate);
						// FinanceApplication.createReportService()
						// .getPriorVATReturnReport("", endDate, reportview);

						// changeDates(new Date(), new Date(selectedEndDate));

					}
				});
		List<ClientTAXAgency> vatAgencies = getCompany().getActiveTAXAgencies();
		for (ClientTAXAgency vatAgency : vatAgencies) {
			if (vatAgency.getName().equals(messages.hmRevenueCustomsVAT()))
				vatAgencyCombo.addItemThenfireEvent(vatAgency);
		}

		// if (UIUtils.isMSIEBrowser()) {
		// dateRangeCombo.setWidth("200px");
		// vatAgencyCombo.setWidth("200px");
		// endingDateCombo.setWidth("150px");
		// }

	}

	private ClientCompany getCompany() {
		ClientCompany company = Accounter.getCompany();
		return company;
	}

	public void setDateRangeHide(boolean hide) {
		if (hide) {
			addItems(vatAgencyCombo, endingDateCombo);
		} else {
			addItems(dateRangeCombo, vatAgencyCombo, endingDateCombo);
		}
	}

	public void fillEndingDatesCombo(ClientTAXAgency selectItem) {
		List<ClientTAXReturn> vatReturns = getCompany().getTAXReturns();
		List<String> endDates = new ArrayList<String>();
		// endDates.add("");
		for (ClientTAXReturn vatReturn : vatReturns) {
			if (vatReturn.getTAXAgency() == selectItem.getID()) {

				endDates.add(DateUtills.getDateAsString(vatReturn
						.getPeriodEndDate()));
			}
		}
		endingDateCombo.initCombo(endDates);
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.reports.ReportToolbar#changeDates
	 * (java.util.Date, java.util.Date)
	 */
	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		itemSelectionHandler.onItemSelectionChanged(0, startDate, endDate);
		reportRequest();
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
			setStartDate(startDate);
			setEndDate(endDate);
		}
	}

	public void reportRequest() {
		String selectedValue = endingDateCombo.getSelectedValue();
		if (selectedValue != null && !selectedValue.isEmpty())
			setSelectedEndDate(DateUtills.getDateFromString(endingDateCombo
					.getSelectedValue()));
		if (getSelectedEndDate() == null) {
			getView().showRecords();
			return;
		}
		setStartDate(getSelectedEndDate());
		setEndDate(getSelectedEndDate());
		// if (selectedEndDate.length() == 0)
		// return;
		reportview.makeReportRequest(selectedVATAgency.getID(),
				getSelectedEndDate());
	}

	public ClientFinanceDate getSelectedEndDate() {
		return selectedEndDate;
	}

	public void setSelectedEndDate(ClientFinanceDate selectedEndDate) {
		this.selectedEndDate = selectedEndDate;
	}
}
