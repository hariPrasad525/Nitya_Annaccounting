package com.nitya.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.User;

public class RoleMembershipMigrator implements IMigrator<User> {

	@Override
	public JSONObject migrate(User obj, MigratorContext context) throws JSONException {
		User createdBy = context.getCompany().getCreatedBy();
		if (createdBy == obj) {
			return null;
		}
		JSONObject roleMemberShip = new JSONObject();
		{
			JSONObject internal = new JSONObject();
			roleMemberShip.put("org.ecgine.core.shared.RoleMembership", internal);
			String _lid = context.getLocalIdProvider().getOrCreate(obj);
			CommonFieldsMigrator.migrateCommonFields(obj, internal, context);
			internal.put("@id", _lid);
			internal.put("@_lid", _lid);
			// ROLE
			{
				JSONObject role = new JSONObject();
				JSONObject json = new JSONObject();
				role.put("org.ecgine.core.shared.Role", json);
				json.put("@_oid", context.getAdminRole());
				json.put("@_lid", context.getLocalIdProvider().getOrCreate());
				internal.put("role", role);
			}
			// USER
			{
				JSONObject user = new JSONObject();
				JSONObject json = new JSONObject();
				user.put("org.ecgine.core.shared.User", json);
				json.put("@_oid", context.get("User", obj.getID()));
				json.put("@_lid", context.getLocalIdProvider().getOrCreate());
				internal.put("user", user);
			}
		}
		return roleMemberShip;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}