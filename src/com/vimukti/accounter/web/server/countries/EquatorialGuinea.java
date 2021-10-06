package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class EquatorialGuinea extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "XAF";
		// GAQ also...
	}

	@Override
	public String[] getStates() {
		return new String[] { "Annobón", "Bioko Norte", "Bioko Sur",
				"Centro Sur", "Kié-Ntem", "Litoral", "Wele-Nzas" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+1:00 Africa/Malabo";
	}

}
