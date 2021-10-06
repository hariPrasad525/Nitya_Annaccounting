package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.AmountsDueToVendorServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class AmountsDueToVendorRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_AMOUNTSDUETOVENDOR;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		AmountsDueToVendorServerReport amountsDueToVendorServerReport = new AmountsDueToVendorServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(amountsDueToVendorServerReport, financeTool);
		amountsDueToVendorServerReport.resetVariables();
		try {
			amountsDueToVendorServerReport.onResultSuccess(financeTool
					.getVendorManager().getAmountsDueToVendor(startDate,
							endDate, getCompany().getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return amountsDueToVendorServerReport.getGridTemplate();
	}

}
