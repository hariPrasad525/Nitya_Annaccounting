package com.nitya.accounter.developer.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.google.gdata.util.common.util.Base64;
import com.nitya.accounter.core.Developer;
import com.nitya.accounter.core.EU;
import com.nitya.accounter.core.User;
import com.nitya.accounter.developer.api.core.ApiProcessor;
import com.nitya.accounter.developer.api.core.ApiResult;
import com.nitya.accounter.developer.api.process.CreateProcessor;
import com.nitya.accounter.developer.api.process.CreateUserSecretProcessor;
import com.nitya.accounter.developer.api.process.DeleteProcessor;
import com.nitya.accounter.developer.api.process.EncryptCompanyProcessor;
import com.nitya.accounter.developer.api.process.ReadProcessor;
import com.nitya.accounter.developer.api.process.ReportsListProcessor;
import com.nitya.accounter.developer.api.process.UpdateProcessor;
import com.nitya.accounter.developer.api.process.lists.AccountsProcessor;
import com.nitya.accounter.developer.api.process.lists.BillsAndExpensesProcessor;
import com.nitya.accounter.developer.api.process.lists.BugetItemsProcessor;
import com.nitya.accounter.developer.api.process.lists.ChalanDetailsListProcessor;
import com.nitya.accounter.developer.api.process.lists.ChargesProcessor;
import com.nitya.accounter.developer.api.process.lists.ClassesProcessor;
import com.nitya.accounter.developer.api.process.lists.CreditsProcessor;
import com.nitya.accounter.developer.api.process.lists.CurrenciesProcessor;
import com.nitya.accounter.developer.api.process.lists.CustomerGroupsProcessor;
import com.nitya.accounter.developer.api.process.lists.CustomerRefundsProcessor;
import com.nitya.accounter.developer.api.process.lists.CustomersProcessor;
import com.nitya.accounter.developer.api.process.lists.FixedAssetItemsProcessor;
import com.nitya.accounter.developer.api.process.lists.InvitableUsersProcessor;
import com.nitya.accounter.developer.api.process.lists.InvoicesProcessor;
import com.nitya.accounter.developer.api.process.lists.ItemGroupsProcessor;
import com.nitya.accounter.developer.api.process.lists.ItemsProcessor;
import com.nitya.accounter.developer.api.process.lists.JobsProcessor;
import com.nitya.accounter.developer.api.process.lists.JournalEntriesProcessor;
import com.nitya.accounter.developer.api.process.lists.LocationsProcessor;
import com.nitya.accounter.developer.api.process.lists.MeasurementsProcessor;
import com.nitya.accounter.developer.api.process.lists.PaymentTermsProcessor;
import com.nitya.accounter.developer.api.process.lists.PaymentsProcessor;
import com.nitya.accounter.developer.api.process.lists.PriceLevelsProcessor;
import com.nitya.accounter.developer.api.process.lists.PurchaseOrdersProcessor;
import com.nitya.accounter.developer.api.process.lists.QuotesProcessor;
import com.nitya.accounter.developer.api.process.lists.ReceivePaymentsProcessor;
import com.nitya.accounter.developer.api.process.lists.RecurringTransactionsProcessor;
import com.nitya.accounter.developer.api.process.lists.RemindersProcessor;
import com.nitya.accounter.developer.api.process.lists.SalesOrdersProcessor;
import com.nitya.accounter.developer.api.process.lists.ShippingMethodsProcessor;
import com.nitya.accounter.developer.api.process.lists.ShippingTermsProcessor;
import com.nitya.accounter.developer.api.process.lists.StockAdjustmentsProcessor;
import com.nitya.accounter.developer.api.process.lists.TaxAgenciesProcessor;
import com.nitya.accounter.developer.api.process.lists.TaxCodesProcessor;
import com.nitya.accounter.developer.api.process.lists.TaxItemsProcessor;
import com.nitya.accounter.developer.api.process.lists.UserActivitiesProcessor;
import com.nitya.accounter.developer.api.process.lists.VendorGroupsProcessor;
import com.nitya.accounter.developer.api.process.lists.VendorPaymentsProcessor;
import com.nitya.accounter.developer.api.process.lists.VendorsProcessor;
import com.nitya.accounter.developer.api.process.lists.WarehouseItemsProcessor;
import com.nitya.accounter.developer.api.process.lists.WarehouseTransfersProcessor;
import com.nitya.accounter.developer.api.process.lists.WarehousesProcessor;
import com.nitya.accounter.developer.api.process.reports.AgedCreditorsProcessor;
import com.nitya.accounter.developer.api.process.reports.AgedCreditorsbyDebitornameProcessor;
import com.nitya.accounter.developer.api.process.reports.AgeddebtorsBydebitorNameProcessor;
import com.nitya.accounter.developer.api.process.reports.AgeddebtorsProcessor;
import com.nitya.accounter.developer.api.process.reports.AmountsDuetoVendorProcessor;
import com.nitya.accounter.developer.api.process.reports.AutomaticTransactionsProcessor;
import com.nitya.accounter.developer.api.process.reports.BalancesheetReportProcessor;
import com.nitya.accounter.developer.api.process.reports.BudgetOverviewProcessor;
import com.nitya.accounter.developer.api.process.reports.BudgetvsActualsProcessor;
import com.nitya.accounter.developer.api.process.reports.CashflowReportProcessor;
import com.nitya.accounter.developer.api.process.reports.CheckDetailsProcessor;
import com.nitya.accounter.developer.api.process.reports.CreditorsProcessor;
import com.nitya.accounter.developer.api.process.reports.CustomerTransactionHistoryProcessor;
import com.nitya.accounter.developer.api.process.reports.DebitorslistProcessor;
import com.nitya.accounter.developer.api.process.reports.DepositDetailProcessor;
import com.nitya.accounter.developer.api.process.reports.DepreciationReportProcessor;
import com.nitya.accounter.developer.api.process.reports.EcsaleslistDetailsProcessor;
import com.nitya.accounter.developer.api.process.reports.EcsaleslistProcessor;
import com.nitya.accounter.developer.api.process.reports.ExpenseReportsProcesser;
import com.nitya.accounter.developer.api.process.reports.InventoryStockStatusByItemProcessor;
import com.nitya.accounter.developer.api.process.reports.InventoryStockStatusByVendorProcessor;
import com.nitya.accounter.developer.api.process.reports.InventoryValuationDetailsProcessor;
import com.nitya.accounter.developer.api.process.reports.InventoryValutionSummaryProcessor;
import com.nitya.accounter.developer.api.process.reports.MinimumandMaximumTransactiondateProcessor;
import com.nitya.accounter.developer.api.process.reports.MissingchecksProcessor;
import com.nitya.accounter.developer.api.process.reports.MostProfitableCustomersProcessor;
import com.nitya.accounter.developer.api.process.reports.PayeeStatementsProcessor;
import com.nitya.accounter.developer.api.process.reports.PriorreturnVatSummaryProcessor;
import com.nitya.accounter.developer.api.process.reports.PriorvatReturnReportProcessor;
import com.nitya.accounter.developer.api.process.reports.PriorvatReturnVatdetailReportProcessor;
import com.nitya.accounter.developer.api.process.reports.ProfitandLossreportProcessor;
import com.nitya.accounter.developer.api.process.reports.PurchaseReportItemsProcessor;
import com.nitya.accounter.developer.api.process.reports.PurchasesbyItemDetailNameProcessor;
import com.nitya.accounter.developer.api.process.reports.PurchasesbyItemDetailProcessor;
import com.nitya.accounter.developer.api.process.reports.PurchasesbyItemSummaryProcessor;
import com.nitya.accounter.developer.api.process.reports.PurchasesbyVendorSummaryProcessor;
import com.nitya.accounter.developer.api.process.reports.PurchasesbyVendordetailProcessor;
import com.nitya.accounter.developer.api.process.reports.PurchasesbyVendordetailbyNameProcessor;
import com.nitya.accounter.developer.api.process.reports.ReconcilationDiscrepanyProcessor;
import com.nitya.accounter.developer.api.process.reports.ReportsByClassProcessor;
import com.nitya.accounter.developer.api.process.reports.ReportsByLocationProcessor;
import com.nitya.accounter.developer.api.process.reports.ReverseChargelistDetailsProcessor;
import com.nitya.accounter.developer.api.process.reports.ReversechargelistProcessor;
import com.nitya.accounter.developer.api.process.reports.SalesByCustomerSummaryProcessor;
import com.nitya.accounter.developer.api.process.reports.SalesReportItemsProcessor;
import com.nitya.accounter.developer.api.process.reports.SalesTaxLiabilityProcessor;
import com.nitya.accounter.developer.api.process.reports.SalesbyCustomerdetailProcessor;
import com.nitya.accounter.developer.api.process.reports.SalesbyItemdetailProcessor;
import com.nitya.accounter.developer.api.process.reports.SalesbyItemdetailbyNameProcessor;
import com.nitya.accounter.developer.api.process.reports.SalesbycustomerDetailbynameProcessor;
import com.nitya.accounter.developer.api.process.reports.TaxItemExceptionDetailProcessor;
import com.nitya.accounter.developer.api.process.reports.TrailBalanceProcessor;
import com.nitya.accounter.developer.api.process.reports.TransactionDetailbyAccountProcessor;
import com.nitya.accounter.developer.api.process.reports.TransactionDetailbyTaxitemnameProcessor;
import com.nitya.accounter.developer.api.process.reports.TransactionHistoryCustomersProcessor;
import com.nitya.accounter.developer.api.process.reports.TransactionHistoryVendorsProcessor;
import com.nitya.accounter.developer.api.process.reports.TransactiondetailbyAccountnameProcessor;
import com.nitya.accounter.developer.api.process.reports.TransactiondetailbyTaxitemProcessor;
import com.nitya.accounter.developer.api.process.reports.UncategorisedAmountsProcessor;
import com.nitya.accounter.developer.api.process.reports.Vat100ReportProcessor;
import com.nitya.accounter.developer.api.process.reports.VatItemDetailsProcessor;
import com.nitya.accounter.developer.api.process.reports.VatitemSummaryProcessor;
import com.nitya.accounter.developer.api.process.reports.VendorTransactionHistoryProcessor;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;

