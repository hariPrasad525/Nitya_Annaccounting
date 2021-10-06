package com.nitya.accounter.web.client.ui.reports;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientPayHead;
import com.nitya.accounter.web.client.core.NumberReportInput;
import com.nitya.accounter.web.client.core.reports.PayHeadSummary;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.core.PayRollReportActions;
import com.nitya.accounter.web.client.ui.serverreports.PayHeadSummaryServerReport;

public class PayHeadSummaryReport extends AbstractReportView<PayHeadSummary> {

	public PayHeadSummaryReport() {
		this.serverReport = new PayHeadSummaryServerReport(this);
		this.getElement().setId("PayHeadSummaryReport");
	}
	
	public PayHeadSummaryReport(String type) {
		this.serverReport = new PayHeadSummaryServerReport(this, type);
	}

	@Override
	public void makeReportRequest(long payHeadId, ClientFinanceDate start,
			ClientFinanceDate end) {

	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_PAY_HEAD;
	}

	@Override
	public void initData() {
		if (data != null) {
			PayHeadSummary headSummary = (PayHeadSummary) data;
			PayHeadReportToolBar payHeadEmployeeToolBar = (PayHeadReportToolBar) this.toolbar;
			payHeadEmployeeToolBar.setPayHead(headSummary.getPayHead());
		}
		super.initData();
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
		PayHeadReportToolBar payHeadEmployeeToolBar = (PayHeadReportToolBar) this.toolbar;
		ClientPayHead selectedPayHead = payHeadEmployeeToolBar
				.getSelectedPayHead();
		UIUtils.generateReport(generationType, startDate.getDate(), endDate
				.getDate(), getReportType(), new NumberReportInput(
				selectedPayHead == null ? 0 : selectedPayHead.getID()));
	}

	private int getReportType() {
		return REPORT_TYPE_PAY_HEAD_SUMMARY_REPORT;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		PayHeadReportToolBar payHeadEmployeeToolBar = (PayHeadReportToolBar) this.toolbar;
		long payHeadId = payHeadEmployeeToolBar.getSelectedPayHead() == null ? data == null ? 0
				: ((PayHeadSummary) data).getPayHead()
				: payHeadEmployeeToolBar.getSelectedPayHead().getID();
		Accounter.createReportService().getPayHeadSummaryReport(payHeadId,
				start, end, this);
	}
}
