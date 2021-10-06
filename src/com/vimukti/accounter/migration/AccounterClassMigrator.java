package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.AccounterClass;

public class AccounterClassMigrator implements IMigrator<AccounterClass> {

	@Override
	public JSONObject migrate(AccounterClass obj, MigratorContext context) throws JSONException {
		JSONObject accounterClass = new JSONObject();
		{
			JSONObject jsonObject = new JSONObject();
			String _lid = context.getLocalIdProvider().getOrCreate(obj);
			jsonObject.put("@id", _lid);
			jsonObject.put("@_lid", _lid);
			accounterClass.put("com.vimukti.accounter.shared.common.AccountClass", jsonObject);
			CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);
			jsonObject.put("name", obj.getclassName());
			jsonObject.put("description", obj.getPath());
			AccounterClass parent = obj.getParent();
			if (parent != null) {
				JSONObject parentClass = new JSONObject();
				JSONObject json = new JSONObject();
				parentClass.put("com.vimukti.accounter.shared.common.AccountClass", json);
				String parentLocalID = context.getLocalIdProvider().getOrCreate(parent);
				json.put("@id", parentLocalID);
				json.put("@_lid", parentLocalID);
				jsonObject.put("subClassOf", parentClass);
			}
		}
		return accounterClass;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}