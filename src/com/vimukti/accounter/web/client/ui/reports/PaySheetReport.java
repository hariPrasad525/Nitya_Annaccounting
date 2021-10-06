package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.StringReportInput;
import com.vimukti.accounter.web.client.core.reports.PayHeadSummary;
import com.vimukti.accounter.web.client.core.reports.PaySheet;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.PayRollReportActions;
import com.vimukti.accounter.web.client.ui.serverreports.PaySheetServerReport;

public class PaySheetReport extends AbstractReportView<PaySheet> {

	public PaySheetReport() {
		this.serverReport = new PaySheetServerReport(this);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createPayrollService().getPaySheet(start, end, this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void OnRecordClick(PaySheet record) {
		PayHeadSummary payHeadSummary = new PayHeadSummary();
		payHeadSummary.setEmployeeId(record.getEmployeeId());
		payHeadSummary.setPayHead(record.getPayheadId());
		payHeadSummary.setStartDate(toolbar.getStartDate());
		payHeadSummary.setEndDate(toolbar.getEndDate());
		payHeadSummary.setDateRange(toolbar.getSelectedDateRange());
		UIUtils.runAction(payHeadSummary,
				PayRollReportActions.getPayHeadSummaryReportAction());
	}

	@Override
	public void export(int generationType) {
		String accountName = data != null ? ((PaySheet) data).getEmployee()
				: null;
		UIUtils.generateReport(generationType, startDate.getDate(), endDate
				.getDate(), REPORT_TYPE_PAYSHEET, new StringReportInput(
				accountName));
	}

}
