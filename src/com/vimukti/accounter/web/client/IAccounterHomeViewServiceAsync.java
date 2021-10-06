/**
 * 
 */
package com.vimukti.accounter.web.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.*;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.core.Lists.ClientTDSInfo;
import com.vimukti.accounter.web.client.core.Lists.CustomerRefundsList;
import com.vimukti.accounter.web.client.core.Lists.DepositsTransfersList;
import com.vimukti.accounter.web.client.core.Lists.DepreciableFixedAssetsList;
import com.vimukti.accounter.web.client.core.Lists.EstimatesAndSalesOrdersList;
import com.vimukti.accounter.web.client.core.Lists.FixedAssetLinkedAccountMap;
import com.vimukti.accounter.web.client.core.Lists.FixedAssetSellOrDisposeReviewJournal;
import com.vimukti.accounter.web.client.core.Lists.InvoicesList;
import com.vimukti.accounter.web.client.core.Lists.IssuePaymentTransactionsList;
import com.vimukti.accounter.web.client.core.Lists.OverDueInvoicesList;
import com.vimukti.accounter.web.client.core.Lists.PayBillTransactionList;
import com.vimukti.accounter.web.client.core.Lists.PayeeList;
import com.vimukti.accounter.web.client.core.Lists.PaymentsList;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersAndItemReceiptsList;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersList;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentTransactionList;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentsList;
import com.vimukti.accounter.web.client.core.Lists.TempFixedAsset;
import com.vimukti.accounter.web.client.core.Lists.TransactionsList;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.ExpensePortletData;
import com.vimukti.accounter.web.client.ui.PayeesBySalesPortletData;
import com.vimukti.accounter.web.client.ui.YearOverYearPortletData;
import com.vimukti.accounter.web.client.ui.settings.StockAdjustmentList;

/**
 * @author Fernandez
 * 
 */
public interface IAccounterHomeViewServiceAsync {

	public void getOverDueInvoices(
			AsyncCallback<ArrayList<OverDueInvoicesList>> callBack);

	public void getBillsOwed(AsyncCallback<ArrayList<ClientEnterBill>> callBack);

	public void getLatestQuotes(
			AsyncCallback<ArrayList<ClientEstimate>> callBack);

	public void getTransactionPayBills(
			AsyncCallback<ArrayList<PayBillTransactionList>> callback);

	public void getTransactionPayBills(long vendorId,
			ClientFinanceDate paymentDate,
			AsyncCallback<ArrayList<PayBillTransactionList>> callback);

	public void getVendorPaymentsList(long fromDate, long toDate, int start,
			int length, int viewType,
			AsyncCallback<PaginationList<PaymentsList>> callBack);

	public void getPaymentsList(long fromDate, long toDate, int start,
			int length, int viewType,
			AsyncCallback<PaginationList<PaymentsList>> callBack);

	public void getRecurringsList(long fromDate, long toDate,
			AsyncCallback<PaginationList<ClientRecurringTransaction>> callBack);

	public void getVendorCreditsAndPayments(long vendorId, long transactionId,
			AsyncCallback<ArrayList<ClientCreditsAndPayments>> callBack);

	public void getChecks(
			AsyncCallback<ArrayList<IssuePaymentTransactionsList>> callBack);

	public void getChecks(long accountId,
			AsyncCallback<ArrayList<IssuePaymentTransactionsList>> callback);

	public void getCreditCardChargesThisMonth(long date,
			AsyncCallback<ArrayList<ClientCreditCardCharge>> callback);

	public void getLatestItems(AsyncCallback<ArrayList<ClientItem>> callBack);

	public void getTotalWithNamesItems(AsyncCallback<ArrayList<ClientItem>> callBack);

	public void getLatestPayments(
			AsyncCallback<ArrayList<PaymentsList>> callBack);

	public void getLatestCashSales(
			AsyncCallback<ArrayList<ClientCashSales>> callBack);

	public void getLatestCustomerRefunds(
			AsyncCallback<ArrayList<ClientCustomerRefund>> callBack);

	public void getLatestBills(AsyncCallback<ArrayList<BillsList>> callBack);

