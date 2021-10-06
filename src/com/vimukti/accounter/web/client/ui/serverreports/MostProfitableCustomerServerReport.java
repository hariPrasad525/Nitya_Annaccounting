package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.MostProfitableCustomers;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

/**
 * 
 * @author kumar kasimala
 * 
 * 
 */
public class MostProfitableCustomerServerReport extends
		AbstractFinaneReport<MostProfitableCustomers> {

	public MostProfitableCustomerServerReport(
			IFinanceReport<MostProfitableCustomers> reportView) {

		this.reportView = reportView;
	}

	public MostProfitableCustomerServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_PERCENTAGE };
	}

	@Override
	public String[] getColunms() {
		return new String[] { Global.get().customer(),
				getMessages().invoicedAmount(), getMessages().cost(),
				getMessages().dollarMargin(), getMessages().percMargin() };
	}

	@Override
	public String getTitle() {
		return messages.mostProfitableCustomer(
				Global.get().customers());
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// if (this.financeTool == null)
		// return;
		// initValues();
		// try {
		// onSuccess(this.financeTool.getMostProfitableCustomers(start, end));
		// } catch (DAOException e) {
		// }
	}

	@Override
	public Object getColumnData(MostProfitableCustomers record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getCustomer();
		case 1:
			return record.getInvoicedAmount();
		case 2:
			return record.getCost();
		case 3:
			return record.getMargin();
		case 4:
			return record.getMarginPercentage();
		}
		return null;
	}

	@Override
	public void processRecord(MostProfitableCustomers record) {
		if (sectionDepth == 0) {
			addSection("", getMessages().total(), new int[] { 1, 2, 3, 4 });
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
		// gridhtml = gridhtml.replace(headerhtml, "");
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
		// generateReportPDF(this.getTitle(), gridhtml, dateRangeHtml);
	}

	@Override
	public ClientFinanceDate getEndDate(MostProfitableCustomers obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(MostProfitableCustomers obj) {
		return obj.getStartDate();
	}

	@Override
	public int getColumnWidth(int index) {
		if (index == 0)
			return 200;
		if (index == 1 || index == 2 || index == 3 || index == 4)
			return 150;

		if (index == 0)
			return 200;
		if (index == 1 || index == 2 || index == 3 || index == 4)
			return 150;
		else
			return -1;
	}

	public int sort(MostProfitableCustomers obj1, MostProfitableCustomers obj2,
			int col) {
		switch (col) {
		case 0:
			return obj1.getCustomer().toLowerCase()
					.compareTo(obj2.getCustomer().toLowerCase());
		case 1:
			return UIUtils.compareDouble(obj1.getInvoicedAmount(),
					obj2.getInvoicedAmount());
		case 2:
			return UIUtils.compareDouble(obj1.getCost(), obj2.getCost());
		case 3:
			return UIUtils.compareDouble(obj1.getMargin(), obj2.getMargin());
		case 4:
			return UIUtils.compareDouble(obj1.getMarginPercentage(),
					obj2.getMarginPercentage());
		}
		return 0;
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { Global.get().customer(),
				getMessages().invoicedAmount(), getMessages().cost(),
				getMessages().dollarMargin(), getMessages().percMargin() };
	}

}
