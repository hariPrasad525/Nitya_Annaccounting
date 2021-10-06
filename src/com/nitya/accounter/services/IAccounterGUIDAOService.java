package com.nitya.accounter.services;

import java.util.ArrayList;
import java.util.Date;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.CashPurchase;
import com.nitya.accounter.core.CashSales;
import com.nitya.accounter.core.CreditCardCharge;
import com.nitya.accounter.core.CreditsAndPayments;
import com.nitya.accounter.core.Customer;
import com.nitya.accounter.core.CustomerRefund;
import com.nitya.accounter.core.EnterBill;
import com.nitya.accounter.core.Estimate;
import com.nitya.accounter.core.Item;
import com.nitya.accounter.core.JournalEntry;
import com.nitya.accounter.core.ReceivePayment;
import com.nitya.accounter.core.TAXAgency;
import com.nitya.accounter.core.TAXReturn;
import com.nitya.accounter.core.TAXReturnEntry;
import com.nitya.accounter.core.TransferFund;
import com.nitya.accounter.core.Vendor;
import com.nitya.accounter.core.WriteCheck;
import com.nitya.accounter.web.client.core.Lists.BillsList;
import com.nitya.accounter.web.client.core.Lists.CustomerRefundsList;
import com.nitya.accounter.web.client.core.Lists.DepreciableFixedAssetsList;
import com.nitya.accounter.web.client.core.Lists.EstimatesAndSalesOrdersList;
import com.nitya.accounter.web.client.core.Lists.FixedAssetLinkedAccountMap;
import com.nitya.accounter.web.client.core.Lists.FixedAssetList;
import com.nitya.accounter.web.client.core.Lists.FixedAssetSellOrDisposeReviewJournal;
import com.nitya.accounter.web.client.core.Lists.InvoicesList;
import com.nitya.accounter.web.client.core.Lists.IssuePaymentTransactionsList;
import com.nitya.accounter.web.client.core.Lists.KeyFinancialIndicators;
import com.nitya.accounter.web.client.core.Lists.MakeDepositTransactionsList;
import com.nitya.accounter.web.client.core.Lists.OverDueInvoicesList;
import com.nitya.accounter.web.client.core.Lists.PayBillTransactionList;
import com.nitya.accounter.web.client.core.Lists.PaymentsList;
import com.nitya.accounter.web.client.core.Lists.PurchaseOrdersAndItemReceiptsList;
import com.nitya.accounter.web.client.core.Lists.PurchaseOrdersList;
import com.nitya.accounter.web.client.core.Lists.ReceivePaymentTransactionList;
import com.nitya.accounter.web.client.core.Lists.ReceivePaymentsList;
import com.nitya.accounter.web.client.core.Lists.SellingOrDisposingFixedAssetList;
import com.nitya.accounter.web.client.core.Lists.TempFixedAsset;

public interface IAccounterGUIDAOService {

	/**
	 * Company Home page widgets Related Dao Methods
	 */

	// public ProfitAndLoss getProfitAndLossReport()
	// throws DAOException;
	public ArrayList<OverDueInvoicesList> getOverDueInvoices()
			throws DAOException;

	public ArrayList<EnterBill> getBillsOwed() throws DAOException;

	public ArrayList<Estimate> getLatestQuotes() throws DAOException;

	// public ArrayList<ExpensesThisFiscalYear> getExpensesThisFiscalYear(
	// Company company) throws DAOException;

	public ArrayList<CreditCardCharge> getCreditCardChargesThisMonth(
			final Date date) throws DAOException;

	public ArrayList<Customer> getLatestCustomers() throws DAOException;

	public ArrayList<Vendor> getLatestVendors() throws DAOException;

	public ArrayList<Item> getLatestItems() throws DAOException;

	public ArrayList<PaymentsList> getLatestPayments() throws DAOException;

	public ArrayList<CashSales> getLatestCashSales() throws DAOException;

	public ArrayList<CustomerRefund> getLatestCustomerRefunds()
			throws DAOException;

	public ArrayList<BillsList> getLatestBills() throws DAOException;

	public ArrayList<CashPurchase> getLatestCashPurchases() throws DAOException;

