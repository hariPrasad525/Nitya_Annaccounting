package com.nitya.accounter.web.client.ui.reports;

import java.util.HashMap;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.MapNumberReportInput;
import com.nitya.accounter.web.client.core.NumberReportInput;
import com.nitya.accounter.web.client.core.reports.UnRealisedLossOrGain;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.serverreports.UnRealisedExchangeLossesAndGainsServerReport;

/**
 * @author Prasanna Kumar G
 * 
 */
public class UnRealisedExchangeLossesAndGainsReport extends
		AbstractReportView<UnRealisedLossOrGain> {

	private HashMap<Long, Double> exchangeRates = new HashMap<Long, Double>();
	private ClientFinanceDate enteredDate = new ClientFinanceDate();

	public UnRealisedExchangeLossesAndGainsReport() {
		this.serverReport = new UnRealisedExchangeLossesAndGainsServerReport(
				this);
	}

	@Override
	public void init() {
		super.init();
		toolbar.setVisible(false);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getUnRealisedExchangeLossesAndGains(
				enteredDate.getDate(), exchangeRates, this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_AS_OF;
	}

	@Override
	public void OnRecordClick(UnRealisedLossOrGain record) {
		// TODO Auto-generated method stub

	}

	public void setExchangeRates(HashMap<Long, Double> exchangeRates) {
		this.exchangeRates = exchangeRates;
	}

	public void setEnteredDate(ClientFinanceDate enteredDate) {
		this.enteredDate = enteredDate;
	}

	@Override
	public void export(int generationType) {
		UIUtils.generateReport(generationType, startDate.getDate(),
				endDate.getDate(), 173,
				new NumberReportInput(enteredDate.getDate()),
				new MapNumberReportInput(exchangeRates));
	}
}
