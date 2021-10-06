package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.Lists.DummyDebitor;
import com.vimukti.accounter.web.client.core.reports.BaseReport;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

public class ARAgingSummaryServerReport extends
		AbstractFinaneReport<DummyDebitor> {

	public ARAgingSummaryServerReport(IFinanceReport<DummyDebitor> reportView) {
		this.reportView = reportView;
	}

	public ARAgingSummaryServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public Object getColumnData(DummyDebitor record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getDebitorName();
			// case 1:
			// return record.getDebitdays_incurrent();
		case 1:
			return record.getDebitdays_in30() + record.getDebitdays_incurrent();
		case 2:
			return record.getDebitdays_in60();
		case 3:
			return record.getDebitdays_in90();
		case 4:
			return record.getDebitdays_inolder();
		case 5:
			return (record.getDebitdays_in30() + record.getDebitdays_in60()
					+ record.getDebitdays_in90()
					+ record.getDebitdays_inolder() + record
						.getDebitdays_incurrent());
		}

		return null;
	}

	@Override
	public int[] getColumnTypes() {

		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {

		return new String[] { getMessages().debtor(),
				getMessages().dayszeroto30(), getMessages().days30to60(),
				getMessages().days60to90(), getMessages().older(),
				getMessages().totalBalance() };
	}

	@Override
	public String getTitle() {
		return getMessages().arAgeingSummary();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// Get the report service and get the debitors using the given start and
		// end time.
		// // The result is set to the view using the callback onSuccess()
		// Accounter.createReportService().getDebitors(
		// new ClientFinanceDate(start), new ClientFinanceDate(end), this);
		/*
		 * FinanceApplication.createReportService().getDebitors(start.getTime(),
		 * new ClientFinanceDate().getTime(), this);
		 */
		/*
		 * AccounterReportServiceImpl reportService = new
		 * AccounterReportServiceImpl() {
		 * 
		 * @Override protected IFinanceTool getFinanceTool() throws
		 * InvaliedSessionException { return this.financeTool; } }; if
		 * (this.financeTool == null) return;
		 * 
		 * try { onSuccess(reportService.getDebitors(start, end)); } catch
		 * (InvaliedSessionException e) { e.printStackTrace(); }
		 */
	}

	@Override
	public void processRecord(DummyDebitor record) {
		if (sectionDepth == 0) {
			addSection("", getMessages().total(), new int[] { 1, 2, 3, 4, 5 });
		} else if (sectionDepth == 1) {
			return;
		}
		// Go on recursive calling if we reached this place
		processRecord(record);
	}

	public void print() {

	}

	// private void printDataForOtherBrowser() {
	// String gridhtml = grid.toString();
	// String headerhtml = grid.getHeader();
	//
	// gridhtml = gridhtml.replaceAll(headerhtml, "");
	// gridhtml = gridhtml.replaceAll(grid.getFooter(), "");
	// headerhtml = headerhtml.replaceAll("td", "th");
	// headerhtml = headerhtml.substring(headerhtml.indexOf("<tr "),
	// headerhtml.indexOf("</tbody>"));
	//
	// String firsRow = "<tr class=\"ReportGridRow depth\">"
	// + "" + "</tr>";
	// headerhtml = headerhtml + firsRow;
	//
	// gridhtml = gridhtml.replace(firsRow, headerhtml);
	// gridhtml = gridhtml.replaceAll("<tbody>", "");
	// gridhtml = gridhtml.replaceAll("</tbody>", "");
	//
	// String dateRangeHtml = null;
	//
	// generateReportPDF(this.getTitle(), gridhtml, dateRangeHtml);
	// }
	//
	// private void printDataForIEBrowser() {
	// String gridhtml = grid.toString();
	// String headerhtml = grid.getHeader();
	// String footerHtml = grid.getFooter();
	//
	// gridhtml = gridhtml.replaceAll("\r\n", "");
	// headerhtml = headerhtml.replaceAll("\r\n", "");
	// footerHtml = footerHtml.replaceAll("\r\n", "");
	//
	// gridhtml = gridhtml.replaceAll(headerhtml, "");
	// gridhtml = gridhtml.replaceAll(footerHtml, "");
	//
	// headerhtml = headerhtml.replaceAll("TD", "TH");
	// headerhtml = headerhtml.substring(headerhtml.indexOf("<TR "),
	// headerhtml.indexOf("</TBODY>"));
	//
	// String firsRow = "<TR class=\"ReportGridRow depth\">"
	// + "" + "</TR>";
	// firsRow = firsRow.replaceAll("\r\n", "");
	// headerhtml = headerhtml + firsRow;
	//
	// gridhtml = gridhtml.replace(firsRow, headerhtml);
	// gridhtml = gridhtml.replaceAll("<TBODY>", "");
	// gridhtml = gridhtml.replaceAll("</TBODY>", "");
	//
	// String dateRangeHtml = null;
	//
	// generateReportPDF(this.getTitle(), gridhtml, dateRangeHtml);
	// }

	@Override
	public ClientFinanceDate getEndDate(DummyDebitor obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(DummyDebitor obj) {
		return obj.getStartDate();
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
	public int getColumnWidth(int index) {
		switch (index) {
		case 1:
			return 110;
		case 2:
			return 110;
		case 3:
			return 110;
		case 4:
			return 110;
		case 5:
			return 110;

		default:
			return -1;
		}
	}

	public int sort(DummyDebitor obj1, DummyDebitor obj2, int col) {
		switch (col) {
		case 0:
			return obj1.getDebitorName().toLowerCase()
					.compareTo(obj2.getDebitorName().toLowerCase());

		case 1:
			return UIUtils.compareDouble(
					(obj1.getDebitdays_in30() + obj1.getDebitdays_incurrent()),
					(obj2.getDebitdays_in30() + obj2.getDebitdays_incurrent()));
		case 2:
			return UIUtils.compareDouble(obj1.getDebitdays_in60(),
					obj2.getDebitdays_in60());
		case 3:
			return UIUtils.compareDouble(obj1.getDebitdays_in90(),
					obj2.getDebitdays_in90());
		case 4:
			return UIUtils.compareDouble(obj1.getDebitdays_inolder(),
					obj2.getDebitdays_inolder());
		case 5:
			return UIUtils.compareDouble(
					(obj1.getDebitdays_in30() + obj1.getDebitdays_in60()
							+ obj1.getDebitdays_in90()
							+ obj1.getDebitdays_inolder() + obj1
							.getDebitdays_incurrent()),
					(obj2.getDebitdays_in30() + obj2.getDebitdays_in60()
							+ obj2.getDebitdays_in90()
							+ obj2.getDebitdays_inolder() + obj2
							.getDebitdays_incurrent()));
		}
		return 0;
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { getMessages().debtor(),
				getMessages().dayszeroto30(), getMessages().days30to60(),
				getMessages().days60to90(), getMessages().older(),
				getMessages().totalBalance() };
	}

}
