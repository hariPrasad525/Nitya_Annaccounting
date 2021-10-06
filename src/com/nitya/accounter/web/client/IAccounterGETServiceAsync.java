/**
 * 
 */
package com.nitya.accounter.web.client;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientAttachment;
import com.nitya.accounter.web.client.core.ClientEmployeeTimeSheet;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientInvoiceFrequencyGroup;
import com.nitya.accounter.web.client.core.ClientReconciliation;
import com.nitya.accounter.web.client.core.ClientReconciliationItem;
import com.nitya.accounter.web.client.core.ClientRewaHrTimeSheet;
import com.nitya.accounter.web.client.core.ClientTAXReturn;
import com.nitya.accounter.web.client.core.ClientTAXReturnEntry;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.core.ClientTransactionLog;
import com.nitya.accounter.web.client.core.ClientUser;
import com.nitya.accounter.web.client.core.HelpLink;
import com.nitya.accounter.web.client.core.HrEmployee;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.core.Lists.KeyFinancialIndicators;

/**
 * @author Fernandez
 * 
 */
public interface IAccounterGETServiceAsync {

	// /**
	// * Core Related
	// */
	//
	// public void checkLogin(String email, String password,
	// AsyncCallback<Boolean> callback);
	//
	// public void getAccount(String accountName,
	// AsyncCallback<ClientAccount> callback);
	//
	// public void getAccount(Long accountId, AsyncCallback<ClientAccount>
	// callback);
	//
	// public void getAccounts(AsyncCallback<ArrayList<ClientAccount>>
	// callback);
	//
	// public void getCompany(String companyName,
	// AsyncCallback<ClientCompany> callback);
	//
	// public void getCompany(Long companyId, AsyncCallback<ClientCompany>
	// callback);
	//
	// public void getCompanies(AsyncCallback<ArrayList<ClientCompany>>
	// callback);
	//
	// public void getCreditRating(String creditRatingName,
	// AsyncCallback<ClientCreditRating> callback);
	//
	// public void getCreditRating(Long creditRatingId,
	// AsyncCallback<ClientCreditRating> callback);
	//
	// public void getCreditRatings(
	// AsyncCallback<ArrayList<ClientCreditRating>> callback);
	//
	// public void getCurrency(String currencyName,
	// AsyncCallback<ClientCurrency> callback);
	//
	// public void getCurrency(Long currencyId,
	// AsyncCallback<ClientCurrency> callback);
	//
	// public void getCurrencies(AsyncCallback<ArrayList<ClientCurrency>>
	// callback);
	//
	// public void getCustomer(String customerName,
	// AsyncCallback<ClientCustomer> callback);
	//
	// public void getCustomer(Long customerId,
	// AsyncCallback<ClientCustomer> callback);
	//
	// public void getCustomers(AsyncCallback<ArrayList<ClientCustomer>>
	// callback);
	//
	// public void getCustomerGroup(String customerGroupName,
	// AsyncCallback<ClientCustomerGroup> callback);
	//
	// public void getCustomerGroup(Long customerGroupId,
	// AsyncCallback<ClientCustomerGroup> callback);
	//
	// public void getCustomerGroups(
	// AsyncCallback<ArrayList<ClientCustomerGroup>> callback);
	//
	// public void getItem(String itemName, AsyncCallback<ClientItem> callback);
	//
	// public void getItem(Long itemId, AsyncCallback<ClientItem> callback);
	//
	// public void getItems(AsyncCallback<ArrayList<ClientItem>> callback);
	//
	// public void getItemGroup(String itemGroupName,
	// AsyncCallback<ClientItemGroup> callback);
	//
	// public void getItemGroup(Long itemGroupId,
	// AsyncCallback<ClientItemGroup> callback);
	//
	// public void getItemGroups(AsyncCallback<ArrayList<ClientItemGroup>>
	// callback);
	//
	// public void getPaymentTerms(String paymentsTermsName,
	// AsyncCallback<ClientPaymentTerms> callback);
	//
	// public void getPaymentTerms(Long paymentsTermsId,
	// AsyncCallback<ClientPaymentTerms> callback);
	//
	// public void getPaymentTerms(AsyncCallback<ArrayList<ClientPaymentTerms>>
	// callback);
	//
	// public void getPriceLevel(String priceLevelName,
	// AsyncCallback<ClientPriceLevel> callback);
	//
	// public void getPriceLevel(Long priceLevelId,
	// AsyncCallback<ClientPriceLevel> callback);
	//
	// public void getPriceLevels(AsyncCallback<ArrayList<ClientPriceLevel>>
	// callback);
	//
	// public void getSalesPerson(String salesPersonName,
	// AsyncCallback<ClientSalesPerson> callback);
	//
	// public void getSalesPerson(Long salesPersonId,
	// AsyncCallback<ClientSalesPerson> callback);
	//
	// public void getSalesPersons(AsyncCallback<ArrayList<ClientSalesPerson>>
	// callback);
	//
	// public void getShippingMethod(String shippingMethodName,
	// AsyncCallback<ClientShippingMethod> callback);
	//
	// public void getShippingMethod(Long shippingMethodId,
	// AsyncCallback<ClientShippingMethod> callback);
	//
	// public void getShippingMethods(
	// AsyncCallback<ArrayList<ClientShippingMethod>> callback);
	//
	// public void getShippingTerms(String shippingTermsName,
	// AsyncCallback<ClientShippingTerms> callback);
	//
	// public void getShippingTerms(Long shippingTermsId,
	// AsyncCallback<ClientShippingTerms> callback);
	//
	// public void getShippingTerms(
	// AsyncCallback<ArrayList<ClientShippingTerms>> callback);
	//
	// public void getTaxAgency(String taxAgencyName,
	// AsyncCallback<ClientTaxAgency> callback);
	//
	// public void getTaxAgency(Long taxAgencyID,
	// AsyncCallback<ClientTaxAgency> callback);
	//
	// public void getTaxAgencies(AsyncCallback<ArrayList<ClientTaxAgency>>
	// callback);
	//
	// public void getTaxCode(String taxCodeName,
	// AsyncCallback<ClientTaxCode> callback);
	//
	// public void getTaxCode(Long taxCodeID, AsyncCallback<ClientTaxCode>
	// callback);
	//
	// public void getTaxCodes(AsyncCallback<ArrayList<ClientTaxCode>>
	// callback);
	//
	// public void getTaxGroup(String taxGroupName,
	// AsyncCallback<ClientTaxGroup> callback);
	//
	// public void getTaxGroup(Long taxGroupID,
	// AsyncCallback<ClientTaxGroup> callback);
	//
	// public void getGroups(AsyncCallback<ArrayList<ClientTaxGroup>> callback);
	//
	// public void getTaxRates(double rate, AsyncCallback<ClientTaxRates>
	// callback);
	//
	// public void getTaxRates(Long taxRateID,
	// AsyncCallback<ClientTaxRates> callback);
	//
	// public void getRates(AsyncCallback<ArrayList<ClientTaxRates>> callback);
	//
	// public void getUnitOfMeasure(String unitOfMeausreName,
	// AsyncCallback<ClientUnitOfMeasure> callback);
	//
	// public void getUnitOfMeasure(Long unitOfMeausreId,
	// AsyncCallback<ClientUnitOfMeasure> callback);
	//
	// public void getUnitOfMeasures(
	// AsyncCallback<ArrayList<ClientUnitOfMeasure>> callback);
	//
	// public void getUser(long userID, AsyncCallback<ClientUser> callback);
	//
	// public void getUser(String email, AsyncCallback<ClientUser> callback);
	//
	// public void getUsers(AsyncCallback<ArrayList<ClientUser>> callback);
	//
	// public void getVendor(String vendorName,
	// AsyncCallback<ClientVendor> callback);
	//
	// public void getVendor(Long vendorId, AsyncCallback<ClientVendor>
	// callback);
	//
	// public void getVendors(AsyncCallback<ArrayList<ClientVendor>> callback);
	//
	// public void getVendorGroup(String vendorGroupName,
	// AsyncCallback<ClientVendorGroup> callback);
	//
	// public void getVendorGroup(Long vendorGroupId,
	// AsyncCallback<ClientVendorGroup> callback);
	//
	// public void getVendorGroups(AsyncCallback<ArrayList<ClientVendorGroup>>
	// callback);
	//
	// /**
	// * Transaction Related
	// */
	//
	// public void getCashPurchase(Long cashPurchaseId,
	// AsyncCallback<ClientCashPurchase> callback);
	//
	// public void getCashSales(Long cashSalesId,
	// AsyncCallback<ClientCashSales> callback);
	//
	// public void getCreditCardCharge(Long creditCardChargeId,
	// AsyncCallback<ClientCreditCardCharge> callback);
	//
	// public void getCustomerCreditMemo(Long customerCreditMemoId,
	// AsyncCallback<ClientCustomerCreditMemo> callback);
	//
	// public void getCustomerRefunds(Long customerRefundsId,
	// AsyncCallback<ClientCustomerRefund> callback);
	//
	// public void getEnterBill(Long enterBillId,
	// AsyncCallback<ClientEnterBill> callback);
	//
	// public void getEstimate(Long estimateId,
	// AsyncCallback<ClientEstimate> callback);
	//
	// public void getInvoice(Long invoiceId, AsyncCallback<ClientInvoice>
	// callback);
	//
	// public void getIssuePayment(Long issuePaymentId,
	// AsyncCallback<ClientIssuePayment> callback);
	//
	// public void getMakeDeposit(Long makeDepositId,
	// AsyncCallback<ClientMakeDeposit> callback);
	//
	// public void getPayBill(Long payBillId, AsyncCallback<ClientPayBill>
	// callback);
	//
	// public void getPurchaseOrder(Long purchaseOrderId,
	// AsyncCallback<ClientPurchaseOrder> callback);
	//
	// public void getReceivePayment(Long receivePaymentId,
	// AsyncCallback<ClientReceivePayment> callback);
	//
	// public void getSalesOrder(Long salesOrderId,
	// AsyncCallback<ClientSalesOrder> callback);
	//
	// public void getTransferFund(Long transferFundId,
	// AsyncCallback<ClientTransferFund> callback);
	//
	// public void getVendorCreditMemo(Long vendorrCreditMemoId,
	// AsyncCallback<ClientVendorCreditMemo> callback);
	//
	// public void getwriterCheck(Long writeCheckId,
	// AsyncCallback<ClientWriteCheck> callback);
	//
	// public void getRegister(Long accountId,
	// AsyncCallback<ArrayList<ClientTransaction>> callback);
	//
	// public void getBanks(AsyncCallback<ArrayList<ClientBank>> callback);
	//
	// public void getCreditsAndPayments(
	// AsyncCallback<ArrayList<ClientCreditsAndPayments>> callback);
	//
	// public void getCreditAndPayment(String memo,
	// AsyncCallback<ClientCreditsAndPayments> callback);
	//
	// public void getCreditAndPayment(Long id,
	// AsyncCallback<ClientCreditsAndPayments> callback);
	//
	// public void getPayee(AsyncCallback<ArrayList<ClientPayee>> callback);
	//
	// /**
	// * Can Delete Objects
	// */
	//
	// public void canDeleteAccount(Long accountId, AsyncCallback<Boolean>
	// result);
	//
	// public void canDeleteCompany(AsyncCallback<Boolean> result);
	//
	// public void canDeleteCreditRating(Long creditRatingId,
	// AsyncCallback<Boolean> result);
	//
	// public void canDeleteCustomer(Long customerId, AsyncCallback<Boolean>
	// result);
	//
	// public void canDeleteCustomerGroup(Long customerGroupId,
	// AsyncCallback<Boolean> result);
	//
	// public void canDeleteItem(Long itemId, AsyncCallback<Boolean> result);
	//
	// public void canDeleteItemGroup(Long itemGroupId,
	// AsyncCallback<Boolean> result);
	//
	// public void canDeletePaymentTerms(Long paymentTermsId,
	// AsyncCallback<Boolean> result);
	//
	// public void canDeletePriceLevel(Long priceLevelId,
	// AsyncCallback<Boolean> result);
	//
	// public void canDeleteSalesPerson(Long salesPersonId,
	// AsyncCallback<Boolean> result);
	//
	// public void canDeleteShippingMethod(Long shippingMethodId,
	// AsyncCallback<Boolean> result);
	//
	// public void canDeleteShippingTerms(Long shippingTermsId,
	// AsyncCallback<Boolean> result);
	//
	// public void canDeleteTaxAgency(Long taxAgencyId,
	// AsyncCallback<Boolean> result);
	//
	// public void canDeleteTaxCode(Long taxCodeId, AsyncCallback<Boolean>
	// result);
	//
	// public void canDeleteTaxGroup(Long taxGroupId, AsyncCallback<Boolean>
	// result);
	//
	// public void canDeleteTaxRates(Long taxRatesId, AsyncCallback<Boolean>
	// result);
	//
	// public void canDeleteUnitOfMeasure(Long unitOfMeasureId,
	// AsyncCallback<Boolean> result);
	//
	// public void canDeleteUser(Long userId, AsyncCallback<Boolean> result);
	//
	// public void canDeleteVendor(Long vendorId, AsyncCallback<Boolean>
	// result);
	//
	// public void canDeleteVendorGroup(Long vendorGroupId,
	// AsyncCallback<Boolean> result);
	//
	// public void getFiscalYears(AsyncCallback<ArrayList<ClientFiscalYear>>
	// callback);
	//
	// public void getFiscalYear(Long yearId,
	// AsyncCallback<ClientFiscalYear> callback);
	//
	// public void getTransactionPaySalesTaxEntriesList(Date transactionDate,
	// AsyncCallback<ArrayList<ClientPaySalesTaxEntries>> callback);
	//
	// public void getPaySalesTax(Long id,
	// AsyncCallback<ClientPaySalesTax> callback);

