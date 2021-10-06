package com.vimukti.accounter.web.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
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
 */
public interface IAccounterHomeViewService extends RemoteService {
    public PaginationList<TransactionsList> getSpentAndReceivedTransactionsList(
            long endOfFiscalYear, long date, int start, int length);

    public ArrayList<OverDueInvoicesList> getOverDueInvoices();

    public ArrayList<ClientEnterBill> getBillsOwed();

    public ArrayList<ClientEstimate> getLatestQuotes();

    PaginationList<BillsList> getBillsAndItemReceiptList(
            boolean isExpensesList, int transactionType, long fromDate,
            long toDate, int start, int length, int viewType);

    public ArrayList<PayBillTransactionList> getTransactionPayBills();

    public ArrayList<PayBillTransactionList> getTransactionPayBills(
            long vendorId, ClientFinanceDate paymentDate);

    PaginationList<PaymentsList> getVendorPaymentsList(long fromDate,
                                                       long toDate, int start, int length, int viewType);

    PaginationList<PaymentsList> getPaymentsList(long fromDate, long toDate,
                                                 int start, int length, int viewType);

    public ArrayList<ClientCreditsAndPayments> getVendorCreditsAndPayments(
            long vendorId, long transactionId);

    public ArrayList<IssuePaymentTransactionsList> getChecks();

    public ArrayList<IssuePaymentTransactionsList> getChecks(long accountId);

    public ArrayList<ClientCreditCardCharge> getCreditCardChargesThisMonth(
            long date);

    public ArrayList<ClientItem> getLatestItems();

    public ArrayList<ClientItem> getTotalWithNamesItems();

    public ArrayList<PaymentsList> getLatestPayments();

    public ArrayList<ClientCashSales> getLatestCashSales();

    public ArrayList<ClientCustomerRefund> getLatestCustomerRefunds();

    public ArrayList<BillsList> getLatestBills();

    public ArrayList<ClientCashPurchase> getLatestCashPurchases();

    public ArrayList<ClientWriteCheck> getLatestChecks();

    public ArrayList<ClientTransferFund> getLatestDeposits();

    /**
     * Other Utility GET Methods
     */

    public String getNextTransactionNumber(int transactionType);

    // To auto generate the next Check number.
    public Long getNextCheckNumber(long accountId);

    // To auto generate the next Issue Payement Check number.
    public String getNextIssuepaymentCheckNumber(long accountId)
            throws AccounterException;

    // To check whether an Account is a Tax Agency Account or noto
    public boolean isTaxAgencyAccount(long accountId);

    // To check whether an Account is a Sales Tax Payable Account or not
    public boolean isSalesTaxPayableAccount(long accountId);

    // To check whether an Account is a Sales Tax Payable Account or not
    public boolean isSalesTaxPayableAccountByName(String accountName);

    PaginationList<ClientEstimate> getEstimates(int type, int status,
                                                long fromDate, long toDate, int start, int length);

    // To get the Estimates/Quotes of a particular customer in the company
    public ArrayList<ClientEstimate> getEstimates(long customerId);

    // To check whether an Invoice or Vendor Bill can be Voidable and Editable
    // or not
    public boolean canVoidOrEdit(long invoiceOrVendorBillId);

    // To display the Item combo box of Transaction Item lines in Creating
    // Creating Invoice,Cash Sale, Customer Credit Memo, Customer Refund
    public ArrayList<ClientItem> getSalesItems();

    // To display the Item combo box of Transaction Item lines in Creating Enter
    // Bill,Cash Purchase, Vendor Credit Memo Transactions
    public ArrayList<ClientItem> getPurchaseItems();

    ArrayList<ClientCreditsAndPayments> getCustomerCreditsAndPayments(
            long customerId, long transactionId);

    // To get all the Invoices and CustomerRefunds of a particular customer
    // which are not paid and display as the Transaction ReceivePayments in
    // ReceivePayment
    public ArrayList<ReceivePaymentTransactionList> getTransactionReceivePayments(
            long customerId, long paymentDate) throws AccounterException;

    // To get a particular Journal Entry
    public ClientJournalEntry getJournalEntry(long journalEntryId);

    PaginationList<ClientJournalEntry> getJournalEntries(long fromDate,
                                                         long toDate, int start, int length);

    // To get all the Journal Entries in a company

    // to get the Account Register of a particular account
    // public AccountRegister getAccountRegister(String accountId)
    // throws DAOException;

    PaginationList<CustomerRefundsList> getCustomerRefundsList(long fromDate,
                                                               long toDate, int viewId);

    // To display the liabilityAccount combo box of New Tax Agency window
    public ArrayList<ClientAccount> getTaxAgencyAccounts();

