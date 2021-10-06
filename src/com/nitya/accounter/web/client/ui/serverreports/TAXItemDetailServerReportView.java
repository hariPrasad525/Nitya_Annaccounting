package com.nitya.accounter.web.client.ui.serverreports;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.ui.reports.IFinanceReport;
import com.nitya.accounter.web.client.ui.reports.TAXItemDetail;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class TAXItemDetailServerReportView extends
		AbstractFinaneReport<TAXItemDetail> {

	private String sectionName;
	private String name;

	public TAXItemDetailServerReportView(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	public TAXItemDetailServerReportView(
			IFinanceReport<TAXItemDetail> reportView) {
		this.reportView = reportView;
	}

	@Override
	public int getColumnWidth(int index) {
		switch (index) {
		case 0:
			return 220;
		case 1:
			return 120;
		case 2:
			return 50;
		case 3:
			return 100;
		case 4:
			return 130;
		case 5:
			return 120;

		default:
			return -1;
		}
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { getMessages().type(), getMessages().date(),
				getMessages().noDot(), getMessages().taxRate(),
				getMessages().grossAmount(), getMessages().taxAmount(),
				getMessages().netAmount() };
	}

	@Override
	public String getTitle() {
		return "Tax Item Detail ";
	}

	@Override
	public String[] getColunms() {
		return new String[] { getMessages().type(), getMessages().date(),
				getMessages().noDot(), getMessages().taxRate(),
				getMessages().grossAmount(), getMessages().taxAmount(),
				getMessages().netAmount() };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
				COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public void processRecord(TAXItemDetail record) {
		if (sectionDepth == 0) {
			this.sectionName = record.getTaxItemName();
			this.name = record.getTaxItemName();
			addSection(sectionName, getMessages().total(), new int[] { 5 });
		} else if (sectionDepth == 1) {
			// No need to do anything, just allow adding this record
			if (!name.equals(record.getTaxItemName())) {
				endSection();
			} else {
				return;
			}
		}
		// Go on recursive calling if we reached this place
		processRecord(record);

	}

	@Override
	public void resetVariables() {
		this.sectionDepth = 0;
		this.sectionName = "";
	}

	@Override
	public Object getColumnData(TAXItemDetail record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return Utility.getTransactionName(record.getTransactionType());

		case 1:
			return getDateByCompanyType(record.getTransactionDate());
		case 2:
			return record.getTransactionNumber();
		case 3:
			return record.isPercentage() ? "    " + record.getTAXRate() + "%"
					: record.getTAXRate();
		case 4:
			return record.getTotal();
		case 5:
			return record.getTaxAmount();
		case 6:
			return record.getNetAmount();
		}
		return null;
	}

	@Override
	public ClientFinanceDate getStartDate(TAXItemDetail obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(TAXItemDetail obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub

	}

}
