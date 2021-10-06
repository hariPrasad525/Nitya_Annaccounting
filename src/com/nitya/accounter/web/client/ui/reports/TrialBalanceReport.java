package com.nitya.accounter.web.client.ui.reports;

import java.util.HashMap;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.reports.TrialBalance;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.serverreports.TrialBalanceServerReport;

public class TrialBalanceReport extends AbstractReportView<TrialBalance> {

	public TrialBalanceReport() {
		this.serverReport = new TrialBalanceServerReport(this);
	}

	@Override
	public void OnRecordClick(TrialBalance record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		UIUtils.runAction(record,
				CompanyAndFinancialReportsAction.transactionDetailByAccount());
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_AS_OF;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getTrialBalance(start, end, this);
	}

	@Override
	public void onEdit() {

	}

	@Override
	public void export(int generationType) {
		UIUtils.generateReport(generationType, startDate.getDate(),
				endDate.getDate(), 113);
	}

	@Override
	public void printPreview() {

	}

	public int sort(TrialBalance obj1, TrialBalance obj2, int col) {
		if (getPreferences().getUseAccountNumbers() == true) {
			switch (col) {
			case 0:
				return obj1.getAccountName().toLowerCase()
						.compareTo(obj2.getAccountName().toLowerCase());
			case 1:
				return obj1.getAccountNumber().toLowerCase()
						.compareTo(obj2.getAccountNumber().toLowerCase());
			case 2:
				return UIUtils.compareDouble(obj1.getDebitAmount(),
						obj2.getDebitAmount());
			case 3:
				return UIUtils.compareDouble(obj1.getCreditAmount(),
						obj2.getCreditAmount());
			}
			return 0;
		} else {
			switch (col) {
			case 0:
				return obj1.getAccountName().toLowerCase()
						.compareTo(obj2.getAccountName().toLowerCase());
			case 1:
				return (Integer) null;
			case 2:
				return UIUtils.compareDouble(obj1.getDebitAmount(),
						obj2.getDebitAmount());
			case 3:
				return UIUtils.compareDouble(obj1.getCreditAmount(),
						obj2.getCreditAmount());
			}
			return 0;
		}

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
