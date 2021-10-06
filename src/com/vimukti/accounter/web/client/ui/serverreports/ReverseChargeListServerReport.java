package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.ReverseChargeList;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

public class ReverseChargeListServerReport extends
		AbstractFinaneReport<ReverseChargeList> {

	public ReverseChargeListServerReport(
			IFinanceReport<ReverseChargeList> reportView) {
		this.reportView = reportView;
	}

	public ReverseChargeListServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public Object getColumnData(ReverseChargeList record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getName();
		case 1:
			return record.getAmount();
		}
		return null;
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		if (getStartDate().getDate() == 0 || getEndDate().getDate() == 0) {
			return new String[] { "", " " + "-" + " " };
		}
		return new String[] { "", getDateByCompanyType(getEndDate()) };
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] {
				"",
				getDateByCompanyType(getStartDate()) + "-"
						+ getDateByCompanyType(getEndDate()) };

	};

	@Override
	public String getTitle() {
		return getMessages().reverseChargeList();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// if (this.financeTool == null)
		// return;
		// resetVariables();
		// try {
		// onSuccess(this.financeTool.getReverseChargeListReport(start, end));
		// } catch (DAOException e) {
		// } catch (ParseException e) {
		// }

	}

	@Override
	public void processRecord(ReverseChargeList record) {
		if (sectionDepth == 0) {
			addSection("", getMessages().total(), new int[] { 1 });
		} else if (sectionDepth == 1) {
			// No need to do anything, just allow adding this record

			return;

		}

	}

	public void print() {

	}

	@Override
	public ClientFinanceDate getStartDate(ReverseChargeList obj) {
		return obj.getStartDate();
	}

	@Override
	protected String getPreviousReportDateRange(Object object) {
		return ((ReverseChargeList) object).getDateRange();
	}

	@Override
	protected ClientFinanceDate getPreviousReportStartDate(Object object) {
		return ((ReverseChargeList) object).getStartDate();
	}

	@Override
	protected ClientFinanceDate getPreviousReportEndDate(Object object) {
		return ((ReverseChargeList) object).getEndDate();
	}

	@Override
	public int getColumnWidth(int index) {
		if (index == 1)
			return 400;
		else
			return -1;
	}

	public int sort(ReverseChargeList obj1, ReverseChargeList obj2, int col) {
		switch (col) {
		case 0:
			return obj1.getName().toLowerCase()
					.compareTo(obj2.getName().toLowerCase());
		case 1:
			return UIUtils.compareDouble(obj1.getAmount(), obj2.getAmount());
		}
		return 0;
	}

	@Override
	public void resetVariables() {
		this.sectionDepth = 0;
	}

	@Override
	public ClientFinanceDate getEndDate(ReverseChargeList obj) {
		// currently not using
		return null;
	}
	
}
