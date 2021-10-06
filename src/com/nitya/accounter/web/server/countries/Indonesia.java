package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Indonesia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Aceh", "Bali", "Bangka-Belitung",
				"Banten", "Bengkulu", "Gorontalo", "Jakarta", "Jambi",
				"Jawa Barat", "Jawa Tengah", "Jawa Timur", "Kalimantan Barat",
				"Kalimantan Selatan", "Kalimantan Tengah", "Kalimantan Timur",
				"Lampung", "Maluku", "Maluku Utara", "Nusa Tenggara Barat",
				"Nusa Tenggara Timur", "Papua", "Riau", "Riau Kepulauan",
				"Sulawesi Selatan", "Sulawesi Tengah", "Sulawesi Tenggara",
				"Sulawesi Utara", "Sumatera Barat", "Sumatera Selatan",
				"Sumatera Utara", "Yogyakarta" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "IDR";
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
		// Asia/Jakarta Java & Sumatra
		// Asia/Pontianak west & central Borneo
		// Asia/Makassar east & south Borneo, Celebes, Bali, Nusa Tengarra, west
		// Timor
		// Asia/Jayapura Irian Jaya & the Moluccas
		return "UTC+8:00 Asia/Makassar";
	}

}
