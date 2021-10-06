package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class BurkinaFaso extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "XAF";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Balé", "Bam", "Banwa", "Bazéga", "Bougouriba",
				"Boulgou", "Boulkiemdé", "Comoé", "Ganzourgou", "Gnagna",
				"Gourma", "Houet", "Ioba", "Kadiogo", "Kénédougou",
				"Komandjoari", "Kompienga", "Kossi", "Koulpélogo",
				"Kouritenga", "Kourwéogo", "Léraba", "Loroum", "Mouhoun",
				"Nahouri", "Namentenga", "Nayala", "Noumbiel", "Oubritenga",
				"Oudalan", "Passoré", "Poni", "Sanguié", "Sanmatenga", "Séno",
				"Sissili", "Soum", "Sourou", "Tapoa", "Tuy", "Yagha",
				"Yatenga", "Ziro", "Zondoma", "Zoundwéogo" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+0:00 Africa/Ouagadougou";
	}

}
