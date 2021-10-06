package com.nitya.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.Location;

public class LocationMigrator implements IMigrator<Location> {

	@Override
	public JSONObject migrate(Location obj, MigratorContext context) throws JSONException {
		JSONObject location = new JSONObject();
		{
			JSONObject jsonObject = new JSONObject();
			String _lid = context.getLocalIdProvider().getOrCreate(obj);
			jsonObject.put("@id", _lid);
			jsonObject.put("@_lid", _lid);
			location.put("com.nitya.accounter.shared.common.Location", jsonObject);
			CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);
			jsonObject.put("name", obj.getName());
		}
		return location;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}