package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.InventoryStockStatusByVendorServerReport;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class InventoryStockStatusByVendorRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_INVENTORY_STOCK_STATUS_BYVENDOR;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		InventoryStockStatusByVendorServerReport stockStatusByVendor = new InventoryStockStatusByVendorServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {
				return getDateInDefaultType(date);
			}
		};
		updateReport(stockStatusByVendor);
		try {
			stockStatusByVendor.onResultSuccess(financeTool
					.getInventoryManager().getInventoryStockStatusByItem(
							company.getID(),
							new ClientFinanceDate(startDate.getDate()),
							new ClientFinanceDate(endDate.getDate())));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return stockStatusByVendor.getGridTemplate();
	}

}
