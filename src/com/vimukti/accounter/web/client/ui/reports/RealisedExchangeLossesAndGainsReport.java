package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.RealisedExchangeLossOrGain;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.RealisedExchangeLossesAndGainsServerReport;

/**
 * @author Prasanna Kumar G
 * 
 */
public class RealisedExchangeLossesAndGainsReport extends
		AbstractReportView<RealisedExchangeLossOrGain> {

	public RealisedExchangeLossesAndGainsReport() {
		this.serverReport = new RealisedExchangeLossesAndGainsServerReport(this);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getRealisedExchangeLossesAndGains(
				start, end, this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void OnRecordClick(RealisedExchangeLossOrGain record) {
		ReportsRPC.openTransactionView(record.getTransactionType(),
				record.getTransaction());
	}

	@Override
	public void export(int generationType) {
		UIUtils.generateReport(generationType, startDate.getDate(),
				endDate.getDate(), 172);
	}

}
