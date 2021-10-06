package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.PurchaseOrderServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class PurchasesOpenRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_PURCHASEORDER_OPEN;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		PurchaseOrderServerReport purchaseOpenOrderServerReport = new PurchaseOrderServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(purchaseOpenOrderServerReport, financeTool);
		purchaseOpenOrderServerReport.resetVariables();
		try {
			purchaseOpenOrderServerReport
					.onResultSuccess(reportsSerivce.getPurchaseOrderReport(
							getInputAsInteger(0),
							startDate.toClientFinanceDate(),
							endDate.toClientFinanceDate(), getCompany().getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return purchaseOpenOrderServerReport.getGridTemplate();
	}

}
