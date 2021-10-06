package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class BosniaAndHerzegovina extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "BAM";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Distrikt Brcko",
				"Federacija Bosna i Hercegovina", "Republika Srpska" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+1:00 Europe/Sarajevo";
	}

}
