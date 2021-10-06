package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Russia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Adygeja", "Aga", "Alanija", "Altaj",
				"Amur", "Arhangelsk", "Astrahan", "Baskortostan", "Belgorod",
				"Brjansk", "Burjatija", "Cecenija", "Celjabinsk", "Cita",
				"Cukotka", "Cuvasija", "Dagestan", "Evenkija", "Gorno-Altaj",
				"Habarovsk", "Hakasija", "Hanty-Mansija", "Ingusetija",
				"Irkutsk", "Ivanovo", "Jamalo-Nenets", "Jaroslavl", "Jevrej",
				"Kabardino-Balkarija", "Kaliningrad", "Kalmykija", "Kaluga",
				"Kamcatka", "Karacaj-Cerkessija", "Karelija", "Kemerovo",
				"Kirov", "Komi", "Komi-Permjak", "Korjakija", "Kostroma",
				"Krasnodar", "Krasnojarsk", "Kurgan", "Kursk", "Leningrad",
				"Lipeck", "Magadan", "Marij El", "Mordovija", "Moskau",
				"Moskovskaja Oblast", "Murmansk", "Nenets", "Niznij Novgorod",
				"Novgorod", "Novosibirsk", "Omsk", "Orenburg", "Orjol",
				"Penza", "Perm", "Primorje", "Pskov", "Rjazan", "Rostov",
				"Saha", "Sahalin", "Samara", "Sankt Petersburg", "Saratov",
				"Smolensk", "Stavropol", "Sverdlovsk", "Tajmyr", "Tambov",
				"Tatarstan", "Tjumen", "Tomsk", "Tula", "Tver", "Tyva",
				"Udmurtija", "Uljanovsk", "Ust-Orda", "Vladimir", "Volgograd",
				"Vologda", "Voronez" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "RUB";
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
		// TODO
		// Europe/Kaliningrad Moscow-01 - Kaliningrad
		// Europe/Moscow Moscow+00 - west Russia
		// Europe/Volgograd Moscow+00 - Caspian Sea
		// Europe/Samara Moscow - Samara, Udmurtia
		// Asia/Yekaterinburg Moscow+02 - Urals
		// Asia/Omsk Moscow+03 - west Siberia
		// Asia/Novosibirsk Moscow+03 - Novosibirsk
		// Asia/Novokuznetsk Moscow+03 - Novokuznetsk
		// Asia/Krasnoyarsk Moscow+04 - Yenisei River
		// Asia/Irkutsk Moscow+05 - Lake Baikal
		// Asia/Yakutsk Moscow+06 - Lena River
		// Asia/Vladivostok Moscow+07 - Amur River
		// Asia/Sakhalin Moscow+07 - Sakhalin Island
		// Asia/Magadan Moscow+08 - Magadan
		// Asia/Kamchatka Moscow+08 - Kamchatka
		// Asia/Anadyr Moscow+08 - Bering Sea
		return "UTC+7:00 Asia/Krasnoyarsk";
	}

}
