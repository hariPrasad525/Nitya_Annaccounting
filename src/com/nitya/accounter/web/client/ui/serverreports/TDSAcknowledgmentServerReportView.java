package com.nitya.accounter.web.client.ui.serverreports;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.reports.TDSAcknowledgmentsReport;
import com.nitya.accounter.web.client.ui.reports.IFinanceReport;
import com.nitya.accounter.web.client.util.DayAndMonthUtil;

public class TDSAcknowledgmentServerReportView extends
		AbstractFinaneReport<TDSAcknowledgmentsReport> {

	public TDSAcknowledgmentServerReportView(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}
	
	public TDSAcknowledgmentServerReportView(
			IFinanceReport<TDSAcknowledgmentsReport> reportView) {
		this.reportView = reportView;
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { messages.acknowledgmentNo(), messages.formType(),
				messages.financialYear(), messages.period(), messages.date() };
	}

	@Override
	public String getTitle() {
		return messages.tdsAcknowledgmentsReport();
	}

	@Override
	public String[] getColunms() {
		return new String[] { messages.acknowledgmentNo(), messages.formType(),
				messages.financialYear(), messages.period(), messages.date() };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE };
	}

	@Override
	public void processRecord(TDSAcknowledgmentsReport record) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getColumnData(TDSAcknowledgmentsReport record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getAckNo();
		case 1:
			return getFormTypes().get(record.getFormType() - 1);
		case 2:
			return Integer.toString(record.getFinancialYearStart()) + "-"
					+ Integer.toString(record.getFinancialYearEnd());
		case 3:
			return getFinancialQuatersList().get(record.getQuater() - 1);
		case 4:
			return getDateByCompanyType(new ClientFinanceDate(record.getDate()));
		}
		return null;
	}

	private List<String> getFormTypes() {
		ArrayList<String> list = new ArrayList<String>();

		list.add("26Q");
		list.add("27Q");
		list.add("27EQ");
		return list;
	}

	public List<String> getFinancialQuatersList() {
		ArrayList<String> list = new ArrayList<String>();

		list.add("Q1" + " " + DayAndMonthUtil.apr() + " - "
				+ DayAndMonthUtil.jun());
		list.add("Q2" + " " + DayAndMonthUtil.jul() + " - "
				+ DayAndMonthUtil.sep());
		list.add("Q3" + " " + DayAndMonthUtil.oct() + " - "
				+ DayAndMonthUtil.dec());
		list.add("Q4" + " " + DayAndMonthUtil.jan() + " - "
				+ DayAndMonthUtil.mar());

		return list;
	}

	@Override
	public ClientFinanceDate getStartDate(TDSAcknowledgmentsReport obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(TDSAcknowledgmentsReport obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub

	}

}
