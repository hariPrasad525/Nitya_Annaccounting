package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.ItemActualCostDetailServerReport;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class ItemActualCostDetailRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_ITEM_ACTUAL_COST_DETAIL;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		ItemActualCostDetailServerReport item = new ItemActualCostDetailServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {
				return getDateInDefaultType(date);
			}
		};
		updateReport(item);
		try {

			long customerId = getInputAsLong(0);
			long jobId = getInputAsLong(1);
			Long itemId = getInputAsLong(2);
			boolean isCost = getInputAsBoolean(3);
			item.onResultSuccess(financeTool.getReportManager()
					.getItemActualCostOrRevenueDetails(
							new FinanceDate(startDate.getDate()),
							new FinanceDate(endDate.getDate()),
							company.getId(), itemId, customerId, jobId, isCost));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item.getGridTemplate();
	}

}
