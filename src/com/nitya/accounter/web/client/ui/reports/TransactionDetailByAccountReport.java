package com.nitya.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.ClientBox;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientTAXReturn;
import com.nitya.accounter.web.client.core.ClientTAXReturnEntry;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.CountryPreferences;
import com.nitya.accounter.web.client.core.NumberReportInput;
import com.nitya.accounter.web.client.core.reports.TransactionDetailByAccount;
import com.nitya.accounter.web.client.core.reports.TrialBalance;
import com.nitya.accounter.web.client.core.reports.VATSummary;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.serverreports.TransactionDetailByAccountServerReport;
import com.nitya.accounter.web.client.util.Countries;

public class TransactionDetailByAccountReport extends
		AbstractReportView<TransactionDetailByAccount> {
	private String currentsectionName = "";
	private int reportType = REPORT_TYPE_TRANSACTIONDETAILBYACCOUNT;

	public TransactionDetailByAccountReport() {
		this.serverReport = new TransactionDetailByAccountServerReport(this);
	}

	@Override
	public void OnRecordClick(TransactionDetailByAccount record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			int type = getType(record);
			if (type == ClientTransaction.TYPE_TAX_RETURN) {
				openTaxReturnView(record.getTransactionId());
			} else {
				ReportsRPC.openTransactionView(getType(record),
						record.getTransactionId());
			}
		}
	}

	private void openTaxReturnView(long transactionId) {
		AccounterAsyncCallback<ClientTAXReturn> callback = new AccounterAsyncCallback<ClientTAXReturn>() {

			public void onException(AccounterException caught) {
				Accounter.showMessage(messages.sessionExpired());
			}

			public void onResultSuccess(ClientTAXReturn result) {
				if (result != null) {
					CountryPreferences countryPreferences = Accounter
							.getCompany().getCountryPreferences();
					if (Accounter.getCompany().getCountry()
							.equals(Countries.UNITED_KINGDOM)
							&& countryPreferences.isVatAvailable()) {
						List<VATSummary> summaries = getVatSummaires(result);
						new VATSummaryReportAction().run(summaries, false);
					} else {
						List<ClientTAXReturnEntry> taxEntries = null;
						List<TAXItemDetail> details = new ArrayList<TAXItemDetail>();
						taxEntries = result.getTaxReturnEntries();
						details = getTaxItemDetails(taxEntries);
						TAXReportsAction taxItemDetailReportAction = TAXReportsAction
								.taxItemDetail();
						taxItemDetailReportAction.setFromReports(true);
						taxItemDetailReportAction.run(details, true);
					}
				}
			}

		};
		Accounter.createGETService().getObjectById(
				new ClientTAXReturn().getObjectType(), transactionId, callback);
	}

	private List<VATSummary> getVatSummaires(ClientTAXReturn data) {

		List<VATSummary> result = new ArrayList<VATSummary>();
		for (ClientBox c : data.getBoxes()) {
			VATSummary summary = new VATSummary();
			summary.setStartDate(new ClientFinanceDate(data
					.getPeriodStartDate()));
			summary.setEndDate(new ClientFinanceDate(data.getPeriodEndDate()));
			summary.setName(c.getName());
			summary.setValue(c.getAmount());
			result.add(summary);
		}

		return result;

	}

	private List<TAXItemDetail> getTaxItemDetails(
			List<ClientTAXReturnEntry> taxEntries) {

		List<TAXItemDetail> details = new ArrayList<TAXItemDetail>();

		for (ClientTAXReturnEntry c : taxEntries) {
			TAXItemDetail detail = new TAXItemDetail();
			detail.setTaxAmount(c.getTaxAmount());
			detail.setTransactionId(c.getTransaction());
			detail.setTaxItemName(Accounter.getCompany()
					.getTAXItem(c.getTaxItem()).getName());
			detail.setTransactionType(c.getTransactionType());
			detail.setTransactionDate(new ClientFinanceDate(c
					.getTransactionDate()));
			detail.setNetAmount(c.getNetAmount());
			detail.setTAXRate(Accounter.getCompany().getTaxItem(c.getTaxItem())
					.getTaxRate());
			detail.setTotal(c.getGrassAmount());
			details.add(detail);
		}
		return details;
	}

	int getType(TransactionDetailByAccount record) {
		if (record.getTransactionType() == 11) {
			return (record.getMemo() != null && record.getMemo()
					.equalsIgnoreCase("supplier prepayment")) ? ClientTransaction.TYPE_VENDOR_PAYMENT
					: ClientTransaction.TYPE_PAY_BILL;
		}

		return record.getTransactionType();
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		TrialBalance accountdetails = (TrialBalance) data;
		Accounter.createReportService().getTransactionDetailByAccount(
				accountdetails == null ? 0 : accountdetails.getAccountId(),
				start, end, this);
	}

	@Override
	public void onEdit() {

	}

	@Override
	public void export(int generationType) {
		long accountId = data != null ? ((TrialBalance) data).getAccountId()
				: 0;
		UIUtils.generateReport(generationType, startDate.getDate(), endDate
				.getDate(), getReportType(), new NumberReportInput(accountId));
	}

	@Override
	public void printPreview() {

	}

	public int sort(TransactionDetailByAccount obj1,
			TransactionDetailByAccount obj2, int col) {
		int ret = obj1.getAccountName().toLowerCase()
				.compareTo(obj2.getAccountName().toLowerCase());
		if (ret != 0) {
			return ret;
		}
		switch (col) {
		case 0:
			return obj1.getAccountName().toLowerCase()
					.compareTo(obj2.getAccountName().toLowerCase());
		case 1:
			String name1 = obj1.getName();
			String name2 = obj2.getName();
			return name1.toLowerCase().compareTo(name2.toLowerCase());
		case 2:
			ClientFinanceDate date1 = obj1.getTransactionDate();
			ClientFinanceDate date2 = obj2.getTransactionDate();
			if (date1 != null && date2 != null) {
				return date1.compareTo(date2);
			}
			break;
		case 3:
			return UIUtils.compareInt(obj1.getTransactionType(),
					obj2.getTransactionType());
		case 4:
			int num1 = UIUtils.isInteger(obj1.getTransactionNumber()) ? Integer
					.parseInt(obj1.getTransactionNumber()) : 0;
			int num2 = UIUtils.isInteger(obj2.getTransactionNumber()) ? Integer
					.parseInt(obj2.getTransactionNumber()) : 0;
			if (num1 != 0 && num2 != 0)
				return UIUtils.compareInt(num1, num2);
			else
				return obj1.getTransactionNumber().compareTo(
						obj2.getTransactionNumber());

		case 5:
			return UIUtils.compareDouble(obj1.getTotal(), obj2.getTotal());
		case 6:
			if (!currentsectionName.toLowerCase().equals(
					obj1.getAccountName().toLowerCase())) {
				return obj1.getAccountName().toLowerCase()
						.compareTo(obj2.getAccountName().toLowerCase());
			} else {
				return UIUtils.compareDouble(obj1.getTotal(), obj2.getTotal());
			}
		}
		return 0;
	}

	public int getReportType() {
		return reportType;
	}

	public void setReportType(int reportType) {
		this.reportType = reportType;
	}

	@Override
	public void restoreView(HashMap<String, Object> map) {
		if (map == null || map.isEmpty()) {
			isDatesArranged = false;
			return;
		}
		ClientFinanceDate startDate = (ClientFinanceDate) map.get("startDate");
		ClientFinanceDate endDate = (ClientFinanceDate) map.get("endDate");
		this.serverReport.setStartAndEndDates(startDate, endDate);
		toolbar.setEndDate(endDate);
		toolbar.setStartDate(startDate);
		toolbar.setDefaultDateRange((String) map.get("selectedDateRange"));
		isDatesArranged = true;
	}

	@Override
	public HashMap<String, Object> saveView() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String selectedDateRange = toolbar.getSelectedDateRange();
		ClientFinanceDate startDate = toolbar.getStartDate();
		ClientFinanceDate endDate = toolbar.getEndDate();
		map.put("selectedDateRange", selectedDateRange);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		return map;
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
	// String firsRow = "<TR class=ReportGridRow>"
	// + grid.rowFormatter.getElement(0).getInnerHTML() + "</TR>";
	// String lastRow = "<TR class=ReportGridRow>"
	// + grid.rowFormatter.getElement(grid.getRowCount() - 1)
	// .getInnerHTML() + "</TR>";
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
	// String dateRangeHtml = "<div style=\"font-family:sans-serif;\"><strong>"
	// + this.toolbar.getStartDate()
	// + " - "
	// + this.toolbar.getEndDate() + "</strong></div>";
	//
	// UIUtils.generateReportPDF(this.getAction().getText(), gridhtml,
	// dateRangeHtml);
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
	// String firsRow = "<tr class=\"ReportGridRow\">"
	// + grid.rowFormatter.getElement(0).getInnerHTML() + "</tr>";
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
	// String dateRangeHtml = "<div style=\"font-family:sans-serif;\"><strong>"
	// + this.toolbar.getStartDate()
	// + " - "
	// + this.toolbar.getEndDate() + "</strong></div>";
	//
	// UIUtils.generateReportPDF(this.getAction().getText(), gridhtml,
	// dateRangeHtml);
	// }

}
