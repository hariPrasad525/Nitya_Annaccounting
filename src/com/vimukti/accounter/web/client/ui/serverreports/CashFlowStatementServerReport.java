package com.vimukti.accounter.web.client.ui.serverreports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;
import com.vimukti.accounter.web.client.ui.reports.ISectionHandler;
import com.vimukti.accounter.web.client.ui.reports.Section;

public class CashFlowStatementServerReport extends
		AbstractFinaneReport<TrialBalance> {
	private List<String> types = new ArrayList<String>();
	private List<String> sectiontypes = new ArrayList<String>();
	private String curentParent;

	private double netIncome;
	private double totaloperating = 0.0D;
	private double totalInvesting = 0.0D;
	private double totalFinaning = 0.0D;

	// private double cashBegigginperiod;

	public CashFlowStatementServerReport(IFinanceReport<TrialBalance> reportView) {
		this.reportView = reportView;
	}

	public CashFlowStatementServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public String getDefaultDateRange() {
		return getMessages().financialYearToDate();
	}

	@Override
	public Object getColumnData(TrialBalance record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getAccountNumber() != null ? record
					.getAccountNumber() + "-" + record.getAccountName() : ""
					+ record.getAccountName();
		case 1:
			return record.getAmount();
		default:
			return null;
		}

	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		// String startDate = DateTimeFormat.getFormat("MM/DD/YY").format(
		// getStartDate());
		// String endDate = DateTimeFormat.getFormat("MM/DD/YY").format(
		// getEndDate());
		// return new String[] { "", startDate + " - " + endDate };
		return new String[] {
				"",
				getDateByCompanyType(getStartDate()) + "  -  "
						+ getDateByCompanyType(getEndDate()) };
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] {
				"",
				getDateByCompanyType(getStartDate()) + "  -  "
						+ getDateByCompanyType(getEndDate()) };
	}

	@Override
	public String getTitle() {
		// return FinanceApplication.constants().profitAndLoss();
		return getMessages().cashFlowReport();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// if (this.financeTool == null)
		// return;
		// initValues();
		// try {
		// onSuccess(this.financeTool.getCashFlowReport(start, end));
		// } catch (DAOException e) {
		// }
		//
		// }
		//
		// private void initValues() {
	}

	@Override
	public void processRecord(TrialBalance record) {
		if (sectionDepth == 0) {
			addTypeSection(" ", getMessages().cashAtEndOfPeriod());
		}
		if (record.getAccountName().equals(getMessages().netIncome())) {
			addOperatingTypes(record);
			this.netIncome = record.getAmount();
			return;
		}
		// cashBegigginperiod = record.getCashAtBeginningOfPeriod();
		if (this.handler == null)
			iniHandler();

		if (record.getCashFlowCategory() == ClientAccount.CASH_FLOW_CATEGORY_OPERATING) {
			addOperatingTypes(record);
		} else if (record.getCashFlowCategory() == ClientAccount.CASH_FLOW_CATEGORY_FINANCING) {
			addFinancingTypes(record);
		} else if (record.getCashFlowCategory() == ClientAccount.CASH_FLOW_CATEGORY_INVESTING) {
			addInvestingTypes(record);
		}
		if (closePrevSection(record.getParentAccount() == 0 ? record
				.getAccountName() : getAccountNameById(record
				.getParentAccount()))) {
			processRecord(record);
		} else {
			addSection(record);
			return;
		}

	}

	private void iniHandler() {
		this.handler = new ISectionHandler<TrialBalance>() {

			@Override
			public void OnSectionAdd(Section<TrialBalance> section) {

			}

			@Override
			public void OnSectionEnd(Section<TrialBalance> section) {

				// if (section.footer == "AR-NI-NC") {
				// section.data[1] = netIncome
				// + +Double.valueOf(section.data[1].toString());
				// }
				if (section.title == getMessages().operatingActivities()) {
					totaloperating = Double.valueOf(section.data[1].toString());
				}
				if (section.title == getMessages().investingActivities()) {
					totalInvesting = Double.valueOf(section.data[1].toString());
				}
				if (section.title == getMessages().financingActivities()) {
					totalFinaning = Double.valueOf(section.data[1].toString());
				}
				if (section.footer == getMessages().cashAtEndOfPeriod()) {
					CashFlowStatementServerReport.this.grid
							.addRow(null,
									1,
									new Object[] {
											getMessages()
													.netCashChangeForThePeriod(),
											(totalFinaning + totaloperating + totalInvesting) },
									true, true, true);
					// CashFlowStatementServerReport.this.grid
					// .addRow(null,
					// 1,
					// new Object[] {
					// getMessages()
					// .cashAtBeginningOfThePeriod(),
					// cashBegigginperiod }, true, true,
					// true);
					section.data[1] = (totalFinaning + totaloperating + totalInvesting);
					// + cashBegigginperiod);

				}
			}

		};

	}

	public String getAccountNameById(long id) {
		for (TrialBalance balance : this.records)
			if (balance.getAccountId() != 0)
				if (balance.getAccountId() == id)
					return balance.getAccountName();
		return null;
	}

	public void closeAllSection() {
		for (int i = types.size() - 1; i > 0; i--) {
			closeSection(i);
		}
	}

	public void closeOtherSections() {
		for (int i = types.size() - 1; i > 0; i--) {
			closePrevSection(types.get(i));
		}
	}

	public void addOperatingTypes(TrialBalance record) {
		if (!sectiontypes.contains(getMessages().operatingActivities())) {
			closeAllSection();
			addTypeSection(getMessages().operatingActivities(), getMessages()
					.netCashProvidedByOperatingActivities());
		}
		if (record.getAccountName().equals(getMessages().netIncome()))
			return;
		if (!sectiontypes.contains(getMessages()
				.adjustmentsToReconcileNetIncomeToNetCash())) {
			addTypeSection(getMessages()
					.adjustmentsToReconcileNetIncomeToNetCash(), getMessages()
					.endOfARNINC());
		}

	}

	public void addInvestingTypes(TrialBalance record) {
		if (!sectiontypes.contains(getMessages().investingActivities())) {
			closeAllSection();
			addTypeSection(getMessages().investingActivities(), getMessages()
					.netCashProvidedByinvestingActivities());
		}
	}

	public void addFinancingTypes(TrialBalance record) {
		if (!sectiontypes.contains(getMessages().financingActivities())) {
			closeAllSection();
			addTypeSection(getMessages().financingActivities(), getMessages()
					.netCashProvidedByfinancingActivities());
		}
	}

	public boolean isParent(TrialBalance record) {
		for (TrialBalance balance : this.records) {
			if (balance.getParentAccount() != 0)
				if (balance.getParentAccount() == record.getAccountId())
					return true;
		}
		return false;
	}

	public boolean addSection(TrialBalance record) {
		if (isParent(record)) {
			types.add(record.getAccountName());
			curentParent = record.getAccountName();
			addSection(
					record.getAccountNumber() + "-" + record.getAccountName(),
					getMessages().total() + record.getAccountName(),
					new int[] { 1 });
			return true;
		}
		return false;
	}

	public boolean closePrevSection(String title) {
		if (curentParent != null && curentParent != "")
			if (!title.equals(curentParent)) {
				if (!sectiontypes.contains(curentParent)) {
					types.remove(types.size() - 1);
					if (types.size() > 0) {
						curentParent = types.get(types.size() - 1);
					}
					endSection();
					return true;
				}
			}
		return false;
	}

	public void closeSection(int index) {
		types.remove(index);
		curentParent = "";
		endSection();
	}

	/**
	 * add Type Section
	 * 
	 * @param title
	 */
	public void addTypeSection(String title, String bottomTitle) {
		if (!sectiontypes.contains(title)) {
			addSection(title, bottomTitle, new int[] { 1 });
			types.add(title);
			sectiontypes.add(title);
		}
	}

	public boolean isExist(String title) {
		if (!sectiontypes.contains(title))
			return true;
		return false;
	}

	/**
	 * Reset Variables in Report,
	 */
	@Override
	public void resetVariables() {
		this.types.clear();
		this.sectiontypes.clear();
		curentParent = "";
		totaloperating = 0.0D;
		totalInvesting = 0.0D;
		totalFinaning = 0.0D;
		// cashBegigginperiod = 0.0D;
	}

	public void print() {

	}

	@Override
	public ClientFinanceDate getEndDate(TrialBalance obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(TrialBalance obj) {
		return obj.getStartDate();

	}

	@Override
	protected String getPreviousReportDateRange(Object object) {
		return ((TrialBalance) object).getDateRange();
	}

	@Override
	protected ClientFinanceDate getPreviousReportStartDate(Object object) {
		return ((TrialBalance) object).getStartDate();
	}

	@Override
	protected ClientFinanceDate getPreviousReportEndDate(Object object) {
		return ((TrialBalance) object).getEndDate();
	}

	@Override
	public int getColumnWidth(int index) {
		if (index == 1)
			return 400;
		else
			return -1;
	}

	// private void printDataForIEBrowser() {
	// String gridhtml = grid.toString();
	// String headerhtml = grid.getHeader();
	// String footerhtml = grid.getFooter();
	//
	// gridhtml = gridhtml.replaceAll("\r\n", "");
	// headerhtml = headerhtml.replaceAll("\r\n", "");
	// footerhtml = footerhtml.replaceAll("\r\n", "");
	//
	// gridhtml = gridhtml.replaceAll(headerhtml, "");
	// gridhtml = gridhtml.replaceAll(footerhtml, "");
	// headerhtml = headerhtml.replaceAll("TD", "TH");
	// headerhtml = headerhtml.substring(headerhtml.indexOf("<TR "),
	// headerhtml.indexOf("</TBODY>"));
	// footerhtml = footerhtml.substring(footerhtml.indexOf("<TR>"),
	// footerhtml.indexOf("</TBODY"));
	// footerhtml = footerhtml.replaceAll("<TR>", "<TR class=listgridfooter>");
	//
	// String firsRow = "<TR class=ReportGridRow>" + "" + "</TR>";
	// String lastRow = "<TR class=ReportGridRow>" + "</TR>";
	//
	// firsRow = firsRow.replaceAll("\r\n", "");
	// lastRow = lastRow.replaceAll("\r\n", "");
	//
	// headerhtml = headerhtml + firsRow;
	// footerhtml = lastRow + footerhtml;
	//
	// gridhtml = gridhtml.replace(firsRow, headerhtml);
	// gridhtml = gridhtml.replace(lastRow, footerhtml);
	// gridhtml = gridhtml.replaceAll("<TBODY>", "");
	// gridhtml = gridhtml.replaceAll("</TBODY>", "");
	//
	// String dateRangeHtml = null;
	//
	// generateReportPDF(this.getTitle(), gridhtml, dateRangeHtml);
	// }
	//
	// private void printDataForOtherBrowser() {
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
	// String firsRow = "<tr class=\"ReportGridRow\">" + "" + "</tr>";
	// String lastRow = "<tr class=\"ReportGridRow\">" + "</tr>";
	//
	// headerhtml = headerhtml + firsRow;
	// footerhtml = lastRow + footerhtml;
	//
	// gridhtml = gridhtml.replace(firsRow, headerhtml);
	// gridhtml = gridhtml.replace(lastRow, footerhtml);
	// gridhtml = gridhtml.replaceAll("<tbody>", "");
	// gridhtml = gridhtml.replaceAll("</tbody>", "");
	//
	// String dateRangeHtml = null;
	//
	// generateReportPDF(this.getTitle(), gridhtml,
	// dateRangeHtml);
	// }

}