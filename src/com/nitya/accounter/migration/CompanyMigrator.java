package com.nitya.accounter.migration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.AccounterClass;
import com.nitya.accounter.core.BankAccount;
import com.nitya.accounter.core.CashPurchase;
import com.nitya.accounter.core.CashSales;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.CompanyPreferences;
import com.nitya.accounter.core.CreditCardCharge;
import com.nitya.accounter.core.Currency;
import com.nitya.accounter.core.Customer;
import com.nitya.accounter.core.CustomerCreditMemo;
import com.nitya.accounter.core.CustomerGroup;
import com.nitya.accounter.core.CustomerPrePayment;
import com.nitya.accounter.core.CustomerRefund;
import com.nitya.accounter.core.Depreciation;
import com.nitya.accounter.core.EnterBill;
import com.nitya.accounter.core.Estimate;
import com.nitya.accounter.core.FixedAsset;
import com.nitya.accounter.core.IAccounterServerCore;
import com.nitya.accounter.core.InventoryAssembly;
import com.nitya.accounter.core.Invoice;
import com.nitya.accounter.core.Item;
import com.nitya.accounter.core.ItemGroup;
import com.nitya.accounter.core.Job;
import com.nitya.accounter.core.JournalEntry;
import com.nitya.accounter.core.Location;
import com.nitya.accounter.core.MakeDeposit;
import com.nitya.accounter.core.Measurement;
import com.nitya.accounter.core.PayBill;
import com.nitya.accounter.core.PaymentTerms;
import com.nitya.accounter.core.PriceLevel;
import com.nitya.accounter.core.PurchaseOrder;
import com.nitya.accounter.core.ReceivePayment;
import com.nitya.accounter.core.ReceiveVAT;
import com.nitya.accounter.core.SalesPerson;
import com.nitya.accounter.core.ShippingMethod;
import com.nitya.accounter.core.ShippingTerms;
import com.nitya.accounter.core.TAXAgency;
import com.nitya.accounter.core.TAXCode;
import com.nitya.accounter.core.TAXGroup;
import com.nitya.accounter.core.TAXItem;
import com.nitya.accounter.core.TAXRateCalculation;
import com.nitya.accounter.core.TDSChalanDetail;
import com.nitya.accounter.core.TDSDeductorMasters;
import com.nitya.accounter.core.TDSResponsiblePerson;
import com.nitya.accounter.core.Transaction;
import com.nitya.accounter.core.TransferFund;
import com.nitya.accounter.core.User;
import com.nitya.accounter.core.Vendor;
import com.nitya.accounter.core.VendorCreditMemo;
import com.nitya.accounter.core.VendorGroup;
import com.nitya.accounter.core.VendorPrePayment;
import com.nitya.accounter.core.Warehouse;
import com.nitya.accounter.core.WriteCheck;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.core.MigrateDetails;

public class CompanyMigrator {
	public static final String ECGINE_URL = "http://nitya.ecgine.com:80";
	private static final String ECGINE_REST_BASE = "/org.ecgine.restapi/u/api/v1";
	private static final String ECGINE_REST_OBJECTS = "/objects";
	private static final String ECGINE_REST_SINGLETON = "/singleton/";
	private static final String LISTS = "/lists/";
	private static final String ACESS_TOKEN = "access_token";
	private static final String FIRST_NAME = "firstname";
	public static final String API_BASE_URL = "/api/v1";
	private static final String LAST_NAME = "lastname";
	private static final String EMAIL = "email";
	private static final String CLIENT_ID = "client_id";
	private static final String CLIENT_SECRET = "client_secret";
	private static final String DOMAIN = "domainName";
	private static final String COMPANY_NAME = "companyname";
	private static final String COUNTRY = "country";
	private static final String DEVICE_ID = "device_id";
	private static final String PASS_WORD = "password";
	private static final String SIGNUP_CREATE_ORG = "/com.nitya.ecgine.master/accounter/signup";
	private static final String LOG_IN = "/org.ecgine.core.server/desktop/login";
	private static final String AUTH_HEADER_NAME = "Authorization";
	private static final String BEARER = "Bearer";
	public static final String USER_ID = "userid";
	public static final String ECGINE_REST_PACKAGE_INSTALL = "/api/v1/package/install";
	private static final String PKG_NAME = "pkgName";
	private static final String PKG_VERSION = "pkgVersion";
	private static final long DELAY_BETWEEN_TASKS = 10 * 1000;
	private static final String ECGINE_REST_JOB_STATUS = "/api/v1/job/status";
	private static final String JOBID = "jobId";
	public static final String ORGANIZATION_ID = "orgid";
	public static final String ECGINE_LIST = "/lists/";
	private MigratorContext context;
	public static final long COMMON_SETTINGS_OLD_ID = 1;
	public static final long FEATURES_OLD_ID = 2;
	public static final long CUSTOMER_AND_SALES_SETTINGS_OLD_ID = 3;
	public static final long TDS_DEDUCTOR_MASTERS_OLD_ID = 4;
	public static final long TDS_RESPONSIBLE_PERSON_OLD_ID = 5;
	public static final String COMMON_SETTINGS = "com.nitya.accounter.shared.common.CommonSettings";
	public static final String TDS_DEDUCTOR_MASTERS = "com.nitya.accounter.shared.tax.TDSDeductorMasters";
	public static final String TDS_RESPONSIBLE_PERSON = "com.nitya.accounter.shared.tax.TDSResponsiblePerson";
	public static final String FEATURES = "com.nitya.accounter.shared.common.Features";
	public static final String CUSTOMER_AND_SALES_SETTINGS = "com.nitya.accounter.shared.customer.CustomerAndSalesSettings";
	public static final String ROLE_FULL_QULIFIED_NAME = "org.ecgine.core.shared.Role";
	private String ECGINE_URL_LOGIN;

