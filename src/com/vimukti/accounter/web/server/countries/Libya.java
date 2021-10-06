package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Libya extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "LYD";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Ajdabiya", "al-Butnan", "al-Hizam al-Ah_d?ar",
				"al-Jabal al-Ahd?ar", "al-Jifarah", "al-Jufrah", "al-Kufrah",
				"al-Marj", "al-Marqab", "al-Qubbah", "al-Wah?at",
				"an-Nuqat al-Hums", "az-Zawiyah", "Bangazi", "Bani Walid",
				"Darnah", "Gadamis", "Garyan", "Gat", "Marzuq", "Misratah",
				"Mizdah", "Nalut", "Sabha", "Sabratah wa Surman", "Surt",
				"Tarabulus", "Tarhunah wa Masallatah", "Wadi al-H?ayat",
				"Wadi aš-Šati", "Yafran wa Jadu" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+2:00 Africa/Tripoli";
	}

}