    PaginationList<ReceivePaymentsList> getReceivePaymentsList(long fromDate,
                                                               long toDate, int transactionType, int start, int length,
                                                               int viewType);

    public ArrayList<ClientItem> getLatestPurchaseItems();

    public ArrayList<PaymentsList> getLatestVendorPayments();

    public ArrayList<ClientReceivePayment> getLatestReceivePayments();

    // public PaginationList<SalesOrdersList> getSalesOrders(long fromDate,
    // long endDate) throws AccounterException;

    PaginationList<PurchaseOrdersList> getPurchaseOrders(long fromDate,
                                                         long toDate, int type, int start, int length)
            throws AccounterException;

    /*
     * public ArrayList<SalesOrdersList> getSalesOrdersForCustomer(long
     * customerID) throws AccounterException;
     *
     * public ArrayList<SalesOrdersList> getPurchaseOrdersForVendor(long
     * vendorID) throws AccounterException;
     */

    public ArrayList<PurchaseOrdersList> getNotReceivedPurchaseOrdersList(
            long vendorID) throws AccounterException;

    /* public ClientPurchaseOrder getPurchaseOrderById(long transactionId); */

    public ArrayList<PurchaseOrdersAndItemReceiptsList> getPurchasesAndItemReceiptsList(
            long vendorId) throws AccounterException;

    public ArrayList<EstimatesAndSalesOrdersList> getEstimatesAndSalesOrdersList(
            long customerId) throws AccounterException;

    public DepreciableFixedAssetsList getDepreciableFixedAssets(
            long depreciationFrom, long depreciationTo)
            throws AccounterException;

    public ClientFinanceDate getDepreciationLastDate()
            throws AccounterException;

    Boolean rollBackDepreciation(long rollBackDepreciationTo)
            throws AccounterException;

    public ArrayList<ClientFinanceDate> getFinancialYearStartDates()
            throws AccounterException;

    public ArrayList<ClientFinanceDate> getAllDepreciationFromDates()
            throws AccounterException;

    public void changeDepreciationStartDateTo(long newStartDate)
            throws AccounterException;

    public ClientTAXReturn getVATReturn(ClientTAXAgency taxAgency,
                                        ClientFinanceDate fromDate, ClientFinanceDate toDate)
            throws AccounterException;

    public FixedAssetSellOrDisposeReviewJournal getReviewJournal(
            TempFixedAsset fixedAsset) throws AccounterException;

    // public boolean createTaxes(int[] vatReturnType);

    public double getAccumulatedDepreciationAmount(int depreciationMethod,
                                                   double depreciationRate, double purchasePrice,
                                                   long depreciationfrom, long depreciationtoDate)
            throws AccounterException;

    public Long getNextNominalCode(int accountType);

    public String getNextFixedAssetNumber();

    public boolean changeFiscalYearsStartDateTo(long newStartDate);

    public void runDepreciation(long depreciationFrom, long depreciationTo,
                                FixedAssetLinkedAccountMap linkedAccounts);

    // public ArrayList<ClientFileTAXEntry> getPaySalesTaxEntries(
    // long transactionDate);

    ArrayList<ClientTransactionPayTAX> getPayTAXEntries()
            throws AccounterException;

    public ArrayList<ClientReceiveVATEntries> getReceiveVATEntries()
            throws AccounterException;

    PaginationList<PayeeList> getPayeeList(int transactionCategory,
                                           boolean isActive, int strat, int length, boolean isPayeeCenter)
            throws AccounterException;

    public PaginationList<InvoicesList> getInvoiceList(long fromDate,

                                                       long toDate, int type, int viewType, int start, int length);

    public String getCustomerNumber();

    public String getVendorNumber();

    public ArrayList<Double> getGraphPointsforAccount(int chartType,
                                                      long accountId);

    boolean changePassWord(String emailID, String oldPassword,
                           String newPassword);

    public ArrayList<BillsList> getEmployeeExpensesByStatus(String userName,
                                                            int status);

    public ArrayList<ClientUserInfo> getAllUsers() throws AccounterException;

    PaginationList<ClientRecurringTransaction> getRecurringsList(long fromDate,
                                                                 long toDate) throws AccounterException;

    // For merging

    public void mergeCustomer(ClientCustomer clientCustomer,
                              ClientCustomer clientCustomer2) throws AccounterException;

    void mergeVendor(ClientVendor fromClientVendor, ClientVendor toClientVendor)
            throws AccounterException;

    void mergeItem(ClientItem froClientItem, ClientItem toClientItem)
            throws AccounterException;

