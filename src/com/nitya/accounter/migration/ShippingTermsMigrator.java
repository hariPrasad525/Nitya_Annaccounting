package com.nitya.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.ShippingTerms;

public class ShippingTermsMigrator implements IMigrator<ShippingTerms> {

	@Override
	public JSONObject migrate(ShippingTerms obj, MigratorContext context) throws JSONException {
		JSONObject shippingTerms = new JSONObject();
		{
			JSONObject jsonObject = new JSONObject();
			String _lid = context.getLocalIdProvider().getOrCreate(obj);
			jsonObject.put("@id", _lid);
			jsonObject.put("@_lid", _lid);
			shippingTerms.put("com.nitya.accounter.shared.common.ShippingTerm", jsonObject);
			CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);
			jsonObject.put("name", obj.getName());
			jsonObject.put("description", obj.getDescription());
		}
		return shippingTerms;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}