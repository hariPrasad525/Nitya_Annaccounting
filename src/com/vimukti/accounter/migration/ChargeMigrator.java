package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.PaymentTerms;

public class ChargeMigrator extends TransactionMigrator<Estimate> {
	@Override
	public JSONObject migrate(Estimate obj, MigratorContext context) throws JSONException {
		JSONObject jsonObj = super.migrate(obj, context);
		jsonObj.put("payee", context.get("Customer", obj.getCustomer().getID()));
		FinanceDate expirationDate = obj.getExpirationDate();
		if (expirationDate != null) {
			jsonObj.put("expirationDate", expirationDate.toEpochDay());
		}
		Contact contact = obj.getContact();
		if (contact != null) {
			jsonObj.put("contact", context.get("Contact", contact.getID()));
		}
		jsonObj.put("phone", obj.getPhone());

		JSONObject jsonShippingAddr = new JSONObject();
		Address shipingAddr = obj.getShippingAdress();
		if (shipingAddr != null) {
			jsonShippingAddr.put("street", shipingAddr.getStreet());
			jsonShippingAddr.put("city", shipingAddr.getCity());
			jsonShippingAddr.put("stateOrProvince", shipingAddr.getStateOrProvinence());
			jsonShippingAddr.put("zipOrPostalCode", shipingAddr.getZipOrPostalCode());
			jsonShippingAddr.put("country", shipingAddr.getCountryOrRegion());
			jsonObj.put("shipTo", jsonShippingAddr);
		}

		JSONObject jsonBillingAddr = new JSONObject();
		Address billingAddr = obj.getAddress();
		if (billingAddr != null) {
			jsonBillingAddr.put("street", billingAddr.getStreet());
			jsonBillingAddr.put("city", billingAddr.getCity());
			jsonBillingAddr.put("stateOrProvince", billingAddr.getStateOrProvinence());
			jsonBillingAddr.put("zipOrPostalCode", billingAddr.getZipOrPostalCode());
			jsonBillingAddr.put("country", billingAddr.getCountryOrRegion());
			jsonObj.put("billTo", jsonBillingAddr);
		}
		PaymentTerms paymentTerm = obj.getPaymentTerm();
		if (paymentTerm != null) {
			jsonObj.put("paymentTerm", context.get("PaymentTerm", paymentTerm.getID()));
		}
		jsonObj.put("deliveryDate", obj.getDeliveryDate().toEpochDay());
		jsonObj.put("transactionType", "SalesQuotation");
		if (obj.getStatus() == Estimate.STATUS_REJECTED) {
			// jsonObj.put("quoteStatus",
			// context.getPickListContext().get("QuotationStatus", "Rejected"));
		}
		jsonObj.put("quotationType", PicklistUtilMigrator.getQuotationTypeIdentifier(1));
		return jsonObj;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		criteria.add(Restrictions.eq("estimateType", Estimate.CHARGES));
	}
}
