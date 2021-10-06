package com.vimukti.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.core.Invoice;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.ShippingMethod;
import com.vimukti.accounter.core.ShippingTerms;

public class InvoiceMigrator extends TransactionMigrator<Invoice> {

	@Override
	public JSONObject migrate(Invoice obj, MigratorContext context) throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);
		if (jsonObject == null) {
			return null;
		}
		JSONObject external = new JSONObject();
		external.put("com.vimukti.accounter.shared.customer.Invoice", jsonObject);
		String _lid = context.getLocalIdProvider().getOrCreate(obj);
		jsonObject.put("@id", _lid);
		jsonObject.put("@_lid", _lid);
		Customer customer = obj.getCustomer();
		{
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.customer.Customer", inter);
			String localId = context.getLocalIdProvider().getOrCreate(customer);
			inter.put("@_oid", context.get("Customer", customer.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("payee", outter);
		}

		// DueDate
		jsonObject.put("dueDate", obj.getDueDate().toEpochDay());
		// BillTo
		Address billTo = obj.getBillingAddress();
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

		// shipTo
		Address shipTo = obj.getBillingAddress();
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

		// shipping Term
		ShippingTerms shippingTerm = obj.getShippingTerm();
		if (shippingTerm != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.common.ShippingTerm", inter);
			String localId = context.getLocalIdProvider().getOrCreate(shippingTerm);
			inter.put("@_oid", context.get("ShippingTerm", shippingTerm.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("shippingTerm", outter);
		}

		// ShippingMethod
		ShippingMethod shippingMethod = obj.getShippingMethod();
		if (shippingMethod != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.common.ShippingMethod", inter);
			String localId = context.getLocalIdProvider().getOrCreate(shippingMethod);
			inter.put("@_oid", context.get("ShippingMethod", shippingMethod.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("shippingMethod", outter);
		}

		// delivery Date
		jsonObject.put("deliveryDate", obj.getDeliverydate().toEpochDay());

		// Sales Orders
		List<Estimate> estimates = obj.getEstimates();
		if (!estimates.isEmpty()) {
			JSONArray array = new JSONArray();
			JSONObject salesOrders = new JSONObject();
			salesOrders.put("com.vimukti.accounter.shared.customer.SalesOrder", array);
			for (Estimate estimate : estimates) {
				if (estimate.getEstimateType() == Estimate.SALES_ORDER) {
					JSONObject inner = new JSONObject();
					inner.put("@_oid", context.get("SalesOrder", estimate.getID()));
					inner.put("@_lid", context.getLocalIdProvider().getOrCreate(estimate));
					array.put(inner);
				}
			}
			if (array.length() > 0) {
				jsonObject.put("salesOrders", array);
			}
		}
		// Credits
		if (!estimates.isEmpty()) {
			JSONArray array = new JSONArray();
			JSONObject credits = new JSONObject();
			credits.put("com.vimukti.accounter.shared.customer.Credit", array);
			for (Estimate estimate : estimates) {
				if (estimate.getEstimateType() == Estimate.CREDITS) {
					JSONObject inner = new JSONObject();
					inner.put("@_oid", context.get("Credit", estimate.getID()));
					inner.put("@_lid", context.getLocalIdProvider().getOrCreate(estimate));
					array.put(inner);
				}
			}
			if (array.length() > 0) {
				jsonObject.put("credits", credits);
			}
		}
		// Sales Quotations
		if (!estimates.isEmpty()) {
			JSONArray array = new JSONArray();
			JSONObject salesQuotes = new JSONObject();
			salesQuotes.put("com.vimukti.accounter.shared.customer.SalesQuotation", array);
			for (Estimate estimate : estimates) {
				if (estimate.getEstimateType() == Estimate.QUOTES) {
					JSONObject inner = new JSONObject();
					inner.put("@_oid", context.get("SalesQuotation", estimate.getID()));
					inner.put("@_lid", context.getLocalIdProvider().getOrCreate(estimate));
					array.put(inner);
				}
			}
			if (array.length() > 0) {
				jsonObject.put("quotations", salesQuotes);
			}
		}
		// Billable Expenses not migrating
		if (!estimates.isEmpty()) {
			JSONArray array = new JSONArray();
			JSONObject salesQuotes = new JSONObject();
			salesQuotes.put("com.vimukti.accounter.shared.customer.SalesQuotation", array);
			for (Estimate estimate : estimates) {
				if (estimate.getEstimateType() == Estimate.BILLABLEEXAPENSES) {
					JSONObject inner = new JSONObject();
					inner.put("@_oid", context.get("SalesQuotation", estimate.getID()));
					inner.put("@_lid", context.getLocalIdProvider().getOrCreate(estimate));
					array.put(inner);
				}
			}
			if (array.length() > 0) {
				jsonObject.put("billableExpenses", salesQuotes);
			}
		}
		// Setting object PaymentTerm
		PaymentTerms paymentTerm = obj.getPaymentTerm();
		if (paymentTerm != null) {
			{
				JSONObject inner = new JSONObject();
				JSONObject outer = new JSONObject();
				outer.put("com.vimukti.accounter.shared.common.PaymentTerm", inner);
				inner.put("@_oid", context.get("PaymentTerms", paymentTerm.getID()));
				inner.put("@_lid", context.getLocalIdProvider().getOrCreate(paymentTerm));
				jsonObject.put("paymentTerm", outer);
			}

		}

		// Contact
		Contact contact = obj.getContact();
		if (contact != null && !contact.getName().isEmpty()) {
			{
				JSONObject inter1 = new JSONObject();
				JSONObject outter1 = new JSONObject();
				outter1.put("com.vimukti.accounter.shared.common.Contact", inter1);
				String localId1 = context.getLocalIdProvider().getOrCreate(contact);
				inter1.put("@_oid", context.get("Contact", contact.getID()));
				inter1.put("@_lid", localId1);
				// jsonObject.put("contact", outter1);
			}

		}
		return external;
	}
}
