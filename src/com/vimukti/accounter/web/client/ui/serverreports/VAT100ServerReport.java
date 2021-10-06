package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.VATSummary;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;
import com.vimukti.accounter.web.client.ui.reports.ISectionHandler;
import com.vimukti.accounter.web.client.ui.reports.Section;

public class VAT100ServerReport extends AbstractFinaneReport<VATSummary> {
	private String sectionName = "";
	protected Double box3amount = 0.0D;
	protected Double box4amount = 0.0D;

	public VAT100ServerReport(long startDate, long endDate, int generationType) {
		super(startDate, endDate, generationType);
	}

	public VAT100ServerReport(IFinanceReport<VATSummary> reportView) {
		this.reportView = reportView;
	}

	@Override
	public Object getColumnData(VATSummary record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getVatReturnEntryName();
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
	public String getTitle() {
		return getMessages().vat100();
	}

	@Override
	public void makeReportRequest(String vatAgency,
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		row = -1;
		this.sectionName = "";
		// FinanceApplication.createReportService().getVAT100Report(vatAgency,
		// startDate.getTime(), endDate.getTime(), this);

	}

	@Override
	public String getDefaultDateRange() {
		return getMessages().financialYearToDate();
	}

	@Override
	public void processRecord(VATSummary record) {
		// if (this.handler == null)
		// iniHandler();
		if (this.row == -1) {
			// this.sectionName = "";
			// addSection("",
			// getMessages().box5NetVATToPayOrReclaimIfNegative(),
			// new int[] {});
			row = 0;
			this.sectionName = getMessages().vatDue();
			addSection(this.sectionName, "", new int[] {});

			// } else if (this.row < 4) {
			// row = row + 1;
			// if (row == 3) {
			// box4amount = record.getValue();
			// // end vat Due section
			// endSection();
			// return;
			// }
			// if (row == 4) {
			// // end net VAT pay section
			// endSection();
			// }
			// return;
			// } else {
			// return;
		}
		// Go on recursive calling if we reached this place
		// processRecord(record);

	}

	protected void iniHandler() {

		this.handler = new ISectionHandler<VATSummary>() {

			@Override
			public void OnSectionAdd(Section<VATSummary> section) {
				if (section.footer
						.equals("BOX 5 Net VAT to pay(or reclaim if negative)")) {
					section.data[0] = "";
				}
			}

			@Override
			public void OnSectionEnd(Section<VATSummary> section) {

				if (section.footer.equals(getMessages().box3TotalVATDue())) {
					box3amount = Double.valueOf(section.data[1].toString());
				}
				if (section.footer.equals(getMessages()
						.box5NetVATToPayOrReclaimIfNegative())) {
					section.data[1] = box3amount - box4amount;
					sectionDepth = 1;
				}

			}
		};

	}

	@Override
	public void makeReportRequest(long start, long end) {
		// if (this.financeTool == null)
		// return;
		// resetVariables();
		// AccounterReportServiceImpl reportsSerive = new
		// AccounterReportServiceImpl() {
		// @Override
		// protected IFinanceTool getFinanceTool()
		// throws InvaliedSessionException {
		// return this.financeTool;
		// }
		// };
		// // if (navigateObjectName == null) {
		// //
		// // onSuccess(fsdfsd.getVAT100Report(start, end));
		// // } else {
		// onSuccess(reportsSerive.getVAT100Report(navigateObjectName, start,
		// end));
		// // }
	}

	public void print() {

		// if (isMSIEBrowser()) {
		// printDataForIEBrowser();
		// } else
		// printDataForOtherBrowser();
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
		// return obj1.getName().compareTo(obj2.getName());
		// case 1:
		// return compareDouble(obj1.getValue(), obj2.getValue());
		// }
		return 0;
	}

	@Override
	public void resetVariables() {
		this.sectionName = "";
		this.row = -1;
		sectionDepth = 0;
	}

	@Override
	public int getColumnWidth(int index) {
		if (index == 1)
			return 300;
		else
			return -1;
	}

}
