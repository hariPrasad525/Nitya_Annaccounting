package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.ECSalesList;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

public class ECSalesListServerReport extends AbstractFinaneReport<ECSalesList> {

	private String sectionName;

	public ECSalesListServerReport(IFinanceReport<ECSalesList> reportView) {
		this.reportView = reportView;
	}

	public ECSalesListServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public Object getColumnData(ECSalesList record, int columnIndex) {
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
		// if (getStartDate().getTime() == 0 || getEndDate().getTime() == 0) {
		// return new String[] { "", " " + "-" + " " };
		// }
		return new String[] {
				"",
				getDateByCompanyType(getStartDate()) + "-"
						+ getDateByCompanyType(getEndDate()) };
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] {
				"",
				getDateByCompanyType(getStartDate()) + "-"
						+ getDateByCompanyType(getEndDate()) };
	}

	@Override
	public int getColumnWidth(int index) {
		if (index == 1)
			return 175;
		else
			return -1;
	}

	@Override
	public String getTitle() {
		return getMessages().ecSalesList();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// if (this.financeTool == null)
		// return;
		// initValues();
		// try {
		// onSuccess(this.financeTool.getECSalesListReport(start, end));
		// } catch (DAOException e) {
		// } catch (ParseException e) {
		// }
	}

	@Override
	public void processRecord(ECSalesList record) {
		if (sectionDepth == 0) {
			addSection("", getMessages().total(), new int[] { 1 });
		} else if (sectionDepth == 1) {
			// No need to do anything, just allow adding this record

			return;
		}
		// Go on recursive calling if we reached this place
		// processRecord(record);

	}

	public void print() {
		// String gridhtml = grid.toString();
		// String headerhtml = grid.getHeader();
		//
		// gridhtml = gridhtml.replaceAll(headerhtml, "");
		// gridhtml = gridhtml.replaceAll(grid.getFooter(), "");
		// headerhtml = headerhtml.replaceAll("td", "th");
		// headerhtml = headerhtml.substring(headerhtml.indexOf("<tr "),
		// headerhtml.indexOf("</tbody>"));
		//
		// String firsRow = "<tr class=\"ReportGridRow depth\">" + "" + "</tr>";
		// headerhtml = headerhtml + firsRow;
		//
		// gridhtml = gridhtml.replace(firsRow, headerhtml);
		// gridhtml = gridhtml.replaceAll("<tbody>", "");
		// gridhtml = gridhtml.replaceAll("</tbody>", "");
		//
		// String dateRangeHtml =
		// "<div style=\"font-family:sans-serif;\"><strong>"
		// + this.getStartDate()
		// + " - "
		// + this.getEndDate()
		// + "</strong></div>";
		//
		// generateReportPDF(this.getTitle(), gridhtml,
		// dateRangeHtml);
	}

	@Override
	public ClientFinanceDate getEndDate(ECSalesList obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(ECSalesList obj) {
		return obj.getStartDate();
	}

	@Override
	protected String getPreviousReportDateRange(Object object) {
		return ((ECSalesList) object).getDateRange();
	}

	@Override
	protected ClientFinanceDate getPreviousReportStartDate(Object object) {
		return ((ECSalesList) object).getStartDate();
	}

	@Override
	protected ClientFinanceDate getPreviousReportEndDate(Object object) {
		return ((ECSalesList) object).getEndDate();
	}

	public int sort(ECSalesList obj1, ECSalesList obj2, int col) {
		switch (col) {
		case 0:
			return obj1.getName().toLowerCase().compareTo(
					obj2.getName().toLowerCase());
		case 1:
			return UIUtils.compareDouble(obj1.getAmount(), obj2.getAmount());
		}
		return 0;
	}

	@Override
	public void resetVariables() {
		this.sectionDepth = 0;
		this.sectionName = "";
	}

}
