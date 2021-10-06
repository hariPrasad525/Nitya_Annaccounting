package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.StringReportInput;
import com.vimukti.accounter.web.client.core.reports.EstimatesByJob;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.EstimatesByJobServerReport;

public class EstimatesByJobReport extends AbstractReportView<EstimatesByJob> {

	public EstimatesByJobReport() {
		this.serverReport = new EstimatesByJobServerReport(this);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getEstimatesByJob(start, end, this);

	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void OnRecordClick(EstimatesByJob record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		ReportsRPC.openTransactionView(record.getTransactionType(),
				record.getEstimateId());
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
		String customerName = this.data != null ? ((EstimatesByJob) this.data)
				.getCustomerName() : "";
		UIUtils.generateReport(generationType, startDate.getDate(),
				endDate.getDate(), 191, new StringReportInput(customerName));
	}

}
