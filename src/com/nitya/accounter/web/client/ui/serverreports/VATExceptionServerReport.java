package com.nitya.accounter.web.client.ui.serverreports;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.reports.VATDetail;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.reports.IFinanceReport;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class VATExceptionServerReport extends AbstractFinaneReport<VATDetail> {

	String sectionTitle = "";
	double accountbalance = 0D;
	private String currentsectionName = "";

	public VATExceptionServerReport(IFinanceReport<VATDetail> reportView) {
		isVATDetailReport = true;
		this.reportView = reportView;
	}

	public VATExceptionServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public void makeReportRequest(long start, long end) {

	}

	@Override
	public ClientFinanceDate getEndDate(VATDetail obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(VATDetail obj) {
		return obj.getStartDate();
	}

	public int sort(VATDetail obj1, VATDetail obj2, int col) {
		int ret = obj1.getBoxName().compareTo(obj2.getBoxName());
		if (ret != 0) {
			return ret;
		}
		switch (col) {
		case 0:
			return obj1.getTransactionName().compareTo(
					obj2.getTransactionName());
		case 1:
			return obj1.getTransactionDate().compareTo(
					obj2.getTransactionDate());
		case 2:
			return UIUtils.compareInt(
					Integer.parseInt(obj1.getTransactionNumber()),
					Integer.parseInt(obj2.getTransactionNumber()));
		case 3:
			return obj1.getPayeeName().compareTo(obj2.getPayeeName());
		case 4:
			return UIUtils.compareDouble(obj1.getVatRate(), obj2.getVatRate());
		case 5:
			return UIUtils.compareDouble(obj1.getNetAmount(),
					obj2.getNetAmount());
		case 6:
			return UIUtils.compareDouble(obj1.getTotal(), obj2.getTotal());
		}
		return 0;
	}

	@Override
	public int getColumnWidth(int index) {
		switch (index) {
		case 0:
			return 220;
		case 1:
			return 80;
		case 2:
			return 80;
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
	public Object getColumnData(VATDetail record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getTransactionName();

		case 1:
			return getDateByCompanyType(record.getTransactionDate());
		case 2:
			return record.getTransactionNumber();
		case 3:
			return record.isPercentage() ? record.getVatRate() + "%" : record
					.getVatRate();
		case 4:
			return record.getNetAmount();
		case 5:
			return record.getTotal();
		case 6:
			if (!currentsectionName.equals(record.getBoxName())) {
				currentsectionName = record.getBoxName();
				accountbalance = 0.0D;
			}
			return accountbalance += record.getTotal();
		}
		return null;
	}

	/*
	 * @see
	 * com.nitya.accounter.web.client.ui.reports.AbstractReportView#getColumnTypes
	 * ()
	 */
	@Override
	public int[] getColumnTypes() {
		// if (toolbar.isToolBarComponentChanged) {
		// return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
		// COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT,
		// COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
		// } else {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
				COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
		// }
	}

	/*
	 * @see
	 * com.nitya.accounter.web.client.ui.reports.AbstractReportView#getColunms
	 * ()
	 */
	@Override
	public String[] getColunms() {
		return new String[] { getMessages().type(), getMessages().date(),
				getMessages().noDot(), getMessages().vatRate(),
				getMessages().filedAmount(), getMessages().currentAmount(),
				getMessages().amountDifference() };
	}

	/*
	 * @see
	 * com.nitya.accounter.web.client.ui.reports.AbstractReportView#getTitle()
	 */
	@Override
	public String getTitle() {
		return getMessages().vatDetail();
	}

	@Override
	public void processRecord(VATDetail record) {
		if (sectionDepth == 0) {
			sectionTitle = record.getBoxName();
			addSection(sectionTitle, sectionTitle, new int[] { 5 });
		} else if (!sectionTitle.equals(record.getBoxName())) {
			endSection();
			sectionTitle = record.getBoxName();
			addSection(sectionTitle, sectionTitle, new int[] { 5 });
		} else {
			return;
		}

		processRecord(record);

	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { getMessages().type(), getMessages().date(),
				getMessages().noDot(), getMessages().vatRate(),
				getMessages().filedAmount(), getMessages().currentAmount(),
				getMessages().amountDifference() };
	}

}
