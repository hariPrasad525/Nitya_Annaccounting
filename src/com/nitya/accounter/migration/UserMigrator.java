package com.nitya.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.Client;
import com.nitya.accounter.core.User;

public class UserMigrator implements IMigrator<User> {

	@Override
	public JSONObject migrate(User obj, MigratorContext context) throws JSONException {
		User createdBy = context.getCompany().getCreatedBy();
		if (createdBy.getID() == obj.getID()) {
			return null;
		}
		Client accClient = obj.getClient();

		JSONObject user = new JSONObject();
		{
			JSONObject userJson = new JSONObject();
			String _lid = context.getLocalIdProvider().getOrCreate(obj);
			userJson.put("@id", _lid);
			userJson.put("@_lid", _lid);
			user.put("org.ecgine.core.shared.User", userJson);
			CommonFieldsMigrator.migrateCommonFields(obj, userJson, context);
			userJson.put("firstName", accClient.getFirstName());
			userJson.put("lastName", accClient.getLastName());
			userJson.put("email", accClient.getEmailId());
			userJson.put("fullName", accClient.getFullName());
		}
		return user;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}