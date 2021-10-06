package com.nitya.accounter.web.client.ui.serverreports;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.reports.VATItemSummary;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.reports.IFinanceReport;

public class VATItemSummaryServerReport extends
		AbstractFinaneReport<VATItemSummary> {

	public VATItemSummaryServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	public VATItemSummaryServerReport(IFinanceReport<VATItemSummary> reportView) {
		this.reportView = reportView;
	}

	@Override
	public Object getColumnData(VATItemSummary record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getName();
		case 1:
			return record.getTaxRate() + "%";
		case 2:
			return record.getAmount();
		case 3:
			return record.getTaxAmount();
		}
		return null;
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		return new String[] { getMessages().name(), getMessages().rate(),
				getMessages().netAmount(), getMessages().taxAmount() };
	}

	@Override
	public ClientFinanceDate getEndDate(VATItemSummary obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(VATItemSummary obj) {
		return obj.getStartDate();
	}

	@Override
	public String getTitle() {
		return getMessages().vatItemSummary();
	}

	@Override
	public int getColumnWidth(int index) {
		switch (index) {
		case 0:
			return 350;
		default:
			return -1;

		}
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// if (this.financeTool == null)
		// return;
		// resetVariables();
		// try {
		// onSuccess(this.financeTool.getVATItemSummaryReport(start, end));
		// } catch (DAOException e) {
		// } catch (ParseException e) {
		// }

	}

	@Override
	public void processRecord(VATItemSummary record) {

	}

	public void print() {

		// if (isMSIEBrowser()) {
		// printDataForIEBrowser();
		// } else
		// printDataForOtherBrowser();
	}

	public int sort(VATItemSummary obj1, VATItemSummary obj2, int col) {

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
	public String[] getDynamicHeaders() {
		return new String[] { getMessages().name(), getMessages().rate(),
				getMessages().netAmount(), getMessages().taxAmount() };
	}

}
