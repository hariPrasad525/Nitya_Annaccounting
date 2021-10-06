package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.PaySlipSummary;
import com.vimukti.accounter.web.client.ui.reports.PaySlipSummaryReport;

public class PaySlipSummaryServerReport extends
		AbstractFinaneReport<PaySlipSummary> {

	public PaySlipSummaryServerReport(PaySlipSummaryReport paySlipSummaryReport) {
		this.reportView = paySlipSummaryReport;
	}

	public PaySlipSummaryServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { getMessages().name(), getMessages().number(),
				getMessages().accountNumber(), getMessages().bankName(),
				getMessages().branchOrdivison(), getMessages().amount(),
				getMessages().email() };
	}

	@Override
	public String getTitle() {
		return getMessages().paySlipSummary();
	}

	@Override
	public String[] getColunms() {
		return new String[] { getMessages().name(), getMessages().number(),
				getMessages().accountNumber(), getMessages().bankName(),
				getMessages().branchOrdivison(), getMessages().amount(),
				getMessages().email() };
	}

	@Override
	public int[] getColumnTypes() {

		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_TEXT };
	}

	@Override
	public void processRecord(PaySlipSummary record) {
		if (sectionDepth == 0) {
			addSection("", getMessages().total(), new int[] { 5 });
		} else if (sectionDepth == 1) {
			return;
		}
		// Go on recursive calling if we reached this place
		processRecord(record);
	}

	@Override
	public Object getColumnData(PaySlipSummary record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getName();
		case 1:
			return record.getNumber();
		case 2:
			return record.getAccountNo();
		case 3:
			return record.getBankName();
		case 4:
			return record.getBranch();
		case 5:
			return record.getAmount();
		case 6:
			return record.getEmail();
		}

		return null;
	}

	@Override
	public ClientFinanceDate getStartDate(PaySlipSummary obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(PaySlipSummary obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub

	}

}
