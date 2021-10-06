package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Lesotho extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "LSL";
		// LSM, ZAR also...
	}

	@Override
	public String[] getStates() {
		return new String[] { "Berea", "Butha-Buthe", "Leribe", "Mafeteng",
				"Maseru", "Mohale's Hoek", "Mokhotlong", "Qacha's Nek",
				"Quthing", "Thaba-Tseka" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+2:00 Africa/Maseru";
	}

}
