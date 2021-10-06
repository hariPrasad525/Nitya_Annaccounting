package com.nitya.accounter.web.client.ui.serverreports;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.reports.BaseReport;
import com.nitya.accounter.web.client.core.reports.ReconcilationItemList;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.client.ui.core.ReportUtility;
import com.nitya.accounter.web.client.ui.reports.IFinanceReport;

public class ReconcilationDetailsByAccountServerReport extends
		AbstractFinaneReport<ReconcilationItemList> {
	private String sectionName = "";

	public ReconcilationDetailsByAccountServerReport(
			IFinanceReport<ReconcilationItemList> reconcilationsReport,
			long bankAccountId) {
		this.reportView = reconcilationsReport;

	}

	public ReconcilationDetailsByAccountServerReport(long startDate,
			long endDate, int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { "", messages.date(), messages.name(),
				messages.number(), messages.debit(), messages.credit() };
	}

	@Override
	public String getTitle() {
		return "ReconcilationDetailsByAccount";
	}

	@Override
	public String[] getColunms() {
		return new String[] { "", messages.date(), messages.name(),
				messages.number(), messages.debit(), messages.credit() };
	}

	@Override
	public int[] getColumnTypes() {

		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_NUMBER, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT };

	}

	@Override
	public int getColumnWidth(int index) {
		switch (index) {
		case 0:
			return 100;
		case 1:
			return 100;
		case 3:
			return 100;
		case 4:
			return 80;
		case 5:
			return 100;
		case 6:
			return 100;
		default:
			return -1;
		}
	}

	@Override
	public boolean isWiderReport() {
		return true;
	}

	@Override
	public void resetVariables() {
		this.sectionDepth = 0;
		this.sectionName = "";
	}

	@Override
	public void processRecord(ReconcilationItemList record) {

		if (sectionDepth == 0) {
			this.sectionName = record.getBankAccountName();
			addSection(new String[] { sectionName }, new String[] { "" },
					new int[] { 4, 5 });
		} else if (sectionDepth == 1) {
			if (!sectionName.equals(record.getBankAccountName())) {
				endSection();
			} else {
				return;
			}
		}
		processRecord(record);
	}

	@Override
	public Object getColumnData(ReconcilationItemList record, int columnIndex) {

		switch (columnIndex) {
		case 0:
			return "";
		case 1:
			return getDateByCompanyType(record.getTransactionDate());
		case 2:
			return ReportUtility.getTransactionName(record.getTransationType());
		case 3:
			return record.getTransactionNo();
		case 4:
			if (DecimalUtil.isLessThan(record.getAmount(), 0.00D)) {
				return Math.abs(record.getAmount());
			}
			return 0.0D;
		case 5:
			if (DecimalUtil.isGreaterThan(record.getAmount(), 0.00D)) {
				return Math.abs(record.getAmount());
			}
			return 0.0D;
		}
		return null;

	}

	@Override
	public ClientFinanceDate getStartDate(ReconcilationItemList obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(ReconcilationItemList obj) {
		return obj.getEndDate();
	}

	@Override
	protected String getPreviousReportDateRange(Object object) {
		return ((BaseReport) object).getDateRange();
	}

	@Override
	protected ClientFinanceDate getPreviousReportStartDate(Object object) {
		return ((BaseReport) object).getStartDate();
	}

	@Override
	protected ClientFinanceDate getPreviousReportEndDate(Object object) {
		return ((BaseReport) object).getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub

	}

}
