package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Laos extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "LAK";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Attopu", "Bokeo", "Bolikhamsay", "Champasak",
				"Houaphanh", "Khammouane", "Luang Nam Tha", "Luang Prabang",
				"Oudomxay", "Phongsaly", "Saravan", "Savannakhet", "Sekong",
				"Viangchan Prefecture", "Viangchan Province", "Xaignabury",
				"Xiang Khuang" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+7:00 Asia/Vientiane";
	}

}
