package com.nitya.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.ItemGroup;

public class ItemGroupMigrator implements IMigrator<ItemGroup> {

	@Override
	public JSONObject migrate(ItemGroup obj, MigratorContext context) throws JSONException {
		JSONObject itemGroup = new JSONObject();
		{
			JSONObject jsonObject = new JSONObject();
			String _lid = context.getLocalIdProvider().getOrCreate(obj);
			jsonObject.put("@id", _lid);
			jsonObject.put("@_lid", _lid);
			itemGroup.put("com.nitya.accounter.shared.common.ItemGroup", jsonObject);
			CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);
			jsonObject.put("name", obj.getName());
		}
		return itemGroup;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}