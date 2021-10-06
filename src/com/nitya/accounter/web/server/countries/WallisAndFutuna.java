package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class WallisAndFutuna extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "XPF";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Alo", "Hahake", "Hihifo", "Mua", "Sigave" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+12:00 Pacific/Wallis";
	}

}
