package com.nitya.accounter.migration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.nitya.accounter.core.ShippingMethod;
import com.nitya.accounter.core.TAXCode;
import com.nitya.accounter.core.TAXItem;
import com.nitya.accounter.core.Vendor;
import com.nitya.accounter.core.VendorGroup;

public class VendorMigrator implements IMigrator<Vendor> {

	@Override
	public JSONObject migrate(Vendor obj, MigratorContext context) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		JSONObject external = new JSONObject();
		external.put("com.nitya.accounter.shared.vendor.Vendor", jsonObject);
		String _lid = context.getLocalIdProvider().getOrCreate(obj);
		jsonObject.put("@id", _lid);
		jsonObject.put("@_lid", _lid);

		CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);
		VendorGroup vendorGroup = obj.getVendorGroup();
		if (vendorGroup != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.nitya.accounter.shared.vendor.VendorGroup", inter);
			String localId = context.getLocalIdProvider().getOrCreate(vendorGroup);
			inter.put("@_oid", context.get("VendorGroup", vendorGroup.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("vendorGroup", outter);
		}

		TAXItem taxItem2 = obj.getTAXItem();
		if (taxItem2 != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.nitya.accounter.shared.tax.TAXITem", inter);
			String localId = context.getLocalIdProvider().getOrCreate(taxItem2);
			inter.put("@_oid", context.get("Tax", taxItem2.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("taxItem", outter);
		}

		// RelationShip field
		jsonObject.put("name", obj.getName());
		jsonObject.put("comments", obj.getMemo());
		jsonObject.put("email", obj.getEmail());
		jsonObject.put("phone", obj.getPhoneNo());
		jsonObject.put("fax", obj.getFaxNo());
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
		jsonObject.put("inActive", !obj.isActive());

		// BussinessRelationShip Fields
		jsonObject.put("tDSApplicable", obj.isTdsApplicable());
		jsonObject.put("companyName", obj.getCompany().getTradingName());
		FinanceDate payeeSince = obj.getPayeeSince();
		if (payeeSince == null) {
			payeeSince = new FinanceDate(obj.getCreatedDate());
		}
		if (payeeSince != null) {
			jsonObject.put("payeeSince", payeeSince.toEpochDay());
		}
		jsonObject.put("webAddress", obj.getWebPageAddress());
		Map<String, List<Long>> childrenMap = context.getChildrenMap();
		String key = "com.nitya.accounter.shared.common.Contact";
		context.putChilderName("Vendor", "contacts");
		List<Long> list = childrenMap.get(key);
		if (list == null) {
			list = new ArrayList<Long>();
			childrenMap.put(key, list);
		}
		// contacts
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
				String localId = context.getLocalIdProvider().getOrCreate(contact);
				jsonContact.put("@id", localId);
				jsonContact.put("@_lid", localId);
				jsonContact.put("contactName", contactName);
				jsonContact.put("title", contact.getTitle());
				jsonContact.put("businessPhone", contact.getBusinessPhone());
				jsonContact.put("email", contact.getEmail());
				jsonContacts.put(jsonContact);

				if (contact.isPrimary()) {

					JSONObject primaryObj = new JSONObject();
					JSONObject objIn = new JSONObject();
					objIn.put("@id", localId);
					objIn.put("@_lid", localId);
					primaryObj.put("com.nitya.accounter.shared.common.Contact", objIn);
					jsonObject.put("primaryContact", primaryObj);
				}

				list.add(contact.getID());
			}
			jsonObject.put("contacts", contactsObj);
		}
		PaymentTerms paymentTerms = obj.getPaymentTerms();
		if (paymentTerms != null) {
			JSONObject inner = new JSONObject();
			JSONObject outer = new JSONObject();
			outer.put("com.nitya.accounter.shared.common.PaymentTerm", inner);
			inner.put("@_oid", context.get("PaymentTerms", paymentTerms.getID()));
			inner.put("@_lid", context.getLocalIdProvider().getOrCreate(paymentTerms));
			jsonObject.put("paymentTerm", outer);
		}
		// currency
		Currency currency = obj.getCurrency();
		JSONObject currencyJson = new JSONObject();
		JSONObject json = new JSONObject();
		currencyJson.put("com.nitya.accounter.shared.common.AccounterCurrency", json);
		json.put("@_oid", context.get("Currency", currency.getID()));
		json.put("@_lid", context.getLocalIdProvider().getOrCreate(currency));
		jsonObject.put("currency", currencyJson);

		jsonObject.put("currencyFactor", obj.getCurrencyFactor());
		Account account = obj.getAccount();
		if (account != null) {
			JSONObject inner = new JSONObject();
			JSONObject outer = new JSONObject();
			outer.put("com.nitya.accounter.shared.common.Account", inner);
			inner.put("@_oid", context.get("Account", account.getID()));
			inner.put("@_lid", context.getLocalIdProvider().getOrCreate(account));
			jsonObject.put("account", outer);
		}
		jsonObject.put("creditLimit", obj.getCreditLimit());
		jsonObject.put("bankName", obj.getBankName());
		jsonObject.put("bankAccountNumber", obj.getBankAccountNo());
		jsonObject.put("bankBranch", obj.getBankBranch());
		jsonObject.put("serviceTaxRegistrationNo", obj.getServiceTaxRegistrationNo());
		jsonObject.put("taxId", obj.getTaxId());
		TAXCode taxCode = obj.getTAXCode();
		if (taxCode != null) {
			JSONObject inner = new JSONObject();
			JSONObject outer = new JSONObject();
			outer.put("com.nitya.accounter.shared.tax.TAXCode", inner);
			inner.put("@_oid", context.get("TaxCode", taxCode.getID()));
			inner.put("@_lid", context.getLocalIdProvider().getOrCreate(taxCode));
			jsonObject.put("taxCode", outer);
		}
		String paymentMethod = obj.getPaymentMethod();
		if (paymentMethod != null) {
			jsonObject.put("paymentMethod", PicklistUtilMigrator.getPaymentMethodIdentifier(paymentMethod));
		} else {
			jsonObject.put("paymentMethod", "CASH");
		}
		ShippingMethod shippingMethod = obj.getShippingMethod();
		if (shippingMethod != null) {
			JSONObject inner = new JSONObject();
			JSONObject outer = new JSONObject();
			outer.put("com.nitya.accounter.shared.common.ShippingMethod", inner);
			inner.put("@_oid", context.get("ShippingMethod", shippingMethod.getID()));
			inner.put("@_lid", context.getLocalIdProvider().getOrCreate(shippingMethod));
			jsonObject.put("preferredShippingMethod", outer);
		}
		jsonObject.put("vATRegistrationNumber", obj.getVATRegistrationNumber());

		jsonObject.put("tDSApplicable", obj.isTdsApplicable());
		// Tds Applicable is enble then only set taxItem\
		if (obj.isTdsApplicable()) {
			TAXItem taxItem = obj.getTAXItem();
			if (taxItem != null) {
				JSONObject inner = new JSONObject();
				JSONObject outer = new JSONObject();
				outer.put("com.nitya.accounter.shared.tax.TAXItem", inner);
				inner.put("@_oid", context.get("Tax", taxItem.getID()));
				inner.put("@_lid", context.getLocalIdProvider().getOrCreate(taxItem));
				jsonObject.put("vendorTDSCode", outer);
			}
		}

		jsonObject.put("openingBalance", BigDecimal.valueOf(obj.getOpeningBalance()));
		return external;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}

}