	public void getLatestCashPurchases(
			AsyncCallback<ArrayList<ClientCashPurchase>> callBack);

	public void getLatestChecks(
			AsyncCallback<ArrayList<ClientWriteCheck>> callBack);

	public void getLatestDeposits(
			AsyncCallback<ArrayList<ClientTransferFund>> callBack);

	/**
	 * Other Utility GET Methods
	 */

	public void getNextTransactionNumber(int transactionType,
			AsyncCallback<String> callback);

	// To auto generate the next Check number.
	public void getNextCheckNumber(long accountId, AsyncCallback<Long> callback);

	public void getNextIssuepaymentCheckNumber(long accountId,
			AsyncCallback<String> callback);

	// To check whether an Account is a Tax Agency Account or not
	public void isTaxAgencyAccount(long accountId,
			AsyncCallback<Boolean> callback);

	// To check whether an Account is a Sales Tax Payable Account or not
	public void isSalesTaxPayableAccount(long accountId,
			AsyncCallback<Boolean> callback);

	// To check whether an Account is a Sales Tax Payable Account or not
	public void isSalesTaxPayableAccountByName(String accountName,
			AsyncCallback<Boolean> callback);

	// To get all the Estimates/Quotes in a company
	public void getEstimates(int type, int status, long fromDate, long toDate,
			int start, int length,
			AsyncCallback<PaginationList<ClientEstimate>> callback);

	// To get the Estimates/Quotes of a particular customer in the company
	public void getEstimates(long customerId,
			AsyncCallback<ArrayList<ClientEstimate>> callback);

	// To check whether an Invoice or Vendor Bill can be Voidable and Editable
	// or not
	public void canVoidOrEdit(long invoiceOrVendorBillId,
			AsyncCallback<Boolean> callback);

	// To display the Item combo box of Transaction Item lines in Creating
	// Creating Invoice,Cash Sale, Customer Credit Memo, Customer Refund
	public void getSalesItems(AsyncCallback<ArrayList<ClientItem>> callback);

	// To display the Item combo box of Transaction Item lines in Creating Enter
	// Bill,Cash Purchase, Vendor Credit Memo Transactions
	public void getPurchaseItems(AsyncCallback<ArrayList<ClientItem>> callback);

	// To get the Credits and Payments of a particular Customer in a company
	public void getCustomerCreditsAndPayments(long customerId,
			long transactionId,
			AsyncCallback<ArrayList<ClientCreditsAndPayments>> callback);

	// To get all the Invoices and CustomerRefunds of a particular customer
	// which are not paid and display as the Transaction ReceivePayments in
	// ReceivePayment
	public void getTransactionReceivePayments(long customerId,
			long paymentDate,
			AsyncCallback<ArrayList<ReceivePaymentTransactionList>> callback);

	// To get a particular Journal Entry
	public void getJournalEntry(long journalEntryId,
			AsyncCallback<ClientJournalEntry> callback);

	// To get all the Journal Entries in a company
	public void getJournalEntries(long fromDate, long toDate,

	int start, int length,
			AsyncCallback<PaginationList<ClientJournalEntry>> callback);

	// to get the Account Register of a particular account
	// public AccountRegister getAccountRegister(String accountId)
	// throws DAOException;

	// To get all Customer Refunds and Write Checks -> for Customer
	public void getCustomerRefundsList(long fromDate, long toDate, int viewId,
			AsyncCallback<PaginationList<CustomerRefundsList>> callback);

	// To display the liabilityAccount combo box of New Tax Agency window
	public void getTaxAgencyAccounts(
			AsyncCallback<ArrayList<ClientAccount>> callback);

	public void getReceivePaymentsList(long fromDate, long toDate,
			int transactionType, int start, int length, int viewType,
			AsyncCallback<PaginationList<ReceivePaymentsList>> callback);

	public void getLatestPurchaseItems(
			AsyncCallback<ArrayList<ClientItem>> callback);

	public void getLatestVendorPayments(
			AsyncCallback<ArrayList<PaymentsList>> callback);

