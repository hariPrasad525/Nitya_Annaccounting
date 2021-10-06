package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Cambodia extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "KHR";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Banteay Mean Chey", "Bat D�mb�ng",
				"K�mp�ng Cham", "K�mp�ng Chhnang", "K�mp�ng Spoeu",
				"K�mp�ng Thum", "K�mp�t", "K�ndal", "Ka�h K�ng", "Kr�ch�h",
				"Krong Kaeb", "Krong Pailin", "Krong Preah Sihanouk",
				"M�nd�l Kiri", "Otdar Mean Chey", "Phnum P�nh", "Pousat",
				"Preah Vih�ar", "Prey Veaeng", "R�tanak Kiri", "Siem Reab",
				"Stueng Traeng", "Svay Rieng", "Takaev" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+7:00 Asia/Phnom_Penh";
	}

}