	private static Logger log = Logger.getLogger(CompanyMigrator.class);

	private Company company;
	private HttpClient client;
	private String accessToken;
	private MigrateDetails details;

	public CompanyMigrator(Company company, MigrateDetails details) {
		this.company = company;
		this.details = details;
		this.client = HttpClientBuilder.create().build();
	}

	public void migrate() throws Exception {
		log.info("***Migrating Company  :" + company.getTradingName()
				+ " @@ID : " + company.getId());
		// Create context
		context = new MigratorContext();
		context.setLocalIdProvider(new LocalIdProvider());
		context.setCompany(company);
		// Organization
		User user = company.getCreatedBy();
		signup(user);
		ECGINE_URL_LOGIN = "http://" + details.getDomain() + ".ecgine.com:80";
		login(user);
		migrateAllObjects();

	}

	private void migrateAllObjects() throws HttpException, JSONException,
			IOException {

		// Currency
		Map<Long, Long> migratedObjects = migrateObjects("Currency",
				Currency.class, new CurrencyMigrator(), context);
		context.put("Currency", migratedObjects);
		// Measurements
		migratedObjects = migrateObjects("Measurement", Measurement.class,
				new MeasurementMigrator(), context);
		context.put("Measurement", migratedObjects);
		// Warehouse
		migratedObjects = migrateObjects("Warehouse", Warehouse.class,
				new WarehouseMigrator(), context);
		context.put("Warehouse", migratedObjects);
		// CommonSettings
		migratedObjects = migrateObjects(COMMON_SETTINGS,
				CompanyPreferences.class, new DefaultCommonSettingsMigrator(),
				context);
		// DefaultAccounts
		migratedObjects = migrateObjects("Account", Account.class,
				new DefaultAccountsMigrator(), context);
		context.put("Account", migratedObjects);

		// Seting Default References to CommonSettings
		migratedObjects = migrateObjects(COMMON_SETTINGS,
				CompanyPreferences.class, new CommonSettingsMigrator(), context);

		// MultiCurrencyMigrator
		if (company.getPreferences().isEnabledMultiCurrency()) {
			migratedObjects = migrateObjects(COMMON_SETTINGS,
					CompanyPreferences.class,
					new AccountingCurrenciesMigrator(), context);
		}
		// Accounts
		migratedObjects = migrateObjects("Account", Account.class,
				new AccountMigrator(), context);
		context.put("Account", migratedObjects);
		// BankAccount
		migratedObjects = migrateObjects("BankAccount", BankAccount.class,
				new BankAccountMigrator(), context);
		context.put("Account", migratedObjects);
		// paymentTerms
		migratedObjects = migrateObjects("PaymentTerm", PaymentTerms.class,
				new PaymentTermsMigrator(), context);
		context.put("PaymentTerms", migratedObjects);
		// taxAgencies
		migratedObjects = migrateObjects("TaxAgency", TAXAgency.class,
				new TaxAgencyMigrator(), context);
		context.put("TaxAgency", migratedObjects);
		// salesPersons
		migratedObjects = migrateObjects("SalesPerson", SalesPerson.class,
				new SalesPersonMigrator(), context);
		context.put("SalesPerson", migratedObjects);
		// Customer Groups
		migratedObjects = migrateObjects("CustomerGroup", CustomerGroup.class,
				new CustomerGroupMigrator(), context);
		context.put("CustomerGroup", migratedObjects);
		// Shipping Methods
		migratedObjects = migrateObjects("ShippingMethod",
				ShippingMethod.class, new ShippingMethodMigrator(), context);
		context.put("ShippingMethod", migratedObjects);
		// Shipping Methods
		migratedObjects = migrateObjects("ShippingTerms", ShippingTerms.class,
				new ShippingTermsMigrator(), context);
		context.put("ShippingTerms", migratedObjects);
		// PriceLevels
		migratedObjects = migrateObjects("PriceLevel", PriceLevel.class,
				new PriceLevelMigrator(), context);
		context.put("PriceLevel", migratedObjects);
		// locations
		migratedObjects = migrateObjects("Location", Location.class,
				new LocationMigrator(), context);
		context.put("Location", migratedObjects);
		// AccounterClasses
		migratedObjects = migrateObjects("AccountClass", AccounterClass.class,
				new AccounterClassMigrator(), context);
		context.put("AccounterClass", migratedObjects);
		// Item groups
		migratedObjects = migrateObjects("ItemGroup", ItemGroup.class,
				new ItemGroupMigrator(), context);
		context.put("ItemGroup", migratedObjects);
		// taxitems
		migratedObjects = migrateObjects("TaxItem", TAXItem.class,
				new TaxItemMigrator(), context);
		context.put("Tax", migratedObjects);
		// TaxGroups
		migratedObjects = migrateObjects("TaxGroup", TAXGroup.class,
				new TAXGroupMigrator(), context);
		context.put("Tax", migratedObjects);
		// taxCodes
		migratedObjects = migrateObjects("TaxCode", TAXCode.class,
				new TAXCodeMigrator(), context);
		context.put("TaxCode", migratedObjects);
		// CompanyDefatultTaxCodeMigration
		migratedObjects = migrateObjects(COMMON_SETTINGS,
				CompanyPreferences.class, new CompanyDefatultTaxCodeMigrator(),
				context);
		// Customers
		migratedObjects = migrateObjects("Customer", Customer.class,
				new CustomerMigrator(), context);
		context.put("Customer", migratedObjects);
		// Vendor Groups
		migratedObjects = migrateObjects("VendorGroup", VendorGroup.class,
				new VendorGroupMigrator(), context);
		context.put("VendorGroup", migratedObjects);
		// Vendor
		migratedObjects = migrateObjects("Vendor", Vendor.class,
				new VendorMigrator(), context);
		context.put("Vendor", migratedObjects);
		// Jobs
		migratedObjects = migrateObjects("Project", Job.class,
				new JobMigrator(), context);
		context.put("Job", migratedObjects);
		// JournalEntries
		migratedObjects = migrateObjects("JournalEntry", JournalEntry.class,
				new JournalEntryMigrator(), context);
		context.put("JournalEntry", migratedObjects);
		// Items
		migratedObjects = migrateObjects("ServiceItem", Item.class,
				new ServiceItemMigrator(), context);
		context.put("Item", migratedObjects);
		// Items
		migratedObjects = migrateObjects("ProductItem", Item.class,
				new ProductItemMigrator(), context);
		context.put("Item", migratedObjects);
		// CustomerAndSalesSettingsMigrator
		migratedObjects = migrateObjects(CUSTOMER_AND_SALES_SETTINGS,
				CompanyPreferences.class,
				new DefaultCustomerAndSalesSettingsMigrator(), context);
		// FeaturesMigrator
		migratedObjects = migrateObjects(FEATURES, CompanyPreferences.class,
				new DefaultFeaturesMigrator(), context);
		// Items
		migratedObjects = migrateObjects("InventoryItem", Item.class,
				new InventoryItemMigrator(), context);
		context.put("Item", migratedObjects);
		// Items
		migratedObjects = migrateObjects("InventoryAssembly",
				InventoryAssembly.class, new InventoryAssemblyMigrator(),
				context);
		context.put("Item", migratedObjects);
		// Build Assembly
		// migratedObjects = migrateObjects("BuildAssembly",
		// BuildAssembly.class, new BuildAssemblyMigrator(), context);
		// context.put("BuildAssembly", migratedObjects);
		// SalesQuotation
		migratedObjects = migrateObjects("SalesQuotation", Estimate.class,
				new SalesQuotationMigrator(), context);
		context.put("SalesQuotation", migratedObjects);
		// SalesOrder
		migratedObjects = migrateObjects("SalesOrder", Estimate.class,
				new SalesOrderMigrator(), context);
		context.put("SalesOrder", migratedObjects);
		// Credits
		migratedObjects = migrateObjects("Credit", Estimate.class,
				new CreditsMigrator(), context);
		context.put("Credit", migratedObjects);

		// PurchaseOrder
		migratedObjects = migrateObjects("PurchaseOrder", PurchaseOrder.class,
				new PurchaseOrderMigrator(), context);
		context.put("PurchaseOrder", migratedObjects);

		// EnterBill
		migratedObjects = migrateObjects("EnterBill", EnterBill.class,
				new EnterBillMigrator(), context);
		context.put("EnterBill", migratedObjects);

		// Invoice
		migratedObjects = migrateObjects("Invoice", Invoice.class,
				new InvoiceMigrator(), context);
		context.put("Invoice", migratedObjects);
		// CustomerPrepayment
		migratedObjects = migrateObjects("CustomerPrepayment",
				CustomerPrePayment.class, new CustomerPrepaymentMigrator(),
				context);
		context.put("CustomerPrepayment", migratedObjects);
		// CustomerCreditMemo
		migratedObjects = migrateObjects("CreditMemo",
				CustomerCreditMemo.class, new CreditMemoMigrator(), context);
		context.put("CreditMemo", migratedObjects);
		// ReceivePayment
		boolean tdsApplicable = context.getCompany().getCountry()
				.equals("India");
		if (tdsApplicable) {

			Map<Long, Long> allReceivePaymentOLDandNEWIDs = new HashMap<Long, Long>();

			// Migrating Only TDS ReceivePayments
			migratedObjects = migrateReceivePaymentOrPayBill(context,
					"ReceivePayment", ReceivePayment.class,
					new ReceivePaymentMigrator(), true);
			allReceivePaymentOLDandNEWIDs.putAll(migratedObjects);

			// Migrating Only non TDS ReceivePayments
			migratedObjects = migrateReceivePaymentOrPayBill(context,
					"ReceivePayment", ReceivePayment.class,
					new ReceivePaymentMigrator(), false);
			allReceivePaymentOLDandNEWIDs.putAll(migratedObjects);

			context.put("ReceivePayment", allReceivePaymentOLDandNEWIDs);

			// put Back to Present State
			migrateVendorOrCustomer(context, "Customer", Customer.class,
					new CustomerTdsEnableMigrator(true));

		} else {
			migratedObjects = migrateObjects("ReceivePayment",
					ReceivePayment.class, new ReceivePaymentMigrator(), context);
			context.put("ReceivePayment", migratedObjects);
		}
		// CashSale
		migratedObjects = migrateObjects("CashSale", CashSales.class,
				new CashSaleMigrator(), context);
		context.put("CashSale", migratedObjects);
		// CashPurchase
		migratedObjects = migrateObjects("CashPurchase", CashPurchase.class,
				new CashPurchaseMigrator(), context);
		context.put("CashPurchase", migratedObjects);
		// Credit Card Expense(Record Expense Type)
		migratedObjects = migrateObjects("PurchaseExpense",
				CreditCardCharge.class, new CreditCardExpenseMigrator(),
				context);
		context.put("PurchaseExpense", migratedObjects);
		// CashExpense(Record Expense Type)
		migratedObjects = migrateObjects("PurchaseExpense", CashPurchase.class,
				new CashExpenseMigrator(), context);
		context.put("PurchaseExpense", migratedObjects);
		// VendorPrepayment
		migratedObjects = migrateObjects("VendorPrepayment",
				VendorPrePayment.class, new VendorPrepaymentMigrator(), context);
		context.put("VendorPrepayment", migratedObjects);
		// DebitNote
		migratedObjects = migrateObjects("DebitNote", VendorCreditMemo.class,
				new DebitNoteMigrator(), context);
		context.put("DebitNote", migratedObjects);
		// PayBill
		if (tdsApplicable) {
			Map<Long, Long> allObjects = new HashMap<Long, Long>();

			// Migrating Only TDS Pay Bills
			migratedObjects = migrateReceivePaymentOrPayBill(context,
					"PayBill", PayBill.class, new PayBillMigrator(), true);
			allObjects.putAll(migratedObjects);

			// Migrating Only NON TDS Pay Bills
			migratedObjects = migrateReceivePaymentOrPayBill(context,
					"PayBill", PayBill.class, new PayBillMigrator(), false);
			allObjects.putAll(migratedObjects);

			context.put("PayBill", allObjects);

			// put Back to Present State
			migrateVendorOrCustomer(context, "Vendor", Vendor.class,
					new VendorTdsEnableMigrator(true));
		} else {
			migratedObjects = migrateObjects("PayBill", PayBill.class,
					new PayBillMigrator(), context);
			context.put("PayBill", migratedObjects);
		}
		// CustomerRefund
		migratedObjects = migrateObjects("CustomerRefund",
				CustomerRefund.class, new CustomerRefundMigrator(), context);
		context.put("CustomerRefund", migratedObjects);
		// MakeDeposit
		migratedObjects = migrateObjects("MakeDeposit", MakeDeposit.class,
				new MakeDepositMigrator(), context);
		context.put("MakeDeposit", migratedObjects);
		// TransferFund
		migratedObjects = migrateObjects("TransferFund", TransferFund.class,
				new TransferFundMigrator(), context);
		context.put("TransferFund", migratedObjects);
		// WriteCheck
		migratedObjects = migrateObjects("WriteCheck", WriteCheck.class,
				new WriteCheckMigrator(), context);
		context.put("WriteCheck", migratedObjects);
		// FixedAsset
		migratedObjects = migrateObjects("FixedAsset", FixedAsset.class,
				new FixedAssetMigrator(), context);
		context.put("FixedAsset", migratedObjects);
		// Depreciation
		migratedObjects = migrateObjects("Depreciation", Depreciation.class,
				new DepreciationMigrator(), context);
		context.put("Depreciation", migratedObjects);
		// TDSDeductorMasters
		migratedObjects = migrateObjects(TDS_DEDUCTOR_MASTERS,
				TDSDeductorMasters.class, new TDSDeductorMastersMigrator(),
				context);
		context.put("TDSDeductorMasters", migratedObjects);
		// TDSResponsiblePerson
		migratedObjects = migrateObjects(TDS_RESPONSIBLE_PERSON,
				TDSResponsiblePerson.class, new TDSResponsiblePersonMigrator(),
				context);
		context.put("TDSResponsiblePerson", migratedObjects);
		// TDSChalan
		migratedObjects = migrateObjects("TdsChallan", TDSChalanDetail.class,
				new TdsChallanMigrator(), context);
		context.put("TdsChallan", migratedObjects);
		// // FileTax
		// migratedObjects = migrateObjects("FileTax", TAXReturn.class,
		// new FileTaxMigrator(), context);
		// context.put("FileTax", migratedObjects);
		// // PayTAX
		// migratedObjects = migrateObjects("PayTAX", PayTAX.class,
		// new PayTaxMigrator(), context);
		// context.put("PayTAX", migratedObjects);
		// TaxRefund
		migratedObjects = migrateObjects("TaxRefund", ReceiveVAT.class,
				new TaxRefundMigrator(), context);
		context.put("TaxRefund", migratedObjects);
		getAdminRole();
		// USERS
		migratedObjects = migrateObjects("User", User.class,
				new UserMigrator(), context);
		context.put("User", migratedObjects);
		// ROLES MEMBERSHIP
		migratedObjects = migrateObjects("RoleMembership", User.class,
				new RoleMembershipMigrator(), context);
		context.put("RoleMembership", migratedObjects);

		updateMigrationStatus();
	}

