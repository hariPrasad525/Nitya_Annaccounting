package com.vimukti.accounter.migration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ibm.icu.math.BigDecimal;
import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.CustomerGroup;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.PriceLevel;
import com.vimukti.accounter.core.SalesPerson;
import com.vimukti.accounter.core.ShippingMethod;
import com.vimukti.accounter.core.TAXCode;

public class CustomerMigrator implements IMigrator<Customer> {

	@Override
	public JSONObject migrate(Customer obj, MigratorContext context) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		JSONObject external = new JSONObject();
		external.put("com.vimukti.accounter.shared.customer.Customer", jsonObject);
		String _lid = context.getLocalIdProvider().getOrCreate(obj);
		jsonObject.put("@id", _lid);
		jsonObject.put("@_lid", _lid);
		CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);

		PriceLevel priceLevel = obj.getPriceLevel();
		if (priceLevel != null) {
			JSONObject plev = new JSONObject();
			JSONObject json = new JSONObject();
			plev.put("com.vimukti.accounter.shared.customer.PriceLevel", json);
			String parentLocalID = context.getLocalIdProvider().getOrCreate(priceLevel);
			json.put("@_oid", context.get("PriceLevel", priceLevel.getID()));
			json.put("@_lid", parentLocalID);
			jsonObject.put("priceLevel", plev);

		}
		SalesPerson salesPerson = obj.getSalesPerson();
		if (salesPerson != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			inter.put("com.vimukti.accounter.shared.customer.SalesPerson", outter);
			String localId = context.getLocalIdProvider().getOrCreate(salesPerson);
			outter.put("@_oid", context.get("SalesPerson", salesPerson.getID()));
			outter.put("@_lid", localId);
			jsonObject.put("salesPerson", inter);
		}
		CustomerGroup customerGroup = obj.getCustomerGroup();
		if (customerGroup != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			inter.put("com.vimukti.accounter.shared.customer.CustomerGroup", outter);
			String localId = context.getLocalIdProvider().getOrCreate(customerGroup);
			outter.put("@_oid", context.get("CustomerGroup", customerGroup.getID()));
			outter.put("@_lid", localId);
			jsonObject.put("customerGroup", inter);
		}
		jsonObject.put("cSTNumber", obj.getCSTno());

		jsonObject.put("taxPayerIdentificationNo", obj.getTINNumber());
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
		jsonObject.put("tDSApplicable", obj.isWillDeductTDS());
		jsonObject.put("companyName", obj.getCompany().getTradingName());
		FinanceDate payeeSince = obj.getPayeeSince();
		if (payeeSince != null) {
			jsonObject.put("payeeSince", payeeSince.toEpochDay());
		} else {
			jsonObject.put("payeeSince", new FinanceDate(obj.getCreatedDate()).toEpochDay());
		}
		jsonObject.put("webAddress", obj.getWebPageAddress());

		// altEmail and altPhone are not found
		Map<String, List<Long>> childrenMap = context.getChildrenMap();
		String key = "com.vimukti.accounter.shared.common.Contact";
		context.putChilderName("Customer", "contacts");
		List<Long> list = childrenMap.get(key);
		if (list == null) {
			list = new ArrayList<Long>();
			childrenMap.put(key, list);
		}
		if (obj.getContacts() != null && !obj.getContacts().isEmpty()) {
			JSONArray jsonContacts = new JSONArray();
			JSONObject contactsObj = new JSONObject();
			contactsObj.put("com.vimukti.accounter.shared.common.Contact", jsonContacts);
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
					JSONObject primaryContact = new JSONObject();
					JSONObject jsonIn = new JSONObject();
					primaryContact.put("com.vimukti.accounter.shared.common.Contact", jsonIn);
					jsonIn.put("@id", localId);
					jsonIn.put("@_lid", localId);
					jsonObject.put("primaryContact", primaryContact);
				}
				list.add(contact.getID());
			}
			jsonObject.put("contacts", contactsObj);
		}

		Currency currency = obj.getCurrency();
		JSONObject currencyJson = new JSONObject();
		JSONObject json = new JSONObject();
		currencyJson.put("com.vimukti.accounter.shared.common.AccounterCurrency", json);
		json.put("@_oid", context.get("Currency", currency.getID()));
		json.put("@_lid", context.getLocalIdProvider().getOrCreate(currency));
		jsonObject.put("currency", currencyJson);

		jsonObject.put("currencyFactor", obj.getCurrencyFactor());

		Account account = obj.getAccount();
		if (account != null) {
			JSONObject inner = new JSONObject();
			JSONObject outer = new JSONObject();
			outer.put("com.vimukti.accounter.shared.common.Account", inner);
			inner.put("@_oid", context.get("Account", account.getID()));
			inner.put("@_lid", context.getLocalIdProvider().getOrCreate(account));
			jsonObject.put("account", outer);
		}

		jsonObject.put("creditLimit", BigDecimal.valueOf(obj.getCreditLimit()));
		jsonObject.put("bankName", obj.getBankName());
		jsonObject.put("bankAccountNumber", obj.getBankAccountNo());
		jsonObject.put("bankBranch", obj.getBankBranch());
		jsonObject.put("serviceTaxRegistrationNo", obj.getServiceTaxRegistrationNo());

		TAXCode taxCode = obj.getTAXCode();
		if (taxCode != null) {
			JSONObject inner = new JSONObject();
			JSONObject outer = new JSONObject();
			outer.put("com.vimukti.accounter.shared.tax.TAXCode", inner);
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

		PaymentTerms paymentTerm = obj.getPaymentTerm();
		if (paymentTerm != null) {
			JSONObject inner = new JSONObject();
			JSONObject outer = new JSONObject();
			outer.put("com.vimukti.accounter.shared.common.PaymentTerm", inner);
			inner.put("@_oid", context.get("PaymentTerms", paymentTerm.getID()));
			inner.put("@_lid", context.getLocalIdProvider().getOrCreate(paymentTerm));
			jsonObject.put("paymentTerm", outer);
		}

		ShippingMethod shippingMethod = obj.getShippingMethod();
		if (shippingMethod != null) {
			JSONObject inner = new JSONObject();
			JSONObject outer = new JSONObject();
			outer.put("com.vimukti.accounter.shared.common.ShippingMethod", inner);
			inner.put("@_oid", context.get("ShippingMethod", shippingMethod.getID()));
			inner.put("@_lid", context.getLocalIdProvider().getOrCreate(shippingMethod));
			jsonObject.put("preferredShippingMethod", outer);
		}

		jsonObject.put("vATRegistrationNumber", obj.getVATRegistrationNumber());
		jsonObject.put("openingBalance", BigDecimal.valueOf(obj.getOpeningBalance()));

		return external;

	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}
