package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Croatia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Bjelovar-Bilogora",
				"Dubrovnik-Neretva", "Grad Zagreb", "Istra", "Karlovac",
				"Koprivnica-Krizevci", "Krapina-Zagorje", "Lika-Senj",
				"Medimurje", "Osijek-Baranja", "Pozega-Slavonija",
				"Primorje-Gorski Kotar", "Sibenik-Knin", "Sisak-Moslavina",
				"Slavonski Brod-Posavina", "Split-Dalmacija", "Varazdin",
				"Virovitica-Podravina", "Vukovar-Srijem", "Zadar", "Zagreb" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "HRK";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return DayAndMonthUtil.january();
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+1:00 Europe/Zagreb";
	}

}
