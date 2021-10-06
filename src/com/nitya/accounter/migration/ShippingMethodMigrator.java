package com.nitya.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.ShippingMethod;

public class ShippingMethodMigrator implements IMigrator<ShippingMethod> {

	@Override
	public JSONObject migrate(ShippingMethod obj, MigratorContext context) throws JSONException {
		JSONObject shippingMethod = new JSONObject();
		{
			JSONObject jsonObject = new JSONObject();
			String _lid = context.getLocalIdProvider().getOrCreate(obj);
			jsonObject.put("@id", _lid);
			jsonObject.put("@_lid", _lid);
			shippingMethod.put("com.nitya.accounter.shared.common.ShippingMethod", jsonObject);
			CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);
			jsonObject.put("name", obj.getName());
			jsonObject.put("description", obj.getDescription());
		}
		return shippingMethod;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}