package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.nitya.accounter.web.client.ui.serverreports.TDSAcknowledgmentServerReportView;

public class TDSAcknowledgementRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_TDS_ACKNOWLEDGEMENT_REPORT;
	}

	@Override
	protected ReportGridTemplate<?> generate() {

		TDSAcknowledgmentServerReportView tdsView = new TDSAcknowledgmentServerReportView(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {
				return getDateInDefaultType(date);
			}
		};
		updateReport(tdsView);
		try {
			tdsView.onResultSuccess(financeTool.getReportManager()
					.getTDSAcknowledgments(this.startDate, this.endDate,
							company.getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tdsView.getGridTemplate();
	}

}
