package com.nitya.accounter.web.client.ui.serverreports;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.core.reports.VATItemDetail;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.reports.IFinanceReport;

public class VATItemDetailServerReport extends
		AbstractFinaneReport<VATItemDetail> {
	private String sectionName;
	private String name;

	public VATItemDetailServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	public VATItemDetailServerReport(IFinanceReport<VATItemDetail> reportView) {
		this.reportView = reportView;
	}

	@Override
	public Object getColumnData(VATItemDetail record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return Utility.getTransactionName(record.getTransactionType());
		case 1:
			return getDateByCompanyType(record.getDate());
		case 2:
			return record.getTransactionNumber();
		case 3:
			return record.getName();
		case 4:
			return record.getMemo();
		case 5:
			return record.getAmount();
		}
		return null;
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
				COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		return new String[] { getMessages().type(), getMessages().date(),
				getMessages().number(), getMessages().name(),
				getMessages().memo(), getMessages().amount() };
	}

	@Override
	public ClientFinanceDate getEndDate(VATItemDetail obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(VATItemDetail obj) {
		return obj.getStartDate();
	}

	@Override
	public String getTitle() {
		return getMessages().vatItemDetail();
	}

	@Override
	public int getColumnWidth(int index) {
		if (index == 2)
			return 65;
		else if (index == 3)
			return 150;
		else if (index == 4)
			return 100;
		else if (index == 1)
			return 85;
		else if (index == 5)
			return 125;
		return 210;
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// if (this.financeTool == null)
		// return;
		// resetVariables();
		// try {
		// try {
		// onSuccess(this.financeTool.getVATItemDetailReport(start, end));
		// } catch (ParseException e) {
		// }
		// } catch (DAOException e) {
		// }
	}

	@Override
	public void processRecord(VATItemDetail record) {
		// VATItemSummary itemList = (VATItemSummary) data;
		// if (sectionDepth == 0) {
		// addSection("", getMessages().total(), new int[] { 5 });
		// } else
		if (sectionDepth == 0) {
			this.sectionName = record.getName();
			this.name = record.getName();
			addSection(sectionName, getMessages().total(), new int[] { 5 });
		} else if (sectionDepth == 1) {
			// No need to do anything, just allow adding this record
			if (!name.equals(record.getName())) {
				endSection();
			} else {
				return;
			}
		}
		// Go on recursive calling if we reached this place
		processRecord(record);

	}

	public void print() {
		// String gridhtml = grid.toString();
		// String headerhtml = grid.getHeader();
		// String footerhtml = grid.getFooter();
		//
		// gridhtml = gridhtml.replaceAll(headerhtml, "");
		// gridhtml = gridhtml.replaceAll(footerhtml, "");
		// headerhtml = headerhtml.replaceAll("td", "th");
		// headerhtml = headerhtml.substring(headerhtml.indexOf("<tr "),
		// headerhtml.indexOf("</tbody>"));
		// footerhtml = footerhtml.substring(footerhtml.indexOf("<tr>"),
		// footerhtml.indexOf("</tbody"));
		// footerhtml = footerhtml.replaceAll("<tr>",
		// "<tr class=\"listgridfooter\">");
		//
		// String firsRow = "<tr class=\"ReportGridRow\">"
		// + "" + "</tr>";
		// String lastRow = "<tr class=\"ReportGridRow\">"
		// + grid.rowFormatter.getElement(grid.getRowCount() - 1)
		// .getInnerHTML() + "</tr>";
		//
		// headerhtml = headerhtml + firsRow;
		// footerhtml = lastRow + footerhtml;
		//
		// gridhtml = gridhtml.replace(firsRow, headerhtml);
		// gridhtml = gridhtml.replace(lastRow, footerhtml);
		// gridhtml = gridhtml.replaceAll("<tbody>", "");
		// gridhtml = gridhtml.replaceAll("</tbody>", "");
		//
		// String dateRangeHtml =
		// "<div style=\"font-family:sans-serif;\"><strong>"
		// + this.getStartDate()
		// + " - "
		// + this.getEndDate() + "</strong></div>";
		//
		// generateReportPDF(this.getTitle(), gridhtml,
		// dateRangeHtml);
	}

	public int sort(VATItemDetail obj1, VATItemDetail obj2, int col) {
		int ret = obj1.getName().compareTo(obj2.getName());
		if (ret != 0) {
			return ret;
		}

		switch (col) {
		case 0:
			return UIUtils.compareInt(obj1.getTransactionType(),
					obj2.getTransactionType());
		case 1:
			return obj1.getDate().compareTo(obj2.getDate());
		case 2:
			return obj1.getTransactionNumber().compareTo(
					obj2.getTransactionNumber());
		case 3:
			return obj1.getName().compareTo(obj2.getName());
		case 4:
			return obj1.getMemo().compareTo(obj2.getMemo());
		case 5:
			return UIUtils.compareDouble(obj1.getAmount(), obj2.getAmount());
		}
		return 0;
	}

	@Override
	public void resetVariables() {
		this.sectionDepth = 0;
		this.sectionName = "";
	}

	@Override
	public boolean isWiderReport() {
		return true;
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { getMessages().type(), getMessages().date(),
				getMessages().number(), getMessages().name(),
				getMessages().memo(), getMessages().amount() };
	}

}
