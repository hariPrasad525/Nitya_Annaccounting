package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Poland extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Dolnoslaskie", "Kujawsko-Pomorskie",
				"Lodzkie", "Lubelskie", "Lubuskie", "Malopolskie",
				"Mazowieckie", "Opolskie", "Podkarpackie", "Podlaskie",
				"Pomorskie", "Slaskie", "Swietokrzyskie",
				"Warminsko-Mazurskie", "Wielkopolskie", "Zachodnio-Pomorskie" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "PLN";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return DayAndMonthUtil.january();
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+1:00 Europe/Warsaw";
	}

}
