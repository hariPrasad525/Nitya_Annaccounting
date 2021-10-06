package com.nitya.accounter.web.client.ui.reports;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.NumberReportInput;
import com.nitya.accounter.web.client.core.reports.BudgetActuals;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;

public class BudgetVsActualsReport extends AbstractReportView<BudgetActuals> {

	long budgetId;
	BudgetVsActualsServerReport serverreport;
	int type;

	public BudgetVsActualsReport() {
		serverreport = new BudgetVsActualsServerReport(this);
		this.serverReport = serverreport;
		this.serverReport.setIshowGridFooter(true);
		this.addStyleName("budget_overview");
	}

	@Override
	public void OnRecordClick(BudgetActuals record) {

		// ReportsRPC.openTransactionView(record.getTransactionType(),
		// record.getTransactionId());
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_BUDGETVSACTUALS;
	}

	@Override
	public void makeReportRequest(long id, ClientFinanceDate start,
			ClientFinanceDate end, int type) {
		this.type = type;
		budgetId = id;
		if (budgetId == 999L) {
			super.addEmptyMessage(messages.noRecordsToShow());
		} else {
			Accounter.createReportService().getBudgetvsAcualReportData(id,
					start, end, type, this);
		}
	}

	@Override
	public void onEdit() {

	}

	@Override
	public void export(int generationType) {
		UIUtils.generateReport(generationType, new ClientFinanceDate()
				.getDate(), new ClientFinanceDate().getDate(), 183,
				new NumberReportInput(type), new NumberReportInput(budgetId));
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {

	}

}
