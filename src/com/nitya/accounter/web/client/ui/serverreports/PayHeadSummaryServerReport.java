package com.nitya.accounter.web.client.ui.serverreports;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.reports.PayHeadSummary;
import com.nitya.accounter.web.client.ui.reports.IFinanceReport;
import com.nitya.accounter.web.client.ui.reports.PayHeadSummaryReport;

public class PayHeadSummaryServerReport extends
		AbstractFinaneReport<PayHeadSummary> {

	private String sectionName = "";
	
	private String type = null;

	public PayHeadSummaryServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	public PayHeadSummaryServerReport(IFinanceReport<PayHeadSummary> reportView) {
		this.reportView = reportView;
	}

	public PayHeadSummaryServerReport(IFinanceReport<PayHeadSummary> reportView, String type) {
		this.reportView = reportView;
		this.type = type;
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { messages.name(), messages.amount() };
	}

	@Override
	public String getTitle() {
		return messages.payHeadSummaryReport();
	}

	@Override
	public String[] getColunms() {
		return new String[] { messages.name(), messages.amount() };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public void processRecord(PayHeadSummary record) {
		if (sectionDepth == 0) {
			this.sectionName = record.getPayHeadName();
			addSection(new String[] { sectionName }, new String[] { "" },
					new int[] { 1 });
		} else if (sectionDepth == 1) {
			// No need to do anything, just allow adding this record
			if (!sectionName.equals(record.getPayHeadName())) {
				endSection();
			} else {
				return;
			}
		}
		// Go on recursive calling if we reached this place
		processRecord(record);

	}

	@Override
	public Object getColumnData(PayHeadSummary record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getEmployeeName();
		case 1:
			return record.getPayHeadAmount();
		}
		return null;
	}

	@Override
	public ClientFinanceDate getStartDate(PayHeadSummary obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(PayHeadSummary obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {

	}

	@Override
	public void resetVariables() {
		this.sectionDepth = 0;
		this.sectionName = "";
	}
}
