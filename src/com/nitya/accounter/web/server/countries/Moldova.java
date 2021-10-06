package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Moldova extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Anenii Noi", "Balti", "Basarabeasca",
				"Briceni", "Cahul", "Calarasi", "Camenca", "Cantemir",
				"Causeni", "Chisinau", "Cimislia", "Criuleni", "Donduseni",
				"Drochia", "Dubasari municipiu", "Edinet", "Falesti",
				"Floresti", "Gagauzia", "Glodeni", "Grigoriopol", "Hincesti",
				"Ialoveni", "Leova", "Nisporeni", "Ocnita", "Orhei", "Rezina",
				"Ribnita", "Riscani", "Singerei", "Slobozia", "Soldanesti",
				"Soroca", "Stefan Voda", "Straseni", "Taraclia", "Telenesti",
				"Tighina", "Tiraspol", "Ungheni" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "MDL";
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
		return "UTC+2:00 Europe/Chisinau";
	}

}
