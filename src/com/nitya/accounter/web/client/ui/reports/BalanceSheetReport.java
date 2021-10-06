package com.nitya.accounter.web.client.ui.reports;

import java.util.HashMap;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.reports.TrialBalance;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.serverreports.BalanceSheetServerReport;

public class BalanceSheetReport extends AbstractReportView<TrialBalance> {

	public BalanceSheetReport() {
		super(false, "");
		this.serverReport = new BalanceSheetServerReport(this);
		this.serverReport.setIshowGridFooter(false);
	}

	@Override
	public void OnRecordClick(TrialBalance record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		if (record.getAccountId() != 0) {
			UIUtils.runAction(record, CompanyAndFinancialReportsAction
					.transactionDetailByAccount());
		} else {
			UIUtils.runAction(record, CompanyAndFinancialReportsAction
					.transactionDetailByAccount());
		}

	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_AS_OF;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getBalanceSheetReport(start, end, this);
	}

	@Override
	public void onEdit() {

	}

	@Override
	public void export(int generationType) {
		UIUtils.generateReport(generationType, startDate.getDate(),
				endDate.getDate(), 112);
	}

	@Override
	public void restoreView(HashMap<String, Object> map) {
		if (map == null || map.isEmpty()) {
			isDatesArranged = false;
			return;
		}
		ClientFinanceDate endDate = (ClientFinanceDate) map.get("endDate");
		toolbar.setEndDate(endDate);
		toolbar.setDefaultDateRange((String) map.get("selectedDateRange"));
		isDatesArranged = true;
	}

	@Override
	public HashMap<String, Object> saveView() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String selectedDateRange = toolbar.getSelectedDateRange();
		ClientFinanceDate endDate = toolbar.getEndDate();
		map.put("selectedDateRange", selectedDateRange);
		map.put("endDate", endDate);
		return map;
	}
}
