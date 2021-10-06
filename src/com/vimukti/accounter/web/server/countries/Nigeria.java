package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Nigeria extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Abia",
				"Abuja Federal Capital Territory", "Adamawa", "Akwa Ibom",
				"Anambra", "Bauchi", "Bayelsa", "Benue", "Borno",
				"Cross River", "Delta", "Ebonyi", "Edo", "Ekiti", "Enugu",
				"Gombe", "Imo", "Jigawa", "Kaduna", "Kano", "Katsina", "Kebbi",
				"Kogi", "Kwara", "Lagos", "Nassarawa", "Niger", "Ogun", "Ondo",
				"Osun", "Oyo", "Plateau", "Rivers", "Sokoto", "Taraba", "Yobe",
				"Zamfara" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "NGN";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return DayAndMonthUtil.january();
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+1:00 Africa/Lagos";
	}

}
