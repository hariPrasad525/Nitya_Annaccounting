package com.nitya.accounter.web.client.ui.serverreports;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.reports.VATSummary;
import com.nitya.accounter.web.client.ui.reports.IFinanceReport;
import com.nitya.accounter.web.client.ui.reports.ISectionHandler;
import com.nitya.accounter.web.client.ui.reports.Section;

public class PriorVATReturnsServerReport extends
		AbstractFinaneReport<VATSummary> {

	private String sectionName = "";
	public int row = -1;
	protected Double box3amount = 0.0D;
	protected Double box4amount = 0.0D;

	public PriorVATReturnsServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
		isVATSummaryReport = true;
	}

	public PriorVATReturnsServerReport(IFinanceReport<VATSummary> reportView) {
		this.reportView = reportView;
		isVATSummaryReport = true;
	}

	@Override
	public Object getColumnData(VATSummary record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getName();
		case 1:
			return record.getValue();
		}
		return null;
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {

		// if (getStartDate().getTime() == 0
		// || getEndDate().getTime() == 0) {
		return new String[] { "", " " };
		// }
		// return new String[] {
		// "",
		// UIUtils.dateFormat(getStartDate()) + "-"
		// + UIUtils.getDateByCompanyType(getEndDate()) };
	}

	@Override
	public String[] getDynamicHeaders() {

		return new String[] { "", getDateByCompanyType(getEndDate()) };

		// return new String[] {
		// "",
		// UIUtils.getDateByCompanyType(getStartDate()) + "-"
		// + UIUtils.getDateByCompanyType(getEndDate()) };

	}

	@Override
	public String getTitle() {
		return getMessages().priorVATReturns();
	}

	@Override
	public void makeReportRequest(String vatAgency, ClientFinanceDate end) {
		this.row = -1;
		// FinanceApplication.createReportService().getPriorReturnVATSummary(
		// vatAgency, end.getTime(), this);

	}

	@Override
	public void processRecord(VATSummary record) {
		if (this.handler == null)
			iniHandler();
		if (this.row == -1) {
			this.sectionName = "";
			addSection("", getMessages()
					.netVATToPayOrReclaimIfNegativeBOX5(), new int[] {});

			this.sectionName = getMessages().vatDue();
			addSection(this.sectionName, getMessages()
					.totalVATDueBOX3(), new int[] { 1 });
			row = 0;
		} else if (this.row < 4) {
			row = row + 1;
			if (row == 3) {
				// end vat Due section
				box4amount = record.getValue();
				endSection();
				return;
			}
			if (row == 4) {
				// end net VAT pay section
				endSection();
			}

			return;
		} else {
			return;
		}
		// Go on recursive calling if we reached this place
		processRecord(record);

	}

	protected void iniHandler() {

		this.handler = new ISectionHandler<VATSummary>() {

			@Override
			public void OnSectionAdd(Section<VATSummary> section) {
				if (section.title == VATSummary.UK_BOX5_NET_VAT) {
					section.data[0] = "";
				}
			}

			@Override
			public void OnSectionEnd(Section<VATSummary> section) {

				if (section.footer.equals(VATSummary.UK_BOX3_TOTAL_OUTPUT)) {
					box3amount = Double.valueOf(section.data[1].toString());
				}
				if (section.footer.equals(VATSummary.UK_BOX5_NET_VAT)) {
					section.data[1] = box3amount - box4amount;
				}

			}
		};

	}

	@Override
	public void makeReportRequest(long start, long end) {
		// if (this.financeTool == null)
		// return;
		// initValues();
		// AccounterReportServiceImpl fsdfsd = new AccounterReportServiceImpl()
		// {
		// @Override
		// protected IFinanceTool getFinanceTool()
		// throws InvaliedSessionException {
		// return this.financeTool;
		// }
		// };
		// onSuccess(fsdfsd.getPriorReturnVATSummary(navigateObjectName, end));
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
		// String firsRow = "<tr class=\"ReportGridRow\">" + "" + "</tr>";
		// headerhtml = headerhtml + firsRow;
		//
		// gridhtml = gridhtml.replaceAll(firsRow, headerhtml);
		// gridhtml = gridhtml.replaceAll("<tbody>", "");
		// gridhtml = gridhtml.replaceAll("</tbody>", "");
		//
		// String dateRangeHtml = null;
		//
		// UIUtils.generateReportPDF(this.getTitle(), gridhtml, dateRangeHtml);
	}

	@Override
	public ClientFinanceDate getEndDate(VATSummary obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(VATSummary obj) {
		return obj.getStartDate();
	}

	public int sort(VATSummary obj1, VATSummary obj2, int col) {

		// switch (col) {
		// case 0:
		// return obj1.getName().toLowerCase().compareTo(
		// obj2.getName().toLowerCase());
		// case 1:
		// return UIUtils.compareDouble(obj1.getValue(), obj2.getValue());
		// }
		return 0;
	}

	@Override
	public void resetVariables() {
		this.sectionDepth = 0;
		this.sectionName = "";
		this.row = -1;
	}

	@Override
	public int getColumnWidth(int index) {
		if (index == 1) {
			return 300;
		}
		return -1;
	}
	
}
