package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.RealisedExchangeLossesAndGainsServerReport;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class RealisedExchangeLossesAndGainsRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REALISED_EXCHANGE_LOSSES_AND_GAINS;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		RealisedExchangeLossesAndGainsServerReport realisesExhanges = new RealisedExchangeLossesAndGainsServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(realisesExhanges);
		realisesExhanges.resetVariables();
		try {
			realisesExhanges.onResultSuccess(financeTool.getReportManager()
					.getRealisedExchangeLossesOrGains(company.getID(),
							startDate.getDate(), endDate.getDate()));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return realisesExhanges.getGridTemplate();
	}

}
