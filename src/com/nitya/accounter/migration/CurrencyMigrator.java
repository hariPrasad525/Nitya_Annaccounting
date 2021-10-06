package com.nitya.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.Currency;

public class CurrencyMigrator implements IMigrator<Currency> {

	@Override
	public JSONObject migrate(Currency obj, MigratorContext context) throws JSONException {
		JSONObject object = new JSONObject();
		JSONObject currency = new JSONObject();
		object.put("com.nitya.accounter.shared.common.AccounterCurrency", currency);
		String _lid = context.getLocalIdProvider().getOrCreate(obj);
		currency.put("@id", _lid);
		currency.put("@_lid", _lid);
		currency.put("currency", obj.getFormalName());
		return object;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub
	}

}
