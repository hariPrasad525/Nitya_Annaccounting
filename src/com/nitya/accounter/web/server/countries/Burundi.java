package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Burundi extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "BIF";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Bubanza", "Bujumbura", "Bururi", "Cankuzo",
				"Cibitoke", "Gitega", "Karuzi", "Kayanza", "Kirundo",
				"Makamba", "Muramvya", "Muyinga", "Ngozi", "Rutana", "Ruyigi" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+2:00 Africa/Bujumbura";
	}

}
