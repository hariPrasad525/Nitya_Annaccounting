package com.nitya.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.TAXItemGroup;

public class TaxMigrator<T extends TAXItemGroup> implements IMigrator<T> {

	@Override
	public JSONObject migrate(T obj, MigratorContext context) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);
		jsonObject.put("name", obj.getName());
		jsonObject.put("inactive", !obj.isActive());
		jsonObject.put("isTaxGroup", false);
		return jsonObject;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}