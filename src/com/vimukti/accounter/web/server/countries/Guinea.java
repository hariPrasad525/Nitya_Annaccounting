package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Guinea extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "GNS";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Basse Guinée", "Conakry", "Guinée Forestière",
				"Haute Guinée", "Moyenne Guinée" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+0:00 Africa/Conakry";
	}

}
