package com.nitya.accounter.web.client.ui.serverreports;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.reports.BaseReport;
import com.nitya.accounter.web.client.core.reports.MISC1099TransactionDetail;
import com.nitya.accounter.web.client.ui.core.ReportUtility;
import com.nitya.accounter.web.client.ui.reports.IFinanceReport;

public class MISC1099TransactionDetailServerReport extends
		AbstractFinaneReport<MISC1099TransactionDetail> {

	private String sectionName = "";

	public MISC1099TransactionDetailServerReport(
			IFinanceReport<MISC1099TransactionDetail> reportView) {
		this.reportView = reportView;
	}

	public MISC1099TransactionDetailServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { getMessages().name(), getMessages().date(),
				getMessages().type(), getMessages().number(),
				getMessages().memo(), getMessages().box1099(),
				getMessages().Account(), getMessages().amount() };
	}

	@Override
	public String getTitle() {
		return messages.MISC1099TransactionDetailByVendor(
				Global.get().Vendor());
	}

	@Override
	public String[] getColunms() {
		return new String[] { getMessages().name(), getMessages().date(),
				getMessages().type(), getMessages().number(),
				getMessages().memo(), getMessages().box1099(),
				getMessages().Account(), getMessages().amount() };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public void processRecord(MISC1099TransactionDetail record) {
		if (sectionDepth == 0) {
			this.sectionName = "";
			addSection(new String[] { sectionName }, new String[] { "" },
					new int[] { 7 });
		} else if (sectionDepth == 1) {
			// No need to do anything, just allow adding this record
			if (!sectionName.equals("")) {
				endSection();
			} else {
				return;
			}
		} else if (sectionDepth == 2) {
			addSection(new String[] { "", "" }, new String[] { "", "", "", "",
					messages.total() }, new int[] { 7 });
		}

		// Go on recursive calling if we reached this place
		processRecord(record);
	}

	@Override
	public Object getColumnData(MISC1099TransactionDetail record,
			int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getName();

		case 1:
			return record.getDate();

		case 2:
			return ReportUtility.getTransactionName(record.getType());

		case 3:
			return record.getNumber();

		case 4:
			return record.getMemo();

		case 5:
			int box1099 = record.getBox1099();
			String box = "";
			if (box1099 > 0) {
				box = messages.box() + box1099;
			}
			return box;

		case 6:
			return record.getAccount();

		case 7:
			return record.getAmount();

		default:
			break;
		}
		return null;
	}

	@Override
	public ClientFinanceDate getStartDate(MISC1099TransactionDetail obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(MISC1099TransactionDetail obj) {
		return obj.getEndDate();
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
