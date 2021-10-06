package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.PurchaseOrder;
import com.vimukti.accounter.core.ShippingMethod;
import com.vimukti.accounter.core.ShippingTerms;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.web.client.core.ClientTransaction;

public class PurchaseOrderMigrator extends TransactionMigrator<PurchaseOrder> {

	@Override
	public JSONObject migrate(PurchaseOrder obj, MigratorContext context) throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);
		JSONObject external = new JSONObject();
		external.put("com.vimukti.accounter.shared.vendor.PurchaseOrder", jsonObject);
		String _lid = context.getLocalIdProvider().getOrCreate(obj);
		jsonObject.put("@id", _lid);
		jsonObject.put("@_lid", _lid);
		// Phone
		jsonObject.put("phone", obj.getPhone());
		// DeliveryDate
		jsonObject.put("deliveryDate", obj.getDeliveryDate().toEpochDay());
		// Payee
		Vendor vendor = obj.getVendor();
		if (vendor != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.vendor.Vendor", inter);
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
			outter.put("com.vimukti.accounter.shared.common.Contact", inter);
			String localId = context.getLocalIdProvider().getOrCreate(contact);
			inter.put("@_oid", context.get("Contact", contact.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("contact", outter);
		}
		// shipTo
		Address shipTo = obj.getShippingAddress();
		if (shipTo != null) {
			JSONObject address = new JSONObject();
			JSONObject jsonAddress = new JSONObject();
			address.put("org.ecgine.core.shared.Address", jsonAddress);
			String tradingAddressLocalID = context.getLocalIdProvider().getOrCreate(shipTo);
			jsonAddress.put("@_lid", tradingAddressLocalID);
			jsonAddress.put("@id", tradingAddressLocalID);
			jsonAddress.put("street", shipTo.getStreet());
			jsonAddress.put("city", shipTo.getCity());
			jsonAddress.put("stateOrProvince", shipTo.getStateOrProvinence());
			jsonAddress.put("zipOrPostalCode", shipTo.getZipOrPostalCode());
			jsonAddress.put("country", shipTo.getCountryOrRegion());
			jsonObject.put("shipTo", address);
		}
		// BillTo
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
			jsonObject.put("billTo", address);
		}
		// PaymentTerm
		PaymentTerms paymentTerm = obj.getPaymentTerm();
		if (paymentTerm != null) {
			JSONObject inner = new JSONObject();
			JSONObject outer = new JSONObject();
			outer.put("com.vimukti.accounter.shared.common.PaymentTerm", inner);
			inner.put("@_oid", context.get("PaymentTerms", paymentTerm.getID()));
			inner.put("@_lid", context.getLocalIdProvider().getOrCreate(paymentTerm));
			jsonObject.put("paymentTerm", outer);
		}
		// shippingMethod
		ShippingMethod shippingMethod = obj.getShippingMethod();
		if (shippingMethod != null) {
			JSONObject inner = new JSONObject();
			JSONObject outer = new JSONObject();
			outer.put("com.vimukti.accounter.shared.common.ShippingMethod", inner);
			inner.put("@_oid", context.get("ShippingMethod", shippingMethod.getID()));
			inner.put("@_lid", context.getLocalIdProvider().getOrCreate(shippingMethod));
			jsonObject.put("shippingMethod", outer);
		}
		// shippingTerms
		ShippingTerms shippingTerms = obj.getShippingTerms();
		if (shippingTerms != null) {
			JSONObject inner = new JSONObject();
			JSONObject outer = new JSONObject();
			outer.put("com.vimukti.accounter.shared.common.ShippingTerm", inner);
			inner.put("@_oid", context.get("ShippingTerm", shippingTerms.getID()));
			inner.put("@_lid", context.getLocalIdProvider().getOrCreate(shippingTerms));
			jsonObject.put("shippingTerm", outer);
		}

		if (obj.getStatus() == ClientTransaction.STATUS_CANCELLED) {
			jsonObject.put("purchaseOrderstatus", "CANCELLED");
		}
		return external;
	}
}
