package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.reports.PayHeadDetails;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

public class PayHeadDetailServerReport extends
		AbstractFinaneReport<PayHeadDetails> {

	String payHeadSectionName = "";

	public PayHeadDetailServerReport(IFinanceReport<PayHeadDetails> reportView) {
		this.reportView = reportView;
	}

	public PayHeadDetailServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { messages.date(), messages.number(),
				messages.employee(), messages.debit(), messages.credit() };
	}

	@Override
	public String getTitle() {
		return messages.payHeadDetailReport();
	}

	@Override
	public String[] getColunms() {
		return new String[] { messages.date(), messages.number(),
				messages.employee(), messages.debit(), messages.credit() };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_DATE, COLUMN_TYPE_NUMBER,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public void processRecord(PayHeadDetails record) {
		if (sectionDepth == 0) {
			this.payHeadSectionName = record.getPayHead();
			addSection(new String[] { payHeadSectionName },
					new String[] { "" }, new int[] { 3, 4 });
		} else if (sectionDepth == 1) {
			// No need to do anything, just allow adding this record
			if (!payHeadSectionName.equals(record.getPayHead())) {
				endSection();
			} else {
				return;
			}
		}
		processRecord(record);
	}

	@Override
	public Object getColumnData(PayHeadDetails record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return getDateByCompanyType(new ClientFinanceDate(
					record.getPeriodEndDate()));
		case 1:
			return record.getTransactionNumber();
		case 2:
			return record.getEmployee();
		case 3:
			return isEarning(record.getPayHeadType()) ? record.getAmount()
					: 0.0;
		case 4:
			return isDeduction(record.getPayHeadType()) ? record.getAmount()
					: 0.0;
		default:
			break;
		}
		return null;
	}

	public boolean isEarning(int type) {
		return type == ClientPayHead.TYPE_EARNINGS_FOR_EMPLOYEES
				|| type == ClientPayHead.TYPE_REIMBURSEMENTS_TO_EMPLOYEES
				|| type == ClientPayHead.TYPE_BONUS;
	}

	public boolean isDeduction(int type) {
		return type == ClientPayHead.TYPE_DEDUCTIONS_FOR_EMPLOYEES
				|| type == ClientPayHead.TYPE_EMPLOYEES_OTHER_CHARGES
				|| type == ClientPayHead.TYPE_EMPLOYEES_STATUTORY_CONTRIBUTIONS
				|| type == ClientPayHead.TYPE_EMPLOYEES_STATUTORY_DEDUCTIONS
				|| type == ClientPayHead.TYPE_LOANS_AND_ADVANCES;
	}

	@Override
	public ClientFinanceDate getStartDate(PayHeadDetails obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(PayHeadDetails obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {

	}

	@Override
	public void resetVariables() {
		payHeadSectionName = "";
		sectionDepth = 0;
		super.resetVariables();
	}

}
