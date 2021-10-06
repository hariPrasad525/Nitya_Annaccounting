package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.reports.UncategorisedAmountsReport;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

public class VATUncategorisedAmountsServerReport extends
		AbstractFinaneReport<UncategorisedAmountsReport> {

	private double balance = 0.0;

	public VATUncategorisedAmountsServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	public VATUncategorisedAmountsServerReport(
			IFinanceReport<UncategorisedAmountsReport> reportView) {
		this.reportView = reportView;
	}

	@Override
	public Object getColumnData(UncategorisedAmountsReport record,
			int columnIndex) {
		switch (columnIndex) {
		case 0:
			return Utility.getTransactionName(record.getTransactionType());
		case 1:
			if (record.getDate() != null)
				return getDateByCompanyType(record.getDate());
			else
				break;
		case 2:
			return record.getTransactionNumber();
		case 3:
			return record.getSourceName();
			// case 4:
			// return record.getMemo();
		case 4:
			balance += record.getAmount();
			return record.getAmount();
		case 5:
			return balance;
		}
		return null;
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		return new String[] { getMessages().type(),
				getMessages().date(), getMessages().no(),
				getMessages().sourceName(),
				getMessages().amount(), getMessages().balance() };
	}

	@Override
	public String getTitle() {
		return getMessages().unCategorisedVATAmountsDetail();
	}

	@Override
	public int getColumnWidth(int index) {

		if (index == 2 || index == 1)
			return 75;
		else if (index == 3)
			return 175;
		else if (index == 4 || index == 5)
			return 170;
		else
			return -1;
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// if (this.financeTool == null)
		// return;
		//
		// resetVariables();
		// try {
		// onSuccess(this.financeTool
		// .getUncategorisedAmountsReport(start, end));
		// } catch (ParseException e) {
		// }
	}

	@Override
	public void processRecord(UncategorisedAmountsReport record) {
		if (sectionDepth == 0) {
			addSection(getMessages().unCategorisedTaxAmountsDetail(),
					getMessages().unCategorisedTaxAmountsDetail(),
					new int[] { 5 });
		} else if (sectionDepth == 1) {
			return;
		}
		// Go on recursive calling if we reached this place
		processRecord(record);

	}

	public void print() {
		// String gridhtml = grid.toString();
		// String headerhtml = grid.getHeader();
		//
		// gridhtml = gridhtml.replaceAll(headerhtml, "");
		// gridhtml = gridhtml.replaceAll(grid.getFooter(), "");
		//
		// headerhtml = headerhtml.replaceAll("td", "th");
		// headerhtml = headerhtml.substring(headerhtml.indexOf("<tr "),
		// headerhtml.indexOf("</tbody>"));
		//
		// String firsRow = "<tr class=\"ReportGridRow\">" + "" + "</tr>";
		// headerhtml = headerhtml + firsRow;
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
	public ClientFinanceDate getEndDate(UncategorisedAmountsReport obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(UncategorisedAmountsReport obj) {
		return obj.getStartDate();
	}

	public int sort(UncategorisedAmountsReport obj1,
			UncategorisedAmountsReport obj2, int col) {

		switch (col) {
		case 0:
			return UIUtils.compareInt(obj1.getTransactionType(),
					obj2.getTransactionType());
		case 1:
			return obj1.getDate().compareTo(obj2.getDate());
		case 2:
			return UIUtils.compareInt(
					Integer.parseInt(obj1.getTransactionNumber()),
					Integer.parseInt(obj2.getTransactionNumber()));
		case 3:
			return obj1.getSourceName().toLowerCase()
					.compareTo(obj2.getSourceName().toLowerCase());
		case 4:
			return obj1.getMemo().toLowerCase()
					.compareTo(obj2.getMemo().toLowerCase());
		case 5:
			return UIUtils.compareDouble(obj1.getAmount(), obj2.getAmount());
		}
		return 0;
	}

	@Override
	public void resetVariables() {
		this.sectionDepth = 0;
		this.balance = 0;
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { getMessages().type(),
				getMessages().date(), getMessages().no(),
				getMessages().sourceName(),
				getMessages().amount(), getMessages().balance() };
	}
	
}
