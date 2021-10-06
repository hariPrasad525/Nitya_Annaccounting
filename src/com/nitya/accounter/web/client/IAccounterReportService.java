package com.nitya.accounter.web.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.nitya.accounter.web.client.core.ClientCustomer;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientItem;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientVendor;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.core.ReportInput;
import com.nitya.accounter.web.client.core.Lists.DummyDebitor;
import com.nitya.accounter.web.client.core.Lists.OpenAndClosedOrders;
import com.nitya.accounter.web.client.core.Lists.PayeeStatementsList;
import com.nitya.accounter.web.client.core.reports.AccountRegister;
import com.nitya.accounter.web.client.core.reports.AgedDebtors;
import com.nitya.accounter.web.client.core.reports.AmountsDueToVendor;
import com.nitya.accounter.web.client.core.reports.BankCheckDetail;
import com.nitya.accounter.web.client.core.reports.BankDepositDetail;
import com.nitya.accounter.web.client.core.reports.BudgetActuals;
import com.nitya.accounter.web.client.core.reports.ClientBudgetList;
import com.nitya.accounter.web.client.core.reports.DepositDetail;
import com.nitya.accounter.web.client.core.reports.DepreciationShedule;
import com.nitya.accounter.web.client.core.reports.ECSalesList;
import com.nitya.accounter.web.client.core.reports.ECSalesListDetail;
import com.nitya.accounter.web.client.core.reports.EstimatesByJob;
import com.nitya.accounter.web.client.core.reports.ExpenseList;
import com.nitya.accounter.web.client.core.reports.IncomeByCustomerDetail;
import com.nitya.accounter.web.client.core.reports.InventoryDetails;
import com.nitya.accounter.web.client.core.reports.InventoryStockStatusDetail;
import com.nitya.accounter.web.client.core.reports.InventoryValutionDetail;
import com.nitya.accounter.web.client.core.reports.InventoryValutionSummary;
import com.nitya.accounter.web.client.core.reports.ItemActualCostDetail;
import com.nitya.accounter.web.client.core.reports.JobActualCostDetail;
import com.nitya.accounter.web.client.core.reports.JobProfitability;
import com.nitya.accounter.web.client.core.reports.JobProfitabilityDetailByJob;
import com.nitya.accounter.web.client.core.reports.MISC1099TransactionDetail;
import com.nitya.accounter.web.client.core.reports.MostProfitableCustomers;
import com.nitya.accounter.web.client.core.reports.PayHeadDetails;
import com.nitya.accounter.web.client.core.reports.PayHeadSummary;
import com.nitya.accounter.web.client.core.reports.ProfitAndLossByLocation;
import com.nitya.accounter.web.client.core.reports.RealisedExchangeLossOrGain;
import com.nitya.accounter.web.client.core.reports.ReconcilationItemList;
import com.nitya.accounter.web.client.core.reports.Reconciliation;
import com.nitya.accounter.web.client.core.reports.ReconciliationDiscrepancy;
import com.nitya.accounter.web.client.core.reports.ReverseChargeList;
import com.nitya.accounter.web.client.core.reports.ReverseChargeListDetail;
import com.nitya.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.nitya.accounter.web.client.core.reports.SalesByLocationDetails;
import com.nitya.accounter.web.client.core.reports.SalesByLocationSummary;
import com.nitya.accounter.web.client.core.reports.SalesTaxLiability;
import com.nitya.accounter.web.client.core.reports.TDSAcknowledgmentsReport;
import com.nitya.accounter.web.client.core.reports.TransactionDetailByAccount;
import com.nitya.accounter.web.client.core.reports.TransactionDetailByTaxItem;
import com.nitya.accounter.web.client.core.reports.TransactionHistory;
import com.nitya.accounter.web.client.core.reports.TrialBalance;
import com.nitya.accounter.web.client.core.reports.UnRealisedLossOrGain;
import com.nitya.accounter.web.client.core.reports.UnbilledCostsByJob;
import com.nitya.accounter.web.client.core.reports.UncategorisedAmountsReport;
import com.nitya.accounter.web.client.core.reports.VATDetail;
import com.nitya.accounter.web.client.core.reports.VATItemDetail;
import com.nitya.accounter.web.client.core.reports.VATItemSummary;
import com.nitya.accounter.web.client.core.reports.VATSummary;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.reports.CheckDetailReport;
import com.nitya.accounter.web.client.ui.reports.CurrencyExchangeRate;
import com.nitya.accounter.web.client.ui.reports.TAXItemDetail;

public interface IAccounterReportService extends RemoteService {

	public ArrayList<SalesByCustomerDetail> getSalesByCustomerSummary(
			final ClientFinanceDate startDate, final ClientFinanceDate endDate);

