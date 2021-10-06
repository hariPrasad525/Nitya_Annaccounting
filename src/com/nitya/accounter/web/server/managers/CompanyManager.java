package com.nitya.accounter.web.server.managers;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.hibernate.dialect.EncryptedStringType;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.Activity;
import com.nitya.accounter.core.ActivityType;
import com.nitya.accounter.core.BankAccount;
import com.nitya.accounter.core.CashPurchase;
import com.nitya.accounter.core.ClientConvertUtil;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.CompanyPreferences;
import com.nitya.accounter.core.Currency;
import com.nitya.accounter.core.Depreciation;
import com.nitya.accounter.core.EnterBill;
import com.nitya.accounter.core.Estimate;
import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.core.FiscalYear;
import com.nitya.accounter.core.FixedAsset;
import com.nitya.accounter.core.Invoice;
import com.nitya.accounter.core.MessageOrTask;
import com.nitya.accounter.core.PortletPageConfiguration;
import com.nitya.accounter.core.RecurringTransaction;
import com.nitya.accounter.core.Reminder;
import com.nitya.accounter.core.ServerConvertUtil;
import com.nitya.accounter.core.Transaction;
import com.nitya.accounter.core.Unit;
import com.nitya.accounter.core.User;
import com.nitya.accounter.core.Utility;
import com.nitya.accounter.core.change.ChangeTracker;
import com.nitya.accounter.core.migration.MigrationUtil;
import com.nitya.accounter.services.DAOException;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientBankAccount;
import com.nitya.accounter.web.client.core.ClientCompany;
import com.nitya.accounter.web.client.core.ClientCompanyPreferences;
import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientMessageOrTask;
import com.nitya.accounter.web.client.core.ClientRecurringTransaction;
import com.nitya.accounter.web.client.core.ClientReminder;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientUnit;
import com.nitya.accounter.web.client.core.ClientUser;
import com.nitya.accounter.web.client.core.ClientUserInfo;
import com.nitya.accounter.web.client.core.CountryPreferences;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.ItemUnitPrice;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.core.SearchInput;
import com.nitya.accounter.web.client.core.SearchResultlist;
import com.nitya.accounter.web.client.core.Lists.DepositsTransfersList;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.server.FinanceTool;
import com.nitya.accounter.web.server.ItemUtils;
import com.nitya.accounter.web.server.OperationContext;
import com.nitya.accounter.web.server.util.CountryPreferenceFactory;
import com.nitya.accounter.web.server.util.ICountryPreferences;

public class CompanyManager extends Manager {

	public void updateDeprecationStartDate(OperationContext context)
			throws AccounterException {
		String arg1 = context.getArg1();

		if (arg1 == null || arg1.isEmpty()) {
			throw new AccounterException(
					"Cann't Update Deprecation Strart Date with Null or Empty");
		}
		FinanceDate newStartDate = new FinanceDate(Long.parseLong(arg1));
		Company company = getCompany(context.getCompanyId());

		CompanyPreferences serverCompanyPreferences1 = company.getPreferences();

		changeDepreciationStartDateTo(newStartDate, company);

		company.setPreferences(serverCompanyPreferences1);
		serverCompanyPreferences1.setDepreciationStartDate(newStartDate);
		// CompanyPreferences serverObject = serverCompanyPreferences1;

		String userID = context.getUserEmail();
		User user1 = company.getUserByUserEmail(userID);

		Activity activity = new Activity(company, user1, ActivityType.EDIT,
				company);
		HibernateUtil.getCurrentSession().save(activity);

		HibernateUtil.getCurrentSession().saveOrUpdate(company);
		ChangeTracker.put(serverCompanyPreferences1);
	}

	private void changeDepreciationStartDateTo(FinanceDate newStartDate,
			Company company) throws AccounterException {

		Session session = HibernateUtil.getCurrentSession();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		// Catching the current Start Long
		FinanceDate startDate = company.getPreferences()
				.getDepreciationStartDate();
		FinanceDate depreciationFirstDate = null;
		Query query = session.getNamedQuery("getDepreciation").setEntity(
				"company", company);
		List<Depreciation> list = query.list();
		if (list != null && list.size() > 0 && list.get(0) != null) {
			Depreciation dep = list.get(0);
			depreciationFirstDate = dep.getDepreciateFrom();
		}

		// Roll-back all the existing depreciation
		if (depreciationFirstDate != null) {
			rollBackDepreciation(depreciationFirstDate, company);
		}

		query = session.getNamedQuery("getFixedAsset.by.purchaseDate")
				.setParameter("purchaseDate", (newStartDate))
				.setEntity("company", company);
		List<FixedAsset> assetsList = query.list();
		for (FixedAsset fixedAsset : assetsList) {

			fixedAsset.setAccumulatedDepreciationAmount(0.0);
			fixedAsset.setSoldOrDisposed(false);
			// fixedAsset.setSellingOrDisposingFixedAsset(null);
			fixedAsset.setBookValue(fixedAsset.getPurchasePrice());
			fixedAsset.setSoldOrDisposedDate(null);
			fixedAsset.setAccountForSale(null);
			fixedAsset.setSalePrice(0.0);
			fixedAsset.setNoDepreciation(false);
			fixedAsset.setDepreciationTillDate(null);
			fixedAsset.setNotes(null);
			fixedAsset.setLossOrGainOnDisposalAccount(null);
			fixedAsset.setTotalCapitalGain(null);
			fixedAsset.setLossOrGain(0.0);

			/**
			 * Saving the action into History
			 */
			// FixedAssetHistory fixedAssetHistory = new FixedAssetHistory();
			// if (fixedAsset.getPurchaseDate().before(newStartDate)) {
			// fixedAsset.setStatus(FixedAsset.STATUS_PENDING);
			//
			// fixedAssetHistory
			// .setActionType(FixedAssetHistory.ACTION_TYPE_ROLLBACK);
			// fixedAssetHistory.setActionDate(new FinanceDate());
			// fixedAssetHistory
			// .setDetails("Fixed Asset Start Long altered from "
			// + format.format(startDate.getAsDateObject())
			// + " to "
			// + format.format(newStartDate.getAsDateObject())
			// + ". All fixed assets journals reversed.");
			//
			// } else {
			//
			// fixedAssetHistory
			// .setActionType(FixedAssetHistory.ACTION_TYPE_NONE);
			// fixedAssetHistory.setActionDate(new FinanceDate());
			// fixedAssetHistory
			// .setDetails("Fixed Asset Start Long altered from "
			// + format.format(startDate.getAsDateObject())
			// + " to "
			// + format.format(newStartDate.getAsDateObject())
			// + ". All fixed assets book values are set");
			//
			// }
			// fixedAsset.getFixedAssetsHistory().add(fixedAssetHistory);
			session.saveOrUpdate(fixedAsset);
			ChangeTracker.put(fixedAsset);

		}

	}

	// SURESH
	public Boolean rollBackDepreciation(long rollBackDepreciationTo,
			long companyId) throws AccounterException {
		return rollBackDepreciation(new FinanceDate(rollBackDepreciationTo),
				getCompany(companyId));
	}

	public boolean rollBackDepreciation(FinanceDate rollBackDepreciationTo,
			Company company) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction t = session.beginTransaction();
		Query query = session.getNamedQuery("getDepreciation.by.ToandStatus")
				.setParameter("deprediationTo", (rollBackDepreciationTo))
				.setParameter("status", Depreciation.APPROVE)
				.setEntity("company", company);
		List<Depreciation> list = query.list();
		for (Depreciation dep : list) {
			// if (dep.getFixedAsset().getStatus() ==
			// FixedAsset.STATUS_REGISTERED) {
			dep.setStatus(Depreciation.ROLLBACK);
			// Rakesh
			dep.setRollBackDepreciationDate(new FinanceDate(
					rollBackDepreciationTo.getDate()));

			// FixedAsset fixedAsset = dep.getFixedAsset();
			// fixedAsset.deleteJournalEntriesTillDate(rollBackDepreciationTo,
			// session);

			session.saveOrUpdate(dep);
			if (dep instanceof Lifecycle) {
				Lifecycle lifecycle = dep;
				lifecycle.onUpdate(session);
			}

			// }
		}
		t.commit();
		// for (Depreciation dep : list) {
		// for (FixedAsset fixedAsset : dep.getFixedAsset()) {
		// if (fixedAsset != null)
		// fixedAsset.getTransactions().clear();
		// }
		// org.hibernate.Transaction t = session.beginTransaction();
		// session.saveOrUpdate(dep);
		//
		// t.commit();
		// }

