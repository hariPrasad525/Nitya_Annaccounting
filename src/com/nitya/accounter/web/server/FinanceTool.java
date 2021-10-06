/**
 * 
 */
package com.nitya.accounter.web.server;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.NotSerializableException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.CallbackException;
import org.hibernate.FlushMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.hibernate.dialect.EncryptedStringType;
import org.json.JSONException;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.AccountTransaction;
import com.nitya.accounter.core.AccounterClass;
import com.nitya.accounter.core.AccounterServerConstants;
import com.nitya.accounter.core.AccounterThreadLocal;
import com.nitya.accounter.core.Activity;
import com.nitya.accounter.core.ActivityType;
import com.nitya.accounter.core.Advertisement;
import com.nitya.accounter.core.Attachment;
import com.nitya.accounter.core.BankAccount;
import com.nitya.accounter.core.BrandingTheme;
import com.nitya.accounter.core.Budget;
import com.nitya.accounter.core.BudgetItem;
import com.nitya.accounter.core.BuildAssembly;
import com.nitya.accounter.core.CashPurchase;
import com.nitya.accounter.core.Client;
import com.nitya.accounter.core.ClientConvertUtil;
import com.nitya.accounter.core.CloneUtil2;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.CreatableObject;
import com.nitya.accounter.core.CreditCardCharge;
import com.nitya.accounter.core.Currency;
import com.nitya.accounter.core.CustomField;
import com.nitya.accounter.core.Customer;
import com.nitya.accounter.core.CustomerPrePayment;
import com.nitya.accounter.core.CustomerRefund;
import com.nitya.accounter.core.Depreciation;
import com.nitya.accounter.core.EmailTemplate;
import com.nitya.accounter.core.EnterBill;
import com.nitya.accounter.core.Estimate;
import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.core.FiscalYear;
import com.nitya.accounter.core.FixedAsset;
import com.nitya.accounter.core.IAccounterServerCore;
import com.nitya.accounter.core.IRASCompanyInfo;
import com.nitya.accounter.core.IRASGeneralLedgerLineInfo;
import com.nitya.accounter.core.IRASInformation;
import com.nitya.accounter.core.IRASPurchaseLineInfo;
import com.nitya.accounter.core.IRASSupplyLineInfo;
import com.nitya.accounter.core.InventoryPurchase;
import com.nitya.accounter.core.Invoice;
import com.nitya.accounter.core.Item;
import com.nitya.accounter.core.Job;
import com.nitya.accounter.core.JournalEntry;
import com.nitya.accounter.core.Location;
import com.nitya.accounter.core.MakeDeposit;
import com.nitya.accounter.core.Measurement;
import com.nitya.accounter.core.MessageOrTask;
import com.nitya.accounter.core.NumberUtils;
import com.nitya.accounter.core.ObjectConvertUtil;
import com.nitya.accounter.core.PayBill;
import com.nitya.accounter.core.PayTAX;
import com.nitya.accounter.core.Payee;
import com.nitya.accounter.core.PaypalTransation;
import com.nitya.accounter.core.PortletConfiguration;
import com.nitya.accounter.core.PortletPageConfiguration;
import com.nitya.accounter.core.PurchaseOrder;
import com.nitya.accounter.core.ReceivePayment;
import com.nitya.accounter.core.ReceiveVAT;
import com.nitya.accounter.core.Reconciliation;
import com.nitya.accounter.core.ReconciliationItem;
import com.nitya.accounter.core.RecurringTransaction;
import com.nitya.accounter.core.Reminder;
import com.nitya.accounter.core.ServerConvertUtil;
import com.nitya.accounter.core.Statement;
import com.nitya.accounter.core.StockAdjustment;
import com.nitya.accounter.core.TAXAgency;
import com.nitya.accounter.core.TAXItem;
import com.nitya.accounter.core.TAXRateCalculation;
import com.nitya.accounter.core.TAXReturnEntry;
import com.nitya.accounter.core.TDSChalanDetail;
import com.nitya.accounter.core.TDSDeductorMasters;
import com.nitya.accounter.core.TDSResponsiblePerson;
import com.nitya.accounter.core.TDSTransactionItem;
import com.nitya.accounter.core.Transaction;
import com.nitya.accounter.core.TransactionDepositItem;
import com.nitya.accounter.core.TransactionItem;
import com.nitya.accounter.core.TransactionLog;
import com.nitya.accounter.core.TransferFund;
import com.nitya.accounter.core.Unit;
import com.nitya.accounter.core.User;
import com.nitya.accounter.core.Util;
import com.nitya.accounter.core.Utility;
import com.nitya.accounter.core.Vendor;
import com.nitya.accounter.core.VendorPrePayment;
import com.nitya.accounter.core.Warehouse;
import com.nitya.accounter.core.WriteCheck;
import com.nitya.accounter.core.change.ChangeTracker;
import com.nitya.accounter.mail.UsersMailSendar;
import com.nitya.accounter.main.PropertyParser;
import com.nitya.accounter.main.ServerConfiguration;
import com.nitya.accounter.server.imports.AccountImporter;
import com.nitya.accounter.server.imports.CustomerImporter;
import com.nitya.accounter.server.imports.Importer;
import com.nitya.accounter.server.imports.InvoiceImporter;
import com.nitya.accounter.server.imports.ItemImporter;
import com.nitya.accounter.server.imports.VendorImporter;
import com.nitya.accounter.services.DAOException;
import com.nitya.accounter.servlets.PaypalTransactionDetails;
import com.nitya.accounter.servlets.PaypalTransactionSearch;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.utils.SecureUtils;
import com.nitya.accounter.utils.MiniTemplator.TemplateSyntaxException;
import com.nitya.accounter.web.client.ClientLocalMessage;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientAccounterClass;
import com.nitya.accounter.web.client.core.ClientAdvertisement;
import com.nitya.accounter.web.client.core.ClientBudget;
import com.nitya.accounter.web.client.core.ClientCompany;
import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.core.ClientETDSFillingItem;
import com.nitya.accounter.web.client.core.ClientEmailAccount;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientIssuePayment;
import com.nitya.accounter.web.client.core.ClientItem;
import com.nitya.accounter.web.client.core.ClientLocation;
import com.nitya.accounter.web.client.core.ClientPayBill;
import com.nitya.accounter.web.client.core.ClientPaypalTransation;
import com.nitya.accounter.web.client.core.ClientPortletConfiguration;
import com.nitya.accounter.web.client.core.ClientPortletPageConfiguration;
import com.nitya.accounter.web.client.core.ClientReconciliation;
import com.nitya.accounter.web.client.core.ClientReconciliationItem;
import com.nitya.accounter.web.client.core.ClientRecurringTransaction;
import com.nitya.accounter.web.client.core.ClientReminder;
import com.nitya.accounter.web.client.core.ClientStatement;
import com.nitya.accounter.web.client.core.ClientTDSChalanDetail;
import com.nitya.accounter.web.client.core.ClientTDSDeductorMasters;
import com.nitya.accounter.web.client.core.ClientTDSResponsiblePerson;
import com.nitya.accounter.web.client.core.ClientTDSTransactionItem;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientTransactionIssuePayment;
import com.nitya.accounter.web.client.core.ClientTransactionLog;
import com.nitya.accounter.web.client.core.ClientTransferFund;
import com.nitya.accounter.web.client.core.ConsultantsDetailsList;
import com.nitya.accounter.web.client.core.Features;
import com.nitya.accounter.web.client.core.HrEmployee;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.ImportField;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.core.RecentTransactionsList;
import com.nitya.accounter.web.client.core.Lists.PayeeList;
import com.nitya.accounter.web.client.core.Lists.ReceivePaymentTransactionList;
import com.nitya.accounter.web.client.core.reports.AccountRegister;
import com.nitya.accounter.web.client.core.reports.DepositDetail;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.imports.ImporterType;
import com.nitya.accounter.web.client.translate.ClientLanguage;
import com.nitya.accounter.web.client.translate.ClientMessage;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.server.managers.CompanyManager;
import com.nitya.accounter.web.server.managers.CustomerManager;
import com.nitya.accounter.web.server.managers.DashboardManager;
import com.nitya.accounter.web.server.managers.ExportManager;
import com.nitya.accounter.web.server.managers.FixedAssestManager;
import com.nitya.accounter.web.server.managers.InventoryManager;
import com.nitya.accounter.web.server.managers.Manager;
import com.nitya.accounter.web.server.managers.PayrollManager;
import com.nitya.accounter.web.server.managers.PrintPDFManager;
import com.nitya.accounter.web.server.managers.PurchaseManager;
import com.nitya.accounter.web.server.managers.ReportManager;
import com.nitya.accounter.web.server.managers.SalesManager;
import com.nitya.accounter.web.server.managers.TaxManager;
import com.nitya.accounter.web.server.managers.UserManager;
import com.nitya.accounter.web.server.managers.VendorManager;
import com.nitya.accounter.web.server.translate.Key;
import com.nitya.accounter.web.server.translate.Language;
import com.nitya.accounter.web.server.translate.LocalMessage;
import com.nitya.accounter.web.server.translate.Message;
import com.nitya.accounter.web.server.translate.Vote;

import au.com.bytecode.opencsv.CSVReader;

/**
 * @author Fernandez
 * 
 */
public class FinanceTool {

	private static final Object Double = null;
	Logger log = Logger.getLogger(FinanceTool.class);
	private InventoryManager inventoryManager;
	private FixedAssestManager fixedAssestManager;
	private DashboardManager dashboardManager;
	private TaxManager taxManager;
	private PurchaseManager purchaseManager;
	private ReportManager reportManager;
	private VendorManager vendorManager;
	private CustomerManager customerManager;
	private SalesManager salesManager;
	private UserManager userManager;
	private Manager manager;
	private CompanyManager companyManager;
	private ExportManager exportManager;
	private PayrollManager payrollManager;

