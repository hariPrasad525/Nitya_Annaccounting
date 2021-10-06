package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.AmountsDueToVendor;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

public class AmountsDueToVendorServerReport extends
		AbstractFinaneReport<AmountsDueToVendor> {

	private String sectionName = "";

	public AmountsDueToVendorServerReport(
			IFinanceReport<AmountsDueToVendor> reportView) {
		this.reportView = reportView;
	}

	public AmountsDueToVendorServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		return new String[] {

		messages.payeeName(Global.get().Vendor()),
				getMessages().active(), getMessages().city(),
				getMessages().state(), getMessages().zipCode(),
				getMessages().phone(), getMessages().balance() };
	}

	@Override
	public String getTitle() {
		return messages.amountDueToVendor(Global.get().Vendor());

	}

	@Override
	public void makeReportRequest(long start, long end) {
		// if (this.financeTool == null)
		// return;
		// initValues();
		// try {
		// onSuccess(this.financeTool.getAmountsDueToVendor(start, end));
		// } catch (DAOException e) {
		// //
		// e.printStackTrace();
		// }
		// }
		//
		// private void initValues() {
		// // TODO Auto-generated method stub

	}

	@Override
	public Object getColumnData(AmountsDueToVendor record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getName();
		case 1:
			return record.getIsActive() ? getMessages().yes() : getMessages()
					.no();
		case 2:
			return record.getCity();
		case 3:
			return record.getState();
		case 4:
			return record.getZip();
		case 5:
			return record.getPhone();
		case 6:
			return record.getAmount();
		}
		return null;
	}

	@Override
	public void processRecord(AmountsDueToVendor record) {
		if (sectionDepth == 0) {
			addSection("", getMessages().total(), new int[] { 6 });
		} else if (sectionDepth == 1) {
			return;
		}
		// Go on recursive calling if we reached this place
		processRecord(record);
	}

	@Override
	public ClientFinanceDate getEndDate(AmountsDueToVendor obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(AmountsDueToVendor obj) {
		return obj.getStartDate();
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] {
				messages.payeeName(Global.get().Vendor()),
				getMessages().active(), getMessages().city(),
				getMessages().state(), getMessages().zipCode(),
				getMessages().phone(), getMessages().balance() };
	}

}
