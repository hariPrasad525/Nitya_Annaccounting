package com.nitya.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.NumberReportInput;
import com.nitya.accounter.web.client.core.reports.ReconciliationDiscrepancy;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.serverreports.ReconciliationDiscrepancyServerReport;

public class ReconciliationDiscrepancyReport extends
		AbstractReportView<ReconciliationDiscrepancy> {

	private long accounId = 0;

	public ReconciliationDiscrepancyReport() {
		this.serverReport = new ReconciliationDiscrepancyServerReport(this);
		serverReport.setRecords(new ArrayList<ReconciliationDiscrepancy>());
		this.getElement().setId("ReconciliationDiscrepancyReport");
	}

	@Override
	public void init() {
		super.init();
		this.toolbar.setAccId(this.getAccounId());
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		this.makeReportRequest(getAccounId(), start, end);
	}

	@Override
	public void makeReportRequest(long acount, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		grid.clear();
		if (acount != 0) {
			setAccounId(acount);
		}
		Accounter.createReportService().getReconciliationDiscrepancy(accounId,
				startDate, endDate, this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_ACCOUNT;
	}

	@Override
	public void OnRecordClick(ReconciliationDiscrepancy record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		ReportsRPC.openTransactionView(record.getTransactionType(),
				record.getTransactionId());
	}

	public void restoreView(Map<String, Object> map) {
		if (map == null || map.isEmpty()) {
			isDatesArranged = false;
			return;
		}
		ClientFinanceDate startDate = (ClientFinanceDate) map.get("startDate");
		ClientFinanceDate endDate = (ClientFinanceDate) map.get("endDate");
		this.serverReport.setStartAndEndDates(startDate, endDate);
		accounId = (Long) map.get("account");
		toolbar.setAccId(accounId);
		toolbar.setEndDate(endDate);
		toolbar.setStartDate(startDate);
		toolbar.setDefaultDateRange((String) map.get("selectedDateRange"));
		isDatesArranged = false;
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
		map.put("account", accounId);
		return map;
	}

	public long getAccounId() {
		return accounId;
	}

	public void setAccounId(long accounId) {
		this.accounId = accounId;
	}

	@Override
	public void export(int generationType) {
		if (getAccounId() == 0) {
			Accounter.showError(messages.pleaseSelect(messages.account()));
		} else {
			UIUtils.generateReport(generationType,
					toolbar.startDate.getDate(), toolbar.endDate.getDate(),
					182, new NumberReportInput(getAccounId()));
		}
	}

}
