package com.nitya.accounter.web.client.ui.core;

import java.util.Map;

public class KeyFinancialIndicator {

	String keyIndicator;

	Map<Integer, Double> indicators;

	double ytd;

	public double getYtd() {
		return ytd;
	}

	public void setYtd(double ytd) {
		this.ytd = ytd;
	}

	public Map<Integer, Double> getIndicators() {
		return indicators;
	}

	public void setIndicators(Map<Integer, Double> indicators) {
		this.indicators = indicators;
	}

	public String getKeyIndicator() {
		return keyIndicator;
	}

	public void setKeyIndicator(String keyIndicator) {
		this.keyIndicator = keyIndicator;
	}

}
