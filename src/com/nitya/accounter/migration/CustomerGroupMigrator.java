package com.nitya.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.CustomerGroup;

public class CustomerGroupMigrator implements IMigrator<CustomerGroup> {

	@Override
	public JSONObject migrate(CustomerGroup obj, MigratorContext context) throws JSONException {
		JSONObject customerGroup = new JSONObject();
		{
			JSONObject jsonObject = new JSONObject();
			String _lid = context.getLocalIdProvider().getOrCreate(obj);
			jsonObject.put("@id", _lid);
			jsonObject.put("@_lid", _lid);
			customerGroup.put("com.nitya.accounter.shared.customer.CustomerGroup", jsonObject);
			CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);
			jsonObject.put("name", obj.getName());
		}
		return customerGroup;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}