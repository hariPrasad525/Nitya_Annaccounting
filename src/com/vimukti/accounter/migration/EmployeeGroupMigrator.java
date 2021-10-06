package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.EmployeeGroup;

public class EmployeeGroupMigrator implements IMigrator<EmployeeGroup> {

	@Override
	public JSONObject migrate(EmployeeGroup obj, MigratorContext context) throws JSONException {
		JSONObject employeeGroup = new JSONObject();
		employeeGroup.put("@class", "com.vimukti.accounter.shared.payroll.EmployeeGroup");
		CommonFieldsMigrator.migrateCommonFields(obj, employeeGroup, context);
		employeeGroup.put("name", obj.getName());
		return employeeGroup;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}