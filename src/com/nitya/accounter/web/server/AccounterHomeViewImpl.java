/**
 *
 */
package com.nitya.accounter.web.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.AccounterThreadLocal;
import com.nitya.accounter.core.CashPurchase;
import com.nitya.accounter.core.CashSales;
import com.nitya.accounter.core.ChequePdfGenerator;
import com.nitya.accounter.core.Client;
import com.nitya.accounter.core.ClientConvertUtil;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.CreditCardCharge;
import com.nitya.accounter.core.CreditsAndPayments;
import com.nitya.accounter.core.CustomerRefund;
import com.nitya.accounter.core.EU;
import com.nitya.accounter.core.EnterBill;
import com.nitya.accounter.core.Estimate;
import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.core.FixedAsset;
import com.nitya.accounter.core.Item;
import com.nitya.accounter.core.Job;
import com.nitya.accounter.core.JournalEntry;
import com.nitya.accounter.core.ReceivePayment;
import com.nitya.accounter.core.ReceiveVATEntries;
import com.nitya.accounter.core.ServerConvertUtil;
import com.nitya.accounter.core.TAXAgency;
import com.nitya.accounter.core.TransferFund;
import com.nitya.accounter.core.WriteCheck;
import com.nitya.accounter.mail.UsersMailSendar;
import com.nitya.accounter.migration.AccounterMigrator;
import com.nitya.accounter.services.DAOException;
import com.nitya.accounter.servlets.BaseServlet;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.IAccounterHomeViewService;
import com.nitya.accounter.web.client.core.*;
import com.nitya.accounter.web.client.core.Lists.BillsList;
import com.nitya.accounter.web.client.core.Lists.ClientTDSInfo;
import com.nitya.accounter.web.client.core.Lists.CustomerRefundsList;
import com.nitya.accounter.web.client.core.Lists.DepositsTransfersList;
import com.nitya.accounter.web.client.core.Lists.DepreciableFixedAssetsList;
import com.nitya.accounter.web.client.core.Lists.EstimatesAndSalesOrdersList;
import com.nitya.accounter.web.client.core.Lists.FixedAssetLinkedAccountMap;
import com.nitya.accounter.web.client.core.Lists.FixedAssetSellOrDisposeReviewJournal;
import com.nitya.accounter.web.client.core.Lists.InvoicesList;
import com.nitya.accounter.web.client.core.Lists.IssuePaymentTransactionsList;
import com.nitya.accounter.web.client.core.Lists.OverDueInvoicesList;
import com.nitya.accounter.web.client.core.Lists.PayBillTransactionList;
import com.nitya.accounter.web.client.core.Lists.PayeeList;
import com.nitya.accounter.web.client.core.Lists.PaymentsList;
import com.nitya.accounter.web.client.core.Lists.PurchaseOrdersAndItemReceiptsList;
import com.nitya.accounter.web.client.core.Lists.PurchaseOrdersList;
import com.nitya.accounter.web.client.core.Lists.ReceivePaymentTransactionList;
import com.nitya.accounter.web.client.core.Lists.ReceivePaymentsList;
import com.nitya.accounter.web.client.core.Lists.TempFixedAsset;
import com.nitya.accounter.web.client.core.Lists.TransactionsList;
import com.nitya.accounter.web.client.core.reports.TransactionHistory;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.ExpensePortletData;
import com.nitya.accounter.web.client.ui.PayeesBySalesPortletData;
import com.nitya.accounter.web.client.ui.Portlet;
import com.nitya.accounter.web.client.ui.YearOverYearPortletData;
import com.nitya.accounter.web.client.ui.settings.StockAdjustmentList;
import com.nitya.accounter.web.server.managers.CompanyManager;

import org.apache.xerces.impl.dv.util.Base64;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @author Fernandez
 *
 */
