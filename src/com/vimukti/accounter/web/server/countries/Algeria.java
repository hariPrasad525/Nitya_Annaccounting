package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Algeria extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Adrar", "al-Agwat", "al-Bayad",
				"al-Buirah", "Algier", "al-Jilfah", "al-Masilah", "al-Midyah",
				"al-Wad", "an-Na'amah", "Annabah", "as-Salif", "at-Tarif",
				"'Ayn ad-Dafla", " Ayn Timusanat", "Bassar", "Batnah",
				"Bijayah", "Biskrah", "Blidah", "Bumardas", "Burj Bu Arririj",
				"Galizan", "Gardayah", "H_ansalah", "Ilizi", "Jijili", "Milah",
				"Mu'askar", "Mustaganam", "Qalmah", "Qusantinah", "Sa'idah",
				"Sakikdah", "Satif", "Sidi bal'abbas", "Suq Ahras",
				"Tamanrasat", "Tibazah", "Tibissah", "Tilimsan", "Tinduf",
				"Tisamsilt", "Tiyarat", "Tizi Wazu", "Umm-al-Bawagi", "Wahran",
				"Warqla" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "DZD";
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
		return "UTC+1:00 Africa/Algiers";
	}

}
