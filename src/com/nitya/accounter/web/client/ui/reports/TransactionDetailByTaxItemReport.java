package com.nitya.accounter.web.client.ui.reports;

import java.util.HashMap;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.StringReportInput;
import com.nitya.accounter.web.client.core.reports.SalesTaxLiability;
import com.nitya.accounter.web.client.core.reports.TransactionDetailByTaxItem;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.serverreports.TransactionDetailByTaxItemServerReport;

@SuppressWarnings("unchecked")
public class TransactionDetailByTaxItemReport extends
		AbstractReportView<TransactionDetailByTaxItem> {

	public TransactionDetailByTaxItemReport() {
		this.serverReport = new TransactionDetailByTaxItemServerReport(this);
	}

	@Override
	public void OnRecordClick(TransactionDetailByTaxItem record) {
		if (Accounter.getUser().canDoInvoiceTransactions())
			ReportsRPC.openTransactionView(record.getTransactionType(),
					record.getTransactionId());
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		SalesTaxLiability taxLiability = (SalesTaxLiability) data;
		if (taxLiability == null) {
			Accounter.createReportService().getTransactionDetailByTaxItem(
					start, end, this);
		} else if (taxLiability.getTaxItemName() != null) {
			Accounter.createReportService().getTransactionDetailByTaxItem(
					taxLiability.getTaxItemName(), taxLiability.getStartDate(),
					taxLiability.getEndDate(), this);

		}
	}

	@Override
	public void onEdit() {

	}

	@Override
	public void export(int generationType) {
		String taxItemName = this.data != null ? ((SalesTaxLiability) data)
				.getTaxItemName() : "";
		UIUtils.generateReport(generationType, startDate.getDate(),
				endDate.getDate(), 114, new StringReportInput(taxItemName));
	}

	@Override
	public void printPreview() {

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

}