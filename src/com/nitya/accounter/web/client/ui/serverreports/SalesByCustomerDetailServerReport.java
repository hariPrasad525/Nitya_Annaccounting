package com.nitya.accounter.web.client.ui.serverreports;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.core.reports.BaseReport;
import com.nitya.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.reports.IFinanceReport;

public class SalesByCustomerDetailServerReport extends
		AbstractFinaneReport<SalesByCustomerDetail> {
	private String sectionName = "";

	public SalesByCustomerDetailServerReport(
			IFinanceReport<SalesByCustomerDetail> reportView) {
		this.reportView = reportView;
	}

	public SalesByCustomerDetailServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public Object getColumnData(SalesByCustomerDetail record, int columnIndex) {
		switch (columnIndex) {
		case 2:
			return Utility.getTransactionName(record.getType());
		case 1:
			return getDateByCompanyType(record.getDate());
		case 3:
			return record.getNumber();
		case 0:
			return "";
			// case 4:
			// return record.getDueDate();
		case 4:
			return record.getAmount();
		}
		return null;
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_NUMBER, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		return new String[] { Global.get().customer(), getMessages().date(),
				getMessages().type(), getMessages().noDot(),
				getMessages().amount() };

	}

	@Override
	public String getTitle() {
		return messages.salesByCustomerDetail(Global.get().Customer());
	}

	@Override
	public int getColumnWidth(int index) {
		// if (index == 0)
		// return 180;
		if (index == 1)
			return 120;
		else if (index == 0)
			return 250;
		else if (index == 4)
			return 160;
		else if (index == 0)
			return 200;
		else if (index == 3)
			return 100;
		else
			return 200;
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// resetVariables();
		// try {
		// if (navigateObjectName == null) {
		//
		// onSuccess(this.financeTool.getSalesByCustomerDetailReport(
		// start, end));
		// } else {
		// onSuccess(this.financeTool.getSalesByCustomerDetailReport(
		// navigateObjectName, start, end));
		// }
		// } catch (DAOException e) {
		// }
	}

	@Override
	public void processRecord(SalesByCustomerDetail record) {
		// if (sectionDepth == 0) {
		// addSection(new String[] { "", "" }, new String[] { "", "", "",
		// getMessages().total() }, new int[] { 4 });
		// } else
		if (sectionDepth == 0) {
			this.sectionName = record.getName();
			addSection(new String[] { sectionName }, new String[] { "", "", "",
					getMessages().total() }, new int[] { 4 });
			// addSection(sectionName, "", new int[] { 5 });
		} else if (sectionDepth == 1) {
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
	}

	@Override
	public ClientFinanceDate getEndDate(SalesByCustomerDetail obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(SalesByCustomerDetail obj) {
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

	public int sort(SalesByCustomerDetail obj1, SalesByCustomerDetail obj2,
			int col) {

		int ret = obj1.getName().toLowerCase()
				.compareTo(obj2.getName().toLowerCase());
		if (ret != 0) {
			return ret;
		}
		switch (col) {
		case 2:
			return UIUtils.compareInt(obj1.getType(), obj2.getType());
		case 1:
			return obj1.getDate().compareTo(obj2.getDate());
		case 3:
			return UIUtils.compareInt(Integer.parseInt(obj1.getNumber()),
					Integer.parseInt(obj2.getNumber()));
		case 0:
			return obj1.getName().toLowerCase()
					.compareTo(obj2.getName().toLowerCase());
		case 4:
			return obj1.getDueDate().compareTo(obj2.getDueDate());
		case 5:
			return UIUtils.compareDouble(obj1.getAmount(), obj2.getAmount());
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
		return new String[] { Global.get().customer(), getMessages().date(),
				getMessages().type(), getMessages().noDot(),
				getMessages().amount() };
	}

	@Override
	public String getDefaultDateRange() {
		return messages.thisMonth();
	}

}
