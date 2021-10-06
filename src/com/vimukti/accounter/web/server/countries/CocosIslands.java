package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class CocosIslands extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "AUD";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Home Island", "West Island" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+6:30 Indian/Cocos";
	}

}