	public void getLatestReceivePayments(
			AsyncCallback<ArrayList<ClientReceivePayment>> callback);

	// public void getSalesOrders(long fromDate, long endDate,
	// AsyncCallback<PaginationList<SalesOrdersList>> callback);

	public void getPurchaseOrders(long fromDate, long toDate, int type,

	int start, int length,
			AsyncCallback<PaginationList<PurchaseOrdersList>> callback);

	/*
	 * public void getSalesOrdersForCustomer(long customerID,
	 * AsyncCallback<ArrayList<SalesOrdersList>> callback);
	 */

	/*
	 * public void getPurchaseOrdersForVendor(long vendorID,
	 * AsyncCallback<ArrayList<SalesOrdersList>> callback);
	 */

	public void getNotReceivedPurchaseOrdersList(long vendorID,
			AsyncCallback<ArrayList<PurchaseOrdersList>> callback);

	/*
	 * public void getPurchaseOrderById(long transactionId,
	 * AsyncCallback<ClientPurchaseOrder> callback);
	 */

	public void getPurchasesAndItemReceiptsList(long vendorID,
			AsyncCallback<ArrayList<PurchaseOrdersAndItemReceiptsList>> callback);

	public void getEstimatesAndSalesOrdersList(long customerID,
			AsyncCallback<ArrayList<EstimatesAndSalesOrdersList>> callback);

	public void getDepreciableFixedAssets(long depreciationFrom,
			long depreciationTo,
			AsyncCallback<DepreciableFixedAssetsList> callback);

	public void getDepreciationLastDate(
			AsyncCallback<ClientFinanceDate> callback);

	public void rollBackDepreciation(long rollBackDepreciationTo,
			AsyncCallback<Boolean> callback);

	public void getFinancialYearStartDates(
			AsyncCallback<ArrayList<ClientFinanceDate>> callback);

	public void getAllDepreciationFromDates(
			AsyncCallback<ArrayList<ClientFinanceDate>> callback);

	public void changeDepreciationStartDateTo(long newStartDate,
			AsyncCallback callback);

	public void getVATReturn(ClientTAXAgency taxAgency,
			ClientFinanceDate fromDate, ClientFinanceDate toDate,
			AsyncCallback<ClientTAXReturn> callback);

	void getReviewJournal(TempFixedAsset fixedAsset,
			AsyncCallback<FixedAssetSellOrDisposeReviewJournal> callback);

	void getAccumulatedDepreciationAmount(int depreciationMethod,
			double depreciationRate, double purchasePrice,
			long depreciationfrom, long depreciationtoDate,
			AsyncCallback<Double> callback);

	public void getNextNominalCode(int accountType, AsyncCallback<Long> callback);

	// public void createTaxes(int[] vatReturnType, AsyncCallback<Boolean>
	// callback);

	public void getNextFixedAssetNumber(AsyncCallback<String> callback);

	public void changeFiscalYearsStartDateTo(long newStartDate,
			AsyncCallback<Boolean> callback);

	public void runDepreciation(long depreciationFrom, long depreciationTo,
			FixedAssetLinkedAccountMap linkedAccounts, AsyncCallback callBack);

	// public void getPaySalesTaxEntries(long transactionDate,
	// AsyncCallback<ArrayList<ClientFileTAXEntry>> callBack);

	public void getPayTAXEntries(
			AsyncCallback<ArrayList<ClientTransactionPayTAX>> callBack);

	public void getPayeeList(int transactionCategory, boolean isActive,
			int strat, int length, boolean isPayeeCenter,
			AsyncCallback<PaginationList<PayeeList>> callBack);

	public void getInvoiceList(long fromDate, long toDate, int invoicesType,
			int viewType, int start, int length,
			AsyncCallback<PaginationList<InvoicesList>> callback);

	public void getCustomerNumber(AsyncCallback<String> callback);

	public void getVendorNumber(AsyncCallback<String> callback);

	void getReceiveVATEntries(
			AsyncCallback<ArrayList<ClientReceiveVATEntries>> callback);

	public void getGraphPointsforAccount(int chartType, long accountId,
			AsyncCallback<ArrayList<Double>> callBack);

