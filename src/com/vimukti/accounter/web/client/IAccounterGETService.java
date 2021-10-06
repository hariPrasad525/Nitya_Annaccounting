/**
 * 
 */
package com.vimukti.accounter.web.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.RemoteService;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientReconciliation;
import com.vimukti.accounter.web.client.core.ClientReconciliationItem;
import com.vimukti.accounter.web.client.core.ClientTAXReturn;
import com.vimukti.accounter.web.client.core.ClientTAXReturnEntry;
import com.vimukti.accounter.web.client.core.ClientTransactionLog;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.HelpLink;
import com.vimukti.accounter.web.client.core.HrEmployee;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.Lists.KeyFinancialIndicators;
import com.vimukti.accounter.web.client.data.InvalidSessionException;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * @author Fernandez
 * 
 */
public interface IAccounterGETService extends RemoteService {

	/**
	 * Core Related
	 * 
	 * @throws InvalidSessionException
	 */

	// public Boolean checkLogin(String email, String password);
	//
	// public ClientAccount getAccount(String accountName);
	//
	// public ClientAccount getAccount(Long accountId);
	//
	// public ArrayList<ClientAccount> getAccounts();
	//
	// public ClientCompany getCompany(String companyName);
	//
	// public ClientCompany getCompany(Long companyId);
	//
	// public ArrayList<ClientCompany> getCompanies();
	//
	// public ClientCreditRating getCreditRating(String creditRatingName);
	//
	// public ClientCreditRating getCreditRating(Long creditRatingId);
	//
	// public ArrayList<ClientCreditRating> getCreditRatings();
	//
	// public ClientCurrency getCurrency(String currencyName);
	//
	// public ClientCurrency getCurrency(Long currencyId);
	//
	// public ArrayList<ClientCurrency> getCurrencies();
	//
	// public ClientCustomer getCustomer(String customerName);
	//
	// public ClientCustomer getCustomer(Long customerId);
	//
	// public ArrayList<ClientCustomer> getCustomers();
	//
	// public ClientCustomerGroup getCustomerGroup(String customerGroupName);
	//
	// public ClientCustomerGroup getCustomerGroup(Long customerGroupId);
	//
	// public ArrayList<ClientCustomerGroup> getCustomerGroups();
	//
	// public ClientItem getItem(String itemName);
	//
	// public ClientItem getItem(Long itemId);
	//
	// public ArrayList<ClientItem> getItems();
	//
	// public ClientItemGroup getItemGroup(String itemGroupName);
	//
	// public ClientItemGroup getItemGroup(Long itemGroupId);
	//
	// public ArrayList<ClientItemGroup> getItemGroups();
	//
	// public ClientPaymentTerms getPaymentTerms(String paymentsTermsName);
	//
	// public ClientPaymentTerms getPaymentTerms(Long paymentsTermsId);
	//
	// public ArrayList<ClientPaymentTerms> getPaymentTerms();
	//
	// public ClientPriceLevel getPriceLevel(String priceLevelName);
	//
	// public ClientPriceLevel getPriceLevel(Long priceLevelId);
	//
	// public ArrayList<ClientPriceLevel> getPriceLevels();
	//
	// public ClientSalesPerson getSalesPerson(String salesPersonName);
	//
	// public ClientSalesPerson getSalesPerson(Long salesPersonId);
	//
	// public ArrayList<ClientSalesPerson> getSalesPersons();
	//
	// public ClientShippingMethod getShippingMethod(String shippingMethodName);
	//
	// public ClientShippingMethod getShippingMethod(Long shippingMethodId);
	//
	// public ArrayList<ClientShippingMethod> getShippingMethods();
	//
	// public ClientShippingTerms getShippingTerms(String shippingTermsName);
	//
	// public ClientShippingTerms getShippingTerms(Long shippingTermsId);
	//
	// public ArrayList<ClientShippingTerms> getShippingTerms();
	//
	// public ClientTaxAgency getTaxAgency(String taxAgencyName);
	//
	// public ClientTaxAgency getTaxAgency(Long taxAgencyID);
	//
	// public ArrayList<ClientTaxAgency> getTaxAgencies();
	//
	// public ClientTaxCode getTaxCode(String taxCodeName);
	//
	// public ClientTaxCode getTaxCode(Long taxCodeID);
	//
	// public ArrayList<ClientTaxCode> getTaxCodes();
	//
	// public ClientTaxGroup getTaxGroup(String taxGroupName);
	//
	// public ClientTaxGroup getTaxGroup(Long taxGroupID);
	//
	// public ArrayList<ClientTaxGroup> getGroups();
	//
	// public ClientTaxRates getTaxRates(double rate);
	//
	// public ClientTaxRates getTaxRates(Long taxRateID);
	//
	// public ArrayList<ClientTaxRates> getRates();
	//
	// public ClientUnitOfMeasure getUnitOfMeasure(String unitOfMeausreName);
	//
	// public ClientUnitOfMeasure getUnitOfMeasure(Long unitOfMeausreId);
	//
	// public ArrayList<ClientUnitOfMeasure> getUnitOfMeasures();
	//
	// public ClientUser getUser(long userID);
	//
	// public ClientUser getUser(String email);
	//
	// public ArrayList<ClientUser> getUsers();
	//
	// public ClientVendor getVendor(String vendorName);
	//
	// public ClientVendor getVendor(Long vendorId);
	//
	// public ArrayList<ClientVendor> getVendors();
	//
	// public ClientVendorGroup getVendorGroup(String vendorGroupName);
	//
	// public ClientVendorGroup getVendorGroup(Long vendorGroupId);
	//
	// public ArrayList<ClientVendorGroup> getVendorGroups();
	//
	// /**
	// * Transaction Related
	// */
	//
	// public ClientCashPurchase getCashPurchase(Long cashPurchaseId);
	//
	// public ClientCashSales getCashSales(Long cashSalesId);
	//
	// public ClientCreditCardCharge getCreditCardCharge(Long
	// creditCardChargeId);
	//
	// public ClientCustomerCreditMemo getCustomerCreditMemo(
	// Long customerCreditMemoId);
	//
	// public ClientCustomerRefund getCustomerRefunds(Long customerRefundsId);
	//
	// public ClientEnterBill getEnterBill(Long enterBillId);
	//
	// public ClientEstimate getEstimate(Long estimateId);
	//
	// public ClientInvoice getInvoice(Long invoiceId);
	//
	// public ClientIssuePayment getIssuePayment(Long issuePaymentId);
	//
	// public ClientMakeDeposit getMakeDeposit(Long makeDepositId);
	//
	// public ClientPayBill getPayBill(Long payBillId);
	//
	// public ClientPurchaseOrder getPurchaseOrder(Long purchaseOrderId);
	//
	// public ClientReceivePayment getReceivePayment(Long receivePaymentId);
	//
	// public ClientSalesOrder getSalesOrder(Long salesOrderId);
	//
	// public ClientTransferFund getTransferFund(Long transferFundId);
	//
	// public ClientVendorCreditMemo getVendorCreditMemo(Long
	// vendorrCreditMemoId);
	//
	// public ClientWriteCheck getwriterCheck(Long writeCheckId);
	//
	// public ArrayList<ClientTransaction> getRegister(Long accountId);
	//
	// public ArrayList<ClientBank> getBanks();
	//
	// public ArrayList<ClientCreditsAndPayments> getCreditsAndPayments();
	//
	// public ClientCreditsAndPayments getCreditAndPayment(String memo);
	//
	// public ClientCreditsAndPayments getCreditAndPayment(Long id);
	//
	// public ArrayList<ClientPayee> getPayee();
	//
	// /**
	// * Can Delete Objects
	// */
	//
	// public Boolean canDeleteAccount(Long accountId);
	//
	// public Boolean canDeleteCompany();
	//
	// public Boolean canDeleteCreditRating(Long creditRatingId);
	//
	// public Boolean caDate dueDate;nDeleteCustomer(Long customerId);
	//
	// public Boolean canDeleteCustomerGroup(Long customerGroupId);
	//
	// public Boolean canDeleteItem(Long itemId);
	//
	// public Boolean canDeleteItemGroup(Long itemGroupId);
	//
	// public Boolean canDeletePaymentTerms(Long paymentTermsId);
	//
	// public Boolean canDeletePriceLevel(Long priceLevelId);
	//
	// public Boolean canDeleteSalesPerson(Long salesPersonId);
	//
	// public Boolean canDeleteShippingMethod(Long shippingMethodId);
	//
	// public Boolean canDeleteShippingTerms(Long shippingTermsId);
	//
	// public Boolean canDeleteTaxAgency(Long taxAgencyId);
	//
	// public Boolean canDeleteTaxCode(Long taxCodeId);
	//
	// public Boolean canDeleteTaxGroup(Long taxGroupId);
	//
	// public Boolean canDeleteTaxRates(Long taxRatesId);
	//
	// public Boolean canDeleteUnitOfMeasure(Long unitOfMeasureId);
	//
	// public Boolean canDeleteUser(Long userId);
	//
	// public Boolean canDeleteVendor(Long vendorId);
	//
	// public Boolean canDeleteVendorGroup(Long vendorGroupId);
	//
	// public ArrayList<ClientFiscalYear> getFiscalYears();
	//
	// public ClientFiscalYear getFiscalYear(Long yearId);
	//
	// public ArrayList<ClientPaySalesTaxEntries>
	// getTransactionPaySalesTaxEntriesList(
	// Date transactionDate);
	//
	// public ClientPaySalesTax getPaySalesTax(Long id);

