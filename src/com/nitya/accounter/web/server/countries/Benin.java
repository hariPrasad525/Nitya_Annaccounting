package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Benin extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "XAF";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Alibori", "Atacora", "Atlantique", "Borgou",
				"Collines", "Couffo", "Donga", "Littoral", "Mono", "Ouémé",
				"Plateau", "Zou" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+1:00 Africa/Porto-Novo";
	}

}
