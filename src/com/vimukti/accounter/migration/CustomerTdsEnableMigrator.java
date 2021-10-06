package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Customer;

public class CustomerTdsEnableMigrator implements IMigrator<Customer> {

	boolean isResetTds;

	public CustomerTdsEnableMigrator(boolean enableTds) {
		this.isResetTds = enableTds;
	}

	@Override
	public JSONObject migrate(Customer obj, MigratorContext context) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		JSONObject external = new JSONObject();
		external.put("com.vimukti.accounter.shared.customer.Customer", jsonObject);
		jsonObject.put("@_lid", context.getLocalIdProvider().getOrCreate(obj));
		jsonObject.put("@_oid", context.get("Customer", obj.getID()));
		if (isResetTds) {
			jsonObject.put("tDSApplicable", obj.isWillDeductTDS());
		} else {
			jsonObject.put("tDSApplicable", true);
		}
		return external;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}