	public void getEmployeeExpensesByStatus(String userName, int status,
			AsyncCallback<ArrayList<BillsList>> callBack);

	public void changePassWord(String emailID, String oldPassword,
			String newPassword, AsyncCallback<Boolean> callback);

	void getAllUsers(AsyncCallback<ArrayList<ClientUserInfo>> callback);

	public void getUsersActivityLog(ClientFinanceDate startDate,
			ClientFinanceDate endDate, int startIndex, int length, long value,
			AsyncCallback<PaginationList<ClientActivity>> callback);

	public void getAuditHistory(int objectType, long objectID, long activityID,
			AsyncCallback<ArrayList<ClientActivity>> callback);

	void mergeCustomer(ClientCustomer clientCustomer,
			ClientCustomer clientCustomer1, AsyncCallback<Void> callback);

	void mergeVendor(ClientVendor fromClientVendor,
			ClientVendor toClientVendor, AsyncCallback<Void> callback);

	void mergeItem(ClientItem froClientItem, ClientItem toClientItem,
			AsyncCallback<Void> callback);

	void mergeAccount(ClientAccount fromClientAccount,
			ClientAccount toClientAccount, AsyncCallback<ClientAccount> callback);

	// this method is used to send Pdf as an attachment in email
	public void sendPdfInMail(String fileName, String subject, String content,
			ClientEmailAccount sender, String recipientEmail, String ccEmail,
			AsyncCallback<Void> callback);

	public void sendTestMail(ClientEmailAccount sender, String recipient,
			AsyncCallback<Boolean> callback);

	public void createPdfFile(String objectID, int type, long brandingThemeId,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			AsyncCallback<List<String>> callback) throws AccounterException;

	public void getBudgetList(
			AsyncCallback<PaginationList<ClientBudget>> callBack);

	// For tds
	public void getPayBillsByTDS(
			AsyncCallback<ArrayList<ClientTDSInfo>> callback);

	public void getWarehouses(
			AsyncCallback<PaginationList<ClientWarehouse>> callBack);

	public void getAllUnits(
			AsyncCallback<PaginationList<ClientMeasurement>> callBack);

	public void getStockTransferItems(long wareHouse,
			AsyncCallback<ArrayList<ClientStockTransferItem>> callBack);

	void getWarehouseTransfersList(
			AsyncCallback<ArrayList<ClientStockTransfer>> callback);

	void getStockAdjustments(long startDate, long endDate,
			AsyncCallback<ArrayList<StockAdjustmentList>> callback);

	void getItemStatuses(long wareHouse,
			AsyncCallback<ArrayList<ClientItemStatus>> callback);

	void getAccounts(int typeOfAccount, boolean isActiveAccount, int start,
			int length, AsyncCallback<PaginationList<ClientAccount>> callBack);

	void getSearchResultByInput(SearchInput input, int start, int length,
			AsyncCallback<PaginationList<SearchResultlist>> callBack);

	public void savePortletPageConfig(ClientPortletPageConfiguration config,
			AsyncCallback<Boolean> callback);

	public void getMostRecentTransactionCurrencyFactor(long companyId,
			long currencyId, long tdate, AsyncCallback<Double> callback);

	public void getPortletPageConfiguration(String pageName,
			AsyncCallback<ClientPortletPageConfiguration> asyncCallback);

	public void getOwePayees(int oweType,
			AsyncCallback<ArrayList<ClientPayee>> callback);

	void getRecentTransactions(int limit,
			AsyncCallback<ArrayList<RecentTransactionsList>> asyncCallback);

	public void getMessagesAndTasks(
			AsyncCallback<ArrayList<ClientMessageOrTask>> callBack);

	void getRemindersList(int start, int length, int viewType,
			AsyncCallback<PaginationList<ClientReminder>> callBack);

	void getAccountsAndValues(long startDate, long endDate,
			AsyncCallback<ExpensePortletData> callback);

	void getEnterBillByEstimateId(long l,
			AsyncCallback<ClientEnterBill> asyncCallback);