	private void getAdminRole() {
		HttpGet get = new HttpGet(ECGINE_URL_LOGIN + ECGINE_REST_BASE + LISTS
				+ ROLE_FULL_QULIFIED_NAME);
		addAuthenticationParameters(get);
		get.setHeader("Content-type", "application/json");
		try {
			HttpResponse response = client.execute(get);
			StatusLine status = response.getStatusLine();
			HttpEntity entity = response.getEntity();
			if (status.getStatusCode() != HttpStatus.SC_OK) {
				if (entity != null) {
					EntityUtils.consume(entity);
				}
			}
			String content = IOUtils.toString(entity.getContent());
			JSONArray roles = new JSONArray(content);
			context.setAdminRole("AdminRole", (Long) roles.get(0));
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public void installPackage(String pkgName, String pkgVersion)
			throws Exception {
		// Creating HTTP request post method.

		HttpPost post = new HttpPost(ECGINE_URL + ECGINE_REST_PACKAGE_INSTALL);

		List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair(PKG_NAME, pkgName));
		postParameters.add(new BasicNameValuePair(PKG_VERSION, pkgVersion));
		post.setEntity(new UrlEncodedFormEntity(postParameters));

		// Adding authentication parameters
		addAuthenticationParameters(post);

		// Executing method
		HttpResponse response = client.execute(post);

		StatusLine status = response.getStatusLine();

		if (status.getStatusCode() != HttpStatus.SC_OK) {
			throw new RuntimeException(status.toString());
		}
		HttpEntity entity = response.getEntity();
		// Read Response
		JSONObject jsonResult = new JSONObject(IOUtils.toString(entity
				.getContent()));
		showPolloingStatus(jsonResult.getLong("jobId"), "Install Package");
		migrateAllObjects();
	}

	private void showPolloingStatus(long jobId, String note) {
		Timer pollingTimer = new Timer();
		JobPollingTask task = new JobPollingTask(jobId, note, pollingTimer);
		pollingTimer.scheduleAtFixedRate(task, new Date(), DELAY_BETWEEN_TASKS);
		try {
			synchronized (pollingTimer) {
				pollingTimer.wait();
			}
		} catch (InterruptedException e) {
			log.error("Error while waiting to install accounter package", e);
			throw new RuntimeException(e);
		}
	}

	class JobPollingTask extends TimerTask {
		private long jobId;
		private String note;
		private Timer pollingTimer;

		public JobPollingTask(long jobId, String note, Timer pollingTimer) {
			this.jobId = jobId;
			this.note = note;
			this.pollingTimer = pollingTimer;
		}

		@Override
		public void run() {
			try {
				JSONObject jsonResult = showStatus(jobId);
				String status = (String) jsonResult.get("status");
				if (jsonResult.has("extStatus")) {
					String extStatus = (String) jsonResult.get("extStatus");
					log.info("Status of " + note + " - " + status + ";"
							+ extStatus);
				} else {
					log.info("Status of " + note + " - " + status);
				}
				if ("Aborted".equals(status) || "Failed".equals(status)
						|| "Completed".equals(status)) {
					log.info("Job " + note + " Completed!!");
					synchronized (pollingTimer) {
						pollingTimer.notifyAll();
					}
					pollingTimer.cancel();
				}
			} catch (Exception e) {
				log.error("Error while polling job status with jobId " + jobId,
						e);
			}
		}
	}

	private JSONObject showStatus(long jobId) throws Exception {
		// Creating HTTP request post method.
		HttpPost post = new HttpPost(ECGINE_URL + ECGINE_REST_JOB_STATUS);

		List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters
				.add(new BasicNameValuePair(JOBID, String.valueOf(jobId)));
		post.setEntity(new UrlEncodedFormEntity(postParameters));

		// Adding authentication parameters
		addAuthenticationParameters(post);

		// Executing method
		HttpResponse response = client.execute(post);

		StatusLine result = response.getStatusLine();

		if (result.getStatusCode() != HttpStatus.SC_OK) {
			throw new RuntimeException(result.toString());
		}

		HttpEntity entity = response.getEntity();
		// Read Response
		JSONObject jsonResult = new JSONObject(IOUtils.toString(entity
				.getContent()));
		return jsonResult;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T extends IAccounterServerCore> Map<Long, Long> migrateVendorOrCustomer(
			MigratorContext context, String identity, Class clazz,
			IMigrator<T> migrator) throws JSONException, HttpException,
			IOException {
		List<Long> ids = new ArrayList<Long>();
		Session session = HibernateUtil.getCurrentSession();
		JSONArray objectArray1 = new JSONArray();
		Criteria criteria = session.createCriteria(clazz, "obj");
		migrator.addRestrictions(criteria);
		List<T> objects = new ArrayList<T>();
		objects = criteria.add(Restrictions.eq("company", company)).list();
		if (objects.isEmpty()) {
			return new HashMap<Long, Long>();
		}
		for (T obj : objects) {
			JSONObject migrate = migrator.migrate(obj, context);
			if (migrate != null) {
				objectArray1.put(migrate);
				ids.add(obj.getID());
			}
		}
		// Send Request TO REST API
		return setRequestToRestApi(identity, context, objectArray1, ids, null);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T extends IAccounterServerCore> Map<Long, Long> migrateReceivePaymentOrPayBill(
			MigratorContext context, String identity, Class clazz,
			IMigrator<T> migrator, boolean isMigrateTDSTxnOnly)
			throws JSONException, HttpException, IOException {
		boolean isReceivePayment = identity.equals("ReceivePayment");
		List<Long> companyIds = new ArrayList<Long>();
		companyIds.add(context.getCompany().getID());
		if (isMigrateTDSTxnOnly) {
			if (isReceivePayment) {
				migrateVendorOrCustomer(context, "Customer", Customer.class,
						new CustomerTdsEnableMigrator(false));
			} else {
				migrateVendorOrCustomer(context, "Vendor", Vendor.class,
						new VendorTdsEnableMigrator(false));
			}
		}
		// Accounter Live Receive Payment and PayBill Ids
		List<Long> ids = new ArrayList<Long>();
		// get Receive Payments or Pay Bills
		Session session = HibernateUtil.getCurrentSession();
		JSONArray objectArray1 = new JSONArray();
		Criteria criteria = session.createCriteria(clazz, "obj");
		migrator.addRestrictions(criteria);
		List<T> objects = new ArrayList<T>();
		if (isMigrateTDSTxnOnly) {
			objects = criteria.add(Restrictions.eq("company", company))
					.add(Restrictions.ne("tdsTotal", 0D)).list();
		} else {
			objects = criteria.add(Restrictions.eq("company", company))
					.add(Restrictions.eq("tdsTotal", 0D)).list();
		}
		if (objects.isEmpty()) {
			return new HashMap<Long, Long>();
		}
		for (T obj : objects) {
			JSONObject migrate = migrator.migrate(obj, context);
			if (migrate != null) {
				objectArray1.put(migrate);
				ids.add(obj.getID());
			}
		}
		// Send Request TO REST API
		return setRequestToRestApi(identity, context, objectArray1, ids, null);
	}

	private void addAuthenticationParameters(HttpRequest request) {
		request.addHeader(AUTH_HEADER_NAME, BEARER + " " + accessToken);
	}

	@SuppressWarnings("unchecked")
	private <T extends IAccounterServerCore> Map<Long, Long> migrateObjects(
			String identity, Class<T> clazz, IMigrator<T> migrator,
			MigratorContext context) throws JSONException, HttpException,
			IOException {
		updateMigrationStatus("Migrating " + identity + "s started");
		List<Long> ids = new ArrayList<Long>();
		// Customer Credit And Vendor Debit Accounter Live Id's
		List<Long> applyCreditIds = new ArrayList<Long>();
		Session session = HibernateUtil.getCurrentSession();
		JSONArray objectArray = new JSONArray();
		Criteria criteria = session.createCriteria(clazz, "obj");
		migrator.addRestrictions(criteria);
		List<T> objects = new ArrayList<T>();
		if (clazz.equals(CompanyPreferences.class)) {
			objects.add((T) company.getPreferences());
		} else {
			objects = criteria.add(Restrictions.eq("company", company)).list();
		}

		if (objects.isEmpty()) {
			return new HashMap<Long, Long>();
		}

		for (T obj : objects) {
			JSONObject migrate = migrator.migrate(obj, context);
			if (migrate != null) {
				objectArray.put(migrate);
				ids.add(obj.getID());
				// Adding Customer Credit And Vendor Debit Id's to Context
				if (obj instanceof CustomerPrePayment
						|| obj instanceof CustomerCreditMemo
						|| obj instanceof VendorPrePayment
						|| obj instanceof VendorCreditMemo) {
					Transaction txn = (Transaction) obj;
					applyCreditIds.add(txn.getCreditsAndPayments().getID());
				}
			}
		}
		if (!clazz.getName().equals("CreditCardCharge")) {
			context.setMaxTXNNumber(null);
		}
		// Send Request TO REST API
		return setRequestToRestApi(identity, context, objectArray, ids,
				applyCreditIds);
	}

	// here if we are sending already inserted objects to update then ids should
	// be null.Because those are old database id's. So no need to insert them in
	// context again
	private Map<Long, Long> setRequestToRestApi(String identity,
			MigratorContext context, JSONArray objectArray, List<Long> ids,
			List<Long> customerCreditIds) throws JSONException, HttpException,
			IOException {

		Map<Long, Long> newAndOldIds = new HashMap<Long, Long>();

		// Setting SingleTon id's
		if (identity.equals(COMMON_SETTINGS)
				&& context.get(COMMON_SETTINGS, COMMON_SETTINGS_OLD_ID) == null) {
			if (!setSingleTonId(identity, newAndOldIds, objectArray,
					COMMON_SETTINGS_OLD_ID)) {
				return newAndOldIds;
			}
		} else if (identity.equals(CUSTOMER_AND_SALES_SETTINGS)
				&& context.get(CUSTOMER_AND_SALES_SETTINGS,
						CUSTOMER_AND_SALES_SETTINGS_OLD_ID) == null) {
			if (!setSingleTonId(identity, newAndOldIds, objectArray,
					CUSTOMER_AND_SALES_SETTINGS_OLD_ID)) {
				return newAndOldIds;
			}
		} else if (identity.equals(FEATURES)
				&& context.get(FEATURES, FEATURES_OLD_ID) == null) {
			if (!setSingleTonId(identity, newAndOldIds, objectArray,
					FEATURES_OLD_ID)) {
				return newAndOldIds;
			}
		} else if (identity.equals(TDS_DEDUCTOR_MASTERS)
				&& context.get(TDS_DEDUCTOR_MASTERS,
						TDS_DEDUCTOR_MASTERS_OLD_ID) == null) {
			if (!setSingleTonId(identity, newAndOldIds, objectArray,
					TDS_DEDUCTOR_MASTERS_OLD_ID)) {
				return newAndOldIds;
			}
		} else if (identity.equals(TDS_RESPONSIBLE_PERSON)
				&& context.get(TDS_RESPONSIBLE_PERSON,
						TDS_RESPONSIBLE_PERSON_OLD_ID) == null) {
			if (!setSingleTonId(identity, newAndOldIds, objectArray,
					TDS_RESPONSIBLE_PERSON_OLD_ID)) {
				return newAndOldIds;
			}
		}
		HttpPost post = new HttpPost(ECGINE_URL_LOGIN + ECGINE_REST_BASE
				+ ECGINE_REST_OBJECTS);
		addAuthenticationParameters(post);
		post.setHeader("Content-type", "application/json");
		post.setEntity(new StringEntity(objectArray.toString()));
		HttpResponse response = client.execute(post);
		StatusLine status = response.getStatusLine();
		HttpEntity entity = response.getEntity();

		if (status.getStatusCode() != HttpStatus.SC_OK) {
			if (entity != null) {
				String content = IOUtils.toString(entity.getContent());
				try {
					JSONArray array = new JSONArray(content);
					for (int i = 0; i < array.length(); i++) {
						JSONObject json = array
								.getJSONObject(i)
								.getJSONObject(
										"org.ecgine.serialization.impl.ObjectWraper")
								.getJSONObject("obj");
						boolean success = json.getBoolean("obj");
						if (success) {
							Long id = json.getLong("id");
							Long oldId = ids.get(i);
							newAndOldIds.put(oldId, id);
							addCustomerAndVendorCreditsToContext(json,
									customerCreditIds, i, context);
							log.info("Migrated "
									+ identity
									+ (ids != null ? "  Accounter ID :" + oldId
											: "") + " Ecgine ID :" + id);
						} else {
							if (json.has("errors")) {
								String error = json.getJSONArray("errors")
										.toString();
								if (!error.equals("[]")) {
									log.info("\n" + "Found Errors In "
											+ identity + error);
								}
							}
						}
					}
				} catch (Exception e) {
					log.error("Error Occurred in server while saving "
							+ identity);
				}
			}
			return newAndOldIds;
			// throw new RuntimeException(status.toString());
		}
		String content = IOUtils.toString(entity.getContent());
		JSONArray array = new JSONArray(content);
		Map<String, List<Long>> ecgineMap = new HashMap<String, List<Long>>();
		Map<String, List<Long>> accounterMap = context.getChildrenMap();
		for (int i = 0; i < array.length(); i++) {
			JSONObject dbResult = array.getJSONObject(i).getJSONObject(
					"org.ecgine.serialization.impl.ObjectWraper");
			JSONObject json = dbResult.getJSONObject("obj");
			if (json.has("id")) {
				long id = json.getLong("id");
				if (ids != null) {
					newAndOldIds.put(ids.get(i), id);
				}
			}
			String childrenName = context.getChildrenName(identity);
			JSONObject internal = json.getJSONObject("obj");
			if (childrenName != null
					&& internal.has(context.getChildrenName(identity))
					&& internal.get(childrenName) instanceof JSONObject) {
				JSONObject jsonObject = internal.getJSONObject(childrenName);
				if (!jsonObject.has("@noValue")) {
					addCustomerAndVendorCreditsToContext(jsonObject,
							customerCreditIds, i, context);
					createEcgineChildrenMap(accounterMap, ecgineMap, jsonObject);
				}
			}
			if (internal.has("taxCalculations")) {
				prepareTaxRateCaluculations(ids.get(i), internal);
			}
			// Printing Errors
			if (json.has("errors")) {
				JSONArray errors = json.getJSONArray("errors");
				if (errors.getJSONObject(0).getInt("@size") != 0) {
					for (int j = 0; j < errors.length(); j++) {
						String error = errors.getJSONObject(j).toString();
						if (!error.equals("[]")) {
							log.info("\n" + "Found Errors In " + identity
									+ error);
						}
					}
				} else {
					log.info("Migrated "
							+ identity
							+ (ids != null ? "  Accounter ID :" + ids.get(i)
									: "") + " Ecgine ID :" + json.getLong("id"));
				}
			}
		}
		if (!ecgineMap.isEmpty()) {
			putChildrenInContext(accounterMap, ecgineMap, context);
		}
		Set<TAXRateCalculation> empty = new HashSet<>();
		context.setTaxRateCalculations(empty);
		updateMigrationStatus("Migrating " + identity + "s finished");
		return newAndOldIds;
	}

	/**
	 * Adding Context new and old taxRateCalculation
	 * 
	 * @param ecgineTXNId
	 * @param internal
	 * @throws JSONException
	 */
	private void prepareTaxRateCaluculations(Long ecgineTXNId,
			JSONObject internal) throws JSONException {
		Object object = internal.get("taxCalculations");
		if (object instanceof String) {
			return;
		}
		JSONObject taxCalculations = (JSONObject) object;
		if (taxCalculations.has("@noValue")) {
			return;
		}
		object = taxCalculations
				.get("com.nitya.accounter.shared.common.TaxCalculation");
		if (object instanceof JSONArray) {
			JSONArray taxcalculations = (JSONArray) object;
			for (int i = 0; i < taxcalculations.length(); i++) {
				JSONObject jsonObject = taxcalculations.getJSONObject(i);
				put(ecgineTXNId, jsonObject);
			}
		} else {
			put(ecgineTXNId, ((JSONObject) object));
		}
	}

	/**
	 * Get taxitem id from taxcalculation json object
	 * 
	 * @param jsonObject
	 * @return
	 * @throws JSONException
	 */
	private Long getTaxItemID(JSONObject jsonObject) throws JSONException {
		JSONObject taxItem = jsonObject.getJSONObject("taxItem").getJSONObject(
				"com.nitya.accounter.shared.tax.TAXITem");
		String taxItemID = taxItem.getString("id");
		return Long.valueOf(taxItemID);
	}

	/**
	 * Get taxRateCalculation id from taxcalculation json object
	 * 
	 * @param jsonObject
	 * @return
	 * @throws JSONException
	 */
	private void put(Long ecgineTXNId, JSONObject taxcalculation)
			throws JSONException {
		JSONObject taxRateCalculation = taxcalculation
				.getJSONObject("taxRateCalculation");
		if (taxRateCalculation.has("@noValue")) {
			return;
		}
		taxRateCalculation = taxRateCalculation
				.getJSONObject("com.nitya.accounter.shared.common.TaxRateCalculation");
		Long taxItemID = getTaxItemID(taxcalculation);
		context.put(ecgineTXNId, taxItemID, taxRateCalculation.getLong("id"));
	}

	/**
	 * Adding Customer Credit And Vendor Debit Id's to Context
	 * 
	 * @param jsonObject
	 * @param customerCreditIds
	 * @param index
	 * @param context
	 * @throws JSONException
	 */
	private void addCustomerAndVendorCreditsToContext(JSONObject jsonObject,
			List<Long> customerCreditIds, int index, MigratorContext context)
			throws JSONException {
		if (jsonObject
				.has("com.nitya.accounter.shared.customer.CustomerCredit")) {
			JSONObject creditObject = jsonObject
					.getJSONObject("com.nitya.accounter.shared.customer.CustomerCredit");
			long customerCreditId = creditObject.getLong("id");
			context.put("CustomerCredit", customerCreditIds.get(index),
					customerCreditId);
		}
		if (jsonObject.has("com.nitya.accounter.shared.vendor.VendorDebit")) {
			JSONObject debitObject = jsonObject
					.getJSONObject("com.nitya.accounter.shared.vendor.VendorDebit");
			long vendorDebitId = debitObject.getLong("id");
			context.put("VendorDebit", customerCreditIds.get(index),
					vendorDebitId);
		}
	}

	private boolean setSingleTonId(String identity,
			Map<Long, Long> newAndOldIds, JSONArray objectArray, long oldId) {
		HttpGet get = new HttpGet(ECGINE_URL_LOGIN + ECGINE_REST_BASE
				+ ECGINE_REST_SINGLETON + identity);
		addAuthenticationParameters(get);
		get.setHeader("Content-type", "application/json");
		try {
			HttpResponse response = client.execute(get);
			StatusLine status = response.getStatusLine();
			HttpEntity entity = response.getEntity();
			if (status.getStatusCode() != HttpStatus.SC_OK) {
				if (entity != null) {
					EntityUtils.consume(entity);
				}
				return false;
			}
			String content = IOUtils.toString(entity.getContent());
			JSONObject json = new JSONObject(content);
			long singleTonId = json.getLong("id");
			HashMap<Long, Long> map = new HashMap<Long, Long>();
			map.put(oldId, singleTonId);
			context.put(identity, map);
			JSONObject jsonObject = objectArray.getJSONObject(0).getJSONObject(
					identity);
			jsonObject.put("@_oid", String.valueOf(singleTonId));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void putChildrenInContext(Map<String, List<Long>> accounterMap,
			Map<String, List<Long>> ecgineMap, MigratorContext context) {
		for (Entry<String, List<Long>> e : accounterMap.entrySet()) {
			String key = e.getKey();
			List<Long> accList = e.getValue();
			if (!ecgineMap.containsKey(key)) {
				continue;
			}
			List<Long> ecgineList = ecgineMap.get(key);
			context.put(key, makeMap(accList, ecgineList));
		}
	}

	private Map<Long, Long> makeMap(List<Long> accList, List<Long> ecgineList) {
		Map<Long, Long> map = new HashMap<Long, Long>();
		for (int i = 0; i < accList.size(); i++) {
			map.put(accList.get(i), ecgineList.get(i));
			if (ecgineList.size() == i + 1) {
				accList.remove(i);
				break;
			}
		}
		return map;
	}

	private void createEcgineChildrenMap(Map<String, List<Long>> accounterMap,
			Map<String, List<Long>> ecgineMap, JSONObject jsonObject)
			throws JSONException {
		for (Entry<String, List<Long>> e : accounterMap.entrySet()) {
			String key = e.getKey();
			if (!jsonObject.has(key)) {
				continue;
			}
			List<Long> list = ecgineMap.get(key);
			if (list == null) {
				list = new ArrayList<Long>();
				ecgineMap.put(key, list);
			}
			Object object = jsonObject.get(key);
			if (object instanceof JSONObject) {
				JSONObject jsonObject2 = jsonObject.getJSONObject(key);
				list.add(jsonObject2.getLong("id"));
			} else {
				JSONArray jsonArray = jsonObject.getJSONArray(key);
				for (int i = 0; i < jsonArray.length(); i++) {
					list.add(jsonArray.getJSONObject(i).getLong("id"));
				}
			}
		}
	}

	private void login(User user) throws JSONException, IOException {
		HttpPost post = new HttpPost(ECGINE_URL_LOGIN + LOG_IN);
		List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		String emailId = user.getClient().getEmailId();
		postParameters.add(new BasicNameValuePair(EMAIL, details.getEmailId()));
		postParameters.add(new BasicNameValuePair(PASS_WORD, details
				.getPassword()));
		postParameters.add(new BasicNameValuePair(CLIENT_ID,
				"accounter-migration"));
		postParameters.add(new BasicNameValuePair(CLIENT_SECRET,
				"accounter-migration"));
		postParameters.add(new BasicNameValuePair(DEVICE_ID,
				"accounter-migration"));
		post.setEntity(new UrlEncodedFormEntity(postParameters));
		HttpResponse response = client.execute(post);
		StatusLine status = response.getStatusLine();
		if (status.getStatusCode() != HttpStatus.SC_OK) {
			throw new RuntimeException(status.toString());
		}
		HttpEntity entity = response.getEntity();
		String responseContent = IOUtils.toString(entity.getContent());
		JSONObject responseResult = new JSONObject(responseContent);
		log.info("Login success for: " + emailId);
		accessToken = responseResult.getString(ACESS_TOKEN);
		log.info("@@@@@@@@ Access Token @@@@@@@@ :" + accessToken);
		updateMigrationStatus("Login success for: " + emailId);
	}

	private void signup(User user) throws HttpException, IOException,
			JSONException, InterruptedException {
		HttpPost post = new HttpPost(ECGINE_URL + SIGNUP_CREATE_ORG);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(EMAIL, details.getEmailId()));
		params.add(new BasicNameValuePair(FIRST_NAME, details.getFirstName()));
		params.add(new BasicNameValuePair(LAST_NAME, details.getLastName()));
		params.add(new BasicNameValuePair(DOMAIN, details.getDomain()));
		params.add(new BasicNameValuePair(PASS_WORD, details.getPassword()));
		Company company = context.getCompany();
		params.add(new BasicNameValuePair(COUNTRY, company.getCountry()));
		params.add(new BasicNameValuePair(COMPANY_NAME, company
				.getPreferences().getTradingName()));
		post.setEntity(new UrlEncodedFormEntity(params));
		HttpResponse response = client.execute(post);
		StatusLine status = response.getStatusLine();
		if (status.getStatusCode() != HttpStatus.SC_OK) {
			throw new RuntimeException(status.toString());
		}
		Thread.sleep(30 * 1000);
		String info = "***Signup Sucessfully with " + details.getEmailId();
		log.info(info);
		updateMigrationStatus(info);
	}

	private void updateMigrationStatus() {
		Session currentSession = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction beginTransaction = currentSession
				.beginTransaction();
		String query = "UPDATE migration_status SET info =:info , status=3 where company_id=:company_id";
		SQLQuery createSQLQuery = currentSession.createSQLQuery(query);
		createSQLQuery.setParameter("info", "Finished Migration");
		createSQLQuery.setParameter("company_id", context.getCompany().getId());
		createSQLQuery.executeUpdate();
		beginTransaction.commit();
	}

	private void updateMigrationStatus(String info) {
		Session currentSession = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction beginTransaction = currentSession
				.beginTransaction();
		String query = "UPDATE migration_status SET info =:info where company_id=:company_id";
		SQLQuery createSQLQuery = currentSession.createSQLQuery(query);
		createSQLQuery.setParameter("info", info);
		createSQLQuery.setParameter("company_id", context.getCompany().getId());
		createSQLQuery.executeUpdate();
		beginTransaction.commit();
	}
}
