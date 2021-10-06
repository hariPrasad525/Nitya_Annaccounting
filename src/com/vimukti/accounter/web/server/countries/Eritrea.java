package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Eritrea extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "ERN";
		// ETB also...
	}

	@Override
	public String[] getStates() {
		return new String[] { "Anseba", "Debub", "Debub-Keih-Bahri",
				"Gash-Barka", "Maekel", "Semien-Keih-Bahri" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+3:00 Africa/Asmara";
	}

}
