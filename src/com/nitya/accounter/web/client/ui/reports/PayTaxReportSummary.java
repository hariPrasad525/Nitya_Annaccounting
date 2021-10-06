package com.nitya.accounter.web.client.ui.reports;

import com.nitya.accounter.web.client.core.ClientEmployee;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.NumberReportInput;
import com.nitya.accounter.web.client.core.reports.PayHeadSummary;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.core.PayRollReportActions;
import com.nitya.accounter.web.client.ui.serverreports.PayTaxSummarServerReport;

public class PayTaxReportSummary extends AbstractReportView<PayHeadSummary> {
	
	public PayTaxReportSummary() {
		this.getElement().setId("PayTaxReportSummary");
		this.serverReport = new PayTaxSummarServerReport(this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_PAY_TAX;
	}

	@Override
	public void OnRecordClick(PayHeadSummary record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		UIUtils.runAction(record,
				PayRollReportActions.getPayHeadDetailReportAction());
	}

	@Override
	public void export(int generationType) {
		UIUtils.generateReport(generationType, startDate.getDate(), endDate
				.getDate(), getReportType(), new NumberReportInput(0));
	}

	private int getReportType() {
		return REPORT_TYPE_PAY_TAX_SUMMARY_REPORT;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		PayTaxSummaryToolBar payTaxSummaryToolBar = (PayTaxSummaryToolBar) this.toolbar;
		ClientEmployee emp = payTaxSummaryToolBar.getEmployee();
		long emplId = emp != null && emp.getName() != messages.all() ? emp.getID() : 0 ;
		Accounter.createReportService().getPayTaxSummaryReport(payTaxSummaryToolBar.getTaxType(), emplId,
				start, end, this);
	}

}
