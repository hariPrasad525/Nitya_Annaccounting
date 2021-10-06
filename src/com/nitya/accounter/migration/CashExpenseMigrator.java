package com.nitya.accounter.migration;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.Address;
import com.nitya.accounter.core.CashPurchase;
import com.nitya.accounter.core.Contact;
import com.nitya.accounter.core.Vendor;
import com.nitya.accounter.web.client.core.ClientTransaction;

public class CashExpenseMigrator extends TransactionMigrator<CashPurchase> {

	@Override
	public JSONObject migrate(CashPurchase obj, MigratorContext context) throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);
		String _lid = context.getLocalIdProvider().getOrCreate(obj);
		jsonObject.put("@id", _lid);
		jsonObject.put("@_lid", _lid);
		JSONObject external = new JSONObject();
		external.put("com.nitya.accounter.shared.vendor.PurchaseExpense", jsonObject);
		Vendor vendor = obj.getVendor();
		jsonObject.put("phone", obj.getPhone());
		if (vendor != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.nitya.accounter.shared.vendor.Vendor", inter);
			String localId = context.getLocalIdProvider().getOrCreate(vendor);
			inter.put("@_oid", context.get("Vendor", vendor.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("payee", outter);
		}
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
		// billTo
		Address vendorAddress = obj.getVendorAddress();
		if (vendorAddress != null) {
			JSONObject address = new JSONObject();
			JSONObject jsonAddress = new JSONObject();
			address.put("org.ecgine.core.shared.Address", jsonAddress);
			String localId = context.getLocalIdProvider().getOrCreate(vendorAddress);
			jsonAddress.put("@_lid", localId);
			jsonAddress.put("@id", localId);
			jsonAddress.put("street", vendorAddress.getStreet());
			jsonAddress.put("city", vendorAddress.getCity());
			jsonAddress.put("stateOrProvince", vendorAddress.getStateOrProvinence());
			jsonAddress.put("zipOrPostalCode", vendorAddress.getZipOrPostalCode());
			jsonAddress.put("country", vendorAddress.getCountryOrRegion());
			jsonObject.put("billTo", address);
		}
		// paymentMethod
		String paymentMethod = obj.getPaymentMethod();
		if (paymentMethod != null) {
			jsonObject.put("paymentMethod", PicklistUtilMigrator.getPaymentMethodIdentifier(paymentMethod));
		} else {
			jsonObject.put("paymentMethod", "CASH");
		}
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

	public void addRestrictions(Criteria criteria) {
		criteria.add(Restrictions.eq("type", ClientTransaction.TYPE_CASH_EXPENSE));
	}
}
