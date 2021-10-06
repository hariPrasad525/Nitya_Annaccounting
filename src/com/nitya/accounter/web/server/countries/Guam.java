package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Guam extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "USD";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Agana Heights", "Agat", "Barrigada",
				"Chalan-Pago-Ordot", "Dededo", "Hagatña", "Inarajan",
				"Mangilao", "Merizo", "Mongmong-Toto-Maite", "Santa Rita",
				"Sinajana", "Talofofo", "Tamuning", "Yigo", "Yona" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+10:00 Pacific/Guam";
	}

}
