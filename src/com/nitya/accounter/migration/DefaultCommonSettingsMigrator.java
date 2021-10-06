package com.nitya.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.Address;
import com.nitya.accounter.core.CompanyPreferences;

public class DefaultCommonSettingsMigrator implements
		IMigrator<CompanyPreferences> {

	@Override
	public JSONObject migrate(CompanyPreferences obj, MigratorContext context)
			throws JSONException {
		JSONObject object = new JSONObject();
		{
			JSONObject commonSettings = new JSONObject();
			object.put("com.nitya.accounter.shared.common.CommonSettings",
					commonSettings);
			String _lid = context.getLocalIdProvider().getOrCreate(obj);
			commonSettings.put("@id", _lid);
			commonSettings.put("@_lid", _lid);
			commonSettings.put("autoApplycredits", false);
			commonSettings.put("useBillable", true);
			commonSettings.put("industryType",
					getIndustryType(obj.getIndustryType()));
			commonSettings.put("taxId", obj.getTaxId());
			commonSettings.put("productAndServicesTrackingByCustomer",
					obj.isProductandSerivesTrackingByCustomerEnabled());
			boolean enabledMultiCurrency = obj.isEnabledMultiCurrency();
			commonSettings.put("multipleCurrency", enabledMultiCurrency);
			// Currency
			{
				JSONObject currencyJson = new JSONObject();
				JSONObject json = new JSONObject();
				currencyJson
						.put("com.nitya.accounter.shared.common.AccounterCurrency",
								json);
				json.put(
						"@_lid",
						context.getLocalIdProvider().getOrCreate(
								obj.getPrimaryCurrency()));
				json.put("@_oid", context.get("Currency", obj
						.getPrimaryCurrency().getID()));
				commonSettings.put("currency", currencyJson);
			}
			// commonSettings.put("tIN", obj.isEnabledMultiCurrency());
			// commonSettings.put("tAN", obj.isEnabledMultiCurrency());
			// commonSettings.put("pAN", obj.isEnabledMultiCurrency());
			commonSettings.put("depreciationStartDate", obj
					.getDepreciationStartDate().toEpochDay());
			commonSettings.put("companyName", obj.getTradingName());
			String legalName = obj.getLegalName();
			if (legalName != null) {
				commonSettings.put("companyHasLegalName", true);
				commonSettings.put("legalName", legalName);
			} else {
				commonSettings.put("companyHasLegalName", false);
			}
			{
				Address tradingAddress = obj.getTradingAddress();
				JSONObject address = new JSONObject();
				JSONObject jsonAddress = new JSONObject();
				address.put("org.ecgine.core.shared.Address", jsonAddress);
				String tradingAddressLocalID = context.getLocalIdProvider()
						.getOrCreate(tradingAddress);
				jsonAddress.put("@_lid", tradingAddressLocalID);
				jsonAddress.put("@id", tradingAddressLocalID);
				jsonAddress.put("street", tradingAddress.getStreet());
				jsonAddress.put("city", tradingAddress.getCity());
				jsonAddress.put("stateOrProvince",
						tradingAddress.getStateOrProvinence());
				jsonAddress.put("zipOrPostalCode",
						tradingAddress.getZipOrPostalCode());
				jsonAddress.put("country", tradingAddress.getCountryOrRegion());
				commonSettings.put("tradingAddress", address);
			}
			commonSettings.put("companyHasRegisteredAddress",
					obj.isShowRegisteredAddress());
			commonSettings.put("chargeOrTrackTax", true);
			boolean taxPerDetailLine = obj.isTaxPerDetailLine();
			commonSettings.put("taxItemInTransactions",
					taxPerDetailLine ? "ONE_PER_DETAIL_LINE"
							: "ONE_PER_TRANSACTION");
			commonSettings.put("enableTrackingTaxPaid", obj.isTrackPaidTax());
			commonSettings.put("enableTDS", obj.isTDSEnabled());

		}
		return object;
	}

	private String getIndustryType(int industryType) {
		switch (industryType) {
		case 1:
			return "FINANCIAL_ACCOUNTING_SERVICES";
		case 2:
			return "AD_AGENCY_OR_MARKETING";
		case 3:
			return "AGRICULTURE_FARMER_SRANCHERS";
		case 4:
			return "PHOTOGRAPHERS_ARTISTS_OR_WRITERS";
		case 5:
			return "VEHICLES_SHOWROOM_SALES";
		case 6:
			return "CHURCHES_AND_OTHER_RELIGIOUS_ORGANIZATIONS";
		case 7:
			return "CONSTRUCTION_AGENTS_COMPANIES";
		case 8:
			return "HVAC_ELECTRICIAN_PLUMBER_ETC_SERVICES";
		case 9:
			return "ARCHITECTURE_ENGINEERING_SERVICES";
		case 10:
			return "FINANCIAL_SERVICES";
		case 11:
			return "BEAUTY_PARLOUR_HAIR_SALOON_SHOPS";
		case 12:
			return "IT_SOFTWARE_HARDWARE";
		case 13:
			return "INSURANCE_AGENTS_COMPANIES";
		case 14:
			return "LAWN_SERVICE_LANDSCA_MAINTENANCE";
		case 15:
			return "LAWYERS_OR_LEGAL_ADVISORY_SERVICES";
		case 16:
			return "HOTEL_MOTEL";
		case 17:
			return "MANUFACTURER_AGENTS_OR_DEALERS";
		case 18:
			return "MANUFACTURE_COMPANIES";
		case 19:
			return "HEALTH_SERVICES_PHARMACY";
		case 20:
			return "NON_PROFIT_ORGANIZATION";
		case 21:
			return "PROFESSIONAL_CONSULTING";
		case 22:
			return "HOME_ASSOCIATION_OR_PROPERTY_MANAGAMENT_SERVICES";
		case 23:
			return "REAL_ESTATE_AGENT_BROKER";
		case 24:
			return "RENTAL_RESIDENTIAL_OR_COMMERCIAL";
		case 25:
			return "REPAIRS_MAINTENANCE";
		case 26:
			return "CATERINGS_DELIVERY_OR_RESTAURANTS_BAR";
		case 27:
			return "RETAIL_ONLINE_STORE_BUSINESS";
		case 28:
			return "FREE_LANCE_SERVICE_DEALER_SALES";
		case 29:
			return "PACKERS_MOVERS_OR_TRANSPORT_SERVICES";
		case 30:
			return "WHOLESALE_SALES_AND_DISTRIBUTION";
		case 31:
			return "GENERAL_PRODUCT_BASED_BUSINESS";
		case 32:
			return "GENERAL_SERVICE_BASED_BUSINESS";
		}
		return null;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}
