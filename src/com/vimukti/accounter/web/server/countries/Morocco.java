package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Morocco extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Casablanca", "Chaouia-Ouardigha",
				"Doukkala-Abda", "Fes-Boulemane", "Gharb-Chrarda-Beni Hssen",
				"Guelmim", "Marrakech-Tensift-Al Haouz", "Meknes-Tafilalet",
				"Oriental", "Rabat-Sale-Zammour-Zaer", "Souss Massa-Draa",
				"Tadla-Azilal", "Tangier-Tetouan", "Taza-Al Hoceima-Taounate" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "MAD";
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
		return "UTC+0:00 Africa/Casablanca";
	}

}
