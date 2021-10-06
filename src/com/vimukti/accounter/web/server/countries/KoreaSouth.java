package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class KoreaSouth extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Cheju", "Chollabuk", "Chollanam",
				"Chungchongbuk", "Chungchongnam", "Inchon", "Kangwon",
				"Kwangju", "Kyonggi", "Kyongsangbuk", "Kyongsangnam", "Pusan",
				"Soul", "Taegu", "Taejon", "Ulsan" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "KRW";
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
		return "UTC+9:00 Asia/Seoul";
	}

}
