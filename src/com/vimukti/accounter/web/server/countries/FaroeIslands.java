package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class FaroeIslands extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "DKK";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Klaksvík", "Norðara Eysturoy", "Norðoy",
				"Sandoy", "Streymoy", "Suðuroy", "Syðra Eysturoy", "Tórshavn",
				"Vága" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+0:00 Atlantic/Faroe";
	}

}
