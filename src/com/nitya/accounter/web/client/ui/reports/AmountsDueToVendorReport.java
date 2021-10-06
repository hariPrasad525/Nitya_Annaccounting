package com.nitya.accounter.web.client.ui.reports;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.reports.AmountsDueToVendor;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.serverreports.AmountsDueToVendorServerReport;

public class AmountsDueToVendorReport extends
		AbstractReportView<AmountsDueToVendor> {

	public AmountsDueToVendorReport() {
		this.serverReport = new AmountsDueToVendorServerReport(this);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getAmountsDueToVendor(start, end, this);
	}

	@Override
	public void OnRecordClick(AmountsDueToVendor record) {
		// nothing to do
	}

	@Override
	public void onEdit() {

	}

	@Override
	public void export(int generationType) {
		UIUtils.generateReport(generationType, startDate.getDate(),
				endDate.getDate(), 149);
	}

	@Override
	public void printPreview() {

	}

	@Override
	public int getToolbarType() {
		return 0;
	}

}
