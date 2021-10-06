package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Malawi extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "MWK";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Balaka", "Blantyre City", "Chikwawa",
				"Chiradzulu", "Chitipa", "Dedza", "Dowa", "Karonga", "Kasungu",
				"Lilongwe City", "Machinga", "Mangochi", "Mchinji", "Mulanje",
				"Mwanza", "Mzimba", "Mzuzu City", "Nkhata Bay", "Nkhotakota",
				"Nsanje", "Ntcheu", "Ntchisi", "Phalombe", "Rumphi", "Salima",
				"Thyolo", "Zomba Municipality" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+2:00 Africa/Blantyre";
	}

}
