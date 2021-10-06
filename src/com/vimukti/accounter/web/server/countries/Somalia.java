package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Somalia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Awdaal", "Baakool", "Baarii", "Baay", "Banaadir",
				"Gaalguuduud", "Gedoo", "Hiiraan", "Jubbada Dhexe",
				"Jubbada Hoose", "Mudug", "Nuugaal", "Sanaag",
				"Shabeellaha Dhexe", "Shabeellaha Hoose", "Sool", "Togdeer",
				"Woqooyi Galbeed" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "SOS";
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
		return "UTC+3:00 Africa/Mogadishu";
	}

}