	void getFixedAssetList(int status,
			AsyncCallback<ArrayList<ClientFixedAsset>> callback);

	void getAdvertisements(
			AsyncCallback<ArrayList<ClientAdvertisement>> callback);

	public void getTransactionToCreate(ClientRecurringTransaction obj,
			long transactionDate, AsyncCallback<ClientTransaction> callBack);

	void getPayeeChecks(int type, long fromDate, long toDate, int start,
			int length, int viewType,
			AsyncCallback<PaginationList<PaymentsList>> callBack);

	void getBillsAndItemReceiptList(boolean isExpensesList,
			int transactionType, long fromDate, long toDate, int start,
			int length, int viewType,
			AsyncCallback<PaginationList<BillsList>> callback);

	void getTDSTransactionItemsList(int formType,
			AsyncCallback<ArrayList<ClientTDSTransactionItem>> callback);

	void getTDSChalanDetailsList(
			AsyncCallback<PaginationList<ClientTDSChalanDetail>> callback);

	void getTDSChallansForAckNo(String ackNo,
			AsyncCallback<ArrayList<ClientTDSChalanDetail>> callback);

	void getDeductorMasterDetails(
			AsyncCallback<ClientTDSDeductorMasters> callback);

	void getIncomeExpensePortletInfo(int type, ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<IncomeExpensePortletInfo>> callback);

	void getIncomeBreakdownPortletData(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ExpensePortletData> callback);

	void getTopCustomersBySalesPortletData(ClientFinanceDate startDate,
			ClientFinanceDate endDate, int limit,
			AsyncCallback<ArrayList<PayeesBySalesPortletData>> callback);

	void getTopVendorsBySalesPortletData(ClientFinanceDate startDate,
			ClientFinanceDate endDate, int limit,
			AsyncCallback<ArrayList<PayeesBySalesPortletData>> callback);

	public void printCheques(long chequeLayoutId,
			ArrayList<PrintCheque> printCheques, AsyncCallback<String> callback);

	void getEtdsDetails(int formNo, int quater, ClientFinanceDate fromDate,
			ClientFinanceDate toDate, int startYear, int endYear,
			AsyncCallback<ArrayList<ClientETDSFillingItem>> callback);

	void updateAckNoForChallans(int formNo, int quater,
			ClientFinanceDate fromDate, ClientFinanceDate toDate,
			int startYear, int endYear, String ackNo, long date,
			AsyncCallback<Boolean> callback);

	void getItemsBySalesQuantity(ClientFinanceDate startDate,
			ClientFinanceDate endDate, int limit,
			AsyncCallback<ArrayList<PayeesBySalesPortletData>> callback);

	void getItemsByPurchaseQuantity(ClientFinanceDate startDate,
			ClientFinanceDate endDate, int limit,
			AsyncCallback<ArrayList<PayeesBySalesPortletData>> callback);

	void getAccountsBalancesByDate(ClientFinanceDate startDate,
			ClientFinanceDate endDate, long accountId, int chartType,
			AsyncCallback<ArrayList<YearOverYearPortletData>> callback);

	void getResponsiblePersonDetails(
			AsyncCallback<ClientTDSResponsiblePerson> accounterAsyncCallback);

	public void savePortletConfiguration(
			ClientPortletConfiguration configuration,
			AsyncCallback<Boolean> asyncCallback);

	void getIRASFileInformation(ClientFinanceDate startDate,
			ClientFinanceDate endDate, boolean isXml,
			AsyncCallback<String> callback);

	public void getDepositByEstimateId(long id,
			AsyncCallback<ClientMakeDeposit> asyncCallback);

	void isChalanDetailsFiled(int formNo, int quater,
			ClientFinanceDate fromDate, ClientFinanceDate toDate,
			int startYear, int endYear, AsyncCallback<Boolean> callback);

	public void getDepositsList(long date, long date2, int start, int length,
			int type,
			AsyncCallback<PaginationList<DepositsTransfersList>> asyncCallback);

	public void getTransfersList(long date, long date2, int start, int length,
			int type,
			AsyncCallback<PaginationList<DepositsTransfersList>> asyncCallback);

