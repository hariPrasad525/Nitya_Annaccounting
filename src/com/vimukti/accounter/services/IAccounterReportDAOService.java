package com.vimukti.accounter.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXReturn;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.AccountBalance;
import com.vimukti.accounter.web.client.core.reports.AccountRegister;
import com.vimukti.accounter.web.client.core.reports.AgedDebtors;
import com.vimukti.accounter.web.client.core.reports.AmountsDueToVendor;
import com.vimukti.accounter.web.client.core.reports.ClientBudgetList;
import com.vimukti.accounter.web.client.core.reports.DepositDetail;
import com.vimukti.accounter.web.client.core.reports.MISC1099TransactionDetail;
import com.vimukti.accounter.web.client.core.reports.MostProfitableCustomers;
import com.vimukti.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.vimukti.accounter.web.client.core.reports.SalesTaxLiability;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByAccount;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByTaxItem;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.client.core.reports.VATDetailReport;
import com.vimukti.accounter.web.client.core.reports.VATItemDetail;
import com.vimukti.accounter.web.client.core.reports.VATSummary;

public interface IAccounterReportDAOService {

	public ArrayList<AccountBalance> getAccountBalances() throws DAOException;

	public ArrayList<TrialBalance> getTrialBalance(String startDate,
			String endDate) throws DAOException;

	public ArrayList<AgedDebtors> getAgedDebtors(String startDate,
			String endDate) throws DAOException;

	public ArrayList<AgedDebtors> getAgedCreditors(String startDate,
			String endDate) throws DAOException;

	public ArrayList<SalesByCustomerDetail> getSalesByCustomerDetailReport(
			String startDate, String endDate) throws DAOException;

	public ArrayList<SalesByCustomerDetail> getSalesByCustomerSummary(
			String startDate, String endDate) throws DAOException;

	public ArrayList<SalesByCustomerDetail> getSalesByItemDetail(
			String startDate, String endDate) throws DAOException;

	public ArrayList<SalesByCustomerDetail> getSalesByItemSummary(
			String startDate, String endDate) throws DAOException;

	public ArrayList<TransactionHistory> getCustomerTransactionHistory(
			String startDate, String endDate) throws DAOException;

	public ArrayList<DepositDetail> getDepositDetail(String startDate,
			String endDate) throws DAOException;

	public ArrayList<SalesByCustomerDetail> getPurchasesByVendorDetail(
			String startDate, String endDate) throws DAOException;

	public ArrayList<SalesByCustomerDetail> getPurchasesByVendorSummary(
			String startDate, String endDate) throws DAOException;

	public ArrayList<SalesByCustomerDetail> getPurchasesByItemDetail(
			String startDate, String endDate) throws DAOException;

	public ArrayList<SalesByCustomerDetail> getPurchasesByItemSummary(
			String startDate, String endDate) throws DAOException;

	public ArrayList<TransactionHistory> getVendorTransactionHistory(
			String startDate, String endDate) throws DAOException;

	public ArrayList<AmountsDueToVendor> getAmountsDueToVendor(
			String startDate, String endDate) throws DAOException;

	public ArrayList<MostProfitableCustomers> getMostProfitableCustomers(
			String startDate, String endDate) throws DAOException;

	public ArrayList<MostProfitableCustomers> getProfitabilityByCustomerDetail(
			String customer, String startDate, String endDate)
			throws DAOException;

	public ArrayList<TransactionDetailByTaxItem> getTransactionDetailByTaxItem(
			String startDate, String endDate) throws DAOException;

	public ArrayList<Transaction> getRegister(Account account)
			throws DAOException;

	public ArrayList<AccountRegister> getAccountRegister(String startDate,
			String endDate, String accountId) throws DAOException;

	public ArrayList<TransactionDetailByAccount> getTransactionDetailByAccount(
			final String startDate, final String endDate) throws DAOException;

	public ArrayList<SalesTaxLiability> getSalesTaxLiabilityReport(
			final String startDate, final String endDate) throws DAOException;

	public ArrayList<Customer> getTransactionHistoryCustomers(String startDate,
			String endDate) throws DAOException;

	public ArrayList<Vendor> getTransactionHistoryVendors(String startDate,
			String endDate) throws DAOException;

	public ArrayList<Item> getSalesReportItems(String startDate, String endDate)
			throws DAOException;

	public ArrayList<Item> getPurchaseReportItems(String startDate,
			String endDate) throws DAOException;

	public Date[] getMinimumAndMaximumTransactionDate() throws DAOException;

	public ArrayList<TrialBalance> getBalanceSheetReport(String startDate,
			String endDate) throws DAOException;

	public ArrayList<TrialBalance> getProfitAndLossReport(String startDate,
			String endDate) throws DAOException;

	public ArrayList<TrialBalance> getCashFlowReport(String startDate,
			String endDate) throws DAOException;

	public ArrayList<SalesByCustomerDetail> getSalesByCustomerDetailReport(
			String customerName, String startDate, String endDate)
			throws DAOException;

	public ArrayList<SalesByCustomerDetail> getSalesByItemDetail(
			String itemName, String startDate, String endDate)
			throws DAOException;

	public ArrayList<SalesByCustomerDetail> getPurchasesByVendorDetail(
			String vendorName, String startDate, String endDate)
			throws DAOException;

	public ArrayList<SalesByCustomerDetail> getPurchasesByItemDetail(
			String itemName, String startDate, String endDate)
			throws DAOException;

	public ArrayList<TransactionDetailByAccount> getTransactionDetailByAccount(
			String accountName, final String startDate, final String endDate)
			throws DAOException;

	public ArrayList<TransactionDetailByTaxItem> getTransactionDetailByTaxItem(
			final String taxItemName, final String startDate,
			final String endDate) throws DAOException;

	public ArrayList<MISC1099TransactionDetail> getMISC1099TransactionDetailReport(
			String debitorName, ClientFinanceDate fromDate,
			ClientFinanceDate toDate);

	public ArrayList<ClientBudgetList> getBudgetItemsList(long id);

	// For UK

	public Map<String, Double> getVATReturnBoxes(String startDate,
			String endDate) throws DAOException;

	public VATSummary getPriorReturnVATSummary(TAXAgency vatAgency, Date endDate)
			throws DAOException;

	public VATDetailReport getPriorVATReturnVATDetailReport(
			TAXAgency vatAgency, Date endDate) throws DAOException;

	public TAXReturn getVATReturnDetails(TAXAgency vatAgency, Date fromDate,
			Date toDate) throws DAOException;

	public ArrayList<VATItemDetail> getVATItemDetailReport(String fromDate,
			String toDate, long companyId) throws DAOException;

	public void createTaxes(int... vatReturnType) throws DAOException;

	public Long getNextNominalCode(int accountType) throws DAOException;
}
