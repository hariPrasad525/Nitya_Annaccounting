package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Bangladesh extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "BDT";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Bagar Hat", "Bandarban", "Barguna", "Barisal",
				"Bhola", "Bogora", "Brahman Bariya", "Chandpur", "Chattagam",
				"Chuadanga", "Dhaka", "Dinajpur", "Faridpur", "Feni",
				"Gaybanda", "Gazipur", "Gopalganj", "Habiganj", "Jaipur Hat",
				"Jamalpur", "Jessor", "Jhalakati", "Jhanaydah", "Khagrachhari",
				"Khulna", "Kishorganj", "Koks Bazar", "Komilla", "Kurigram",
				"Kushtiya", "Lakshmipur", "Lalmanir Hat", "Madaripur",
				"Magura", "Maimansingh", "Manikganj", "Maulvi Bazar",
				"Meherpur", "Munshiganj", "Naral", "Narayanganj", "Narsingdi",
				"Nator", "Naugaon", "Nawabganj", "Netrakona", "Nilphamari",
				"Noakhali", "Pabna", "Panchagarh", "Patuakhali", "Pirojpur",
				"Rajbari", "Rajshahi", "Rangamati", "Rangpur", "Satkhira",
				"Shariatpur", "Sherpur", "Silhat", "Sirajganj", "Sunamganj",
				"Tangayal", "Thakurgaon" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+6:00 Asia/Dhaka";
	}

}