	// public ArrayList<AccountBalance> getAccountBalances();

	public ArrayList<TrialBalance> getTrialBalance(ClientFinanceDate startDate,
			ClientFinanceDate endDate);

	public ArrayList<AgedDebtors> getAgedDebtors(ClientFinanceDate startDate,
			ClientFinanceDate endDate);

	public ArrayList<AgedDebtors> getAgedCreditors(ClientFinanceDate startDate,
			ClientFinanceDate endDate);

	public ArrayList<SalesByCustomerDetail> getSalesByCustomerDetailReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<SalesByCustomerDetail> getSalesByItemDetail(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<SalesByCustomerDetail> getSalesByItemSummary(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<TransactionHistory> getCustomerTransactionHistory(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<DepositDetail> getDepositDetail(
			final ClientFinanceDate startDate, final ClientFinanceDate endDate);

	public ArrayList<SalesByCustomerDetail> getPurchasesByVendorDetail(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<SalesByCustomerDetail> getPurchasesByVendorSummary(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<SalesByCustomerDetail> getPurchasesByItemDetail(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<SalesByCustomerDetail> getPurchasesByItemSummary(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<TransactionHistory> getVendorTransactionHistory(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<AmountsDueToVendor> getAmountsDueToVendor(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<MostProfitableCustomers> getMostProfitableCustomers(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<TransactionDetailByTaxItem> getTransactionDetailByTaxItem(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	ArrayList<ClientTransaction> getRegister(long accountId);

	PaginationList<AccountRegister> getAccountRegister(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long accountId, int start, int lenght);

	public ArrayList<ClientCustomer> getTransactionHistoryCustomers(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<ClientVendor> getTransactionHistoryVendors(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<ClientItem> getSalesReportItems(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<ClientItem> getPurchaseReportItems(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	ArrayList<TransactionDetailByAccount> getTransactionDetailByAccount(
			long accountId, ClientFinanceDate startDate,
			ClientFinanceDate endDate);

	public ArrayList<TransactionDetailByAccount> getAutomaticTransactions(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<SalesTaxLiability> getSalesTaxLiabilityReport(
			final ClientFinanceDate startDate, final ClientFinanceDate endDate);

	public ArrayList<SalesByCustomerDetail> getSalesByCustomerDetailReport(
			String customerName, ClientFinanceDate startDate,
			ClientFinanceDate endDate);

	public ArrayList<SalesByCustomerDetail> getSalesByItemDetail(
			String itemName, ClientFinanceDate startDate,
			ClientFinanceDate endDate);

	public ArrayList<SalesByCustomerDetail> getPurchasesByVendorDetail(
			String vendorName, ClientFinanceDate startDate,
			ClientFinanceDate endDate);

	public ArrayList<SalesByCustomerDetail> getPurchasesByItemDetail(
			String itemName, ClientFinanceDate startDate,
			ClientFinanceDate endDate);

	public ArrayList<ClientFinanceDate> getMinimumAndMaximumTransactionDate();

	public ArrayList<TransactionDetailByTaxItem> getTransactionDetailByTaxItem(
			String taxItemname, ClientFinanceDate startDate,
			ClientFinanceDate endDate);

	public ArrayList<TrialBalance> getBalanceSheetReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<TrialBalance> getProfitAndLossReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	ArrayList<ProfitAndLossByLocation> getProfitAndLossByLocationReport(
			int categoryType, ClientFinanceDate startDate,
			ClientFinanceDate endDate);

	ArrayList<SalesByLocationDetails> getSalesByLocationDetailsReport(
			boolean isLocation, boolean isCustomer,
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<TrialBalance> getCashFlowReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	// public ArrayList<OpenAndClosedOrders> getPurchaseOpenOrderReport(
	// ClientFinanceDate startDate, ClientFinanceDate endDate);

	// public ArrayList<OpenAndClosedOrders> getPurchaseCompletedOrderReport(
	// ClientFinanceDate startDate, ClientFinanceDate endDate);

	// public ArrayList<OpenAndClosedOrders> getPurchaseCancelledOrderReport(
	// ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<OpenAndClosedOrders> getPurchaseOrderReport(int type,
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	// public ArrayList<OpenAndClosedOrders> getPurchaseClosedOrderReport(
	// ClientFinanceDate startDate, ClientFinanceDate endDate);

	// public ArrayList<OpenAndClosedOrders> getSalesOpenOrderReport(
	// ClientFinanceDate startDate, ClientFinanceDate endDate);
	//
	// public ArrayList<OpenAndClosedOrders> getSalesCompletedOrderReport(
	// ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<OpenAndClosedOrders> getSalesOrderReport(int type,
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	// public ArrayList<OpenAndClosedOrders> getSalesCancelledOrderReport(
	// ClientFinanceDate startDate, ClientFinanceDate endDate);
	//
	// public ArrayList<OpenAndClosedOrders> getSalesClosedOrderReport(
	// ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<VATDetail> getPriorVATReturnVATDetailReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	ArrayList<VATDetail> getPriorVATReturnReport(long vatAgancy,
			ClientFinanceDate endDate);

	ArrayList<VATSummary> getPriorReturnVATSummary(long vatAgency,
			ClientFinanceDate endDate);

	ArrayList<VATSummary> getVAT100Report(long vatAgncy,
			ClientFinanceDate fromDate, ClientFinanceDate toDate);

	public ArrayList<UncategorisedAmountsReport> getUncategorisedAmountsReport(
			ClientFinanceDate fromDate, ClientFinanceDate toDate);

	public ArrayList<ECSalesList> getECSalesListReport(
			ClientFinanceDate fromDate, ClientFinanceDate toDate);

	public ArrayList<ECSalesList> getECSalesListReport(
			ClientFinanceDate fromDate, ClientFinanceDate toDate, long companyId);

	public ArrayList<ECSalesListDetail> getECSalesListDetailReport(
			String payeeName, ClientFinanceDate fromDate,
			ClientFinanceDate toDate);

	public ArrayList<ReverseChargeListDetail> getReverseChargeListDetailReport(
			String payeeName, ClientFinanceDate fromDate,
			ClientFinanceDate toDate);

	public ArrayList<ReverseChargeList> getReverseChargeListReport(
			ClientFinanceDate fromDate, ClientFinanceDate toDate);

	public ArrayList<DummyDebitor> getDebitors(ClientFinanceDate startDate,
			ClientFinanceDate endDate) throws AccounterException;

	public ArrayList<DummyDebitor> getCreditors(ClientFinanceDate startDate,
			ClientFinanceDate endDate) throws AccounterException;

	public ArrayList<AgedDebtors> getAgedDebtors(String Name,
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<AgedDebtors> getAgedCreditors(String Name,
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<VATItemSummary> getVATItemSummaryReport(
			ClientFinanceDate fromDate, ClientFinanceDate toDate);

	public ArrayList<VATItemDetail> getVATItemDetailReport(String vatItemName,
			ClientFinanceDate fromDate, ClientFinanceDate toDate);

	public ArrayList<ExpenseList> getExpenseReportByType(int status,
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	ArrayList<CheckDetailReport> getCheckDetailReport(long paymentmethod,
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	ArrayList<PayeeStatementsList> getCustomerStatement(long customer,
			long fromDate, long toDate) throws AccounterException;

	ArrayList<SalesByLocationSummary> getSalesByLocationSummaryReport(
			boolean isLocation, boolean isCustomer,
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	ArrayList<SalesByLocationDetails> getSalesByLocationDetailsForLocation(
			boolean isLocation, boolean isCustomer, String locationName,
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<MISC1099TransactionDetail> getMISC1099TransactionDetailReport(
			long vendorId, int boxNo, ClientFinanceDate fromDate,
			ClientFinanceDate toDate);

	ArrayList<ClientBudgetList> getBudgetItemsList(long budgetID);

	ArrayList<TAXItemDetail> getTAXItemDetailReport(long taxAgency,
			long startDate, long endDate) throws AccounterException;

	ArrayList<VATDetail> getVATExceptionDetailReport(ClientFinanceDate start,
			ClientFinanceDate end, long taxRetunId) throws AccounterException;

	ArrayList<TAXItemDetail> getTAXItemExceptionDetailReport(long taxAgency,
			long startDate, long endDate) throws AccounterException;

	ArrayList<TDSAcknowledgmentsReport> getTDSAcknowledgments(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	ArrayList<PayeeStatementsList> getStatements(boolean isVendor, long id,
			int viewType, ClientFinanceDate fromDate, ClientFinanceDate toDate);

	ArrayList<Reconciliation> getAllReconciliations(ClientFinanceDate start,
			ClientFinanceDate end, long companyId);

	ArrayList<DepreciationShedule> getDepreciationSheduleReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate, int status,
			long comapnyId);

	ArrayList<ReconcilationItemList> getReconciliationItemByBankAccountID(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long bankAccountId, long comapnyId);

	PaginationList<TransactionHistory> getCustomerTransactionsList(long id,
			int transactionType, int transactionStatusType,
			ClientFinanceDate startDate, ClientFinanceDate endDate, int start,
			int length);

	List<TransactionHistory> getConsultantTransactionsList(long id,
            int transactionType, int transactionStatusType,
            ClientFinanceDate startDate, ClientFinanceDate endDate, int start,
            int length);

	PaginationList<TransactionHistory> getVendorTransactionsList(long id,
			int transactionType, int transactionStatusType,
			ClientFinanceDate startDate, ClientFinanceDate endDate, int start,
			int length);

	public ArrayList<BudgetActuals> getBudgetvsAcualReportData(long id,
			ClientFinanceDate start, ClientFinanceDate end, int type);

	ArrayList<RealisedExchangeLossOrGain> getRealisedExchangeLossesAndGains(
			ClientFinanceDate start, ClientFinanceDate end)
			throws AccounterException;

	ArrayList<UnRealisedLossOrGain> getUnRealisedExchangeLossesAndGains(
			long enteredDate, HashMap<Long, Double> exchangeRates)
			throws AccounterException;

	ArrayList<CurrencyExchangeRate> getExchangeRatesOfDate(long date)
			throws AccounterException;

	ArrayList<InventoryStockStatusDetail> getInventoryStockStatusByVendor(
			ClientFinanceDate start, ClientFinanceDate end)
			throws AccounterException;

	ArrayList<InventoryValutionDetail> getInventoryValutionDetail(long id,
			ClientFinanceDate start, ClientFinanceDate end)
			throws AccounterException;

	ArrayList<InventoryStockStatusDetail> getInventoryStockStatusByItem(
			ClientFinanceDate start, ClientFinanceDate end)
			throws AccounterException;

	ArrayList<InventoryValutionSummary> getInventoryValutionSummary(
			long wareHouseId, ClientFinanceDate start, ClientFinanceDate end)
			throws AccounterException;

	ArrayList<BankDepositDetail> getBankingDepositDetils(
			ClientFinanceDate start, ClientFinanceDate end)
			throws AccounterException;

	ArrayList<TransactionDetailByAccount> getMissingCheckDetils(long accountId,
			ClientFinanceDate start, ClientFinanceDate end)
			throws AccounterException;

	ArrayList<ReconciliationDiscrepancy> getReconciliationDiscrepancy(
			long accountId, ClientFinanceDate start, ClientFinanceDate end)
			throws AccounterException;

	ArrayList<BankCheckDetail> getBankCheckDetils(ClientFinanceDate start,
			ClientFinanceDate end) throws AccounterException;

	ArrayList<EstimatesByJob> getEstimatesByJob(ClientFinanceDate start,
			ClientFinanceDate end);

	ArrayList<JobActualCostDetail> getJobActualCostOrRevenueDetails(
			ClientFinanceDate start, ClientFinanceDate end,
			boolean isActualcostDetail, long transactionId, long jobId);

	ArrayList<JobProfitability> getJobProfitabilitySummaryReport(
			ClientFinanceDate start, ClientFinanceDate end)
			throws AccounterException;

	ArrayList<UnbilledCostsByJob> getUnBilledCostsByJob(
			ClientFinanceDate start, ClientFinanceDate end);

	ArrayList<ItemActualCostDetail> getItemActualCostDetail(
			ClientFinanceDate start, ClientFinanceDate end, long itemId,
			long customerId, long jobId, boolean isActualcostDetail);

	ArrayList<JobProfitabilityDetailByJob> getJobProfitabilityDetailByJobReport(
			long payeeId, long jobId, ClientFinanceDate start,
			ClientFinanceDate end) throws AccounterException;

	ArrayList<TransactionDetailByAccount> getTransactionDetailByAccountAndCategory(
			int categoryType, long categoryId, long accountId,
			ClientFinanceDate start, ClientFinanceDate end);

	ArrayList<String> exportToFile(int exportType, int reportType,
			long startDate, long endDate, ReportInput[] inputs)
			throws AccounterException;

	ArrayList<IncomeByCustomerDetail> getIncomeByCustomerDetail(
			ClientFinanceDate startDate, ClientFinanceDate endDate)
			throws AccounterException;

	ArrayList<InventoryDetails> getInventoryDetails(ClientFinanceDate start,
			ClientFinanceDate end);

	ArrayList<PayHeadSummary> getPayHeadSummaryReport(long payHeadId,
			ClientFinanceDate start, ClientFinanceDate end)
			throws AccounterException;

	ArrayList<PayHeadDetails> getPayHeadDetailReportList(long employeeId,
			long payHeadId, ClientFinanceDate fromDate, ClientFinanceDate toDate)
			throws AccounterException;

	ArrayList<PayHeadSummary> getPayTaxSummaryReport(String taxType, long employeeId,
			ClientFinanceDate start, ClientFinanceDate end)
			throws AccounterException;

}
