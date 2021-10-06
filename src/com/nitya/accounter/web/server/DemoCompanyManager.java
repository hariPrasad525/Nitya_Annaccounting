package com.nitya.accounter.web.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import com.nitya.accounter.core.TAXReturn;
import com.nitya.accounter.main.ServerConfiguration;
import com.nitya.accounter.web.client.core.ClientAddress;
import com.nitya.accounter.web.client.core.ClientBank;
import com.nitya.accounter.web.client.core.ClientBankAccount;
import com.nitya.accounter.web.client.core.ClientCompanyPreferences;
import com.nitya.accounter.web.client.core.ClientContact;
import com.nitya.accounter.web.client.core.ClientCreditRating;
import com.nitya.accounter.web.client.core.ClientCustomer;
import com.nitya.accounter.web.client.core.ClientEstimate;
import com.nitya.accounter.web.client.core.ClientFiscalYear;
import com.nitya.accounter.web.client.core.ClientFixedAsset;
import com.nitya.accounter.web.client.core.ClientInvoice;
import com.nitya.accounter.web.client.core.ClientItem;
import com.nitya.accounter.web.client.core.ClientItemGroup;
import com.nitya.accounter.web.client.core.ClientPayTAX;
import com.nitya.accounter.web.client.core.ClientPriceLevel;
import com.nitya.accounter.web.client.core.ClientPurchaseOrder;
import com.nitya.accounter.web.client.core.ClientSalesPerson;
import com.nitya.accounter.web.client.core.ClientShippingMethod;
import com.nitya.accounter.web.client.core.ClientShippingTerms;
import com.nitya.accounter.web.client.core.ClientTAXAgency;
import com.nitya.accounter.web.client.core.ClientTAXCode;
import com.nitya.accounter.web.client.core.ClientTAXGroup;
import com.nitya.accounter.web.client.core.ClientTAXItemGroup;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.core.ClientVendor;
import com.nitya.accounter.web.client.core.DemoCompany;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class DemoCompanyManager {

	public static void main(String[] args) throws IOException {
		XStream xStream = new XStream(new DomDriver());

		xStream = createAlias(xStream);

		File file = getFile();

		Object object = xStream.fromXML(new FileInputStream(file));

	}

	private static XStream createAlias(XStream xStream) {

		xStream.alias("ClientCompany", DemoCompany.class);

		xStream.alias("preferences", ClientCompanyPreferences.class);

		xStream.alias("customers", List.class);
		xStream.alias("Customer", ClientCustomer.class);

		xStream.alias("banks", List.class);
		xStream.alias("ClientBank", ClientBank.class);

		xStream.alias("ClientAddress", ClientAddress.class);
		xStream.alias("ClientContact", ClientContact.class);

		xStream.alias("vendors", List.class);
		xStream.alias("Vendor", ClientVendor.class);

		xStream.alias("accounts", List.class);
		xStream.alias("BankAccount", ClientBankAccount.class);

		xStream.alias("items", List.class);
		xStream.alias("ClientItem", ClientItem.class);

		xStream.alias("quotes", List.class);
		xStream.alias("Quote", ClientEstimate.class);

		xStream.alias("transactionItems", List.class);
		xStream.alias("ClientTransactionItem", ClientTransactionItem.class);

		xStream.alias("invoices", List.class);
		xStream.alias("Invoice", ClientInvoice.class);

		xStream.alias("salesOrders", List.class);

		xStream.alias("purchaseOrders", List.class);
		xStream.alias("ClientPurchaseOrder", ClientPurchaseOrder.class);

		xStream.alias("shippingTerms", List.class);
		xStream.alias("ClientShippingTerms", ClientShippingTerms.class);

		xStream.alias("shippingMethods", List.class);
		xStream.alias("ClientShippingMethod", ClientShippingMethod.class);

		xStream.alias("priceLevels", List.class);
		xStream.alias("ClientPriceLevel", ClientPriceLevel.class);

		xStream.alias("itemGroups", List.class);
		xStream.alias("ClientItemGroup", ClientItemGroup.class);

		xStream.alias("taxGroups", List.class);
		xStream.alias("ClientTAXGroup", ClientTAXGroup.class);

		xStream.alias("paySalesTaxs", List.class);
		xStream.alias("ClientPayTAX", ClientPayTAX.class);

		xStream.alias("creditRatings", List.class);
		xStream.alias("ClientCreditRating", ClientCreditRating.class);

		xStream.alias("salesPersons", List.class);
		xStream.alias("ClientSalesPerson", ClientSalesPerson.class);

		xStream.alias("taxItemGroups", List.class);
		xStream.alias("ClientTAXItemGroup", ClientTAXItemGroup.class);

		xStream.alias("fiscalYears", List.class);
		xStream.alias("ClientFiscalYear", ClientFiscalYear.class);

		xStream.alias("fixedAssets", List.class);
		xStream.alias("ClientFixedAsset", ClientFixedAsset.class);

		xStream.alias("vatReturns", List.class);
		xStream.alias("ClientTAXReturn", TAXReturn.class);

		xStream.alias("taxAgencies", List.class);
		xStream.alias("ClientTAXAgency", ClientTAXAgency.class);

		xStream.alias("taxCodes", List.class);
		xStream.alias("ClientTAXCode", ClientTAXCode.class);

		return xStream;

	}

	private static File getFile() {
		return new File(ServerConfiguration.getDefaultCompanyDir()
				+ "/unitedkingdom.xml");
	}

	public DemoCompany getDemoCompany() throws IOException {
		XStream xStream = new XStream(new DomDriver());

		xStream = createAlias(xStream);

		File file = getFile();

		Object object = xStream.fromXML(new FileInputStream(file));

		return (DemoCompany) object;
	}

}
