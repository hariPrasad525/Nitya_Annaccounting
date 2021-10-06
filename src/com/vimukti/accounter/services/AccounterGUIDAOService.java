package com.vimukti.accounter.services;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.support.TransactionTemplate;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.CashPurchase;
import com.vimukti.accounter.core.CashSales;
import com.vimukti.accounter.core.CreditCardCharge;
import com.vimukti.accounter.core.CreditsAndPayments;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.CustomerRefund;
import com.vimukti.accounter.core.EnterBill;
import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.JournalEntry;
import com.vimukti.accounter.core.ReceivePayment;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXReturn;
import com.vimukti.accounter.core.TAXReturnEntry;
import com.vimukti.accounter.core.TransferFund;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.core.WriteCheck;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.core.Lists.CustomerRefundsList;
import com.vimukti.accounter.web.client.core.Lists.DepreciableFixedAssetsList;
import com.vimukti.accounter.web.client.core.Lists.EstimatesAndSalesOrdersList;
import com.vimukti.accounter.web.client.core.Lists.FixedAssetLinkedAccountMap;
import com.vimukti.accounter.web.client.core.Lists.FixedAssetList;
import com.vimukti.accounter.web.client.core.Lists.FixedAssetSellOrDisposeReviewJournal;
import com.vimukti.accounter.web.client.core.Lists.InvoicesList;
import com.vimukti.accounter.web.client.core.Lists.IssuePaymentTransactionsList;
import com.vimukti.accounter.web.client.core.Lists.KeyFinancialIndicators;
import com.vimukti.accounter.web.client.core.Lists.MakeDepositTransactionsList;
import com.vimukti.accounter.web.client.core.Lists.OverDueInvoicesList;
import com.vimukti.accounter.web.client.core.Lists.PayBillTransactionList;
import com.vimukti.accounter.web.client.core.Lists.PaymentsList;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersAndItemReceiptsList;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersList;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentTransactionList;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentsList;
import com.vimukti.accounter.web.client.core.Lists.SellingOrDisposingFixedAssetList;
import com.vimukti.accounter.web.client.core.Lists.TempFixedAsset;

