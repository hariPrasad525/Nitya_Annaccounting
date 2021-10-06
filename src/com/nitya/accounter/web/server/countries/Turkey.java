package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Turkey extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Adana", "Adiyaman", "Afyonkarahisar",
				"Agri", "Aksaray", "Amasya", "Ankara", "Antalya", "Ardahan",
				"Artvin", "Aydin", "Balikesir", "Bartin", "Batman", "Bayburt",
				"Bilecik", "Bingol", "Bitlis", "Bolu", "Burdur", "Bursa",
				"Canakkale", "Cankiri", "Corum", "Denizli", "Diyarbakir",
				"Duzce", "Edirne", "Elazig", "Erzincan", "Erzurum",
				"Eskisehir", "Gaziantep", "Giresun", "Gumushane", "Hakkari",
				"Hatay", "Igdir", "Isparta", "Istanbul", "Izmir",
				"Kahramanmaras", "Karabuk", "Karaman", "Kars", "Kastamonu",
				"Kayseri", "Kilis", "Kirikkale", "Kirklareli", "Kirsehir",
				"Kocaeli", "Konya", "Kutahya", "Malatya", "Manisa", "Mardin",
				"Mersin", "Mugla", "Mus", "Nevsehir", "Nigde", "Ordu",
				"Osmaniye", "Rize", "Sakarya", "Samsun", "Sanliurfa", "Siirt",
				"Sinop", "Sirnak", "Sivas", "Tekirdag", "Tokat", "Trabzon",
				"Tunceli", "Usak", "Van", "Yalova", "Yozgat", "Zonguldak" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "TRY";
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
		return "UTC+2:00 Europe/Istanbul";
	}

}