public class AccounterHomeViewImpl extends AccounterRPCBaseServiceImpl
		implements IAccounterHomeViewService {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public AccounterHomeViewImpl() {
		super();

	}

	@Override
	public ArrayList<ClientEnterBill> getBillsOwed() {

		List<ClientEnterBill> clientEnterBills = new ArrayList<ClientEnterBill>();

		List<EnterBill> serverEnterBills = null;
		try {

			// serverEnterBills =
			// getFinanceTool().getBillsOwed();
			// for (EnterBill enterBill : serverEnterBills) {
			// clientEnterBills.add((ClientEnterBill) new
			// ClientConvertUtil().toClientObject(
			// serverEnterBills, ClientEnterBill.class));
			// }
			// bill = (List<ClientEnterBill>) manager.merge(bill);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientEnterBill>(clientEnterBills);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.nitya.accounter.web.client.IAccounterHomeViewService#getLatestQuotes
	 * (com.nitya.accounter.web.client.core.Company)
	 */

	@Override
	public ArrayList<ClientEstimate> getLatestQuotes() {
		List<ClientEstimate> clientEstimates = new ArrayList<ClientEstimate>();
		List<Estimate> serverEstimates = null;
		try {

			serverEstimates = getFinanceTool().getCustomerManager()
					.getLatestQuotes(getCompanyId());
			for (Estimate estimate : serverEstimates) {
				clientEstimates.add(new ClientConvertUtil().toClientObject(
						estimate, ClientEstimate.class));
			}
			// estimate = (List<ClientEstimate>) manager.merge(estimate);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientEstimate>(clientEstimates);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.nitya.accounter.web.client.IAccounterHomeViewService#getOverDueInvoices
	 * (com.nitya.accounter.web.client.core.Company)
	 */

	@Override
	public ArrayList<OverDueInvoicesList> getOverDueInvoices() {
		List<OverDueInvoicesList> invoice = null;

		try {

			invoice = getFinanceTool().getDashboardManager()
					.getOverDueInvoices(getCompanyId());

			// invoice = (List<OverDueInvoicesList>) manager.merge(invoice);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<OverDueInvoicesList>(invoice);
	}

	@Override
	public PaginationList<PaymentsList> getPaymentsList(long fromDate,
			long toDate, int start, int length, int viewType) {
		PaginationList<PaymentsList> paymentsList = null;
		try {
			FinanceDate[] dates = getMinimumAndMaximumDates(
					new ClientFinanceDate(fromDate), new ClientFinanceDate(
							toDate), getCompanyId());
			paymentsList = getFinanceTool().getCustomerManager()
					.getPaymentsList(getCompanyId(), dates[0].getDate(),
							dates[1].getDate(), start, length, viewType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return paymentsList;
	}

	@Override
	public ArrayList<PayBillTransactionList> getTransactionPayBills() {
		List<PayBillTransactionList> paybillTrList = null;
		try {
			paybillTrList = getFinanceTool().getVendorManager()
					.getTransactionPayBills(getCompanyId());
			// paybillTrList = (List<PayBillTransactionList>)
			// manager.merge(paybillTrList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<PayBillTransactionList>(paybillTrList);
	}

	// public ArrayList<ClientFileTAXEntry> getPaySalesTaxEntries(
	// long transactionDate) {
	// List<ClientFileTAXEntry> clientPaySlesTaxEntries = new
	// ArrayList<ClientFileTAXEntry>();
	// try {
	// List<FileTAXEntry> paySalesTaxEntriesList = getFinanceTool()
	// .getTaxManager().getTransactionPaySalesTaxEntriesList(
	// transactionDate, getCompanyId());
	// for (FileTAXEntry salesTaxEntry : paySalesTaxEntriesList) {
	// ClientFileTAXEntry paySalesTxEntry = new ClientFileTAXEntry();
	// paySalesTxEntry.setID(salesTaxEntry.getID());
	// paySalesTxEntry.setAmount(salesTaxEntry.getAmount());
	// paySalesTxEntry.setBalance(salesTaxEntry.getBalance());
	// // paySalesTxEntry.setStatus(salesTaxEntry.getTransaction()
	// // .getStatus());
	// paySalesTxEntry.setTaxAgency(salesTaxEntry.getTaxAgency()
	// .getID());
	// if (salesTaxEntry.getTaxRateCalculation() != null)
	// paySalesTxEntry.setTaxRateCalculation(salesTaxEntry
	// .getTaxRateCalculation().getID());
	// if (salesTaxEntry.getTaxItem() != null)
	// paySalesTxEntry.setTaxItem(salesTaxEntry.getTaxItem()
	// .getID());
	// if (salesTaxEntry.getTaxAdjustment() != null)
	// paySalesTxEntry.setTaxAdjustment(salesTaxEntry
	// .getTaxAdjustment().getID());
	//
	// // paySalesTxEntry.setTransaction(salesTaxEntry.getTransaction()
	// // .getID());
	// paySalesTxEntry.setTransactionDate(salesTaxEntry
	// .getTransactionDate().getDate());
	// clientPaySlesTaxEntries.add(paySalesTxEntry);
	// // paySalesTxEntry
	// // .setVoid(salesTaxEntry.getTransaction().isVoid());
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// return new ArrayList<ClientFileTAXEntry>(clientPaySlesTaxEntries);
	//
	// }

	@Override
	public ArrayList<PayBillTransactionList> getTransactionPayBills(
			long vendorId, ClientFinanceDate paymentDate) {

		List<PayBillTransactionList> paybillTrList = null;

		try {

			paybillTrList = getFinanceTool().getVendorManager()
					.getTransactionPayBills(vendorId, getCompanyId(),
							new FinanceDate(paymentDate));

			// paybillTrList = (List<PayBillTransactionList>) manager
			// .merge(paybillTrList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<PayBillTransactionList>(paybillTrList);
	}

	@Override
	public ArrayList<ClientCreditsAndPayments> getVendorCreditsAndPayments(
			long vendorId, long transactionId) {
		List<ClientCreditsAndPayments> clientCreditsAndPaymentsList = new ArrayList<ClientCreditsAndPayments>();
		List<CreditsAndPayments> serverCreditsAndPayments = null;
		try {

			serverCreditsAndPayments = getFinanceTool().getVendorManager()
					.getCreditsAndPayments(vendorId, transactionId,
							getCompanyId());
			for (CreditsAndPayments creditsAndPayments : serverCreditsAndPayments) {
				ClientCreditsAndPayments clientObject = new ClientConvertUtil()
						.toClientObject(creditsAndPayments,
								ClientCreditsAndPayments.class);
				clientObject.setTransactionDate(creditsAndPayments
						.getTransaction().getDate().toClientFinanceDate());
				clientCreditsAndPaymentsList.add(clientObject);
			}
			// creditsAndPaymentsList = (List<ClientCreditsAndPayments>) manager
			// .merge(creditsAndPaymentsList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientCreditsAndPayments>(
				clientCreditsAndPaymentsList);

	}

	@Override
	public PaginationList<PaymentsList> getVendorPaymentsList(long fromDate,
			long toDate, int start, int length, int viewType) {

		PaginationList<PaymentsList> vendorPaymentsList = new PaginationList<PaymentsList>();

		try {
			FinanceDate[] dates = getMinimumAndMaximumDates(
					new ClientFinanceDate(fromDate), new ClientFinanceDate(
							toDate), getCompanyId());
			vendorPaymentsList = getFinanceTool().getVendorManager()
					.getVendorPaymentsList(getCompanyId(), dates[0].getDate(),
							dates[1].getDate(), start, length, viewType);

			// vendorPaymentsList = (List<PaymentsList>) manager
			// .merge(vendorPaymentsList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return vendorPaymentsList;

	}

	@Override
	public ArrayList<IssuePaymentTransactionsList> getChecks() {

		List<IssuePaymentTransactionsList> checks = null;

		try {

			checks = getFinanceTool().getVendorManager().getChecks(
					getCompanyId());

			// checks = (List<IssuePaymentTransactionsList>)
			// manager.merge(checks);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<IssuePaymentTransactionsList>(checks);

	}

	@Override
	public ArrayList<IssuePaymentTransactionsList> getChecks(long accountId) {

		List<IssuePaymentTransactionsList> checks = new ArrayList<IssuePaymentTransactionsList>();

		try {

			checks = getFinanceTool().getVendorManager().getChecks(accountId,
					getCompanyId());

			// checks = (List<IssuePaymentTransactionsList>)
			// manager.merge(checks);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<IssuePaymentTransactionsList>(checks);

	}

	@Override
	public ArrayList<ClientCreditCardCharge> getCreditCardChargesThisMonth(
			long date) {
		List<ClientCreditCardCharge> clientCreditCardCharges = new ArrayList<ClientCreditCardCharge>();
		List<CreditCardCharge> serverCreditCardCharges = null;
		try {

			serverCreditCardCharges = getFinanceTool().getDashboardManager()
					.getCreditCardChargesThisMonth(date, getCompanyId());
			for (CreditCardCharge creditCardCharge : serverCreditCardCharges) {
				clientCreditCardCharges.add(new ClientConvertUtil()
						.toClientObject(creditCardCharge,
								ClientCreditCardCharge.class));
			}
			// creditCardCharges = (List<ClientCreditCardCharge>) manager
			// .merge(creditCardCharges);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientCreditCardCharge>(clientCreditCardCharges);
	}

	@Override
	public ArrayList<BillsList> getLatestBills() {
		List<BillsList> bills = null;

		try {

			bills = getFinanceTool().getVendorManager().getLatestBills(
					getCompanyId());

			// bills = (List<BillsList>) manager.merge(bills);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<BillsList>(bills);
	}

	@Override
	public ArrayList<ClientCashPurchase> getLatestCashPurchases() {
		List<ClientCashPurchase> clientCashPurchase = new ArrayList<ClientCashPurchase>();
		List<CashPurchase> serverCashPurchases = null;
		try {

			serverCashPurchases = getFinanceTool().getPurchageManager()
					.getLatestCashPurchases(getCompanyId());
			for (CashPurchase cashPurchase : serverCashPurchases) {
				clientCashPurchase.add(new ClientConvertUtil().toClientObject(
						cashPurchase, ClientCashPurchase.class));
			}
			// cashPurchase = (List<ClientCashPurchase>)
			// manager.merge(cashPurchase);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientCashPurchase>(clientCashPurchase);
	}

	@Override
	public PaginationList<ClientBudget> getBudgetList() {

		PaginationList<ClientBudget> budgetList = null;
		try {

			budgetList = getFinanceTool().getBudgetList(getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return budgetList;
	}

	@Override
	public ArrayList<ClientCashSales> getLatestCashSales() {
		List<ClientCashSales> clientCashSales = new ArrayList<ClientCashSales>();
		List<CashSales> serverCashSales = null;
		try {

			serverCashSales = getFinanceTool().getSalesManager()
					.getLatestCashSales(getCompanyId());
			for (CashSales cashSales : serverCashSales) {
				clientCashSales.add(new ClientConvertUtil().toClientObject(
						cashSales, ClientCashSales.class));
			}
			// cashSales = (List<ClientCashSales>) manager.merge(cashSales);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientCashSales>(clientCashSales);
	}

	@Override
	public ArrayList<ClientWriteCheck> getLatestChecks() {
		List<ClientWriteCheck> clientWriteChecks = new ArrayList<ClientWriteCheck>();
		List<WriteCheck> serverWriteChecks = null;
		try {

			serverWriteChecks = getFinanceTool()
					.getLatestChecks(getCompanyId());
			for (WriteCheck writeCheck : serverWriteChecks) {
				clientWriteChecks.add(new ClientConvertUtil().toClientObject(
						writeCheck, ClientWriteCheck.class));
			}
			// checks = (List<ClientWriteCheck>) manager.merge(checks);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientWriteCheck>(clientWriteChecks);
	}

	@Override
	public ArrayList<ClientCustomerRefund> getLatestCustomerRefunds() {
		List<ClientCustomerRefund> clientCustomerRefunds = new ArrayList<ClientCustomerRefund>();
		List<CustomerRefund> serverCustomerRefunds = null;
		try {

			serverCustomerRefunds = getFinanceTool().getCustomerManager()
					.getLatestCustomerRefunds(getCompanyId());
			for (CustomerRefund customerRefund : serverCustomerRefunds) {
				clientCustomerRefunds.add(new ClientConvertUtil()
						.toClientObject(customerRefund,
								ClientCustomerRefund.class));
			}
			// customerRefunds = (List<ClientCustomerRefund>) manager
			// .merge(customerRefunds);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientCustomerRefund>(clientCustomerRefunds);
	}

	@Override
	public ArrayList<ClientTransferFund> getLatestDeposits() {
		List<ClientTransferFund> clientMakeDeposits = new ArrayList<ClientTransferFund>();
		List<TransferFund> serverMakeDeposits = null;
		try {

			serverMakeDeposits = getFinanceTool().getLatestDeposits(
					getCompanyId());
			for (TransferFund makeDeposit : serverMakeDeposits) {
				clientMakeDeposits.add(new ClientConvertUtil().toClientObject(
						makeDeposit, ClientTransferFund.class));
			}
			// deposites = (List<ClientMakeDeposit>) manager.merge(deposites);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientTransferFund>(clientMakeDeposits);
	}

	@Override
	public ArrayList<ClientItem> getLatestItems() {
		List<ClientItem> clientItems = new ArrayList<ClientItem>();
		List<Item> serverItems = null;
		try {

			serverItems = getFinanceTool().getLatestItems(getCompanyId());
			for (Item item : serverItems) {
				clientItems.add(new ClientConvertUtil().toClientObject(item,
						ClientItem.class));
			}
			// items = (List<ClientItem>) manager.merge(items);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientItem>(clientItems);
	}

	@Override
	public ArrayList<ClientItem> getTotalWithNamesItems() {
		List<ClientItem> clientItems = new ArrayList<ClientItem>();
		List<Item> serverItems = null;
		try {

			serverItems = getFinanceTool().getTotalWithNamesItems(getCompanyId());
			for (Item item : serverItems) {
				clientItems.add(new ClientConvertUtil().toClientObject(item,
						ClientItem.class));
			}
			// items = (List<ClientItem>) manager.merge(items);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientItem>(clientItems);
	}

	@Override
	public ArrayList<PaymentsList> getLatestPayments() {
		List<PaymentsList> payments = null;

		try {

			payments = getFinanceTool().getCustomerManager().getLatestPayments(
					getCompanyId());

			// payments = (List<PaymentsList>) manager.merge(payments);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<PaymentsList>(payments);
	}

	@Override
	public String getNextTransactionNumber(int transactionType) {
		String nextTransactionNumber = "";
		try {

			nextTransactionNumber = getFinanceTool().getNextTransactionNumber(
					transactionType, getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return nextTransactionNumber;

	}

	@Override
	public boolean canVoidOrEdit(long invoiceOrVendorBillId) {
		boolean isCanVoidOrEdit = false;
		try {

			isCanVoidOrEdit = getFinanceTool().canVoidOrEdit(
					invoiceOrVendorBillId, getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isCanVoidOrEdit;
	}

	@Override
	public ArrayList<ClientCreditsAndPayments> getCustomerCreditsAndPayments(
			long customerId, long transactionId) {
		List<ClientCreditsAndPayments> clientCreditsAndPayments = new ArrayList<ClientCreditsAndPayments>();
		List<CreditsAndPayments> serverCreditsAndPayments = null;
		try {

			serverCreditsAndPayments = getFinanceTool().getCustomerManager()
					.getCreditsAndPayments(customerId, transactionId,
							getCompanyId());
			for (CreditsAndPayments creditsAndPayments : serverCreditsAndPayments) {
				ClientCreditsAndPayments clientObject = new ClientConvertUtil()
						.toClientObject(creditsAndPayments,
								ClientCreditsAndPayments.class);
				clientObject.setTransactionDate(creditsAndPayments
						.getTransaction().getDate().toClientFinanceDate());
				clientCreditsAndPayments.add(clientObject);
			}
			// creditsAndPayments = (List<ClientCreditsAndPayments>) manager
			// .merge(creditsAndPayments);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientCreditsAndPayments>(clientCreditsAndPayments);
	}

	@Override
	public PaginationList<CustomerRefundsList> getCustomerRefundsList(
			long fromDate, long toDate, int viewId) {
		PaginationList<CustomerRefundsList> customerRefundsList = null;
		try {
			FinanceDate[] dates = getMinimumAndMaximumDates(
					new ClientFinanceDate(fromDate), new ClientFinanceDate(
							toDate), getCompanyId());
			customerRefundsList = getFinanceTool().getCustomerManager()
					.getCustomerRefundsList(getCompanyId(), dates[0], dates[1],
							viewId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return customerRefundsList;
	}

	@Override
	public PaginationList<ClientEstimate> getEstimates(int estimateType,
			int viewType, long fromDate, long toDate, int start, int length) {
		PaginationList<ClientEstimate> clientEstimate = new PaginationList<ClientEstimate>();
		List<Estimate> serverEstimates = null;
		try {
			FinanceDate[] dates = getMinimumAndMaximumDates(
					new ClientFinanceDate(fromDate), new ClientFinanceDate(
							toDate), getCompanyId());
			serverEstimates = getFinanceTool().getCustomerManager()
					.getEstimates(getCompanyId(), estimateType, viewType,
							dates[0], dates[1], start, length);
			clientEstimate.setStart(start);
			clientEstimate.setTotalCount(serverEstimates.size());
			for (Estimate estimate : serverEstimates) {
				clientEstimate.add(new ClientConvertUtil().toClientObject(
						estimate, ClientEstimate.class));
			}
			// estimate = (List<ClientEstimate>) manager.merge(estimate);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientEstimate;
	}

	@Override
	public ArrayList<ClientEstimate> getEstimates(long customerId) {
		List<ClientEstimate> clientEstimate = new ArrayList<ClientEstimate>();
		List<Estimate> serverEstimates = null;
		try {
			serverEstimates = getFinanceTool().getCustomerManager()
					.getEstimates(customerId, getCompanyId());
			for (Estimate estimate : serverEstimates) {
				clientEstimate.add(new ClientConvertUtil().toClientObject(
						estimate, ClientEstimate.class));
			}
			// estimate = (List<ClientEstimate>) manager.merge(estimate);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientEstimate>(clientEstimate);
	}

	@Override
	public PaginationList<InvoicesList> getInvoiceList(long fromDate,
			long toDate, int type, int viewType, int start, int length) {
		PaginationList<InvoicesList> invoicesList = null;
		try {
			FinanceDate[] dates = getMinimumAndMaximumDates(
					new ClientFinanceDate(fromDate), new ClientFinanceDate(
							toDate), getCompanyId());
			invoicesList = getFinanceTool().getInventoryManager()
					.getInvoiceList(getCompanyId(), dates[0].getDate(),
							dates[1].getDate(), type, viewType, start, length);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return invoicesList;
	}

	public ArrayList<ClientFinanceDate> getMinimumAndMaximumTransactionDate(
			long companyId) {
		List<ClientFinanceDate> transactionDates = new ArrayList<ClientFinanceDate>();
		try {

			ClientFinanceDate[] dates = getFinanceTool().getManager()
					.getMinimumAndMaximumTransactionDate(getCompanyId());
			transactionDates.add(dates[0]);
			transactionDates.add(dates[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<ClientFinanceDate>(transactionDates);
	}

	private FinanceDate[] getMinimumAndMaximumDates(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {

		// if ((startDate.equals("") || startDate == null)
		// || (endDate.equals("") || endDate == null)) {

		List<ClientFinanceDate> dates = getMinimumAndMaximumTransactionDate(companyId);
		ClientFinanceDate startDate1 = dates.get(0) == null ? new ClientFinanceDate()
				: dates.get(0);
		ClientFinanceDate endDate2 = dates.get(1) == null ? new ClientFinanceDate()
				: dates.get(1);

		FinanceDate transtartDate;
		if (startDate == null || startDate.isEmpty())
			transtartDate = new FinanceDate(startDate1);
		else
			transtartDate = new FinanceDate(startDate.getDate());
		FinanceDate tranendDate;
		if (endDate == null || endDate.isEmpty())
			tranendDate = new FinanceDate(endDate2);
		else
			tranendDate = new FinanceDate(endDate.getDate());

		return new FinanceDate[] { transtartDate, tranendDate };
	}

	@Override
	public PaginationList<ClientJournalEntry> getJournalEntries(long fromDate,
			long toDate, int start, int length) {
		PaginationList<ClientJournalEntry> clientJournalEntries = new PaginationList<ClientJournalEntry>();
		PaginationList<JournalEntry> serverJournalEntries = null;
		try {
			FinanceDate[] dates = getMinimumAndMaximumDates(
					new ClientFinanceDate(fromDate), new ClientFinanceDate(
							toDate), getCompanyId());
			serverJournalEntries = getFinanceTool().getJournalEntries(
					getCompanyId(), dates[0], dates[1], start, length);
			clientJournalEntries.setTotalCount(serverJournalEntries
					.getTotalCount());
			clientJournalEntries.setStart(serverJournalEntries.getStart());
			for (JournalEntry journalEntry : serverJournalEntries) {
				clientJournalEntries
						.add(new ClientConvertUtil().toClientObject(
								journalEntry, ClientJournalEntry.class));
			}
			// journalEntry = (List<ClientJournalEntry>)
			// manager.merge(journalEntry);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientJournalEntries;
	}

	@Override
	public ClientJournalEntry getJournalEntry(long journalEntryId) {
		ClientJournalEntry clientJournalEntry = null;
		JournalEntry serverJournalEntry = null;
		try {

			serverJournalEntry = getFinanceTool().getJournalEntry(
					journalEntryId, getCompanyId());
			clientJournalEntry = new ClientConvertUtil().toClientObject(
					serverJournalEntry, ClientJournalEntry.class);
			// journalEntry = (ClientJournalEntry) manager.merge(journalEntry);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientJournalEntry;
	}

	@Override
	public Long getNextCheckNumber(long accountId) {
		Long nextCheckNumber = 0l;
		try {

			nextCheckNumber = getFinanceTool().getNextCheckNumber(accountId);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return nextCheckNumber;
	}

	@Override
	public String getNextIssuepaymentCheckNumber(long accountId)
			throws AccounterException {
		return getFinanceTool().getNextIssuePaymentCheckNumber(accountId,
				getCompanyId());
	}

	@Override
	public ArrayList<ClientItem> getPurchaseItems() {
		List<ClientItem> clientItems = new ArrayList<ClientItem>();
		List<Item> serverItems = null;
		try {

			serverItems = getFinanceTool().getPurchageManager()
					.getPurchaseItems(getCompanyId());
			for (Item item : serverItems) {
				clientItems.add(new ClientConvertUtil().toClientObject(item,
						ClientItem.class));
			}
			// item = (List<ClientItem>) manager.merge(item);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientItem>(clientItems);
	}

	@Override
	public ArrayList<ClientItem> getSalesItems() {
		List<ClientItem> clientItems = new ArrayList<ClientItem>();
		List<Item> serverItems = null;

		try {

			serverItems = getFinanceTool().getSalesManager().getSalesItems(
					getCompanyId());
			for (Item item : serverItems) {
				clientItems.add(new ClientConvertUtil().toClientObject(item,
						ClientItem.class));
			}
			// item = (List<ClientItem>) manager.merge(item);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientItem>(clientItems);
	}

	@Override
	public ArrayList<ClientAccount> getTaxAgencyAccounts() {
		List<ClientAccount> clientAccount = new ArrayList<ClientAccount>();
		List<Account> serverAccounts = null;
		try {

			serverAccounts = getFinanceTool().getTaxManager()
					.getTaxAgencyAccounts(getCompanyId());
			for (Account account : serverAccounts) {
				clientAccount.add(new ClientConvertUtil().toClientObject(
						account, ClientAccount.class));
			}
			// account = (List<ClientAccount>) manager.merge(account);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientAccount>(clientAccount);
	}

	@Override
	public ArrayList<ReceivePaymentTransactionList> getTransactionReceivePayments(
			long customerId, long paymentDate) throws AccounterException {
		List<ReceivePaymentTransactionList> receivePaymentTransactionList = null;

		receivePaymentTransactionList = getFinanceTool()
				.getTransactionReceivePayments(customerId, paymentDate,
						getCompanyId());

		// receivePaymentTransactionList =
		// (List<ReceivePaymentTransactionList>) manager
		// .merge(receivePaymentTransactionList);

		return new ArrayList<ReceivePaymentTransactionList>(
				receivePaymentTransactionList);
	}

	@Override
	public boolean isSalesTaxPayableAccount(long accountId) {
		boolean isSalesTaxPayable = false;
		try {
			FinanceTool tool = getFinanceTool();
			isSalesTaxPayable = tool.getSalesManager()
					.isSalesTaxPayableAccount(accountId, getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isSalesTaxPayable;
	}

	@Override
	public boolean isSalesTaxPayableAccountByName(String accountName) {
		boolean isSalesTaxPayable = false;
		try {

			isSalesTaxPayable = getFinanceTool()
					.getSalesManager()
					.isSalesTaxPayableAccountByName(accountName, getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isSalesTaxPayable;
	}

	@Override
	public boolean isTaxAgencyAccount(long accountId) {
		boolean isTaxAgency = false;
		try {

			isTaxAgency = getFinanceTool().getTaxManager().isTaxAgencyAccount(
					accountId);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isTaxAgency;
	}

	@Override
	public PaginationList<ReceivePaymentsList> getReceivePaymentsList(
			long fromDate, long toDate, int transactionType, int start,
			int length, int viewType) {

		try {
			FinanceDate[] dates = getMinimumAndMaximumDates(
					new ClientFinanceDate(fromDate), new ClientFinanceDate(
							toDate), getCompanyId());
			return getFinanceTool().getCustomerManager()
					.getReceivePaymentsList(getCompanyId(), dates[0].getDate(),
							dates[1].getDate(), transactionType, start, length,
							viewType);

			// receivePaymentList = (List<ReceivePaymentsList>) manager
			// .merge(receivePaymentList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new PaginationList<ReceivePaymentsList>();
	}

	@Override
	public ArrayList<ClientItem> getLatestPurchaseItems() {
		ArrayList<ClientItem> clientItems = new ArrayList<ClientItem>();
		List<Item> serverItems = null;
		try {

			serverItems = getFinanceTool().getPurchageManager()
					.getLatestPurchaseItems(getCompanyId());
			for (Item item : serverItems) {
				clientItems.add(new ClientConvertUtil().toClientObject(item,
						ClientItem.class));
			}
			// item = (List<ClientItem>) manager.merge(item);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientItems;
	}

	@Override
	public ArrayList<PaymentsList> getLatestVendorPayments() {
		ArrayList<PaymentsList> paymentsList = null;

		try {

			paymentsList = getFinanceTool().getVendorManager()
					.getLatestVendorPayments(getCompanyId());

			// paymentsList = (List<PaymentsList>) manager.merge(paymentsList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return paymentsList;
	}

	@Override
	public ArrayList<ClientReceivePayment> getLatestReceivePayments() {
		ArrayList<ClientReceivePayment> clientReceivePaymentList = new ArrayList<ClientReceivePayment>();
		ArrayList<ReceivePayment> serverReceivePaymentList = null;
		try {

			serverReceivePaymentList = getFinanceTool().getCustomerManager()
					.getLatestReceivePayments(getCompanyId());
			for (ReceivePayment receivePayment : serverReceivePaymentList) {
				clientReceivePaymentList.add(new ClientConvertUtil()
						.toClientObject(receivePayment,
								ClientReceivePayment.class));
			}
			// receivePaymentList = (List<ClientReceivePayment>) manager
			// .merge(receivePaymentList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientReceivePaymentList;
	}

	/*
	 * @Override public List<SalesOrdersList> getPurchaseOrdersForVendor(long
	 * vendorID) throws AccounterException {
	 *
	 * FinanceTool tool = getFinanceTool();
	 *
	 * return tool != null ? tool.getPurchaseOrdersForVendor(vendorID) : null; }
	 */
	// @Override
	// public PaginationList<SalesOrdersList> getSalesOrders(long fromDate,
	// long toDate) throws AccounterException {
	//
	// FinanceTool tool = getFinanceTool();
	//
	// try {
	// FinanceDate[] dates = getMinimumAndMaximumDates(
	// new ClientFinanceDate(fromDate), new ClientFinanceDate(
	// toDate), getCompanyId());
	// return tool != null ? tool.getSalesManager().getSalesOrdersList(
	// getCompanyId(), dates[0].getDate(), dates[1].getDate())
	// : null;
	// } catch (DAOException e) {
	// e.printStackTrace();
	// }
	// return null;
	// }

	/*
	 * @Override public List<SalesOrdersList> getSalesOrdersForCustomer(long
	 * customerID) throws AccounterException {
	 *
	 * FinanceTool tool = getFinanceTool();
	 *
	 * return tool != null ? tool.getSalesOrdersForCustomer(customerID) : null;
	 *
	 * }
	 */

	@Override
	public ArrayList<PurchaseOrdersList> getNotReceivedPurchaseOrdersList(
			long vendorID) throws AccounterException {

		FinanceTool tool = getFinanceTool();

		try {
			return tool != null ? tool.getPurchageManager()
					.getNotReceivedPurchaseOrdersList(vendorID, getCompanyId())
					: null;
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<PurchaseOrdersAndItemReceiptsList> getPurchasesAndItemReceiptsList(
			long vendorId) throws AccounterException {
		try {
			FinanceTool tool = getFinanceTool();
			return tool != null ? tool.getPurchageManager()
					.getPurchasesAndItemReceiptsList(vendorId, getCompanyId())
					: null;
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public ArrayList<EstimatesAndSalesOrdersList> getEstimatesAndSalesOrdersList(
			long customerId) throws AccounterException {

		try {
			FinanceTool tool = getFinanceTool();
			return tool != null ? tool.getCustomerManager()
					.getEstimatesAndSalesOrdersList(customerId, getCompanyId())
					: null;
		} catch (DAOException e) {

			e.printStackTrace();
		}
		return null;

	}

	// @Override
	// public List<ClientDepreciationFixedAsset> getDepreciationFixedAssets(
	// Date startDate, Date toDate) {
	// try {
	// FinanceTool tool = getFinanceTool();
	// return tool != null ? tool.getDepreciationFixedAssets(startDate,
	// toDate) : null;
	// } catch (DAOException e) {
	//
	// e.printStackTrace();
	// }
	// return null;
	// }

	@Override
	public ClientTAXReturn getVATReturn(ClientTAXAgency taxAgency,
			ClientFinanceDate fromDate, ClientFinanceDate toDate)
			throws AccounterException {

		try {
			FinanceTool tool = getFinanceTool();

			TAXAgency serverVatAgency = new ServerConvertUtil().toServerObject(
					new TAXAgency(), taxAgency, getSession());

			return tool.getTaxManager().getVATReturnDetails(serverVatAgency,
					new FinanceDate(fromDate), new FinanceDate(toDate),
					getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
			throw new AccounterException(e);
		}
	}

	@Override
	public FixedAssetSellOrDisposeReviewJournal getReviewJournal(
			TempFixedAsset fixedAsset) throws AccounterException {
		try {
			return getFinanceTool().getFixedAssetManager().getReviewJournal(
					fixedAsset, getCompanyId());
		} catch (DAOException e) {

			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<ClientFixedAsset> getFixedAssetList(int status)
			throws AccounterException {
		ArrayList<ClientFixedAsset> list = new ArrayList<ClientFixedAsset>();
		ArrayList<FixedAsset> list1 = null;
		try {

			list1 = getFinanceTool().getFixedAssetManager().getFixedAssets(
					status, getCompanyId());
			for (FixedAsset asset : list1) {
				list.add(new ClientConvertUtil().toClientObject(asset,
						ClientFixedAsset.class));
			}
		} catch (DAOException e) {

			e.printStackTrace();
		}
		return list;
	}

	@Override
	public DepreciableFixedAssetsList getDepreciableFixedAssets(
			long depreciationFrom, long depreciationTo)
			throws AccounterException {
		try {
			FinanceTool tool = getFinanceTool();
			return tool != null ? tool.getFixedAssetManager()
					.getDepreciableFixedAssets(depreciationFrom,
							depreciationTo, getCompanyId()) : null;
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<ClientFinanceDate> getAllDepreciationFromDates()
			throws AccounterException {
		try {
			FinanceTool tool = getFinanceTool();
			return tool != null ? tool.getFixedAssetManager()
					.getAllDepreciationFromDates(getCompanyId()) : null;
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ClientFinanceDate getDepreciationLastDate()
			throws AccounterException {
		try {
			FinanceTool tool = getFinanceTool();
			return tool != null ? tool.getFixedAssetManager()
					.getDepreciationLastDate(getCompanyId()) : null;
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<ClientFinanceDate> getFinancialYearStartDates()
			throws AccounterException {
		try {
			FinanceTool tool = getFinanceTool();
			return tool != null ? tool
					.getFinancialYearStartDates(getCompanyId()) : null;
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Boolean rollBackDepreciation(long rollBackDepreciationTo)
			throws AccounterException {
		FinanceTool tool = getFinanceTool();
		return tool.getCompanyManager().rollBackDepreciation(
				rollBackDepreciationTo, getCompanyId());

	}

	@Override
	public void changeDepreciationStartDateTo(long newStartDate)
			throws AccounterException {
		FinanceTool tool = getFinanceTool();
		OperationContext opContext = new OperationContext(getCompanyId(),
				newStartDate);

		tool.getCompanyManager().updateDeprecationStartDate(opContext);
	}

	@Override
	public double getAccumulatedDepreciationAmount(int depreciationMethod,
			double depreciationRate, double purchasePrice,
			long depreciationfromDate, long depreciationtoDate)
			throws AccounterException {
		try {
			FinanceTool tool = getFinanceTool();
			return tool.getFixedAssetManager().getCalculatedDepreciatedAmount(
					depreciationMethod, depreciationRate, purchasePrice,
					depreciationfromDate, depreciationtoDate, getCompanyId());
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return 0.0;
	}

	@Override
	public Long getNextNominalCode(int accountType) {
		Long nextTransactionNumber = 0l;
		try {

			nextTransactionNumber = getFinanceTool().getNextNominalCode(
					accountType, getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return nextTransactionNumber;

	}

	/*
	 * public boolean createTaxes(int[] vatReturnType) { try {
	 * getFinanceTool().createTaxes(vatReturnType); return true; } catch
	 * (Exception e) { e.printStackTrace(); return false; }
	 *
	 * }
	 */

	@Override
	public String getNextFixedAssetNumber() {
		String nextFixedAssestNumber = "";
		try {

			nextFixedAssestNumber = getFinanceTool().getNextFixedAssetNumber(
					getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return nextFixedAssestNumber;
	}

	@Override
	public boolean changeFiscalYearsStartDateTo(long newStartDate) {
		try {
			OperationContext opContext = new OperationContext(getCompanyId(),
					newStartDate);
			getFinanceTool().getCompanyManager().updateCompanyStartDate(
					opContext);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void runDepreciation(long depreciationFrom, long depreciationTo,
			FixedAssetLinkedAccountMap linkedAccounts) {
		try {
			getFinanceTool().getFixedAssetManager().runDepreciation(
					depreciationFrom, depreciationTo, linkedAccounts,
					getCompanyId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ArrayList<ClientTransactionPayTAX> getPayTAXEntries()
			throws AccounterException {
		FinanceTool tool = getFinanceTool();
		if (tool == null)
			return null;
		return (ArrayList<ClientTransactionPayTAX>) tool.getTaxManager()
				.getPayTAXEntries(getCompanyId());
	}

	@Override
	public PaginationList<PayeeList> getPayeeList(int transactionCategory,
			boolean isActive, int strat, int length, boolean isPayeeCenter)
			throws AccounterException {
		FinanceTool tool = getFinanceTool();
		return tool != null ? tool.getPayeeList(transactionCategory, isActive,
				strat, length, getCompanyId(), isPayeeCenter) : null;
	}

	@Override
	public String getCustomerNumber() {
		String nextCustomerNumber = "";
		try {

			nextCustomerNumber = getFinanceTool().getCustomerManager()
					.getNextCustomerNumber(getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return nextCustomerNumber;

	}

	@Override
	public String getVendorNumber() {
		String nextCustomerNumber = "";
		try {

			nextCustomerNumber = getFinanceTool().getVendorManager()
					.getNextVendorNumber(getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return nextCustomerNumber;
	}

	@Override
	public ArrayList<ClientReceiveVATEntries> getReceiveVATEntries()
			throws AccounterException {

		ArrayList<ClientReceiveVATEntries> clientEntries = new ArrayList<ClientReceiveVATEntries>();

		FinanceTool tool = getFinanceTool();
		if (tool == null)
			return clientEntries;
		List<ReceiveVATEntries> entries = tool.getTaxManager()
				.getReceiveVATEntries(getCompanyId());
		for (ReceiveVATEntries entry : entries) {
			ClientReceiveVATEntries clientEntry = new ClientReceiveVATEntries();
			// VATReturn vatReturn =(VATReturn) entry.getTransaction();
			// ClientVATReturn clientVATReturn = new
			// ClientConvertUtil().toClientObject(vatReturn,ClientVATReturn.class);
			clientEntry.setTAXReturn(entry.getTransaction().getID());
			clientEntry.setTAXAgency(entry.getTAXAgency() != null ? entry
					.getTAXAgency().getID() : null);
			clientEntry.setBalance(entry.getBalance());
			clientEntry.setAmount(entry.getAmount());

			clientEntries.add(clientEntry);
		}
		return new ArrayList<ClientReceiveVATEntries>(clientEntries);

	}

	@Override
	public ArrayList<Double> getGraphPointsforAccount(int chartType,
			long accountId) {

		List<Double> resultList = new ArrayList<Double>();
		try {

			resultList = getFinanceTool().getDashboardManager()
					.getGraphPointsforAccount(chartType, accountId,
							getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<Double>(resultList);
	}

	@Override
	public ArrayList<BillsList> getEmployeeExpensesByStatus(String userName,
			int status) {
		List<BillsList> resultList = null;
		try {
			resultList = getFinanceTool().getDashboardManager()
					.getEmployeeExpensesByStatus(userName, status,
							getCompanyId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<BillsList>(resultList);
	}

	@Override
	public boolean changePassWord(String emailID, String oldPassword,
			String newPassword) {
		boolean changePassword = false;
		try {
			changePassword = getFinanceTool().getUserManager()
					.changeMyPassword(emailID, oldPassword, newPassword);

			byte[] d2 = EU.generateD2(newPassword, emailID,
					getThreadLocalRequest().getSession().getId());

			getThreadLocalRequest().getSession().setAttribute(
					BaseServlet.SECRET_KEY_COOKIE, Base64.encode(d2));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return changePassword;
	}

	@Override
	public ArrayList<ClientUserInfo> getAllUsers() throws AccounterException {
		FinanceTool tool = getFinanceTool();
		if (tool != null) {
			return tool.getUserManager().getAllUsers(getCompanyId());
		}
		return null;
	}

	@Override
	public PaginationList<ClientRecurringTransaction> getRecurringsList(
			long fromDate, long toDate) throws AccounterException {
		FinanceTool tool = getFinanceTool();

		if (tool != null) {
			FinanceDate[] dates = getMinimumAndMaximumDates(
					new ClientFinanceDate(fromDate), new ClientFinanceDate(
							toDate), getCompanyId());
			return tool.getAllRecurringTransactions(getCompanyId(), dates[0],
					dates[1]);
		}
		return null;
	}

	@Override
	public void mergeCustomer(ClientCustomer fromClientCustomer,
			ClientCustomer toClientCustomer) throws AccounterException {
		FinanceTool tool = getFinanceTool();
		if (tool != null) {

			try {
				tool.getCustomerManager().mergeCustomer(fromClientCustomer,
						toClientCustomer, getCompanyId());
			} catch (DAOException e) {

				e.printStackTrace();
			}
		}

	}

	@Override
	public void mergeVendor(ClientVendor fromClientVendor,
			ClientVendor toClientVendor) throws AccounterException {
		FinanceTool tool = getFinanceTool();
		if (tool != null) {

			tool.getVendorManager().mergeVendor(fromClientVendor,
					toClientVendor, getCompanyId());
		}

	}

	@Override
	public void mergeItem(ClientItem froClientItem, ClientItem toClientItem)
			throws AccounterException {
		FinanceTool tool = getFinanceTool();
		if (tool != null) {

			tool.mergeItem(froClientItem, toClientItem, getCompanyId());
		}

	}

	@Override
	public ClientAccount mergeAccount(ClientAccount fromClientAccount,
			ClientAccount toClientAccount) throws AccounterException {
		FinanceTool tool = getFinanceTool();
		if (tool != null) {

			return tool.mergeAcoount(fromClientAccount, toClientAccount,
					getCompanyId());
		}
		return null;
	}

	@Override
	public PaginationList<ClientActivity> getUsersActivityLog(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			int startIndex, int length, long value) throws AccounterException {
		FinanceTool tool = getFinanceTool();
		if (tool != null) {
			return tool.getUserManager().getUsersActivityLog(startDate,
					endDate, startIndex, length, getCompanyId(), value);
		}
		return null;

	}

	// public ArrayList<ClientEmployee> getAllEmployees()
	// throws AccounterException {
	// FinanceTool tool = getFinanceTool();
	// if (tool != null) {
	// return tool.getAllEmployees();
	// }
	// return null;
	// }

	// this method is used to send Pdf as an attachment in email
	@Override
	public void sendPdfInMail(String fileName, String subject, String content,
			ClientEmailAccount sender, String recipientEmail, String ccEmail)
			throws AccounterException {
		FinanceTool tool = getFinanceTool();
		if (tool != null) {
			long id = getCompanyId();
			tool.sendPdfInMail(fileName, subject, content, sender,
					recipientEmail, ccEmail, id);

		}

	}

	@Override
	public boolean sendTestMail(ClientEmailAccount sender, String recipient) {
		try {
			UsersMailSendar.sendTestMail(sender, recipient);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * to generate PDF File for Invoice
	 *
	 * @throws AccounterException
	 */

	@Override
	public List<String> createPdfFile(String objectID, int type,
			long brandingThemeId, ClientFinanceDate startDate,
			ClientFinanceDate endDate) throws AccounterException {
		FinanceTool tool;
		try {
			tool = getFinanceTool();
			if (tool != null) {
				long id = getCompanyId();
				return tool.createPdfFile(objectID, type, brandingThemeId, id,
						startDate, endDate);
			}
		} catch (Exception e) {
			throw new AccounterException(e.getMessage());
		}
		return null;
	}

	@Override
	public ArrayList<ClientTDSInfo> getPayBillsByTDS()
			throws AccounterException {
		FinanceTool tool = getFinanceTool();
		if (tool != null) {
			return tool.getVendorManager().getPayBillsByTDS(getCompanyId());
		}
		return null;
	}

	@Override
	public PaginationList<ClientWarehouse> getWarehouses() {
		FinanceTool tool = new FinanceTool();
		return tool.getInventoryManager().getWarehouses(getCompanyId());
	}

	@Override
	public PaginationList<ClientMeasurement> getAllUnits() {
		FinanceTool tool = new FinanceTool();
		return tool.getInventoryManager().getAllUnits(getCompanyId());
	}

	@Override
	public ArrayList<ClientStockTransferItem> getStockTransferItems(
			long wareHouse) {
		FinanceTool tool = new FinanceTool();
		return tool.getInventoryManager().getStockTransferItems(getCompanyId(),
				wareHouse);
	}

	@Override
	public ArrayList<ClientStockTransfer> getWarehouseTransfersList()
			throws AccounterException {
		FinanceTool tool = new FinanceTool();
		return tool.getInventoryManager().getWarehouseTransfersList(
				getCompanyId());
	}

	@Override
	public ArrayList<StockAdjustmentList> getStockAdjustments(long startDate,
			long endDate) throws AccounterException {
		FinanceTool tool = new FinanceTool();
		return tool.getInventoryManager().getStockAdjustments(startDate,
				endDate, getCompanyId());
	}

	@Override
	public ArrayList<ClientItemStatus> getItemStatuses(long wareHouse)
			throws AccounterException {
		FinanceTool tool = new FinanceTool();
		return tool.getInventoryManager().getItemStatuses(wareHouse,
				getCompanyId());
	}

	@Override
	public PaginationList<ClientAccount> getAccounts(int typeOfAccount,
			boolean isActiveAccounts, int start, int length)
			throws AccounterException {
		FinanceTool tool = new FinanceTool();
		if (typeOfAccount == ClientAccount.TYPE_BANK) {
			return tool.getCompanyManager().getBankAccounts(getCompanyId(),
					isActiveAccounts, start, length);
		} else {
			return tool.getCompanyManager().getAccounts(typeOfAccount,
					isActiveAccounts, start, length, getCompanyId());
		}
	}

	@Override
	public PaginationList<SearchResultlist> getSearchResultByInput(
			SearchInput input, int start, int length) {
		FinanceTool tool = new FinanceTool();
		return tool.getCompanyManager().getSearchByInput(input, start, length,
				getCompanyId());

	}

	@Override
	public boolean savePortletPageConfig(ClientPortletPageConfiguration config) {
		FinanceTool tool = new FinanceTool();
		return tool.savePortletPageConfig(config);
	}

	@Override
	public double getMostRecentTransactionCurrencyFactor(long companyId,
			long currencyId, long tdate) throws AccounterException {
		FinanceTool tool = new FinanceTool();
		return tool.getMostRecentTransactionCurreencyFactorBasedOnCurrency(
				companyId, currencyId, tdate);
	}

	@Override
	public ClientPortletPageConfiguration getPortletPageConfiguration(
			String pageName) {
		FinanceTool tool = new FinanceTool();
		return tool.getPortletPageConfiguration(pageName);
	}

	@Override
	public ArrayList<ClientPayee> getOwePayees(int oweType) {
		FinanceTool tool = new FinanceTool();
		if (oweType == Portlet.TYPE_I_OWE) {
			return (ArrayList<ClientPayee>) tool.getDashboardManager()
					.getWhoIOwe(getCompanyId());
		} else if (oweType == Portlet.TYPE_OWE_TO_ME) {
			return (ArrayList<ClientPayee>) tool.getDashboardManager()
					.getWhoOwesMe(getCompanyId());
		}
		return null;
	}

	@Override
	public ArrayList<RecentTransactionsList> getRecentTransactions(int limit) {
		FinanceTool tool = new FinanceTool();
		ArrayList<RecentTransactionsList> activities = tool
				.getRecentTransactionsList(getCompanyId(), limit);
		// CHECKING WHETHER THAT TRANSACTION DELETED OR NOT..
		// for (int i = 0; i < activities.size(); i++) {
		// ClientActivity clientActivity = activities.get(i);
		// if (clientActivity.getActivityType() == 4) {
		// for (int j = 0; j < activities.size(); j++) {
		// if (activities.get(j).getObjectID() == clientActivity
		// .getObjectID()) {
		// activities.remove(activities.get(j));
		// }
		// }
		// }
		// }

		return activities;
	}

	@Override
	public ArrayList<ClientMessageOrTask> getMessagesAndTasks()
			throws AccounterException {
		FinanceTool tool = new FinanceTool();
		return tool.getCompanyManager().getMessagesAndTasks(getCompanyId());
	}

	@Override
	public ArrayList<ClientActivity> getAuditHistory(int objectType,
			long objectID, long activityID) throws AccounterException {
		FinanceTool tool = getFinanceTool();
		if (tool != null) {
			return tool.getUserManager().getAuditHistory(objectType, objectID,
					activityID, getCompanyId());
		}
		return null;
	}

	@Override
	public PaginationList<ClientReminder> getRemindersList(int start,
			int length, int viewType) throws AccounterException {
		FinanceTool tool = new FinanceTool();
		return tool.getCompanyManager().getRemindersList(start, length,
				viewType, getCompanyId());
	}

	@Override
	public ExpensePortletData getAccountsAndValues(long startDate, long endDate) {
		FinanceTool tool = new FinanceTool();
		FinanceDate[] dates = getMinimumAndMaximumDates(new ClientFinanceDate(
				startDate), new ClientFinanceDate(endDate), getCompanyId());
		ExpensePortletData portletData = tool.getDashboardManager()
				.getExpensesAccountsBalances(getCompanyId(),
						dates[0].getDate(), dates[1].getDate());
		return portletData;
	}

	@Override
	public ClientEnterBill getEnterBillByEstimateId(long estimate)
			throws AccounterException {
		return new FinanceTool().getVendorManager().getEnterBillByEstimateId(
				estimate);
	}

	@Override
	public ArrayList<ClientAdvertisement> getAdvertisements()
			throws AccounterException {
		FinanceTool financeTool = new FinanceTool();
		return (ArrayList<ClientAdvertisement>) financeTool.getAdvertisements();
	}

	@Override
	public ClientTransaction getTransactionToCreate(
			ClientRecurringTransaction obj, long transactionDate)
			throws AccounterException {
		try {
			return new FinanceTool().getCompanyManager()
					.getTransactionToCreate(obj, transactionDate);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AccounterException(e.getMessage());
		}
	}

	@Override
	public PaginationList<PaymentsList> getPayeeChecks(int type, long fromDate,
			long toDate, int start, int length, int viewType) {
		PaginationList<PaymentsList> checks = null;
		try {
			FinanceDate[] dates = getMinimumAndMaximumDates(
					new ClientFinanceDate(fromDate), new ClientFinanceDate(
							toDate), getCompanyId());
			checks = getFinanceTool().getVendorManager().getPayeeChecks(
					getCompanyId(), type, dates[0], dates[1], viewType, start,
					length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return checks;
	}

	@Override
	public PaginationList<BillsList> getBillsAndItemReceiptList(
			boolean isExpensesList, int transactionType, long fromDate,
			long toDate, int start, int length, int viewType) {
		PaginationList<BillsList> billList = null;
		try {
			FinanceDate[] dates = getMinimumAndMaximumDates(
					new ClientFinanceDate(fromDate), new ClientFinanceDate(
							toDate), getCompanyId());
			billList = getFinanceTool().getVendorManager().getBillsList(
					isExpensesList, getCompanyId(), transactionType,
					dates[0].getDate(), dates[1].getDate(), start, length,
					viewType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return billList;
	}

	@Override
	public ArrayList<ClientTDSTransactionItem> getTDSTransactionItemsList(
			int formType) {

		List<ClientTDSTransactionItem> transactionItemList = null;
		try {

			transactionItemList = getFinanceTool().getTDSTransactionItemsList(
					formType, getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<ClientTDSTransactionItem>(transactionItemList);
	}

	@Override
	public PaginationList<ClientTDSChalanDetail> getTDSChalanDetailsList() {
		PaginationList<ClientTDSChalanDetail> chalanList = new PaginationList<ClientTDSChalanDetail>();
		try {

			chalanList = getFinanceTool().getTDSChalanDetailsList(
					getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return chalanList;
	}

	@Override
	public ArrayList<ClientTDSChalanDetail> getTDSChallansForAckNo(String ackNo)
			throws AccounterException {
		return getFinanceTool().getTDSChallansForAckNo(ackNo, getCompanyId());
	}

	@Override
	public ClientTDSDeductorMasters getDeductorMasterDetails() {

		try {

			return getFinanceTool().getTDSDeductorMasterDetails(getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<IncomeExpensePortletInfo> getIncomeExpensePortletInfo(
			int type, ClientFinanceDate startDate, ClientFinanceDate endDate)
			throws AccounterException {
		FinanceDate[] dates = getMinimumAndMaximumDates(startDate, endDate,
				getCompanyId());
		return getFinanceTool().getDashboardManager()
				.getIncomeExpensePortletInfo(getCompanyId(), type,
						dates[0].getDate(), dates[1].getDate());
	}

	@Override
	public ExpensePortletData getIncomeBreakdownPortletData(
			ClientFinanceDate startDate, ClientFinanceDate endDate)
			throws AccounterException {
		FinanceTool tool = new FinanceTool();
		FinanceDate[] dates = getMinimumAndMaximumDates(startDate, endDate,
				getCompanyId());
		if (startDate.equals(new ClientFinanceDate())
				&& endDate.equals(new ClientFinanceDate())) {
			startDate = new ClientFinanceDate(0);
			endDate = new ClientFinanceDate(0);
		}
		ExpensePortletData portletData = tool.getDashboardManager()
				.getIncomeAccountsBalances(getCompanyId(), dates[0].getDate(),
						dates[1].getDate());
		return portletData;
	}

	@Override
	public ArrayList<PayeesBySalesPortletData> getTopCustomersBySalesPortletData(
			ClientFinanceDate startDate, ClientFinanceDate endDate, int limit)
			throws AccounterException {
		FinanceDate[] dates = getMinimumAndMaximumDates(startDate, endDate,
				getCompanyId());
		return getFinanceTool().getDashboardManager().getCustomersBySales(
				getCompanyId(), dates[0], dates[1], limit);
	}

	@Override
	public ArrayList<PayeesBySalesPortletData> getTopVendorsBySalesPortletData(
			ClientFinanceDate startDate, ClientFinanceDate endDate, int limit)
			throws AccounterException {
		FinanceDate[] dates = getMinimumAndMaximumDates(startDate, endDate,
				getCompanyId());
		return getFinanceTool().getDashboardManager().getVendorsBySales(
				getCompanyId(), dates[0], dates[1], limit);
	}

	@Override
	public String printCheques(long chequeLayoutId,
			ArrayList<PrintCheque> printCheques) {
		try {
			Company company = (Company) HibernateUtil.getCurrentSession().get(
					Company.class, getCompanyId());
			return ChequePdfGenerator.generate(chequeLayoutId,
					company.getDisplayName(), printCheques);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<ClientETDSFillingItem> getEtdsDetails(int formNo,
			int quater, ClientFinanceDate fromDate, ClientFinanceDate toDate,
			int startYear, int endYear) {
		List<ClientETDSFillingItem> etdsList = new ArrayList<ClientETDSFillingItem>();
		try {

			etdsList = getFinanceTool().getEtdsList(formNo, quater, fromDate,
					toDate, startYear, endYear, getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<ClientETDSFillingItem>(etdsList);
	}

	@Override
	public boolean updateAckNoForChallans(int formNo, int quater,
			ClientFinanceDate fromDate, ClientFinanceDate toDate,
			int startYear, int endYear, String ackNo, long date)
			throws AccounterException {
		return getFinanceTool().updateAckNoForChallans(formNo, quater,
				fromDate, toDate, startYear, endYear, ackNo, date,
				getCompanyId());
	}

	@Override
	public ArrayList<PayeesBySalesPortletData> getItemsBySalesQuantity(
			ClientFinanceDate startDate, ClientFinanceDate endDate, int limit)
			throws AccounterException {
		FinanceDate[] dates = getMinimumAndMaximumDates(startDate, endDate,
				getCompanyId());
		return getFinanceTool().getDashboardManager().getItemsBySalesQuantity(
				getCompanyId(), dates[0], dates[1], limit);
	}

	@Override
	public ArrayList<PayeesBySalesPortletData> getItemsByPurchaseQuantity(
			ClientFinanceDate startDate, ClientFinanceDate endDate, int limit)
			throws AccounterException {
		FinanceDate[] dates = getMinimumAndMaximumDates(startDate, endDate,
				getCompanyId());
		return getFinanceTool().getDashboardManager()
				.getItemsByPurchaseQuantity(getCompanyId(), dates[0], dates[1],
						limit);
	}

	@Override
	public ArrayList<YearOverYearPortletData> getAccountsBalancesByDate(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long accountId, int chartType) throws AccounterException {
		FinanceDate[] dates = getMinimumAndMaximumDates(startDate, endDate,
				getCompanyId());
		return getFinanceTool().getDashboardManager()
				.getAccountsBalancesByDate(getCompanyId(), dates[0], dates[1],
						accountId, chartType);
	}

	@Override
	public ClientTDSResponsiblePerson getResponsiblePersonDetails() {
		try {

			return getFinanceTool().getResponsiblePersonDetails(getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean savePortletConfiguration(
			ClientPortletConfiguration configuration) {
		FinanceTool tool = new FinanceTool();
		return tool.savePortletConfiguration(configuration);
	}

	public PaginationList<ClientStatement> getBankStatements(long accountId) {

		PaginationList<ClientStatement> bankStatements = null;
		try {
			bankStatements = getFinanceTool().getBankStatements(accountId,
					getCompanyId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bankStatements;

	}

	@Override
	public String getIRASFileInformation(ClientFinanceDate startDate,
			ClientFinanceDate endDate, boolean isXml) throws AccounterException {
		return getFinanceTool().getIRASFileInformationByDate(getCompanyId(),
				startDate.getDate(), endDate.getDate(), isXml);
	}

	@Override
	public ClientMakeDeposit getDepositByEstimateId(long estimate)
			throws AccounterException {
		return new FinanceTool().getVendorManager().getDepositByEstimateId(
				estimate);
	}

	@Override
	public boolean isChalanDetailsFiled(int formNo, int quater,
			ClientFinanceDate fromDate, ClientFinanceDate toDate,
			int startYear, int endYear) throws AccounterException {
		List<ClientTDSChalanDetail> chalanList;
		try {
			chalanList = new FinanceTool().getChalanList(formNo, quater,
					fromDate, toDate, startYear, endYear, getCompanyId());
			for (ClientTDSChalanDetail clientTDSChalanDetail : chalanList) {
				if (clientTDSChalanDetail.isFiled()) {
					return true;
				}
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public PaginationList<DepositsTransfersList> getDepositsList(long fromDate,
			long toDate, int start, int length, int type)
			throws AccounterException {
		try {
			return getFinanceTool().getCompanyManager().getDepositsList(
					getCompanyId(), fromDate, toDate, start, length, type);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public PaginationList<DepositsTransfersList> getTransfersList(
			long fromDate, long toDate, int start, int length, int type)
			throws AccounterException {
		try {
			return getFinanceTool().getCompanyManager().getTransfersList(
					getCompanyId(), fromDate, toDate, start, length, type);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public PaginationList<TransactionsList> getSpentTransactionsList(
			long fromDate, long toDate, int start, int length) {
		PaginationList<TransactionsList> transactionsList = null;
		try {
			FinanceDate[] dates = getMinimumAndMaximumDates(null, null,
					getCompanyId());
			transactionsList = getFinanceTool().getInventoryManager()
					.getSpentTransactionsList(getCompanyId(),
							dates[0].getDate(), dates[1].getDate(), start,
							length);
			return transactionsList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public PaginationList<TransactionsList> getReceivedTransactionsList(
			long fromDate, long toDate, int start, int length) {
		PaginationList<TransactionsList> transactionsList = null;
		try {
			FinanceDate[] dates = getMinimumAndMaximumDates(null, null,
					getCompanyId());
			transactionsList = getFinanceTool().getInventoryManager()
					.getReceivedTransactionsList(getCompanyId(),
							dates[0].getDate(), dates[1].getDate(), start,
							length);
			return transactionsList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public PaginationList<TransactionsList> getSpentAndReceivedTransactionsList(
			long endOfFiscalYear, long date, int start, int length) {
		PaginationList<TransactionsList> transactionsList = null;
		FinanceDate[] dates = getMinimumAndMaximumDates(new ClientFinanceDate(
				20110930), new ClientFinanceDate(), getCompanyId());

		try {
			transactionsList = getFinanceTool().getInventoryManager()
					.getReceivedTransactionsList(getCompanyId(),
							dates[0].getDate(), dates[1].getDate(), start,
							length);

			PaginationList<TransactionsList> list2 = getFinanceTool()
					.getInventoryManager().getSpentTransactionsList(
							getCompanyId(), dates[0].getDate(),
							dates[1].getDate(), start, length);
			if (list2 != null && !list2.isEmpty()) {
				for (TransactionsList tr : list2) {
					transactionsList.add(tr);
				}
			}
		} catch (DAOException e) {
			e.printStackTrace();
		} catch (AccounterException e) {
			e.printStackTrace();
		}

		return transactionsList;
	}

	@Override
	public Set<InvitableUser> getIvitableUsers() throws AccounterException {
		Set<InvitableUser> invitableUsers = new HashSet<InvitableUser>();
		Session currentSession = HibernateUtil.getCurrentSession();
		long companyId = getCompanyId();
		Company company = (Company) currentSession
				.get(Company.class, companyId);
		Client client = company.getCreatedBy().getClient();

		Set<String> userMailds = client.getClientSubscription().getMembers();
		userMailds.add("support@annaccounting.com");
		List list = currentSession.getNamedQuery("list.Users.by.emailIds")
				.setParameterList("users", userMailds).list();

		Iterator iterator = list.iterator();
		Map<String, InvitableUser> invitableUsersMap = new HashMap<String, InvitableUser>();
		Set<String> existed = new HashSet<String>();
		Set<String> notExisted = new HashSet<String>();
		while (iterator.hasNext()) {
			Object[] next = (Object[]) iterator.next();
			String email = (String) next[0];
			if (invitableUsersMap.get(email) == null) {
				InvitableUser in = new InvitableUser();
				in.setEmail(email);
				in.setFirstName((String) next[1]);
				in.setLastName((String) next[2]);
				invitableUsersMap.put(email, in);
			}

			if (companyId == (Long) next[3]) {
				if (!(Boolean) next[4]) {
					existed.add(email);
					continue;
				}
			}
			notExisted.add(email);
		}

		for (String s : userMailds) {
			if (existed.contains(s)) {
				continue;
			}

			InvitableUser user = invitableUsersMap.get(s);
			if (user == null) {
				user = new InvitableUser();
				user.setEmail(s);
			}
			invitableUsers.add(user);
		}
		return invitableUsers;
	}

	@Override
	public int getClientCompaniesCount() {
		long id = AccounterThreadLocal.get().getClient().getID();
		CompanyManager manager = new CompanyManager();
		int companies = 0;
		try {
			companies = manager.getClientCompaniesCount(id);
		} catch (AccounterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return companies;
	}

	public PaginationList<PurchaseOrdersList> getPurchaseOrders(long fromDate,
			long toDate, int type, int start, int length)
			throws AccounterException {
		try {
			FinanceTool tool = getFinanceTool();
			FinanceDate[] dates = getMinimumAndMaximumDates(
					new ClientFinanceDate(fromDate), new ClientFinanceDate(
							toDate), getCompanyId());
			return tool != null ? tool.getPurchageManager()
					.getPurchaseOrdersList(getCompanyId(), dates[0].getDate(),
							dates[1].getDate(), type, start, length) : null;
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<ImportField> getFieldsOf(int importerType)
			throws AccounterException {
		return (ArrayList<ImportField>) getFinanceTool().getFieldsOfImporter(
				importerType);
	}

	@Override
	public HashMap<Integer, Object> importData(String filePath,
			int importerType, HashMap<String, String> importMap,
			String dateFormate) throws AccounterException {

		return getFinanceTool().importData(getCompanyId(), getUserEmail(),
				filePath, importerType, importMap, dateFormate);
	}

	@Override
	public PaginationList<TransactionHistory> getItemTransactionsList(
			long itemId, int transactionType, int transactionStatus,
			ClientFinanceDate startDate, ClientFinanceDate endDate, int start,
			int length) {
		FinanceDate[] dates = getMinimumAndMaximumDates(startDate, endDate,
				getCompanyId());
		PaginationList<TransactionHistory> resultList = new PaginationList<TransactionHistory>();
		try {
			resultList = getFinanceTool().getInventoryManager()
					.getTransactionsByType(itemId, transactionType,
							transactionStatus, dates[0].getDate(),
							dates[1].getDate(), getCompanyId(), start, length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	@Override
	public ArrayList<ClientJob> getJobsByCustomer(long id) {
		List<ClientJob> jobs = new ArrayList<ClientJob>();
		Session currentSession = HibernateUtil.getCurrentSession();
		@SuppressWarnings("unchecked")
		ArrayList<Job> list = (ArrayList<Job>) currentSession
				.getNamedQuery("getJobsByCustomer")
				.setParameter("customerId", id)
				.setParameter("companyId", getCompanyId()).list();
		try {
			for (Job job : list) {
				ClientJob clientJob = new ClientConvertUtil().toClientObject(
						job, ClientJob.class);
				jobs.add(clientJob);
			}
			return (ArrayList<ClientJob>) jobs;
		} catch (AccounterException e) {
			e.printStackTrace();
		}
		return null;
	}

	public PaginationList<ClientJob> getJobs() {
		PaginationList<ClientJob> resultList = new PaginationList<ClientJob>();

		try {
			resultList = getFinanceTool().getCustomerManager().getJobsList(
					getCompanyId());
		} catch (AccounterException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	@Override
	public ArrayList<EstimatesAndSalesOrdersList> getSalesOrdersList(
			long customerId) throws AccounterException {

		FinanceTool tool = getFinanceTool();
		return tool != null ? tool.getCustomerManager().getSalesOrdersList(
				customerId, getCompanyId()) : null;

	}

	public ClientWriteCheck getWriteCheckByEstimateId(long ld)
			throws AccounterException {

		return new FinanceTool().getVendorManager().getWriteCheckByEstimateId(
				ld);

	}

	@Override
	public ClientCashPurchase getCashPurchaseByEstimateId(long id)
			throws AccounterException {
		return new FinanceTool().getVendorManager()
				.getCashPurchaseByEstimateId(id);
	}

	@Override
	public PaginationList<ClientTAXAdjustment> getTaxAdjustmentsList(
			int viewType, long startDate, long endDate, int start, int length) {
		return new FinanceTool().getTaxManager().getTaxAdjustments(viewType,
				getCompanyId(), startDate, endDate, start, length);
	}

	@Override
	public void mergeClass(ClientAccounterClass fromClass,
			ClientAccounterClass toClass) throws AccounterException {
		FinanceTool tool = getFinanceTool();
		if (tool != null) {
			try {
				tool.mergeClass(fromClass, toClass, getCompanyId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void mergeLocation(ClientLocation fromLocation,
			ClientLocation toLocation) throws AccounterException {

		FinanceTool tool = getFinanceTool();
		if (tool != null) {
			try {
				tool.mergeLocation(fromLocation, toLocation, getCompanyId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public Long getTransaction(boolean isPrev, long id, int type, int subType)
			throws AccounterException {
		return getFinanceTool().getCompanyManager().getTransaction(isPrev,
				getCompanyId(), id, type, subType);
	}

	@Override
	public ArrayList<ItemUnitPrice> getUnitPricesByPayee(boolean isCust,
			long payee, long item) throws AccounterException {
		return getFinanceTool().getCompanyManager().getUnitPricesByPayee(
				getCompanyId(), isCust, payee, item);
	}

	@Override
	public PaginationList<PaymentsList> getPayRunsList(
			ClientFinanceDate startDate, ClientFinanceDate endDate, int start,
			int length, int type, int transactionType)
			throws AccounterException {
		PaginationList<PaymentsList> payruns = null;
		try {
			FinanceDate[] dates = getMinimumAndMaximumDates(startDate, endDate,
					getCompanyId());
			payruns = getFinanceTool().getPayrollManager().getPayRunsList(
					getCompanyId(), dates[0].getDate(), dates[1].getDate(),
					type, start, length, transactionType);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return payruns;
	}

	public PaginationList<ClientTAXReturn> getTaxReturnList(long startDate,
			long endDate, int start, int length, int viewId) {
		PaginationList<ClientTAXReturn> taxReturns = null;
		try {
			FinanceDate[] dates = getMinimumAndMaximumDates(
					new ClientFinanceDate(startDate), new ClientFinanceDate(
							endDate), getCompanyId());
			taxReturns = getFinanceTool().getTaxManager().getTaxReturns(
					getCompanyId(), dates[0].getDate(), dates[1].getDate(),
					start, length, viewId);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return taxReturns;
	}

	@Override
	public PaginationList<ClientBuildAssembly> getBuildAssembliesList(
			long startDate, long endDate, int start, int viewId, int length) {
		PaginationList<ClientBuildAssembly> buildAssemblies = null;
		try {
			FinanceDate[] dates = getMinimumAndMaximumDates(
					new ClientFinanceDate(startDate), new ClientFinanceDate(
							endDate), getCompanyId());
			buildAssemblies = getFinanceTool().getInventoryManager()
					.getBuildAssembly(getCompanyId(), dates[0].getDate(),
							dates[1].getDate(), start, viewId, length);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return buildAssemblies;
	}

	@Override
	public PaginationList<ClientPayTAX> getPayTaxList(long startDate,
			long endDate, int viewId, int start, int length) {
		PaginationList<ClientPayTAX> payTAXs = null;
		try {
			FinanceDate[] dates = getMinimumAndMaximumDates(
					new ClientFinanceDate(startDate), new ClientFinanceDate(
							endDate), getCompanyId());
			payTAXs = getFinanceTool().getTaxManager().getPayTaxList(
					getCompanyId(), dates[0].getDate(), dates[1].getDate(),
					start, length, viewId);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return payTAXs;
	}

	@Override
	public PaginationList<ClientReceiveVAT> getTaxRefundsList(long startDate,
			long endDate, int start, int length, int viewId) {
		PaginationList<ClientReceiveVAT> receiveVATs = null;
		try {
			FinanceDate[] dates = getMinimumAndMaximumDates(
					new ClientFinanceDate(startDate), new ClientFinanceDate(
							endDate), getCompanyId());
			receiveVATs = getFinanceTool().getTaxManager().getTaxRefunds(
					getCompanyId(), dates[0].getDate(), dates[1].getDate(),
					start, length, viewId);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return receiveVATs;
	}

	@Override
	public ArrayList<ClientAccount> getPaypalAccounts() {

		ArrayList<ClientAccount> clientAccounts = new ArrayList<ClientAccount>();

		try {
			clientAccounts = getFinanceTool().getPaypalAccounts(getCompanyId());
		} catch (AccounterException e) {
			e.printStackTrace();
		}
		return clientAccounts;
	}

	@Override
	public PaginationList<ClientPaypalTransation> getSavedPaypalTransaction(
			ClientAccount clientAccount) {
		PaginationList<ClientPaypalTransation> transactionList = null;
		try {

			transactionList = getFinanceTool().getSavedPaypalTransactions(
					clientAccount, getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return transactionList;
	}

	@Override
	public List<ClientPaypalTransation> getNewPaypalTransactionsList(
			long accountID) {
		List<ClientPaypalTransation> tran = new ArrayList<ClientPaypalTransation>();
		try {
			tran = getFinanceTool().getnewPaypalTransaction(getCompanyId(),
					accountID);
		} catch (AccounterException e) {
			e.printStackTrace();
		}
		return tran;
	}

	@Override
	public String getPaypalTransactionDetailsForId(String transactionID,
			long accountID) {
		try {
			getFinanceTool().getCompletePaypalTransactionDetailsForID(
					transactionID, accountID);
		} catch (AccounterException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public PaginationList<ConsultantsDetailsList> getConsultantItemsDetails() {
		PaginationList<ConsultantsDetailsList> consultantItems = new PaginationList<ConsultantsDetailsList>();
		try {

			consultantItems = getFinanceTool().getConsultantItemsDetails(getCompanyId());


		} catch (Exception e) {
			e.printStackTrace();
		}

		return consultantItems;
	}


}
