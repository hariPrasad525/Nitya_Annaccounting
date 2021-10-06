package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Taiwan extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Changhwa", "Chiayi Hsien",
				"Chiayi Shih", "Hsinchu Hsien", "Hsinchu Shih", "Hualien",
				"Ilan", "Kaohsiung Hsien", "Kaohsiung Shih", "Keelung Shih",
				"Kinmen", "Miaoli", "Nantou", "Penghu", "Pingtung",
				"Taichung Hsien", "Taichung Shih", "Tainan Hsien",
				"Tainan Shih", "Taipei Hsien", "Taitung", "Taoyuan", "Yunlin" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "TWD";
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
		return "UTC+8:00 Asia/Taipei";
	}

}