public class ApiBaseServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Map<String, ApiProcessor> processors = new HashMap<String, ApiProcessor>();

	@Override
	public void init() throws ServletException {

		// Operations
		processors.put("usersecret", new CreateUserSecretProcessor());
		processors.put("encryptcompany", new EncryptCompanyProcessor());
		processors.put("reportslist", new ReportsListProcessor());

		// CRUD
		processors.put("create", new CreateProcessor());
		processors.put("read", new ReadProcessor());
		processors.put("update", new UpdateProcessor());
		processors.put("delete", new DeleteProcessor());

		// Lists
		processors.put("Account", new AccountsProcessor());
		processors.put("Customer", new CustomersProcessor());
		processors.put("Invoice", new InvoicesProcessor());
		processors.put("Item", new ItemsProcessor());
		processors.put("Currency", new CurrenciesProcessor());
		processors.put("BudgetItem", new BugetItemsProcessor());
		processors.put("Expense", new BillsAndExpensesProcessor());
		processors.put("ChalanDetails", new ChalanDetailsListProcessor());
		processors.put("Charges", new ChargesProcessor());
		processors.put("Classes", new ClassesProcessor());
		processors.put("Credits", new CreditsProcessor());
		processors.put("CustomerGroup", new CustomerGroupsProcessor());
		processors.put("CustomerRefund", new CustomerRefundsProcessor());
		processors.put("FixedAsset", new FixedAssetItemsProcessor());
		processors.put("InvitableUsers", new InvitableUsersProcessor());
		processors.put("ItemGroup", new ItemGroupsProcessor());
		processors.put("Job", new JobsProcessor());
		processors.put("JournalEntry", new JournalEntriesProcessor());
		processors.put("Location", new LocationsProcessor());
		processors.put("Measurement", new MeasurementsProcessor());
		processors.put("Payments", new PaymentsProcessor());
		processors.put("PaymentTerms", new PaymentTermsProcessor());
		processors.put("PriceLevel", new PriceLevelsProcessor());
		processors.put("PurchaseOrder", new PurchaseOrdersProcessor());
		processors.put("Quotes", new QuotesProcessor());
		processors.put("ReceivePayment", new ReceivePaymentsProcessor());
		processors.put("RecurringTransaction",
				new RecurringTransactionsProcessor());
		processors.put("Reminder", new RemindersProcessor());
		processors.put("SalesOrder", new SalesOrdersProcessor());
		processors.put("ShippingMethod", new ShippingMethodsProcessor());
		processors.put("ShippingTerms", new ShippingTermsProcessor());
		processors.put("StockAdjustment", new StockAdjustmentsProcessor());
		processors.put("TAXAgency", new TaxAgenciesProcessor());
		processors.put("TAXCode", new TaxCodesProcessor());
		processors.put("TAXItem", new TaxItemsProcessor());
		processors.put("UserActivities", new UserActivitiesProcessor());
		processors.put("VendorGroup", new VendorGroupsProcessor());
		processors.put("VendorPrePayment", new VendorPaymentsProcessor());
		processors.put("Vendor", new VendorsProcessor());
		processors.put("WarehouseItems", new WarehouseItemsProcessor());
		processors.put("Warehouse", new WarehousesProcessor());
		processors.put("WarehouseTransfers", new WarehouseTransfersProcessor());

		// Reports
		processors.put("salesbycustomersummary",
				new SalesByCustomerSummaryProcessor());
		processors.put("PurchaseOrder", new PurchaseOrdersProcessor());
		processors.put("AgedCreditorsbyDebitorname",
				new AgedCreditorsbyDebitornameProcessor());
		processors.put("AgedCreditors", new AgedCreditorsProcessor());
		processors.put("AgedDebtorsByDebitor",
				new AgeddebtorsBydebitorNameProcessor());
		processors.put("Ageddebtors", new AgeddebtorsProcessor());
		processors.put("AmountsDuetoVendor", new AmountsDuetoVendorProcessor());
		processors.put("BalanceSheetReport", new BalancesheetReportProcessor());
		processors.put("CashflowReport", new CashflowReportProcessor());
		processors.put("CheckDetails", new CheckDetailsProcessor());
		processors.put("Creditors", new CreditorsProcessor());
		processors.put("CustomerTransactionHistory",
				new CustomerTransactionHistoryProcessor());
		processors.put("Debitorslist", new DebitorslistProcessor());
		processors.put("DepositDetail", new DepositDetailProcessor());
		processors.put("EcsaleslistDetails", new EcsaleslistDetailsProcessor());
		processors.put("Ecsaleslist", new EcsaleslistProcessor());
		processors.put("ExpenseReports", new ExpenseReportsProcesser());
		processors.put("MinimumandMaximumTransactiondate",
				new MinimumandMaximumTransactiondateProcessor());
		processors.put("MostProfitableCustomers",
				new MostProfitableCustomersProcessor());
		processors.put("PayeeStatements", new PayeeStatementsProcessor());
		processors.put("PriorreturnVatSummary",
				new PriorreturnVatSummaryProcessor());
		processors.put("PriorvatReturnReport",
				new PriorvatReturnReportProcessor());
		processors.put("PriorvatReturnVatdetail",
				new PriorvatReturnVatdetailReportProcessor());
		processors.put("ProfitandLossreport",
				new ProfitandLossreportProcessor());
		processors.put("PurchaseReportItems",
				new PurchaseReportItemsProcessor());
		processors.put("PurchasesbyItemDetailName",
				new PurchasesbyItemDetailNameProcessor());
		processors.put("PurchasesbyItemDetail",
				new PurchasesbyItemDetailProcessor());
		processors.put("PurchasesbyItemSummary",
				new PurchasesbyItemSummaryProcessor());
		processors.put("PurchasesbyVendordetailbyName",
				new PurchasesbyVendordetailbyNameProcessor());
		processors.put("PurchasesbyVendordetail",
				new PurchasesbyVendordetailProcessor());
		processors.put("PurchasesbyVendorSummary",
				new PurchasesbyVendorSummaryProcessor());
		processors.put("ReverseChargelistDetails",
				new ReverseChargelistDetailsProcessor());
		processors.put("Reversechargelist", new ReversechargelistProcessor());
		processors.put("SalesbycustomerDetailbyname",
				new SalesbycustomerDetailbynameProcessor());
		processors.put("SalesbyCustomerdetail",
				new SalesbyCustomerdetailProcessor());
		processors.put("SalesbyItemdetailbyName",
				new SalesbyItemdetailbyNameProcessor());
		processors.put("SalesbyItemdetail", new SalesbyItemdetailProcessor());
		processors.put("SalesordeList", new SalesOrdersProcessor());
		processors.put("SalesReportItems", new SalesReportItemsProcessor());
		processors.put("SalesTaxLiability", new SalesTaxLiabilityProcessor());
		processors.put("TrailBalance", new TrailBalanceProcessor());
		processors.put("TransactiondetailbyAccountname",
				new TransactiondetailbyAccountnameProcessor());
		processors.put("TransactionDetailbyAccount",
				new TransactionDetailbyAccountProcessor());
		processors.put("TransactionDetailbyTaxitemname",
				new TransactionDetailbyTaxitemnameProcessor());
		processors.put("TransactiondetailbyTaxitem",
				new TransactiondetailbyTaxitemProcessor());
		processors.put("TransactionHistoryCustomers",
				new TransactionHistoryCustomersProcessor());
		processors.put("TransactionHistoryVendors",
				new TransactionHistoryVendorsProcessor());
		processors.put("UncategorisedAmounts",
				new UncategorisedAmountsProcessor());
		processors.put("Vat100Report", new Vat100ReportProcessor());
		processors.put("VatItemDetails", new VatItemDetailsProcessor());
		processors.put("VatitemSummary", new VatitemSummaryProcessor());
		processors.put("VendorTransactionHistory",
				new VendorTransactionHistoryProcessor());
		processors.put("Budget Overview", new BudgetOverviewProcessor());
		processors.put("Budget Vs Actuals", new BudgetvsActualsProcessor());
		processors.put("Depreciation", new DepreciationReportProcessor());
		processors.put("inventoryValutionSummary",
				new InventoryValutionSummaryProcessor());
		processors.put("inventoryValuationDetails",
				new InventoryValuationDetailsProcessor());
		processors.put("inventoryStockStatusByItem",
				new InventoryStockStatusByItemProcessor());
		processors.put("inventoryStockStatusByVendor",
				new InventoryStockStatusByVendorProcessor());
		processors.put("missingchecks", new MissingchecksProcessor());
		processors.put("reconcilationDiscrepany",
				new ReconcilationDiscrepanyProcessor());
		processors.put("taxItemExceptionDetailReport",
				new TaxItemExceptionDetailProcessor());
		processors.put("ReportsByClass", new ReportsByClassProcessor());
		processors.put("ReportsByLocation", new ReportsByLocationProcessor());
		processors.put("AutomaticTransactions",
				new AutomaticTransactionsProcessor());
		super.init();
	}

	protected void doProcess(HttpServletRequest req, HttpServletResponse resp,
			String type) throws IOException {
		ApiProcessor processor = processors.get(type);
		if (processor == null) {
			sendFail(req, resp, "Wrong request type.");
			return;
		}
		Session session = HibernateUtil.openSession();
		try {

			String authentication = req.getParameter("authentication");
			if (authentication == null || !isAuthenticated(authentication)) {
				sendFail(req, resp,
						"authentication fail. 'authentication' is not present");
				return;
			}
			User user = getUser((String) req.getAttribute("emailId"),
					(Long) req.getAttribute("companyId"));
			byte[] d2 = Base64.decode(authentication);
			if (user != null && user.getSecretKey() != null) {
				EU.createCipher(user.getSecretKey(), d2,
						req.getParameter("ApiKey"));
			}
			processor.beforeProcess(req, resp);
			processor.process(req, resp);
			sendData(req, resp, processor.getResult());
			EU.removeCipher();
		} catch (AccounterException e) {
			String msg = AccounterExceptions.getErrorString(e.getErrorCode());
			if (msg == null) {
				msg = "";
			}
			String m = e.getMessage();
			if (m != null) {
				if (!msg.isEmpty()) {
					msg += ", ";
				}
				msg += m;
			}
			sendFail(req, resp, msg);
		} catch (Exception e) {
			sendFail(req, resp, e.getMessage());
			return;
		} finally {
			if (session.isOpen()) {
				session.close();
			}
		}
	}

	public static User getUser(String emailId, Long serverCompanyID) {
		if (serverCompanyID == null) {
			return null;
		}
		Session session = HibernateUtil.getCurrentSession();
		Query namedQuery = session
				.getNamedQuery("getUser.by.mailId.and.companyId");
		namedQuery.setParameter("emailId", emailId).setParameter("companyId",
				serverCompanyID);
		User user = (User) namedQuery.uniqueResult();
		return user;
	}

	private boolean isAuthenticated(String authentication) {

		return true;
	}

	protected void sendFail(HttpServletRequest req, HttpServletResponse resp,
			String result) {
		try {
			ApiResult apiResult = new ApiResult();
			apiResult.setStatus(ApiResult.FAIL);
			apiResult.setResult(result);
			sendData(req, resp, apiResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendData(HttpServletRequest req, HttpServletResponse resp,
			ApiResult result) {
		updateDeveloper(req, result.getStatus());
		try {
			ApiSerializationFactory factory = getSerializationFactory(req);
			String string = factory.serialize(result);
			ServletOutputStream outputStream;
			outputStream = resp.getOutputStream();
			outputStream.write(string.getBytes());
			outputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateDeveloper(HttpServletRequest req, int status) {
		Developer developer = null;
		long id = ((Long) req.getAttribute("id")).longValue();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			developer = (Developer) session.get(Developer.class, id);
			if (status == ApiResult.SUCCESS) {
				developer.succeedRequests++;
			} else {
				developer.failureRequests++;
			}
			session.saveOrUpdate(developer);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}

	}

	public static ApiSerializationFactory getSerializationFactory(
			HttpServletRequest req) throws ServletException {
		String string = getNameFromReq(req, 2);
		if (string.equals("xml")) {
			return new ApiSerializationFactory(false);
		} else if (string.equals("json")) {
			return new ApiSerializationFactory(true);
		}
		throw new ServletException("Wrong Stream Formate");
	}

	private static String getNameFromReq(HttpServletRequest req,
			int indexFromLast) {
		String url = req.getRequestURI();
		String[] urlParts = url.split("/");
		String last = urlParts[urlParts.length - indexFromLast];
		return last;
	}

}
