package com.nitya.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.CompanyPreferences;
import com.nitya.accounter.core.Currency;

public class AccountingCurrenciesMigrator implements IMigrator<CompanyPreferences> {

	@Override
	public JSONObject migrate(CompanyPreferences obj, MigratorContext context) throws JSONException {
		JSONObject object = new JSONObject();
		{
			JSONObject commonSettings = new JSONObject();
			object.put("com.nitya.accounter.shared.common.CommonSettings", commonSettings);
			commonSettings.put("@_oid",
					context.get(CompanyMigrator.COMMON_SETTINGS, CompanyMigrator.COMMON_SETTINGS_OLD_ID));
			commonSettings.put("@_lid", context.getLocalIdProvider().getOrCreate(obj));
			JSONArray currencies = new JSONArray();
			JSONObject currenciesJson = new JSONObject();
			currenciesJson.put("com.nitya.accounter.shared.common.AccounterCurrency", currencies);
			for (Currency currency : context.getCompany().getCurrencies()) {
				{
					JSONObject json = new JSONObject();
					String _currencyLid = context.getLocalIdProvider().getOrCreate(currency);
					json.put("@_lid", _currencyLid);
					json.put("@_oid", context.get("Currency", currency.getID()));
					currencies.put(json);
				}
			}
			commonSettings.put("accountingCurrencies", currenciesJson);
		}
		return object;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}
