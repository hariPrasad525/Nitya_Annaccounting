package com.vimukti.accounter.services;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientBank;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientCashSales;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCreditCardCharge;
import com.vimukti.accounter.web.client.core.ClientCreditRating;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientCustomerCreditMemo;
import com.vimukti.accounter.web.client.core.ClientCustomerGroup;
import com.vimukti.accounter.web.client.core.ClientCustomerRefund;
import com.vimukti.accounter.web.client.core.ClientEnterBill;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFiscalYear;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.core.ClientIssuePayment;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientItemGroup;
import com.vimukti.accounter.web.client.core.ClientJournalEntry;
import com.vimukti.accounter.web.client.core.ClientPayBill;
import com.vimukti.accounter.web.client.core.ClientPayTAX;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientPurchaseOrder;
import com.vimukti.accounter.web.client.core.ClientReceivePayment;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.core.ClientTransferFund;
import com.vimukti.accounter.web.client.core.ClientUnitOfMeasure;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientVendorCreditMemo;
import com.vimukti.accounter.web.client.core.ClientVendorGroup;
import com.vimukti.accounter.web.client.core.ClientWriteCheck;

public interface IAccounterClientService {

	/**
	 * Core Part
	 */

	public ClientAccount createAccount(long companyId, ClientAccount account)
			throws DAOException;

	public ClientAccount alterAccount(long companyId, ClientAccount account)
			throws DAOException;

	public ClientCompany createCompany(ClientCompany company)
			throws DAOException;

	public ClientCompany alterCompany(ClientCompany company)
			throws DAOException;

	public ClientFiscalYear createFiscalYear(long company,
			ClientFiscalYear fiscalYear) throws DAOException;

	public ClientFiscalYear alterFiscalYear(long company,
			ClientFiscalYear fiscalYear) throws DAOException;

	public ClientCreditRating createCreditRating(long company,
			ClientCreditRating creditRating) throws DAOException;

	public ClientCreditRating alterCreditRating(long company,
			ClientCreditRating creditRating) throws DAOException;

	public ClientCurrency createCurrency(long company, ClientCurrency currency)
			throws DAOException;

	public ClientCurrency alterCurrency(long company, ClientCurrency currency)
			throws DAOException;

	public ClientCustomer createCustomer(long company, ClientCustomer customer)
			throws DAOException;

	public ClientJournalEntry createJournalEntry(long company,
			ClientJournalEntry journalEntry) throws DAOException;

	public ClientJournalEntry alterJournalEntry(long company,
			ClientJournalEntry journalEntry) throws DAOException;

	public ClientCustomer alterCustomer(long company, ClientCustomer customer)
			throws DAOException;

	public ClientCustomerGroup createCustomerGroup(long company,
			ClientCustomerGroup customerGroup) throws DAOException;

	public ClientCustomerGroup alterCustomerGroup(long company,
			ClientCustomerGroup customerGroup) throws DAOException;

	public ClientItem createItem(long company, ClientItem item)
			throws DAOException;

	public ClientItem alterItem(long company, ClientItem item)
			throws DAOException;

	public ClientItemGroup createItemGroup(long company,
			ClientItemGroup itemGroup) throws DAOException;

	public ClientItemGroup alterItemGroup(long company,
			ClientItemGroup itemGroup) throws DAOException;

	public ClientPaymentTerms createPaymentTerms(long company,
			ClientPaymentTerms paymentTerms) throws DAOException;

	public ClientPaymentTerms alterPaymentTerms(long company,
			ClientPaymentTerms paymentTerms) throws DAOException;

	public ClientPriceLevel createPriceLevel(long company,
			ClientPriceLevel priceLevel) throws DAOException;

	public ClientPriceLevel alterPriceLevel(long company,
			ClientPriceLevel priceLevel) throws DAOException;

	public ClientSalesPerson createSalesPerson(long company,
			ClientSalesPerson salesPerson) throws DAOException;

	public ClientSalesPerson alterSalesPerson(long company,
			ClientSalesPerson salesPerson) throws DAOException;

	public ClientShippingMethod createShippingMethod(long company,
			ClientShippingMethod shippingMethod) throws DAOException;

	public ClientShippingMethod alterShippingMethod(long company,
			ClientShippingMethod shippingMethod) throws DAOException;

	public ClientShippingTerms createShippingTerms(long company,
			ClientShippingTerms shippingTerms) throws DAOException;

	public ClientShippingTerms alterShippingTerms(long company,
			ClientShippingTerms shippingTerms) throws DAOException;

	public ClientTAXAgency createTaxAgency(long company,
			ClientTAXAgency taxAgency) throws DAOException;

	public ClientTAXAgency alterTaxAgency(long company,
			ClientTAXAgency taxAgency) throws DAOException;

	public ClientTAXCode createTaxCode(long company, ClientTAXCode taxCode)
			throws DAOException;

	public ClientTAXCode alterTaxCode(long company, ClientTAXCode taxCode)
			throws DAOException;

	public ClientTAXGroup createTaxGroup(long company, ClientTAXGroup taxGroup)
			throws DAOException;