	public <T extends IAccounterCore> void getObjectById(
			AccounterCoreType type, long id, AsyncCallback<T> callback);

	public <T extends IAccounterCore> void getObjectByName(
			AccounterCoreType type, String name, AsyncCallback<T> callback);

	public <T extends IAccounterCore> void getObjects(AccounterCoreType type,
			AsyncCallback<ArrayList<T>> callback);

	void getKeyFinancialIndicators(
			AsyncCallback<KeyFinancialIndicators> callback);

	void getHREmployees(AsyncCallback<ArrayList<HrEmployee>> callback);

	public void getHelpLinks(int type,
			AsyncCallback<ArrayList<HelpLink>> callback);

	/**
	 * @param string
	 * @param string2
	 * @param b
	 * @param i
	 * @param checkLoginCallback
	 */
	public void getUser(String string, String string2, boolean b, int i,
			AsyncCallback<ClientUser> checkLoginCallback);

	// public void getCountries(AsyncCallback<List<String>> callback);
	//
	// public void getTimezones(AsyncCallback<List<String>> callback);
	//
	// public void getStates(String country, AsyncCallback<List<String>>
	// callback);

	// public void getCurrencies(AsyncCallback<List<ClientCurrency>> callback);

	/**
	 * @param id
	 * @param clientFinanceDate2
	 * @param clientFinanceDate
	 * @param accounterAsyncCallback
	 */
	public void getAllTransactionsOfAccount(long id,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			AsyncCallback<ArrayList<ClientReconciliationItem>> asyncCallback);

