package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Canada extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Alberta", "British Columbia",
				"Manitoba", "New Brunswick", "Newfoundland and Labrador",
				"Northwest Territories", "Nova Scotia", "Nunavut", "Ontario",
				"Prince Edward Island", "Quebec", "Saskatchewan", "Yukon" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "CAD";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return DayAndMonthUtil.april();
	}

	@Override
	public String getDefaultTimeZone(String state) {
		// TODO
		// America/St_Johns Newfoundland Time, including SE Labrador
		// America/Halifax Atlantic Time - Nova Scotia (most places), PEI
		// America/Glace_Bay Atlantic Time - Nova Scotia - places that did not
		// observe DST 1966-1971
		// America/Moncton Atlantic Time - New Brunswick
		// America/Goose_Bay Atlantic Time - Labrador - most locations
		// America/Blanc-Sablon Atlantic Standard Time - Quebec - Lower North
		// Shore
		// America/Montreal Eastern Time - Quebec - most locations
		// America/Toronto Eastern Time - Ontario - most locations
		// America/Nipigon Eastern Time - Ontario & Quebec - places that did not
		// observe DST 1967-1973
		// America/Thunder_Bay Eastern Time - Thunder Bay, Ontario
		// America/Iqaluit Eastern Time - east Nunavut - most locations
		// America/Pangnirtung Eastern Time - Pangnirtung, Nunavut
		// America/Resolute Eastern Standard Time - Resolute, Nunavut
		// America/Atikokan Eastern Standard Time - Atikokan, Ontario and
		// Southampton I, Nunavut
		// America/Rankin_Inlet Central Time - central Nunavut
		// America/Winnipeg Central Time - Manitoba & west Ontario
		// America/Rainy_River Central Time - Rainy River & Fort Frances,
		// Ontario
		// America/Regina Central Standard Time - Saskatchewan - most locations
		// America/Swift_Current Central Standard Time - Saskatchewan - midwest
		// America/Edmonton Mountain Time - Alberta, east British Columbia &
		// west Saskatchewan
		// America/Cambridge_Bay Mountain Time - west Nunavut
		// America/Yellowknife Mountain Time - central Northwest Territories
		// America/Inuvik Mountain Time - west Northwest Territories
		// America/Dawson_Creek Mountain Standard Time - Dawson Creek & Fort
		// Saint John, British Columbia
		// America/Vancouver Pacific Time - west British Columbia
		// America/Whitehorse Pacific Time - south Yukon
		// America/Dawson Pacific Time - north Yukon
		return "UTC-6:00 America/Rankin_Inlet";
	}

}
