package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Ethiopia extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "ETB";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Addis Abeba", "Afar", "Amhara", "Benishangul",
				"Diredawa", "Gambella", "Harar", "Oromia", "Somali",
				"Southern", "Tigray" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+3:00 Africa/Addis_Ababa";
	}

}
