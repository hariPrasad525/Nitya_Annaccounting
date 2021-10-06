package com.nitya.accounter.web.client.ui.reports;

import java.util.HashMap;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientTAXAgency;
import com.nitya.accounter.web.client.core.ClientTAXItem;
import com.nitya.accounter.web.client.core.NumberReportInput;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.serverreports.TAXItemDetailServerReportView;

/**
 * 
 * @author Sai Prasad N
 * 
 */

public class TaxItemDetailReportView extends AbstractReportView<TAXItemDetail> {

	private long taxAgency;
	private long taxReturnId;
	private TaxAgencyStartDateEndDateToolbar toolBar;
	private boolean isFromReports;

	public TaxItemDetailReportView() {
		super(false, messages.noRecordsToShow());
		this.serverReport = new TAXItemDetailServerReportView(this);
		this.getElement().setId("taxitemdetailreportview");
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_TAXAGENCY;
	}

	@Override
	public void makeReportRequest(long vatAgency, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		this.taxAgency = vatAgency;

		Accounter.createReportService().getTAXItemDetailReport(
				this.taxAgency,
				startDate != null ? startDate.getDate()
						: new ClientFinanceDate().getDate(), endDate.getDate(),
				this);
	}

	@Override
	public void OnRecordClick(TAXItemDetail record) {
		if (Accounter.getUser().canDoInvoiceTransactions())
			ReportsRPC.openTransactionView(record.getTransactionType(),
					record.getTransactionId());

	}

	@Override
	public void export(int generationType) {
		toolBar = (TaxAgencyStartDateEndDateToolbar) this.toolbar;
		this.taxAgency = toolBar.taxAgencyCombo.getSelectedValue().getID();
		this.startDate = toolbar.getStartDate();
		this.endDate = toolbar.getEndDate();
		UIUtils.generateReport(generationType, startDate.getDate(), endDate
				.getDate(), 165, new NumberReportInput(this.taxReturnId),
				new NumberReportInput(this.taxAgency));
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {

		Object data = getData();
		if (data != null) {
			List<TAXItemDetail> detail = (List<TAXItemDetail>) data;
			ClientTAXAgency taxAgency2 = null;
			for (TAXItemDetail td : detail) {
				ClientTAXItem taxItemByName = Accounter.getCompany()
						.getTaxItemByName(td.getTaxItemName());
				taxAgency2 = Accounter.getCompany().getTaxAgency(
						taxItemByName.getTaxAgency());
				this.startDate = td.getStartDate();
				this.endDate = td.getEndDate();
			}
			toolBar = (TaxAgencyStartDateEndDateToolbar) this.toolbar;
			toolBar.taxAgencyCombo.select(taxAgency2);
			toolBar.fromItem.setEnteredDate(toolBar.getStartDate());
			toolBar.toItem.setEnteredDate(toolBar.getEndDate());
			if (!isFromReports) {
				toolBar.taxAgencyCombo.setEnabled(false);
				toolBar.fromItem.setEnabled(false);
				toolBar.toItem.setEnabled(false);
				toolBar.updateButton.setEnabled(false);
				toolBar.dateRangeItem.setEnabled(false);
			}
			this.serverReport.initRecords(detail);
		} else {
			super.initData();
		}
	}

	@Override
	public HashMap<String, Object> saveView() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String selectedDateRange = toolbar.getSelectedDateRange();
		ClientFinanceDate startDate = toolbar.getStartDate();
		ClientFinanceDate endDate = toolbar.getEndDate();
		long status = this.taxAgency;
		map.put("selectedDateRange", selectedDateRange);
		map.put("taxAgency", status);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		return map;
	}

	@Override
	public void restoreView(HashMap<String, Object> map) {
		if (map == null || map.isEmpty()) {
			isDatesArranged = false;
			return;
		}
		ClientFinanceDate startDate = (ClientFinanceDate) map.get("startDate");
		ClientFinanceDate endDate = (ClientFinanceDate) map.get("endDate");
		this.serverReport.setStartAndEndDates(startDate, endDate);
		long status1 = ((Long) map.get("taxAgency"));
		toolbar.setPayeeId(status1);
		toolbar.setEndDate(endDate);
		toolbar.setStartDate(startDate);
		toolbar.setDefaultDateRange((String) map.get("selectedDateRange"));
		isDatesArranged = true;
	}

	public long getTaxReturnId() {
		return taxReturnId;
	}

	public void setTaxReturnId(long taxReturnId) {
		this.taxReturnId = taxReturnId;
	}

	public void setIsFromReports(boolean isFromReports) {
		this.isFromReports = isFromReports;
	}
}
