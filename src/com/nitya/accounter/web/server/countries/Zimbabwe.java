package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Zimbabwe extends AbstractCountryPreferences {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getPreferredCurrency() {
		return "ZWD";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Bulawayo", "Harare", "Manicaland",
				"Mashonaland Central", "Mashonaland East", "Mashonaland West",
				"Masvingo", "Matabeleland North", "Matabeleland South",
				"Midlands" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+2:00 Africa/Harare";
	}

}
