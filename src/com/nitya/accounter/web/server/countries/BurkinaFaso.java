package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class BurkinaFaso extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "XAF";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Bal�", "Bam", "Banwa", "Baz�ga", "Bougouriba",
				"Boulgou", "Boulkiemd�", "Como�", "Ganzourgou", "Gnagna",
				"Gourma", "Houet", "Ioba", "Kadiogo", "K�n�dougou",
				"Komandjoari", "Kompienga", "Kossi", "Koulp�logo",
				"Kouritenga", "Kourw�ogo", "L�raba", "Loroum", "Mouhoun",
				"Nahouri", "Namentenga", "Nayala", "Noumbiel", "Oubritenga",
				"Oudalan", "Passor�", "Poni", "Sangui�", "Sanmatenga", "S�no",
				"Sissili", "Soum", "Sourou", "Tapoa", "Tuy", "Yagha",
				"Yatenga", "Ziro", "Zondoma", "Zoundw�ogo" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+0:00 Africa/Ouagadougou";
	}

}
