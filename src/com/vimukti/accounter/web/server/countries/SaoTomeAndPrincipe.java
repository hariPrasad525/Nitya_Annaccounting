package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class SaoTomeAndPrincipe extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "�gua Grande", "Cantagalo", "Cau�", "Lemba",
				"Lobata", "M�-Zochi", "Pagu�" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "STD";
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
		return "UTC+0:00 Africa/Sao_Tome";
	}
}
