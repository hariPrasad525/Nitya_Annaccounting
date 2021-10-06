package com.nitya.accounter.migration;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.Contact;
import com.nitya.accounter.core.Customer;
import com.nitya.accounter.core.Estimate;

public class CreditsMigrator extends TransactionMigrator<Estimate> {
	@Override
	public JSONObject migrate(Estimate obj, MigratorContext context) throws JSONException {
		JSONObject jsonObj = super.migrate(obj, context);
		JSONObject external = new JSONObject();
		external.put("com.nitya.accounter.shared.customer.Credit", jsonObj);
		String _lid = context.getLocalIdProvider().getOrCreate(obj);
		jsonObj.put("@id", _lid);
		jsonObj.put("@_lid", _lid);
		Contact contact = obj.getContact();
		if (contact != null && !contact.getName().isEmpty()) {
			Long contactID = context.get("Contact", contact.getID());
			if (contactID != null && contactID != 0L) {
				JSONObject inter = new JSONObject();
				JSONObject outter = new JSONObject();
				outter.put("com.nitya.accounter.shared.common.Contact", inter);
				String localId = context.getLocalIdProvider().getOrCreate(contact);
				inter.put("@_oid", context.get("Contact", contact.getID()));
				inter.put("@_lid", localId);
				jsonObj.put("contact", outter);
			}
		}
		Customer customer = obj.getCustomer();
		if (customer != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.nitya.accounter.shared.customer.Customer", inter);
			String localId = context.getLocalIdProvider().getOrCreate(customer);
			inter.put("@_oid", context.get("Customer", customer.getID()));
			inter.put("@_lid", localId);
			jsonObj.put("payee", outter);
		}
		return external;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		criteria.add(Restrictions.eq("estimateType", Estimate.CREDITS));
	}
}