	/**
	 * This will Get Called when Create Operation is Invoked by the Client
	 * 
	 * @param createContext
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws AccounterException
	 */
	public long create(OperationContext createContext)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		try {
			IAccounterCore data = createContext.getData();
			String userID = createContext.getUserEmail();
			Company company = getCompany(createContext.getCompanyId());
			User user = company.getUserByUserEmail(userID);
			if (data == null) {
				throw new AccounterException(
						AccounterException.ERROR_ILLEGAL_ARGUMENT,
						"Operation Data Found Null...." + createContext);
			}

			Class<IAccounterServerCore> serverClass = ObjectConvertUtil
					.getServerEqivalentClass(data.getClass());
			IAccounterServerCore serverObject;
			try {
				serverObject = serverClass.newInstance();
			} catch (Exception e1) {
				throw new AccounterException(AccounterException.ERROR_INTERNAL);
			}

			serverObject = new ServerConvertUtil().toServerObject(serverObject,
					data, session);

			ObjectConvertUtil.setCompany(serverObject, company);

			// if (serverObject instanceof CreatableObject) {
			// // get the user from user id
			// User user = getCompany().getUserByUserEmail(userID);
			// Timestamp currentTime = new Timestamp(
			// System.currentTimeMillis());
			// ((CreatableObject) serverObject).setCreatedBy(user);
			//
			// ((CreatableObject) serverObject).setCreatedDate(currentTime);
			// ((CreatableObject) serverObject).setLastModifier(user);
			//
			// ((CreatableObject) serverObject)
			// .setLastModifiedDate(currentTime);
			// }
			getManager().canEdit(serverObject, data);
			checkFeatures(serverObject);
			isTransactionNumberExist(data, company);
			session.save(serverObject);

			if (serverObject instanceof Currency) {
				((Currency) serverObject)
						.createAccountsReveivablesAndPayables(session);
			} else if (serverObject instanceof Item) {
				((Item) serverObject).doCreateEffectForInventoryItem();
			}

			transaction.commit();

			org.hibernate.Transaction newTransaction = session
					.beginTransaction();
			Activity activity = new Activity(company, user, ActivityType.ADD,
					serverObject);
			session.save(activity);
			if (serverObject instanceof Transaction) {
				Transaction savingTrax = (Transaction) serverObject;
				savingTrax.setLastActivity(activity);
			}
			session.saveOrUpdate(serverObject);

			newTransaction.commit();

			if (serverObject instanceof Transaction) {

				org.hibernate.Transaction ht = session.beginTransaction();
				Transaction tt = (Transaction) serverObject;
				try {
					List<Item> inventory = tt.getInventoryUsed();
					InventoryRemappingService.sheduleRemap(inventory);
					int type = tt.getType();
					if (type == Transaction.TYPE_ENTER_BILL
							|| type == Transaction.TYPE_CASH_PURCHASE
							|| type == Transaction.TYPE_VENDOR_CREDIT_MEMO) {
						ItemUtils.updateAverageCost(tt.getItemsUsed());
					}
					ht.commit();
				} catch (Exception e) {
					e.printStackTrace();
					ht.rollback();
				}
			}

			ChangeTracker.put(serverObject);

			return serverObject.getID();
		} catch (CallbackException e) {
			transaction.rollback();
			Throwable cause = e.getCause();
			if (cause instanceof AccounterException) {
				throw (AccounterException) cause;
			} else {
				log.error(e.getMessage(), e);
				throw new AccounterException(AccounterException.ERROR_INTERNAL,
						e.getMessage());
			}
		} catch (Exception e) {
			transaction.rollback();
			if (e instanceof AccounterException) {
				log.error(e.getMessage(), e);
				throw (AccounterException) e;
			} else {
				log.error(e.getMessage(), e);
				throw new AccounterException(AccounterException.ERROR_INTERNAL,
						e.getMessage());
			}
		}
	}

	private void checkFeatures(IAccounterServerCore data)
			throws AccounterException {
		User user = AccounterThreadLocal.get();
		Company company = user.getCompany();
		user = company.getCreatedBy();
		Set<String> features = user.getClient().getClientSubscription()
				.getSubscription().getFeatures();
		
		//Kumar Kasimala changed..
		

		if (data instanceof Transaction) {
			Transaction transaction = (Transaction) data;
			if (!features.contains(Features.TRANSACTIONS)) {
				int count = getCompanyManager().getTransactionsCount(
						company.getId());
				if (count >= company.getTransactionsLimit()) {
					throw new AccounterException(
							AccounterException.ERROR_CANT_CREATE_MORE_TRANSACTIONS);
				}
			}

			if (!features.contains(Features.DRAFTS)) {
				if (transaction.getSaveStatus() == Transaction.STATUS_DRAFT) {
					throw new AccounterException(
							AccounterException.ERROR_PERMISSION_DENIED);
				}
			}

			if (!features.contains(Features.MULTI_CURRENCY)) {
				if (transaction.getCurrency() != null
						&& !transaction
								.getCurrency()
								.getFormalName()
								.equals(company.getPrimaryCurrency()
										.getFormalName())) {
					throw new AccounterException(
							AccounterException.ERROR_CANT_EDIT_MULTI_CURRENCY_TRANSACTION);
				}
			}

		} else if (data instanceof Estimate) {
			Estimate estimate = (Estimate) data;
			int type = estimate.getType();
			if (type == Estimate.SALES_ORDER
					&& !features.contains(Features.SALSE_ORDER)) {
				throw new AccounterException(
						AccounterException.ERROR_PERMISSION_DENIED);
			}

			if (type == Estimate.BILLABLEEXAPENSES
					&& !features.contains(Features.BILLABLE_EXPENSE)) {
				throw new AccounterException(
						AccounterException.ERROR_PERMISSION_DENIED);
			}

			if ((type == Estimate.CREDITS || type == Estimate.CHARGES)
					&& !features.contains(Features.CREDITS_CHARGES)) {
				throw new AccounterException(
						AccounterException.ERROR_PERMISSION_DENIED);
			}
		} else if (data instanceof PurchaseOrder) {
			if (!features.contains(Features.PURCHASE_ORDER)) {
				throw new AccounterException(
						AccounterException.ERROR_PERMISSION_DENIED);
			}
		} else if (data instanceof Job) {
			if (!features.contains(Features.JOB_COSTING)) {
				throw new AccounterException(
						AccounterException.ERROR_PERMISSION_DENIED);
			}
		} else if (data instanceof Location) {
			if (!features.contains(Features.LOCATION)) {
				throw new AccounterException(
						AccounterException.ERROR_PERMISSION_DENIED);
			}
		} else if (data instanceof AccounterClass) {
			if (!features.contains(Features.CLASS)) {
				throw new AccounterException(
						AccounterException.ERROR_PERMISSION_DENIED);
			}
		} else if (data instanceof FixedAsset || data instanceof Depreciation) {
			if (!features.contains(Features.FIXED_ASSET)) {
				throw new AccounterException(
						AccounterException.ERROR_PERMISSION_DENIED);
			}
		} else if (data instanceof Item) {
			Item item = (Item) data;
			if ((item.getType() == Item.TYPE_INVENTORY_ASSEMBLY || item
					.getType() == Item.TYPE_INVENTORY_PART)
					&& !features.contains(Features.INVENTORY)) {
				throw new AccounterException(
						AccounterException.ERROR_PERMISSION_DENIED);
			}
		} else if (data instanceof StockAdjustment
				|| data instanceof Measurement || data instanceof Unit
				|| data instanceof Warehouse || data instanceof BuildAssembly) {
			if (!features.contains(Features.INVENTORY)) {
				throw new AccounterException(
						AccounterException.ERROR_PERMISSION_DENIED);
			}
		} else if (data instanceof Budget || data instanceof BudgetItem) {
			if (!features.contains(Features.BUDGET)) {
				throw new AccounterException(
						AccounterException.ERROR_PERMISSION_DENIED);
			}
		} else if (data instanceof RecurringTransaction) {
			if (!features.contains(Features.RECURRING_TRANSACTIONS)) {
				throw new AccounterException(
						AccounterException.ERROR_PERMISSION_DENIED);
			}
		}
	}

	/**
	 * This will Get Called when Update Operation is Invoked by the Client
	 * 
	 * @param createContext
	 * @throws AccounterException
	 */
	public long update(OperationContext updateContext)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		org.hibernate.Transaction hibernateTransaction = session
				.beginTransaction();
		String userID = updateContext.getUserEmail();
		Company company = getCompany(updateContext.getCompanyId());
		User user = company.getUserByUserEmail(userID);
		try {
			IAccounterCore data = updateContext.getData();

			if (data == null) {
				throw new AccounterException(
						AccounterException.ERROR_ILLEGAL_ARGUMENT,
						"Operation Data Found Null...." + updateContext);
			}

			Class<?> classforName = ObjectConvertUtil
					.classforName(updateContext.getArg2());

			IAccounterServerCore serverObject = (IAccounterServerCore) session
					.get(classforName, Long.parseLong(updateContext.getArg1()));

			int version = serverObject.getVersion();
			if (version != data.getVersion()) {
				throw new AccounterException(
						AccounterException.ERROR_VERSION_MISMATCH);
			}
			IAccounterServerCore clonedObject = new CloneUtil2(
					IAccounterServerCore.class).clone(null, serverObject,
					data.getClass());
			ObjectConvertUtil.setCompany(clonedObject, company);

			getManager().canEdit(clonedObject, data);

			isTransactionNumberExist(data, company);

			serverObject = new ServerConvertUtil().toServerObject(serverObject,
					data, session);

			serverObject.setVersion(++version);

			if (serverObject instanceof Transaction) {
				Transaction transaction = (Transaction) serverObject;
				Set<String> features = company.getCreatedBy().getClient()
						.getClientSubscription().getSubscription()
						.getFeatures();
				if (!features.contains(Features.MULTI_CURRENCY)) {
					if (!transaction
							.getCurrency()
							.getFormalName()
							.equals(company.getPrimaryCurrency()
									.getFormalName())) {
						throw new AccounterException(
								AccounterException.ERROR_CANT_EDIT_MULTI_CURRENCY_TRANSACTION);
					}
				}

				Transaction clonedTransaction = (Transaction) clonedObject;
				if (!(transaction.getSaveStatus() == Transaction.STATUS_VOID
						&& clonedTransaction.getSaveStatus() == Transaction.STATUS_VOID || clonedTransaction
							.getSaveStatus() == Transaction.STATUS_DRAFT)) {
					transaction.onEdit(clonedTransaction);
				}
			}
			if (serverObject instanceof Lifecycle) {
				Lifecycle lifecycle = (Lifecycle) serverObject;
				lifecycle.onUpdate(session);
			}

			// if (serverObject instanceof Company) {
			// Company cmp = (Company) serverObject;
			// cmp.toCompany((ClientCompany) command.data);
			// ChangeTracker.put(cmp.toClientCompany());
			// }

			// called this method to save on unsaved objects in
			// session.
			// before going commit. because unsaved objects getting
			// update when transaction commit,
			// Util.loadObjectByStringID(session, command.arg2,
			// command.arg1);

			if (serverObject instanceof CreatableObject) {
				// get the user from user id
				((CreatableObject) serverObject).setLastModifier(getCompany(
						updateContext.getCompanyId()).getUserByUserEmail(
						updateContext.getUserEmail()));

				((CreatableObject) serverObject)
						.setLastModifiedDate(new Timestamp(System
								.currentTimeMillis()));
			}

			session.saveOrUpdate(serverObject);
			hibernateTransaction.commit();

			org.hibernate.Transaction newTransaction = session
					.beginTransaction();
			Activity activity = null;
			if (serverObject instanceof Transaction) {
				if (((Transaction) serverObject).isVoid()) {
					activity = new Activity(company, user, ActivityType.VOID,
							serverObject);
				} else {
					activity = new Activity(company, user, ActivityType.EDIT,
							serverObject);
				}
			} else {
				activity = new Activity(company, user, ActivityType.EDIT,
						serverObject);
			}
			session.saveOrUpdate(activity);
			if (serverObject instanceof Transaction) {
				((Transaction) serverObject).setLastActivity(activity);
			}
			session.saveOrUpdate(serverObject);
			newTransaction.commit();

			if (serverObject instanceof Transaction) {
				org.hibernate.Transaction ht = session.beginTransaction();
				Transaction tt = (Transaction) serverObject;
				try {
					List<Item> inventory = tt.getInventoryUsed();
					InventoryRemappingService.sheduleRemap(inventory);
					int type = tt.getType();
					if (type == Transaction.TYPE_ENTER_BILL
							|| type == Transaction.TYPE_CASH_PURCHASE
							|| type == Transaction.TYPE_VENDOR_CREDIT_MEMO) {
						ItemUtils.updateAverageCost(tt.getItemsUsed());
					}
					ht.commit();

				} catch (Exception e) {
					e.printStackTrace();
					ht.rollback();
				}
			}

			ChangeTracker.put(serverObject);
			return serverObject.getID();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			hibernateTransaction.rollback();
			if (e instanceof AccounterException) {
				throw (AccounterException) e;
			} else {
				throw new AccounterException(AccounterException.ERROR_INTERNAL);
			}
		}

	}

	/**
	 * This will Get Called when Delete Operation is Invoked by the Client
	 * 
	 * @param createContext
	 * @throws AccounterException
	 * @throws HibernateException
	 */
	public boolean delete(OperationContext context) throws AccounterException {

		Session session = HibernateUtil.getCurrentSession();

		org.hibernate.Transaction hibernateTransaction = session
				.beginTransaction();

		try {
			String arg1 = (context).getArg1();
			String arg2 = (context).getArg2();

			if (arg1 == null || arg2 == null) {
				throw new AccounterException(
						AccounterException.ERROR_ILLEGAL_ARGUMENT,
						"Delete Operation Cannot be Processed id or cmd.arg2 Found Null...."
								+ context);
			}

			Class<?> clientClass = ObjectConvertUtil
					.getEqivalentClientClass(arg2);

			Class<?> serverClass = ObjectConvertUtil
					.getServerEqivalentClass(clientClass);

			IAccounterServerCore serverObject = (IAccounterServerCore) session
					.get(serverClass, Long.parseLong(arg1));

			if (serverObject == null) {
				throw new AccounterException(
						AccounterException.ERROR_ALREADY_DELETED);
			}
			// if (objects != null && objects.size() > 0) {

			// IAccounterServerCore serverObject = (IAccounterServerCore)
			// objects
			// .get(0);
			String userID = context.getUserEmail();
			Company company = getCompany(context.getCompanyId());
			User user1 = company.getUserByUserEmail(userID);

			if (serverObject instanceof FiscalYear) {
				((FiscalYear) serverObject)
						.canDelete((FiscalYear) serverObject);
				session.delete(serverObject);
				// ChangeTracker.put(serverObject);
			} else if (serverObject instanceof User) {
				User user = (User) serverObject;
				if (company.getCreatedBy().getID() == user.getID()) {
					throw new AccounterException(
							AccounterException.ERROR_ILLEGAL_ARGUMENT);
				}
				user.setDeleted(true);
				session.saveOrUpdate(user);
			} else if (serverObject instanceof RecurringTransaction) {
				session.delete(serverObject);
			} else if (serverObject instanceof Reconciliation) {
				session.delete(serverObject);
			} else if (serverObject instanceof Budget) {
				session.delete(serverObject);
			} else if (serverObject instanceof TDSChalanDetail) {
				session.delete(serverObject);
			} else if (serverObject instanceof CustomField) {
				session.delete(serverObject);
			} else if (serverObject instanceof Transaction
					&& ((Transaction) serverObject).isTemplate()) {
				session.delete(((Transaction) serverObject)
						.getRecurringTransaction());
			} else if (serverObject instanceof StockAdjustment) {
				session.delete(serverObject);
			} else if (serverObject instanceof BuildAssembly) {
				session.delete(serverObject);
			} else if (serverObject instanceof MessageOrTask) {
				session.delete(serverObject);

			} else if (serverObject instanceof EmailTemplate) {
				session.delete(serverObject);
			} else {
				if (serverObject instanceof Transaction) {
					Transaction transaction = (Transaction) serverObject;
					if (!transaction.getReconciliationItems().isEmpty()) {
						throw new AccounterException(
								AccounterException.ERROR_DELETING_TRANSACTION_RECONCILIED);
					} else if (transaction.getCreditsAndPayments() != null) {
						transaction.getCreditsAndPayments().canEdit(
								transaction, false);
					}
				} else if (serverObject instanceof Account
						|| serverObject instanceof BankAccount) {

					Boolean isDefault = ((Account) serverObject).isDefault()
							|| (Boolean) session
									.getNamedQuery("isCompanyAccount")
									.setParameter("accountId",
											serverObject.getID())
									.setParameter("companyId", company.getID())
									.list().get(0);
					if (isDefault) {
						throw new AccounterException(
								AccounterException.ERROR_DELETING_SYSTEM_ACCOUNT);
					}
				} else if (serverObject instanceof TAXItem) {
					((TAXItem) serverObject).canDelete(serverObject);
				} else if (serverObject instanceof Currency) {
					Currency currency = (Currency) serverObject;
					long accountPayable = currency.getAccountsPayable().getID();
					Boolean isExists = (Boolean) session
							.getNamedQuery("isCompanyAccount")
							.setParameter("accountId", accountPayable)
							.setParameter("companyId", company.getID()).list()
							.get(0);
					if (isExists
							|| !canDelete(
									AccounterCoreType.ACCOUNT
											.getServerClassSimpleName(),
									accountPayable, company.getID())) {
						throw new AccounterException(
								AccounterException.ERROR_OBJECT_IN_USE);
					}
					accountPayable = currency.getAccountsReceivable().getID();
					isExists = (Boolean) session
							.getNamedQuery("isCompanyAccount")
							.setParameter("accountId", accountPayable)
							.setParameter("companyId", company.getID()).list()
							.get(0);
					if (isExists
							|| !canDelete(
									AccounterCoreType.ACCOUNT
											.getServerClassSimpleName(),
									accountPayable, company.getID())) {
						throw new AccounterException(
								AccounterException.ERROR_OBJECT_IN_USE);
					}
				}
				if (canDelete(serverClass.getSimpleName(),
						Long.parseLong(arg1), company.getID())) {
					session.delete(serverObject);
				} else {
					throw new AccounterException(
							AccounterException.ERROR_OBJECT_IN_USE);
				}
			}
			Activity activity = new Activity(company, user1,
					ActivityType.DELETE, serverObject);
			session.save(activity);

			hibernateTransaction.commit();

			if (serverObject instanceof Transaction) {
				org.hibernate.Transaction ht = session.beginTransaction();
				Transaction tt = (Transaction) serverObject;
				try {

					List<Item> inventory = tt.getInventoryUsed();
					InventoryRemappingService.sheduleRemap(inventory);
					int type = tt.getType();
					if (type == Transaction.TYPE_ENTER_BILL
							|| type == Transaction.TYPE_CASH_PURCHASE
							|| type == Transaction.TYPE_VENDOR_CREDIT_MEMO) {
						ItemUtils.updateAverageCost(tt.getItemsUsed());
					}
					ht.commit();
				} catch (Exception e) {
					e.printStackTrace();
					ht.rollback();
				}
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			hibernateTransaction.rollback();
			if (e instanceof AccounterException) {
				throw (AccounterException) e;
			} else {
				throw new AccounterException(AccounterException.ERROR_INTERNAL);
			}
		}
		return true;

	}

	public boolean canDelete(String serverClass, long id, long companyId) {
		String queryName = getCanDeleteQueryName(serverClass);
		Query query = HibernateUtil.getCurrentSession()
				.getNamedQuery(queryName).setParameter("inputId", id)
				.setParameter("companyId", companyId);
		return executeQuery(query);
	}

	private String getCanDeleteQueryName(String serverClass) {
		StringBuffer query = new StringBuffer("canDelete");
		query.append(serverClass);
		// if (serverClass.equals("TAXItem") || serverClass.equals("TAXGroup"))
		// {
		// if (companyType == Company.ACCOUNTING_TYPE_US) {
		// query.append("ForUS");
		// }
		// }
		return query.toString();
	}

	/**
	 * if command type id create, alter and delete, then changes will be add in
	 * chanageTracker,Put changes in comet stream
	 */
	public void putChangesInCometStream(long ServerCompanyID) {
		try {
			IAccounterCore[] changes = ChangeTracker.getChanges();
			if (changes != null && changes.length > 0) {
				log.info("Sending Changes From ChangeTracker:" + changes.length);
				Session session = null;
				session = HibernateUtil.getCurrentSession();
				Company company = getCompany(ServerCompanyID);
				List<User> users = session.getNamedQuery("getAllUsers")
						.setEntity("company", company).list();
				for (User user : users) {
					try {
						CometStream stream = CometManager.getStream(
								ServerCompanyID, user.getClient().getEmailId());
						if (stream == null) {
							continue;
						}
						for (IAccounterCore obj : changes) {
							if (obj != null) {
								stream.put(obj);
							}
						}
						log.info("Sent " + changes.length + " change to "
								+ user.getClient().getEmailId());
					} catch (NotSerializableException e) {
						e.printStackTrace();
						log.error("Failed to Process Request", e);

					}
				}
				ChangeTracker.clearChanges();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean executeQuery(Query query) {
		List<?> queryResult = query.list();
		Boolean flag = true;
		if (queryResult != null && queryResult.size() > 0
				&& queryResult.get(0) != null) {
			if (queryResult.get(0) instanceof Long) {
				Long obj = (Long) queryResult.get(0);
				if (obj != null) {
					flag = false;

				}
			} else {
				for (Object obj : queryResult) {
					Object[] result = (Object[]) obj;
					for (Object object : result) {
						if (object != null) {
							flag = false;
							break;
						}
					}
					if (!flag) {
						break;
					}
				}
			}

		}
		return flag;
	}

	@Deprecated
	/*
	 * The code in this method is shifted to onUpdate of FiscalYear
	 */
	public void alterFiscalYear(FiscalYear fiscalYear, long companyId)
			throws DAOException {
		try {
			Session session = HibernateUtil.getCurrentSession();
			Company company = getCompany(companyId);
			List<FiscalYear> list = new ArrayList<FiscalYear>();
			if (fiscalYear.getStartDate().equals(
					fiscalYear.getPreviousStartDate())) {
				session.saveOrUpdate(fiscalYear);
			} else if (!fiscalYear.getStartDate().equals(
					fiscalYear.getPreviousStartDate())) {
				FinanceDate modifiedStartDate = fiscalYear.getStartDate();
				FinanceDate existingLeastStartDate = modifiedStartDate;
				FinanceDate existingHighestEndDate = modifiedStartDate;
				Boolean exist = Boolean.FALSE;
				list = session.getNamedQuery("getFiscalYearf")
						.setEntity("company", company).list();
				if (list.size() > 0) {
					Iterator i = list.iterator();
					if (i.hasNext()) {
						FiscalYear fs = (FiscalYear) i.next();
						existingLeastStartDate = fs.getStartDate();
						existingHighestEndDate = fs.getEndDate();
					}
					i = list.iterator();
					while (i.hasNext()) {
						FiscalYear fs = (FiscalYear) i.next();
						if (modifiedStartDate.after(fs.getStartDate())
								&& modifiedStartDate.before(fs.getEndDate())) {
							exist = Boolean.TRUE;
							break;
						}
						if (fs.getStartDate().before(existingLeastStartDate)) {
							existingLeastStartDate = fs.getStartDate();
						}
						if (fs.getEndDate().after(existingHighestEndDate)) {
							existingHighestEndDate = fs.getEndDate();
						}

					}
					if (!exist) {
						Calendar cal = Calendar.getInstance();
						cal.setTime(modifiedStartDate.getAsDateObject());
						Integer modifiedYear = cal.get(Calendar.YEAR);

						cal.setTime(existingLeastStartDate.getAsDateObject());
						Integer existingLeastYear = cal.get(Calendar.YEAR);

						cal.setTime(existingHighestEndDate.getAsDateObject());
						Integer existingHighestYear = cal.get(Calendar.YEAR);
						if (modifiedStartDate.before(existingLeastStartDate)) {
							int diff = existingLeastYear - modifiedYear;
							for (int k = 0; k < diff; k++) {

								cal.set(modifiedYear + k, 0, 1);
								FinanceDate startDate = (new FinanceDate(
										cal.getTime()));

								cal.set(modifiedYear + k, 11, 31);
								FinanceDate endDate = (new FinanceDate(
										cal.getTime()));

								FiscalYear fs = new FiscalYear();
								fs.setStartDate(startDate);
								fs.setEndDate(endDate);
								fs.setStatus(FiscalYear.STATUS_OPEN);
								fs.setIsCurrentFiscalYear(Boolean.FALSE);
								session.save(fs);
							}

						} else if (modifiedStartDate
								.after(existingHighestEndDate)) {
							int diff = modifiedYear - existingLeastYear;
							for (int k = 1; k <= diff; k++) {
								cal.set(existingLeastYear + k, 0, 1);
								FinanceDate startDate = (new FinanceDate(
										cal.getTime()));

								cal.set(existingLeastYear + k, 0, 1);
								FinanceDate endDate = (new FinanceDate(
										cal.getTime()));
								FiscalYear fs = new FiscalYear();
								fs.setStartDate(startDate);
								fs.setEndDate(endDate);
								fs.setStatus(FiscalYear.STATUS_OPEN);
								fs.setIsCurrentFiscalYear(Boolean.FALSE);
								session.save(fs);
							}
						}

					}
					fiscalYear.setStartDate(fiscalYear.getPreviousStartDate());
					session.saveOrUpdate(fiscalYear);

				}
			} else if (fiscalYear.getStatus() == FiscalYear.STATUS_CLOSE) {
				if (!fiscalYear.getIsCurrentFiscalYear()) {
					throw (new DAOException(
							DAOException.INVALID_REQUEST_EXCEPTION, null));
				} else {
					session.saveOrUpdate(fiscalYear);
				}
			}

		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}

	}

	public PaginationList<JournalEntry> getJournalEntries(long companyId,
			FinanceDate fromDate, FinanceDate toDate, int start, int length)
			throws DAOException {
		try {
			int total = 0;
			List<JournalEntry> list;
			Session session = HibernateUtil.getCurrentSession();
			Company company = getCompany(companyId);
			Query query = session.getNamedQuery("getJournalEntry")
					.setEntity("company", company)
					.setParameter("fromDate", fromDate)
					.setParameter("toDate", toDate);
			// /If length will be -1 then get list for mobile With out limits
			if (length == -1) {
				list = query.list();
			} else {
				total = query.list().size();
				list = query.setFirstResult(start).setMaxResults(length).list();
			}
			PaginationList<JournalEntry> result = new PaginationList<JournalEntry>();

			for (JournalEntry j : list) {
				result.add(j);
			}
			result.setTotalCount(total);

			result.setStart(start);

			if (list != null) {
				return result;
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public JournalEntry getJournalEntry(long journalEntryId, long companyId)
			throws DAOException {
		try {

			Session session = HibernateUtil.getCurrentSession();
			Company company = getCompany(companyId);
			Query query = session.getNamedQuery("getJournalEntry.by.id")
					.setParameter("id", journalEntryId)
					.setEntity("company", company);
			List<JournalEntry> list = query.list();

			if (list.size() > 0) {
				return list.get(0);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public String getNextIssuePaymentCheckNumber(long account, long companyId) {
		return NumberUtils.getNextCheckNumber(companyId, account);
	}

	public String getNextFixedAssetNumber(long companyId) throws DAOException {
		// try {
		//
		// Session session = HibernateUtil.getCurrentSession();
		// Query query = session.getNamedQuery("getNextFixedAssetNumber");
		// List list = query.list();
		//
		// if (list != null) {
		// return (list.size() > 0) ? ((Long) list.get(0)) + 1 : 1;
		// } else
		// throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
		// null));
		// } catch (DAOException e) {
		// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		// }
		return NumberUtils.getNextFixedAssetNumber(getCompany(companyId));

	}

	public List<ReceivePaymentTransactionList> getTransactionReceivePayments(
			long customerId, long paymentDate1, long companyId)
			throws AccounterException {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			FinanceDate paymentDate = null;
			paymentDate = new FinanceDate(paymentDate1);
			Session session = HibernateUtil.getCurrentSession();
			Query query = session
					.getNamedQuery(
							"getReceivePaymentTransactionsListForCustomer")
					.setParameter("customerId", customerId)
					.setParameter("companyId", companyId)
					.setParameter("paymentDate", paymentDate1);
			List list = query.list();

			// Query query = session.getNamedQuery(
			// "getReceivePaymentTransactionsListForCustomer");
			// query.setLong("customerId", customerId);
			// List list = query.list();

			List<ReceivePaymentTransactionList> queryResult = new ArrayList<ReceivePaymentTransactionList>();
			Company company = getCompany(companyId);
			query = session
					.getNamedQuery(
							"getEntry.by.customerId.debitand.balanceDue.orderbyid")
					.setParameter("id", customerId)
					.setParameter("company", company);

			List<JournalEntry> openingBalanceEntries = query.list();

			for (JournalEntry je : openingBalanceEntries) {
				double currencyFactor = je.getCurrencyFactor();
				ReceivePaymentTransactionList receivePaymentTransactionList = new ReceivePaymentTransactionList();
				receivePaymentTransactionList.setTransactionId(je.getID());
				receivePaymentTransactionList.setType(je.getType());
				receivePaymentTransactionList.setDueDate(new ClientFinanceDate(
						je.getDate().getDate()));
				receivePaymentTransactionList.setNumber(je.getNumber());
				receivePaymentTransactionList.setInvoiceAmount(je
						.getDebitTotal());
				receivePaymentTransactionList.setAmountDue(je.getBalanceDue());
				receivePaymentTransactionList
						.setDiscountDate(new ClientFinanceDate(je.getDate()
								.getDate()));
				queryResult.add(receivePaymentTransactionList);
			}

			if (list != null) {
				Object[] object = null;
				Iterator iterator = list.iterator();

				while ((iterator).hasNext()) {

					ReceivePaymentTransactionList receivePaymentTransactionList = new ReceivePaymentTransactionList();
					object = (Object[]) iterator.next();

					receivePaymentTransactionList
							.setTransactionId((Long) object[0]);
					receivePaymentTransactionList.setType((Integer) object[1]);
					receivePaymentTransactionList
							.setDueDate(object[2] == null ? null
									: new ClientFinanceDate((Long) object[2]));
					receivePaymentTransactionList
							.setNumber((object[3] == null ? null
									: ((String) object[3])));

					// int discount = (object[8] != null && !paymentDate
					// .after((Long) object[6]))
					// ? ((Integer) object[8])
					// : 0;

					double invoicedAmount = (Double) object[4];
					receivePaymentTransactionList
							.setInvoiceAmount(invoicedAmount);

					double amountDue = (Double) object[5];
					receivePaymentTransactionList.setAmountDue(amountDue);
					ClientFinanceDate transactionDate = object[6] == null ? null
							: new ClientFinanceDate((Long) object[6]);
					receivePaymentTransactionList
							.setDiscountDate(transactionDate);
					receivePaymentTransactionList
							.setPayment((Double) object[7]);
					double discPerc = 0;
					if (object[8] != null) {
						boolean isDateDriven = (Boolean) object[8];
						int ifPaidWithin = (Integer) object[9];
						double discount = (Double) object[10];

						if (isDateDriven) {
							ClientFinanceDate currentDate = new ClientFinanceDate(
									paymentDate.getAsDateObject());
							if (currentDate.getYear() == transactionDate
									.getYear()
									&& currentDate.getMonth() == transactionDate
											.getMonth()
									&& currentDate.getDay() <= ifPaidWithin) {

								discPerc = discount;
							} else {
								discPerc = 0;
							}
						} else {
							long differenceBetween = UIUtils.getDays_between(
									transactionDate.getDateAsObject(),
									paymentDate.getAsDateObject());
							if (differenceBetween >= 0
									&& differenceBetween <= ifPaidWithin) {
								discPerc = discount;
							} else {
								discPerc = 0;
							}
						}
					}
					receivePaymentTransactionList
							.setCashDiscount((amountDue / 100) * (discPerc));
					queryResult.add(receivePaymentTransactionList);
				}
			} else {
				throw new AccounterException(
						AccounterException.ERROR_ILLEGAL_ARGUMENT);
			}
			return queryResult;
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof AccounterException) {
				throw (AccounterException) e;
			}
			throw new AccounterException(AccounterException.ERROR_INTERNAL, e);
		}

	}

	public boolean canVoidOrEdit(long invoiceOrVendorBillId, long companyId)
			throws DAOException {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Query query = session
				.getNamedQuery(
						"get.canVoidOrEditTransaction.from.transactionID")
				.setParameter("id", invoiceOrVendorBillId)
				.setParameter("company", company);
		List list = query.list();

		return (Boolean) list.iterator().next();
	}

	public ArrayList<WriteCheck> getLatestChecks(long companyId)
			throws DAOException {
		try {

			Session session = HibernateUtil.getCurrentSession();
			Query query = session.getNamedQuery("getLatestChecks")
					.setParameter("companyId", companyId);
			// FIXME ::: check the sql query and change it to hql query if
			// required try to fix all sql queries
			List list2 = query.list();

			Object object[] = null;
			Iterator iterator = list2.iterator();
			List<WriteCheck> list = new ArrayList<WriteCheck>();
			while (iterator.hasNext()) {

				object = (Object[]) iterator.next();
				WriteCheck writeCheck = new WriteCheck();
				// writeCheck
				// .setID((object[0] == null ? null : ((Long) object[0])));
				writeCheck.setDate((new FinanceDate((Long) object[1])));
				writeCheck.setPayToType((Integer) object[2]);
				writeCheck.setCustomer(object[4] != null ? (Customer) session
						.get(Customer.class, ((Long) object[4])) : null);
				writeCheck.setVendor(object[5] != null ? (Vendor) session.get(
						Vendor.class, ((Long) object[5])) : null);
				writeCheck.setTaxAgency(object[6] != null ? (TAXAgency) session
						.get(TAXAgency.class, ((Long) object[5])) : null);
				writeCheck.setAmount((Double) object[7]);
				list.add(writeCheck);
			}
			if (list != null) {
				return new ArrayList<WriteCheck>(list);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public ArrayList<TransferFund> getLatestDeposits(long companyId)
			throws DAOException {
		try {

			Session session = HibernateUtil.getCurrentSession();
			Query query = session.getNamedQuery("getLatestDeposits")
					.setParameter("companyId", companyId);
			List list2 = query.list();

			Object[] object = null;
			Iterator iterator = list2.iterator();
			List<TransferFund> list = new ArrayList<TransferFund>();
			while (iterator.hasNext()) {
				object = (Object[]) iterator.next();
				TransferFund makeDeposit = new TransferFund();
				// makeDeposit.setID((object[0] == null ? null
				// : ((Long) object[0])));
				makeDeposit.setDepositIn(object[1] == null ? null
						: (Account) session.get(Account.class,
								((Long) object[1])));
				makeDeposit.setMemo((String) object[2]);
				makeDeposit.setTotal((object[3] == null ? null
						: ((Double) object[3])));
				makeDeposit.setCashBackAccount(object[4] == null ? null
						: (Account) session.get(Account.class,
								((Long) object[4])));
				makeDeposit.setCashBackMemo((String) object[5]);
				makeDeposit.setCashBackAmount((object[6] == null ? null
						: ((Double) object[6])));
				list.add(makeDeposit);

			}
			if (list != null) {
				return new ArrayList<TransferFund>(list);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public ArrayList<Item> getLatestItems(long companyId) throws DAOException {
		try {

			Session session = HibernateUtil.getCurrentSession();
			Query query = session.getNamedQuery("getLatestItems").setParameter(
					"companyId", companyId);
			List list2 = query.list();

			Object object[] = null;
			Iterator iterator = list2.iterator();
			List<Item> list = new ArrayList<Item>();
			while (iterator.hasNext()) {
				object = (Object[]) iterator.next();
				Item item = new Item();
				// item.setID((object[0] == null ? null : ((Long) object[0])));
				item.setName((String) object[1]);
				item.setType((Integer) object[2]);
				item.setSalesPrice((Double) object[3]);
				list.add(item);
			}
			if (list != null) {
				return new ArrayList<Item>(list);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public ArrayList<Item> getTotalWithNamesItems(long companyId) throws DAOException {
		try {

			Session session = HibernateUtil.getCurrentSession();
			Query query = session.getNamedQuery("getTotalWithNamesItems").setParameter(
					"companyId", companyId);
			List list2 = query.list();

			Object object[] = null;
			Iterator iterator = list2.iterator();
			List<Item> list = new ArrayList<Item>();
			while (iterator.hasNext()) {
				object = (Object[]) iterator.next();
				Item item = new Item();
				// item.setID((object[0] == null ? null : ((Long) object[0])));
				item.setId((Long) object[0]);
				item.setName((String) object[1]);
				item.setSalesPrice(object[2] == null ? 0 : (Double) object[2]);
				item.setOpeningBalance(object[3] == null ? 0 : (Double) object[3]);
				item.setPath((String) object[4]);
				list.add(item);
			}
			if (list != null) {
				return new ArrayList<Item>(list);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public Long getNextCheckNumber(long accountId) throws DAOException {

		// try {
		// Session session = HibernateUtil.getCurrentSession();
		// long account = getLongIdForGivenid(AccounterCoreType.ACCOUNT,
		// account2);
		// Query query = session
		// .createQuery(
		// "from com.nitya.accounter.core.WriteCheck wc  where wc.bankAccount.id = ?")
		// .setParameter(0, account);
		// List list1 = query.list();
		// if (list1.size() <= 0) {
		// return 1L;
		// }
		//
		// query = session
		// .createQuery(
		// "select max(wc.checkNumber) from com.nitya.accounter.core.WriteCheck wc where wc.bankAccount.id = ? ")
		// .setParameter(0, account);
		// List list = query.list();
		// if (list != null && list.size() > 0) {
		// Object obj = list.get(0);
		// return (obj != null)
		// ? (Long) obj != -1 ? ((Long) obj) + 1 : 1
		// : 1;
		// } else
		// throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
		// null));
		// } catch (DAOException e) {
		// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		// }
		return null;
	}

	public Long getNextNominalCode(int accountType, long companyId)
			throws DAOException {

		try {
			int accountSubBaseType = Utility.getAccountSubBaseType(accountType);
			Session session = HibernateUtil.getCurrentSession();
			Company company = getCompany(companyId);
			Integer range[] = company.getNominalCodeRange(accountSubBaseType);

			Query query = session
					.getNamedQuery("getNextNominalCodeForGivenAccountType")
					.setParameter("subBaseType", accountSubBaseType)
					.setParameter("companyId", companyId);
			List list = query.list();
			Long nextNominalCode = (list.size() > 0) ? ((Long) list.get(0)) + 1
					: range[0];
			if (nextNominalCode > range[1]) {
				throw (new DAOException(DAOException.RANGE_EXCEED_EXCEPTION,
						null,
						"Nominal Code Range exceeded. Please delete the existing accounts of this type"));
			}

			if (list != null) {
				return nextNominalCode;
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}

	}

	public void test() throws Exception {
		// HibernateTemplate template = getHibernateTemplate();
		//
		// // List list = template.findByNamedQueryAndNamedParam(
		// // "test", "companyId", company);
		// List list = template.findByNamedQuery("test");
		// if (list != null) {
		// Iterator i = list.iterator();
		// Object o[] = null;
		// while (i.hasNext()) {
		// o = (Object[]) i.next();
		// String accName = (String) o[0];
		// String taName = (String) o[1];
		//
		// }
		// }
	}

	/**
	 * 
	 * This method will give us the total effect of Selling or Disposing a Fixed
	 * Asset, before Sell or Dispose this Fixed Asset.
	 * 
	 * @param companyId
	 * 
	 * @return
	 * @throws Exception
	 */

	public ArrayList<ClientFinanceDate> getFinancialYearStartDates(
			long companyId) throws DAOException {

		List<ClientFinanceDate> startDates = new ArrayList<ClientFinanceDate>();
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		FinanceDate depreciationStartDate = company.getPreferences()
				.getDepreciationStartDate();
		Calendar depStartDateCal = new GregorianCalendar();
		depStartDateCal.setTime(depreciationStartDate.getAsDateObject());
		Query query = session.getNamedQuery(
				"getFiscalYear.by.check.status.startDate").setEntity("company",
				company);
		List<FiscalYear> list = query.list();
		for (FiscalYear fs : list) {
			FinanceDate date = fs.getStartDate();
			Calendar cal = new GregorianCalendar();
			cal.setTime(date.getAsDateObject());
			cal.set(Calendar.DAY_OF_MONTH,
					depStartDateCal.get(Calendar.DAY_OF_MONTH));
			cal.set(Calendar.MONTH, depStartDateCal.get(Calendar.MONTH));
			startDates.add(new ClientFinanceDate(cal.getTime()));
		}
		return new ArrayList<ClientFinanceDate>(startDates);
	}

	public List<Long> getSubAccounts(long accountId, long companyId) {
		ArrayList<Long> list = new ArrayList<Long>();
		if (accountId != 0) {
			Set<Account> accounts = getCompany(companyId).getAccounts();
			addSubAccounts(accounts, accountId, list);
		}
		return list;
	}

	public void addSubAccounts(Set<Account> accounts, long accountId,
			List<Long> list) {
		for (Account account : accounts) {
			if (account.getParent() != null
					&& account.getParent().getID() == accountId) {
				list.add(account.getID());
				addSubAccounts(accounts, account.getID(), list);
			}
		}
	}

	public PaginationList<AccountRegister> getAccountRegister(
			final FinanceDate startDate, final FinanceDate endDate,
			final long accountId, long companyId, int start, int length)
			throws DAOException {
		int total = 0;
		List<Long> parents = new ArrayList<Long>();
		parents.add(accountId);
		parents.addAll(getSubAccounts(accountId, companyId));

		PaginationList<AccountRegister> result = new PaginationList<AccountRegister>();
		for (Long account : parents) {
			List<AccountRegister> queryResult = new ArrayList<AccountRegister>();
			Session session = HibernateUtil.getCurrentSession();
			Query balanceQuery = session
					.getNamedQuery("getAccountRegisterOpeningBalance")
					.setParameter("companyId", companyId)
					.setParameter("accountId", account)
					.setParameter("startDate", startDate.getDate())
					.setParameter("openingBalance",
							AccounterServerConstants.OPENING_BALANCE,
							EncryptedStringType.INSTANCE);
			Double balance = (java.lang.Double) balanceQuery.uniqueResult();
			if (balance == null) {
				balance = 0.00D;
			}
			if (start > 0) {
				Query balanceQueryWithLimit = session
						.getNamedQuery(
								"getAccountRegisterOpeningBalancewithLimit")
						.setParameter("companyId", companyId)
						.setParameter("accountId", account)
						.setParameter("startDate", startDate.getDate())
						.setParameter("openingBalance",
								AccounterServerConstants.OPENING_BALANCE,
								EncryptedStringType.INSTANCE)
						.setParameter("limit", start)
						.setParameter("endDate", endDate.getDate());

				Double balanceWithLimit = (java.lang.Double) balanceQueryWithLimit
						.uniqueResult();

				if (balanceWithLimit == null) {
					balanceWithLimit = 0.00D;
				}
				balance += balanceWithLimit;
			}
			Query query = session
					.getNamedQuery("getAccountRegister")
					.setParameter("companyId", companyId)
					.setParameter("accountId", account)
					.setParameter("startDate", startDate.getDate())
					.setParameter("endDate", endDate.getDate())
					.setParameter("openingBalance",
							AccounterServerConstants.OPENING_BALANCE,
							EncryptedStringType.INSTANCE)
					.setParameter("creditors",
							AccounterServerConstants.ACCOUNTS_PAYABLE,
							EncryptedStringType.INSTANCE)
					.setParameter("debtors",
							AccounterServerConstants.ACCOUNTS_RECEIVABLE,
							EncryptedStringType.INSTANCE)
					.setParameter("multiple", "Multiple",
							EncryptedStringType.INSTANCE);

			List l = query.list();
			if (l != null && !(l.isEmpty())) {
				Object[] object = null;
				String payeeName = null;
				PayeeList payeeList = null;
				Iterator iterator = l.iterator();

				int index = 0;
				while ((iterator).hasNext()) {

					AccountRegister accountRegister = new AccountRegister();
					object = (Object[]) iterator.next();

					accountRegister.setDate(new ClientFinanceDate(
							(Long) object[0]));
					accountRegister.setType(object[1] == null ? 0
							: (Integer) object[1]);
					accountRegister.setNumber((String) object[2]);
					accountRegister.setAmount(object[3] == null ? 0
							: ((Double) object[3]).doubleValue());
					accountRegister.setPayTo((String) object[4]);
					accountRegister.setCheckNumber(object[5] == null ? null
							: ((String) object[5]));
					accountRegister.setAccount((String) object[6]);
					/*
					 * Clob cl = (Clob) object[7]; if (cl == null) {
					 * 
					 * accountRegister.setMemo("");
					 * 
					 * } else {
					 * 
					 * StringBuffer strOut = new StringBuffer(); String aux; try
					 * { BufferedReader br = new BufferedReader(cl
					 * .getCharacterStream()); while ((aux = br.readLine()) !=
					 * null) strOut.append(aux);
					 * accountRegister.setMemo(strOut.toString()); } catch
					 * (java.sql.SQLException e1) {
					 * 
					 * } catch (java.io.IOException e2) {
					 * 
					 * } }
					 */
					accountRegister.setMemo((String) object[7]);
					accountRegister.setTransactionId((Long) object[8]);
					accountRegister.setVoided(object[9] == null ? false
							: (Boolean) object[9]);
					accountRegister.setCurrency(object[10] == null ? 0
							: (Long) object[10]);
					accountRegister.setCurrencyfactor(object[11] == null ? 0
							: (Double) object[11]);
					queryResult.add(accountRegister);

					// if(index<start){
					// balance +=accountRegister.getAmount();
					// }

					index++;
				}
				total += queryResult.size();
				AccountRegister ar = new AccountRegister();
				ar.setAmount(balance);
				if (result.isEmpty()) {
					result.add(0, ar);
				} else {
					result.set(0, ar);
				}

				if (length < 0) {
					result.addAll(queryResult);
				} else {
					int toIndex = start + length;
					if (toIndex > queryResult.size()) {
						toIndex = queryResult.size();
					}
					if (start < toIndex) {
						result.addAll(queryResult.subList(start, toIndex));
					} else {
						return result;
					}
				}
			}
		}

		result.setTotalCount(total);
		result.setStart(start);
		return result;
	}

	public String getNextTransactionNumber(int transactionType, long companyId) {

		// Query query = HibernateUtil
		// .getCurrentSession()
		// .createQuery(
		// "select max(t.id) from com.nitya.accounter.core.Transaction t where t.type =:transactionType")
		// .setParameter("transactionType", transactionType);
		//
		// List list = query.list();
		//
		// if (list == null || list.size() <= 0 || list.get(0) == null) {
		// return "1";
		// }
		//
		// String prevNumber = getPreviousTransactionNumber(transactionType,
		// (Long) list.get(0));
		//
		// return getStringwithIncreamentedDigit(prevNumber);
		return NumberUtils.getNextTransactionNumber(transactionType,
				getCompany(companyId));
	}

	public ArrayList<HrEmployee> getHREmployees() {
		Session session = HibernateUtil.getCurrentSession();
		SQLQuery query = session.createSQLQuery(
				"SELECT empd.FULL_NAME as name FROM USERS empd").addScalar(
				"name", Hibernate.STRING);
		List list = query.list();

		// Object[] object = null;
		Iterator iterator = list.iterator();
		List<HrEmployee> hrEmployees = new ArrayList<HrEmployee>();
		while ((iterator).hasNext()) {

			HrEmployee hrEmployee = new HrEmployee();
			// object = (Object[]) iterator.next();
			hrEmployee.setEmployeeName((String) iterator.next());
			// hrEmployee.setEmployeeNum((String) object[1]);

			hrEmployees.add(hrEmployee);
		}
		return new ArrayList<HrEmployee>(hrEmployees);
	}

	public String getPreviousTransactionNumber(int transactionType,
			long maxCount) {

		Query query = HibernateUtil.getCurrentSession()
				.getNamedQuery("getTransactionNumber.from.typeandId")
				.setParameter(0, transactionType).setParameter(0, maxCount);

		List list = query.list();

		String num = (list != null) && (list.size() > 0) ? ((String) list
				.get(0)) : "";
		if (num.replaceAll("[\\D]", "").length() > 0) {
			return num;
		} else {
			if (maxCount != 0) {
				maxCount = maxCount - 1;
				return getPreviousTransactionNumber(transactionType, maxCount);
			}
		}

		return "0";
	}

	private boolean isTransactionNumberExist(IAccounterCore object,
			Company company) throws AccounterException {
		FlushMode flushMode = HibernateUtil.getCurrentSession().getFlushMode();
		HibernateUtil.getCurrentSession().setFlushMode(FlushMode.COMMIT);

		try {

			if (object instanceof ClientTransaction
					&& !(object instanceof ClientTransferFund)
					&& !(object instanceof ClientTransferFund)) {

				ClientTransaction clientObject = (ClientTransaction) object;

				if (clientObject.isVoid())
					return true;

				if (clientObject instanceof ClientPayBill
						&& ((ClientPayBill) clientObject).getPayBillType() == 1) {
					return true;
				}

				if (clientObject.getNumber() == null
						|| clientObject.getNumber().equals(""))
					return true;
				Query query = HibernateUtil
						.getCurrentSession()
						.getNamedQuery("getTransaction.by.check.type.number.id")
						.setParameter("company", company)
						.setParameter("type", clientObject.getType())
						.setParameter("number", clientObject.getNumber(),
								EncryptedStringType.INSTANCE)
						.setParameter("id", clientObject.getID());

				List list = query.list();

				if (list != null
						&& list.size() > 0
						&& list.get(0) != null
						&& !(company.getPreferences()
								.getAllowDuplicateDocumentNumbers())) {
					throw new AccounterException(
							AccounterException.ERROR_NUMBER_CONFLICT,
							" A Transaction already exists with this number. Please give another one. ");
				}

			}
			return true;

		} finally {
			HibernateUtil.getCurrentSession().setFlushMode(flushMode);
		}
	}

	/**
	 * Returns the Current Company
	 * 
	 * @return
	 */
	public Company getCompany(long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		return (Company) session.get(Company.class, companyId);
	}

	public static void createViews() {
		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		session.getNamedQuery("createSalesPurchasesView").executeUpdate();
		session.getNamedQuery("createTransactionHistoryView").executeUpdate();
		session.getNamedQuery("createDeleteCompanyFunction").executeUpdate();
		session.getNamedQuery("createInventoryPurchaseHistory").executeUpdate();
		session.getNamedQuery("getInventoryHistoryView").executeUpdate();
		session.getNamedQuery("JobsTransactionsView").executeUpdate();
		session.getNamedQuery("createSubscriptionFunction").executeUpdate();
		session.getNamedQuery("transactionsCreatedCountTrigger")
				.executeUpdate();
		session.getNamedQuery("companiesCountTrigger").executeUpdate();
		session.getNamedQuery("lastClientUpDateTrigger").executeUpdate();
		session.getNamedQuery("changedPasswordCountTrigger").executeUpdate();
		session.getNamedQuery("noOfUsersPerCompanyCountTrigger")
				.executeUpdate();
		session.getNamedQuery("noOfTRansactionPerCompanyCountTrigger")
				.executeUpdate();
		session.getNamedQuery("clientUpdateTrigger").executeUpdate();
		session.getNamedQuery("userInsertTrigger").executeUpdate();
		session.getNamedQuery("transactionsUpdateCountTrigger").executeUpdate();
		// session.getNamedQuery("updateAccounterClassPathTrigger")
		// .executeUpdate();
		// session.getNamedQuery("updateItemPathTrigger").executeUpdate();

		transaction.commit();
	}

	public static void createViewsForclient(long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		session.getNamedQuery("createSalesPurchasesViewForclient")
				.setParameter("companyId", companyId).executeUpdate();
		session.getNamedQuery("createTransactionHistoryViewForclient")
				.setParameter("companyId", companyId).executeUpdate();
	}

	/**
	 * 
	 * 
	 * 
	 * P.is_active AS IS_ACTIVE, P.NAME AS CUSTOMER_NAME, P.id AS ID, P.balance
	 * AS BALANCE, CASE WHEN t.currency_factor IS NULL THEN 1 ELSE
	 * t.currency_factor END AS TRANSACTION_CURRENCY_FACTOR, P.currency AS
	 * PAYEE_CURRENCY
	 */
	public PaginationList<PayeeList> getPayeeList(int category,
			boolean isActive, int start, int length, long companyId,
			boolean isPayeeCenter) throws AccounterException {
		if (isPayeeCenter) {
			Session session = HibernateUtil.getCurrentSession();
			PaginationList<PayeeList> queryResult = new PaginationList<PayeeList>();
			int total = 0;
			Query query = session.getNamedQuery("getPayeesForCenters")
					.setParameter("companyId", companyId)
					.setParameter("isActive", isActive)
					.setParameter("payee_type", category);
			List list = query.list();
			if (list != null && !list.isEmpty()) {
				Iterator iterator = list.iterator();
				while ((iterator).hasNext()) {
					Object[] object = (Object[]) iterator.next();
					PayeeList payeeList = new PayeeList();
					payeeList.setActive((object[0] == null ? false
							: ((Boolean) object[0]).booleanValue()));
					payeeList.setPayeeName((String) object[1]);
					payeeList.setID((Long) object[2]);
					payeeList.setBalance((Double) object[3]);
					payeeList.setCurrecny((Long) object[4]);
					payeeList.setType((Integer) object[5]);
					queryResult.add(payeeList);
				}
			}
			total = queryResult.size();
			PaginationList<PayeeList> result = new PaginationList<PayeeList>();
			if (length < 0) {
				result.addAll(queryResult);
			} else {
				int toIndex = start + length;
				if (toIndex > queryResult.size()) {
					toIndex = queryResult.size();
				}
				result.addAll(queryResult.subList(start, toIndex));
			}
			result.setTotalCount(total);
			result.setStart(start);
			return result;
		} else {
			return getPayeeList(category, isActive, start, length, companyId);
		}
	}

	public PaginationList<PayeeList> getPayeeList(int category,
			boolean isActive, int start, int length, long companyId)
			throws AccounterException {
		try {
			Session session = HibernateUtil.getCurrentSession();
			int total = 0;
			FinanceDate currentDate = new FinanceDate();

			Calendar currentMonthStartDateCal = new GregorianCalendar();
			currentMonthStartDateCal.setTime(currentDate.getAsDateObject());
			currentMonthStartDateCal.set(Calendar.DATE, 1);

			Calendar currentMonthEndDateCal = new GregorianCalendar();
			currentMonthEndDateCal.setTime(currentDate.getAsDateObject());
			currentMonthEndDateCal.set(Calendar.DATE, currentMonthEndDateCal
					.getActualMaximum(Calendar.DAY_OF_MONTH));

			Calendar previousFirstMonthStartDateCal = new GregorianCalendar();
			previousFirstMonthStartDateCal.setTime(currentDate
					.getAsDateObject());
			previousFirstMonthStartDateCal.set(Calendar.MONTH,
					previousFirstMonthStartDateCal.get(Calendar.MONTH) - 1);
			previousFirstMonthStartDateCal.set(Calendar.DATE, 1);

			Calendar previousFirstMonthEndDateCal = new GregorianCalendar();
			previousFirstMonthEndDateCal.setTime(currentDate.getAsDateObject());
			previousFirstMonthEndDateCal.set(Calendar.MONTH,
					previousFirstMonthEndDateCal.get(Calendar.MONTH) - 1);
			previousFirstMonthEndDateCal.set(Calendar.DATE,
					previousFirstMonthEndDateCal
							.getActualMaximum(Calendar.DAY_OF_MONTH));

			Calendar previousSecondMonthStartDateCal = new GregorianCalendar();
			previousSecondMonthStartDateCal.setTime(currentDate
					.getAsDateObject());
			previousSecondMonthStartDateCal.set(Calendar.MONTH,
					previousSecondMonthStartDateCal.get(Calendar.MONTH) - 2);
			previousSecondMonthStartDateCal.set(Calendar.DATE, 1);

			Calendar previousSecondMonthEndDateCal = new GregorianCalendar();
			previousSecondMonthEndDateCal
					.setTime(currentDate.getAsDateObject());
			previousSecondMonthEndDateCal.set(Calendar.MONTH,
					previousSecondMonthEndDateCal.get(Calendar.MONTH) - 2);
			previousSecondMonthEndDateCal.set(Calendar.DATE,
					previousSecondMonthEndDateCal
							.getActualMaximum(Calendar.DAY_OF_MONTH));

			Calendar previousThirdMonthStartDateCal = new GregorianCalendar();
			previousThirdMonthStartDateCal.setTime(currentDate
					.getAsDateObject());
			previousThirdMonthStartDateCal.set(Calendar.MONTH,
					previousThirdMonthStartDateCal.get(Calendar.MONTH) - 3);
			previousThirdMonthStartDateCal.set(Calendar.DATE, 1);

			Calendar previousThirdMonthEndDateCal = new GregorianCalendar();
			previousThirdMonthEndDateCal.setTime(currentDate.getAsDateObject());
			previousThirdMonthEndDateCal.set(Calendar.MONTH,
					previousThirdMonthEndDateCal.get(Calendar.MONTH) - 3);
			previousThirdMonthEndDateCal.set(Calendar.DATE,
					previousThirdMonthEndDateCal
							.getActualMaximum(Calendar.DAY_OF_MONTH));

			Calendar previousFourthMonthStartDateCal = new GregorianCalendar();
			previousFourthMonthStartDateCal.setTime(currentDate
					.getAsDateObject());
			previousFourthMonthStartDateCal.set(Calendar.MONTH,
					previousFourthMonthStartDateCal.get(Calendar.MONTH) - 4);
			previousFourthMonthStartDateCal.set(Calendar.DATE, 1);

			Calendar previousFourthMonthEndDateCal = new GregorianCalendar();
			previousFourthMonthEndDateCal
					.setTime(currentDate.getAsDateObject());
			previousFourthMonthEndDateCal.set(Calendar.MONTH,
					previousFourthMonthEndDateCal.get(Calendar.MONTH) - 4);
			previousFourthMonthEndDateCal.set(Calendar.DATE,
					previousFourthMonthEndDateCal
							.getActualMaximum(Calendar.DAY_OF_MONTH));

			Calendar previousFifthMonthStartDateCal = new GregorianCalendar();
			previousFifthMonthStartDateCal.setTime(currentDate
					.getAsDateObject());
			previousFifthMonthStartDateCal.set(Calendar.MONTH,
					previousFifthMonthStartDateCal.get(Calendar.MONTH) - 5);
			previousFifthMonthStartDateCal.set(Calendar.DATE, 1);

			Calendar previousFifthMonthEndDateCal = new GregorianCalendar();
			previousFifthMonthEndDateCal.setTime(currentDate.getAsDateObject());
			previousFifthMonthEndDateCal.set(Calendar.MONTH,
					previousFifthMonthEndDateCal.get(Calendar.MONTH) - 5);
			previousFifthMonthEndDateCal.set(Calendar.DATE,
					previousFifthMonthEndDateCal
							.getActualMaximum(Calendar.DAY_OF_MONTH));

			Query query = null;

			if (category == Payee.TYPE_CUSTOMER) {

				query = session
						.getNamedQuery("getCustomersList")
						.setParameter("companyId", companyId)
						.setParameter(
								"currentMonthStartDateCal",
								new FinanceDate(currentMonthStartDateCal
										.getTime()).getDate())
						.setParameter(
								"currentMonthEndDateCal",
								new FinanceDate(currentMonthEndDateCal
										.getTime()).getDate())
						.setParameter(
								"previousFirstMonthStartDateCal",
								new FinanceDate(previousFirstMonthStartDateCal
										.getTime()).getDate())
						.setParameter(
								"previousFirstMonthEndDateCal",
								new FinanceDate(previousFirstMonthEndDateCal
										.getTime()).getDate())
						.setParameter(
								"previousSecondMonthStartDateCal",
								new FinanceDate(previousSecondMonthStartDateCal
										.getTime()).getDate())
						.setParameter(
								"previousSecondMonthEndDateCal",
								new FinanceDate(previousSecondMonthEndDateCal
										.getTime()).getDate())
						.setParameter(
								"previousThirdMonthStartDateCal",
								new FinanceDate(previousThirdMonthStartDateCal
										.getTime()).getDate())
						.setParameter(
								"previousThirdMonthEndDateCal",
								new FinanceDate(previousThirdMonthEndDateCal
										.getTime()).getDate())
						.setParameter(
								"previousFourthMonthStartDateCal",
								new FinanceDate(previousFourthMonthStartDateCal
										.getTime()).getDate())
						.setParameter(
								"previousFourthMonthEndDateCal",
								new FinanceDate(previousFourthMonthEndDateCal
										.getTime()).getDate())
						.setParameter(
								"previousFifthMonthStartDateCal",
								new FinanceDate(previousFifthMonthStartDateCal
										.getTime()).getDate())
						.setParameter(
								"previousFifthMonthEndDateCal",
								new FinanceDate(previousFifthMonthEndDateCal
										.getTime()).getDate())
						.setParameter("isActive", isActive);

			} else if (category == Payee.TYPE_VENDOR) {

				query = session
						.getNamedQuery("getVendorsList")
						.setParameter("companyId", companyId)
						.setParameter(
								"currentMonthStartDateCal",
								new FinanceDate(currentMonthStartDateCal
										.getTime()).getDate())
						.setParameter(
								"currentMonthEndDateCal",
								new FinanceDate(currentMonthEndDateCal
										.getTime()).getDate())
						.setParameter(
								"previousFirstMonthStartDateCal",
								new FinanceDate(previousFirstMonthStartDateCal
										.getTime()).getDate())
						.setParameter(
								"previousFirstMonthEndDateCal",
								new FinanceDate(previousFirstMonthEndDateCal
										.getTime()).getDate())
						.setParameter(
								"previousSecondMonthStartDateCal",
								new FinanceDate(previousSecondMonthStartDateCal
										.getTime()).getDate())
						.setParameter(
								"previousSecondMonthEndDateCal",
								new FinanceDate(previousSecondMonthEndDateCal
										.getTime()).getDate())
						.setParameter(
								"previousThirdMonthStartDateCal",
								new FinanceDate(previousThirdMonthStartDateCal
										.getTime()).getDate())
						.setParameter(
								"previousThirdMonthEndDateCal",
								new FinanceDate(previousThirdMonthEndDateCal
										.getTime()).getDate())
						.setParameter(
								"previousFourthMonthStartDateCal",
								new FinanceDate(previousFourthMonthStartDateCal
										.getTime()).getDate())
						.setParameter(
								"previousFourthMonthEndDateCal",
								new FinanceDate(previousFourthMonthEndDateCal
										.getTime()).getDate())
						.setParameter(
								"previousFifthMonthStartDateCal",
								new FinanceDate(previousFifthMonthStartDateCal
										.getTime()).getDate())
						.setParameter(
								"previousFifthMonthEndDateCal",
								new FinanceDate(previousFifthMonthEndDateCal
										.getTime()).getDate())
						.setParameter("isActive", isActive);
				;
			} else if (category == Payee.TYPE_TAX_AGENCY) {

				query = session
						.getNamedQuery("getTAXAgencyList")
						.setParameter("companyId", companyId)
						.setParameter(
								"currentMonthStartDateCal",
								new FinanceDate(currentMonthStartDateCal
										.getTime()).getDate())
						.setParameter(
								"currentMonthEndDateCal",
								new FinanceDate(currentMonthEndDateCal
										.getTime()).getDate())
						.setParameter(
								"previousFirstMonthStartDateCal",
								new FinanceDate(previousFirstMonthStartDateCal
										.getTime()).getDate())
						.setParameter(
								"previousFirstMonthEndDateCal",
								new FinanceDate(previousFirstMonthEndDateCal
										.getTime()).getDate())
						.setParameter(
								"previousSecondMonthStartDateCal",
								new FinanceDate(previousSecondMonthStartDateCal
										.getTime()).getDate())
						.setParameter(
								"previousSecondMonthEndDateCal",
								new FinanceDate(previousSecondMonthEndDateCal
										.getTime()).getDate())
						.setParameter(
								"previousThirdMonthStartDateCal",
								new FinanceDate(previousThirdMonthStartDateCal
										.getTime()).getDate())
						.setParameter(
								"previousThirdMonthEndDateCal",
								new FinanceDate(previousThirdMonthEndDateCal
										.getTime()).getDate())
						.setParameter(
								"previousFourthMonthStartDateCal",
								new FinanceDate(previousFourthMonthStartDateCal
										.getTime()).getDate())
						.setParameter(
								"previousFourthMonthEndDateCal",
								new FinanceDate(previousFourthMonthEndDateCal
										.getTime()).getDate())
						.setParameter(
								"previousFifthMonthStartDateCal",
								new FinanceDate(previousFifthMonthStartDateCal
										.getTime()).getDate())
						.setParameter(
								"previousFifthMonthEndDateCal",
								new FinanceDate(previousFifthMonthEndDateCal
										.getTime()).getDate())
						.setParameter("isActive", isActive);
				;
			}
			List list = query.list();
			if (list != null) {
				Object[] object = null;
				String payeeName = null;
				PayeeList payeeList = null;
				Iterator iterator = list.iterator();
				PaginationList<PayeeList> queryResult = new PaginationList<PayeeList>();
				while ((iterator).hasNext()) {

					object = (Object[]) iterator.next();

					if (payeeName != null && ((String) object[1]) != null
							&& payeeName.equals(object[1])) {

						double currentMount = (Double) object[4];
						payeeList.setCurrentMonth(payeeList.getCurrentMonth()
								+ (currentMount));

						double preMnt = (Double) object[5];
						payeeList.setPreviousMonth(payeeList.getPreviousMonth()
								+ (preMnt));

						double pre2Mnt = (Double) object[6];
						payeeList.setPreviousSecondMonth(payeeList
								.getPreviousSecondMonth() + (pre2Mnt));

						double pre3Mnt = (Double) object[7];
						payeeList.setPreviousThirdMonth(payeeList
								.getPreviousThirdMonth() + (pre3Mnt));

						double pre4mnt = (Double) object[8];
						payeeList.setPreviousFourthMonth(payeeList
								.getPreviousFourthMonth() + (pre4mnt));

						double pre5Mnt = (Double) object[9];
						payeeList.setPreviousFifthMonth(payeeList
								.getPreviousFifthMonth() + (pre5Mnt));

						double yearToDate = (Double) object[10];
						payeeList.setYearToDate(payeeList.getYearToDate()
								+ (yearToDate));

					} else {

						payeeList = new PayeeList();

						payeeList.setActive((object[0] == null ? false
								: ((Boolean) object[0]).booleanValue()));
						payeeName = (String) object[1];
						payeeList.setPayeeName(payeeName);
						payeeList.setType((Integer) object[2]);
						payeeList.setID((Long) object[3]);
						payeeList.setCurrecny((Long) object[13]);

						double currentMonth = (Double) object[4];
						payeeList.setCurrentMonth(currentMonth);
						double preMnt = (Double) object[5];
						payeeList.setPreviousMonth(preMnt);
						double pre2Mnt = (Double) object[6];
						payeeList.setPreviousSecondMonth(pre2Mnt);
						double pre3Mnt = (Double) object[7];
						payeeList.setPreviousThirdMonth(pre3Mnt);
						double pre4Mnt = (Double) object[8];
						payeeList.setPreviousFourthMonth(pre4Mnt);
						double pre5Mnt = (Double) object[9];
						payeeList.setPreviousFifthMonth(pre5Mnt);
						double yearToDate = (Double) object[10];
						payeeList.setYearToDate(yearToDate);
						payeeList.setBalance((Double) object[11]);

						queryResult.add(payeeList);
					}
				}
				total = queryResult.size();
				PaginationList<PayeeList> result = new PaginationList<PayeeList>();
				if (length < 0) {
					result.addAll(queryResult);
				} else {
					int toIndex = start + length;
					if (toIndex > queryResult.size()) {
						toIndex = queryResult.size();
					}
					result.addAll(queryResult.subList(start, toIndex));
				}
				result.setTotalCount(total);
				result.setStart(start);
				return result;
			} else
				throw (new AccounterException(
						AccounterException.ERROR_ILLEGAL_ARGUMENT));
		} catch (AccounterException e) {
			throw (new AccounterException(AccounterException.ERROR_INTERNAL, e));
		}
	}

	public ArrayList<DepositDetail> getDepositDetail(FinanceDate startDate,
			FinanceDate endDate, long companyId) {

		Session session = HibernateUtil.getCurrentSession();
		List list = session.getNamedQuery("getDepositDetail")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate()).list();
		Map<Long, List<DepositDetail>> map = new LinkedHashMap<Long, List<DepositDetail>>();
		List<DepositDetail> depositDetails = new ArrayList<DepositDetail>();
		Iterator it = list.iterator();
		Long tempTransactionID = null;
		double tempAmount = 0;
		while (it.hasNext()) {
			Object[] object = (Object[]) it.next();
			DepositDetail d = new DepositDetail();
			d.setTransactionId((Long) object[0]);
			d.setTransactionType((Integer) object[1]);
			d.setTransactionNumber((String) object[2]);
			d.setTransactionDate(new ClientFinanceDate((Long) object[3]));
			d.setName((String) object[4]);
			d.setAccountName((String) object[5]);
			if (tempTransactionID == null
					|| (d.getTransactionId() != tempTransactionID)) {
				d.setTotal((Double) object[6]);
				tempAmount = d.getTotal();
			} else {
				// double amount = DecimalUtil.isGreaterThan(tempAmount, 0) ? 1
				// * ((Double) object[6]) : ((Double) object[6]);
				d.setTotal((Double) object[6]);
			}
			if (map.containsKey(d.getTransactionId())) {
				map.get(d.getTransactionId()).add(d);
			} else {
				List<DepositDetail> tempList = new ArrayList<DepositDetail>();
				tempList.add(d);
				map.put(d.getTransactionId(), tempList);
			}
			// depositDetails.add(d);
			tempTransactionID = d.getTransactionId();
		}

		Long[] ids = new Long[map.keySet().size()];
		map.keySet().toArray(ids);
		for (Long s : ids)
			depositDetails.addAll(map.get(s));

		return new ArrayList<DepositDetail>(depositDetails);
	}

	public PaginationList<ClientRecurringTransaction> getAllRecurringTransactions(
			long companyId, FinanceDate fromDate, FinanceDate toDate)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		List<RecurringTransaction> transactions = session
				.getNamedQuery("list.RecurringTransaction")
				.setEntity("company", getCompany(companyId))
				.setParameter("fromDate", fromDate)
				.setParameter("toDate", toDate).list();

		PaginationList<ClientRecurringTransaction> clientObjs = new PaginationList<ClientRecurringTransaction>();
		for (RecurringTransaction recurringTransaction : transactions) {
			ClientRecurringTransaction clientObject = new ClientConvertUtil()
					.toClientObject(recurringTransaction,
							ClientRecurringTransaction.class);

			clientObjs.add(clientObject);
		}

		return clientObjs;
	}

	/**
	 * 
	 * @param companyId
	 * @param payee
	 * @param tdate
	 * @return
	 * @throws AccounterException
	 */
	public double getMostRecentTransactionCurreencyFactorBasedOnCurrency(
			long companyId, long currencyId, long tdate)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session
				.getNamedQuery(
						"getMostRecentTransactionCurrencyFactor.orderby.id.basedon.currency")
				.setLong("companyId", companyId)
				.setLong("transactionDate", tdate)
				.setLong("currency", currencyId);

		List list = query.list();
		if (list.isEmpty()) {
			return 1;
		}
		Object factorObj = list.get(0);
		if (factorObj != null) {
			return ((Double) factorObj).doubleValue();
		} else {
			return 1;
		}
	}

	public ClientAccount mergeAcoount(ClientAccount fromClientAccount,
			ClientAccount toClientAccount, long companyId)
			throws AccounterException {

		Session session = HibernateUtil.getCurrentSession();

		Account fromAcc = (Account) session.get(Account.class,
				fromClientAccount.getID());
		Account toAcc = (Account) session.get(Account.class,
				toClientAccount.getID());

		if (fromAcc.getCompany().getId() != companyId
				|| toAcc.getCompany().getId() != companyId) {
			throw new AccounterException(
					AccounterException.ERROR_ILLEGAL_ARGUMENT,
					"Illegal Access for the Object");
		}

		org.hibernate.Transaction tx = session.beginTransaction();
		Company company = getCompany(companyId);
		double mergeBalance = toClientAccount
				.getTotalBalanceInAccountCurrency()
				+ fromClientAccount.getTotalBalanceInAccountCurrency();

		try {
			session.getNamedQuery("update.merge.Account.oldBalance.tonew")
					.setLong("from", toClientAccount.getID())
					.setDouble("balance", mergeBalance)
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.accounttransaction.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery(
					"update.merge.accountcustomercreditmemo.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID())
					.setEntity("company", company).executeUpdate();

			// session.getNamedQuery("update.merge.issuepayment.old.tonew")
			// .setLong("fromID", fromClientAccount.getID())
			// .setLong("toID", toClientAccount.getID())
			// .setEntity("company", company).executeUpdate();

			session.getNamedQuery(
					"update.merge.accounttrasactionexpense.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID()).executeUpdate();

			session.getNamedQuery("update.merge.payexpense.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID())
					.setEntity("company", company).executeUpdate();
			session.getNamedQuery(
					"update.merge.accountreceivepayment.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.transactionitem.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID()).executeUpdate();

			session.getNamedQuery(
					"update.merge.transactionitemeffectingAccount.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID()).executeUpdate();

			session.getNamedQuery("update.merge.journalEntry.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID()).executeUpdate();

			session.getNamedQuery(
					"update.merge.transactionmakedepositentries.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID()).executeUpdate();

			session.getNamedQuery("update.merge.accountwritecheck.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.accounttaxrefund.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.accountpaytax.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.accountcustomerrefud.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery(
					"update.merge.accountcustomerprepay.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.accountvendorrprepay.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.accountcashsale.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.accountcashpurchase.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery(
					"update.merge.accounttransferfundsto.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery(
					"update.merge.accounttransferfundsfrom.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID())
					.setEntity("company", company).executeUpdate();

			Account account = (Account) session.get(Account.class,
					toClientAccount.getID());

			Account fromAccount = (Account) session.get(Account.class,
					fromClientAccount.getID());
			User user = AccounterThreadLocal.get();
			Activity activity = new Activity(company, user, ActivityType.MERGE,
					account);
			session.save(activity);

			company.getAccounts().remove(fromAccount);
			session.saveOrUpdate(company);
			fromAccount.setCompany(null);
			session.delete(fromAccount);
			tx.commit();
			Account toaccount = (Account) session.get(Account.class,
					toClientAccount.getID());
			return (ClientAccount) new ClientConvertUtil().toClientObject(
					toaccount, Util.getClientClass(toaccount));
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		}
		return null;

	}

	public void mergeItem(ClientItem fromClientItem, ClientItem toClientItem,
			long companyId) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction tx = session.beginTransaction();
		Company company = getCompany(companyId);

		Item from = (Item) session.get(Item.class, fromClientItem.getID());
		Item to = (Item) session.get(Item.class, toClientItem.getID());

		if (from.getCompany().getId() != companyId
				|| to.getCompany().getId() != companyId) {
			throw new AccounterException(
					AccounterException.ERROR_ILLEGAL_ARGUMENT,
					"Illegal Access for the Object");
		}

		try {

			session.getNamedQuery("update.mergeItem.oldcost.tonewcost")
					.setLong("from", toClientItem.getID())
					.setBoolean("status", fromClientItem.isActive())
					.setDouble("price", fromClientItem.getSalesPrice())
					.setDouble("p_Price", fromClientItem.getPurchasePrice())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.itemUpdates.old.tonew")
					.setLong("fromID", fromClientItem.getID())
					.setLong("toID", toClientItem.getID()).executeUpdate();

			// session.getNamedQuery("update.merge.itemsalesorder.old.tonew")
			// .setLong("fromID", fromClientItem.getID())
			// .setLong("toID", toClientItem.getID())
			// .setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.trasactionexpense.old.tonew")
					.setLong("fromID", fromClientItem.getID())
					.setLong("toID", toClientItem.getID()).executeUpdate();

			session.getNamedQuery("update.merge.trasactionitem.old.tonew")
					.setLong("fromID", fromClientItem.getID())
					.setLong("toID", toClientItem.getID()).executeUpdate();

			if (fromClientItem.isInventory()) {
				session.getNamedQuery("update.merge.itemUpdate.old.tonew")
						.setLong("fromID", fromClientItem.getID())
						.setLong("toID", toClientItem.getID()).executeUpdate();

				session.getNamedQuery("update.merge.inventoryHistory.old.tonew")
						.setLong("fromID", fromClientItem.getID())
						.setLong("toID", toClientItem.getID()).executeUpdate();

				session.getNamedQuery(
						"update.merge.inventoryAssembly.old.tonew")
						.setLong("fromID", fromClientItem.getID())
						.setLong("toID", toClientItem.getID()).executeUpdate();
				InventoryRemappingService.sheduleRemap(to);

			}

			Item toItem = (Item) session.get(Item.class, toClientItem.getID());

			Item fromItem = (Item) session.get(Item.class,
					fromClientItem.getID());

			User user = AccounterThreadLocal.get();

			Activity activity = new Activity(company, user, ActivityType.MERGE,
					toItem);

			session.save(activity);
			company.getItems().remove(fromItem);
			session.saveOrUpdate(company);
			fromItem.setCompany(null);
			session.delete(fromItem);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		}
	}

	public void recordActivity(Company company, User user, ActivityType type) {
		Activity activity = new Activity(company, user, type);

	}

	/**
	 * This method will create a duplicate transaction for argument transaction.
	 * 
	 * @param recurringTransaction
	 * @return {@link Transaction}
	 * @throws AccounterException
	 * @throws CloneNotSupportedException
	 */
	public static Transaction createDuplicateTransaction(
			RecurringTransaction recurringTransaction)
			throws AccounterException, CloneNotSupportedException {

		Session session = HibernateUtil.getCurrentSession();
		FlushMode flushMode = session.getFlushMode();
		session.setFlushMode(FlushMode.COMMIT);
		try {
			Transaction transaction = recurringTransaction.getTransaction();

			Transaction newTransaction = transaction.clone();
			newTransaction.setCompany(recurringTransaction.getCompany());
			newTransaction.setRecurringTransaction(recurringTransaction);
			newTransaction
					.setNumber(NumberUtils.getNextTransactionNumber(
							newTransaction.getType(),
							recurringTransaction.getCompany()));
			for (TransactionItem transactionItem : newTransaction
					.getTransactionItems()) {
				transactionItem.setTransaction(newTransaction);
				transactionItem.setReferringTransactionItem(null);
				transactionItem.setPurchases(new HashSet<InventoryPurchase>());
			}
			FinanceDate transactionDate = recurringTransaction
					.getNextScheduledTransactionDate();
			if (transactionDate != null) {
				newTransaction.setDate(transactionDate);
				if (newTransaction instanceof Invoice) {
					((Invoice) newTransaction).setDueDate(transactionDate);
					((Invoice) newTransaction).setDeliverydate(transactionDate);
				} else if (newTransaction instanceof EnterBill) {
					((EnterBill) newTransaction).setDueDate(transactionDate);
					((EnterBill) newTransaction)
							.setDeliveryDate(transactionDate);
				} else if (newTransaction instanceof CashPurchase) {
					((CashPurchase) newTransaction)
							.setDeliveryDate(transactionDate);
				} else if (newTransaction instanceof Estimate) {
					((Estimate) newTransaction)
							.setExpirationDate(transactionDate);
					((Estimate) newTransaction)
							.setDeliveryDate(transactionDate);
				} else if (newTransaction instanceof MakeDeposit) {
					for (TransactionDepositItem transactionItem : ((MakeDeposit) newTransaction)
							.getTransactionDepositItems()) {
						transactionItem.setTransaction(newTransaction);
					}
				}
			}
			newTransaction
					.setAccountTransactionEntriesList(new HashSet<AccountTransaction>());
			newTransaction
					.setTaxRateCalculationEntriesList(new HashSet<TAXRateCalculation>());
			newTransaction
					.setReconciliationItems(new HashSet<ReconciliationItem>());
			newTransaction.setAttachments(new HashSet<Attachment>());
			newTransaction.setLastActivity(null);
			newTransaction.setHistory(null);
			return newTransaction;
		} finally {
			session.setFlushMode(flushMode);
		}
	}

	/**
	 * this method is used to send Pdf as an attachment in email
	 * 
	 * @param objectID
	 * @param type
	 * @param brandingThemeId
	 * @param mimeType
	 * @param subject
	 * @param content
	 * @param senderEmail
	 * @param toEmail
	 * @param ccEmail
	 * @return
	 * @throws TemplateSyntaxException
	 * @throws IOException
	 */
	public void sendPdfInMail(String fileName, String subject, String content,
			ClientEmailAccount sender, String toEmail, String ccEmail,
			long companyId) throws AccounterException {

		Company company = getCompany(companyId);
		String companyName = company.getTradingName();

		File file = new File(ServerConfiguration.getTmpDir(), fileName);
		// Sending the pdf as a mail with transaction Number,removing the other
		// Numbers
		if (fileName.contains("_")) {
			fileName = fileName.substring(0, fileName.lastIndexOf("_"))
					+ ".pdf";
		}

		try {
			UsersMailSendar.sendPdfMail(fileName, file, companyName, subject,
					content, sender, toEmail, ccEmail);
		} catch (IOException e) {
			throw new AccounterException(e.getMessage());
		}

	}

	/**
	 * to generate PDF File for Invoice
	 * 
	 * @param endDate
	 * @param startDate
	 * 
	 * @throws Exception
	 */
	public List<String> createPdfFile(String objectID, int type,
			long brandingThemeId, long companyId, ClientFinanceDate startDate,
			ClientFinanceDate endDate) throws Exception {
		BrandingTheme brandingTheme = (BrandingTheme) getManager()
				.getServerObjectForid(AccounterCoreType.BRANDINGTHEME,
						brandingThemeId);
		Company company = getCompany(companyId);
		if (company == null) {
			return null;
		}
		return getPrintManager().generatePDFFile(company, brandingTheme, type,
				objectID, startDate, endDate);
	}

	/**
	 * For profit and loss by location query.
	 * 
	 * @param isLocation
	 * 
	 * @param startDate
	 * @param endDate
	 * @return {@link ArrayList<ProfitAndLossByLocation>}
	 */

	public PaginationList<ClientBudget> getBudgetList(long companyId)
			throws DAOException {

		Session session = HibernateUtil.getCurrentSession();

		try {

			ArrayList<Budget> budgetList = new ArrayList<Budget>(session
					.getNamedQuery("list.Budget")
					.setEntity("company", getCompany(companyId)).list());

			PaginationList<ClientBudget> clientBudgetObjs = new PaginationList<ClientBudget>();

			for (Budget budget : budgetList) {
				ClientBudget clientObject = new ClientConvertUtil()
						.toClientObject(budget, ClientBudget.class);

				clientBudgetObjs.add(clientObject);
			}

			return clientBudgetObjs;

		} catch (Exception e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}

	}

	public List<ClientReconciliationItem> getAllTransactionsOfAccount(long id,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		List list = session
				.getNamedQuery("getTransactionsOfAccount")
				.setLong("companyId", companyId)
				.setLong("accountId", id)
				.setParameter("endDate", endDate.getDate())
				.setLong("openingBalanceAccount",
						company.getOpeningBalancesAccount().getID()).list();
		List<ClientReconciliationItem> reconciliationItems = new ArrayList<ClientReconciliationItem>();

		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {

			Object[] objects = (Object[]) iterator.next();

			Transaction trasnaction = (Transaction) session.get(
					Transaction.class, (Long) objects[0]);

			ReconciliationItem reconciliationItem = new ReconciliationItem(
					trasnaction);

			Double amount = (Double) objects[1];

			reconciliationItem.setAmount(amount);

			reconciliationItems
					.add((ClientReconciliationItem) new ClientConvertUtil()
							.toClientObject(reconciliationItem,
									Util.getClientClass(reconciliationItem)));

		}
		return reconciliationItems;
	}

	/**
	 * @param accountID
	 */
	public ArrayList<ClientReconciliation> getReconciliationsByBankAccountID(
			long accountID, long companyID) throws AccounterException {
		List<Reconciliation> reconciliations = getReconciliationslist(
				accountID, companyID);
		ArrayList<ClientReconciliation> reconciliationsList = new ArrayList<ClientReconciliation>();
		Iterator iterator = reconciliations.iterator();
		ClientConvertUtil convertUtil = new ClientConvertUtil();
		while (iterator.hasNext()) {
			Reconciliation next = (Reconciliation) iterator.next();
			Hibernate.initialize(next.getItems());
			ClientReconciliation clientObject = convertUtil.toClientObject(
					next, ClientReconciliation.class);
			reconciliationsList.add(clientObject);
		}
		return reconciliationsList;

	}

	public List<Reconciliation> getReconciliationslist(long accountID,
			long companyID) {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyID);
		List list = session.getNamedQuery("get.reconciliations.by.accountId")
				.setLong("accountID", accountID).setEntity("company", company)
				.list();
		return list;

	}

	/**
	 * @param accountID
	 * @return
	 */
	public double getOpeningBalanceforReconciliation(long accountID,
			long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		List list = session
				.getNamedQuery(
						"get.OpeningBalance.Of.Account.from.Reconciliations")
				.setLong("accountID", accountID).setEntity("company", company)
				.list();
		if (list.isEmpty()) {
			return 0.0;
		}
		return ((Reconciliation) list.get(0)).getClosingBalance();
	}

	/**
	 * @param transactionId
	 * @param transactionId2
	 * @param noteDescription
	 * @throws AccounterException
	 */
	public long createNote(long companyId, long transactionId,
			String noteDescription) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		try {
			Company company = getCompany(companyId);
			TransactionLog noteHistory = new TransactionLog(
					TransactionLog.TYPE_NOTE);
			noteHistory.setCompany(company);
			Transaction transaction2 = (Transaction) session.get(
					Transaction.class, transactionId);
			noteHistory.setDescription(noteDescription);
			noteHistory.setCompany(company);
			noteHistory.setTransaction(transaction2);
			List<TransactionLog> history = transaction2.getHistory();
			history.add(noteHistory);
			session.saveOrUpdate(transaction2);
			transaction.commit();
			return noteHistory.getID();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
			throw new AccounterException(AccounterException.ERROR_INTERNAL, e);
		}

	}

	/**
	 * Return Transactions All Activity
	 * 
	 * @param transactionId
	 * @return
	 * @throws AccounterException
	 */
	public List<ClientTransactionLog> getTransactionHistory(long transactionId,
			long companyId) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Transaction transaction = (Transaction) session.get(Transaction.class,
				transactionId);
		if (transaction == null) {
			return new ArrayList<ClientTransactionLog>();
		}
		List<TransactionLog> history = transaction.getHistory();
		List<ClientTransactionLog> transactionLogs = new ArrayList<ClientTransactionLog>();
		Iterator iterator = history.iterator();

		while (iterator.hasNext()) {
			TransactionLog log = (TransactionLog) iterator.next();
			ClientTransactionLog clientTransactionLog = new ClientConvertUtil()
					.toClientObject(log, ClientTransactionLog.class);
			transactionLogs.add(clientTransactionLog);
		}
		return transactionLogs;
	}

	public SalesManager getSalesManager() {
		if (salesManager == null) {
			salesManager = new SalesManager();
		}
		return salesManager;
	}

	public CustomerManager getCustomerManager() {
		if (customerManager == null) {
			customerManager = new CustomerManager();
		}
		return customerManager;
	}

	public VendorManager getVendorManager() {
		if (vendorManager == null) {
			vendorManager = new VendorManager();
		}
		return vendorManager;
	}

	public ReportManager getReportManager() {
		if (reportManager == null) {
			reportManager = new ReportManager();
		}
		return reportManager;
	}

	public PurchaseManager getPurchageManager() {
		if (purchaseManager == null) {
			purchaseManager = new PurchaseManager();
		}
		return purchaseManager;
	}

	public TaxManager getTaxManager() {
		if (taxManager == null) {
			taxManager = new TaxManager();
		}
		return taxManager;
	}

	public DashboardManager getDashboardManager() {
		if (dashboardManager == null) {
			dashboardManager = new DashboardManager();
		}
		return dashboardManager;
	}

	public FixedAssestManager getFixedAssetManager() {
		if (fixedAssestManager == null) {
			fixedAssestManager = new FixedAssestManager();
		}
		return fixedAssestManager;
	}

	public InventoryManager getInventoryManager() {
		if (inventoryManager == null) {
			inventoryManager = new InventoryManager();
		}
		return inventoryManager;
	}

	public UserManager getUserManager() {
		if (userManager == null) {
			userManager = new UserManager();
		}
		return userManager;
	}

	public CompanyManager getCompanyManager() {
		if (companyManager == null) {
			companyManager = new CompanyManager();
		}
		return companyManager;
	}

	public ExportManager getExportManager() {
		if (exportManager == null) {
			exportManager = new ExportManager(this);
		}
		return exportManager;
	}

	public Manager getManager() {
		if (manager == null) {
			manager = new Manager();
		}
		return manager;
	}

	public PayrollManager getPayrollManager() {
		if (payrollManager == null) {
			payrollManager = new PayrollManager();
		}
		return payrollManager;
	}

	public boolean createMessage(Message message) {
		Session session = null;
		try {
			session = HibernateUtil.getCurrentSession();

			if (message.getId() == 0) {
				Query messageQuery = session.getNamedQuery("getMessageByValue")
						.setParameter("value", message.getValue());
				Message oldMessage = (Message) messageQuery.uniqueResult();
				if (oldMessage != null) {
					Set<Key> keys = oldMessage.getKeys();
					keys.addAll(message.getKeys());
					oldMessage.setKeys(keys);
					message = oldMessage;
				}
			}

			org.hibernate.Transaction transaction = session.beginTransaction();
			session.saveOrUpdate(message);
			transaction.commit();

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// public ClientMessage getNextMessage(String lang, int lastMessageId)
	// throws AccounterException {
	// Session session = null;
	// try {
	// session = HibernateUtil.openSession();
	// Query query = session.getNamedQuery("getNextMessageId")
	// .setParameter("lastMessageId", lastMessageId);
	// int messageId = 0;
	// Object[] object = (Object[]) query.uniqueResult();
	// if (object != null) {
	// messageId = (Integer) object[0];
	// Integer tot = (Integer) object[1];
	// }
	//
	// if (messageId == 0) {
	// return null;
	// }
	// ClientMessage clientMessage = getMessage(messageId, lang);
	// int i = 0;
	// return clientMessage;
	// } catch (Exception e) {
	// return null;
	// } finally {
	// if (session != null) {
	// session.close();
	// }
	// }
	// }

	public PaginationList<ClientMessage> getMessages(int status, String lang,
			String email, int frm, int limit, String searchTerm) {
		Session session = null;
		try {
			session = HibernateUtil.getCurrentSession();
			PaginationList<Message> messages = new PaginationList<Message>();
			switch (status) {
			case ClientMessage.ALL:
				messages = getMessagesList(lang, frm, limit, searchTerm);
				break;

			case ClientMessage.UNTRANSLATED:
				messages = getUntranslatedMessages(lang, frm, limit, searchTerm);
				break;

			case ClientMessage.MYTRANSLATIONS:
				messages = getMyTranslations(lang, email, frm, limit,
						searchTerm);
				break;

			case ClientMessage.UNCONFIRMED:
				messages = getUnApprovedMessages(lang, frm, limit, searchTerm);
				break;

			default:
				break;
			}

			PaginationList<ClientMessage> clientMessages = new PaginationList<ClientMessage>();
			clientMessages.setTotalCount(messages.getTotalCount());

			for (Message message : messages) {

				Set<LocalMessage> localMessages = new HashSet<LocalMessage>();

				for (LocalMessage localMessage : message.getLocalMessages()) {
					if (localMessage.getLang().getLanguageCode().equals(lang)) {
						localMessages.add(localMessage);
					}
				}
				message.setLocalMessages(localMessages);

				ClientMessage clientMessage = new ClientMessage();
				clientMessage.setId(message.getId());
				clientMessage.setValue(message.getValue());
				clientMessage.setComment(message.getComment());
				ArrayList<ClientLocalMessage> clientLocalMessages = new ArrayList<ClientLocalMessage>();
				for (LocalMessage localMessage : localMessages) {
					ClientLocalMessage clientLocalMessage = new ClientLocalMessage();
					clientLocalMessage.setId(localMessage.getId());
					clientLocalMessage.setValue(localMessage.getValue());
					clientLocalMessage.setApproved(localMessage.isApproved());
					clientLocalMessage.setCreateBy(localMessage.getCreatedBy()
							.getFirstName());
					clientLocalMessage.setVotes(localMessage.getUps());
					clientLocalMessages.add(clientLocalMessage);
				}
				clientMessage.setLocalMessages(clientLocalMessages);
				clientMessages.add(clientMessage);
			}
			return clientMessages;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private PaginationList<Message> getUnApprovedMessages(String lang, int frm,
			int limit, String searchTerm) {
		PaginationList<Message> messages = new PaginationList<Message>();

		Session session = null;
		try {
			session = HibernateUtil.getCurrentSession();

			Query approvedMessagesQuery = session
					.getNamedQuery("getUnApprovedMessages")
					.setParameter("lang", lang).setInteger("limt", limit)
					.setInteger("fm", frm)
					.setParameter("searchTerm", "%" + searchTerm + "%");

			int count = ((BigInteger) session
					.getNamedQuery("getCountOfUnApprovedMessages")
					.setParameter("lang", lang)
					.setParameter("searchTerm", "%" + searchTerm + "%")
					.uniqueResult()).intValue();

			messages.setTotalCount(count);

			Iterator iter = approvedMessagesQuery.list().iterator();
			while (iter.hasNext()) {
				Message message = new Message();
				Object[] next = (Object[]) iter.next();
				message.setId(((BigInteger) next[0]).longValue());
				message.setValue((String) next[1]);
				message.setComment((String) next[3]);

				Query localMessagesQuery = session
						.getNamedQuery("getLocalMessagesByMessageId")
						.setParameter("messageId", message.getId())
						.setParameter("lang", lang);
				List<LocalMessage> localMessages = localMessagesQuery.list();

				message.setLocalMessages(new HashSet<LocalMessage>(
						localMessages));
				messages.add(message);
			}
			return messages;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private PaginationList<Message> getMyTranslations(String lang,
			String email, int frm, int limit, String searchTerm) {
		PaginationList<Message> messages = new PaginationList<Message>();

		Session session = null;
		try {
			session = HibernateUtil.getCurrentSession();
			Client client = getUserManager().getClient(email);
			if (client == null) {
				return null;
			}
			Query myTranslationsQuery = session
					.getNamedQuery("getMyTranslations")
					.setParameter("lang", lang).setInteger("fm", frm)
					.setInteger("limt", limit)
					.setParameter("clientId", client.getID())
					.setParameter("searchTerm", "%" + searchTerm + "%");

			int count = ((BigInteger) session
					.getNamedQuery("getCountOfMyTranslations")
					.setParameter("lang", lang)
					.setParameter("clientId", client.getID())
					.setParameter("searchTerm", "%" + searchTerm + "%")
					.uniqueResult()).intValue();

			messages.setTotalCount(count);

			List queryList = myTranslationsQuery.list();
			Iterator i = queryList.iterator();
			while (i.hasNext()) {
				Message message = new Message();
				Object[] next = (Object[]) i.next();
				message.setId(((BigInteger) next[0]).longValue());
				message.setValue((String) next[1]);
				message.setComment((String) next[3]);

				Query localMessagesQuery = session
						.getNamedQuery("getLocalMessagesByMessageId")
						.setParameter("messageId", message.getId())
						.setParameter("lang", lang);
				List<LocalMessage> localMessages = localMessagesQuery.list();

				message.setLocalMessages(new HashSet<LocalMessage>(
						localMessages));
				messages.add(message);
			}
			return messages;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private PaginationList<Message> getUntranslatedMessages(String lang,
			int frm, int limit, String searchTerm) {
		PaginationList<Message> messages = new PaginationList<Message>();

		Session session = null;
		try {
			session = HibernateUtil.getCurrentSession();
			Query messageIdsQuery = session
					.getNamedQuery("getUntranslatedMessages")
					.setParameter("lang", lang).setInteger("limt", limit)
					.setInteger("fm", frm)
					.setParameter("searchTerm", "%" + searchTerm + "%");

			int count = ((BigInteger) session
					.getNamedQuery("getCountOfUntranslatedMessages")
					.setParameter("lang", lang)
					.setParameter("searchTerm", "%" + searchTerm + "%")
					.uniqueResult()).intValue();

			messages.setTotalCount(count);

			List list = messageIdsQuery.list();
			Iterator iterator = list.iterator();
			while (iterator.hasNext()) {
				Message message = new Message();
				Object[] next = (Object[]) iterator.next();
				message.setId(((BigInteger) next[0]).longValue());
				message.setValue((String) next[1]);
				message.setComment((String) next[3]);
				message.setLocalMessages(new HashSet<LocalMessage>());
				messages.add(message);
			}
			return messages;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private PaginationList<Message> getMessagesList(String lang, int frm,
			int limit, String searchTerm) {
		PaginationList<Message> messages = new PaginationList<Message>();

		Session session = null;
		try {
			session = HibernateUtil.getCurrentSession();
			Query query = session.getNamedQuery("getMessagesByLimit")
					.setInteger("fm", frm).setInteger("limt", limit)
					.setParameter("lang", lang)
					.setParameter("searchTerm", "%" + searchTerm + "%");
			int count = ((BigInteger) session
					.getNamedQuery("getCountOfMessages")
					.setParameter("searchTerm", "%" + searchTerm + "%")
					.uniqueResult()).intValue();

			messages.setTotalCount(count);

			List list2 = query.list();
			Iterator iterator1 = list2.iterator();
			while (iterator1.hasNext()) {
				Message message = new Message();
				Object[] next = (Object[]) iterator1.next();

				message.setId(((BigInteger) next[0]).longValue());
				message.setValue((String) next[1]);
				message.setComment((String) next[3]);

				Query localMessagesQuery = session
						.getNamedQuery("getLocalMessagesByMessageId")
						.setParameter("messageId", message.getId())
						.setParameter("lang", lang);
				List<LocalMessage> localMessages = localMessagesQuery.list();

				message.setLocalMessages(new HashSet<LocalMessage>(
						localMessages));

				messages.add(message);
			}
			return messages;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean addVote(long localMessageId, String userEmail) {
		Session session = null;
		try {
			session = HibernateUtil.getCurrentSession();

			Client client = getUserManager().getClient(userEmail);
			if (client == null) {
				return false;
			}
			Query query = session.getNamedQuery("getLocalMessageById")
					.setParameter("id", localMessageId);
			LocalMessage localMessage = (LocalMessage) query.uniqueResult();
			if (localMessage == null) {
				return false;
			}

			long clientId = client.getID();
			Query voteQuery = session.getNamedQuery("getVoteByClientId")
					.setParameter("clientId", clientId)
					.setParameter("localMessageId", localMessage.getId());
			Vote vote = (Vote) voteQuery.uniqueResult();

			if (vote == null) {
				vote = new Vote();
				vote.setLocalMessage(localMessage);
				vote.setClient(client);
				localMessage.setUps(localMessage.getUps() + 1);

				org.hibernate.Transaction voteTransaction = session
						.beginTransaction();
				session.saveOrUpdate(vote);
				voteTransaction.commit();

				org.hibernate.Transaction transaction = session
						.beginTransaction();
				session.saveOrUpdate(localMessage);
				transaction.commit();
			}

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public ClientLocalMessage addTranslation(String userEmail, long id,
			String lang, String value) {
		Session session = null;
		try {
			session = HibernateUtil.getCurrentSession();

			Client client = getUserManager().getClient(userEmail);
			if (client == null) {
				return null;
			}

			Query namedQuery = session.getNamedQuery("getLocalMessageByClient")
					.setParameter("clientId", client.getID())
					.setParameter("messageId", id).setParameter("lang", lang);
			LocalMessage uniqueResult = (LocalMessage) namedQuery
					.uniqueResult();
			if (uniqueResult != null) {

				Query deleteVotesQuery = session.getNamedQuery(
						"deleteVotesByLocalMessage").setParameter("id",
						uniqueResult.getId());
				Query deleteQuery = session.getNamedQuery("deleteLocalMessage")
						.setParameter("id", uniqueResult.getId());

			}

			Query messageQuery = session.getNamedQuery("getMessageById")
					.setParameter("id", id);
			Message message = (Message) messageQuery.uniqueResult();
			Set<LocalMessage> localMessages2 = message.getLocalMessages();
			for (LocalMessage localMessage : localMessages2) {
				if (localMessage.getCreatedBy() != null
						&& localMessage.getCreatedBy().equals(client)
						&& localMessage.getLang().equals(lang)) {
					return null;
				}
			}

			Query languageQuery = session.getNamedQuery("getLanguageById")
					.setParameter("code", lang);
			Language language = (Language) languageQuery.uniqueResult();

			LocalMessage localMessage = new LocalMessage();
			localMessage.setMessage(message);
			localMessage.setLang(language);
			localMessage.setValue(value);
			localMessage.setCreatedDate(new Date(System.currentTimeMillis()));
			localMessage.setCreatedBy(client);

			org.hibernate.Transaction transaction = session.beginTransaction();
			session.saveOrUpdate(localMessage);
			transaction.commit();

			addVote(localMessage.getId(), userEmail);

			org.hibernate.Transaction mesgTransaction = session
					.beginTransaction();
			Set<LocalMessage> localMessages = message.getLocalMessages();
			localMessages.add(localMessage);
			message.setLocalMessages(localMessages);
			session.saveOrUpdate(message);
			mesgTransaction.commit();

			ClientLocalMessage clm = new ClientLocalMessage();
			clm.setApproved(localMessage.isApproved());
			if (localMessage.getCreatedBy() != null) {
				clm.setCreateBy(localMessage.getCreatedBy().getEmailId());
			}
			clm.setId(localMessage.getId());
			clm.setValue(localMessage.getValue());
			clm.setVotes(localMessage.getUps());

			return clm;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// public ArrayList<Status> getTranslationStatus() {
	// Session session = null;
	// try {
	// session = HibernateUtil.openSession();
	//
	// ArrayList<Status> list = new ArrayList<Status>();
	//
	// List<String> languages = getLanguages();
	//
	// Query noOfMessagesQuery = session.getNamedQuery("getNoOfMessages");
	// BigInteger noOfMessages = (BigInteger) noOfMessagesQuery
	// .uniqueResult();
	//
	// for (String language : languages) {
	//
	// Query noOfTranslatedMessagesQuery = session.getNamedQuery(
	// "getNoOfMessagesTranslatedByLang").setParameter("lang",
	// language);
	// BigInteger translatedMessages = (BigInteger) noOfTranslatedMessagesQuery
	// .uniqueResult();
	//
	// Query noOfApprovedMessagesQuery = session.getNamedQuery(
	// "getNoOfMessagesApprovedByLang").setParameter("lang",
	// language);
	// BigInteger approvedMessages = (BigInteger) noOfApprovedMessagesQuery
	// .uniqueResult();
	//
	// Status status = new Status();
	// status.setTotal(noOfMessages.intValue());
	// status.setLang(language);
	// status.setTranslated(translatedMessages.intValue());
	// status.setApproved(approvedMessages.intValue());
	//
	// list.add(status);
	// }
	// return list;
	// } catch (Exception e) {
	// return null;
	// } finally {
	// if (session != null) {
	// session.close();
	// }
	// }
	//
	// }

	public List<Message> getAllMessages() {
		Session session = null;
		try {
			session = HibernateUtil.getCurrentSession();

			Query query = session.getNamedQuery("getMessages");
			List<Message> list = query.list();

			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean setApprove(long id, boolean isApproved) {
		Session session = null;
		try {
			session = HibernateUtil.getCurrentSession();

			Query query = session.getNamedQuery("getLocalMessageById")
					.setParameter("id", id);
			LocalMessage localMessage = (LocalMessage) query.uniqueResult();

			if (localMessage == null) {
				return false;
			}

			Query messagesQuery = session
					.getNamedQuery("getLocalMessagesByMessageId")
					.setParameter("messageId",
							localMessage.getMessage().getId())
					.setParameter("lang",
							localMessage.getLang().getLanguageCode());
			List<LocalMessage> list = messagesQuery.list();

			for (LocalMessage locMessage : list) {
				if (locMessage.isApproved()) {
					locMessage.setApproved(false);
					org.hibernate.Transaction transaction = session
							.beginTransaction();
					session.saveOrUpdate(locMessage);
					transaction.commit();
				}
			}

			localMessage.setApproved(isApproved);

			org.hibernate.Transaction transaction = session.beginTransaction();
			session.saveOrUpdate(localMessage);
			transaction.commit();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteMessage(Message message) {
		Session session = null;
		try {
			session = HibernateUtil.getCurrentSession();
			org.hibernate.Transaction transaction = session.beginTransaction();
			session.delete(message);
			transaction.commit();

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private long updatePrimaryCurrency(long companyId, ClientCurrency currency)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Currency existingCurrency = company.getCurrency(currency
				.getFormalName());
		if (existingCurrency == null) {
			existingCurrency = new Currency();
			existingCurrency = new ServerConvertUtil().toServerObject(
					existingCurrency, currency, session);
			existingCurrency.setCompany(company);
			session.save(existingCurrency);
		}
		company.getPreferences().setPrimaryCurrency(existingCurrency);
		Query query = session
				.getNamedQuery("update.primay.currency.in.company")
				.setParameter("companyId", companyId)
				.setParameter("currencyId", existingCurrency.getID());
		query.executeUpdate();
		return existingCurrency.getID();
	}

	/**
	 * Return all local messages of given language for the given client. All
	 * Values must be single quote escaped for Javascript
	 * 
	 * ex: that's => that\'s
	 * 
	 * @param clientId
	 * @param langCode
	 * @return
	 */
	public HashMap<String, String> getKeyAndValues(long clientId,
			String langCode) {
		// Replace("'", "\\'")
		Session session = null;
		try {
			session = HibernateUtil.getCurrentSession();

			Query query = session.getNamedQuery("getKeyAndValues")
					.setParameter("clientId", clientId)
					.setParameter("lang", langCode);
			List list = query.list();

			HashMap<String, String> result = new HashMap<String, String>();

			Iterator iterator = list.iterator();
			while (iterator.hasNext()) {
				Object[] next = (Object[]) iterator.next();
				String key = (String) next[0];
				String value = (String) next[1];
				if (result.containsKey(key)) {
					continue;
				}
				// String replace = value.replace("'", "\\'");
				result.put(key, value);
			}

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<ClientLanguage> getLanguages() {
		Session session = null;
		try {
			session = HibernateUtil.getCurrentSession();

			Query query = session.getNamedQuery("getLanguages");
			List<Language> list = query.list();

			ArrayList<ClientLanguage> result = new ArrayList<ClientLanguage>();

			for (Language language : list) {
				ClientLanguage clientLanguage = new ClientLanguage(
						language.getLanguageTooltip(),
						language.getLangugeName(), language.getLanguageCode());
				result.add(clientLanguage);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean canApprove(String userEmail, String lang) {
		Session session = null;
		try {
			session = HibernateUtil.getCurrentSession();
			Client client = getUserManager().getClient(userEmail);
			for (Language language : client.getLanguages()) {
				if (language.getLanguageCode().equals(lang)) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean savePortletPageConfig(
			ClientPortletPageConfiguration pageConfiguration) {
		if (pageConfiguration == null) {
			return false;
		}
		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction tx = null;
		try {
			tx = session.beginTransaction();
			PortletPageConfiguration serverObj = null;
			String pageName = pageConfiguration.getPageName();
			User user = AccounterThreadLocal.get();
			long userId = user.getID();
			Query query = session.getNamedQuery("getPortletPageConfiguration")
					.setParameter("pageName", pageName)
					.setParameter("userId", userId);
			serverObj = (PortletPageConfiguration) query.uniqueResult();
			if (serverObj == null) {
				serverObj = new PortletPageConfiguration();
				serverObj.setUser(user);
			}
			if (serverObj.getID() != 0) {
				pageConfiguration.setId(serverObj.getID());
			}
			serverObj = new ServerConvertUtil().toServerObject(serverObj,
					(IAccounterCore) pageConfiguration, session);
			session.saveOrUpdate(serverObj);
			tx.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			return false;
		} finally {
		}
	}

	public ClientPortletPageConfiguration getPortletPageConfiguration(
			String pageName) {
		if (pageName == null) {
			return null;
		}
		Session session = HibernateUtil.getCurrentSession();
		try {
			long userId = AccounterThreadLocal.get().getID();
			long companyId = AccounterThreadLocal.get().getCompany().getId();
			Query query = session.getNamedQuery("getPortletPageConfiguration")
					.setParameter("pageName", pageName)
					.setParameter("userId", userId)
			/* .setParameter("companyId", companyId) */;
			PortletPageConfiguration pageConfiguration = (PortletPageConfiguration) query
					.uniqueResult();
			ClientPortletPageConfiguration clientPortletPageConfiguration;
			if (pageConfiguration != null) {
				clientPortletPageConfiguration = new ClientConvertUtil()
						.toClientObject(pageConfiguration,
								ClientPortletPageConfiguration.class);
				return clientPortletPageConfiguration;
			} else {
				ClientCompany company = new ClientConvertUtil().toClientObject(
						getCompany(companyId), ClientCompany.class);
				return company.getPortletPageConfiguration(pageName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
		}
	}

	/**
	 * getting the recent Transactions
	 * 
	 * @param companyId
	 * @param limit
	 * @return List<RecentTransactionsList>
	 */
	public ArrayList<RecentTransactionsList> getRecentTransactionsList(
			long companyId, int limit) {
		Session session = HibernateUtil.getCurrentSession();
		try {
			List l = session.getNamedQuery("getRecentTransactionList")
					.setParameter("companyId", companyId)
					.setParameter("limit", limit).list();
			Object[] object = null;
			Iterator iterator = l.iterator();
			ArrayList<RecentTransactionsList> activities = new ArrayList<RecentTransactionsList>();
			while (iterator.hasNext()) {
				RecentTransactionsList recentTransactionsList = new RecentTransactionsList();
				object = (Object[]) iterator.next();
				recentTransactionsList.setID(((BigInteger) object[0])
						.longValue());
				recentTransactionsList.setType((Integer) (object[1]));
				double amount = (Double) (object[2]);
				if (DecimalUtil.isEquals(amount, 0.0)) {
					continue;
				}
				recentTransactionsList.setAmount((Double) (object[2]));
				recentTransactionsList.setName(object[3] != null ? String
						.valueOf(object[3]) : null);
				recentTransactionsList
						.setTransactionDate(object[4] == null ? null
								: new ClientFinanceDate(
										((BigInteger) object[4]).longValue()));
				recentTransactionsList.setCurrecyId(object[5] == null ? null
						: ((BigInteger) object[5]).longValue());
				recentTransactionsList
						.setEstimateType(object[6] != null ? (Integer) object[6]
								: 0);
				activities.add(recentTransactionsList);
			}
			return activities;
		} catch (Exception e) {
			System.err.println(e);
		}
		return null;
	}

	public boolean deleteTransactionFromDb(Long companyId, IAccounterCore obj)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		org.hibernate.Transaction transaction = session.beginTransaction();

		IAccounterServerCore serverObject = null;
		serverObject = new ServerConvertUtil().toServerObject(serverObject,
				obj, session);

		if (serverObject == null) {
			throw new AccounterException(
					AccounterException.ERROR_ILLEGAL_ARGUMENT);
		}

		if (serverObject instanceof Transaction) {
			Transaction trans = (Transaction) serverObject;
			trans.onDelete(session);
		}

		Query query = session.getNamedQuery("getTaxreturnByTransactionid")
				.setParameter("transaction", serverObject);
		List<TAXReturnEntry> list = query.list();

		for (TAXReturnEntry taxReturnEntry : list) {
			taxReturnEntry.setTransaction(null);
			session.save(taxReturnEntry);
		}

		Class<?> clientClass = ObjectConvertUtil.getEqivalentClientClass(obj
				.getObjectType().getClientClassSimpleName());
		Class<?> serverClass = ObjectConvertUtil
				.getServerEqivalentClass(clientClass);

		if (canDelete(serverClass.getSimpleName(), obj.getID(), companyId)) {
			session.delete(serverObject);
		} else {
			throw new AccounterException(AccounterException.ERROR_OBJECT_IN_USE);
		}

		transaction.commit();

		return true;
	}

	public boolean createOrSkipTransactions(List<ClientReminder> records,
			boolean isCreate, long companyId) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction hibernateTransaction = session
				.beginTransaction();
		if (!isCreate) {
			// If user wanted to skip transactions.
			for (ClientReminder obj : records) {
				Reminder reminder = null;
				reminder = new ServerConvertUtil().toServerObject(reminder,
						obj, session);
				RecurringTransaction recurringTransaction = reminder
						.getRecurringTransaction();
				recurringTransaction.getReminders().remove(reminder);
				session.saveOrUpdate(recurringTransaction);
			}
		} else {
			for (ClientReminder obj : records) {
				Reminder reminder = null;
				reminder = new ServerConvertUtil().toServerObject(reminder,
						obj, session);

				Transaction transaction = null;
				try {
					transaction = createDuplicateTransaction(reminder
							.getRecurringTransaction());
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
					throw new AccounterException(e.getMessage());
				}
				FinanceDate transactionDate = reminder.getTransactionDate();
				if (transactionDate != null) {
					transaction.setDate(transactionDate);
					if (transaction instanceof Invoice) {
						((Invoice) transaction).setDueDate(transactionDate);
					} else if (transaction instanceof EnterBill) {
						((EnterBill) transaction).setDueDate(transactionDate);
					} else if (transaction instanceof CashPurchase) {
						((CashPurchase) transaction)
								.setDeliveryDate(transactionDate);
					} else if (transaction instanceof Estimate) {
						((Estimate) transaction)
								.setExpirationDate(transactionDate);
						((Estimate) transaction)
								.setDeliveryDate(transactionDate);
					}
				}
				session.saveOrUpdate(transaction);
				RecurringTransaction recurringTransaction = reminder
						.getRecurringTransaction();
				recurringTransaction.getReminders().remove(reminder);
				session.saveOrUpdate(recurringTransaction);
			}
		}
		deleteRecurringTask(session, companyId);
		hibernateTransaction.commit();
		return true;
	}

	public void deleteRecurringTask(Session session, long companyId) {
		List<Reminder> list = session.getNamedQuery("getReminders")
				.setLong("companyId", companyId).list();

		if (list == null || list.isEmpty()) {
			MessageOrTask task = (MessageOrTask) session
					.getNamedQuery("getRecurringReminderTask")
					.setParameter("companyId", companyId).uniqueResult();
			session.delete(task);
		}
	}

	public void doCreateIssuePaymentEffect(Long companyId,
			ClientIssuePayment obj) {

		List<ClientTransactionIssuePayment> transactionIssuePayment = obj
				.getTransactionIssuePayment();
		long accountId = obj.getAccount();
		for (ClientTransactionIssuePayment clientTransactionIssuePayment : transactionIssuePayment) {
			Company company = getCompany(companyId);
			String nextCheckNumber = NumberUtils.getNextCheckNumber(companyId,
					accountId);
			if (UIUtils.toLong(obj.getCheckNumber()) > UIUtils
					.toLong(nextCheckNumber)) {
				nextCheckNumber = obj.getCheckNumber();
			}

			Transaction transaction = null;
			int recordType = clientTransactionIssuePayment.getRecordType();
			switch (recordType) {
			case ClientTransaction.TYPE_WRITE_CHECK:
				transaction = getTransactionById(
						clientTransactionIssuePayment.getWriteCheck(), company);
				WriteCheck writeCheck = (WriteCheck) transaction;
				writeCheck.setCheckNumber(nextCheckNumber);
				transaction = writeCheck;
				break;

			case ClientTransaction.TYPE_CASH_PURCHASE:
			case ClientTransaction.TYPE_CASH_EXPENSE:
			case ClientTransaction.TYPE_EMPLOYEE_EXPENSE:
				transaction = getTransactionById(
						clientTransactionIssuePayment.getCashPurchase(),
						company);
				CashPurchase cashPurchase = (CashPurchase) transaction;
				cashPurchase.setCheckNumber(nextCheckNumber);
				transaction = cashPurchase;
				break;

			case ClientTransaction.TYPE_CUSTOMER_REFUNDS:
				transaction = getTransactionById(
						clientTransactionIssuePayment.getCustomerRefund(),
						company);
				CustomerRefund customerRefund = (CustomerRefund) transaction;
				customerRefund.setCheckNumber(nextCheckNumber);
				transaction = customerRefund;
				break;

			case ClientTransaction.TYPE_PAY_TAX:
				transaction = getTransactionById(
						clientTransactionIssuePayment.getPaySalesTax(), company);
				PayTAX payTax = (PayTAX) transaction;
				payTax.setCheckNumber(nextCheckNumber);
				transaction = payTax;
				break;

			case ClientTransaction.TYPE_PAY_BILL:

				transaction = getTransactionById(
						clientTransactionIssuePayment.getPayBill(), company);
				PayBill payBill = (PayBill) transaction;
				payBill.setCheckNumber(nextCheckNumber);
				transaction = payBill;
				break;

			case ClientTransaction.TYPE_VENDOR_PAYMENT:
				transaction = getTransactionById(
						clientTransactionIssuePayment.getPayBill(), company);
				VendorPrePayment vendorPayment = (VendorPrePayment) transaction;
				vendorPayment.setCheckNumber(nextCheckNumber);
				transaction = vendorPayment;
				break;

			case ClientTransaction.TYPE_CREDIT_CARD_CHARGE:
			case ClientTransaction.TYPE_CREDIT_CARD_EXPENSE:
				transaction = getTransactionById(
						clientTransactionIssuePayment.getCreditCardCharge(),
						company);
				CreditCardCharge creditCardCharge = (CreditCardCharge) transaction;
				creditCardCharge.setCheckNumber(nextCheckNumber);
				transaction = creditCardCharge;
				break;

			case ClientTransaction.TYPE_RECEIVE_TAX:
				transaction = getTransactionById(
						clientTransactionIssuePayment.getReceiveVAT(), company);
				ReceiveVAT receiveVat = (ReceiveVAT) transaction;
				receiveVat.setCheckNumber(nextCheckNumber);
				transaction = receiveVat;
				break;

			case ClientTransaction.TYPE_CUSTOMER_PREPAYMENT:
				transaction = getTransactionById(
						clientTransactionIssuePayment.getCustomerPrepayment(),
						company);
				CustomerPrePayment customerPrePayment = (CustomerPrePayment) transaction;
				customerPrePayment.setCheckNumber(nextCheckNumber);
				transaction = customerPrePayment;
				break;

			}
			Session session = HibernateUtil.getCurrentSession();

			org.hibernate.Transaction beginTransaction = session
					.beginTransaction();

			transaction.setStatus(Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED);
			session.save(transaction);

			Query query = session.getNamedQuery("getAccount.by.id")
					.setLong("id", accountId).setEntity("company", company);
			Account account = (Account) query.uniqueResult();
			account.setLastCheckNum(nextCheckNumber);
			session.save(account);

			beginTransaction.commit();
		}

	}

	public Transaction getTransactionById(long transactionId, Company company) {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getTransaction.by.id")
				.setParameter("transactionId", transactionId)
				.setEntity("company", company);
		Transaction trans = (Transaction) query.uniqueResult();
		return trans;
	}

	List<ClientAdvertisement> getAdvertisements() throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getAdvertisements");
		List<Advertisement> list = query.list();
		List<ClientAdvertisement> adds = new ArrayList<ClientAdvertisement>();
		for (Advertisement advertisement : list) {
			ClientAdvertisement clientAdvertisement = new ClientConvertUtil()
					.toClientObject(advertisement, ClientAdvertisement.class);
			adds.add(clientAdvertisement);
		}
		return adds;

	}

	public List<ClientTDSTransactionItem> getTDSTransactionItemsList(
			int formType, long companyId) {

		ArrayList<ClientTDSTransactionItem> arrayList = new ArrayList<ClientTDSTransactionItem>();

		Session session = HibernateUtil.getCurrentSession();
		Query query;
		if (formType != TDSChalanDetail.Form27EQ) {
			query = session.getNamedQuery("getTDSPayBillTransactionsList")
					.setParameter("companyId", companyId);
		} else {
			query = session.getNamedQuery(
					"getTDSReceivePaymentTransactionsList").setParameter(
					"companyId", companyId);
		}
		List list = query.list();
		if (list == null) {
			return arrayList;
		}
		Iterator iterator = list.iterator();

		// ArrayList<ClientTDSChalanDetail> chalanList = new
		// ArrayList<ClientTDSChalanDetail>();
		// ArrayList<TDSChalanDetail> chalansGot = new
		// ArrayList<TDSChalanDetail>(
		// session.getNamedQuery("list.TdsChalanDetails")
		// .setEntity("company", getCompany(companyId)).list());
		//
		// for (TDSChalanDetail chalan : chalansGot) {
		// ClientTDSChalanDetail clientObject = null;
		// try {
		// clientObject = new ClientConvertUtil().toClientObject(chalan,
		// ClientTDSChalanDetail.class);
		// } catch (AccounterException e) {
		// e.printStackTrace();
		// }
		// chalanList.add(clientObject);
		// }

		while (iterator.hasNext()) {

			Object[] next = (Object[]) iterator.next();

			Long payeeId = (Long) next[0];
			Double tdsTotal = (Double) next[1];
			Double total = (Double) next[2];
			Long date = (Long) next[3];
			Long trID = (Long) next[4];

			ClientTDSTransactionItem clientTDSTransactionItem = new ClientTDSTransactionItem();
			clientTDSTransactionItem.setVendor(payeeId);
			clientTDSTransactionItem.setTdsAmount(tdsTotal);
			clientTDSTransactionItem.setTotalAmount(total);
			clientTDSTransactionItem.setTransactionDate(date);
			clientTDSTransactionItem.setTransaction(trID);
			clientTDSTransactionItem.setSurchargeAmount(0);
			clientTDSTransactionItem.setEduCess(0);
			clientTDSTransactionItem.setTotalTax(tdsTotal);

			// boolean present = false;
			// if (chalanList.size() > 0) {
			//
			// for (ClientTDSChalanDetail chalan : chalanList) {
			// if (present == true) {
			// break;
			// }
			// for (ClientTDSTransactionItem item : chalan
			// .getTransactionItems()) {
			// if (clientTDSTransactionItem.getTransaction() != item
			// .getTransaction()) {
			// present = false;
			// } else {
			// present = true;
			// break;
			// }
			//
			// }
			// }
			// }
			//
			// if (present == false) {
			arrayList.add(clientTDSTransactionItem);
			// }

		}
		return arrayList;
	}

	public PaginationList<ClientTDSChalanDetail> getTDSChalanDetailsList(
			Long companyId) throws DAOException {
		Session session = HibernateUtil.getCurrentSession();
		PaginationList<ClientTDSChalanDetail> chalanList = new PaginationList<ClientTDSChalanDetail>();

		try {

			ArrayList<TDSChalanDetail> chalansGot = new ArrayList<TDSChalanDetail>(
					session.getNamedQuery("list.TdsChalanDetails")
							.setEntity("company", getCompany(companyId)).list());

			for (TDSChalanDetail chalan : chalansGot) {
				ClientTDSChalanDetail clientObject = new ClientConvertUtil()
						.toClientObject(chalan, ClientTDSChalanDetail.class);

				chalanList.add(clientObject);
			}

			return chalanList;

		} catch (Exception e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));

		}
	}

	public ArrayList<ClientTDSChalanDetail> getTDSChallansForAckNo(
			String ackNo, long companyId) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		ArrayList<ClientTDSChalanDetail> result = new ArrayList<ClientTDSChalanDetail>();

		List<TDSChalanDetail> challansGot = session
				.getNamedQuery("getTDSChallansForAckNo")
				.setParameter("ackNo", ackNo, EncryptedStringType.INSTANCE)
				.setLong("companyId", companyId).list();

		for (TDSChalanDetail chalan : challansGot) {
			ClientTDSChalanDetail clientObject = new ClientConvertUtil()
					.toClientObject(chalan, ClientTDSChalanDetail.class);

			result.add(clientObject);
		}

		return result;
	}

	public ClientTDSDeductorMasters getTDSDeductorMasterDetails(long companyId)
			throws DAOException {

		Session session = HibernateUtil.getCurrentSession();

		try {

			TDSDeductorMasters deductor = (TDSDeductorMasters) session
					.getNamedQuery("getTdsDeductor")
					.setLong("companyId", companyId).uniqueResult();

			ClientTDSDeductorMasters clientObject = null;
			if (deductor != null) {
				clientObject = new ClientConvertUtil().toClientObject(deductor,
						ClientTDSDeductorMasters.class);

			}
			return clientObject;

		} catch (Exception e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public List<ClientETDSFillingItem> getEtdsList(int formNo, int quater,
			ClientFinanceDate fromDate, ClientFinanceDate toDate,
			int startYear, int endYear, Long companyId) throws DAOException {
		Session session = HibernateUtil.getCurrentSession();

		List<ClientETDSFillingItem> etdsList = new ArrayList<ClientETDSFillingItem>();

		boolean isForm27EQ = (formNo == TDSChalanDetail.Form27EQ);
		try {

			Query query = session
					.getNamedQuery("getTdsChalanDetails")
					.setEntity("company", getCompany(companyId))
					.setParameter("formNum", formNo)
					.setParameter("quarter", quater)
					.setParameter("fromDate",
							new FinanceDate(fromDate.getDate()))
					.setParameter("toDate", new FinanceDate(toDate.getDate()))
					.setParameter("startYear", startYear + 1)
					.setParameter("endYear", endYear + 1);

			ArrayList<TDSChalanDetail> chalansGot = (ArrayList<TDSChalanDetail>) query
					.list();
			for (TDSChalanDetail chalan : chalansGot) {
				int i = 1;

				for (TDSTransactionItem item : chalan.getTdsTransactionItems()) {
					ClientETDSFillingItem eTDSObj = new ClientETDSFillingItem();
					eTDSObj.setSerialNo(i);

					double total = 0;
					total = chalan.getIncomeTaxAmount()
							+ chalan.getSurchangePaidAmount()
							+ chalan.getEducationCessAmount()
							+ chalan.getInterestPaidAmount()
							+ chalan.getOtherAmount()
							+ chalan.getPenaltyPaidAmount();

					eTDSObj.setBankBSRCode(chalan.getBankBsrCode());
					eTDSObj.setChalanSerialNumber(chalan
							.getChalanSerialNumber());
					eTDSObj.setSectionForPayment(chalan.getPaymentSection());
					eTDSObj.setTotalTDSfordeductees(total);
					eTDSObj.setDateTaxDeposited(chalan.getDateTaxPaid());
					eTDSObj.setDeducteeID(item.getVendor().getID());
					eTDSObj.setPanOfDeductee(item.getVendor().getTaxId());
					eTDSObj.setDateOFpayment(item.getTransactionDate()
							.getDate());
					eTDSObj.setAmountPaid(item.getTotalAmount());
					eTDSObj.setTds(item.getTdsAmount());
					eTDSObj.setSurcharge(item.getSurchargeAmount());
					eTDSObj.setEducationCess(item.getEduCess());
					eTDSObj.setTotalTaxDEducted(item.getTotalTax());
					eTDSObj.setTotalTaxDeposited(item.getTotalTax());
					eTDSObj.setDateofDeduction(item.getTransactionDate()
							.getDate());
					Transaction transaction = item.getTransaction();
					if (transaction instanceof PayBill) {
						eTDSObj.setTaxRate(((PayBill) transaction)
								.getTdsTaxItem().getTaxRate());
					}
					if (chalan.isBookEntry())
						eTDSObj.setBookEntry("YES");
					else
						eTDSObj.setBookEntry("NO");

					if (!chalan.isVoid()) {
						etdsList.add(eTDSObj);
					}
					i++;
				}
			}
			return etdsList;
		} catch (Exception e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public boolean updateAckNoForChallans(int formNo, int quater,
			ClientFinanceDate fromDate, ClientFinanceDate toDate,
			int startYear, int endYear, String ackNo, long date, long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query query = session
					.getNamedQuery("getTdsChalanDetails")
					.setEntity("company", getCompany(companyId))
					.setParameter("formNum", formNo)
					.setParameter("quarter", quater)
					.setParameter("fromDate",
							new FinanceDate(fromDate.getDate()))
					.setParameter("toDate", new FinanceDate(toDate.getDate()))
					.setParameter("startYear", startYear + 1)
					.setParameter("endYear", endYear + 1);

			ArrayList<TDSChalanDetail> chalansGot = (ArrayList<TDSChalanDetail>) query
					.list();

			for (TDSChalanDetail challan : chalansGot) {
				challan.setEtdsfillingAcknowledgementNo(ackNo);
				challan.setAcknowledgementDate(new FinanceDate(date));
				challan.setFiled(true);

				session.saveOrUpdate(challan);
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return false;
		}
		return true;
	}

	public List<ClientTDSChalanDetail> getChalanList(int formNo, int quater,
			ClientFinanceDate fromDate, ClientFinanceDate toDate,
			int startYear, int endYear, Long companyId) throws DAOException {
		Session session = HibernateUtil.getCurrentSession();

		ArrayList<ClientTDSChalanDetail> chalanList = new ArrayList<ClientTDSChalanDetail>();
		try {

			Query query = session
					.getNamedQuery("getTdsChalanDetails")
					.setEntity("company", getCompany(companyId))
					.setParameter("formNum", formNo)
					.setParameter("quarter", quater)
					.setParameter("fromDate",
							new FinanceDate(fromDate.getDate()))
					.setParameter("toDate", new FinanceDate(toDate.getDate()))
					.setParameter("startYear", startYear + 1)
					.setParameter("endYear", endYear + 1);

			ArrayList<TDSChalanDetail> chalansGot = (ArrayList<TDSChalanDetail>) query
					.list();
			int i = 1;
			for (TDSChalanDetail chalan : chalansGot) {

				ClientTDSChalanDetail clientObject = new ClientConvertUtil()
						.toClientObject(chalan, ClientTDSChalanDetail.class);
				chalanList.add(clientObject);
			}
			return chalanList;
		} catch (Exception e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public ClientTDSResponsiblePerson getResponsiblePersonDetails(long companyId)
			throws DAOException {
		Session session = HibernateUtil.getCurrentSession();

		try {

			TDSResponsiblePerson person = (TDSResponsiblePerson) session
					.getNamedQuery("getTdsResposiblePersonDetails")
					.setLong("companyId", companyId).uniqueResult();

			ClientTDSResponsiblePerson clientObject = null;

			if (person != null) {
				clientObject = new ClientConvertUtil().toClientObject(person,
						ClientTDSResponsiblePerson.class);
			}
			return clientObject;

		} catch (Exception e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public boolean savePortletConfiguration(
			ClientPortletConfiguration configuration) {
		if (configuration == null) {
			return false;
		}
		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction tx = null;
		try {
			tx = session.beginTransaction();
			PortletConfiguration serverObj = new PortletConfiguration();
			serverObj = new ServerConvertUtil().toServerObject(serverObj,
					(IAccounterCore) configuration, session);
			session.saveOrUpdate(serverObj);
			tx.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			return false;
		} finally {
		}
	}

	public String getIRASFileInformationByDate(Long companyId, long startDate,
			long endDate, boolean isXml) {
		Session session = HibernateUtil.getCurrentSession();

		IRASInformation gstInformation = new IRASInformation();
		Company company = getCompany(companyId);

		IRASCompanyInfo companyInfo = new IRASCompanyInfo();
		companyInfo.setCompanyName(company.getTradingName());
		Map<String, String> companyFields = company.getPreferences()
				.getCompanyFields();
		String companyUEN = companyFields.get("CompanyUEN");
		String gstNo = companyFields.get("GST No");
		companyInfo.setCompanyUEN(companyUEN);
		companyInfo.setGSTNo(gstNo);
		companyInfo.setPeriodStart(new FinanceDate(startDate));

		companyInfo.setPeriodEnd(new FinanceDate(endDate));
		companyInfo.setIAFCreationDate(new FinanceDate());
		companyInfo.setProductVersion("Anna Accounting");
		companyInfo.setIAFVersion("IAFv1.0.0");

		gstInformation.setCompanyInfo(companyInfo);

		List<IRASPurchaseLineInfo> purchaseLineInfo = new ArrayList<IRASPurchaseLineInfo>();

		Query query = session.getNamedQuery("getPurchaseLineItems")
				.setLong("startDate", startDate).setLong("endDate", endDate)
				.setParameter("companyId", companyId);
		List list = query.list();
		Iterator iterator = list.iterator();

		while (iterator.hasNext()) {
			IRASPurchaseLineInfo gstPurchaseLineInfo = new IRASPurchaseLineInfo();
			Object[] next = (Object[]) iterator.next();

			String vendorName = (String) next[0];

			Query namedQuery = session
					.getNamedQuery("getVendor.by.name")
					.setParameter("name", vendorName,
							EncryptedStringType.INSTANCE)
					.setEntity("company", company);
			Vendor vendor = (Vendor) namedQuery.uniqueResult();
			String vendorUEN = vendor.getPayeeFields().get("CompanyUEN");
			gstPurchaseLineInfo.setSupplierName(vendorName);
			gstPurchaseLineInfo.setSupplierUEN(vendorUEN);
			gstPurchaseLineInfo.setInvoiceDate(new FinanceDate((Long) next[1]));
			gstPurchaseLineInfo.setInvoiceNo((String) next[2]);
			gstPurchaseLineInfo.setPermitNo("");
			gstPurchaseLineInfo.setProductDescription((String) next[3]);

			double price = (Double) next[4];

			gstPurchaseLineInfo.setTaxCode((String) next[5]);

			double taxAmount = (Double) next[6];

			String currencyName = (String) next[7];
			double currencyFactor = (Double) next[8];

			gstPurchaseLineInfo.setPurchaseValueSGD(price * currencyFactor);
			gstPurchaseLineInfo.setGSTValueSGD(taxAmount * currencyFactor);
			gstPurchaseLineInfo.setLineNo((Long) next[9] + 1);

			if (currencyName.equals("SGD")) {
				gstPurchaseLineInfo.setFCYCode("XXX");
			} else {
				gstPurchaseLineInfo.setFCYCode(currencyName);
				gstPurchaseLineInfo.setPurchaseFCY(price);
				gstPurchaseLineInfo.setGSTFCY(taxAmount);
			}

			purchaseLineInfo.add(gstPurchaseLineInfo);

		}
		gstInformation.setPurchaseLines(purchaseLineInfo);

		List<IRASSupplyLineInfo> supplyLineInfo = new ArrayList<IRASSupplyLineInfo>();

		Query supplyQuery = session.getNamedQuery("getSupplyLineItems")
				.setLong("startDate", startDate).setLong("endDate", endDate)
				.setParameter("companyId", companyId);
		Iterator iterator1 = supplyQuery.list().iterator();

		while (iterator1.hasNext()) {
			IRASSupplyLineInfo gstSupplyLineInfo = new IRASSupplyLineInfo();
			Object[] next = (Object[]) iterator1.next();

			String customerName = (String) next[0];

			Query namedQuery = session
					.getNamedQuery("getCustomer.by.name")
					.setParameter("name", customerName,
							EncryptedStringType.INSTANCE)
					.setEntity("company", company);
			Customer customer = (Customer) namedQuery.uniqueResult();
			String customerUEN = customer.getPayeeFields().get("CustomerUEN");

			gstSupplyLineInfo.setCustomerName(customerName);
			gstSupplyLineInfo.setCustomerUEN(customerUEN);
			gstSupplyLineInfo.setInvoiceDate(new FinanceDate((Long) next[1]));
			gstSupplyLineInfo.setInvoiceNo((String) next[2]);
			gstSupplyLineInfo.setProductDescription((String) next[3]);

			double price = (Double) next[4];

			gstSupplyLineInfo.setTaxCode((String) next[5]);

			double taxAmount = (Double) next[6];

			String currencyName = (String) next[7];
			double currencyFactor = (Double) next[8];

			gstSupplyLineInfo.setSupplyValueSGD(price * currencyFactor);
			gstSupplyLineInfo.setGSTValueSGD(taxAmount * currencyFactor);

			gstSupplyLineInfo.setCountry((String) next[9]);
			gstSupplyLineInfo.setLineNo((Long) next[10] + 1);

			if (currencyName.equals("SGD")) {
				gstSupplyLineInfo.setFCYCode("XXX");
			} else {
				gstSupplyLineInfo.setFCYCode(currencyName);
				gstSupplyLineInfo.setSupplyFCY(price);
				gstSupplyLineInfo.setGSTFCY(taxAmount);
			}

			supplyLineInfo.add(gstSupplyLineInfo);

		}
		gstInformation.setSupplyLines(supplyLineInfo);

		List<IRASGeneralLedgerLineInfo> glLineInfo = new ArrayList<IRASGeneralLedgerLineInfo>();

		Query glQuery = session.getNamedQuery("getGLLineItems")
				.setLong("startDate", startDate).setLong("endDate", endDate)
				.setParameter("companyId", companyId);
		Iterator iterator2 = glQuery.list().iterator();

		while (iterator2.hasNext()) {
			IRASGeneralLedgerLineInfo gstGLLineInfo = new IRASGeneralLedgerLineInfo();
			Object[] next = (Object[]) iterator2.next();

			gstGLLineInfo.setTransactionDate(new FinanceDate((Long) next[0]));
			gstGLLineInfo.setAccountID((String) next[1]);
			gstGLLineInfo.setAccountName((String) next[2]);
			gstGLLineInfo.setTransactionDescription((String) next[3]);
			gstGLLineInfo.setName((String) next[4]);
			long transactionId = (Long) next[5];
			gstGLLineInfo.setTransactionID(String.valueOf(transactionId));

			gstGLLineInfo.setCredit((Double) next[6]);
			gstGLLineInfo.setDebit((Double) next[7]);
			gstGLLineInfo.setBalance((Double) next[8]);

			Query namedQuery = session.getNamedQuery("getTransaction.by.id")
					.setLong("transactionId", transactionId)
					.setEntity("company", company);
			Transaction transaction = (Transaction) namedQuery.uniqueResult();

			if (transaction != null) {
				String documentId = getSourceDocumentId(transaction);
				String sourceType = getSourceType(transaction);

				gstGLLineInfo.setSourceDocumentID(documentId);
				gstGLLineInfo.setSourceType(sourceType);
			}

			glLineInfo.add(gstGLLineInfo);

		}
		gstInformation.setGeneralLedgerLines(glLineInfo);

		if (isXml) {
			return createXmlFile(gstInformation);
		} else {
			return createTxtFile(gstInformation);
		}

	}

	protected String createTxtFile(IRASInformation result) {
		FileOutputStream stream;
		try {
			File file = new File(ServerConfiguration.getTmpDir(),
					SecureUtils.createID() + ".txt");
			stream = new FileOutputStream(file);

			DataOutputStream outputStream = new DataOutputStream(stream);

			result.toTxt(outputStream);

			return file.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected String createXmlFile(IRASInformation result) {
		FileOutputStream stream;
		try {
			File file = new File(ServerConfiguration.getTmpDir(),
					SecureUtils.createID() + ".xml");

			stream = new FileOutputStream(file);

			result.toXML(stream);

			return file.getName();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	String PURCHASES = "Purchases";
	String CASH_RECEIPT = "Cash Receipt";
	String AR = "AR";
	String AP = "AP";
	String INVENTORY = "Inventory";
	String SALES = "Sales";
	String CASH_DISBURSEMENT = "Cash Disbursement";
	String GENERAL_JOURNAL = "General Journal";
	private PrintPDFManager printPDFManager;

	private String getSourceType(Transaction transaction) {
		switch (transaction.getType()) {
		case Transaction.TYPE_CASH_PURCHASE:
		case Transaction.TYPE_CASH_EXPENSE:
		case Transaction.TYPE_PAY_BILL:
			return PURCHASES;

		case Transaction.TYPE_CASH_SALES:
			return CASH_RECEIPT;

		case Transaction.TYPE_CUSTOMER_CREDIT_MEMO:
		case Transaction.TYPE_CUSTOMER_PRE_PAYMENT:
		case Transaction.TYPE_CUSTOMER_REFUNDS:
		case Transaction.TYPE_INVOICE:
		case Transaction.TYPE_RECEIVE_TAX:
			return AR;

		case Transaction.TYPE_ENTER_BILL:
		case Transaction.TYPE_VENDOR_CREDIT_MEMO:
		case Transaction.TYPE_EXPENSE:
		case Transaction.TYPE_CREDIT_CARD_CHARGE:
		case Transaction.TYPE_PAY_TAX:
			return AP;

		case Transaction.TYPE_JOURNAL_ENTRY:
		case Transaction.TYPE_TRANSFER_FUND:
		case Transaction.TYPE_ADJUST_SALES_TAX:
		case Transaction.TYPE_TAX_RETURN:
		case Transaction.TYPE_WRITE_CHECK:
			return GENERAL_JOURNAL;

		case Transaction.TYPE_RECEIVE_PAYMENT:
			return SALES;

		default:
			return null;
		}
	}

	private String getSourceDocumentId(Transaction transaction) {

		switch (transaction.getType()) {
		case Transaction.TYPE_CASH_PURCHASE:
		case Transaction.TYPE_CASH_SALES:
		case Transaction.TYPE_CUSTOMER_CREDIT_MEMO:
		case Transaction.TYPE_ENTER_BILL:
		case Transaction.TYPE_JOURNAL_ENTRY:
		case Transaction.TYPE_TRANSFER_FUND:
		case Transaction.TYPE_PAY_BILL:
		case Transaction.TYPE_VENDOR_CREDIT_MEMO:
		case Transaction.TYPE_ADJUST_SALES_TAX:
		case Transaction.TYPE_TAX_RETURN:
		case Transaction.TYPE_RECEIVE_TAX:
		case Transaction.TYPE_CREDIT_CARD_CHARGE:
		case Transaction.TYPE_CREDIT_CARD_EXPENSE:
		case Transaction.TYPE_CASH_EXPENSE:
		case Transaction.TYPE_PAY_TAX:
		case Transaction.TYPE_INVOICE:
			return transaction.getNumber();

		case Transaction.TYPE_CUSTOMER_PRE_PAYMENT:
			if (transaction.getPaymentMethod().equals("Check")) {
				CustomerPrePayment customerPrePayment = (CustomerPrePayment) transaction;
				return customerPrePayment.getCheckNumber();
			} else {
				return transaction.getNumber();
			}

		case Transaction.TYPE_CUSTOMER_REFUNDS:
			if (transaction.getPaymentMethod().equals("Check")) {
				CustomerRefund customerRefund = (CustomerRefund) transaction;
				return customerRefund.getCheckNumber();
			} else {
				return transaction.getNumber();
			}

		case Transaction.TYPE_RECEIVE_PAYMENT:
			if (transaction.getPaymentMethod().equals("Check")) {
				ReceivePayment receivePayment = (ReceivePayment) transaction;
				return receivePayment.getCheckNumber();
			} else {
				return transaction.getNumber();
			}

		case Transaction.TYPE_WRITE_CHECK:
			if (transaction.getPaymentMethod().equals("Check")) {
				WriteCheck writeCheck = (WriteCheck) transaction;
				return writeCheck.getCheckNumber();
			} else {
				return transaction.getNumber();
			}

			// case Transaction.TYPE_PAY_EXPENSE:
			// if (transaction.getPaymentMethod().equals("Check")) {
			// PayExpense payExpense = (PayExpense) transaction;
			// return payExpense.getCheckNumber();
			// } else {
			// return transaction.getNumber();
			// }

		}
		return "";
	}

	public ArrayList<TDSChalanDetail> getChalanList(FinanceDate startDate,
			FinanceDate endDate, String acknowledgementNo, long companyId) {
		Session session = HibernateUtil.getCurrentSession();

		try {

			Query query = session
					.getNamedQuery("getTdsChalanDetailsByAcknowledgementNo")
					.setEntity("company", getCompany(companyId))
					.setParameter("startDate", startDate.getDate())
					.setParameter("endDate", endDate.getDate())
					.setParameter("acknowledgementNo", acknowledgementNo,
							EncryptedStringType.INSTANCE);

			return (ArrayList<TDSChalanDetail>) query.list();
		} catch (Exception e) {
			try {
				throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
			} catch (DAOException e1) {
				e1.printStackTrace();
			}
		}
		return new ArrayList<TDSChalanDetail>();
	}

	/**
	 * If accountId = 0, returns all bank statements if accountId != 0 , returns
	 * bank statements of that particular accountId
	 * 
	 * @param companyId
	 * @return
	 */
	public PaginationList<ClientStatement> getBankStatements(long accountId,
			long companyId) {

		Session session = HibernateUtil.getCurrentSession();

		PaginationList<ClientStatement> statements = new PaginationList<ClientStatement>();
		try {
			@SuppressWarnings("unchecked")
			ArrayList<Statement> listStatements = new ArrayList<Statement>(
					session.getNamedQuery("list.bankStatement")
							.setParameter("companyId", companyId).list());
			for (Statement statement : listStatements) {
				ClientStatement clientObject = new ClientConvertUtil()
						.toClientObject(statement, ClientStatement.class);

				if (accountId == 0) {// for displaying in BankStatementsView,
										// add all statement objects
					statements.add(clientObject);
				} else if (accountId != 0) {
					if (accountId == clientObject.getAccount()) {// for a
																	// particular
						// accountId in
						// statementObject
						statements.add(clientObject);
					}
				}

			}
		} catch (AccounterException e) {
			e.printStackTrace();
		}
		return statements;

	}

	public HashMap<Integer, Object> importData(long companyId,
			String userEmail, String filePath, int importerType,
			Map<String, String> importMap, String dateFormate)
			throws AccounterException {
		Map<Integer, Object> exceptions = new HashMap<Integer, Object>();
		try {
			Importer<? extends IAccounterCore> importer = getImporterByType(
					importerType, importMap, dateFormate, companyId);
			long successCount = 0;
			String[] headers = null;
			boolean isHeader = true;
			File file = new File(filePath);
			CSVReader reader = new CSVReader(new FileReader(file));
			OperationContext context = new OperationContext(companyId,
					(IAccounterCore) null, userEmail);
			String[] nextLine;
			int currentLine = 0;
			while ((nextLine = reader.readNext()) != null) {
				Map<String, String> columnNameValueMap = new HashMap<String, String>();
				if (isHeader) {
					headers = nextLine;
					isHeader = false;
				} else {
					currentLine++;
					if (nextLine.length == headers.length) {
						for (int i = 0; i < nextLine.length; i++) {
							String value = nextLine[i].trim().replaceAll("\"",
									"");
							columnNameValueMap.put(headers[i], value);
						}
						try {
							List<AccounterException> loadResult = importer
									.loadData(columnNameValueMap);
							if (!loadResult.isEmpty()) {
								exceptions.put(currentLine, loadResult);
								continue;
							}

							IAccounterCore data = importer.getData();
							context.setData(data);
							create(context);
						} catch (AccounterException e) {
							exceptions.put(currentLine, e);
							continue;
						}
						successCount++;
					}
				}
			}
			if (exceptions.isEmpty()) {
				file.delete();
			}
			exceptions.put(0, successCount);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AccounterException(e.getMessage());
		}
		return (HashMap<Integer, Object>) exceptions;
	}

	private Importer<? extends IAccounterCore> getImporterByType(
			int importerType, Map<String, String> importMap, String dateFormat,
			long companyId) {
		Importer<? extends IAccounterCore> importerByType = getImporterByType(importerType);
		importerByType.updateFields(importMap);
		importerByType.setDateFormat(dateFormat);
		importerByType.setCompanyId(companyId);
		getCompany(companyId);
		return importerByType;
	}

	private Importer<? extends IAccounterCore> getImporterByType(
			int importerType) {
		switch (importerType) {
		case ImporterType.INVOICE:
			return new InvoiceImporter();
		case ImporterType.CUSTOMER:
			return new CustomerImporter();
		case ImporterType.VENDOR:
			return new VendorImporter();
		case ImporterType.ITEM:
			return new ItemImporter();
		case ImporterType.ACCOUNT:
			return new AccountImporter();
		}
		return null;
	}

	public List<ImportField> getFieldsOfImporter(int importerType) {
		Importer<? extends IAccounterCore> importerByType = getImporterByType(importerType);
		return importerByType.getFields();
	}

	public long getAccountByNumberOrName(long companyId,
			String accountNoOrName, boolean isAccountName) {
		if (!isAccountName) {
			return getAccountByNumber(companyId, accountNoOrName);
		} else {
			return getAccountByName(companyId, accountNoOrName);
		}
	}

	public Long getAccountByNumber(Long companyId, String accountNumber) {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Object uniqueResult = session
				.getNamedQuery("getAccountByNumber")
				.setParameter("company", company)
				.setParameter("accountNumber", accountNumber,
						EncryptedStringType.INSTANCE).uniqueResult();
		if (uniqueResult != null) {
			return (Long) uniqueResult;
		}
		return new Long(0);
	}

	public Long getAccountByName(Long companyId, String accountNameOrNumber) {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Object uniqueResult = session
				.getNamedQuery("getAccountByName")
				.setParameter("company", company)
				.setParameter("accountName", accountNameOrNumber,
						EncryptedStringType.INSTANCE).uniqueResult();
		if (uniqueResult != null) {
			return (Long) uniqueResult;
		}
		return new Long(0);
	}

	public Long getItemGrupByName(Long companyId, String itemGrupName) {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Object uniqueResult = session
				.getNamedQuery("getItemGrupByName")
				.setParameter("company", company)
				.setParameter("itemGrupName", itemGrupName,
						EncryptedStringType.INSTANCE).uniqueResult();
		if (uniqueResult != null) {
			return (Long) uniqueResult;
		}
		return new Long(0);
	}


	public Long getInvoiceFrequencyByCompany(Long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Object uniqueResult = session
				.getNamedQuery("getAllInvoiceFrequencyByCompany")
				.setParameter("company", company)
				.uniqueResult();
		if (uniqueResult != null) {
			return (Long) uniqueResult;
		}
		return new Long(0);
	}

	public Long getMeasurmentByName(Long companyId, String measurement) {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Object uniqueResult = session.getNamedQuery("getMeasurementByName")
				.setParameter("company", company)
				.setParameter("measurement", measurement).uniqueResult();
		if (uniqueResult != null) {
			return (Long) uniqueResult;
		}
		return new Long(0);
	}

	public Long getWarehouseByName(Long companyId, String warehouse) {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Object uniqueResult = session
				.getNamedQuery("getWarehouseByName")
				.setParameter("company", company)
				.setParameter("warehouse", warehouse,
						EncryptedStringType.INSTANCE).uniqueResult();
		if (uniqueResult != null) {
			return (Long) uniqueResult;
		}
		return new Long(0);
	}

	public Long getPayeeByName(Long companyId, String payee) {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Object uniqueResult = session.getNamedQuery("getPayeeIdByName")
				.setParameter("company", company)
				.setParameter("payee", payee, EncryptedStringType.INSTANCE)
				.uniqueResult();
		if (uniqueResult != null) {
			return (Long) uniqueResult;
		}
		return new Long(0);
	}

	public long getCurrencyIdByName(String currency, long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Object uniqueResult = session.getNamedQuery("getCurrencyIdByCode")
				.setParameter("company", company)
				.setParameter("currency", currency).uniqueResult();
		if (uniqueResult != null) {
			return (Long) uniqueResult;
		}
		return new Long(0);
	}

	public void mergeClass(ClientAccounterClass fromClass,
			ClientAccounterClass toClass, Long companyId)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction tx = session.beginTransaction();
		Company company = getCompany(companyId);

		AccounterClass from = (AccounterClass) session.get(
				AccounterClass.class, fromClass.getID());
		AccounterClass to = (AccounterClass) session.get(AccounterClass.class,
				toClass.getID());

		if (from.getCompany().getId() != companyId
				|| to.getCompany().getId() != companyId) {
			throw new AccounterException(
					AccounterException.ERROR_ILLEGAL_ARGUMENT,
					"Illegal Access for the Object");
		}

		try {
			session.getNamedQuery(
					"update.merge.transactionitem.class.old.tonew")
					.setLong("fromID", fromClass.getID())
					.setLong("toID", toClass.getID()).executeUpdate();

			session.getNamedQuery("update.merge.transaction.class.old.tonew")
					.setLong("fromID", fromClass.getID())
					.setLong("toID", toClass.getID()).executeUpdate();

			session.getNamedQuery(
					"update.merge.transaction.deposit.item.class.old.tonew")
					.setLong("fromID", fromClass.getID())
					.setLong("toID", toClass.getID()).executeUpdate();

			User user = AccounterThreadLocal.get();

			AccounterClass toAccounterClass = (AccounterClass) session.get(
					AccounterClass.class, toClass.getID());

			AccounterClass fromAccounterClass = (AccounterClass) session.get(
					AccounterClass.class, fromClass.getID());

			Activity activity = new Activity(company, user, ActivityType.MERGE,
					toAccounterClass);
			session.save(activity);

			company.getAccounterClasses().remove(fromAccounterClass);
			session.saveOrUpdate(company);

			session.delete(fromAccounterClass);
			tx.commit();

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
	}

	public void mergeLocation(ClientLocation fromLocation,
			ClientLocation toLocation, Long companyId)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction tx = session.beginTransaction();
		Company company = getCompany(companyId);

		Location from = (Location) session.get(Location.class,
				fromLocation.getID());
		Location to = (Location) session.get(Location.class,
				fromLocation.getID());

		if (from.getCompany().getId() != companyId
				|| to.getCompany().getId() != companyId) {
			throw new AccounterException(
					AccounterException.ERROR_ILLEGAL_ARGUMENT,
					"Illegal Access for the Object");
		}

		try {
			session.getNamedQuery("update.merge.transaction.location.old.tonew")
					.setLong("fromID", fromLocation.getID())
					.setLong("toID", toLocation.getID()).executeUpdate();

			User user = AccounterThreadLocal.get();

			Location toLocationObj = (Location) session.get(Location.class,
					toLocation.getID());

			Location fromLocationObj = (Location) session.get(Location.class,
					fromLocation.getID());

			Activity activity = new Activity(company, user, ActivityType.MERGE,
					toLocationObj);
			session.save(activity);

			company.getLocations().remove(fromLocationObj);
			session.saveOrUpdate(company);

			session.delete(fromLocationObj);
			tx.commit();

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
	}

	/**
	 * get the new paypal transactions
	 * 
	 * @param companyID
	 * @param accountID
	 * @return
	 */

	public List<ClientPaypalTransation> getnewPaypalTransaction(Long companyID,
			Long accountID) {

		Session session = HibernateUtil.getCurrentSession();

		Account account = (Account) session.get(Account.class, accountID);
		String paypalToken = account.getPaypalToken();
		String paypalSecretkey = account.getPaypalSecretkey();

		PaypalTransactionSearch paypalTransactionSearch = new PaypalTransactionSearch();
		FinanceDate date = new FinanceDate();
		if (account.getEndDate() == null) {
			date.setDate(1);
			date.setMonth(1);
		} else {
			date = account.getEndDate();
		}

		List<ClientPaypalTransation> transactionList = new ArrayList<ClientPaypalTransation>();

		String startDate = date.toString();
		ArrayList<PaypalTransation> transaction = null;
		try {
			transaction = paypalTransactionSearch.getTransaction(startDate,
					getCompany(companyID), paypalToken, paypalSecretkey,
					accountID);
		} catch (JSONException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		for (PaypalTransation paypalTransation : transaction) {

			try {
				ClientPaypalTransation clientObject = new ClientConvertUtil()
						.toClientObject(paypalTransation,
								ClientPaypalTransation.class);
				transactionList.add(clientObject);
			} catch (AccounterException e) {
				e.printStackTrace();
			}

		}
		return transactionList;
	}

	public PaginationList<ClientPaypalTransation> getSavedPaypalTransactions(
			ClientAccount clientAccount, Long companyId) {

		Session session = HibernateUtil.getCurrentSession();

		ArrayList<PaypalTransation> transactions = new ArrayList<PaypalTransation>(
				session.getNamedQuery("list.PaypalTransactions")
						.setEntity("company", getCompany(companyId))
						.setParameter("accountId", clientAccount.getID())
						.list());

		PaginationList<ClientPaypalTransation> clientTransactions = new PaginationList<ClientPaypalTransation>();

		for (PaypalTransation transaction : transactions) {
			ClientPaypalTransation clientObject;
			try {
				clientObject = new ClientConvertUtil().toClientObject(
						transaction, ClientPaypalTransation.class);
				clientTransactions.add(clientObject);
			} catch (AccounterException e) {
				e.printStackTrace();
			}
		}
		return clientTransactions;
	}

	public ArrayList<ClientAccount> getPaypalAccounts(Long companyId) {

		Session session = HibernateUtil.getCurrentSession();
		ArrayList<Account> accounts = new ArrayList<Account>(session
				.getNamedQuery("list.get.PaypalAccounts")
				.setEntity("company", getCompany(companyId))
				.setParameter("type", ClientAccount.TYPE_PAYPAL).list());

		ArrayList<ClientAccount> clientAccounts = new ArrayList<ClientAccount>();

		for (Account account : accounts) {
			ClientAccount clientaccount;
			try {
				clientaccount = new ClientConvertUtil().toClientObject(account,
						ClientAccount.class);
				clientAccounts.add(clientaccount);
			} catch (AccounterException e) {
				e.printStackTrace();
			}
		}
		return clientAccounts;
	}

	public void getCompletePaypalTransactionDetailsForID(String transactionID,
			long accountID) {
		Session session = HibernateUtil.getCurrentSession();

		Account account = (Account) session.get(Account.class, accountID);
		String paypalToken = account.getPaypalToken();
		String paypalSecretkey = account.getPaypalSecretkey();

		PaypalTransactionDetails paypalTransactionDetails = new PaypalTransactionDetails(
				transactionID, paypalToken, paypalSecretkey);
		try {
			paypalTransactionDetails.createHeader();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		paypalTransactionDetails.getTransactionDetails();

	}

	public PrintPDFManager getPrintManager() {
		if (printPDFManager == null) {
			printPDFManager = new PrintPDFManager();
		}
		return printPDFManager;
	}

	public PaginationList<ConsultantsDetailsList> getConsultantItemsDetails(
			long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		try {
			List l = session.getNamedQuery("getTotalWithNamesItems")
					.setParameter("companyId", companyId)
					.list();
			Object[] object = null;
			Iterator iterator = l.iterator();
			PaginationList<ConsultantsDetailsList> activities = new PaginationList<ConsultantsDetailsList>();
			HashMap<String, ConsultantsDetailsList> productMap = new HashMap<String, ConsultantsDetailsList>();
			while (iterator.hasNext()) {
				ConsultantsDetailsList recentTransactionsList = new ConsultantsDetailsList();
				object = (Object[]) iterator.next();
				recentTransactionsList.setId((Long) object[0]);
				recentTransactionsList.setName(object[1] != null ? (String) (object[1]):"");
				recentTransactionsList.setAmount(object[2] != null ? (Double) (object[2]):0);
				recentTransactionsList.setBalance(object[4] != null ? (Double) (object[3]):0);
				recentTransactionsList.setOpeningBalance(object[4] != null ? (Double) (object[4]):0);
				recentTransactionsList.setCurrency(object[5] != null ? (String)object[5]:"");
				recentTransactionsList.setClientName(object[6] != null ? (String)object[6]:"");
				recentTransactionsList.setAddresss(object[7] != null ? (String)object[7]:"");
				recentTransactionsList.setEmail(object[8] != null ? (String)object[8]:"");
				recentTransactionsList.setPhone(object[9] != null ? (String)object[9]:"");
	 		    productMap.put(recentTransactionsList.getId()+"", recentTransactionsList);
			}	
			PropertyParser prop = new PropertyParser();
			try {
				prop.loadFile("config/mysqlconfig.ini");
			} catch (FileNotFoundException e1) {		
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			String databaseUrl = prop.getProperty("sqlDatabaseUrl", null);
			String databaseName = prop.getProperty("sqlDatabaseName", null);
			String username = prop.getProperty("username", null);
			String password = prop.getProperty("password", null);
			MysqlDataSource dataSource = new MysqlDataSource();
			dataSource.setUser(username);
			dataSource.setPassword(password);
			dataSource.setServerName(databaseUrl);
			dataSource.setDatabaseName(databaseName);
			dataSource.setPortNumber(3306);
			dataSource.setPort(3306);
			dataSource.setCharacterEncoding("latin1");
			Connection con;
				try {
					con = dataSource.getConnection();
//							DriverManager.getConnection(  
//							conn.getSchema(),conn.getUsername(),conn.getPassword());
		
				java.sql.Statement stmt = con.createStatement();
				
				String query ="SELECT emp_number as id,\r\n" + 
						"emp_firstname as firstName,\r\n" + 
						"emp_middle_name as middleName,\r\n" + 
						"emp_lastname as lastName,		\r\n" + 
						"emp_street1 as add1,\r\n" + 
						"city_code as add2,\r\n" + 
						"emp_work_email as email, \r\n" + 
						"emp_mobile as phone\r\n" + 
						" FROM rewahrdb.hs_hr_employee where emp_status !=\"EST000\";" ;
				ResultSet rs = stmt.executeQuery(query );
				while(rs.next())
				{
					ConsultantsDetailsList recentTransactionsList = new ConsultantsDetailsList();
					recentTransactionsList.setId(Integer.parseInt(rs.getString("id")));
					recentTransactionsList.setName(rs.getString("firstName")+" "+rs.getString("middleName")+" "+rs.getString("lastName"));
					recentTransactionsList.setAmount(0);
					recentTransactionsList.setBalance(0);
					recentTransactionsList.setOpeningBalance(0);
					recentTransactionsList.setCurrency("INR");
					recentTransactionsList.setClientName("");
					recentTransactionsList.setAddresss(rs.getString("add1")+" "+rs.getString("add2"));
					recentTransactionsList.setEmail(rs.getString("email"));
					recentTransactionsList.setPhone(rs.getString("phone"));
		 		    productMap.put(recentTransactionsList.getId()+"", recentTransactionsList);
				}
				rs.close();
				stmt.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			activities.addAll(productMap.values());
			activities.setTotalCount(productMap.size());
			activities.setStart(0);
			System.out.println(activities.getStart()+"and "+activities.getTotalCount());
			return activities;
		} catch (Exception e) {
			System.err.println(e);
		}
		return null;
	}


    public ConsultantsDetailsList getConsultantItemsDetailsById() {
		ConsultantsDetailsList list = new ConsultantsDetailsList();
		list.setPhone("7777777");
		return list;
    }
}
