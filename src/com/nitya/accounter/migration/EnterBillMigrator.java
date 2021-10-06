package com.nitya.accounter.migration;

import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.Address;
import com.nitya.accounter.core.Contact;
import com.nitya.accounter.core.EnterBill;
import com.nitya.accounter.core.Estimate;
import com.nitya.accounter.core.PaymentTerms;
import com.nitya.accounter.core.PurchaseOrder;
import com.nitya.accounter.core.Vendor;

public class EnterBillMigrator extends TransactionMigrator<EnterBill> {

	@Override
	public JSONObject migrate(EnterBill obj, MigratorContext context) throws JSONException {
		JSONObject enterBill = super.migrate(obj, context);
		JSONObject external = new JSONObject();
		external.put("com.nitya.accounter.shared.vendor.EnterBill", enterBill);
		String _lid = context.getLocalIdProvider().getOrCreate(obj);
		enterBill.put("@id", _lid);
		enterBill.put("@_lid", _lid);
		// payee
		{
			Vendor vendor = obj.getVendor();
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.nitya.accounter.shared.vendor.Vendor", inter);
			String localId = context.getLocalIdProvider().getOrCreate(vendor);
			inter.put("@_oid", context.get("Vendor", vendor.getID()));
			inter.put("@_lid", localId);
			enterBill.put("payee", outter);
		}
		// dueDate
		enterBill.put("dueDate", obj.getDueDate().toEpochDay());
		enterBill.put("phone", obj.getPhone());
		// billTo
		Address billTo = obj.getVendorAddress();
		if (billTo != null) {
			JSONObject address = new JSONObject();
			JSONObject jsonAddress = new JSONObject();
			address.put("org.ecgine.core.shared.Address", jsonAddress);
			String tradingAddressLocalID = context.getLocalIdProvider().getOrCreate(billTo);
			jsonAddress.put("@_lid", tradingAddressLocalID);
			jsonAddress.put("@id", tradingAddressLocalID);
			jsonAddress.put("street", billTo.getStreet());
			jsonAddress.put("city", billTo.getCity());
			jsonAddress.put("stateOrProvince", billTo.getStateOrProvinence());
			jsonAddress.put("zipOrPostalCode", billTo.getZipOrPostalCode());
			jsonAddress.put("country", billTo.getCountryOrRegion());
			enterBill.put("billTo", address);
		}
		// PaymentTerm
		PaymentTerms paymentTerm = obj.getPaymentTerm();
		if (paymentTerm != null) {
			JSONObject inner = new JSONObject();
			JSONObject outer = new JSONObject();
			outer.put("com.nitya.accounter.shared.common.PaymentTerm", inner);
			inner.put("@_oid", context.get("PaymentTerms", paymentTerm.getID()));
			inner.put("@_lid", context.getLocalIdProvider().getOrCreate(paymentTerm));
			enterBill.put("paymentTerm", outer);
		}
		// Delivery Date
		enterBill.put("deliveryDate", obj.getDeliveryDate().toEpochDay());
		// Contact
		Contact contact = obj.getContact();
		if (contact != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.nitya.accounter.shared.common.Contact", inter);
			String localId = context.getLocalIdProvider().getOrCreate(contact);
			inter.put("@_oid", context.get("Contact", contact.getID()));
			inter.put("@_lid", localId);
			// enterBill.put("contact", outter);
		}
		// BILLABLE EXPENSES
		Set<Estimate> estimates = obj.getEstimates();
		if (!estimates.isEmpty()) {
			JSONArray array = new JSONArray();
			JSONObject items = new JSONObject();
			items.put("com.nitya.accounter.shared.customer.SalesQuotation", array);
			for (Estimate estimate : estimates) {
				if (estimate.getEstimateType() == 1) {
					JSONObject inner = new JSONObject();
					inner.put("@_oid", context.get("SalesQuotation", estimate.getID()));
					inner.put("@_lid", context.getLocalIdProvider().getOrCreate(estimate));
					array.put(inner);
				}
			}
			enterBill.put("salesQuotations", items);
		}
		// PURCHASE ORDERS
		List<PurchaseOrder> purchaseOrders = obj.getPurchaseOrders();
		if (!purchaseOrders.isEmpty()) {
			JSONArray ordersArray = new JSONArray();
			JSONObject items = new JSONObject();
			items.put("com.nitya.accounter.shared.vendor.PurchaseOrder", ordersArray);
			for (PurchaseOrder order : purchaseOrders) {
				JSONObject inner = new JSONObject();
				inner.put("@_oid", context.get("PurchaseOrder", order.getID()));
				inner.put("@_lid", context.getLocalIdProvider().getOrCreate(order));
				ordersArray.put(inner);
			}
			enterBill.put("purchaseOrders", items);
		}
		return external;
	}
}
