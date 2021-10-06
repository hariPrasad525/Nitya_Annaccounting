package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class HeardIslandAndMcdonaldIslands extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "AUD";
	}

	@Override
	public String[] getStates() {
		return new String[] {};
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+1:00 Europe/Andorra";
	}

}