	/**
	 * @param accountName
	 */
	public void getReconciliationsByBankAccountID(long accountName,
			AsyncCallback<ArrayList<ClientReconciliation>> callback);

	/**
	 * @param id
	 * @param accounterAsyncCallback
	 */
	public void getOpeningBalanceforReconciliation(long id,
			AsyncCallback<Double> accounterAsyncCallback);

	public void getTransactionHistory(long transactionId,
			AsyncCallback<ArrayList<ClientTransactionLog>> callback);

	void getLastTAXReturnEndDate(long agencyId, AsyncCallback<Long> callback);

	void getTAXReturnEntries(
			long agency,
			long startDate,
			long endDate,
			AsyncCallback<ArrayList<ClientTAXReturnEntry>> accounterAsyncCallback);

	public void getAllTAXReturns(
			int start,
			int lenght,
			int viewType,
			AsyncCallback<PaginationList<ClientTAXReturn>> accounterAsyncCallback);

	public void getAssetValuesForInventories(
			AsyncCallback<HashMap<Long, Double>> callback);

	public void getClientCompaniesCount(AsyncCallback<Integer> callback);

	public void getClientTransactionItem(long id, AsyncCallback<ClientTransactionItem> asyncCallback);

	public void getAllInvoiceFrequency( AsyncCallback<List<ClientInvoiceFrequencyGroup>> asyncCallback);

	public void getInvoiceFrequency(long id, AsyncCallback<ClientInvoiceFrequencyGroup> asyncCallback);
	
	public void getEmployeeTimeSheetValues(long fromDate, long toDate, AsyncCallback<PaginationList<ClientEmployeeTimeSheet>> asyncCallback);

	public void getTimeSheetAttachments(long id, AsyncCallback<ArrayList<ClientAttachment>> asyncCallback);

	public void updateTimeSheetBilling(long empTimeSheetId, AsyncCallback<Boolean> asyncCallback);

	public void getRewaTimeSheet(long id,Date fromDate, Date toDate, AsyncCallback<ArrayList<ClientRewaHrTimeSheet>> asyncCallback);

}