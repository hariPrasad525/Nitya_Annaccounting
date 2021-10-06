package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.InventoryValuationDetailsServerReport;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class InventoryValutionDetailRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_INVENTORY_VALUTION_DETAIL;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		InventoryValuationDetailsServerReport detailReport = new InventoryValuationDetailsServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {
				return getDateInDefaultType(date);
			}
		};
		updateReport(detailReport);
		try {
			detailReport.onResultSuccess(financeTool.getInventoryManager()
					.getInventoryValutionDetail(company.getID(),
							new ClientFinanceDate(startDate.getDate()),
							new ClientFinanceDate(endDate.getDate()),
							getInputAsLong(0)));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return detailReport.getGridTemplate();
	}

}
