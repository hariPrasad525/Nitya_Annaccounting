package com.nitya.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.HashMap;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.NumberReportInput;
import com.nitya.accounter.web.client.core.reports.TransactionDetailByAccount;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.serverreports.MissingChecksServerReport;

public class MissingChecksReport extends
		AbstractReportView<TransactionDetailByAccount> {

	private long accounId = 0;

	public MissingChecksReport() {
		this.serverReport = new MissingChecksServerReport(this);
		serverReport.setRecords(new ArrayList<TransactionDetailByAccount>());
		this.getElement().setId("MissingChecksReport");
	}

	@Override
	public void init() {
		super.init();
		this.toolbar.setAccId(this.accounId);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		this.makeReportRequest(getAccountId(), start, end);

	}

	public void makeReportRequest(long account, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		grid.removeAllRows();
		if (account == 0) {
			grid.addEmptyMessage(messages.pleaseSelect(messages.account()));
			return;
		}
		setAccountId(account);
		Accounter.createReportService().getMissingCheckDetils(account,
				startDate, endDate, this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_ACCOUNT;
	}

	@Override
	public void OnRecordClick(TransactionDetailByAccount record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		ReportsRPC.openTransactionView(record.getTransactionType(),
				record.getTransactionId());
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
		accounId = (Long) map.get("account");
		toolbar.setEndDate(endDate);
		toolbar.setStartDate(startDate);
		toolbar.setAccId(accounId);
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

	public long getAccountId() {
		return accounId;
	}

	public void setAccountId(long accounId) {
		this.accounId = accounId;
	}

	@Override
	public void export(int generationType) {
		if (getAccountId() == 0) {
			Accounter.showError(messages.pleaseSelect(messages.account()));
		} else {
			UIUtils.generateReport(generationType,
					toolbar.startDate.getDate(), toolbar.endDate.getDate(),
					181, new NumberReportInput(getAccountId()));
		}
	}

}
