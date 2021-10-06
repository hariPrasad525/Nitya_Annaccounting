package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Albania extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "ALL";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Berat", "Bulqiz�", "Delvin�", "Devoll", "Dibr�",
				"Durr�s", "Elbasan", "Fier", "Gjirokast�r", "Gramsh", "Has",
				"Kavaj�", "Kolonj�", "Kor��", "Kruj�", "Ku�ov�", "Kuk�s",
				"Kurbin", "Lezh�", "Librazhd", "Lushnj�", "Mallakast�r",
				"Malsi e Madhe", "Mat", "Mirdit�", "Peqin", "P�rmet",
				"Pogradec", "Puk�", "Sarand�", "Shkod�r", "Skrapar",
				"Tepelen�", "Tirana", "Tropoj�", "Vlor�" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+1:00 Europe/Tirane";
	}
}
