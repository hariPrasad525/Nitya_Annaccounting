package com.nitya.accounter.services;

import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.IAccounterServerCore;

public interface IAccounterService {

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public <T extends IAccounterServerCore> long createObject(T t) // /////
			throws DAOException; // /////

	// /////
	public <T extends IAccounterServerCore> Boolean updateObject(T t) // /////
			throws DAOException; // /////

	// /////
	public <T extends IAccounterServerCore> Boolean deleteObject(T object) // /////
			throws DAOException; // /////

	// /////

	public <T extends IAccounterServerCore> T getObjectById(Class<?> clazz, // /////
			String id) throws DAOException; // /////

	// /////

	public <T extends IAccounterServerCore> T getObjectByName(Class<?> clazz, // /////
			String name, Company company) throws DAOException; // /////

	/**
	 * /////// This method is to check whether an Object is deletable or not.
	 * ///////
	 * 
	 * @param <T>
	 *            ///////
	 * @param clazz
	 * @param id
	 * @return true if the object related to the given string is deletable,
	 *         otherwise return false.
	 * @throws DAOException
	 */

	public <T extends IAccounterServerCore> Boolean canDelete(Class clazz,
			long companyId, long id) throws DAOException;
	// /////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * public void createAccount(Account account) throws DAOException;
	 * 
	 * public void deleteAccount(Account account) throws DAOException;
	 * 
	 * public void alterAccount(Account account) throws DAOException;
	 * 
	 * public void createCompany(Company company) throws DAOException;
	 * 
	 * public void deleteCompany(Company company) throws DAOException;
	 * 
	 * public void alterCompany(Company company) throws DAOException;
	 * 
	 * public void createFiscalYear(FiscalYear fiscalYear) throws DAOException;
	 * 
	 * public void deleteFiscalYear(FiscalYear fiscalYear) throws DAOException;
	 * 
	 * public void alterFiscalYear(FiscalYear fiscalYear) throws DAOException;
	 * 
	 * public void createCreditRating(CreditRating creditRating) throws
	 * DAOException;
	 * 
	 * public void deleteCreditRating(CreditRating creditRating) throws
	 * DAOException;
	 * 
	 * public void alterCreditRating(CreditRating creditRating) throws
	 * DAOException;
	 * 
	 * public void createCurrency(Currency currency) throws DAOException;
	 * 
	 * public void deleteCurrency(Currency currency) throws DAOException;
	 * 
	 * public void alterCurrency(Currency currency) throws DAOException;
	 * 
	 * public void createCustomer(Customer customer) throws DAOException;
	 * 
	 * public void createJournalEntry(JournalEntry journalEntry) throws
	 * DAOException;
	 * 
	 * public void alterJournalEntry(JournalEntry journalEntry) throws
	 * DAOException;
	 * 
	 * public void deleteCustomer(Customer customer) throws DAOException;
	 * 
	 * public void alterCustomer(Customer customer) throws DAOException;
	 * 
	 * public void createCustomerGroup(CustomerGroup customerGroup) throws
	 * DAOException;
	 * 
	 * public void deleteCustomerGroup(CustomerGroup customerGroup) throws
	 * DAOException;
	 * 
	 * public void alterCustomerGroup(CustomerGroup customerGroup) throws
	 * DAOException;
	 * 
	 * public void createItem(Item item) throws DAOException;
	 * 
	 * public void deleteItem(Item item) throws DAOException;
	 * 
	 * public void alterItem(Item item) throws DAOException;
	 * 
	 * public void createItemGroup(ItemGroup itemGroup) throws DAOException;
	 * 
	 * public void deleteItemGroup(ItemGroup itemGroup) throws DAOException;
	 * 
	 * public void alterItemGroup(ItemGroup itemGroup) throws DAOException;
	 * 
	 * public void createPaymentTerms(PaymentTerms paymentTerms) throws
	 * DAOException;
	 * 
	 * public void deletePaymentTerms(PaymentTerms paymentTerms) throws
	 * DAOException;
	 * 
	 * public void alterPaymentTerms(PaymentTerms paymentTerms) throws
	 * DAOException;
	 * 
	 * public void createPriceLevel(PriceLevel priceLevel) throws DAOException;
	 * 
	 * public void deletePriceLevel(PriceLevel priceLevel) throws DAOException;
	 * 
	 * public void alterPriceLevel(PriceLevel priceLevel) throws DAOException;
	 * 
	 * public void createSalesPerson(SalesPerson salesPerson) throws
	 * DAOException;
	 * 
	 * public void deleteSalesPerson(SalesPerson salesPerson) throws
	 * DAOException;
	 * 
	 * public void alterSalesPerson(SalesPerson salesPerson) throws
	 * DAOException;
	 * 
	 * public void createShippingMethod(ShippingMethod shippingMethod) throws
	 * DAOException;
	 * 
	 * public void deleteShippingMethod(ShippingMethod shippingMethod) throws
	 * DAOException;
	 * 
	 * public void alterShippingMethod(ShippingMethod shippingMethod) throws
	 * DAOException;
	 * 
	 * public void createShippingTerms(ShippingTerms shippingTerms) throws
	 * DAOException;
	 * 
	 * public void deleteShippingTerms(ShippingTerms shippingTerms) throws
	 * DAOException;
	 * 
	 * public void alterShippingTerms(ShippingTerms shippingTerms) throws
	 * DAOException;
	 * 
	 * public void createTaxAgency(TaxAgency taxAgency) throws DAOException;
	 * 
	 * public void deleteTaxAgency(TaxAgency taxAgency) throws DAOException;
	 * 
	 * public void alterTaxAgency(TaxAgency taxAgency) throws DAOException;
	 * 
	 * public void createTaxCode(TaxCode taxCode) throws DAOException;
	 * 
	 * public void deleteTaxCode(TaxCode taxCode) throws DAOException;
	 * 
	 * public void alterTaxCode(TaxCode taxCode) throws DAOException;
	 * 
	 * public void createTaxGroup(TaxGroup taxGroup) throws DAOException;
	 * 
	 * public void deleteTaxGroup(TaxGroup taxGroup) throws DAOException;
	 * 
	 * public void alterTaxGroup(TaxGroup taxGroup) throws DAOException;
	 * 
	 * public void createTaxRates(TaxRates taxRates) throws DAOException;
	 * 
	 * public void deleteTaxRates(TaxRates taxRates) throws DAOException;
	 * 
	 * public void alterTaxRates(TaxRates taxRates) throws DAOException;
	 * 
	 * public void createUnitOfMeasure(UnitOfMeasure untiOfMeasure) throws
	 * DAOException;
	 * 
	 * public void deleteUnitOfMeasure(UnitOfMeasure untiOfMeasure) throws
	 * DAOException;
	 * 
	 * public void alterUnitOfMeasure(UnitOfMeasure untiOfMeasure) throws
	 * DAOException;
	 * 
	 * public void createUser(User user) throws DAOException;
	 * 
	 * public void deleteUser(User user) throws DAOException;
	 * 
	 * public void alterUser(User user) throws DAOException;
	 * 
	 * public void createVendor(Vendor vendor) throws DAOException;
	 * 
	 * public void deleteVendor(Vendor vendor) throws DAOException;
	 * 
	 * public void alterVendor(Vendor vendor) throws DAOException;
	 * 
	 * public void createVendorGroup(VendorGroup vendorGroup) throws
	 * DAOException;
	 * 
	 * public void deleteVendorGroup(VendorGroup vendorGroup) throws
	 * DAOException;
	 * 
	 * public void alterVendorGroup(VendorGroup vendorGroup) throws
	 * DAOException;
	 *//**
	 * Transaction Related
	 */
	/*
	 * 
	 * public void createCashPurchase(CashPurchase cashPurchase) throws
	 * DAOException;
	 * 
	 * public void deleteCashPurchase(CashPurchase cashPurchase) throws
	 * DAOException;
	 * 
	 * public void alterCashPurchase(CashPurchase cashPurchase) throws
	 * DAOException;
	 * 
	 * public void createCashSales(CashSales cashSales) throws DAOException;
	 * 
	 * public void deleteCashSales(CashSales cashSales) throws DAOException;
	 * 
	 * public void alterCashSales(CashSales cashSales) throws DAOException;
	 * 
	 * public void createCreditCardCharge(CreditCardCharge creditCardCharge)
	 * throws DAOException;
	 * 
	 * public void deleteCreditCardCharge(CreditCardCharge creditCardCharge)
	 * throws DAOException;
	 * 
	 * public void alterCreditCardCharge(CreditCardCharge creditCardCharge)
	 * throws DAOException;
	 * 
	 * public void createCustomerCreditMemo(CustomerCreditMemo
	 * customerCreditMemo) throws DAOException;
	 * 
	 * public void deleteCustomerCreditMemo(CustomerCreditMemo
	 * customerCreditMemo) throws DAOException;
	 * 
	 * public void alterCustomerCreditMemo(CustomerCreditMemo
	 * customerCreditMemo) throws DAOException;
	 * 
	 * public void createCustomerRefunds(CustomerRefund customerRefunds) throws
	 * DAOException;
	 * 
	 * public void deleteCustomerRefunds(CustomerRefund customerRefunds) throws
	 * DAOException;
	 * 
	 * public void alterCustomerRefunds(CustomerRefund customerRefunds) throws
	 * DAOException;
	 * 
	 * public void createEnterBill(EnterBill enterBill) throws DAOException;
	 * 
	 * public void deleteEnterBill(EnterBill enterBill) throws DAOException;
	 * 
	 * public void alterEnterBill(EnterBill enterBill) throws DAOException;
	 * 
	 * public void alterExpense(Expense expense) throws DAOException;
	 * 
	 * public void createEstimate(Estimate estimate) throws DAOException;
	 * 
	 * public void deleteEstimate(Estimate estimate) throws DAOException;
	 * 
	 * public void alterEstimate(Estimate estimate) throws DAOException;
	 * 
	 * public void createInvoice(Invoice invoice) throws DAOException;
	 * 
	 * public void deleteInvoice(Invoice invoice) throws DAOException;
	 * 
	 * public void alterInvoice(Invoice invoice) throws DAOException;
	 * 
	 * public void createIssuePayment(IssuePayment issuePayment) throws
	 * DAOException;
	 * 
	 * public void deleteIssuePayment(IssuePayment issuePayment) throws
	 * DAOException;
	 * 
	 * public void alterIssuePayment(IssuePayment issuePayment) throws
	 * DAOException;
	 * 
	 * public void createMakeDeposit(MakeDeposit makeDeposit) throws
	 * DAOException;
	 * 
	 * public void deleteMakeDeposit(MakeDeposit makeDeposit) throws
	 * DAOException;
	 * 
	 * public void alterMakeDeposit(MakeDeposit makeDeposit) throws
	 * DAOException;
	 * 
	 * public void createPayBill(PayBill payBill) throws DAOException;
	 * 
	 * public void deletePayBill(PayBill payBill) throws DAOException;
	 * 
	 * public void alterPayBill(PayBill payBill) throws DAOException;
	 * 
	 * public void createPurchaseOrder(PurchaseOrder purchaseOrder) throws
	 * DAOException;
	 * 
	 * public void deletePurchaseOrder(PurchaseOrder purchaseOrder) throws
	 * DAOException;
	 * 
	 * public void alterPurchaseOrder(PurchaseOrder purchaseOrder) throws
	 * DAOException;
	 * 
	 * public void createReceivePayment(ReceivePayment receivePayment) throws
	 * DAOException;
	 * 
	 * public void deleteReceivePayment(ReceivePayment receivePayment) throws
	 * DAOException;
	 * 
	 * public void alterReceivePayment(ReceivePayment receivePayment) throws
	 * DAOException;
	 * 
	 * public void createSalesOrder(SalesOrder salesOrder) throws DAOException;
	 * 
	 * public void deleteSalesOrder(SalesOrder salesOrder) throws DAOException;
	 * 
	 * public void alterSalesOrder(SalesOrder salesOrder) throws DAOException;
	 * 
	 * public void createTransferFund(TransferFund transferFund) throws
	 * DAOException;
	 * 
	 * public void deleteTransferFund(TransferFund transferFund) throws
	 * DAOException;
	 * 
	 * public void alterTransferFund(TransferFund transferFund) throws
	 * DAOException;
	 * 
	 * public void createVendorCreditMemo(VendorCreditMemo vendorCreditMemo)
	 * throws DAOException;
	 * 
	 * public void deleteVendorCreditMemo(VendorCreditMemo vendorCreditMemo)
	 * throws DAOException;
	 * 
	 * public void alterVendorCreditMemo(VendorCreditMemo vendorCreditMemo)
	 * throws DAOException;
	 * 
	 * public void createWriteCheck(WriteCheck writeCheck) throws DAOException;
	 * 
	 * public void deleteWriteCheck(WriteCheck writeCheck) throws DAOException;
	 * 
	 * public void alterWriteCheck(WriteCheck writeCheck) throws DAOException;
	 * 
	 * public void createBank(Bank bank) throws DAOException;
	 * 
	 * public void createPaySalesTax(PaySalesTax paySalesTax) throws
	 * DAOException;
	 * 
	 * public void alterPaySalesTax(PaySalesTax paySalesTax) throws
	 * DAOException;
	 * 
	 * void createExpense(Expense expense) throws DAOException;
	 * 
	 * void createPayExpense(PayExpense payExpense) throws DAOException;
	 * 
	 * public void createFixedAsset(FixedAsset fixedAsset) throws DAOException;
	 * 
	 * public void alterFixedAsset(FixedAsset fixedAsset) throws DAOException;
	 * 
	 * public void deleteFixedAsset(FixedAsset fixedAsset) throws DAOException;
	 * 
	 * public void createSellingOrDisposingFixedAsset(
	 * SellingOrDisposingFixedAsset sellingOrDisposingFixedAsset) throws
	 * DAOException;
	 */
}