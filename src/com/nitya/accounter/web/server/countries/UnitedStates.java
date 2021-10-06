package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.client.util.OrganizationType;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class UnitedStates extends AbstractCountryPreferences {

	protected static AccounterMessages messages=Global.get().messages();
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Alabama", "Alaska", "Arizona",
				"Arkansas", "California", "Colorado", "Connecticut",
				"Delaware", "District of Columbia", "Florida", "Georgia",
				"Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas",
				"Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts",
				"Michigan", "Minnesota", "Mississippi", "Missouri", "Montana",
				"Nebraska", "Nevada", "New Hampshire", "New Jersey",
				"New Mexico", "New York", "North Carolina", "North Dakota",
				"Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island",
				"South Carolina", "South Dakota", "Tennessee", "Texas", "Utah",
				"Vermont", "Virginia", "Washington", "West Virginia",
				"Wisconsin", "Wyoming"

		};
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "USD";
	}

	@Override
	public OrganizationType[] getOrganizationTypes() {
		OrganizationType[] types = {
				new OrganizationType("", messages
						.soleProprietorship(), messages
						.soleProprietorshipDesc()),
				new OrganizationType("", messages
						.partnershipOrLLP(), messages
						.partnershipOrLLPDesc()),
				new OrganizationType("", messages.LLC(), messages.LLCDesc()),
				new OrganizationType("", messages.corporation(),
						messages.corporationDesc()),
				new OrganizationType("", messages.sCorporation(),
						messages.sCorporationDesc()),
				new OrganizationType("", messages.nonProfit(),
						messages.nonProfitDesc()) };
		return types;
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return DayAndMonthUtil.october();
	}

	@Override
	public boolean isServiceTaxIdAndCompanyTaxIdSame() {
		return true;
	}

	@Override
	public boolean isVatAvailable() {
		return false;
	}

	@Override
	public boolean isSalesTaxAvailable() {
		return true;
	}

	@Override
	public boolean isSalesTaxIdAndCompanyTaxIdSame() {
		return true;
	}

	@Override
	public boolean isServiceTaxAvailable() {
		return false;
	}

	@Override
	public boolean isTDSAvailable() {
		return false;
	}

	@Override
	public String getDefaultTimeZone(String state) {
		// TODO
		// America/New_York Eastern Time
		// America/Detroit Eastern Time - Michigan - most locations
		// America/Kentucky/Louisville Eastern Time - Kentucky - Louisville area
		// America/Kentucky/Monticello Eastern Time - Kentucky - Wayne County
		// America/Indiana/Indianapolis Eastern Time - Indiana - most locations
		// America/Indiana/Vincennes Eastern Time - Indiana - Daviess, Dubois,
		// Knox & Martin Counties
		// America/Indiana/Winamac Eastern Time - Indiana - Pulaski County
		// America/Indiana/Marengo Eastern Time - Indiana - Crawford County
		// America/Indiana/Petersburg Eastern Time - Indiana - Pike County
		// America/Indiana/Vevay Eastern Time - Indiana - Switzerland County
		// America/Chicago Central Time
		// America/Indiana/Tell_City Central Time - Indiana - Perry County
		// America/Indiana/Knox Central Time - Indiana - Starke County
		// America/Menominee Central Time - Michigan - Dickinson, Gogebic, Iron
		// & Menominee Counties
		// America/North_Dakota/Center Central Time - North Dakota - Oliver
		// County
		// America/North_Dakota/New_Salem Central Time - North Dakota - Morton
		// County (except Mandan area)
		// America/Denver Mountain Time
		// America/Boise Mountain Time - south Idaho & east Oregon
		// America/Shiprock Mountain Time - Navajo
		// America/Phoenix Mountain Standard Time - Arizona
		// America/Los_Angeles Pacific Time
		// America/Anchorage Alaska Time
		// America/Juneau Alaska Time - Alaska panhandle
		// America/Yakutat Alaska Time - Alaska panhandle neck
		// America/Nome Alaska Time - west Alaska
		// America/Adak Aleutian Islands
		// Pacific/Honolulu Hawaii
		return "UTC-7:00 America/Denver";
	}

}