    public PaginationList<ClientActivity> getUsersActivityLog(
            ClientFinanceDate startDate, ClientFinanceDate endDate,
            int startIndex, int length, long value) throws AccounterException;

    public ArrayList<ClientActivity> getAuditHistory(int objectType,
                                                     long objectID, long activityID) throws AccounterException;

    ClientAccount mergeAccount(ClientAccount fromClientAccount,
                               ClientAccount toClientAccount) throws AccounterException;

    // for sending pdf in email

    void sendPdfInMail(String fileName, String subject, String content,
                       ClientEmailAccount sender, String recipientEmail, String ccEmail)
            throws AccounterException;

    public boolean sendTestMail(ClientEmailAccount sender, String recipient);

    List<String> createPdfFile(String objectID, int type, long brandingThemeId,
                               ClientFinanceDate startDate, ClientFinanceDate endDate)
            throws AccounterException;

    public PaginationList<ClientBudget> getBudgetList();

    public PaginationList<ClientTDSChalanDetail> getTDSChalanDetailsList();

    public ArrayList<ClientTDSChalanDetail> getTDSChallansForAckNo(String ackNo)
            throws AccounterException;

    ArrayList<ClientTDSTransactionItem> getTDSTransactionItemsList(int formType);

    ArrayList<ClientETDSFillingItem> getEtdsDetails(int formNo, int quater,
                                                    ClientFinanceDate fromDate, ClientFinanceDate toDate,
                                                    int startYear, int endYear);

    boolean updateAckNoForChallans(int formNo, int quater,
                                   ClientFinanceDate fromDate, ClientFinanceDate toDate,
                                   int startYear, int endYear, String ackNo, long date)
            throws AccounterException;

    ClientTDSDeductorMasters getDeductorMasterDetails();

    // for TDS

    public ArrayList<ClientTDSInfo> getPayBillsByTDS()
            throws AccounterException;

    public PaginationList<ClientWarehouse> getWarehouses();

    public PaginationList<ClientMeasurement> getAllUnits();

    public ArrayList<ClientStockTransferItem> getStockTransferItems(
            long wareHouse);

    public ArrayList<ClientStockTransfer> getWarehouseTransfersList()
            throws AccounterException;

    public ArrayList<StockAdjustmentList> getStockAdjustments(long startDate,
                                                              long endDate) throws AccounterException;

    ArrayList<ClientItemStatus> getItemStatuses(long wareHouse)
            throws AccounterException;

    PaginationList<ClientAccount> getAccounts(int typeOfAccount,
                                              boolean isActiveAccount, int start, int length)
            throws AccounterException;

    PaginationList<SearchResultlist> getSearchResultByInput(SearchInput input,
                                                            int start, int length);

    boolean savePortletPageConfig(ClientPortletPageConfiguration config);

    public double getMostRecentTransactionCurrencyFactor(long companyId,
                                                         long currencyId, long tdate) throws AccounterException;

    ClientPortletPageConfiguration getPortletPageConfiguration(String pageName);

    ArrayList<ClientPayee> getOwePayees(int oweType);

    ArrayList<RecentTransactionsList> getRecentTransactions(int limit);

    ArrayList<ClientMessageOrTask> getMessagesAndTasks()
            throws AccounterException;

    PaginationList<ClientReminder> getRemindersList(int start, int length,
                                                    int viewType) throws AccounterException;

    ExpensePortletData getAccountsAndValues(long startDate, long endDate);

    ClientEnterBill getEnterBillByEstimateId(long l) throws AccounterException;

    ClientWriteCheck getWriteCheckByEstimateId(long l)
            throws AccounterException;

    ClientCashPurchase getCashPurchaseByEstimateId(long id)
            throws AccounterException;

    ArrayList<ClientFixedAsset> getFixedAssetList(int status)
            throws AccounterException;

    ArrayList<ClientAdvertisement> getAdvertisements()
            throws AccounterException;

    ClientTransaction getTransactionToCreate(ClientRecurringTransaction obj,
                                             long transactionDate) throws AccounterException;

    PaginationList<PaymentsList> getPayeeChecks(int type, long fromDate,
                                                long toDate, int start, int length, int viewType);

    ArrayList<IncomeExpensePortletInfo> getIncomeExpensePortletInfo(int type,
                                                                    ClientFinanceDate startDate, ClientFinanceDate endDate)
            throws AccounterException;

    ExpensePortletData getIncomeBreakdownPortletData(
            ClientFinanceDate startDate, ClientFinanceDate endDate)
            throws AccounterException;

    ArrayList<PayeesBySalesPortletData> getTopCustomersBySalesPortletData(
            ClientFinanceDate startDate, ClientFinanceDate endDate, int limit)
            throws AccounterException;

