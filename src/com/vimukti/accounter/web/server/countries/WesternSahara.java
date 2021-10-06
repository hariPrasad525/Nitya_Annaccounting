package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class WesternSahara extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "MAD";
		// MRO also..
	}

	@Override
	public String[] getStates() {
		return new String[] { "al-'Ayun", "as-Samarah", "Bu Jaydur",
				"Wad-ad-Ðahab" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+0:00 Africa/El_Aaiun";
	}

}
