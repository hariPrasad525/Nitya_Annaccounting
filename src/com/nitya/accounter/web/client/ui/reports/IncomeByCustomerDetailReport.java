package com.nitya.accounter.web.client.ui.reports;

import java.util.HashMap;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.reports.IncomeByCustomerDetail;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.serverreports.IncomeByCustomerDetailServerReport;

public class IncomeByCustomerDetailReport extends
		AbstractReportView<IncomeByCustomerDetail> {

	public IncomeByCustomerDetailReport() {
		this.serverReport = new IncomeByCustomerDetailServerReport(this);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getIncomeByCustomerDetail(start, end,
				this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void OnRecordClick(IncomeByCustomerDetail record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		ReportsRPC.openTransactionView(record.getTransactionType(),
				record.getId());
	}

	@Override
	public void export(int generationType) {
		UIUtils.generateReport(generationType,
				toolbar.getStartDate().getDate(), toolbar.getEndDate()
						.getDate(), 201);
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

	@Override
	public boolean canPrint() {
		return true;
	}

	@Override
	public boolean canExportToCsv() {
		return true;
	}
}
