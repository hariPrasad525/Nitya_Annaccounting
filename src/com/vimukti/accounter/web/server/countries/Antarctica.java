package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Antarctica extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "NOK";
	}

	@Override
	public String[] getStates() {
		return new String[] {};
	}

	@Override
	public String getDefaultTimeZone(String state) {
		// TODO
		// Antarctica/McMurdo McMurdo Station, Ross Island
		// Antarctica/South_Pole Amundsen-Scott Station, South Pole
		// Antarctica/Rothera Rothera Station, Adelaide Island
		// Antarctica/Palmer Palmer Station, Anvers Island
		// Antarctica/Mawson Mawson Station, Holme Bay
		// Antarctica/Davis Davis Station, Vestfold Hills
		// Antarctica/Casey Casey Station, Bailey Peninsula
		// Antarctica/Vostok Vostok Station, Lake Vostok
		// Antarctica/DumontDUrville Dumont-d'Urville Station, Terre Adelie
		// Antarctica/Syowa Syowa Station, E Ongul I
		// Antarctica/Macquarie Macquarie Island Station, Macquarie Island
		return "UTC+12:00 Antarctica/South_Pole";
	}

}
