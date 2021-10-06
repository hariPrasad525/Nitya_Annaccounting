package com.nitya.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.Address;
import com.nitya.accounter.core.Contact;
import com.nitya.accounter.core.Currency;
import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.core.PaymentTerms;
import com.nitya.accounter.core.TAXAgency;

public class TaxAgencyMigrator implements IMigrator<TAXAgency> {

	@Override
	public JSONObject migrate(TAXAgency obj, MigratorContext context) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		JSONObject external = new JSONObject();
		{
			String _lid = context.getLocalIdProvider().getOrCreate(obj);
			jsonObject.put("@id", _lid);
			jsonObject.put("@_lid", _lid);
			external.put("com.nitya.accounter.shared.tax.TaxAgency", jsonObject);
		}
		CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);
		jsonObject.put("name", obj.getName());
		jsonObject.put("inactive", !obj.isActive());
		// TAX AGENCY SNICE
		FinanceDate payeeSince = obj.getPayeeSince();
		if (payeeSince != null) {
			jsonObject.put("payeeSince", payeeSince.toEpochDay());
		} else {
			jsonObject.put("payeeSince", new FinanceDate(obj.getCreatedDate()).toEpochDay());
		}
		// Setting object PaymentTerm
		PaymentTerms paymentTerm = obj.getPaymentTerm();
		if (paymentTerm != null) {
			JSONObject purLiaAcc = new JSONObject();
			JSONObject json = new JSONObject();
			purLiaAcc.put("com.nitya.accounter.shared.common.PaymentTerm", json);
			String parentLocalID = context.getLocalIdProvider().getOrCreate(paymentTerm);
			json.put("@_oid", context.get("PaymentTerms", paymentTerm.getID()));
			json.put("@_lid", parentLocalID);
			jsonObject.put("paymentTerm", purLiaAcc);
		}
		jsonObject.put("taxType", getTaxTypeString(obj.getTaxType()));
		// Setting Purchase Liability Account of company
		Account purchaseLiabilityAccount = obj.getPurchaseLiabilityAccount();
		if (purchaseLiabilityAccount != null) {
			JSONObject purLiaAcc = new JSONObject();
			JSONObject json = new JSONObject();
			purLiaAcc.put("com.nitya.accounter.shared.common.Account", json);
			String parentLocalID = context.getLocalIdProvider().getOrCreate(purchaseLiabilityAccount);
			json.put("@_oid", context.get("Account", purchaseLiabilityAccount.getID()));
			json.put("@_lid", parentLocalID);
			jsonObject.put("purchaseLiabilityAccount", purLiaAcc);
		}
		// Setting Sales Liability Account of Company
		Account salesLiabilityAccount = obj.getSalesLiabilityAccount();
		if (salesLiabilityAccount != null) {
			JSONObject salesLibAcc = new JSONObject();
			JSONObject json = new JSONObject();
			salesLibAcc.put("com.nitya.accounter.shared.common.Account", json);
			String parentLocalID = context.getLocalIdProvider().getOrCreate(salesLiabilityAccount);
			json.put("@_oid", context.get("Account", salesLiabilityAccount.getID()));
			json.put("@_lid", parentLocalID);
			jsonObject.put("salesLiabilityAccount", salesLibAcc);
		}
		// Setting Filed Liability Account of Company
		Account filedLiabilityAccount = obj.getFiledLiabilityAccount();
		if (filedLiabilityAccount != null) {
			JSONObject filedLiabibilyAccount = new JSONObject();
			JSONObject json = new JSONObject();
			filedLiabibilyAccount.put("com.nitya.accounter.shared.common.Account", json);
			String parentLocalID = context.getLocalIdProvider().getOrCreate(filedLiabilityAccount);
			json.put("@_oid", context.get("Account", filedLiabilityAccount.getID()));
			json.put("@_lid", parentLocalID);
			jsonObject.put("filedLiabilityAccount", filedLiabibilyAccount);
		}
		FinanceDate asDateObject = obj.getLastTAXReturnDate();
		if (asDateObject != null) {
			jsonObject.put("lastFileTaxDate", asDateObject.toEpochDay());
		}
		jsonObject.put("email", obj.getEmail());
		jsonObject.put("fax", obj.getFaxNo());
		jsonObject.put("phone", obj.getPhoneNo());
		jsonObject.put("webAddress", obj.getWebPageAddress());
		// Addresses
		Address shipToAddress = null;
		Address billToAddress = null;
		for (Address primaryAddress : obj.getAddress()) {
			if (primaryAddress.getType() == Address.TYPE_BILL_TO) {
				billToAddress = primaryAddress;
			}
			if (primaryAddress.getType() == Address.TYPE_SHIP_TO) {
				shipToAddress = primaryAddress;
			}
		}
		// SHIP TO
		if (shipToAddress != null) {
			JSONObject address = new JSONObject();
			JSONObject jsonAddress = new JSONObject();
			address.put("org.ecgine.core.shared.Address", jsonAddress);
			String shipToLocalID = context.getLocalIdProvider().getOrCreate(shipToAddress);
			jsonAddress.put("@_lid", shipToLocalID);
			jsonAddress.put("@id", shipToLocalID);
			jsonAddress.put("street", shipToAddress.getStreet());
			jsonAddress.put("city", shipToAddress.getCity());
			jsonAddress.put("stateOrProvince", shipToAddress.getStateOrProvinence());
			jsonAddress.put("zipOrPostalCode", shipToAddress.getZipOrPostalCode());
			jsonAddress.put("country", shipToAddress.getCountryOrRegion());
			jsonObject.put("shipTo", address);
		}
		// BILL TO
		if (billToAddress != null) {
			JSONObject address = new JSONObject();
			JSONObject jsonAddress = new JSONObject();
			address.put("org.ecgine.core.shared.Address", jsonAddress);
			String addressLocalID = context.getLocalIdProvider().getOrCreate(billToAddress);
			jsonAddress.put("@_lid", addressLocalID);
			jsonAddress.put("@id", addressLocalID);
			jsonAddress.put("street", billToAddress.getStreet());
			jsonAddress.put("city", billToAddress.getCity());
			jsonAddress.put("stateOrProvince", billToAddress.getStateOrProvinence());
			jsonAddress.put("zipOrPostalCode", billToAddress.getZipOrPostalCode());
			jsonAddress.put("country", billToAddress.getCountryOrRegion());
			jsonObject.put("billTo", address);
		}
		// Contacts
		if (obj.getContacts() != null && !obj.getContacts().isEmpty()) {
			JSONArray jsonContacts = new JSONArray();
			JSONObject contactsObj = new JSONObject();
			contactsObj.put("com.nitya.accounter.shared.common.Contact", jsonContacts);
			for (Contact contact : obj.getContacts()) {
				String contactName = contact.getName();
				if (contactName == null || contactName.equals("")) {
					continue;
				}
				JSONObject jsonContact = new JSONObject();
				JSONObject json = new JSONObject();
				String localId = context.getLocalIdProvider().getOrCreate(contact);
				json.put("@id", localId);
				json.put("@_lid", localId);
				jsonContact.put("contactName", contactName);
				jsonContact.put("title", contact.getTitle());
				jsonContact.put("businessPhone", contact.getBusinessPhone());
				jsonContact.put("email", contact.getEmail());
				jsonContacts.put(jsonContact);
			}
			jsonObject.put("contacts", contactsObj);
		}
		{
			Currency currency = obj.getCurrency();
			JSONObject currencyJson = new JSONObject();
			JSONObject json = new JSONObject();
			currencyJson.put("com.nitya.accounter.shared.common.AccounterCurrency", json);
			json.put("@_oid", context.get("Currency", currency.getID()));
			json.put("@_lid", context.getLocalIdProvider().getOrCreate(currency));
			jsonObject.put("currency", currencyJson);
		}
		jsonObject.put("currencyFactor", obj.getCurrencyFactor());

		return external;
	}

	private static String getTaxTypeString(int taxType) {
		if (taxType == TAXAgency.TAX_TYPE_SALESTAX) {
			return "SALES_TAX";
		}
		if (taxType == TAXAgency.TAX_TYPE_VAT) {
			return "VAT";
		}
		if (taxType == 0 || taxType == TAXAgency.TAX_TYPE_SERVICETAX) {
			return "SERVICE_TAX";
		}
		if (taxType == TAXAgency.TAX_TYPE_OTHER) {
			return "OTHER";
		}
		if (taxType == TAXAgency.TAX_TYPE_TDS) {
			return "TDS";
		}
		return null;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}
