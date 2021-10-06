package com.nitya.accounter.core.reports.generators;

import java.util.Map;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.nitya.accounter.web.client.ui.serverreports.UnRealisedExchangeLossesAndGainsServerReport;

public class UnRealisedExchangeLossesAndGainsRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return UNREALISED_EXCHANGE_LOSSES_AND_GAINS;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		UnRealisedExchangeLossesAndGainsServerReport realisesExhanges = new UnRealisedExchangeLossesAndGainsServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(realisesExhanges);
		realisesExhanges.resetVariables();
		long enteredDate = getInputAsLong(0);
		@SuppressWarnings("unchecked")
		Map<Long, Double> exchangeRates = (Map<Long, Double>) getInputAsNumberMap(1);
		try {
			realisesExhanges.onResultSuccess(financeTool.getReportManager()
					.getUnRealisedExchangeLossesAndGains(enteredDate,
							company.getID(), exchangeRates));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return realisesExhanges.getGridTemplate();
	}
}
