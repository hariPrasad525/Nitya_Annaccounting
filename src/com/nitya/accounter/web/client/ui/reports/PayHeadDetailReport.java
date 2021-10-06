package com.nitya.accounter.web.client.ui.reports;

import com.nitya.accounter.web.client.core.ClientEmployee;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientPayHead;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.NumberReportInput;
import com.nitya.accounter.web.client.core.reports.PayHeadDetails;
import com.nitya.accounter.web.client.core.reports.PayHeadSummary;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.serverreports.PayHeadDetailServerReport;

public class PayHeadDetailReport extends AbstractReportView<PayHeadDetails> {

	public PayHeadDetailReport() {
		this.getElement().setId("PayHeadDetailReport");
		this.serverReport = new PayHeadDetailServerReport(this);
	}

	@Override
	public void initData() {
		if (data != null) {
			PayHeadSummary headSummary = (PayHeadSummary) data;
			PayHeadEmployeeToolBar payHeadEmployeeToolBar = (PayHeadEmployeeToolBar) this.toolbar;
			payHeadEmployeeToolBar.setEmployee(headSummary.getEmployeeId());
			payHeadEmployeeToolBar.setPayHead(headSummary.getPayHead());
		}
		super.initData();
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		PayHeadEmployeeToolBar payHeadEmployeeToolBar = (PayHeadEmployeeToolBar) this.toolbar;
		long employeeId = payHeadEmployeeToolBar.getEmployee() == null ? data == null ? 0
				: ((PayHeadSummary) data).getEmployeeId()
				: payHeadEmployeeToolBar.getEmployee().getID();
		long payHeadId = payHeadEmployeeToolBar.getSelectedPayHead() == null ? data == null ? 0
				: ((PayHeadSummary) data).getPayHead()
				: payHeadEmployeeToolBar.getSelectedPayHead().getID();
		Accounter.createReportService().getPayHeadDetailReportList(employeeId,
				payHeadId, start, end, this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_PAY_HEAD_EMPLOYEE;
	}

	@Override
	public void OnRecordClick(PayHeadDetails record) {
		ReportsRPC.openTransactionView(ClientTransaction.TYPE_PAY_RUN,
				record.getTransactionId());
	}

	@Override
	public void export(int generationType) {
		PayHeadEmployeeToolBar payHeadEmployeeToolBar = (PayHeadEmployeeToolBar) this.toolbar;
		ClientPayHead selectedPayHead = payHeadEmployeeToolBar
				.getSelectedPayHead();
		ClientEmployee employee = payHeadEmployeeToolBar.getEmployee();
		UIUtils.generateReport(generationType, startDate.getDate(), endDate
				.getDate(), REPORT_TYPE_PAY_HEAD_DETAIL_REPORT,
				new NumberReportInput(employee == null ? 0 : employee.getID()),
				new NumberReportInput(selectedPayHead == null ? 0
						: selectedPayHead.getID()));

	}

}
