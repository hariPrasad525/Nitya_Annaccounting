package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.PaymentTerms;

public class SalesQuotationMigrator extends TransactionMigrator<Estimate> {
	@Override
	public JSONObject migrate(Estimate obj, MigratorContext context) throws JSONException {
		if (!(obj.getEstimateType() == Estimate.QUOTES || obj.getEstimateType() == Estimate.BILLABLEEXAPENSES
				|| obj.getEstimateType() == Estimate.CHARGES)) {
			return null;
		}
		JSONObject jsonObj = super.migrate(obj, context);
		JSONObject external = new JSONObject();
		external.put("com.vimukti.accounter.shared.customer.SalesQuotation", jsonObj);
		String _lid = context.getLocalIdProvider().getOrCreate(obj);
		jsonObj.put("@id", _lid);
		jsonObj.put("@_lid", _lid);
		{
			Customer customer = obj.getCustomer();
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.customer.Customer", inter);
			inter.put("@_oid", context.get("Customer", customer.getID()));
			inter.put("@_lid", context.getLocalIdProvider().getOrCreate(customer));
			jsonObj.put("payee", outter);
		}
		FinanceDate expirationDate = obj.getExpirationDate();
		if (expirationDate != null) {
			jsonObj.put("expirationDate", expirationDate.toEpochDay());
		}
		Contact contact = obj.getContact();
		if (contact != null && contact.getID() != 0L) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.common.Contact", inter);
			inter.put("@_oid", context.get("com.vimukti.accounter.shared.common.Contact", contact.getID()));
			inter.put("@_lid", context.getLocalIdProvider().getOrCreate(contact));
			jsonObj.put("contact", outter);
		}
		jsonObj.put("phone", obj.getPhone());

		Address shipingAddr = obj.getShippingAdress();
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

		Address billingAddr = obj.getAddress();
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
		PaymentTerms paymentTerm = obj.getPaymentTerm();
		if (paymentTerm != null) {
			JSONObject inner = new JSONObject();
			JSONObject outer = new JSONObject();
			outer.put("com.vimukti.accounter.shared.common.PaymentTerm", inner);
			inner.put("@_oid", context.get("PaymentTerms", paymentTerm.getID()));
			inner.put("@_lid", context.getLocalIdProvider().getOrCreate(paymentTerm));
			jsonObj.put("paymentTerm", inner);
		}

		jsonObj.put("deliveryDate", obj.getDeliveryDate().toEpochDay());
		jsonObj.put("transactionType", "SALES_QUOTATION");
		if (obj.getStatus() == Estimate.STATUS_REJECTED) {
			jsonObj.put("quoteStatus", "REJECTED");
		}
		jsonObj.put("quotationType", PicklistUtilMigrator.getQuotationTypeIdentifier(obj.getEstimateType()));
		return external;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
	}
}