		return true;

	}

	// RAKESH

	//
	// public void rollBackDepreciation(Long rollBackDepreciationTo)
	// throws DAOException {
	// Session session = HibernateUtil.getCurrentSession();
	// Query query = session
	// .createQuery(
	// "from com.nitya.accounter.core.Transaction T wehre t.transactionDate=?")
	// .setParameter(0, rollBackDepreciationTo);
	//
	// List<Transaction> list = query.list();
	// for (Transaction tran : list) {
	// tran.setVoid(true);
	// org.hibernate.Transaction t = session.beginTransaction();
	// session.saveOrUpdate(tran);
	// if (tran instanceof Lifecycle) {
	// Lifecycle lifecycle = (Lifecycle) tran;
	// lifecycle.onUpdate(session);
	// }
	// t.commit();
	//
	// }
	// }

	public void updateCompanyPreferences(OperationContext context)
			throws AccounterException {

		Session session = HibernateUtil.getCurrentSession();

		org.hibernate.Transaction transaction = session.beginTransaction();
		try {
			IAccounterCore data = context.getData();

			if (data == null) {
				throw new AccounterException(
						AccounterException.ERROR_ILLEGAL_ARGUMENT,
						"Update Company Preferences, as the Source Object could not be Found....");
			}

			Company company = getCompany(context.getCompanyId());
			// String IdentiName =
			// this.getSpace().getIDentity().getDisplayName();

			CompanyPreferences serverCompanyPreferences = company
					.getPreferences();

			serverCompanyPreferences = new ServerConvertUtil().toServerObject(
					serverCompanyPreferences, (ClientCompanyPreferences) data,
					session);

			company.setPreferences(serverCompanyPreferences);

			String userID = context.getUserEmail();
			User user1 = getCompany(context.getCompanyId()).getUserByUserEmail(
					userID);

			Activity activity = new Activity(company, user1, ActivityType.EDIT,
					serverCompanyPreferences);
			session.save(activity);
			session.update(company);
			transaction.commit();
			// CompanyPreferences serverObject = serverCompanyPreferences;
			ChangeTracker.put(serverCompanyPreferences);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			transaction.rollback();
			throw new AccounterException(AccounterException.ERROR_INTERNAL);
		}
	}

	public Long updateCompany(OperationContext context)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		try {
			IAccounterCore data = context.getData();
			if (data == null) {
				throw new AccounterException(
						AccounterException.ERROR_PERMISSION_DENIED,
						"Update Company , as the Source Object could not be Found....");
			}
			ClientCompany clientCompany = (ClientCompany) data;
			Company cmp = getCompany(context.getCompanyId());

			int oldInventoryScheme = cmp.getPreferences()
					.getActiveInventoryScheme();

			int newInventoryScheme = clientCompany.getPreferences()
					.getActiveInventoryScheme();

			createOrUpdatePrimaryCurrency(clientCompany, cmp);

			cmp.updatePreferences(clientCompany);

			String userID = context.getUserEmail();
			User user1 = cmp.getUserByUserEmail(userID);

			Activity activity = new Activity(cmp, user1,
					ActivityType.UPDATE_PREFERENCE);
			session.save(activity);
			session.update(cmp);

			if (oldInventoryScheme != newInventoryScheme) {
				Query countQ = session.getNamedQuery(
						"getAllInventoryTransactionsCount").setLong("cid",
						cmp.getId());
				Long count = Long.valueOf(countQ.uniqueResult().toString());
				if (count != null && count > ItemUtils.MAX_INVENTORY_TXS) {
					throw new AccounterException(
							AccounterException.ERROR_CANT_EDIT,
							"Cannot change inventory scheme. Too many inventory transactions");
				}
				ItemUtils.remapAllInventory(cmp);
			}

			transaction.commit();
			ChangeTracker.put(cmp.toClientCompany());
			return cmp.getID();
		} catch (AccounterException e) {
			transaction.rollback();
			if (e.getErrorCode() != 0) {
				throw e;
			}
			throw new AccounterException(AccounterException.ERROR_INTERNAL);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			transaction.rollback();
			throw new AccounterException(AccounterException.ERROR_INTERNAL);
		}

	}

	private void createOrUpdatePrimaryCurrency(ClientCompany company,
			Company serverCompany) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		ClientCurrency primaryCurrency = company.getPreferences()
				.getPrimaryCurrency();
		Currency existcurrency = serverCompany.getCurrency(primaryCurrency
				.getFormalName());
		ClientConvertUtil clientConvertUtil = new ClientConvertUtil();
		if (existcurrency == null) {
			existcurrency = new Currency();
			existcurrency = new ServerConvertUtil().toServerObject(
					existcurrency, primaryCurrency, session);
			existcurrency.setCompany(getCompany(company.getID()));
			session.saveOrUpdate(existcurrency);
			ClientCurrency clientObject = clientConvertUtil.toClientObject(
					existcurrency, ClientCurrency.class);
			company.getPreferences().setPrimaryCurrency(clientObject);
			company.getCurrencies().add(clientObject);
			ChangeTracker.put(existcurrency);
		} else {
			ClientCurrency clientObject = clientConvertUtil.toClientObject(
					existcurrency, ClientCurrency.class);
			company.getPreferences().setPrimaryCurrency(clientObject);
		}
		serverCompany.getPreferences().setPrimaryCurrency(existcurrency);
	}

	public void updateCompanyStartDate(OperationContext context)
			throws AccounterException {
		String arg1 = context.getArg1();
		if (arg1 == null || arg1.isEmpty()) {
			throw new AccounterException(
					AccounterException.ERROR_PERMISSION_DENIED,
					"Cann't Update the Compamy StartData with Null or Empty");
		}
		FinanceDate modifiedStartDate = new FinanceDate(Long.parseLong(arg1));

		changeFiscalYearsStartDate(modifiedStartDate, context.getCompanyId());

		Company company = getCompany(context.getCompanyId());

		CompanyPreferences serverCompanyPreferences = company.getPreferences();

		company.setPreferences(serverCompanyPreferences);

		serverCompanyPreferences.setStartDate(modifiedStartDate);
		serverCompanyPreferences.setPreventPostingBeforeDate(modifiedStartDate);
		serverCompanyPreferences.setStartOfFiscalYear(modifiedStartDate);
		// CompanyPreferences serverObject = serverCompanyPreferences;
		String userID = context.getUserEmail();
		User user1 = getCompany(context.getCompanyId()).getUserByUserEmail(
				userID);

		Activity activity = new Activity(company, user1, ActivityType.EDIT);
		HibernateUtil.getCurrentSession().save(activity);
		HibernateUtil.getCurrentSession().update(company);
		ChangeTracker.put(serverCompanyPreferences);
	}

	private void changeFiscalYearsStartDate(FinanceDate modifiedStartDate,
			long companyId) {

		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		// List<FiscalYear> list = new ArrayList<FiscalYear>();

		// FinanceDate existingLeastStartDate = modifiedStartDate;
		// FinanceDate existingHighestEndDate = modifiedStartDate;
		// list = session
		// .createQuery(
		// "from com.nitya.accounter.core.FiscalYear f order by f.startDate")
		// .list();

		Query query = session.getNamedQuery("getMinStartDateAndMaxEndDate")
				.setParameter("companyId", companyId);
		List list = query.list();
		Object[] object = (Object[]) list.get(0);

		FinanceDate existingLeastStartDate = new FinanceDate((Long) object[0]);
		FinanceDate existingHighestEndDate = new FinanceDate((Long) object[1]);

		// if (list.size() > 0) {
		// Iterator<FiscalYear> i = list.iterator();
		// FiscalYear firstFiscalYear = list.get(0) != null ? list.get(0)
		// : null;
		// FiscalYear lastFiscalYear = null;
		// while (i.hasNext()) {
		// lastFiscalYear = (FiscalYear) i.next();
		// }
		// existingLeastStartDate = firstFiscalYear.getPreviousStartDate();
		//
		// existingHighestEndDate = lastFiscalYear.getEndDate();

		// if ((modifiedStartDate.before(existingLeastStartDate) ||
		// modifiedStartDate
		// .after(existingHighestEndDate))) {

		Calendar leastStartDateCal = Calendar.getInstance();
		leastStartDateCal.setTime(existingLeastStartDate.getAsDateObject());

		Calendar highestEndDateCal = Calendar.getInstance();
		highestEndDateCal.setTime(existingHighestEndDate.getAsDateObject());

		Calendar modifiedDateCal = Calendar.getInstance();
		modifiedDateCal.setTime(modifiedStartDate.getAsDateObject());

		if (modifiedStartDate.getDate() >= existingLeastStartDate.getDate()
				&& modifiedStartDate.getDate() <= existingHighestEndDate
						.getDate()) {
			return;
		} else if (modifiedStartDate.getDate() < existingLeastStartDate
				.getDate()) {

			// if (modifiedStartDate.before(existingLeastStartDate)) {

			Calendar tempCal = Calendar.getInstance();
			tempCal.setTime(leastStartDateCal.getTime());

			FinanceDate tempDate = existingLeastStartDate;

			// while (tempCal.compareTo(modifiedDateCal) > 0) {
			while (tempDate.getDate() > modifiedStartDate.getDate()) {

				Calendar cal = Calendar.getInstance();

				cal.setTime(tempCal.getTime());
				cal.set(Calendar.MONTH, tempCal.get(Calendar.MONTH) - 12);

				FinanceDate startDate = new FinanceDate(cal.getTime());

				cal.clear();
				cal.setTime(tempCal.getTime());
				cal.set(Calendar.DAY_OF_MONTH,
						tempCal.get(Calendar.DAY_OF_MONTH) - 1);

				FinanceDate endDate = new FinanceDate(cal.getTime());

				FiscalYear fs = new FiscalYear(startDate, endDate,
						FiscalYear.STATUS_OPEN, Boolean.FALSE);
				fs.checkIsCurrentFY(fs);
				session.save(fs);
				ChangeTracker.put(fs);

				tempDate = startDate;

				tempCal.setTime(startDate.getAsDateObject());

			}
			// } else if (modifiedStartDate.after(existingHighestEndDate)) {

		} else if (modifiedStartDate.getDate() > existingHighestEndDate
				.getDate()) {

			Calendar tempCal = Calendar.getInstance();
			tempCal.setTime(highestEndDateCal.getTime());

			FinanceDate tempDate = existingHighestEndDate;

			// while (tempCal.compareTo(modifiedDateCal) < 0) {

			while (tempDate.getDate() < modifiedStartDate.getDate()) {

				Calendar cal = Calendar.getInstance();

				cal.setTime(tempCal.getTime());
				cal.set(Calendar.DAY_OF_MONTH,
						tempCal.get(Calendar.DAY_OF_MONTH) + 1);

				FinanceDate startDate = new FinanceDate(cal.getTime());

				cal.clear();
				cal.setTime(tempCal.getTime());
				cal.set(Calendar.MONTH, tempCal.get(Calendar.MONTH) + 12);

				FinanceDate endDate = new FinanceDate(cal.getTime());
				FiscalYear fs = new FiscalYear(startDate, endDate,
						FiscalYear.STATUS_OPEN, Boolean.FALSE);
				fs.checkIsCurrentFY(fs);
				session.save(fs);
				ChangeTracker.put(fs);

				tempDate = endDate;

				tempCal.setTime(endDate.getAsDateObject());
			}
		}

		// }
		Utility.updateCurrentFiscalYear(getCompany(companyId));
		transaction.commit();

	}

	public ClientCompanyPreferences getClientCompanyPreferences(Company company)
			throws AccounterException {
		ClientCompanyPreferences clientCompanyPreferences = new ClientConvertUtil()
				.toClientObject(company.getPreferences(),
						ClientCompanyPreferences.class);
		return clientCompanyPreferences;
	}

	public ClientCompany getClientCompany(String logInUserEmail, long companyId)
			throws AccounterException {

		Session session = HibernateUtil.getCurrentSession();

		Company company = getCompany(companyId);
		if (company != null && companyId != 0
				&& company.getVersion() < Company.CURRENT_VERSION
				&& company.isConfigured()) {
			MigrationUtil.migrate(company);
		}
		User logInUser = company.getUserByUserEmail(logInUserEmail);
		if (logInUser == null) {
			throw new AccounterException(
					AccounterException.ERROR_PERMISSION_DENIED);
		}
		Set<PortletPageConfiguration> portletPages = logInUser
				.getPortletPages();
		if (!logInUser.isActive()) {
			activateUser(logInUser);
		}

		logInUser = HibernateUtil.initializeAndUnproxy(logInUser);
		Hibernate.initialize(company);

		// company.setAccounts(getAccountsListBySorted());
		//
		// company.setFiscalYears(new
		// ArrayList<FiscalYear>(session.getNamedQuery(
		// "list.FiscalYear").list()));
		//
		// company.setPayees(new ArrayList<Payee>(session.getNamedQuery(
		// "list.Payee").list()));
		//
		// company.setItems(new
		// ArrayList<Item>(session.getNamedQuery("list.Item")
		// .list()));
		//
		// company.setCustomerGroups(new ArrayList<CustomerGroup>(session
		// .getNamedQuery("list.CustomerGroup").list()));
		//
		// company.setVendorGroups(new ArrayList<VendorGroup>(session
		// .getNamedQuery("list.VendorGroup").list()));
		//
		// company.setShippingTerms(new ArrayList<ShippingTerms>(session
		// .getNamedQuery("list.ShippingTerms").list()));
		//
		// company.setShippingMethods(new ArrayList<ShippingMethod>(session
		// .getNamedQuery("list.ShippingMethod").list()));
		//
		// company.setPriceLevels(new
		// ArrayList<PriceLevel>(session.getNamedQuery(
		// "list.PriceLevel").list()));
		//
		// company.setItemGroups(new ArrayList<ItemGroup>(session.getNamedQuery(
		// "list.ItemGroup").list()));
		//
		// company.setTaxGroups(new ArrayList<TAXGroup>(session.getNamedQuery(
		// "list.TAXGroup").list()));
		//
		// company.setPaymentTerms(new ArrayList<PaymentTerms>(session
		// .getNamedQuery("list.PaymentTerms").list()));
		//
		// company.setCreditRatings(new ArrayList<CreditRating>(session
		// .getNamedQuery("list.CreditRating").list()));
		//
		// company.setSalesPersons(new ArrayList<SalesPerson>(session
		// .getNamedQuery("list.SalesPerson").list()));
		//
		// company.setTaxCodes(new ArrayList<TAXCode>(session.getNamedQuery(
		// "list.TAXCode").list()));
		//
		// company.setTaxItems(new ArrayList<TAXItem>(session.getNamedQuery(
		// "list.TAXItem").list()));
		//
		// company.setTaxItemGroups(new ArrayList<TAXItemGroup>(session
		// .getNamedQuery("list.TAXItemGroups").list()));
		//
		// company.setBanks(new
		// ArrayList<Bank>(session.getNamedQuery("list.Bank")
		// .list()));
		//
		// company.setTaxrates(new ArrayList<TaxRates>(session.getNamedQuery(
		// "list.TaxRates").list()));
		//
		// company.setFixedAssets(new
		// ArrayList<FixedAsset>(session.getNamedQuery(
		// "list.FixedAsset").list()));
		// // company
		// // .setSellingDisposingFixedAssets(new
		// // HashSet<SellingOrDisposingFixedAsset>(
		// // session.getNamedQuery(
		// // "list.SellingOrDisposingFixedAsset").list()));
		//
		// company.setVatReturns(new ArrayList<VATReturn>(session.getNamedQuery(
		// "list.VATReturn").list()));
		// company.setCurrencies(new HashSet<Currency>(session.getNamedQuery(
		// "list.currency").list()));
		//
		// company.setTaxAdjustments(new ArrayList<TAXAdjustment>(session
		// .getNamedQuery("list.TAXAdjustment").list()));
		//
		// // company.setVatCodes(new ArrayList<TAXCode>(session.getNamedQuery(
		// // "list.VATCode").list()));
		//
		// // company.setVatItemGroups(new ArrayList<TAXItemGroup>(session
		// // .getNamedQuery("list.VATItemGroup").list()));
		//
		// company.setTaxAgencies(new
		// ArrayList<TAXAgency>(session.getNamedQuery(
		// "list.TAXAgency").list()));
		//
		// company.setVatBoxes(new
		// HashSet<Box>(session.getNamedQuery("list.Box")
		// .list()));
		// company.setVatReturnBoxes(new HashSet<VATReturnBox>(session
		// .getNamedQuery("list.VATReturnBox").list()));

		// company.setAccounterClasses(new HashSet<AccounterClass>(session
		// .getNamedQuery("list.TrackClass").list()));
		company = company.toCompany(company);
		ClientConvertUtil clientConvertUtil = new ClientConvertUtil();
		ClientCompany clientCompany = clientConvertUtil.toClientObject(company,
				ClientCompany.class);
		clientCompany.setTransactionsCount(getTransactionsCount(companyId));
		ClientFinanceDate[] dates = getMinimumAndMaximumTransactionDate(companyId);

		clientCompany.setTransactionStartDate(dates[0]);
		clientCompany.setTransactionEndDate(dates[1]);

		clientCompany.setUsersList(getAllEmployees(company));

		// User logInUSer = (User) session.getNamedQuery("getuser.by.email")
		// .setParameter("email", logInUserEmail).uniqueResult();
		clientCompany.setLoggedInUser(logInUser.getClientUser());
		int premiumType = company.getCreatedBy().getClient()
				.getClientSubscription().getPremiumType();
		clientCompany.setPremiumType(premiumType);
		List list = session.getNamedQuery("get.All.Units")
				.setEntity("company", company).list();

		ArrayList<ClientUnit> units = new ArrayList<ClientUnit>();
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			Unit unit = (Unit) iterator.next();
			units.add(clientConvertUtil.toClientObject(unit, ClientUnit.class));
		}

		clientCompany.setUnits(units);
		clientCompany.setConfigured(company.isConfigured());

		clientCompany.setCountryPreferences(getCountryPreferences(company
				.getRegisteredAddress().getCountryOrRegion(), company
				.getRegisteredAddress().getStateOrProvinence()));

		return clientCompany;
	}

	public int getTransactionsCount(long companyId) {
		Calendar instance = Calendar.getInstance();
		instance.set(Calendar.DATE, 1);
		Session session = HibernateUtil.getCurrentSession();
		Integer count = (Integer) session
				.getNamedQuery("transactions.count.per.month")
				.setParameter("companyId", companyId)
				.setParameter("startDate",
						new Timestamp(instance.getTime().getTime()))
				.uniqueResult();
		if (count == null) {
			return 0;
		}
		return count;
	}

	private void activateUser(User user) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		try {
			user.setActive(true);
			session.saveOrUpdate(user);
			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			throw new AccounterException(AccounterException.ERROR_INTERNAL);
		}

	}

	/**
	 * @param company
	 * @return
	 * @throws AccounterException
	 */
	public ArrayList<ClientUserInfo> getAllEmployees(Company company)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Set<User> financeUsers = company.getUsers();
		ArrayList<ClientUserInfo> employees = new ArrayList<ClientUserInfo>();
		for (User user : financeUsers) {
			if (!user.isDeleted()) {
				ClientUser clientUser = new ClientConvertUtil().toClientObject(
						user, ClientUser.class);
				updateClientUser(clientUser, user.getClient());
				ClientUserInfo userInfo = clientUser.toUserInfo();
				employees.add(userInfo);
			}
		}
		return employees;
	}

	public PaginationList<ClientAccount> getAccounts(int type,
			Boolean isAciveAccount, int start, int length, Long companyId)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		int total = 0;
		List<Account> list;
		if (type != 0) {
			Query query = session.getNamedQuery("getAccountsOfType")
					.setLong("companyId", companyId).setInteger("type", type)
					.setBoolean("isActive", isAciveAccount);
			total = query.list().size();
			list = query.setFirstResult(start).setMaxResults(length).list();
		} else {
			// Dont know
			list = new ArrayList<Account>();
			Set<Account> accounts = getCompany(companyId).getAccounts();
			for (Account a : accounts) {
				if (a.getIsActive() == isAciveAccount) {
					list.add(a);
				}
			}
			total = list.size();
			if (length >= 0) {
				int max = start + length;
				if (max > total) {
					max = total;
				}
				list = list.subList(start, max);
			}
		}
		PaginationList<ClientAccount> result = new PaginationList<ClientAccount>();
		for (Account a : list) {
			ClientAccount account;
			if (a instanceof BankAccount) {
				account = new ClientConvertUtil().toClientObject(a,
						ClientBankAccount.class);
			} else {
				account = new ClientConvertUtil().toClientObject(a,
						ClientAccount.class);
			}
			result.add(account);
		}
		result.setTotalCount(total);
		result.setStart(start);
		return result;
	}

	public PaginationList<ClientAccount> getBankAccounts(Long companyId,
			boolean isActiveaccount, int start, int length)
			throws AccounterException {
		int total = 0;
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getBankAccountsOfType")
				.setLong("companyId", companyId)
				.setParameter("isActive", isActiveaccount);
		total = query.list().size();
		@SuppressWarnings("unchecked")
		List<BigInteger> list = query.setFirstResult(start)
				.setMaxResults(length).list();
		PaginationList<ClientAccount> result = new PaginationList<ClientAccount>();
		ClientCompany company = new ClientConvertUtil().toClientObject(
				getCompany(companyId), ClientCompany.class);
		for (BigInteger id : list) {
			ClientAccount account = company.getAccount(id.longValue());
			if (account.getIsActive() == isActiveaccount) {
				result.add(account);
			}
		}
		result.setTotalCount(total);
		result.setStart(start);
		return result;
	}

	public PaginationList<ClientReminder> getRemindersList(int start,
			int length, int viewType, Long companyId) throws AccounterException {
		int total = 0;
		Session session = HibernateUtil.getCurrentSession();
		Query query = null;
		if (viewType != 0) {
			query = session.getNamedQuery("getRemindersByViewType")
					.setLong("companyId", companyId)
					.setInteger("viewType", viewType);
		} else {
			query = session.getNamedQuery("getAllReminders").setLong(
					"companyId", companyId);
		}
		total = query.list().size();
		@SuppressWarnings("unchecked")
		List<Reminder> l = query.setFirstResult(start).setMaxResults(length)
				.list();
		PaginationList<ClientReminder> result = new PaginationList<ClientReminder>();
		for (Reminder reminder : l) {
			ClientReminder clientReminder = new ClientConvertUtil()
					.toClientObject(reminder, ClientReminder.class);
			result.add(clientReminder);
		}
		result.setTotalCount(total);
		result.setStart(start);
		return result;
	}

	public PaginationList<SearchResultlist> getSearchByInput(SearchInput input,
			int start, int length, Long companyId) {

		Session session = HibernateUtil.getCurrentSession();
		Query query = null;

		if (input.getTransactionType() == Transaction.ALL) {
			if (input.getSearchbyType() == SearchInput.TYPE_AMOUNT) {
				query = session.getNamedQuery("getAllTransactionsByAmount")
						.setParameter("companyId", companyId)
						.setParameter("amount", input.getAmount())
						.setParameter("match", input.getMatchType());

			} else if (input.getSearchbyType() == SearchInput.TYPE_DATE) {
				query = session.getNamedQuery("getAllTransactionsByDate")
						.setParameter("companyId", companyId)
						.setParameter("date", input.getValue());
			} else if (input.getSearchbyType() == SearchInput.TYPE_DESC_MEMO) {
				query = session
						.getNamedQuery("getAllTransactionsByDescOrMemo")
						.setParameter("companyId", companyId)
						.setParameter("memo", "%" + input.getFindBy() + "%",
								EncryptedStringType.INSTANCE);
			} else if (input.getSearchbyType() == SearchInput.TYPE_PAYEE) {
				query = session
						.getNamedQuery("getAllTransactionsByPayeeName")
						.setParameter("companyId", companyId)
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			}
			// if (input.getSearchbyType() == SearchInput.TYPE_REF_NO) {
			// query = session.getNamedQuery("getAllTransactionsByRefNo")
			// .setParameter("value", input.getFindBy());
			// }

		} else if (input.getTransactionType() == Transaction.TYPE_ENTER_BILL) {
			if (input.getSearchbyType() == SearchInput.TYPE_ACCOUNT) {
				query = session
						.getNamedQuery("getAllEnterBillssByAccounName")
						.setParameter("companyId", companyId)
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_AMOUNT) {
				query = session.getNamedQuery("getAllEnterBillssByAmount")
						.setParameter("companyId", companyId)
						.setParameter("amount", input.getAmount())
						.setParameter("match", input.getMatchType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_DATE) {
				query = session.getNamedQuery("getAllEnterBillsByDate")
						.setParameter("companyId", companyId)
						.setParameter("date", input.getValue());
			} else if (input.getSearchbyType() == SearchInput.TYPE_DESC_MEMO) {
				query = session
						.getNamedQuery("getAllEnterBillsByDescOrMemo")
						.setParameter("companyId", companyId)
						.setParameter("memo", "%" + input.getFindBy() + "%",
								EncryptedStringType.INSTANCE);
			} else if (input.getSearchbyType() == SearchInput.TYPE_PRODUCT_SERVICE) {
				query = session
						.getNamedQuery("getAllEnterBillsByProductOrService")
						.setParameter("companyId", companyId)
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_DUE_DATE) {
				ClientFinanceDate date = new ClientFinanceDate(
						input.getFindBy());
				query = session.getNamedQuery("getAllEnterBillsByDueDate")
						.setParameter("companyId", companyId)
						.setParameter("date", date.getDate());
			} else if (input.getSearchbyType() == SearchInput.TYPE_VENDOR) {
				query = session
						.getNamedQuery("getAllEnterBillsByVendorName")
						.setParameter("companyId", companyId)
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			}
			// if (input.getSearchbyType() == SearchInput.TYPE_REF_NO) {
			// query = session.getNamedQuery("getAllTransactionsByRefNo")
			// .setParameter("value", input.getFindBy());
			// }

		} else if (input.getTransactionType() == Transaction.TYPE_PAY_BILL) {
			if (input.getSearchbyType() == SearchInput.TYPE_VENDOR) {
				query = session
						.getNamedQuery("getAllPayBillsByVendorName")
						.setParameter("companyId", companyId)
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_ACCOUNT) {
				query = session
						.getNamedQuery("getAllPayBillssByAccounName")
						.setParameter("companyId", companyId)
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());

			} else if (input.getSearchbyType() == SearchInput.TYPE_AMOUNT) {
				query = session.getNamedQuery("getAllPayBillssByAmount")
						.setParameter("companyId", companyId)
						.setParameter("amount", input.getAmount())
						.setParameter("match", input.getMatchType());

			} else if (input.getSearchbyType() == SearchInput.TYPE_DATE) {
				query = session.getNamedQuery("getAllPayBillsByDate")
						.setParameter("companyId", companyId)
						.setParameter("date", input.getValue());
			} else if (input.getSearchbyType() == SearchInput.TYPE_CHEQUE_NO) {
				query = session
						.getNamedQuery("getAllPayBillsByChequeNo")
						.setParameter("companyId", companyId)
						.setParameter("number", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			}
			// if (input.getSearchbyType() == SearchInput.TYPE_PRODUCT_SERVICE)
			// {
			// query = session
			// .getNamedQuery("getAllPayBillsByProductOrService")
			// .setParameter("companyId", companyId)
			// .setParameter("value", input.getFindBy());
			// }
		} else if (input.getTransactionType() == Transaction.TYPE_CASH_EXPENSE) {
			if (input.getSearchbyType() == SearchInput.TYPE_VENDOR) {
				query = session
						.getNamedQuery("getAllCashExpensesByVendorName")
						.setParameter("companyId", companyId)
						.setParameter("type", input.getTransactionType())
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_ACCOUNT) {
				query = session
						.getNamedQuery("getAllCashExpensesByAccounName")
						.setParameter("companyId", companyId)
						.setParameter("type", input.getTransactionType())
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());

			} else if (input.getSearchbyType() == SearchInput.TYPE_AMOUNT) {
				query = session.getNamedQuery("getAllCashExpensesByAmount")
						.setParameter("companyId", companyId)
						.setParameter("type", input.getTransactionType())
						.setParameter("amount", input.getAmount())
						.setParameter("match", input.getMatchType());

			} else if (input.getSearchbyType() == SearchInput.TYPE_DATE) {
				query = session.getNamedQuery("getAllCashExpensesByDate")
						.setParameter("companyId", companyId)
						.setParameter("type", input.getTransactionType())
						.setParameter("date", input.getValue());
			} else if (input.getSearchbyType() == SearchInput.TYPE_PRODUCT_SERVICE) {
				query = session
						.getNamedQuery("getAllCashExpensesByProductOrService")
						.setParameter("companyId", companyId)
						.setParameter("type", input.getTransactionType())
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_DESC_MEMO) {
				query = session
						.getNamedQuery("getAllCashExpensesByDescOrMemo")
						.setParameter("companyId", companyId)
						.setParameter("type", input.getTransactionType())
						.setParameter("memo", "%" + input.getFindBy() + "%",
								EncryptedStringType.INSTANCE);
			} // if (input.getSearchbyType() == SearchInput.TYPE_REF_NO) {
				// query = session.getNamedQuery("getAllCashExpensesByRefNo")
				// .setParameter("value", input.getFindBy());//not
				// }

		} else if (input.getTransactionType() == Transaction.TYPE_WRITE_CHECK) {
			if (input.getSearchbyType() == SearchInput.TYPE_CUSTOMER) {
				query = session
						.getNamedQuery("getAllWriteChecksByCustomerName")
						.setParameter("companyId", companyId)
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_VENDOR) {
				query = session
						.getNamedQuery("getAllWriteChecksByVendorName")
						.setParameter("companyId", companyId)
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_ACCOUNT) {
				query = session
						.getNamedQuery("getAllWriteChecksByAccounName")
						.setParameter("companyId", companyId)
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_AMOUNT) {
				query = session.getNamedQuery("getAllWriteChecksByAmount")
						.setParameter("companyId", companyId)
						.setParameter("amount", input.getAmount())
						.setParameter("match", input.getMatchType());

			} else if (input.getSearchbyType() == SearchInput.TYPE_DATE) {
				query = session.getNamedQuery("getAllWriteChecksByDate")
						.setParameter("companyId", companyId)
						.setParameter("date", input.getValue());
			} else if (input.getSearchbyType() == SearchInput.TYPE_DESC_MEMO) {
				query = session
						.getNamedQuery("getAllWriteChecksByDescOrMemo")
						.setParameter("companyId", companyId)
						.setParameter("memo", "%" + input.getFindBy() + "%",
								EncryptedStringType.INSTANCE);
			} else if (input.getSearchbyType() == SearchInput.TYPE_CHEQUE_NO) {
				query = session
						.getNamedQuery("getAllWriteChecksByChequeNo")
						.setParameter("companyId", companyId)
						.setParameter("number", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			}
			// else if (input.getSearchbyType() ==
			// SearchInput.TYPE_PRODUCT_SERVICE) {
			// query = session
			// .getNamedQuery("getAllWriteChecksByProductOrService")
			// .setParameter("companyId", companyId)
			// .setParameter("value", input.getFindBy());// Not
			// // Completed
			// }
		} else if (input.getTransactionType() == Transaction.TYPE_CREDIT_CARD_EXPENSE) {
			if (input.getSearchbyType() == SearchInput.TYPE_AMOUNT) {
				query = session
						.getNamedQuery("getAllCreditCardExpensesByAmount")
						.setParameter("companyId", companyId)
						.setParameter("type", input.getTransactionType())
						.setParameter("amount", input.getAmount())
						.setParameter("match", input.getMatchType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_ACCOUNT) {
				query = session
						.getNamedQuery("getAllCreditCardExpensesByAccounName")
						.setParameter("companyId", companyId)
						.setParameter("type", input.getTransactionType())
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());

			} else if (input.getSearchbyType() == SearchInput.TYPE_DATE) {
				query = session.getNamedQuery("getAllCreditCardExpensesByDate")
						.setParameter("companyId", companyId)
						.setParameter("type", input.getTransactionType())
						.setParameter("date", input.getValue());
			} else if (input.getSearchbyType() == SearchInput.TYPE_DESC_MEMO) {
				query = session
						.getNamedQuery("getAllCreditCardExpensesByDescOrMemo")
						.setParameter("companyId", companyId)
						.setParameter("type", input.getTransactionType())
						.setParameter("memo", "%" + input.getFindBy() + "%",
								EncryptedStringType.INSTANCE);
			} else if (input.getSearchbyType() == SearchInput.TYPE_PRODUCT_SERVICE) {
				query = session
						.getNamedQuery(
								"getAllCreditCardExpensesByProductOrService")
						.setParameter("companyId", companyId)
						.setParameter("type", input.getTransactionType())
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_VENDOR) {
				query = session
						.getNamedQuery("getAllCreditCardExpensesByVendorName")
						.setParameter("companyId", companyId)
						.setParameter("type", input.getTransactionType())
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			}
			// if (input.getSearchbyType() == SearchInput.TYPE_REF_NO) {
			// query = session
			// .getNamedQuery("getAllCreditCardExpensesByRefNo")
			// .setParameter("companyId", companyId)
			// .setParameter("value", input.getFindBy());// Not
			// }

		} else if (input.getTransactionType() == Transaction.TYPE_CREDIT_CARD_CHARGE) {
			if (input.getSearchbyType() == SearchInput.TYPE_AMOUNT) {
				query = session
						.getNamedQuery("getAllCreditCardChargesByAmount")
						.setParameter("companyId", companyId)
						.setParameter("amount", input.getAmount())
						.setParameter("match", input.getMatchType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_ACCOUNT) {
				query = session
						.getNamedQuery("getAllCreditCardChargesByAccounName")
						.setParameter("companyId", companyId)
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());

			} else if (input.getSearchbyType() == SearchInput.TYPE_DATE) {
				query = session.getNamedQuery("getAllCreditCardChargesByDate")
						.setParameter("companyId", companyId)
						.setParameter("date", input.getValue());
			} else if (input.getSearchbyType() == SearchInput.TYPE_DESC_MEMO) {
				query = session
						.getNamedQuery("getAllCreditCardChargesByDescOrMemo")
						.setParameter("companyId", companyId)
						.setParameter("memo", "%" + input.getFindBy() + "%",
								EncryptedStringType.INSTANCE);
			} else if (input.getSearchbyType() == SearchInput.TYPE_PRODUCT_SERVICE) {
				query = session
						.getNamedQuery(
								"getAllCreditCardChargesByProductOrService")
						.setParameter("companyId", companyId)
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_VENDOR) {
				query = session
						.getNamedQuery("getAllCreditCardChargesByVendorName")
						.setParameter("companyId", companyId)
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			}
			// if (input.getSearchbyType() == SearchInput.TYPE_REF_NO) {
			// query = session.getNamedQuery("getAllCreditCardChargesByRefNo")
			// .setParameter("companyId", companyId)
			// .setParameter("value", input.getFindBy());
			// }

		} else if (input.getTransactionType() == Transaction.TYPE_CUSTOMER_CREDIT_MEMO) {
			if (input.getSearchbyType() == SearchInput.TYPE_AMOUNT) {
				query = session
						.getNamedQuery("getAllCustomerCreditNotesByAmount")
						.setParameter("companyId", companyId)
						.setParameter("amount", input.getAmount())
						.setParameter("match", input.getMatchType());

			} else if (input.getSearchbyType() == SearchInput.TYPE_CUSTOMER) {
				query = session
						.getNamedQuery(
								"getAllCustomerCreditNotesByCustomerName")
						.setParameter("companyId", companyId)
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_DATE) {
				query = session
						.getNamedQuery("getAllCustomerCreditNotesByDate")
						.setParameter("companyId", companyId)
						.setParameter("date", input.getValue());
			} else if (input.getSearchbyType() == SearchInput.TYPE_CREDIT_NOTE_NO) {
				query = session
						.getNamedQuery(
								"getAllCustomerCreditNotesByCreditNoteeNo")
						.setParameter("companyId", companyId)
						.setParameter("number", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_DESC_MEMO) {
				query = session
						.getNamedQuery("getAllCustomerCreditNotesByDescOrMemo")
						.setParameter("companyId", companyId)
						.setParameter("memo", "%" + input.getFindBy() + "%",
								EncryptedStringType.INSTANCE);
			} else if (input.getSearchbyType() == SearchInput.TYPE_PRODUCT_SERVICE) {
				query = session
						.getNamedQuery(
								"getAllCustomerCreditNotesByProductOrService")
						.setParameter("companyId", companyId)
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			}
		} else if (input.getTransactionType() == Transaction.TYPE_ESTIMATE) {
			if (input.getSearchbyType() == SearchInput.TYPE_CUSTOMER) {
				query = session
						.getNamedQuery("getAllEstimatesByCustomerName")
						.setParameter("companyId", companyId)
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType())
						.setParameter("estimateType",
								input.getTransactionSubType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_ACCOUNT) {
				query = session
						.getNamedQuery("getAllEstimatesByAccounName")
						.setParameter("companyId", companyId)
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType())
						.setParameter("estimateType",
								input.getTransactionSubType());

			} else if (input.getSearchbyType() == SearchInput.TYPE_AMOUNT) {
				query = session
						.getNamedQuery("getAllEstimatesByAmount")
						.setParameter("companyId", companyId)
						.setParameter("amount", input.getAmount())
						.setParameter("match", input.getMatchType())
						.setParameter("estimateType",
								input.getTransactionSubType());

			} else if (input.getSearchbyType() == SearchInput.TYPE_DATE) {
				query = session
						.getNamedQuery("getAllEstimatesByDate")
						.setParameter("companyId", companyId)
						.setParameter("date", input.getValue())
						.setParameter("estimateType",
								input.getTransactionSubType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_INVOICE_DATE) {
				query = session
						.getNamedQuery("getAllEstimatesByInvoiceDate")
						.setParameter("companyId", companyId)
						.setParameter("date", input.getValue())
						.setParameter("estimateType",
								input.getTransactionSubType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_DESC_MEMO) {
				query = session
						.getNamedQuery("getAllEstimatesByDescOrMemo")
						.setParameter("companyId", companyId)
						.setParameter("memo", "%" + input.getFindBy() + "%",
								EncryptedStringType.INSTANCE)
						.setParameter("estimateType",
								input.getTransactionSubType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_PRODUCT_SERVICE) {
				query = session
						.getNamedQuery("getAllEstimatesByProductOrService")
						.setParameter("companyId", companyId)
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType())
						.setParameter("estimateType",
								input.getTransactionSubType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_CHARGE_NO) {
				query = session
						.getNamedQuery("getAllEstimatesByChargeNo")
						.setParameter("companyId", companyId)
						.setParameter("number", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType())
						.setParameter("estimateType",
								input.getTransactionSubType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_ESTIMATE_NO) {
				query = session
						.getNamedQuery("getAllEstimatesByEstimateNo")
						.setParameter("companyId", companyId)
						.setParameter("number", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType())
						.setParameter("estimateType",
								input.getTransactionSubType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_CREDIT_NO) {
				query = session
						.getNamedQuery("getAllEstimatesByCreditNo")
						.setParameter("companyId", companyId)
						.setParameter("number", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType())
						.setParameter("estimateType",
								input.getTransactionSubType());
			}

		} else if (input.getTransactionType() == Transaction.TYPE_JOURNAL_ENTRY) {
			if (input.getSearchbyType() == SearchInput.TYPE_ACCOUNT) {
				query = session
						.getNamedQuery("getAllJournalEntriesByAccount")
						.setParameter("companyId", companyId)
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_DATE) {
				query = session.getNamedQuery("getAllJournalEntriesByDate")
						.setParameter("companyId", companyId)
						.setParameter("date", input.getValue());
			} else if (input.getSearchbyType() == SearchInput.TYPE_DESC_MEMO) {
				query = session
						.getNamedQuery("getAllJournalEntriesByDescOrMemo")
						.setParameter("companyId", companyId)
						.setParameter("memo", "%" + input.getFindBy() + "%",
								EncryptedStringType.INSTANCE);
			} else if (input.getSearchbyType() == SearchInput.TYPE_ENTRY_NO) {
				String searchString = null;
				if (input.getMatchType() == 3) {
					searchString = "%" + input.getFindBy() + "%";
				} else {
					searchString = input.getFindBy();
				}
				query = session
						.getNamedQuery("getAllJournalEntriesByEntryNo")
						.setParameter("companyId", companyId)
						.setParameter("number", searchString,
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			}
		} else if (input.getTransactionType() == Transaction.TYPE_INVOICE) {
			if (input.getSearchbyType() == SearchInput.TYPE_AMOUNT) {
				query = session.getNamedQuery("getAllInvoicesByAmount")
						.setParameter("companyId", companyId)
						.setParameter("amount", input.getAmount())
						.setParameter("match", input.getMatchType());

			} else if (input.getSearchbyType() == SearchInput.TYPE_CUSTOMER) {
				query = session
						.getNamedQuery("getAllInvoicesByCustomerName")
						.setParameter("companyId", companyId)
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_DATE) {
				query = session.getNamedQuery("getAllInvoicesByDate")
						.setParameter("companyId", companyId)
						.setParameter("date", input.getValue());
			} else if (input.getSearchbyType() == SearchInput.TYPE_DUE_DATE) {
				query = session.getNamedQuery("getAllInvoicesByDueDate")
						.setParameter("companyId", companyId)
						.setParameter("date", input.getValue());
			} else if (input.getSearchbyType() == SearchInput.TYPE_INOVICE_NO) {
				query = session
						.getNamedQuery("getAllInvoicesByInvoiceNo")
						.setParameter("companyId", companyId)
						.setParameter("number", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_DESC_MEMO) {
				query = session
						.getNamedQuery("getAllInvoicesByDescOrMemo")
						.setParameter("companyId", companyId)
						.setParameter("memo", "%" + input.getFindBy() + "%",
								EncryptedStringType.INSTANCE);
			} else if (input.getSearchbyType() == SearchInput.TYPE_PRODUCT_SERVICE) {
				query = session
						.getNamedQuery("getAllInvoicesByProductOrService")
						.setParameter("companyId", companyId)
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			}

		} else if (input.getTransactionType() == Transaction.TYPE_RECEIVE_PAYMENT) {
			if (input.getSearchbyType() == SearchInput.TYPE_CUSTOMER) {
				query = session
						.getNamedQuery("getAllReceivePaymentsByCustomerName")
						.setParameter("companyId", companyId)
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_AMOUNT) {
				query = session.getNamedQuery("getAllReceivePaymentsByAmount")
						.setParameter("companyId", companyId)
						.setParameter("amount", input.getAmount())
						.setParameter("match", input.getMatchType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_ACCOUNT) {
				query = session
						.getNamedQuery("getAllReceivePaymentsByAccount")
						.setParameter("companyId", companyId)
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_DATE) {
				query = session.getNamedQuery("getAllReceivePaymentsByDate")
						.setParameter("companyId", companyId)
						.setParameter("date", input.getValue());
			} else if (input.getSearchbyType() == SearchInput.TYPE_DESC_MEMO) {
				query = session
						.getNamedQuery("getAllReceivePaymentsByDescOrMemo")
						.setParameter("companyId", companyId)
						.setParameter("memo", "%" + input.getFindBy() + "%",
								EncryptedStringType.INSTANCE);
			} else if (input.getSearchbyType() == SearchInput.TYPE_RECEIVED_NO) {
				query = session
						.getNamedQuery(
								"getAllReceivePaymentsByReceivedChequeNo")
						.setParameter("companyId", companyId)
						.setParameter("number", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			}
		} else if (input.getTransactionType() == Transaction.TYPE_CUSTOMER_REFUNDS) {
			if (input.getSearchbyType() == SearchInput.TYPE_CUSTOMER) {
				query = session
						.getNamedQuery("getAllCustomerRefundsByCustomerName")
						.setParameter("companyId", companyId)
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_AMOUNT) {
				query = session.getNamedQuery("getAllCustomerRefundsByAmount")
						.setParameter("companyId", companyId)
						.setParameter("amount", input.getAmount())
						.setParameter("match", input.getMatchType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_DATE) {
				query = session.getNamedQuery("getAllCustomerRefundsByDate")
						.setParameter("companyId", companyId)
						.setParameter("date", input.getValue());
			} else if (input.getSearchbyType() == SearchInput.TYPE_DESC_MEMO) {
				query = session
						.getNamedQuery("getAllCustomerRefundsByDescOrMemo")
						.setParameter("companyId", companyId)
						.setParameter("memo", "%" + input.getFindBy() + "%",
								EncryptedStringType.INSTANCE);
			} else if (input.getSearchbyType() == SearchInput.TYPE_REFUND_NO) {
				query = session
						.getNamedQuery("getAllCustomerRefundsByRefundNo")
						.setParameter("companyId", companyId)
						.setParameter("number", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			}
		} else if (input.getTransactionType() == Transaction.TYPE_CASH_SALES) {
			if (input.getSearchbyType() == SearchInput.TYPE_CUSTOMER) {
				query = session
						.getNamedQuery("getAllCashSalesByCustomerName")
						.setParameter("companyId", companyId)
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_AMOUNT) {
				query = session.getNamedQuery("getAllCashSalesByAmount")
						.setParameter("companyId", companyId)
						.setParameter("amount", input.getAmount())
						.setParameter("match", input.getMatchType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_DATE) {
				query = session.getNamedQuery("getAllCashSalesByDate")
						.setParameter("companyId", companyId)
						.setParameter("date", input.getValue());
			} else if (input.getSearchbyType() == SearchInput.TYPE_DESC_MEMO) {
				query = session
						.getNamedQuery("getAllCashSalesByDescOrMemo")
						.setParameter("companyId", companyId)
						.setParameter("memo", "%" + input.getFindBy() + "%",
								EncryptedStringType.INSTANCE);
			} else if (input.getSearchbyType() == SearchInput.TYPE_PRODUCT_SERVICE) {
				query = session
						.getNamedQuery("getAllCashSalesByProductOrService")
						.setParameter("companyId", companyId)
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_SALE_NO) {
				query = session
						.getNamedQuery("getAllCashSalesByCashSaleNo")
						.setParameter("companyId", companyId)
						.setParameter("number", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			}
		} else if (input.getTransactionType() == Transaction.TYPE_TRANSFER_FUND) {
			if (input.getSearchbyType() == SearchInput.TYPE_ACCOUNT) {
				query = session
						.getNamedQuery("getAllTransfersByAccounName")
						.setParameter("companyId", companyId)
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_DATE) {
				query = session.getNamedQuery("getAllTransfersByDate")
						.setParameter("companyId", companyId)
						.setParameter("date", input.getValue());
			} else if (input.getSearchbyType() == SearchInput.TYPE_DESC_MEMO) {
				query = session
						.getNamedQuery("getAllTransfersByDescOrMemo")
						.setParameter("companyId", companyId)
						.setParameter("memo", "%" + input.getFindBy() + "%",
								EncryptedStringType.INSTANCE);
			} else if (input.getSearchbyType() == SearchInput.TYPE_AMOUNT) {
				query = session.getNamedQuery("getAllTransfersByAmount")
						.setParameter("companyId", companyId)
						.setParameter("amount", input.getAmount())
						.setParameter("match", input.getMatchType());
			}

		} else if (input.getTransactionType() == Transaction.TYPE_MAKE_DEPOSIT) {
			if (input.getSearchbyType() == SearchInput.TYPE_ACCOUNT) {
				query = session
						.getNamedQuery("getAllDepositsByAccounName")
						.setParameter("companyId", companyId)
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_DATE) {
				query = session.getNamedQuery("getAllDepositsByDate")
						.setParameter("companyId", companyId)
						.setParameter("date", input.getValue());
			} else if (input.getSearchbyType() == SearchInput.TYPE_DESC_MEMO) {
				query = session
						.getNamedQuery("getAllDepositsByDescOrMemo")
						.setParameter("companyId", companyId)
						.setParameter("memo", "%" + input.getFindBy() + "%",
								EncryptedStringType.INSTANCE);
			} else if (input.getSearchbyType() == SearchInput.TYPE_AMOUNT) {
				query = session.getNamedQuery("getAllDepositsByAmount")
						.setParameter("companyId", companyId)
						.setParameter("amount", input.getAmount())
						.setParameter("match", input.getMatchType());
			}

		} else if (input.getTransactionType() == Transaction.TYPE_VENDOR_CREDIT_MEMO) {
			if (input.getSearchbyType() == SearchInput.TYPE_VENDOR) {
				query = session
						.getNamedQuery("getAllVendorCreditsByVendorName")
						.setParameter("companyId", companyId)
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_ACCOUNT) {
				query = session
						.getNamedQuery("getAllVendorCreditsByAccounName")
						.setParameter("companyId", companyId)
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_AMOUNT) {
				query = session.getNamedQuery("getAllVendorCreditsByAmount")
						.setParameter("companyId", companyId)
						.setParameter("amount", input.getAmount())
						.setParameter("match", input.getMatchType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_DATE) {
				ClientFinanceDate date = new ClientFinanceDate(
						input.getFindBy());
				query = session.getNamedQuery("getAllVendorCreditsByDate")
						.setParameter("companyId", companyId)
						.setParameter("date", date.getDate());
			}
			if (input.getSearchbyType() == SearchInput.TYPE_PRODUCT_SERVICE) {
				query = session
						.getNamedQuery("getAllVendorCreditsByProductOrService")
						.setParameter("companyId", companyId)
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			} else if (input.getSearchbyType() == SearchInput.TYPE_DESC_MEMO) {
				query = session
						.getNamedQuery("getAllVendorCreditsByDescOrMemo")
						.setParameter("companyId", companyId)
						.setParameter("memo", "%" + input.getFindBy() + "%",
								EncryptedStringType.INSTANCE);
			}// if (input.getSearchbyType() == SearchInput.TYPE_REF_NO) {
				// query = session.getNamedQuery("getAllVendorCreditsByRefNo")
				// .setParameter("value", input.getFindBy());
				// }

		} else if (input.getTransactionType() == Transaction.TYPE_PAY_TAX) {
			if (input.getSearchbyType() == SearchInput.TYPE_ACCOUNT) {
				query = session
						.getNamedQuery("getAllTaxPaymentsByAccounName")
						.setParameter("companyId", companyId)
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			}
			if (input.getSearchbyType() == SearchInput.TYPE_DATE) {
				ClientFinanceDate date = new ClientFinanceDate(
						input.getFindBy());
				query = session.getNamedQuery("getAllTaxPaymentsByDate")
						.setParameter("companyId", companyId)
						.setParameter("date", date.getDate());
			}
			if (input.getSearchbyType() == SearchInput.TYPE_DESC_MEMO) {
				query = session
						.getNamedQuery("getAllTaxPaymentsByDescOrMemo")
						.setParameter("companyId", companyId)
						.setParameter("memo", "%" + input.getFindBy() + "%",
								EncryptedStringType.INSTANCE);
			}
		} else if (input.getTransactionType() == Transaction.TYPE_ADJUST_VAT_RETURN) {
			if (input.getSearchbyType() == SearchInput.TYPE_ACCOUNT) {
				query = session
						.getNamedQuery("getAllTaxAdjustmentsByAccounName")
						.setParameter("companyId", companyId)
						.setParameter("name", input.getFindBy(),
								EncryptedStringType.INSTANCE)
						.setParameter("match", input.getMatchType());
			}
			if (input.getSearchbyType() == SearchInput.TYPE_DATE) {
				ClientFinanceDate date = new ClientFinanceDate(
						input.getFindBy());
				query = session.getNamedQuery("getAllTaxAdjustmentsByDate")
						.setParameter("companyId", companyId)
						.setParameter("date", date.getDate());
			}
			if (input.getSearchbyType() == SearchInput.TYPE_DESC_MEMO) {
				query = session
						.getNamedQuery("getAllTaxAdjustmentsByDescOrMemo")
						.setParameter("companyId", companyId)
						.setParameter("memo", "%" + input.getFindBy() + "%",
								EncryptedStringType.INSTANCE);
			}

		}

		List list = query.list();
		if (list != null) {

			Object[] object = null;
			Iterator iterator = list.iterator();
			PaginationList<SearchResultlist> queryResult = new PaginationList<SearchResultlist>();
			while ((iterator).hasNext()) {

				SearchResultlist searchList = new SearchResultlist();
				object = (Object[]) iterator.next();

				long tId = (object[0] == null ? 0 : ((Long) object[0]));
				searchList.setId(tId);
				searchList.setDate(new ClientFinanceDate((Long) object[1]));
				searchList.setTransactionType((Integer) object[2]);
				searchList.setTransactionSubType(input.getTransactionSubType());
				searchList.setAmount((Double) object[3]);
				searchList.setCurrency((Long) object[4]);
				if (object.length > 5) {
					searchList.setTransactionSubType((Integer) object[5]);
				}
				queryResult.add(searchList);
			}

			PaginationList<SearchResultlist> result = new PaginationList<SearchResultlist>();
			for (int i = start, j = 0; (i < queryResult.size() && j < length); i++, j++) {
				result.add(queryResult.get(i));
			}

			result.setTotalCount(queryResult.size());
			return result;

		}
		return null;
	}

	public ArrayList<ClientMessageOrTask> getMessagesAndTasks(long companyId)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		List<MessageOrTask> list = session.getNamedQuery("getMessagesAndTasks")
				.setParameter("companyId", companyId).list();
		ClientConvertUtil convertUtil = new ClientConvertUtil();

		ArrayList<ClientMessageOrTask> result = new ArrayList<ClientMessageOrTask>();

		for (MessageOrTask mt : list) {
			ClientMessageOrTask cmt = convertUtil.toClientObject(mt,
					ClientMessageOrTask.class);
			result.add(cmt);
		}
		return result;
	}

	public ClientTransaction getTransactionToCreate(
			ClientRecurringTransaction obj, long date)
			throws AccounterException, ClassNotFoundException {

		Session session = HibernateUtil.getCurrentSession();

		FinanceDate transactionDate;
		if (date == 0) {
			transactionDate = new FinanceDate();
		} else {
			transactionDate = new FinanceDate(date);
		}

		RecurringTransaction recurringTransaction = null;
		recurringTransaction = new ServerConvertUtil().toServerObject(
				recurringTransaction, obj, session);

		Transaction transaction = null;
		try {
			transaction = FinanceTool
					.createDuplicateTransaction(recurringTransaction);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			throw new AccounterException(e.getMessage());
		}
		if (transactionDate != null) {
			transaction.setDate(transactionDate);
			if (transaction instanceof Invoice) {
				((Invoice) transaction).setDueDate(transactionDate);
			} else if (transaction instanceof EnterBill) {
				((EnterBill) transaction).setDueDate(transactionDate);
			} else if (transaction instanceof CashPurchase) {
				((CashPurchase) transaction).setDeliveryDate(transactionDate);
			} else if (transaction instanceof Estimate) {
				((Estimate) transaction).setExpirationDate(transactionDate);
				((Estimate) transaction).setDeliveryDate(transactionDate);
			}
		}

		transaction.setSaveStatus(0);
		transaction.setRecurringTransaction(null);

		String simpleName = transaction.getClass().getSimpleName();
		StringBuffer clientName = new StringBuffer(
				"com.nitya.accounter.web.client.core.Client");
		clientName.append(simpleName);

		Class<ClientTransaction> clazz = (Class<ClientTransaction>) Class
				.forName(clientName.toString());

		return new ClientConvertUtil().toClientObject(transaction, clazz);
	}

	public PaginationList<DepositsTransfersList> getDepositsList(
			long companyId, long fromDate, long toDate, int start, int length,
			int viewType) throws DAOException {
		PaginationList<DepositsTransfersList> queryResult = new PaginationList<DepositsTransfersList>();
		int total = 0;
		List list;
		try {
			Session session = HibernateUtil.getCurrentSession();
			Query query = session.getNamedQuery("getDepositsList")
					.setParameter("companyId", companyId)
					.setParameter("fromDate", fromDate)
					.setParameter("toDate", toDate)
					.setParameter("viewType", viewType);

			if (length == -1) {
				list = query.list();
			} else {
				total = query.list().size();
				list = query.setFirstResult(start).setMaxResults(length).list();
			}

			if (list != null) {
				Object[] object = null;
				Iterator iterator = list.iterator();

				while ((iterator).hasNext()) {

					DepositsTransfersList depositsTransfersList = new DepositsTransfersList();
					object = (Object[]) iterator.next();

					depositsTransfersList
							.setTransactionId((object[0] == null ? 0
									: ((Long) object[0])));
					depositsTransfersList
							.setTransactionDate((object[1] == null ? null
									: (new ClientFinanceDate(((Long) object[1])))));
					depositsTransfersList.setType((Integer) object[2]);
					depositsTransfersList
							.setTransactionNumber((object[3] == null ? null
									: ((String) object[3])));

					depositsTransfersList.setInAccount((String) object[4]);

					depositsTransfersList
							.setAmount(object[5] != null ? (Double) object[5]
									: 0);
					depositsTransfersList
							.setCurrency(object[6] != null ? (Long) object[6]
									: 0);
					queryResult.add(depositsTransfersList);
				}
				queryResult.setTotalCount(total);
				queryResult.setStart(start);
				return queryResult;
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}

	}

	public PaginationList<DepositsTransfersList> getTransfersList(
			Long companyId, long fromDate, long toDate, int start, int length,
			int viewType) throws DAOException {
		PaginationList<DepositsTransfersList> queryResult = new PaginationList<DepositsTransfersList>();
		int total = 0;
		List list;
		try {
			Session session = HibernateUtil.getCurrentSession();
			Query query = session.getNamedQuery("getTransfersList")
					.setParameter("companyId", companyId)
					.setParameter("fromDate", fromDate)
					.setParameter("toDate", toDate)
					.setParameter("viewType", viewType);

			if (length == -1) {
				list = query.list();
			} else {
				total = query.list().size();
				list = query.setFirstResult(start).setMaxResults(length).list();
			}

			if (list != null) {
				Object[] object = null;
				Iterator iterator = list.iterator();

				while ((iterator).hasNext()) {

					DepositsTransfersList depositsTransfersList = new DepositsTransfersList();
					object = (Object[]) iterator.next();

					depositsTransfersList
							.setTransactionId((object[0] == null ? 0
									: ((Long) object[0])));
					depositsTransfersList
							.setTransactionDate((object[1] == null ? null
									: (new ClientFinanceDate(((Long) object[1])))));
					depositsTransfersList.setType((Integer) object[2]);
					depositsTransfersList
							.setTransactionNumber((object[3] == null ? null
									: ((String) object[3])));

					depositsTransfersList.setInAccount((String) object[4]);
					depositsTransfersList.setFromAccount((String) object[5]);

					depositsTransfersList.setAmount((Double) object[6]);
					depositsTransfersList
							.setCurrency((Long) object[7] == null ? 0
									: (Long) object[7]);
					depositsTransfersList.setStatus((Integer) object[8]);
					queryResult.add(depositsTransfersList);
				}
				queryResult.setTotalCount(total);
				queryResult.setStart(start);
				return queryResult;
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}

	}

	/**
	 * @param clientId
	 * @return
	 * @throws AccounterException
	 */
	public int getClientCompaniesCount(long clientId) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Object obj = session.getNamedQuery("getClirntCompanyCount")
				.setParameter("clientId", clientId).uniqueResult();
		return ((Long) obj).intValue();
	}

	public Long getTransaction(boolean isPrev, Long companyId, long id,
			int type, int subType) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Object obj = null;
		if (isPrev) {
			obj = session.getNamedQuery("getPrevTransaction")
					.setParameter("company", companyId).setParameter("id", id)
					.setParameter("type", type)
					.setParameter("subType", subType).uniqueResult();
		} else {
			obj = session.getNamedQuery("getNextTransaction")
					.setParameter("company", companyId).setParameter("id", id)
					.setParameter("type", type)
					.setParameter("subType", subType).uniqueResult();
		}
		if (obj == null) {
			return null;
		}

		return (Long) obj;
	}

	public ArrayList<ItemUnitPrice> getUnitPricesByPayee(Long companyId,
			boolean isCust, long payee, long item) {
		Session session = HibernateUtil.getCurrentSession();
		ArrayList<ItemUnitPrice> result = new ArrayList<ItemUnitPrice>();
		Query query = null;
		if (isCust) {
			query = session.getNamedQuery("getUnitPricesByCustomer")
					.setParameter("company", companyId)
					.setParameter("customer", payee).setParameter("item", item);
		} else {
			query = session.getNamedQuery("getUnitPricesByVendor")
					.setParameter("company", companyId)
					.setParameter("vendor", payee).setParameter("item", item);
		}
		List list = query.list();

		if (list != null) {
			Object[] object = null;
			Iterator iterator = list.iterator();

			while (iterator.hasNext()) {
				object = (Object[]) iterator.next();
				ItemUnitPrice payeeUnitPrice = new ItemUnitPrice();
				payeeUnitPrice.setNumber((String) object[0]);
				payeeUnitPrice.setDate((Long) object[1]);
				payeeUnitPrice.setUnitPrice("" + (Double) object[2]);
				result.add(payeeUnitPrice);
			}
		}

		return result;
	}

	public CountryPreferences getCountryPreferences(String countryName,
			String state) {
		ICountryPreferences iCountryPreferences = CountryPreferenceFactory
				.get(countryName);

		if (iCountryPreferences == null) {
			return null;
		}

		CountryPreferences countryPreferences = new CountryPreferences();
		countryPreferences.setCompanyFields(iCountryPreferences
				.getCompanyFields());
		countryPreferences.setCustomerFields(iCountryPreferences
				.getCustomerFields());
		countryPreferences.setPreferredCurrency(iCountryPreferences
				.getPreferredCurrency());
		countryPreferences.setSalesTaxAvailable(iCountryPreferences
				.isSalesTaxAvailable());
		countryPreferences.setServiceTaxAvailable(iCountryPreferences
				.isServiceTaxAvailable());
		countryPreferences.setStates(iCountryPreferences.getStates());
		countryPreferences
				.setTDSAvailable(iCountryPreferences.isTDSAvailable());
		countryPreferences
				.setVatAvailable(iCountryPreferences.isVatAvailable());
		countryPreferences.setVendorFields(iCountryPreferences
				.getVendorFields());
		countryPreferences
				.setDefaultFiscalYearStartingMonth(iCountryPreferences
						.getDefaultFiscalYearStartingMonth());

		if (state == null || state.isEmpty()) {
			String[] states = iCountryPreferences.getStates();
			if (states != null && states.length > 0) {
				state = iCountryPreferences.getStates()[0];
			}
		}

		countryPreferences.setDefaultTimeZone(iCountryPreferences
				.getDefaultTimeZone(state));

		return countryPreferences;
	}
}