    ArrayList<PayeesBySalesPortletData> getTopVendorsBySalesPortletData(
            ClientFinanceDate startDate, ClientFinanceDate endDate, int limit)
            throws AccounterException;

    ArrayList<PayeesBySalesPortletData> getItemsBySalesQuantity(
            ClientFinanceDate startDate, ClientFinanceDate endDate, int limit)
            throws AccounterException;

    public String printCheques(long chequeLayoutId,
                               ArrayList<PrintCheque> printCheques);

    ArrayList<PayeesBySalesPortletData> getItemsByPurchaseQuantity(
            ClientFinanceDate startDate, ClientFinanceDate endDate, int limit)
            throws AccounterException;

    ArrayList<YearOverYearPortletData> getAccountsBalancesByDate(
            ClientFinanceDate startDate, ClientFinanceDate endDate,
            long accountId, int chartType) throws AccounterException;

    ClientTDSResponsiblePerson getResponsiblePersonDetails();

    boolean savePortletConfiguration(ClientPortletConfiguration configuration);

    String getIRASFileInformation(ClientFinanceDate startDate,
                                  ClientFinanceDate endDate, boolean isXml) throws AccounterException;

    public PaginationList<ClientStatement> getBankStatements(long accountId);

    public PaginationList<TransactionsList> getSpentTransactionsList(
            long endOfFiscalYear, long date, int start, int length);

    public PaginationList<TransactionsList> getReceivedTransactionsList(
            long endOfFiscalYear, long date, int start, int length);

    ClientMakeDeposit getDepositByEstimateId(long id) throws AccounterException;

    boolean isChalanDetailsFiled(int formNo, int quater,
                                 ClientFinanceDate fromDate, ClientFinanceDate toDate,
                                 int startYear, int endYear) throws AccounterException;

    PaginationList<DepositsTransfersList> getDepositsList(long date,
                                                          long date2, int start, int length, int type)
            throws AccounterException;

    PaginationList<DepositsTransfersList> getTransfersList(long date,
                                                           long date2, int start, int length, int type)
            throws AccounterException;

    public Set<InvitableUser> getIvitableUsers() throws AccounterException;

    public int getClientCompaniesCount();

    HashMap<Integer, Object> importData(String filePath, int importerType,
                                        HashMap<String, String> importMap, String dateFormate)
            throws AccounterException;

    ArrayList<ImportField> getFieldsOf(int importerType)
            throws AccounterException;

    ArrayList<ClientJob> getJobsByCustomer(long id);

    PaginationList<TransactionHistory> getItemTransactionsList(long itemId,
                                                               int transactionType, int transactionStatus,
                                                               ClientFinanceDate startDate, ClientFinanceDate endDate, int start,
                                                               int length);

    PaginationList<ClientJob> getJobs();

    ArrayList<EstimatesAndSalesOrdersList> getSalesOrdersList(long id)
            throws AccounterException;

    PaginationList<ClientTAXAdjustment> getTaxAdjustmentsList(int viewType,
                                                              long startDate, long endDate, int start, int length);

    void mergeClass(ClientAccounterClass clientClass,
                    ClientAccounterClass clientClass1) throws AccounterException;

    Long getTransaction(boolean isPrev, long id, int type, int subType)
            throws AccounterException;

    void mergeLocation(ClientLocation clientFromLocation,
                       ClientLocation clientToLocation) throws AccounterException;

    ArrayList<ItemUnitPrice> getUnitPricesByPayee(boolean isCust, long payee,
                                                  long item) throws AccounterException;

    PaginationList<PaymentsList> getPayRunsList(ClientFinanceDate startDate,
                                                ClientFinanceDate endDate, int start, int length, int type,
                                                int transactionType) throws AccounterException;

    PaginationList<ClientTAXReturn> getTaxReturnList(long startDate,
                                                     long endDate, int start, int length, int viewId);

    PaginationList<ClientBuildAssembly> getBuildAssembliesList(long startDate,
                                                               long endDate, int start, int length, int viewId);

    PaginationList<ClientPayTAX> getPayTaxList(long startDate, long endDate,
                                               int start, int length, int viewId);

    PaginationList<ClientReceiveVAT> getTaxRefundsList(long startDate,
                                                       long endDate, int start, int length, int viewId);

    List<ClientPaypalTransation> getNewPaypalTransactionsList(long accountID);

    PaginationList<ClientPaypalTransation> getSavedPaypalTransaction(
            ClientAccount clientAccount);

    ArrayList<ClientAccount> getPaypalAccounts();

    String getPaypalTransactionDetailsForId(String transactionID, long accountID);

    List<ConsultantsDetailsList> getConsultantItemsDetails();

}