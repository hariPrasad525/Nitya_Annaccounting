package com.nitya.accounter.web.client.ui.serverreports;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.reports.TransactionDetailByTaxItem;
import com.nitya.accounter.web.client.ui.core.ReportUtility;
import com.nitya.accounter.web.client.ui.reports.IFinanceReport;

public class TransactionDetailByTaxItemServerReport extends
		AbstractFinaneReport<TransactionDetailByTaxItem> {

	private String sectionName = "";

	public TransactionDetailByTaxItemServerReport(
			IFinanceReport<TransactionDetailByTaxItem> reportView) {
		this.reportView = reportView;
	}

	public TransactionDetailByTaxItemServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public Object getColumnData(TransactionDetailByTaxItem record,
			int columnIndex) {
		switch (columnIndex) {
		case 0:
			return ReportUtility
					.getTransactionName(record.getTransactionType());
		case 1:
			return record.getRate();
		case 2:
			return getDateByCompanyType(record.getDate());
		case 3:
			return record.getNumber();
		case 4:
			return record.getTaxItemName();
			// return record.getTaxCodeName();
		case 5:
			return record.getMemo();
		case 6:
			return record.getSalesTaxAmount();
		case 7:
			return record.getTaxableAmount();
		}
		return null;
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_NUMBER,
				COLUMN_TYPE_DATE, COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		return new String[] { "", getMessages().taxRate(),
				getMessages().date(), getMessages().no(),
				getMessages().name(), getMessages().memo(),
				getMessages().salesTax(), getMessages().taxableAmount() };

	}

	@Override
	public String getTitle() {
		return getMessages().transactionDetailByTaxCode();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// if (this.financeTool == null)
		// return;
		// resetVariables();
		// try {
		// if (navigateObjectName == null) {
		//
		// onSuccess(this.financeTool.getTransactionDetailByTaxcode(start,
		// end));
		// } else {
		// onSuccess(this.financeTool.getTransactionDetailByTaxcode(
		// navigateObjectName, start, end));
		// }
		// } catch (DAOException e) {
		// //
		// e.printStackTrace();
		// }
	}

	@Override
	public void processRecord(TransactionDetailByTaxItem record) {
		// if (sectionDepth == 0) {
		// addSection("", getMessages().total(), new int[] { 6, 7 });
		// } else
		if (sectionDepth == 0) {
			this.sectionName = record.getTaxItemName();
			addSection(record.getTaxAgencyName() + "-" + sectionName, "",
					new int[] { 6, 7 });
		} else if (sectionDepth == 1) {
			// No need to do anything, just allow adding this record
			if (!sectionName.equals(record.getTaxItemName())) {
				endSection();
			} else {
				return;
			}
		}
		// Go on recursive calling if we reached this place
		processRecord(record);
	}

	// its not using any where
	public void print() {

	}

	@Override
	public ClientFinanceDate getEndDate(TransactionDetailByTaxItem obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(TransactionDetailByTaxItem obj) {
		return obj.getStartDate();
	}

	@Override
	public int getColumnWidth(int index) {

		switch (index) {
		case 1:
			return 100;
		case 2:
			return 100;
		case 3:
			return 40;
		case 4:
			return 120;
		case 5:
			return 120;
		case 6:
			return 80;
		case 7:
			return 100;

		default:
			return -1;
		}

	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { "", getMessages().taxRate(),
				getMessages().date(), getMessages().no(),
				getMessages().name(), getMessages().memo(),
				getMessages().salesTax(), getMessages().taxableAmount() };
	}

}
