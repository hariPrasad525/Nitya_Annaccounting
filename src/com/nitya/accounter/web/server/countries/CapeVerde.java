package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class CapeVerde extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "CVE";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Boavista", "Brava", "Fogo", "Maio", "Sal",
				"Santo Ant�o", "S�o Nicolau", "S�o Tiago", "S�o Vicente" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC-1:00 Atlantic/Cape_Verde";
	}

}