	public ArrayList<WriteCheck> getLatestChecks() throws DAOException;

	public ArrayList<TransferFund> getLatestDeposits() throws DAOException;

	public ArrayList<InvoicesList> getLatestInvoices() throws DAOException;

	public ArrayList<ReceivePayment> getLatestReceivePayments()
			throws DAOException;

	public ArrayList<PaymentsList> getLatestVendorPayments()
			throws DAOException;

	public ArrayList<Item> getLatestSalesItems() throws DAOException;

	public ArrayList<Item> getLatestPurchaseItems() throws DAOException;

	/**
	 * Other Dao Methods required for GUI
	 */

	// To auto generate the next Transaction number for all types of
	// Transactions
	public Long getNextTransactionNumber(int transactionType)
			throws DAOException;

	// To auto generate the next Voucher number for all vouchers.
	public Long getNextVoucherNumber() throws DAOException;

	// To auto generate the next Check number.
	public Long getNextCheckNumber(String account) throws DAOException;

	public Long getNextNominalCode(int accountType) throws DAOException;

	// To check whether an Account is a Tax Agency Account or not
	public boolean isTaxAgencyAccount(String account) throws DAOException;

	// To check whether an Account is a Sales Tax Payable Account or not
	public boolean isSalesTaxPayableAccount(String accountId)
			throws DAOException;

	// To check whether an Account is a Sales Tax Payable Account or not
	public boolean isSalesTaxPayableAccountByName(String accountName)
			throws DAOException;

	// To get all the Estimates/Quotes in a company
	public ArrayList<Estimate> getEstimates() throws DAOException;

	// To get the Estimates/Quotes of a particular customer in the company
	public ArrayList<Estimate> getEstimates(String customer)
			throws DAOException;

	// To check whether an Invoice or Vendor Bill can be Voidable and Editable
	// or not
	public boolean canVoidOrEdit(String invoiceOrVendorBillId)
			throws DAOException;

	// To display the Item combo box of Transaction Item lines in Creating
	// Creating Invoice,Cash Sale, Customer Credit Memo, Customer Refund
	public ArrayList<Item> getSalesItems() throws DAOException;

	// To display the Item combo box of Transaction Item lines in Creating Enter
	// Bill,Cash Purchase, Vendor Credit Memo Transactions
	public ArrayList<Item> getPurchaseItems() throws DAOException;

	// To get the Credits and Payments of a particular Customer in a company
	public ArrayList<CreditsAndPayments> getCustomerCreditsAndPayments(
			String customer) throws DAOException;

	// To get the Credits and Payments of a particular Vendor in a company
	public ArrayList<CreditsAndPayments> getVendorCreditsAndPayments(
			String vendor) throws DAOException;

	// To get all the Invoices and CustomerRefunds of a particular customer
	// which are not paid and display as the Transaction ReceivePayments in
	// ReceivePayment
	public ArrayList<ReceivePaymentTransactionList> getTransactionReceivePayments(
			String customerId) throws DAOException;

	// To get all the Vendor Bills and MakeDeposit(New Vendor Entries) which are
	// not paid and display as the Transaction PayBills in PayBill
	public ArrayList<PayBillTransactionList> getTransactionPayBills()
			throws DAOException;

	// To get all the Vendor Bills and MakeDeposit(New Vendor Entries) of a
	// particular Vendor which are not paid and display as the Transaction
	// PayBills in PayBill
	public ArrayList<PayBillTransactionList> getTransactionPayBills(
			String vendorId) throws DAOException;

	// To get all the Write Checks in a company
	public ArrayList<IssuePaymentTransactionsList> getChecks()
			throws DAOException;

	// To get the Write Checks related to a particular Account in a company
	public ArrayList<IssuePaymentTransactionsList> getChecks(String account)
			throws DAOException;

	// To get a particular Journal Entry
	public JournalEntry getJournalEntry(String journalEntryId)
			throws DAOException;

	// To get all the Journal Entries in a company
	public ArrayList<JournalEntry> getJournalEntries() throws DAOException;

