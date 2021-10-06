package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.PurchaseByItemDetailServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class PurchasesByItemDetailRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_PURCHASEBYITEMDETAIL;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		PurchaseByItemDetailServerReport purchaseByItemDetailServerReport = new PurchaseByItemDetailServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(purchaseByItemDetailServerReport, financeTool);
		purchaseByItemDetailServerReport.resetVariables();
		try {
			if (getInputAsString(0) == null) {
				purchaseByItemDetailServerReport.onResultSuccess(financeTool
						.getPurchageManager().getPurchasesByItemDetail(
								startDate, endDate, getCompany().getID()));
			} else {
				purchaseByItemDetailServerReport.onResultSuccess(financeTool
						.getPurchageManager().getPurchasesByItemDetail(
								getInputAsString(0), startDate, endDate,
								getCompany().getID()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return purchaseByItemDetailServerReport.getGridTemplate();
	}

}
