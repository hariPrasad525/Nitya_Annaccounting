package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Japan extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Aichi", "Akita", "Aomori", "Chiba",
				"Ehime", "Fukui", "Fukuoka", "Fukushima", "Gifu", "Gumma",
				"Hiroshima", "Hokkaido", "Hyogo", "Ibaraki", "Ishikawa",
				"Iwate", "Kagawa", "Kagoshima", "Kanagawa", "Kochi",
				"Kumamoto", "Kyoto", "Mie", "Miyagi", "Miyazaki", "Nagano",
				"Nagasaki", "Nara", "Niigata", "Oita", "Okayama", "Okinawa",
				"Osaka", "Saga", "Saitama", "Shiga", "Shimane", "Shizuoka",
				"Tochigi", "Tokio", "Tokushima", "Tottori", "Toyama",
				"Wakayama", "Yamagata", "Yamaguchi", "Yamanashi" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "JPY";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return DayAndMonthUtil.april();
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+9:00 Asia/Tokyo";
	}

}