	// to get the Account Register of a particular account
	// public AccountRegister getAccountRegister(String accountId)
	// throws DAOException;

	// To get all Customer Refunds , Vendor Payments,Cash Purchases, Credit Card
	// Charge and all Write Checks
	public ArrayList<PaymentsList> getPaymentsList() throws DAOException;

	// To get all Invoices, Customer Credit Memo, Cash Sales and Write Checks ->
	// for Customer
	public ArrayList<InvoicesList> getInvoiceList() throws DAOException;

	// To get all Customer Refunds and Write Checks -> for Customer
	public ArrayList<CustomerRefundsList> getCustomerRefundsList()
			throws DAOException;

	// To get all Vendor Bills, Vendor Credit Memo, Cash Purchase and Credit
	// Card Charge and Write Check -> Vendor
	public ArrayList<BillsList> getBillsList() throws DAOException;

	// To get all Vendor Payments, PayBills, Write Check -> Vendor and TaxAgency
	public ArrayList<ReceivePaymentsList> getReceivePaymentsList()
			throws DAOException;

	// To get all Vendor Payments, PayBills, Write Check -> Vendor and TaxAgency
	public ArrayList<PaymentsList> getVendorPaymentsList() throws DAOException;

	// To display the liabilityAccount combo box of New Tax Agency window
	public ArrayList<Account> getTaxAgencyAccounts() throws DAOException;

	public ArrayList<MakeDepositTransactionsList> getTransactionMakeDeposits()
			throws DAOException;

	public void test() throws Exception;

	public ArrayList<TAXReturnEntry> getTransactionPaySalesTaxEntriesList(
			Date transactionDate) throws DAOException;

	public ArrayList<EstimatesAndSalesOrdersList> getEstimatesAndSalesOrdersList(
			String customerId) throws DAOException;

	public ArrayList<PurchaseOrdersAndItemReceiptsList> getPurchasesAndItemReceiptsList(
			String vendorId) throws DAOException;

	// ArrayList<SalesOrdersList> getSalesOrdersList(String customerId)
	// throws DAOException;

	ArrayList<PurchaseOrdersList> getPurchaseOrdersList(String vendorId)
			throws DAOException;

	ArrayList<PurchaseOrdersList> getNotReceivedPurchaseOrdersList(
			String vendorId) throws DAOException;

	public ArrayList<DepreciableFixedAssetsList> getDepreciationFixedAssets(
			Date startDate, Date toDate) throws DAOException;

	TAXReturn getVATReturn(TAXAgency vatAgency, Date fromDate, Date toDate)
			throws DAOException;

	ArrayList<FixedAssetList> getFixedAssets(int status) throws DAOException;

	ArrayList<SellingOrDisposingFixedAssetList> getSellingOrDisposingFixedAssets()
			throws DAOException;

	public void runDepreciation(Date depreciationFrom, Date depreciationTo,
			FixedAssetLinkedAccountMap linkedAccounts) throws DAOException;

	public DepreciableFixedAssetsList getDepreciableFixedAssets(
			Date depreciationFrom, Date depreciationTo) throws DAOException;

	public Date getDepreciationLastDate() throws DAOException;

	public void rollBackDepreciation(Date rollBackDepreciationTo)
			throws DAOException;

	public double getCalculatedDepreciatedAmount(int depreciationMethod,
			double depreciationRate, double purchasePrice,
			Date depreciationFrom, Date depreciationTo) throws DAOException;

	public double getCalculatedRollBackDepreciationAmount(String fixedAssetID,
			Date rollBackDepreciationTo) throws DAOException;

	public FixedAssetSellOrDisposeReviewJournal getReviewJournal(
			TempFixedAsset fixedAsset) throws DAOException;

	public void changeDepreciationStartDateTo(Date newStartDate)
			throws DAOException;

	public ArrayList<Date> getFinancialYearStartDates() throws DAOException;

	public ArrayList<Date> getAllDepreciationFromDates() throws DAOException;

	public void changeFiscalYearsStartDateTo(Date newStartDate)
			throws DAOException;

	public KeyFinancialIndicators getKeyFinancialIndicators()
			throws DAOException;

}
