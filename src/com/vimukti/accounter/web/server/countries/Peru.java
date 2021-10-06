package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Peru extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Amazonas", "Ancash", "Apurimac",
				"Arequipa", "Ayacucho", "Cajamarca", "Callao", "Cusco",
				"Huancavelica", "Huanuco", "Ica", "Junin", "La Libertad",
				"Lambayeque", "Lima Provincias", "Loreto", "Madre de Dios",
				"Moquegua", "Pasco", "Piura", "Puno", "San Martin", "Tacna",
				"Tumbes", "Ucayali" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "PEN";
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
		return "UTC-5:00 America/Lima";
	}

}
