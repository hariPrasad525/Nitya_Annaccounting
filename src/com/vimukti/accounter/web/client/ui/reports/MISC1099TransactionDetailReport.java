package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.MISC1099TransactionDetail;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.MISC1099TransactionDetailServerReport;

public class MISC1099TransactionDetailReport extends
		AbstractReportView<MISC1099TransactionDetail> {
	private long vendorId;
	private int boxNo;

	public MISC1099TransactionDetailReport(long vendorId, int boxNo) {
		this.serverReport = new MISC1099TransactionDetailServerReport(this);
		this.vendorId = vendorId;
		this.boxNo = boxNo;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getMISC1099TransactionDetailReport(
				vendorId, boxNo, start, end, this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void OnRecordClick(MISC1099TransactionDetail record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		ReportsRPC.openTransactionView(record.getType(),
				record.getTransactionid());
	}

	@Override
	public void export(int generationType) {
		UIUtils.generateReport(generationType, startDate.getDate(),
				endDate.getDate(), 154);
	}

}
