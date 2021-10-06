package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.InventoryStockStatusByItemServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class InventoryStockStatusByItemRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_INVENTORY_STOCK_STATUS_BYITEM;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		InventoryStockStatusByItemServerReport stockStatusItem = new InventoryStockStatusByItemServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {
				return getDateInDefaultType(date);
			}
		};
		updateReport(stockStatusItem);
		try {
			stockStatusItem.onResultSuccess(financeTool.getInventoryManager()
					.getInventoryStockStatusByItem(company.getID(),
							new ClientFinanceDate(startDate.getDate()),
							new ClientFinanceDate(endDate.getDate())));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return stockStatusItem.getGridTemplate();
	}

}
