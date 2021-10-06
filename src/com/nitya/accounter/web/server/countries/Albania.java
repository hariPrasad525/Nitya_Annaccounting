package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Albania extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "ALL";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Berat", "Bulqizë", "Delvinë", "Devoll", "Dibrë",
				"Durrës", "Elbasan", "Fier", "Gjirokastër", "Gramsh", "Has",
				"Kavajë", "Kolonjë", "Korçë", "Krujë", "Kuçovë", "Kukës",
				"Kurbin", "Lezhë", "Librazhd", "Lushnjë", "Mallakastër",
				"Malsi e Madhe", "Mat", "Mirditë", "Peqin", "Përmet",
				"Pogradec", "Pukë", "Sarandë", "Shkodër", "Skrapar",
				"Tepelenë", "Tirana", "Tropojë", "Vlorë" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+1:00 Europe/Tirane";
	}
}
