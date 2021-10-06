package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.json.JSONException;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.Lists.FixedAssetLinkedAccountMap;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

/**
 * 
 * @author Suresh Garikapati.
 * 
 *         Depreciation is the reduction in the cost of an assetused for
 *         business purposes during certain amount of time due to usage, passage
 *         of time, wear and tear, technological outdating or obsolescence,
 *         depletion, inadequacy, rot, rust, decay or other such factors.
 * 
 *         In accounting, however, depreciation is a term used to describe any
 *         method of attributing the historical or purchase cost of an asset
 *         across its useful life. Depreciation in accounting is often
 *         mistakenly seen as a basis for recognizing impairment of an asset,
 *         but unexpected changes in value, where seen as significant enough to
 *         account for, are handled through write-downs or similar techniques
 *         which adjust the book value of the asset to reflect its current
 *         value.
 * 
 *         In our Accounting Software we have two types of Methods for
 *         Depreciation. 1) Staright line 2) Declaining Balance
 */
public class Depreciation extends CreatableObject implements
		IAccounterServerCore, Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6128335133352855706L;
	public static final int METHOD_STRAIGHT_LINE = 1;
	public static final int METHOD_REDUCING_BALANCE = 2;

	public static final int APPROVE = 1;
	public static final int ROLLBACK = 2;

	public static final int DEPRECIATION_FOR_ALL_FIXEDASSET = 1;
	public static final int DEPRECIATION_FOR_SINGLE_FIXEDASSET = 2;

	/**
	 * To know whether this Depreciation is for Approving or for Rollback.
	 */
	int status;

	/**
	 * The date from which the Depreciation is start.
	 */
	FinanceDate depreciateFrom;

	/**
	 * The date till which the Depreciation should run.
	 */
	FinanceDate depreciateTo;

	/**
	 * This contains the list of possible Registered Fixed Assets which has
	 * purchased under this period. (between Depreciation from and to dates)
	 */
	List<FixedAsset> fixedAssets;
	FixedAsset fixedAsset;

	/**
	 * This is the property to categorize this Depreciation among all the
	 * existing Depreciations created. i.e., whether this Depreciation has been
	 * created for Single Fixed Asset.
	 */
	int depreciationFor;

	/**
	 * This is the temporary variable used to hold the previous status while
	 * updating this Depreciation.
	 */
	transient int previousStatus;
	transient FinanceDate rollBackDepreciationDate;

	FixedAssetLinkedAccountMap linkedAccounts;

	public Depreciation() {
		this.depreciationFor = DEPRECIATION_FOR_ALL_FIXEDASSET;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public FinanceDate getDepreciateFrom() {
		return depreciateFrom;
	}

	public void setDepreciateFrom(FinanceDate depreciateFrom) {
		this.depreciateFrom = depreciateFrom;
	}

	public FinanceDate getDepreciateTo() {
		return depreciateTo;
	}

	public void setDepreciateTo(FinanceDate depreciateTo) {
		this.depreciateTo = depreciateTo;
	}

	public List<FixedAsset> getFixedAssets() {
		return fixedAssets;
	}

	public void setFixedAssets(List<FixedAsset> fixedAssets) {
		this.fixedAssets = fixedAssets;
	}

	public int getDepreciationFor() {
		return depreciationFor;
	}

	public FixedAsset getFixedAsset() {
		return fixedAsset;
	}

	public void setFixedAsset(FixedAsset fixedAsset) {
		this.fixedAsset = fixedAsset;
	}

	public void setDepreciationFor(int depreciationFor) {
		this.depreciationFor = depreciationFor;
	}

	public FinanceDate getRollBackDepreciationDate() {
		return rollBackDepreciationDate;
	}

	public void setRollBackDepreciationDate(FinanceDate rollbackDepreciationDate) {
		this.rollBackDepreciationDate = rollbackDepreciationDate;
	}

	public FixedAssetLinkedAccountMap getLinkedAccounts() {
		return linkedAccounts;
	}

	public void setLinkedAccounts(FixedAssetLinkedAccountMap linkedAccounts) {
		this.linkedAccounts = linkedAccounts;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed || this.fixedAsset == null)
			return true;

		this.isOnSaveProccessed = true;
		/**
		 * Checking whether this Depreciation is approved or not.
		 */
		if (this.getStatus() != 0) {
			if (this.getStatus() == APPROVE) {

				// if (this.linkedAccounts != null) {
				// // Query query = session
				// // .createQuery(
				// //
				// "from com.vimukti.accounter.core.Account a where a.id in (:accountsList)"
				// // )
				// // .setParameterList("accountsList",
				// // this.linkedAccounts.keySet());
				// // List<Account> assetAccounts = query.list();
				// for (Account assetAccount : this.linkedAccounts.keySet()) {
				// Account changedLinkedAccount = this.linkedAccounts
				// .get(assetAccount);
				// if (!assetAccount.linkedAccumulatedDepreciationAccount
				// .getID().equals(
				// changedLinkedAccount.getID())) {
				// // Account changedLinkedAccount = (Account) session
				// // .createQuery(
				// //
				// "from com.vimukti.accounter.core.Account a where a.id =?"
				// // )
				// // .setParameter(0, changedLinkedAccountID)
				// // .uniqueResult();
				// assetAccount
				// .setLinkedAccumulatedDepreciationAccount(changedLinkedAccount);
				// session.saveOrUpdate(assetAccount);
				// }
				// }
				//
				// }

				// for (FixedAsset fixedAsset : fixedAssets) {
				if (fixedAsset != null) {

					Calendar fromCal = new GregorianCalendar();
					Calendar toCal = new GregorianCalendar();

					/**
					 * Checking all the fixed assets whether they are registered
					 * or not. if Registered then we should run the depreciation
					 * for those Fixed Assets. Run the depreciation in other
					 * words creating the corresponding Journal Entries for the
					 * Depreciation.
					 */
					if (fixedAsset.getStatus() == FixedAsset.STATUS_REGISTERED) {
						fromCal.setTime((fixedAsset.getPurchaseDate()
								.getAsDateObject())
								.compareTo((this.depreciateFrom
										.getAsDateObject())) <= 0 ? (this.depreciateFrom
								.getAsDateObject()) : (fixedAsset
								.getPurchaseDate().getAsDateObject()));
						toCal.setTime(this.depreciateTo.getAsDateObject());
						fixedAsset
								.createJournalEntriesForDepreciationAtTheEndOfEveryMonthFromStartDateToEndDate(
										fromCal, toCal, session);
						ChangeTracker.put(fixedAsset);
					}

				}

				// }
			}
		}

		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}

		/**
		 * Checking whether this update call is for roll back this Depreciation
		 * or not.
		 */
		if (this.previousStatus == APPROVE && this.getStatus() == ROLLBACK) {

			// for (FixedAsset fixedAsset : this.getFixedAssets()) {
			if (fixedAsset != null) {

				// /**
				// *
				// * Save the Action into History
				// */
				// FixedAssetHistory fixedAssetHistory = new
				// FixedAssetHistory();
				// if (fixedAsset.isSoldOrDisposed()) {
				// fixedAssetHistory
				// .setActionType(FixedAssetHistory.ACTION_TYPE_DISPOSAL_REVERSED);
				// } else {
				// fixedAssetHistory
				// .setActionType(FixedAssetHistory.ACTION_TYPE_ROLLBACK);
				// }
				//
				// fixedAssetHistory.setActionDate(new FinanceDate());
				// fixedAssetHistory.setDetails("Depreciation Rolled back.");
				// fixedAssetHistory.setCompany(fixedAsset.getCompany());
				// fixedAsset.getFixedAssetsHistory().add(fixedAssetHistory);

				/**
				 * In order to roll-back the depreciation, we need to roll-back
				 * all the Journal Entries created at the time of Depreciation.
				 */
				// for (Transaction trans : fixedAsset.getTransactions()) {
				// if (trans.getDate().compareTo(this.depreciateFrom) >= 0) {
				// if (!trans.isVoid()) {
				// this.fixedAsset.setBookValue(this.fixedAsset
				// .getBookValue()
				// + trans.getTotal());
				// this.fixedAsset
				// .setAccumulatedDepreciationAmount(this.fixedAsset
				// .getAccumulatedDepreciationAmount()
				// - trans.getTotal());
				//
				// trans.setVoid(true);
				// if (trans instanceof Lifecycle) {
				// Lifecycle lifeCycle = (Lifecycle) trans;
				// lifeCycle.onUpdate(session);
				// }
				// }
				// }
				//
				// }
				FinanceDate lstDepreciationDate = null;
				FinanceDate depreciationStartDate = getCompany()
						.getPreferences().getDepreciationStartDate();
				Calendar startCal = Calendar.getInstance();
				startCal.setTime(depreciationStartDate.getAsDateObject());

				for (Transaction trans : fixedAsset.getTransactions()) {
					if (lstDepreciationDate == null)
						lstDepreciationDate = trans.getDate();
					if (trans.getDate()
							.compareTo(this.rollBackDepreciationDate) >= 0) {
						if (!trans.isVoid()) {
							// if (depreciationFor !=
							// DEPRECIATION_FOR_SINGLE_FIXEDASSET) {
							this.fixedAsset.setBookValue(this.fixedAsset
									.getBookValue() + trans.getTotal());
							// }

							this.fixedAsset
									.setAccumulatedDepreciationAmount(this.fixedAsset
											.getAccumulatedDepreciationAmount()
											- trans.getTotal());
							// To update the depreciation and Accumulated
							// Depreciation account
							JournalEntry journalEntry = (JournalEntry) trans;
							// for (Entry entry : journalEntry.entries) {
							// entry.updateAccountBalances(session, true);
							// }

							if (lstDepreciationDate.before(trans.getDate()))
								lstDepreciationDate = trans.getDate();
							Calendar rollBackCal = Calendar.getInstance();
							rollBackCal.setTime(trans.getDate()
									.getAsDateObject());

							// If we use Reducing balance to set the opening
							// balance for fiscal year to every previous year
							if (this.fixedAsset.getDepreciationMethod() == Depreciation.METHOD_REDUCING_BALANCE) {
								if ((rollBackCal.get(Calendar.MONTH) == startCal
										.get(Calendar.MONTH))
										&& (rollBackCal.get(Calendar.YEAR)) != startCal
												.get(Calendar.YEAR)) {
									double amount = this.fixedAsset
											.getOpeningBalanceForFiscalYear()
											* this.fixedAsset
													.getDepreciationRate()
											/ 1200;
									amount = amount / .88;
									this.fixedAsset
											.setOpeningBalanceForFiscalYear(this.fixedAsset
													.getOpeningBalanceForFiscalYear()
													+ 12 * amount);
								}

							}

							trans.setSaveStatus(Transaction.STATUS_VOID);
							if (trans instanceof Lifecycle) {
								Lifecycle lifeCycle = trans;
								lifeCycle.onUpdate(session);
							}
						}

					}

				}

				Calendar lastDepCal = Calendar.getInstance();
				Calendar depStartCal = Calendar.getInstance();

				// Date depreciationStartDate1 = Company.getCompany()
				// .getPreferences().getDepreciationStartDate();
				// Date lastDepreciationDate = getDepreciationLastDate();

				// depStartCal.setTime(depreciationStartDate);

				// Rakesh
				// lastDepCal.setTime(lstDepreciationDate);
				// DecimalFormat decimalFormat = new DecimalFormat("##.##");

				// double amount1 = this.fixedAsset
				// .getOpeningBalanceForFiscalYear()
				// * this.fixedAsset.getDepreciationRate() / 1200;
				// //
				// long number = Math.round(amount * 100);
				// amount = (double) ((double) number / 100.0);

				// amount = Utility.roundTo2Digits(amount);
				//
				// lastDepCal.set(Calendar.DAY_OF_MONTH, 1);
				// depStartCal.set(Calendar.DAY_OF_MONTH, 1);

				// if (lastDepCal.getTime().after(rollBackDepreciationDate)) {
				//
				// // if (this.fixedAsset.getDepreciationMethod() ==
				// Depreciation.METHOD_REDUCING_BALANCE) {
				// //
				// //
				// this.fixedAsset.setOpeningBalanceForFiscalYear(this.fixedAsset.getBookValue());
				// // }
				//
				// // if (lastDepCal.get(Calendar.MONTH) ==
				// depStartCal.get(Calendar.MONTH)) {
				// //
				// // amount = amount / 0.88;
				// // amount = Utility.roundTo2Digits(amount);
				// //
				// // }
				//
				// while
				// (!lastDepCal.getTime().before(rollBackDepreciationDate)) {
				//
				// if (!rollBackDepreciationDate.after(lastDepCal.getTime())) {
				//
				// this.fixedAsset.setBookValue(this.fixedAsset.getBookValue()+
				// amount);
				// lastDepCal.setTime(lstDepreciationDate);
				// DecimalFormat decimalFormat = new DecimalFormat("##.##");
				// }
				//
				// // if (this.fixedAsset.getDepreciationMethod() ==
				// Depreciation.METHOD_REDUCING_BALANCE) {
				// //
				// //
				// this.fixedAsset.setOpeningBalanceForFiscalYear(this.fixedAsset.getOpeningBalanceForFiscalYear()+
				// amount);
				// // }
				//
				// Calendar rollBackCal=Calendar.getInstance();
				// rollBackCal.setTime(rollBackDepreciationDate);
				//
				//
				//
				// lastDepCal.set(Calendar.MONTH, lastDepCal.get(Calendar.MONTH)
				// - 1);
				//
				// if (rollBackCal.get(Calendar.MONTH) ==
				// depStartCal.get(Calendar.MONTH)) {
				//
				// amount = amount / 0.88;
				// amount = Utility.roundTo2Digits(amount);
				//
				// }
				//
				// if (lastDepCal.getTime().compareTo( rollBackDepreciationDate)
				// >= 0) {
				//
				// boolean flag = false;
				// while (lastDepCal.get(Calendar.MONTH) !=
				// depStartCal.get(Calendar.MONTH)) {
				//
				// if (this.fixedAsset.getDepreciationMethod() ==
				// Depreciation.METHOD_REDUCING_BALANCE) {
				//
				// this.fixedAsset.setOpeningBalanceForFiscalYear(this.fixedAsset.getOpeningBalanceForFiscalYear()
				// + amount);
				//
				// lastDepCal.set(Calendar.MONTH, lastDepCal.get(Calendar.MONTH)
				// - 1);
				//
				// flag = true;
				// }
				// }
				//
				// if (flag) {
				//
				// if (this.fixedAsset.getDepreciationMethod() ==
				// Depreciation.METHOD_REDUCING_BALANCE) {
				// this.fixedAsset.setOpeningBalanceForFiscalYear(this.fixedAsset.getOpeningBalanceForFiscalYear()
				// + amount);
				// }
				// }
				// }
				// }
				//
				// if ((lastDepCal.getTime().before(rollBackDepreciationDate) &&
				// lastDepCal.get(Calendar.MONTH) == depStartCal
				// .get(Calendar.MONTH))) {
				// if (this.fixedAsset.getDepreciationMethod() ==
				// Depreciation.METHOD_REDUCING_BALANCE) {
				//
				// this.fixedAsset.setOpeningBalanceForFiscalYear(Utility.roundTo2Digits(this.fixedAsset.getOpeningBalanceForFiscalYear()+
				// amount));
				//
				// }
				// }
				//
				// }
				//
				// this.fixedAsset.setBookValue(Utility.roundTo2Digits(this.fixedAsset.getBookValue()));

				Calendar tempCal = Calendar.getInstance();
				tempCal.setTime(rollBackDepreciationDate.getAsDateObject());
				tempCal.set(Calendar.DAY_OF_MONTH,
						tempCal.get(Calendar.DAY_OF_MONTH) - 1);
				Date empCal = tempCal.getTime();
				this.setDepreciateTo(new FinanceDate((empCal.getTime())));
			}

			if (this.fixedAsset.getStatus() == FixedAsset.STATUS_SOLD_OR_DISPOSED) {
				// JournalEntry journalEntry = new JournalEntry();
				// journalEntry.doRollbackDepreciationJournalEntry(fixedAsset);
				this.fixedAsset.setStatus(FixedAsset.STATUS_REGISTERED);
				this.fixedAsset.setBookValue(this.fixedAsset.getBookValue()
						- this.fixedAsset.getPurchasePrice());
				this.fixedAsset.setSoldOrDisposedDate(null);
				// session.save(journalEntry);
			}

			if (rollBackDepreciationDate.after(this.depreciateFrom)) {
				this.setStatus(Depreciation.APPROVE);
			}

			fixedAsset.deleteJournalEntriesTillDate(rollBackDepreciationDate,
					session);
			// }

		}
		ChangeTracker.put(fixedAsset);
		return false;
	}

	@Override
	public boolean onDelete(Session s) throws CallbackException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLoad(Session s, Serializable id) {
		super.onLoad(s, id);
		this.previousStatus = status;
	}

	/**
	 * @param fixedAsset
	 * 
	 *            If this Fixed Asset is purchased before the Last Depreciation
	 *            Date then we must run Depreciation for this Fixed Asset from
	 *            the Purchase Date till the Last Depreciation Date. So the
	 *            following method should called.
	 */
	public static void runDepreciationFromPurchaseDateToLastDepreciationDate(
			FixedAsset fixedAsset) {

		FinanceDate startDate = fixedAsset.getCompany().getPreferences()
				.getDepreciationStartDate();
		Session session = HibernateUtil.getCurrentSession();
		Calendar fromCal = new GregorianCalendar();
		fromCal.setTime(fixedAsset.getPurchaseDate().after(startDate) ? (fixedAsset
				.getPurchaseDate().getAsDateObject()) : (startDate
				.getAsDateObject()));

		Calendar toCal = new GregorianCalendar();
		toCal.setTime(getDepreciationLastDate(fixedAsset.getCompany())
				.getAsDateObject());
		if (fromCal.getTime().compareTo(toCal.getTime()) != 0) {
			Depreciation depreciation = new Depreciation();
			depreciation.setStatus(APPROVE);
			// List<FixedAsset> fixedAssets = new ArrayList<FixedAsset>();
			// fixedAssets.add(fixedAsset);
			// depreciation.setFixedAssets(fixedAssets);
			depreciation.setFixedAsset(fixedAsset);
			depreciation.setDepreciationFor(DEPRECIATION_FOR_SINGLE_FIXEDASSET);
			depreciation.setDepreciateFrom(new FinanceDate(fromCal.getTime()));
			depreciation.setDepreciateTo(new FinanceDate(toCal.getTime()));
			depreciation.setCompany(fixedAsset.getCompany());
			session.save(depreciation);
		}
	}

	/**
	 * 
	 * @param rollBackDepreciationTo
	 * @param company
	 * @throws Exception
	 * 
	 *             The following method will roll back all the Approved
	 *             Depreciations till the given date.
	 * 
	 */

	public static void rollBackDepreciation(FinanceDate rollBackDepreciationTo,
			Company company) throws Exception {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session
				.getNamedQuery("getDepreciation.from.DepreciationFom.andStatus")
				.setParameter("depreciationFrom", rollBackDepreciationTo)
				.setInteger("status", Depreciation.APPROVE)
				.setEntity("company", company);
		List<Depreciation> list = query.list();
		for (Depreciation dep : list) {
			dep.setStatus(Depreciation.ROLLBACK);
			session.saveOrUpdate(dep);
		}
		// for (Depreciation dep : list) {
		// for (FixedAsset fixedAsset : dep.getFixedAssets()) {
		// fixedAsset.getTransactions().clear();
		// }
		// session.saveOrUpdate(dep);
		// }

	}

	public static void rollBackDepreciation(long fixedAssetID,
			FinanceDate rollBackDepreciationTo, Company company)
			throws Exception {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session
				.getNamedQuery(
						"getDepreciation.from.depreciateFrom.byFixedassetId")
				.setLong("date", rollBackDepreciationTo.getDate())
				.setInteger("status", Depreciation.APPROVE)
				.setLong("id", fixedAssetID).setEntity("company", company);
		List<Depreciation> list = query.list();
		for (Depreciation dep : list) {
			dep.setStatus(Depreciation.ROLLBACK);
			dep.setDepreciationFor(DEPRECIATION_FOR_SINGLE_FIXEDASSET);
			org.hibernate.Transaction t = session.beginTransaction();
			session.saveOrUpdate(dep);
			if (dep instanceof Lifecycle) {
				Lifecycle lifecycle = dep;
				lifecycle.onUpdate(session);
			}
			t.commit();

		}
	}

	/**
	 * This method is to get the last Depreciation Date till which the
	 * Depreciation run.
	 * 
	 * @param company
	 */

	public static FinanceDate getDepreciationLastDate(Company company) {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session
				.getNamedQuery("getMaxDepreciation.from.Depreciation.byStatus")
				.setInteger("depreciationFor",
						Depreciation.DEPRECIATION_FOR_ALL_FIXEDASSET)
				.setInteger("status", Depreciation.APPROVE)
				.setEntity("company", company);
		List<Depreciation> list = query.list();
		if (list != null && list.size() > 0 && list.get(0) != null) {
			Depreciation dep = list.get(0);
			return dep.getDepreciateTo();
		}
		return company.getPreferences().getDepreciationStartDate();
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.depreciation()).gap();

		w.put(messages.status(), this.status);

	}

	@Override
	public void selfValidate() throws AccounterException {
	}
}
