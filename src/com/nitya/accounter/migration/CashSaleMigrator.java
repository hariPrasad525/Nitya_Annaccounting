package com.nitya.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.Address;
import com.nitya.accounter.core.CashSales;
import com.nitya.accounter.core.Contact;
import com.nitya.accounter.core.Customer;
import com.nitya.accounter.core.Estimate;
import com.nitya.accounter.core.ShippingMethod;
import com.nitya.accounter.core.ShippingTerms;

public class CashSaleMigrator extends TransactionMigrator<CashSales> {
	@Override
	public JSONObject migrate(CashSales obj, MigratorContext context) throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);
		String _lid = context.getLocalIdProvider().getOrCreate(obj);
		jsonObject.put("@id", _lid);
		jsonObject.put("@_lid", _lid);
		JSONObject external = new JSONObject();
		external.put("com.nitya.accounter.shared.customer.CashSale", jsonObject);

		// Account
		Account depositIn = obj.getDepositIn();
		if (depositIn != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.nitya.accounter.shared.common.Account", inter);
			String localId = context.getLocalIdProvider().getOrCreate(depositIn);
			inter.put("@_oid", context.get("Account", depositIn.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("depositIn", outter);
		}

		jsonObject.put("phone", obj.getPhone());

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
			jsonObject.put("billTo", address);
		}
		Address shipingTo = obj.getShippingAdress();
		if (shipingTo != null) {
			JSONObject address = new JSONObject();
			JSONObject jsonAddress = new JSONObject();
			address.put("org.ecgine.core.shared.Address", jsonAddress);
			String localId = context.getLocalIdProvider().getOrCreate(shipingTo);
			jsonAddress.put("@_lid", localId);
			jsonAddress.put("@id", localId);
			jsonAddress.put("street", shipingTo.getStreet());
			jsonAddress.put("city", shipingTo.getCity());
			jsonAddress.put("stateOrProvince", shipingTo.getStateOrProvinence());
			jsonAddress.put("zipOrPostalCode", shipingTo.getZipOrPostalCode());
			jsonAddress.put("country", shipingTo.getCountryOrRegion());
			jsonObject.put("shipTo", address);
		}

		List<Estimate> salesOrders = obj.getSalesOrders();
		if (!salesOrders.isEmpty()) {
			JSONArray array = new JSONArray();
			JSONObject items = new JSONObject();
			items.put("com.nitya.accounter.shared.customer.SalesOrder", array);
			for (Estimate quot : salesOrders) {
				JSONObject inter = new JSONObject();
				JSONObject outter = new JSONObject();
				String localId = context.getLocalIdProvider().getOrCreate(quot);
				inter.put("@_oid", context.get("SalesOrder", quot.getID()));
				inter.put("@_lid", localId);
				array.put(outter);
			}
			jsonObject.put("salesOrders", items);
		}
		ShippingTerms shippingTerm = obj.getShippingTerm();
		if (shippingTerm != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.nitya.accounter.shared.common.ShippingTerm", inter);
			String localId = context.getLocalIdProvider().getOrCreate(shippingTerm);
			inter.put("@_oid", context.get("ShippingTerm", shippingTerm.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("shippingTerm", outter);
		}
		ShippingMethod shippingMethod = obj.getShippingMethod();
		if (shippingMethod != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.nitya.accounter.shared.common.ShippingMethod", inter);
			String localId = context.getLocalIdProvider().getOrCreate(shippingMethod);
			inter.put("@_oid", context.get("ShippingMethod", shippingMethod.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("shippingMethod", outter);
		}
		jsonObject.put("deliveryDate", obj.getDeliverydate().toEpochDay());
		// super (CustomerTransaction field)
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
		Customer customer = obj.getCustomer();
		if (customer != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.nitya.accounter.shared.customer.Customer", inter);
			String localId = context.getLocalIdProvider().getOrCreate(customer);
			inter.put("@_oid", context.get("Customer", customer.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("payee", outter);
		}

		String paymentMethod = obj.getPaymentMethod();
		if (paymentMethod != null) {
			jsonObject.put("paymentMethod", PicklistUtilMigrator.getPaymentMethodIdentifier(paymentMethod));
		} else {
			jsonObject.put("paymentMethod", "CASH");
		}
		try {
			jsonObject.put("chequeNumber", Long.parseLong(obj.getCheckNumber()));
		} catch (NumberFormatException nfe) {

		}
		return external;
	}
}
