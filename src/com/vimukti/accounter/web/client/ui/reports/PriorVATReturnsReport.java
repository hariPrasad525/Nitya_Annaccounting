package com.vimukti.accounter.web.client.ui.reports;

import java.util.HashMap;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.NumberReportInput;
import com.vimukti.accounter.web.client.core.reports.VATSummary;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.PriorVATReturnsServerReport;

public class PriorVATReturnsReport extends AbstractReportView<VATSummary> {

	private int row = -1;
	private static long vatAgency;

	public PriorVATReturnsReport() {

		super(false, messages.pleaseSelectVATAgencyAndEndingDateToViewReport());
		isVATPriorReport = true;
		isVATSummaryReport = true;
		this.serverReport = new PriorVATReturnsServerReport(this);

	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public void initData() {

		Object data = getData();
		if (data != null) {
			List<VATSummary> summary = (List<VATSummary>) data;
			TaxAgencyStartDateEndDateToolbar toolBar = (TaxAgencyStartDateEndDateToolbar) this.toolbar;
			// TODO set taxagency item to combo
			toolBar.taxAgencyCombo.setEnabled(false);
			toolBar.fromItem.setEnteredDate(this.startDate);
			toolBar.toItem.setEnteredDate(this.endDate);
			toolBar.fromItem.setEnabled(false);
			toolBar.toItem.setEnabled(false);
			toolBar.dateRangeItem.setEnabled(false);
			toolBar.updateButton.setEnabled(false);
			this.serverReport.initRecords(summary);
		} else {
			super.initData();
		}
	}

	@Override
	public void OnRecordClick(VATSummary record) {

	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_TAXAGENCY;
	}

	@Override
	public void makeReportRequest(long vatAgency, ClientFinanceDate end) {
		this.row = -1;
		if (this.serverReport instanceof PriorVATReturnsServerReport)
			((PriorVATReturnsServerReport) this.serverReport).row = -1;
		Accounter.createReportService().getPriorReturnVATSummary(vatAgency,
				end, this);
		this.vatAgency = vatAgency;
	}

	@Override
	public void makeReportRequest(long vatAgency, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		this.row = -1;
		if (this.serverReport instanceof PriorVATReturnsServerReport)
			((PriorVATReturnsServerReport) this.serverReport).row = -1;
		Accounter.createReportService().getPriorReturnVATSummary(vatAgency,
				endDate, this);
		this.vatAgency = vatAgency;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {

		removeLoadingImage();

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void export(int generationType) {
		UIUtils.generateReport(generationType, startDate.getDate(),
				((PriorVATReturnToolBar) toolbar).getSelectedEndDate()
						.getDate(), 136, new NumberReportInput(vatAgency));
	}

	@Override
	public void printPreview() {

	}

	public int sort(VATSummary obj1, VATSummary obj2, int col) {

		// switch (col) {
		// case 0:
		// return obj1.getName().toLowerCase().compareTo(
		// obj2.getName().toLowerCase());
		// case 1:
		// return UIUtils.compareDouble(obj1.getValue(), obj2.getValue());
		// }
		return 0;
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
		toolbar.setEndDate(endDate);
		toolbar.setStartDate(startDate);
		long status1 = (Long) map.get("SelectTAXAgency");
		PriorVATReturnsReport.vatAgency = status1;
	}

	@Override
	public HashMap<String, Object> saveView() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		long status = PriorVATReturnsReport.vatAgency;
		ClientFinanceDate startDate = toolbar.getStartDate();
		ClientFinanceDate endDate = toolbar.getEndDate();
		map.put("SelectTAXAgency", status);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		return map;
	}

}
