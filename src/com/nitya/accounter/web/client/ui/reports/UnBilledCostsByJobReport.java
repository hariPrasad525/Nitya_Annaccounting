package com.nitya.accounter.web.client.ui.reports;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.StringReportInput;
import com.nitya.accounter.web.client.core.reports.UnbilledCostsByJob;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.serverreports.UnBilledCostsByJobServerReport;

public class UnBilledCostsByJobReport extends
		AbstractReportView<UnbilledCostsByJob> {
	public UnBilledCostsByJobReport() {
		this.serverReport = new UnBilledCostsByJobServerReport(this);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getUnBilledCostsByJob(start, end, this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void OnRecordClick(UnbilledCostsByJob record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		ReportsRPC.openTransactionView(record.getType(),
				record.getTransaction());
	}

	@Override
	public boolean canPrint() {
		return true;
	}

	@Override
	public boolean canExportToCsv() {
		return true;
	}

	@Override
	public void export(int generationType) {
		String customerName = this.data != null ? ((UnbilledCostsByJob) this.data)
				.getCustomerName() : "";
		UIUtils.generateReport(generationType, startDate.getDate(),
				endDate.getDate(), 186, new StringReportInput(customerName));
	}
}