	public <T extends IAccounterCore> T getObjectById(AccounterCoreType type,
			long id) throws AccounterException;

	public <T extends IAccounterCore> T getObjectByName(AccounterCoreType type,
			String name) throws AccounterException;

	public <T extends IAccounterCore> ArrayList<T> getObjects(
			AccounterCoreType type);

	// public List<String> getTimezones();

	KeyFinancialIndicators getKeyFinancialIndicators();

	ArrayList<HrEmployee> getHREmployees() throws AccounterException;

	public ArrayList<HelpLink> getHelpLinks(int type) throws AccounterException;

	public ClientUser getUser(String userName, String password,
			boolean isremeber, int offset);

	ArrayList<ClientReconciliationItem> getAllTransactionsOfAccount(long id,
			ClientFinanceDate startDate, ClientFinanceDate endDate)
			throws AccounterException;

	ArrayList<ClientReconciliation> getReconciliationsByBankAccountID(
			long accountName) throws AccounterException;

	double getOpeningBalanceforReconciliation(long id)
			throws AccounterException;

	ArrayList<ClientTransactionLog> getTransactionHistory(long transactionId)
			throws AccounterException;

	long getLastTAXReturnEndDate(long agencyId) throws AccounterException;

	ArrayList<ClientTAXReturnEntry> getTAXReturnEntries(long agency,
			long startDate, long endDate) throws AccounterException;

	PaginationList<ClientTAXReturn> getAllTAXReturns(int start, int lenght,
			int viewType) throws AccounterException;

	HashMap<Long, Double> getAssetValuesForInventories()
			throws AccounterException;

	public int getClientCompaniesCount();

}