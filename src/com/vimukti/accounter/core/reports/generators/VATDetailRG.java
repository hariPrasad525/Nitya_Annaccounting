package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.vimukti.accounter.web.client.ui.serverreports.VATDetailServerReportView;

public class VATDetailRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_VATDETAIL;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		VATDetailServerReportView vatDetailServerReportView = new VATDetailServerReportView(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(vatDetailServerReportView, financeTool);
		vatDetailServerReportView.resetVariables();
		try {
			vatDetailServerReportView.onResultSuccess(financeTool
					.getReportManager().getVATDetailReport(startDate, endDate,
							company.getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vatDetailServerReportView.getGridTemplate();
	}

}
