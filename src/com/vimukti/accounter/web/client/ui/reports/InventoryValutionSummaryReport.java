package com.vimukti.accounter.web.client.ui.reports;

import java.util.HashMap;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.NumberReportInput;
import com.vimukti.accounter.web.client.core.reports.InventoryValutionSummary;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.InventoryValutionSummaryServerReport;

public class InventoryValutionSummaryReport extends
		AbstractReportView<InventoryValutionSummary> {
	private long warehouseId = 0;

	public InventoryValutionSummaryReport() {
		this.getElement().setId("inventory-valuation-summary");
		this.serverReport = new InventoryValutionSummaryServerReport(this);
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {

		makeReportRequest(getWarehouseId(), start, end);
	}

	public void makeReportRequest(long wareHouseId,
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		grid.removeAllRows();
		grid.addLoadingImagePanel();
		setWarehouseId(wareHouseId);
		Accounter.createReportService().getInventoryValutionSummary(
				wareHouseId, startDate, endDate, this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_WAREHOUSE;
	}

	@Override
	public void OnRecordClick(InventoryValutionSummary record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		UIUtils.runAction(record,
				InventoryReportsAction.valuationDetails(record.getId()));
	}

	@Override
	public void export(int generationType) {
		UIUtils.generateReport(generationType, startDate.getDate(),
				endDate.getDate(), REPORT_TYPE_INVENTORY_VALUTION_SUMMARY,
				new NumberReportInput(getWarehouseId()));
	}

	public long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(long warehouseId) {
		this.warehouseId = warehouseId;
	}

	@Override
	public void restoreView(HashMap<String, Object> map) {
		if (map == null || map.isEmpty()) {
			isDatesArranged = false;
			return;
		}
		ClientFinanceDate startDate = (ClientFinanceDate) map.get("startDate");
		ClientFinanceDate endDate = (ClientFinanceDate) map.get("endDate");
		this.serverReport.setStartAndEndDates(startDate, endDate);
		toolbar.setEndDate(endDate);
		toolbar.setStartDate(startDate);
		toolbar.setDefaultDateRange((String) map.get("selectedDateRange"));
		isDatesArranged = true;
	}

	@Override
	public HashMap<String, Object> saveView() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String selectedDateRange = toolbar.getSelectedDateRange();
		ClientFinanceDate startDate = toolbar.getStartDate();
		ClientFinanceDate endDate = toolbar.getEndDate();
		map.put("selectedDateRange", selectedDateRange);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		return map;
	}

}
