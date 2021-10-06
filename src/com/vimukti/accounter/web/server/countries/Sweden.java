package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Sweden extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Blekinge", "Dalarna", "G�vleborg",
				"Gotland", "Halland", "J�mtland", "J�nk�ping", "Kalmar",
				"Kronoberg", "Norrbotten", "�rebro", "�sterg�tland", "Sk�ne",
				"S�dermanland", "Stockholm", "Uppsala", "V�rmland",
				"V�sterbotten", "V�sternorrland", "V�stmanland",
				"V�stra G�taland" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "SEK";
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
		return "UTC+1:00 Europe/Stockholm";
	}

}
