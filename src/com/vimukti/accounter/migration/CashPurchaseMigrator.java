package com.vimukti.accounter.migration;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.CashPurchase;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.PurchaseOrder;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.web.client.core.ClientTransaction;

public class CashPurchaseMigrator extends TransactionMigrator<CashPurchase> {

	@Override
	public JSONObject migrate(CashPurchase obj, MigratorContext context) throws JSONException {
		JSONObject cashPurchase = super.migrate(obj, context);
		String _lid = context.getLocalIdProvider().getOrCreate(obj);
		cashPurchase.put("@id", _lid);
		cashPurchase.put("@_lid", _lid);
		JSONObject external = new JSONObject();
		external.put("com.vimukti.accounter.shared.vendor.CashPurchase", cashPurchase);

		Vendor vendor = obj.getVendor();
		cashPurchase.put("phone", obj.getPhone());
		if (vendor != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.vendor.Vendor", inter);
			String localId = context.getLocalIdProvider().getOrCreate(vendor);
			inter.put("@_oid", context.get("Vendor", vendor.getID()));
			inter.put("@_lid", localId);
			cashPurchase.put("payee", outter);
		}
		Contact contact = obj.getContact();
		if (contact != null && !contact.getName().isEmpty()) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.common.Contact", inter);
			String localId = context.getLocalIdProvider().getOrCreate(contact);
			inter.put("@_oid", context.get("Contact", contact.getID()));
			inter.put("@_lid", localId);
			// cashPurchase.put("contact", outter);
		}
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
			cashPurchase.put("billTo", address);
		}
		String paymentMethod = obj.getPaymentMethod();
		if (paymentMethod != null) {
			cashPurchase.put("paymentMethod", PicklistUtilMigrator.getPaymentMethodIdentifier(paymentMethod));
		} else {
			cashPurchase.put("paymentMethod", "CASH");
		}
		// Account
		Account account = obj.getPayFrom();
		if (account != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.common.Account", inter);
			String localId = context.getLocalIdProvider().getOrCreate(account);
			inter.put("@_oid", context.get("Account", account.getID()));
			inter.put("@_lid", localId);
			cashPurchase.put("paymentAccount", outter);
		}
		cashPurchase.put("deliveryDate", obj.getDeliveryDate().toEpochDay());

		List<PurchaseOrder> purchaseOrders = obj.getPurchaseOrders();
		if (!purchaseOrders.isEmpty()) {
			JSONArray array = new JSONArray();
			JSONObject items = new JSONObject();
			items.put("com.vimukti.accounter.shared.vendor.PurchaseOrder", array);
			for (PurchaseOrder purchaseOrder : purchaseOrders) {
				JSONObject outter = new JSONObject();
				String localId = context.getLocalIdProvider().getOrCreate(purchaseOrder);
				outter.put("@_oid", context.get("Account", purchaseOrder.getID()));
				outter.put("@_lid", localId);
				array.put(outter);
			}
			cashPurchase.put("purchaseOrders", items);
		}
		cashPurchase.put("notes", obj.getMemo());
		return external;
	}

	public void addRestrictions(Criteria criteria) {
		criteria.add(Restrictions.eq("type", ClientTransaction.TYPE_CASH_PURCHASE));
	}
}
