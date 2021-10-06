package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Mongolia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Arhangaj", "Bajanhongor", "Bayan-�lgij", "Bulgan",
				"Darhan-Uul", "Dornod", "Dornogovi", "Dundgovi", "Govi-Altaj",
				"Govisumber", "H�ntij", "Hovd", "H�vsg�l", "�mn�govi", "Orhon",
				"�v�rhangaj", "S�l�ng�", "S�hbaatar", "T�v", "Ulaanbaatar",
				"Uvs", "Zavhan" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "MNT";
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
		if (state.equals("Bayan-�lgij") || state.equals("Govi-Altaj")
				|| state.equals("Hovd") || state.equals("Uvs")
				|| state.equals("Zavhan")) {
			return "UTC+7:00 Asia/Hovd";
		} else if (state.equals("Dornod") || state.equals("S�hbaatar")) {
			return "UTC+8:00 Asia/Choibalsan";
		} else {
			return "UTC+8:00 Asia/Ulaanbaatar";
		}
	}
}
