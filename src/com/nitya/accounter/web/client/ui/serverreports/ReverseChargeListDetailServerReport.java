package com.nitya.accounter.web.client.ui.serverreports;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.core.reports.BaseReport;
import com.nitya.accounter.web.client.core.reports.ReverseChargeListDetail;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.reports.IFinanceReport;

public class ReverseChargeListDetailServerReport extends
		AbstractFinaneReport<ReverseChargeListDetail> {

	private String sectionName;

	public ReverseChargeListDetailServerReport(
			IFinanceReport<ReverseChargeListDetail> reportView) {
		this.reportView = reportView;
	}

	public ReverseChargeListDetailServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public Object getColumnData(ReverseChargeListDetail record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return Utility.getTransactionName(record.getTransactionType());
		case 1:
			return getDateByCompanyType(record.getDate());
		case 2:
			return record.getNumber();
		case 3:
			return record.getName();
		case 4:
			return record.getMemo();
		case 5:
			return record.getAmount();
		case 6:
			if (record.isPercentage())
				return record.getSalesPrice() + "%";
			else
				return record.getSalesPrice();
		}
		return null;

	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
				COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_TEXT };
	}

	@Override
	public String[] getColunms() {
		return new String[] { getMessages().date(), getMessages().noDot(),
				getMessages().name(), getMessages().memo(),
				getMessages().amount(), getMessages().salesPrice() };
	}

	@Override
	public String getTitle() {
		return getMessages().reverseChargeListDetail();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// if (this.financeTool == null)
		// return;
		// resetVariables();
		// try {
		// onSuccess(this.financeTool.getReverseChargeListDetailReport(
		// navigateObjectName, start, end));
		// } catch (DAOException e) {
		// } catch (ParseException e) {
		// }
	}

	@Override
	public void processRecord(ReverseChargeListDetail record) {
		if (sectionDepth == 0) {
			addSection("", getMessages().total(), new int[] { 5 });
		} else if (sectionDepth == 1) {
			this.sectionName = record.getName();
			addSection(sectionName, "", new int[] { 5 });
			return;
		} else if (sectionDepth == 2) {
			// No need to do anything, just allow adding this record
			if (!sectionName.equals(record.getName())) {
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
		// gridhtml = gridhtml.replaceAll(headerhtml, "");
		// headerhtml = headerhtml.replaceAll("td", "th");
		// headerhtml = headerhtml.substring(headerhtml.indexOf("<tr "),
		// headerhtml.indexOf("</tbody>"));
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
		// generateReportPDF(this.getTitle(), gridhtml, dateRangeHtml);
	}

	@Override
	public ClientFinanceDate getEndDate(ReverseChargeListDetail obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(ReverseChargeListDetail obj) {
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
		// if (index == 0)
		// return 200;
		// else
		if (index == 3)
			return 150;
		else if (index == 5 || index == 6)
			return 150;
		else if (index == 4)
			return 100;
		else if (index == 1 || index == 2)
			return 80;
		else
			return -1;
	}

	public int sort(ReverseChargeListDetail obj1, ReverseChargeListDetail obj2,
			int col) {
		int ret = obj1.getName().toLowerCase()
				.compareTo(obj2.getName().toLowerCase());
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
			return UIUtils.compareToInt(obj1.getNumber(), obj2.getNumber());
		case 3:
			return obj1.getName().toLowerCase()
					.compareTo(obj2.getName().toLowerCase());
		case 4:
			return obj1.getMemo().toLowerCase()
					.compareTo(obj2.getMemo().toLowerCase());
		case 5:
			return UIUtils.compareDouble(obj1.getAmount(), obj2.getAmount());
		case 6:
			return UIUtils.compareDouble(obj1.getSalesPrice(),
					obj2.getSalesPrice());
		}
		return 0;
	}

	@Override
	public void resetVariables() {
		this.sectionDepth = 0;
		this.sectionName = "";
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { getMessages().date(), getMessages().noDot(),
				getMessages().name(), getMessages().memo(),
				getMessages().amount(), getMessages().salesPrice() };
	}
	
}
