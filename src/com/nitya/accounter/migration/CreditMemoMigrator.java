package com.nitya.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.Address;
import com.nitya.accounter.core.Contact;
import com.nitya.accounter.core.Customer;
import com.nitya.accounter.core.CustomerCreditMemo;

public class CreditMemoMigrator extends TransactionMigrator<CustomerCreditMemo> {
	@Override
	public JSONObject migrate(CustomerCreditMemo obj, MigratorContext context) throws JSONException {
		JSONObject jsonObj = super.migrate(obj, context);
		JSONObject external = new JSONObject();
		external.put("com.nitya.accounter.shared.customer.CreditMemo", jsonObj);
		String _lid = context.getLocalIdProvider().getOrCreate(obj);
		context.putChilderName("CreditMemo", "credit");
		jsonObj.put("@id", _lid);
		jsonObj.put("@_lid", _lid);
		Address billingAddress = obj.getBillingAddress();
		if (billingAddress != null) {
			JSONObject address = new JSONObject();
			JSONObject jsonAddress = new JSONObject();
			address.put("org.ecgine.core.shared.Address", jsonAddress);
			String localId = context.getLocalIdProvider().getOrCreate(billingAddress);
			jsonAddress.put("@_lid", localId);
			jsonAddress.put("@id", localId);
			jsonAddress.put("street", billingAddress.getStreet());
			jsonAddress.put("city", billingAddress.getCity());
			jsonAddress.put("stateOrProvince", billingAddress.getStateOrProvinence());
			jsonAddress.put("zipOrPostalCode", billingAddress.getZipOrPostalCode());
			jsonAddress.put("country", billingAddress.getCountryOrRegion());
			jsonObj.put("billTo", address);
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
		Contact contact = obj.getContact();
		if (contact != null && !contact.getName().isEmpty()) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.nitya.accounter.shared.common.Contact", inter);
			String localId = context.getLocalIdProvider().getOrCreate(contact);
			inter.put("@_oid", context.get("Contact", contact.getID()));
			inter.put("@_lid", localId);
			jsonObj.put("contact", outter);
		}
		return external;
	}
}