	public void getBankStatements(long accountId,
			AsyncCallback<PaginationList<ClientStatement>> asyncCallback);

	void getSpentTransactionsList(long endOfFiscalYear, long date, int start,
			int length, AsyncCallback<PaginationList<TransactionsList>> callback);

	void getReceivedTransactionsList(long endOfFiscalYear, long date,
			int start, int length,
			AsyncCallback<PaginationList<TransactionsList>> callback);

	public void getSpentAndReceivedTransactionsList(long endOfFiscalYear,
			long date, int i, int j,
			AsyncCallback<PaginationList<TransactionsList>> asyncCallback);

	public void getIvitableUsers(AsyncCallback<Set<InvitableUser>> asyncCallback);

	public void getClientCompaniesCount(AsyncCallback<Integer> callback);

	public void getFieldsOf(int importerType,
			AsyncCallback<ArrayList<ImportField>> callback);

	public void getJobsByCustomer(long id,
			AsyncCallback<ArrayList<ClientJob>> asyncCallback);

	void importData(String filePath, int importerType,
			HashMap<String, String> importMap, String dateFormate,
			AsyncCallback<HashMap<Integer, Object>> callback);

	public void getItemTransactionsList(long itemId, int transactionType,
			int transactionStatus, ClientFinanceDate startDate,
			ClientFinanceDate endDate, int start, int length,
			AsyncCallback<PaginationList<TransactionHistory>> callBack);

	public void getJobs(AsyncCallback<PaginationList<ClientJob>> callback);

	public void getSalesOrdersList(long id,
			AsyncCallback<ArrayList<EstimatesAndSalesOrdersList>> callback);

	void getWriteCheckByEstimateId(long l,
			AsyncCallback<ClientWriteCheck> callback);

	public void getCashPurchaseByEstimateId(long id,
			AsyncCallback<ClientCashPurchase> callback);

	public void getTaxAdjustmentsList(int viewType, long startDate,
			long endDate, int start, int length,
			AsyncCallback<PaginationList<ClientTAXAdjustment>> callback);

	public void mergeClass(ClientAccounterClass clientClass,
			ClientAccounterClass clientClass1, AsyncCallback<Void> callback);

	public void mergeLocation(ClientLocation clientFromLocation,
			ClientLocation clientToLocation, AsyncCallback<Void> callback);

	void getTransaction(boolean isPrev, long id, int type, int subType,
			AsyncCallback<Long> callback);

	void getUnitPricesByPayee(boolean isCust, long payee, long item,
			AsyncCallback<ArrayList<ItemUnitPrice>> callback);

	public void getPayRunsList(ClientFinanceDate startDate,
			ClientFinanceDate endDate, int start, int length, int type,
			int transactionType,
			AsyncCallback<PaginationList<PaymentsList>> callBack);

	void getTaxReturnList(long startDate, long endDate, int start, int length,
			int viewId, AsyncCallback<PaginationList<ClientTAXReturn>> callback);

	void getBuildAssembliesList(long startDate, long endDate, int start,
			int length, int viewId,
			AsyncCallback<PaginationList<ClientBuildAssembly>> callback);

	public void getPayTaxList(long startDate, long endDate, int start,
			int length, int viewId,
			AsyncCallback<PaginationList<ClientPayTAX>> callback);

	public void getTaxRefundsList(long startDate, long endDate, int start,
			int length, int viewId,
			AsyncCallback<PaginationList<ClientReceiveVAT>> callback);

	void getNewPaypalTransactionsList(long accountID,
			AsyncCallback<List<ClientPaypalTransation>> callBack);

	public void getSavedPaypalTransaction(ClientAccount clientAccount,
			AsyncCallback<PaginationList<ClientPaypalTransation>> callBack);

	public void getPaypalAccounts(
			AsyncCallback<ArrayList<ClientAccount>> asyncCallback);

	public void getPaypalTransactionDetailsForId(String transactionID,
			long accountID, AsyncCallback<String> asyncCallback);


	void getConsultantItemsDetails(AsyncCallback<List<ConsultantsDetailsList>> arrayListAsyncCallback);
}