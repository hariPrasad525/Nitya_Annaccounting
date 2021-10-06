package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

public class TrialBalanceServerReport extends
		AbstractFinaneReport<TrialBalance> {
	public TrialBalanceServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	public TrialBalanceServerReport(IFinanceReport<TrialBalance> reportView) {
		this.reportView = reportView;
	}

	@Override
	public String getDefaultDateRange() {
		return messages.financialYearToDate();
	}

	@Override
	public Object getColumnData(TrialBalance record, int columnIndex) {
		if (getPreferences().getUseAccountNumbers() == true) {
			switch (columnIndex) {
			case 0:
				return record.getAccountName();
			case 1:
				return record.getAccountNumber();
			case 2:
				return record.getDebitAmount();
			case 3:
				return record.getCreditAmount();
			}
			return null;
		} else {
			switch (columnIndex) {
			case 0:
				return record.getAccountName();
			case 1:
				return null;
			case 2:
				return record.getDebitAmount();
			case 3:
				return record.getCreditAmount();
			}
			return null;
		}
	}

	@Override
	public int[] getColumnTypes() {
		if (getPreferences().getUseAccountNumbers() == true) {
			return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_NUMBER,
					COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
		} else {
			return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_NUMBER,
					COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
		}

	}

	@Override
	public String[] getColunms() {
		if (getPreferences().getUseAccountNumbers() == true) {
			return new String[] { messages.accountName(),
					messages.accountNumber(), messages.debit(),
					messages.credit() };
		} else {
			return new String[] { messages.accountName(), "", messages.debit(),
					messages.credit() };
		}
	}

	@Override
	public String getTitle() {
		return messages.trialBalance();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// if (this.financeTool == null)
		// return;
		//
		// resetVariables();
		// try {
		// onSuccess(this.financeTool.getTrialBalance(start, end));
		// } catch (DAOException e) {
		// e.printStackTrace();
		// }
	}

	@Override
	public void processRecord(TrialBalance record) {
		if (sectionDepth == 0) {
			addSection(new String[] { "", "" }, new String[] { "",
					messages.total() }, new int[] { 2, 3 });
		} else if (sectionDepth == 1) {
			return;
		}
		// Go on recursive calling if we reached this place
		processRecord(record);
	}

	public void print() {

	}

	@Override
	public ClientFinanceDate getEndDate(TrialBalance obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(TrialBalance obj) {
		return obj.getStartDate();
	}

	@Override
	protected String getPreviousReportDateRange(Object object) {
		return ((TrialBalance) object).getDateRange();
	}

	@Override
	protected ClientFinanceDate getPreviousReportStartDate(Object object) {
		return ((TrialBalance) object).getStartDate();
	}

	@Override
	protected ClientFinanceDate getPreviousReportEndDate(Object object) {
		return ((TrialBalance) object).getEndDate();
	}

	@Override
	public int getColumnWidth(int index) {
		if (index == 2 || index == 3)
			return 145;
		if (index == 1)
			return 130;
		else
			return 200;
	}

	public int sort(TrialBalance obj1, TrialBalance obj2, int col) {
		if (getPreferences().getUseAccountNumbers() == true) {
			switch (col) {
			case 0:
				return obj1.getAccountName().toLowerCase().compareTo(
						obj2.getAccountName().toLowerCase());
			case 1:
				return obj1.getAccountNumber().toLowerCase().compareTo(
						obj2.getAccountNumber().toLowerCase());
			case 2:
				return UIUtils.compareDouble(obj1.getDebitAmount(), obj2
						.getDebitAmount());
			case 3:
				return UIUtils.compareDouble(obj1.getCreditAmount(), obj2
						.getCreditAmount());
			}
			return 0;
		} else {
			switch (col) {
			case 0:
				return obj1.getAccountName().toLowerCase().compareTo(
						obj2.getAccountName().toLowerCase());
			case 1:
				return (Integer) null;
			case 2:
				return UIUtils.compareDouble(obj1.getDebitAmount(), obj2
						.getDebitAmount());
			case 3:
				return UIUtils.compareDouble(obj1.getCreditAmount(), obj2
						.getCreditAmount());
			}
			return 0;
		}

	}

	@Override
	public void resetVariables() {
		this.sectionDepth = 0;
	}

	@Override
	public String[] getDynamicHeaders() {
		if (getPreferences().getUseAccountNumbers() == true) {
			return new String[] { messages.accountName(),
					messages.accountNumber(), messages.debit(),
					messages.credit() };
		} else {
			return new String[] { messages.accountName(), "", messages.debit(),
					messages.credit() };
		}
	}

}
