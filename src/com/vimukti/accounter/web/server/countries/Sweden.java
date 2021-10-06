package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Sweden extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Blekinge", "Dalarna", "Gävleborg",
				"Gotland", "Halland", "Jämtland", "Jönköping", "Kalmar",
				"Kronoberg", "Norrbotten", "Örebro", "Östergötland", "Skåne",
				"Södermanland", "Stockholm", "Uppsala", "Värmland",
				"Västerbotten", "Västernorrland", "Västmanland",
				"Västra Götaland" };
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