	public ClientTAXGroup alterTaxGroup(long company, ClientTAXGroup taxGroup)
			throws DAOException;

	public ClientUnitOfMeasure createUnitOfMeasure(long company,
			ClientUnitOfMeasure untiOfMeasure) throws DAOException;

	public ClientUnitOfMeasure alterUnitOfMeasure(long company,
			ClientUnitOfMeasure untiOfMeasure) throws DAOException;

	public ClientUser createUser(ClientUser user) throws DAOException;

	public ClientUser alterUser(long company, ClientUser user)
			throws DAOException;

	public ClientVendor createVendor(long company, ClientVendor vendor)
			throws DAOException;

	public ClientVendor alterVendor(long company, ClientVendor vendor)
			throws DAOException;

	public ClientVendorGroup createVendorGroup(long company,
			ClientVendorGroup vendorGroup) throws DAOException;

	public ClientVendorGroup alterVendorGroup(long company,
			ClientVendorGroup vendorGroup) throws DAOException;

	/**
	 * Transaction Related
	 */

	public ClientCashPurchase createCashPurchase(long company,
			ClientCashPurchase cashPurchase) throws DAOException;

	public ClientCashPurchase alterCashPurchase(long company,
			ClientCashPurchase cashPurchase) throws DAOException;

	public ClientCashSales createCashSales(long company,
			ClientCashSales cashSales) throws DAOException;

	public ClientCashSales alterCashSales(long company,
			ClientCashSales cashSales) throws DAOException;

	public ClientCreditCardCharge createCreditCardCharge(long company,
			ClientCreditCardCharge creditCardCharge) throws DAOException;

	public ClientCreditCardCharge alterCreditCardCharge(long company,
			ClientCreditCardCharge creditCardCharge) throws DAOException;

	public ClientCustomerCreditMemo createCustomerCreditMemo(long company,
			ClientCustomerCreditMemo customerCreditMemo) throws DAOException;

	public ClientCustomerCreditMemo alterCustomerCreditMemo(long company,
			ClientCustomerCreditMemo customerCreditMemo) throws DAOException;

	public ClientCustomerRefund createCustomerRefunds(long company,
			ClientCustomerRefund customerRefunds) throws DAOException;

	public ClientCustomerRefund alterCustomerRefunds(long company,
			ClientCustomerRefund customerRefunds) throws DAOException;

	public ClientEnterBill createEnterBill(long company,
			ClientEnterBill enterBill) throws DAOException;

	public ClientEnterBill alterEnterBill(long company,
			ClientEnterBill enterBill) throws DAOException;

	public ClientEstimate createEstimate(long company, ClientEstimate estimate)
			throws DAOException;

	public ClientEstimate alterEstimate(long company, ClientEstimate estimate)
			throws DAOException;

	public ClientInvoice createInvoice(long company, ClientInvoice invoice)
			throws DAOException;

	public ClientInvoice alterInvoice(long company, ClientInvoice invoice)
			throws DAOException;

	public ClientIssuePayment createIssuePayment(long company,
			ClientIssuePayment issuePayment) throws DAOException;

	public ClientIssuePayment alterIssuePayment(long company,
			ClientIssuePayment issuePayment) throws DAOException;

	public ClientTransferFund createMakeDeposit(long company,
			ClientTransferFund makeDeposit) throws DAOException;

	public ClientTransferFund alterMakeDeposit(long company,
			ClientTransferFund makeDeposit) throws DAOException;

	public ClientPayBill createPayBill(long company, ClientPayBill payBill)
			throws DAOException;

	public ClientPayBill alterPayBill(long company, ClientPayBill payBill)
			throws DAOException;

	public ClientPurchaseOrder createPurchaseOrder(long company,
			ClientPurchaseOrder purchaseOrder) throws DAOException;

	public ClientPurchaseOrder alterPurchaseOrder(long company,
			ClientPurchaseOrder purchaseOrder) throws DAOException;

	public ClientReceivePayment createReceivePayment(long company,
			ClientReceivePayment receivePayment) throws DAOException;

	public ClientReceivePayment alterReceivePayment(long company,
			ClientReceivePayment receivePayment) throws DAOException;

	public ClientVendorCreditMemo createVendorCreditMemo(long company,
			ClientVendorCreditMemo vendorCreditMemo) throws DAOException;

	public ClientVendorCreditMemo alterVendorCreditMemo(long company,
			ClientVendorCreditMemo vendorCreditMemo) throws DAOException;

	public ClientWriteCheck createWriteCheck(long company,
			ClientWriteCheck writeCheck) throws DAOException;

	public ClientWriteCheck alterWriteCheck(long company,
			ClientWriteCheck writeCheck) throws DAOException;

	public ClientBank createBank(long company, ClientBank bank)
			throws DAOException;

	public ClientPayTAX createPaySalesTax(long company, ClientPayTAX paySalesTax)
			throws DAOException;

	public ClientPayTAX alterPaySalesTax(long company, ClientPayTAX paySalesTax)
			throws DAOException;

	public Boolean deleteObject(final Class class1, final long Id)
			throws DAOException;

}