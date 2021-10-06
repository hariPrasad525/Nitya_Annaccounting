package com.nitya.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.Contact;
import com.nitya.accounter.core.Vendor;
import com.nitya.accounter.core.VendorCreditMemo;

public class DebitNoteMigrator extends TransactionMigrator<VendorCreditMemo> {

	@Override
	public JSONObject migrate(VendorCreditMemo obj, MigratorContext context) throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);
		JSONObject external = new JSONObject();
		external.put("com.nitya.accounter.shared.vendor.DebitNote", jsonObject);
		jsonObject.put("phone", obj.getPhone());
		String txnLid = context.getLocalIdProvider().getOrCreate(obj);
		jsonObject.put("@id", txnLid);
		jsonObject.put("@_lid", txnLid);
		// Vendor
		Vendor vendor = obj.getVendor();
		{
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.nitya.accounter.shared.vendor.Vendor", inter);
			String localId = context.getLocalIdProvider().getOrCreate(vendor);
			inter.put("@_oid", context.get("Vendor", vendor.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("payee", outter);
		}
		// Contact
		Contact contact = obj.getContact();
		if (contact != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.nitya.accounter.shared.common.Contact", inter);
			String localId = context.getLocalIdProvider().getOrCreate(contact);
			inter.put("@_oid", context.get("Contact", contact.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("contact", outter);
		}
		return external;
	}
}