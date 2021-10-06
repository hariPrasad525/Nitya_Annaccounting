package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.reports.Reconciliation;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

public class ReconcilationsServerReport extends
		AbstractFinaneReport<Reconciliation> {

	public ReconcilationsServerReport(
			IFinanceReport<Reconciliation> reconcilationsReport) {
		this.reportView = reconcilationsReport;
	}

	public ReconcilationsServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { messages.Account(), messages.accountType(),
				messages.startDate(), messages.lastReconcilliationDate() };
	}

	@Override
	public String getTitle() {
		return messages.reconciliations();
	}

	@Override
	public int getColumnWidth(int index) {
		if (index == 0)
			return 160;
		else if (index == 1)
			return 70;
		else if (index == 2)
			return 70;
		else if (index == 3)
			return 100;
		else
			return -1;
	}

	@Override
	public String[] getColunms() {
		return new String[] { messages.Account(), messages.accountType(),
				messages.startDate(), messages.lastReconcilliationDate() };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_NUMBER, COLUMN_TYPE_NUMBER,
				COLUMN_TYPE_DATE, COLUMN_TYPE_DATE };
	}

	@Override
	public void processRecord(Reconciliation record) {
		addSection(new String[] {}, new String[] { "" }, new int[] {});
	}

	@Override
	public Object getColumnData(Reconciliation record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getAccountName();
		case 1:
			return Utility.getAccountTypeString(record.getAccountType());
		case 2:
			return record.getStatementdate();
		case 3:
			return record.getReconcilationdate();
		}
		return null;
	}

	@Override
	public ClientFinanceDate getStartDate(Reconciliation obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(Reconciliation obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {

	}
}
