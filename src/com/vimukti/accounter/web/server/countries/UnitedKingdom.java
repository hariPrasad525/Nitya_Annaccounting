package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class UnitedKingdom extends AbstractCountryPreferences {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Buckinghamshire", "Hampshire",
				"Cambridgeshire", "Hackney", "East Riding of Yorkshire",
				"Worcestershire", "Barking and Dagenham", "Moray",
				"Staffordshire", "Devon", "Harrow", "Falkirk",
				"Rhondda Cynon Taff", "East Ayrshire", "Sutton", "Larne",
				"Bedfordshire", "Tameside", "Cleveland", "Lothian Region",
				"Coventry", "Nottinghamshire", "Leicestershire", "Knowsley",
				"Torbay", "Wrexham", "Derbyshire", "Darlington",
				"Renfrewshire", "Hammersmith and Fulham", "Highland",
				"Orkney Islands", "West Yorkshire", "Merton", "Hertfordshire",
				"Merseyside", "Bristol", "Bexley", "East Renfrewshire",
				"Edinburgh", "Thurrock", "North Lanarkshire", "Barnet",
				"Conwy", "Bracknell Forest", "North Yorkshire",
				"Milton Keynes", "Rotherham", "London", "Angus",
				"Scottish Borders", "Dumfries and Galloway", "Greater London",
				"Tyne and Wear", "Surrey", "Suffolk", "Leeds", "Fermanagh",
				"Oxfordshire", "Moyle", "West Midlands", "East Lothian",
				"Aberdeenshire", "Calderdale", "Newport", "Ceredigion",
				"Limavady", "Clwyd", "Dungannon", "Rutland", "Sheffield",
				"Wokingham", "Slough", "Dyfed", "Cookstown",
				"Richmond upon Thames", "Blackpool", "Doncaster",
				"North Lincolnshire", "Denbighshire", "Bolton", "Caerphilly",
				"Lewisham", "Sunderland", "Armagh", "Stockton-on-Tees",
				"North Tyneside", "Greenwich", "Lincolnshire", "Solihull",
				"Vale of Glamorgan", "Grampian Region", "Islington", "Oldham",
				"Magherafelt", "Waltham Forest", "Bury", "South Yorkshire",
				"Northumberland", "Middlesbrough", "Southampton", "Durham",
				"Enfield", "Greater Manchester", "Redcar and Cleveland",
				"Sefton", "StHelens", "West Sussex", "Trafford", "Newham",
				"West Glamorgan", "Kensington and Chelsea", "Dorset",
				"Argyll and Bute", "Hillingdon", "East Dunbartonshire",
				"South Gloucestershire", "Neath Port Talbot", "Fife", "Down",
				"Merthyr Tydfil", "Castlereagh", "Wolverhampton", "Liverpool",
				"Plymouth", "Croydon", "Ealing", "Brent", "Nottingham",
				"Norfolk", "Lancashire", "Strabane", "Halton", "Camden",
				"Ballymoney", "Windsor and maidenhead", "Isles of Scilly",
				"Coleraine", "Reading", "Wirral", "Derry", "Cumbria",
				"Carrickfergus", "Southwark", "Gateshead", "Manchester",
				"Wiltshire", "South Glamorgan", "Gloucestershire",
				"South Lanarkshire", "Essex", "North Down", "Warrington",
				"East Sussex", "Belfast", "Herefordshire", "North Somerset",
				"Antrim", "Banbridge", "Carmarthenshire", "Northamptonshire",
				"Walsall", "Clackmannanshire", "South Ayrshire",
				"North Ayrshire", "Bradford", "Ballymena", "Dundee City",
				"Tayside Region", "Glasgow City", "Omagh", "Medway",
				"Warwickshire", "Haringey", "South Tyneside", "Kirklees",
				"Mid Glamorgan", "Flintshire", "Ards", "Lambeth", "Shropshire",
				"Telford and Wrekin", "Hounslow", "Craigavon", "Birmingham",
				"Monmouthshire", "Isle of Wight", "Portsmouth", "Somerset",
				"Shetland Islands", "Poole", "Sandwell", "West Dunbartonshire",
				"Borders Region", "Aberdeen City", "Berkshire", "Stirling",
				"Southend-on-Sea", "Bath and North East Somerset",
				"Western Isles", "Inverclyde", "Midlothian",
				"Perth and Kinross", "Leicester", "Swansea",
				"Kingston upon Hull", "Wandsworth", "West Berkshire",
				"Tower Hamlets", "Lisburn", "Bournemouth", "Cardiff",
				"Eilean Siar", "Dudley", "North East Lincolnshire", "Wigan",
				"Gwent", "Strathclyde Region", "West Lothian", "Redbridge",
				"Cheshire", "Newtownabbey", "Brighton and Hove", "Wakefield",
				"Newcastle upon Tyne", "Bromley", "Stockport", "Torfaen",
				"Newry and Mourne", "Hartlepool", "Pembrokeshire",
				"Hereford and Worcester", "Peterborough", "Stoke-on-Trent",
				"Powys", "Barnsley", "Kent", "Westminster", "Bridgend",
				"Derby", "Cornwall", "Gwynedd", "Blackburn with Darwen",
				"Humberside", "York", "Swindon", "Isle of Anglesey" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "GBP";
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
	public String getDefaultDateFormat() {
		return super.getDefaultDateFormat();
	}

	@Override
	public boolean isVatAvailable() {
		return true;
	}

	@Override
	public boolean isVatIdAndCompanyTaxIdSame() {
		return false;
	}

	@Override
	public boolean isSalesTaxAvailable() {
		return false;
	}

	@Override
	public boolean isTDSAvailable() {
		return false;
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+0:00 Europe/London";
	}

}
