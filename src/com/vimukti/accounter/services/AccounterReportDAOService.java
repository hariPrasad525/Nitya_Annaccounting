package com.vimukti.accounter.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.support.TransactionTemplate;

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

public class AccounterReportDAOService extends HibernateDaoSupport implements
		IAccounterReportDAOService {

	TransactionTemplate transactionTemplate;
	IAccounterDAOService accounterDao;

	public void setTransactionTemplate(TransactionTemplate template) {
		this.transactionTemplate = template;
	}

	public void setAccounterDao(IAccounterDAOService accounterDao) {
		this.accounterDao = accounterDao;
	}

	// private long getLongIdForGivenid(AccounterCoreType entity, String
	// account) {
	//
	// Session session = Utility.getCurrentSession();
	// // String hqlQuery = "select entity.id from "
	// // + entity.getServerClassSimpleName()
	// // + " entity where entity.id=?";
	// Query query = session.getNamedQuery(
	// "get.entity.from." + entity.getServerClassSimpleName())
	// .setString(0, account);
	// ArrayList l = query.ArrayList();
	// if (l != null && l.get(0) != null) {
	// return (Long) l.get(0);
	// } else
	// return 0;
	//
	// }

	@Override
	public void createTaxes(int... vatReturnType) throws DAOException {

	}

	@Override
	public ArrayList<AccountBalance> getAccountBalances() throws DAOException {

		return null;
	}

	@Override
	public ArrayList<AccountRegister> getAccountRegister(String startDate,
			String endDate, String accountId) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<AgedDebtors> getAgedCreditors(String startDate,
			String endDate) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<AgedDebtors> getAgedDebtors(String startDate,
			String endDate) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<AmountsDueToVendor> getAmountsDueToVendor(
			String startDate, String endDate) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<TrialBalance> getBalanceSheetReport(String startDate,
			String endDate) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<TrialBalance> getCashFlowReport(String startDate,
			String endDate) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<TransactionHistory> getCustomerTransactionHistory(
			String startDate, String endDate) throws DAOException {

		return null;
	}

	@Override
	public Date[] getMinimumAndMaximumTransactionDate() throws DAOException {

		return null;
	}

	@Override
	public ArrayList<MostProfitableCustomers> getMostProfitableCustomers(
			String startDate, String endDate) throws DAOException {

		return null;
	}

	@Override
	public Long getNextNominalCode(int accountType) throws DAOException {

		return null;
	}

	@Override
	public VATSummary getPriorReturnVATSummary(TAXAgency vatAgency, Date endDate)
			throws DAOException {

		return null;
	}

	@Override
	public VATDetailReport getPriorVATReturnVATDetailReport(
			TAXAgency vatAgency, Date endDate) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<TrialBalance> getProfitAndLossReport(String startDate,
			String endDate) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<MostProfitableCustomers> getProfitabilityByCustomerDetail(
			String customer, String startDate, String endDate)
			throws DAOException {

		return null;
	}

	@Override
	public ArrayList<Item> getPurchaseReportItems(String startDate,
			String endDate) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<SalesByCustomerDetail> getPurchasesByItemDetail(
			String startDate, String endDate) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<SalesByCustomerDetail> getPurchasesByItemDetail(
			String itemName, String startDate, String endDate)
			throws DAOException {

		return null;
	}

	@Override
	public ArrayList<SalesByCustomerDetail> getPurchasesByItemSummary(
			String startDate, String endDate) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<SalesByCustomerDetail> getPurchasesByVendorDetail(
			String startDate, String endDate) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<SalesByCustomerDetail> getPurchasesByVendorDetail(
			String vendorName, String startDate, String endDate)
			throws DAOException {

		return null;
	}

	@Override
	public ArrayList<SalesByCustomerDetail> getPurchasesByVendorSummary(
			String startDate, String endDate) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<Transaction> getRegister(Account account)
			throws DAOException {

		return null;
	}

	@Override
	public ArrayList<SalesByCustomerDetail> getSalesByCustomerDetailReport(
			String startDate, String endDate) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<SalesByCustomerDetail> getSalesByCustomerDetailReport(
			String customerName, String startDate, String endDate)
			throws DAOException {

		return null;
	}

	@Override
	public ArrayList<SalesByCustomerDetail> getSalesByCustomerSummary(
			String startDate, String endDate) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<SalesByCustomerDetail> getSalesByItemDetail(
			String startDate, String endDate) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<SalesByCustomerDetail> getSalesByItemDetail(
			String itemName, String startDate, String endDate)
			throws DAOException {

		return null;
	}

	@Override
	public ArrayList<SalesByCustomerDetail> getSalesByItemSummary(
			String startDate, String endDate) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<Item> getSalesReportItems(String startDate, String endDate)
			throws DAOException {

		return null;
	}

	@Override
	public ArrayList<SalesTaxLiability> getSalesTaxLiabilityReport(
			String startDate, String endDate) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<TransactionDetailByAccount> getTransactionDetailByAccount(
			String startDate, String endDate) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<TransactionDetailByAccount> getTransactionDetailByAccount(
			String accountName, String startDate, String endDate)
			throws DAOException {

		return null;
	}

	@Override
	public ArrayList<TransactionDetailByTaxItem> getTransactionDetailByTaxItem(
			String startDate, String endDate) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<TransactionDetailByTaxItem> getTransactionDetailByTaxItem(
			String taxItemName, String startDate, String endDate)
			throws DAOException {

		return null;
	}

	@Override
	public ArrayList<Customer> getTransactionHistoryCustomers(String startDate,
			String endDate) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<Vendor> getTransactionHistoryVendors(String startDate,
			String endDate) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<TrialBalance> getTrialBalance(String startDate,
			String endDate) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<VATItemDetail> getVATItemDetailReport(String fromDate,
			String toDate, long companyId) throws DAOException {

		return null;
	}

	@Override
	public Map<String, Double> getVATReturnBoxes(String startDate,
			String endDate) throws DAOException {

		return null;
	}

	@Override
	public TAXReturn getVATReturnDetails(TAXAgency vatAgency, Date fromDate,
			Date toDate) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<TransactionHistory> getVendorTransactionHistory(
			String startDate, String endDate) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<DepositDetail> getDepositDetail(String startDate,
			String endDate) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<MISC1099TransactionDetail> getMISC1099TransactionDetailReport(
			String debitorName, ClientFinanceDate fromDate,
			ClientFinanceDate toDate) {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<ClientBudgetList> getBudgetItemsList(long id) {
		return null;

	}
}
