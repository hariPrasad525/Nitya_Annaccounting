package com.nitya.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.PayrollUnit;

public class PayrollUnitMigrator implements IMigrator<PayrollUnit> {
	@Override
	public JSONObject migrate(PayrollUnit payrollunit, MigratorContext context) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("@class", "com.nitya.accounter.shared.payroll.PayRollUnit");
		CommonFieldsMigrator.migrateCommonFields(payrollunit, jsonObject, context);
		jsonObject.put("symbol", payrollunit.getSymbol());
		jsonObject.put("formalName", payrollunit.getFormalname());
		jsonObject.put("numberOfDecimalPlaces", payrollunit.getNoofDecimalPlaces());
		return jsonObject;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}