package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Job;

public class JobMigrator implements IMigrator<Job> {

	@Override
	public JSONObject migrate(Job obj, MigratorContext context) throws JSONException {

		JSONObject external = new JSONObject();
		{
			JSONObject jsonObject = new JSONObject();
			external.put("com.vimukti.accounter.shared.customer.Project", jsonObject);
			String _lid = context.getLocalIdProvider().getOrCreate(obj);
			jsonObject.put("@id", _lid);
			jsonObject.put("@_lid", _lid);

			CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);

			jsonObject.put("name", obj.getJobName());
			FinanceDate startDate = obj.getStartDate();
			jsonObject.put("startDate", startDate.toEpochDay());
			FinanceDate endDate = obj.getEndDate();
			if (startDate.after(endDate)) {
				jsonObject.put("endDate", startDate.toEpochDay());
			} else {
				jsonObject.put("endDate", endDate.toEpochDay());
			}
			Customer customer = obj.getCustomer();
			if (customer != null) {
				JSONObject inter = new JSONObject();
				JSONObject outter = new JSONObject();
				outter.put("com.vimukti.accounter.shared.customer.Customer", inter);
				String localId = context.getLocalIdProvider().getOrCreate(customer);
				inter.put("@_oid", context.get("Customer", customer.getID()));
				inter.put("@_lid", localId);
				jsonObject.put("customer", outter);
			}
			jsonObject.put("status", PicklistUtilMigrator.getProjectStatusIdentity(obj.getJobStatus()));
		}
		return external;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}
