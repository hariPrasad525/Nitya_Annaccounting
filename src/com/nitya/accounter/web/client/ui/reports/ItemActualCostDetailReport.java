package com.nitya.accounter.web.client.ui.reports;

import com.nitya.accounter.web.client.core.BooleanReportInput;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.NumberReportInput;
import com.nitya.accounter.web.client.core.reports.ItemActualCostDetail;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.serverreports.ItemActualCostDetailServerReport;

public class ItemActualCostDetailReport extends
		AbstractReportView<ItemActualCostDetail> {
	private long itemId;
	private long customerId;
	private long jobId;
	private boolean isActualcostDetail;

	public ItemActualCostDetailReport(boolean isActualcostDetail, long itemId,
			long customerId, long jobId) {
		this.itemId = itemId;
		this.customerId = customerId;
		this.jobId = jobId;
		this.isActualcostDetail = isActualcostDetail;
		this.serverReport = new ItemActualCostDetailServerReport(this,
				isActualcostDetail);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getItemActualCostDetail(start, end,
				itemId, customerId, jobId, isActualcostDetail, this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void OnRecordClick(ItemActualCostDetail record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		ReportsRPC.openTransactionView(record.getTransationType(),
				record.getTransactionId());

	}

	@Override
	public boolean canPrint() {
		return true;
	}

	@Override
	public boolean canExportToCsv() {
		return true;
	}

	@Override
	public void export(int generationType) {
		UIUtils.generateReport(generationType, startDate.getDate(),
				endDate.getDate(), 187, new NumberReportInput(customerId),
				new NumberReportInput(jobId), new NumberReportInput(itemId),
				new BooleanReportInput(isActualcostDetail));
	}

}
