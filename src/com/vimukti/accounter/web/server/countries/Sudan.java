package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Sudan extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "A'ali-an-Nil", "al-Bah?r-al-Ah?mar",
				"al-Buh?ayrat", "al-Jazirah", "al-Qad?arif", "al-Wah?dah",
				"an-Nil-al-Abyad?", "an-Nil-al-Azraq", "aš-Šamaliyah",
				"Bah?r-al-Jabal", "Garb-al-Istiwa'iyah", "Garb Bah?r-al-Gazal",
				"Garb Darfur", "Garb Kurdufan", "Janub Darfur",
				"Janub Kurdufan", "Junqali", "Kassala", "Khartum",
				"Nahr-an-Nil", "Šamal Bah?r-al-Gazal", "Šamal Darfur",
				"Šamal Kurdufan", "Šarq-al-Istiwa'iyah", "Sinnar", "Warab" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "SDG";
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
		return "UTC+3:00 Africa/Khartoum";
	}
}
