package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.ShippingMethod;
import com.vimukti.accounter.core.ShippingTerms;

public class SalesOrderMigrator extends TransactionMigrator<Estimate> {
	@Override
	public JSONObject migrate(Estimate estimate, MigratorContext context) throws JSONException {
		JSONObject jsonObj = super.migrate(estimate, context);
		JSONObject external = new JSONObject();
		external.put("com.vimukti.accounter.shared.customer.SalesOrder", jsonObj);
		String _lid = context.getLocalIdProvider().getOrCreate(estimate);
		jsonObj.put("@id", _lid);
		jsonObj.put("@_lid", _lid);
		Customer customer = estimate.getCustomer();
		if (customer != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.customer.Customer", inter);
			String localId = context.getLocalIdProvider().getOrCreate(customer);
			inter.put("@_oid", context.get("Customer", customer.getID()));
			inter.put("@_lid", localId);
			jsonObj.put("payee", outter);
		}

		Contact contact = estimate.getContact();
		if (contact != null && !contact.getName().isEmpty()) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.common.Contact", inter);
			String localId = context.getLocalIdProvider().getOrCreate(contact);
			inter.put("@_oid", context.get("Contact", contact.getID()));
			inter.put("@_lid", localId);
			jsonObj.put("contact", outter);
		}
		jsonObj.put("phone", estimate.getPhone());

		Address shipingAddr = estimate.getShippingAdress();
		if (shipingAddr != null) {
			JSONObject address = new JSONObject();
			JSONObject jsonAddress = new JSONObject();
			address.put("org.ecgine.core.shared.Address", jsonAddress);
			String tradingAddressLocalID = context.getLocalIdProvider().getOrCreate(shipingAddr);
			jsonAddress.put("@_lid", tradingAddressLocalID);
			jsonAddress.put("@id", tradingAddressLocalID);
			jsonAddress.put("street", shipingAddr.getStreet());
			jsonAddress.put("city", shipingAddr.getCity());
			jsonAddress.put("stateOrProvince", shipingAddr.getStateOrProvinence());
			jsonAddress.put("zipOrPostalCode", shipingAddr.getZipOrPostalCode());
			jsonAddress.put("country", shipingAddr.getCountryOrRegion());
			jsonObj.put("shipTo", address);
		}

		Address billingAddr = estimate.getAddress();
		if (billingAddr != null) {
			JSONObject address = new JSONObject();
			JSONObject jsonAddress = new JSONObject();
			address.put("org.ecgine.core.shared.Address", jsonAddress);
			String tradingAddressLocalID = context.getLocalIdProvider().getOrCreate(billingAddr);
			jsonAddress.put("@_lid", tradingAddressLocalID);
			jsonAddress.put("@id", tradingAddressLocalID);
			jsonAddress.put("street", billingAddr.getStreet());
			jsonAddress.put("city", billingAddr.getCity());
			jsonAddress.put("stateOrProvince", billingAddr.getStateOrProvinence());
			jsonAddress.put("zipOrPostalCode", billingAddr.getZipOrPostalCode());
			jsonAddress.put("country", billingAddr.getCountryOrRegion());
			jsonObj.put("billTo", address);
		}
		jsonObj.put("customerReference", estimate.getReference());

		PaymentTerms paymentTerm = estimate.getPaymentTerm();
		if (paymentTerm != null) {
			JSONObject inner = new JSONObject();
			JSONObject outer = new JSONObject();
			outer.put("com.vimukti.accounter.shared.common.PaymentTerm", inner);
			inner.put("@_oid", context.get("PaymentTerms", paymentTerm.getID()));
			inner.put("@_lid", context.getLocalIdProvider().getOrCreate(paymentTerm));
			jsonObj.put("paymentTerm", outer);
		}
		ShippingMethod shippingMethod = estimate.getShippingMethod();
		if (shippingMethod != null) {
			JSONObject inner = new JSONObject();
			JSONObject outer = new JSONObject();
			outer.put("com.vimukti.accounter.shared.common.ShippingMethod", inner);
			inner.put("@_oid", context.get("ShippingMethod", shippingMethod.getID()));
			inner.put("@_lid", context.getLocalIdProvider().getOrCreate(shippingMethod));
			jsonObj.put("shippingMethod", outer);
		}
		ShippingTerms shippingTerm = estimate.getShippingTerm();
		if (shippingTerm != null) {
			JSONObject inner = new JSONObject();
			JSONObject outer = new JSONObject();
			outer.put("com.vimukti.accounter.shared.common.ShippingTerm", inner);
			inner.put("@_oid", context.get("ShippingTerm", shippingTerm.getID()));
			inner.put("@_lid", context.getLocalIdProvider().getOrCreate(shippingTerm));
			jsonObj.put("shippingTerm", outer);
		}
		jsonObj.put("deliveryDate", estimate.getDeliveryDate().toEpochDay());
		jsonObj.put("remarks", estimate.getMemo());
		if (estimate.getStatus() == Estimate.STATUS_REJECTED) {
			jsonObj.put("salesOrderStatus", "CANCELLED");
		}
		jsonObj.put("customerOrderNo", estimate.getCustomerOrderNumber());
		return external;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		criteria.add(Restrictions.eq("estimateType", Estimate.SALES_ORDER));
	}
}