public class AccounterGUIDAOService extends HibernateDaoSupport implements
		IAccounterGUIDAOService {

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
	// Query query = session.getNamedQuery(
	// "get.entity.from." + entity.getServerClassFullyQualifiedName())
	// .setString(0, account);
	// List<?> l = query.list();
	// if (l != null && !l.isEmpty() && l.get(0) != null) {
	// return (Long) l.get(0);
	// } else
	// return 0;
	//
	// }

	@Override
	public boolean canVoidOrEdit(String invoiceOrVendorBillId)
			throws DAOException {
		return false;
	}

	@Override
	public void changeDepreciationStartDateTo(Date newStartDate)
			throws DAOException {

	}

	@Override
	public void changeFiscalYearsStartDateTo(Date newStartDate)
			throws DAOException {

	}

	@Override
	public ArrayList<Date> getAllDepreciationFromDates() throws DAOException {

		return null;
	}

	@Override
	public ArrayList<BillsList> getBillsList() throws DAOException {

		return null;
	}

	@Override
	public ArrayList<EnterBill> getBillsOwed() throws DAOException {

		return null;
	}

	@Override
	public double getCalculatedDepreciatedAmount(int depreciationMethod,
			double depreciationRate, double purchasePrice,
			Date depreciationFrom, Date depreciationTo) throws DAOException {

		return 0;
	}

	@Override
	public double getCalculatedRollBackDepreciationAmount(String fixedAssetID,
			Date rollBackDepreciationTo) throws DAOException {

		return 0;
	}

	@Override
	public ArrayList<IssuePaymentTransactionsList> getChecks()
			throws DAOException {

		return null;
	}

	@Override
	public ArrayList<IssuePaymentTransactionsList> getChecks(String account)
			throws DAOException {

		return null;
	}

	@Override
	public ArrayList<CreditCardCharge> getCreditCardChargesThisMonth(Date date)
			throws DAOException {

		return null;
	}

	@Override
	public ArrayList<CreditsAndPayments> getCustomerCreditsAndPayments(
			String customer) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<CustomerRefundsList> getCustomerRefundsList()
			throws DAOException {

		return null;
	}

	@Override
	public DepreciableFixedAssetsList getDepreciableFixedAssets(
			Date depreciationFrom, Date depreciationTo) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<DepreciableFixedAssetsList> getDepreciationFixedAssets(
			Date startDate, Date toDate) throws DAOException {

		return null;
	}

	@Override
	public Date getDepreciationLastDate() throws DAOException {

		return null;
	}

	@Override
	public ArrayList<Estimate> getEstimates() throws DAOException {

		return null;
	}

	@Override
	public ArrayList<Estimate> getEstimates(String customer)
			throws DAOException {

		return null;
	}

	@Override
	public ArrayList<EstimatesAndSalesOrdersList> getEstimatesAndSalesOrdersList(
			String customerId) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<Date> getFinancialYearStartDates() throws DAOException {

		return null;
	}

	@Override
	public ArrayList<FixedAssetList> getFixedAssets(int status)
			throws DAOException {

		return null;
	}

	@Override
	public ArrayList<InvoicesList> getInvoiceList() throws DAOException {

		return null;
	}

	@Override
	public ArrayList<JournalEntry> getJournalEntries() throws DAOException {

		return null;
	}

	@Override
	public JournalEntry getJournalEntry(String journalEntryId)
			throws DAOException {

		return null;
	}

	@Override
	public KeyFinancialIndicators getKeyFinancialIndicators()
			throws DAOException {

		return null;
	}

	@Override
	public ArrayList<BillsList> getLatestBills() throws DAOException {

		return null;
	}

	@Override
	public ArrayList<CashPurchase> getLatestCashPurchases() throws DAOException {

		return null;
	}

	@Override
	public ArrayList<CashSales> getLatestCashSales() throws DAOException {

		return null;
	}

	@Override
	public ArrayList<WriteCheck> getLatestChecks() throws DAOException {

		return null;
	}

	@Override
	public ArrayList<CustomerRefund> getLatestCustomerRefunds()
			throws DAOException {

		return null;
	}

	@Override
	public ArrayList<Customer> getLatestCustomers() throws DAOException {

		return null;
	}

	@Override
	public ArrayList<TransferFund> getLatestDeposits() throws DAOException {

		return null;
	}

	@Override
	public ArrayList<InvoicesList> getLatestInvoices() throws DAOException {

		return null;
	}

	@Override
	public ArrayList<Item> getLatestItems() throws DAOException {

		return null;
	}

	@Override
	public ArrayList<PaymentsList> getLatestPayments() throws DAOException {

		return null;
	}

	@Override
	public ArrayList<Item> getLatestPurchaseItems() throws DAOException {

		return null;
	}

	@Override
	public ArrayList<Estimate> getLatestQuotes() throws DAOException {

		return null;
	}

	@Override
	public ArrayList<ReceivePayment> getLatestReceivePayments()
			throws DAOException {

		return null;
	}

	@Override
	public ArrayList<Item> getLatestSalesItems() throws DAOException {

		return null;
	}

	@Override
	public ArrayList<PaymentsList> getLatestVendorPayments()
			throws DAOException {

		return null;
	}

	@Override
	public ArrayList<Vendor> getLatestVendors() throws DAOException {

		return null;
	}

	@Override
	public Long getNextCheckNumber(String account) throws DAOException {

		return null;
	}

	@Override
	public Long getNextNominalCode(int accountType) throws DAOException {

		return null;
	}

	@Override
	public Long getNextTransactionNumber(int transactionType)
			throws DAOException {

		return null;
	}

	@Override
	public Long getNextVoucherNumber() throws DAOException {

		return null;
	}

	@Override
	public ArrayList<PurchaseOrdersList> getNotReceivedPurchaseOrdersList(
			String vendorId) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<OverDueInvoicesList> getOverDueInvoices()
			throws DAOException {

		return null;
	}

	@Override
	public ArrayList<PaymentsList> getPaymentsList() throws DAOException {

		return null;
	}

	@Override
	public ArrayList<Item> getPurchaseItems() throws DAOException {

		return null;
	}

	@Override
	public ArrayList<PurchaseOrdersList> getPurchaseOrdersList(String vendorId)
			throws DAOException {

		return null;
	}

	@Override
	public ArrayList<PurchaseOrdersAndItemReceiptsList> getPurchasesAndItemReceiptsList(
			String vendorId) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<ReceivePaymentsList> getReceivePaymentsList()
			throws DAOException {

		return null;
	}

	@Override
	public FixedAssetSellOrDisposeReviewJournal getReviewJournal(
			TempFixedAsset fixedAsset) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<Item> getSalesItems() throws DAOException {

		return null;
	}

	@Override
	public ArrayList<SellingOrDisposingFixedAssetList> getSellingOrDisposingFixedAssets()
			throws DAOException {

		return null;
	}

	@Override
	public ArrayList<Account> getTaxAgencyAccounts() throws DAOException {

		return null;
	}

	@Override
	public ArrayList<MakeDepositTransactionsList> getTransactionMakeDeposits()
			throws DAOException {

		return null;
	}

	@Override
	public ArrayList<PayBillTransactionList> getTransactionPayBills()
			throws DAOException {

		return null;
	}

	@Override
	public ArrayList<PayBillTransactionList> getTransactionPayBills(
			String vendorId) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<TAXReturnEntry> getTransactionPaySalesTaxEntriesList(
			Date transactionDate) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<ReceivePaymentTransactionList> getTransactionReceivePayments(
			String customerId) throws DAOException {

		return null;
	}

	@Override
	public TAXReturn getVATReturn(TAXAgency vatAgency, Date fromDate,
			Date toDate) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<CreditsAndPayments> getVendorCreditsAndPayments(
			String vendor) throws DAOException {

		return null;
	}

	@Override
	public ArrayList<PaymentsList> getVendorPaymentsList() throws DAOException {

		return null;
	}

	@Override
	public boolean isSalesTaxPayableAccount(String accountId)
			throws DAOException {

		return false;
	}

	@Override
	public boolean isSalesTaxPayableAccountByName(String accountName)
			throws DAOException {

		return false;
	}

	@Override
	public boolean isTaxAgencyAccount(String account) throws DAOException {

		return false;
	}

	@Override
	public void rollBackDepreciation(Date rollBackDepreciationTo)
			throws DAOException {

	}

	@Override
	public void runDepreciation(Date depreciationFrom, Date depreciationTo,
			FixedAssetLinkedAccountMap linkedAccounts) throws DAOException {

	}

	@Override
	public void test() throws Exception {

	}

}
