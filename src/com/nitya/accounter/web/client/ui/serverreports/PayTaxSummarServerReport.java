package com.nitya.accounter.web.client.ui.serverreports;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.reports.PayHeadSummary;
import com.nitya.accounter.web.client.ui.reports.IFinanceReport;

public class PayTaxSummarServerReport extends AbstractFinaneReport<PayHeadSummary> {

	private String sectionName;

	public PayTaxSummarServerReport(IFinanceReport<PayHeadSummary> reportView) {
		this.reportView = reportView;
	}
	
	@Override
	public String[] getDynamicHeaders() {
		return this.getColunms();
	}
	
	@Override
	public String getDefaultDateRange() {
		return messages.thisFinancialQuarter();
	}
	
	@Override
	public String[] getColunms() {
		return new String[] { messages.name(), messages.deductionsForEmployees(),  Global.get().messages2().deductionsFromCompany(), messages.total() };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
	}
	
	@Override
	public String getTitle() {
		return messages.payHeadSummaryReport();
	}


	@Override
	public void processRecord(PayHeadSummary record) {
		if (sectionDepth == 0) {
			this.sectionName = record.getPayHeadName();
			addSection(new String[] { sectionName }, new String[] { "" },
					new int[] { 1, 2 });
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
		case 2:
			return record.getEmployeerDeductions();
		case 3:
			return record.getEmployeerDeductions() + record.getPayHeadAmount();
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
		// TODO Auto-generated method stub
		
	}

}
