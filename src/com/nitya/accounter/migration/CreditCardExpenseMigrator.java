package com.nitya.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.Contact;
import com.nitya.accounter.core.CreditCardCharge;
import com.nitya.accounter.core.Vendor;

public class CreditCardExpenseMigrator extends TransactionMigrator<CreditCardCharge> {

	@Override
	public JSONObject migrate(CreditCardCharge obj, MigratorContext context) throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);
		JSONObject external = new JSONObject();
		external.put("com.nitya.accounter.shared.vendor.PurchaseExpense", jsonObject);
		String _lid = context.getLocalIdProvider().getOrCreate(obj);
		jsonObject.put("@id", _lid);
		jsonObject.put("@_lid", _lid);
		jsonObject.put("phone", obj.getPhone());
		Contact contact = obj.getContact();
		if (contact != null && !contact.getName().isEmpty()) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.nitya.accounter.shared.common.Contact", inter);
			String localId = context.getLocalIdProvider().getOrCreate(contact);
			inter.put("@_oid", context.get("Contact", contact.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("contact", outter);

		}
		Vendor vendor = obj.getVendor();
		if (vendor != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.nitya.accounter.shared.vendor.Vendor", inter);
			String localId = context.getLocalIdProvider().getOrCreate(vendor);
			inter.put("@_oid", context.get("Vendor", vendor.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("payee", outter);

		}
		String paymentMethod = obj.getPaymentMethod();
		if (paymentMethod != null) {
			jsonObject.put("paymentMethod", PicklistUtilMigrator.getPaymentMethodIdentifier(paymentMethod));
		} else {
			jsonObject.put("paymentMethod", "CASH");
		}

		Long chequeNumber = null;
		try {
			chequeNumber = Long.valueOf(obj.getCheckNumber());
			jsonObject.put("chequeNumber", chequeNumber);
		} catch (Exception e) {
		}
		jsonObject.put("notes", obj.getMemo());

		// payFrom Account
		Account payFrom = obj.getPayFrom();
		if (payFrom != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.nitya.accounter.shared.common.Account", inter);
			String localId = context.getLocalIdProvider().getOrCreate(payFrom);
			inter.put("@_oid", context.get("Account", payFrom.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("paymentAccount", outter);
		}
		return external;

	}
}
