package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.VendorGroup;

public class VendorGroupMigrator implements IMigrator<VendorGroup> {

	@Override
	public JSONObject migrate(VendorGroup obj, MigratorContext context) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		JSONObject external = new JSONObject();
		external.put("com.vimukti.accounter.shared.vendor.VendorGroup", jsonObject);
		String _lid = context.getLocalIdProvider().getOrCreate(obj);
		jsonObject.put("@id", _lid);
		jsonObject.put("@_lid", _lid);
		CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);
		jsonObject.put("name", obj.getName());
		return external;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}