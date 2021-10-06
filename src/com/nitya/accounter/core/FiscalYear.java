package com.nitya.accounter.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONException;

import com.nitya.accounter.core.change.ChangeTracker;
import com.nitya.accounter.services.SessionUtils;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCommand;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.externalization.AccounterMessages;

public class FiscalYear extends CreatableObject implements IAccounterServerCore {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3871302599394319194L;
	public static final int STATUS_OPEN = 1;
	public static final int STATUS_CLOSE = 2;

	/**
	 * The StartDate for the Fiscal Year
	 */
	FinanceDate startDate;

	/**
	 * The End Date for the Fiscal Year
	 */
	FinanceDate endDate;

	/**
	 * Fiscal Year status, either STATUS_OPEN or STATUS_CLOSE
	 */
	int status = STATUS_OPEN;

	boolean isCurrentFiscalYear = Boolean.FALSE;

	/**
	 * Previous Start Date
	 */
	transient FinanceDate previousStartDate;

	transient int previousStatus;

	boolean isDefault;

	transient FinanceDate previousEndDate;

	public FiscalYear() {
	}

	public FiscalYear(FinanceDate startDate, FinanceDate endDate, int status,
			boolean isCurrentFiscalYear) {

		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.isCurrentFiscalYear = isCurrentFiscalYear;

	}

	/**
	 * @return the startDate
	 */
	public FinanceDate getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(FinanceDate startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public FinanceDate getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(FinanceDate endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the isCurrentFiscalYear
	 */
	public boolean getIsCurrentFiscalYear() {
		return isCurrentFiscalYear;
	}

	/**
	 * @param isCurrentFiscalYear
	 *            the isCurrentFiscalYear to set
	 */
	public void setIsCurrentFiscalYear(boolean isCurrentFiscalYear) {
		this.isCurrentFiscalYear = isCurrentFiscalYear;
	}

	public FinanceDate getPreviousStartDate() {
		return previousStartDate;
	}

	public void setPreviousStartDate(FinanceDate previousStartDate) {
		this.previousStartDate = previousStartDate;
	}

	/**
	 * @return the isDefault
	 */
	public boolean isDefault() {
		return isDefault;
	}

	/**
	 * @param isDefault
	 *            the isDefault to set
	 */
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	@Override
	public boolean onDelete(Session s) throws CallbackException {
		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(getID());
		accounterCore.setObjectType(AccounterCoreType.FISCALYEAR);
		ChangeTracker.put(accounterCore);
		return false;
	}

	@Override
	public void onLoad(Session s, Serializable id) {
		super.onLoad(s, id);
		this.previousStartDate = this.startDate;
		this.previousStatus = this.status;
		this.previousEndDate = this.endDate;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed) {
			return true;
		}
		super.onSave(session);
		this.isOnSaveProccessed = true;
		// this.setPreviousStartDate(this.getStartDate());
		this.setStatus(FiscalYear.STATUS_OPEN);
		Utility.updateCurrentFiscalYear(getCompany());
		onUpdate(session);
		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		super.onUpdate(session);
		if (this.previousStatus == STATUS_OPEN && this.status == STATUS_CLOSE) {

			SessionUtils.update(this, session);
			// move the Net Income to the Retain Earnings Account and updating
			// the Income and Expense accounts balances accordingly.

			Query query = session
					.getNamedQuery("getNetIncome")
					.setParameter("companyId", getCompany().getID())
					.setParameter("startDate", this.previousStartDate.getDate())
					.setParameter("endDate", this.endDate.getDate());

			List list = query.list();
			double netIncome = 0;
			if (list != null && list.size() > 0) {
				netIncome = list.get(0) == null ? 0.0 : (Double) list.get(0);

				Query q = session
						.getNamedQuery(
								"getFisaclId.andSum.fromAccountTransaction")
						.setParameter("startDate", this.previousStartDate)
						.setParameter("endDate", this.endDate)
						.setEntity("company", getCompany());

				List list1 = q.list();
				Object[] object = null;
				List<AccountTransactionByAccount> accountTransactionList = new ArrayList<AccountTransactionByAccount>();
				Iterator it = list1.iterator();
				while (it.hasNext()) {
					object = (Object[]) it.next();
					AccountTransactionByAccount accountTransactionByTaxAccount = new AccountTransactionByAccount();
					accountTransactionByTaxAccount
							.setAccount(object[0] != null ? (Account) session
									.get(Account.class, (Long) object[0])
									: null);
					accountTransactionByTaxAccount
							.setAmount(object[1] != null ? (Double) object[1]
									: 0.0);
					accountTransactionList.add(accountTransactionByTaxAccount);
				}

				// Query nextTransactionNumberQuery = session
				// .getNamedQuery("getNextTransactionNumber");
				// nextTransactionNumberQuery.setLong("type",
				// Transaction.TYPE_JOURNAL_ENTRY);
				// List l = nextTransactionNumberQuery.list();
				// long nextTransactionNumber = 1;
				// if (l != null && l.size() > 0) {
				// nextTransactionNumber = ((Long) l.get(0)).longValue() + 1;
				// }
				String nextTransactionNumber = NumberUtils
						.getNextTransactionNumber(
								Transaction.TYPE_JOURNAL_ENTRY, getCompany());
				// One Journal Entry for this closing Fiscal Year.
				// Journal Entries for Income and Expenses Accounts for the
				// above amount adjustments
				// JournalEntry journalEntry = new JournalEntry(this, netIncome,
				// accountTransactionList, nextTransactionNumber,
				// JournalEntry.TYPE_NORMAL_JOURNAL_ENTRY);
				// session.save(journalEntry);

				List<AccountTransactionByAccount> cashBasisAccountEntries = new ArrayList<AccountTransactionByAccount>();
				for (AccountTransactionByAccount accountTransactionByAccount : accountTransactionList) {
					if (accountTransactionByAccount.getAccount().isConsiderAsCashAccount
							&& !(accountTransactionByAccount
									.getAccount()
									.getName()
									.equals(AccounterServerConstants.OTHER_CASH_INCOME) || accountTransactionByAccount
									.getAccount()
									.getName()
									.equals(AccounterServerConstants.OTHER_CASH_EXPENSE))) {
						cashBasisAccountEntries
								.add(accountTransactionByAccount);
					}
				}

				if (cashBasisAccountEntries.size() > 0) {

					// JournalEntry cashBasisJournalEntry = new JournalEntry(
					// this,
					// cashBasisAccountEntries,
					// NumberUtils
					// .getStringwithIncreamentedDigit(nextTransactionNumber),
					// JournalEntry.TYPE_CASH_BASIS_JOURNAL_ENTRY);
					// session.save(cashBasisJournalEntry);
				}

			}
		} else if (this.getPreviousStartDate() != null
				&& this.getStartDate().equals(this.getPreviousStartDate())) {
			// session.saveOrUpdate(this);
		} else if ((this.getPreviousStartDate() != null && !this.getStartDate()
				.equals(this.getPreviousStartDate()))
				|| this.getPreviousStartDate() == null) {
			Company company = getCompany();
			if (company != null) {
				company.getPreferences().setStartDate(this.startDate);
				// company.getPreferences().setPreventPostingBeforeDate(
				// this.startDate);
				session.saveOrUpdate(company);
				this.setEndDate(getEndDateForStartDate(this.startDate.getDate()));
				checkIsCurrentFY(this);
				// session.saveOrUpdate(this);
				addOrUpdateFiscalYears(this);
			}
		}
		// else if (!this.getStartDate().equals(this.getPreviousStartDate())) {
		//
		// List<FiscalYear> list = new ArrayList<FiscalYear>();
		// Date modifiedStartDate = this.getStartDate();
		// Date existingLeastStartDate = modifiedStartDate;
		// Date existingHighestEndDate = modifiedStartDate;
		// Boolean exist = Boolean.FALSE;
		// list = session
		// .createQuery(
		// "from com.nitya.accounter.core.FiscalYear f order by f.startDate")
		// .list();
		// if (list.size() > 0) {
		// Iterator<FiscalYear> i = list.iterator();
		// // if (i.hasNext()) {
		// // FiscalYear fs = (FiscalYear) i.next();
		// // existingLeastStartDate = fs.getStartDate();
		// // existingHighestEndDate = fs.getEndDate();
		// // }
		// // i = list.iterator();
		// // while (i.hasNext()) {
		// // FiscalYear fs = (FiscalYear) i.next();
		// // if (modifiedStartDate.after(fs.getStartDate())
		// // && modifiedStartDate.before(fs.getEndDate())) {
		// // exist = Boolean.TRUE;
		// // break;
		// // }
		// // if (fs.getStartDate().before(existingLeastStartDate)) {
		// // existingLeastStartDate = fs.getStartDate();
		// // }
		// // if (fs.getEndDate().after(existingHighestEndDate)) {
		// // existingHighestEndDate = fs.getEndDate();
		// // }
		// //
		// // }
		// FiscalYear firstFiscalYear = list.get(0) != null ? list.get(0)
		// : null;
		// FiscalYear lastFiscalYear = null;
		// while (i.hasNext()) {
		// lastFiscalYear = (FiscalYear) i.next();
		// }
		// existingLeastStartDate = firstFiscalYear.getPreviousStartDate();
		//
		// existingHighestEndDate = lastFiscalYear.getEndDate();
		//
		// if ((modifiedStartDate.before(existingLeastStartDate) ||
		// modifiedStartDate
		// .after(existingHighestEndDate))) {
		// Calendar cal = Calendar.getInstance();
		// cal.setTime(modifiedStartDate);
		// Integer modifiedYear = cal.get(Calendar.YEAR);
		//
		// cal.setTime(existingLeastStartDate);
		// Integer existingLeastYear = cal.get(Calendar.YEAR);
		//
		// cal.setTime(existingHighestEndDate);
		// Integer existingHighestYear = cal.get(Calendar.YEAR);
		// if (modifiedStartDate.before(existingLeastStartDate)) {
		// int diff = existingLeastYear - modifiedYear;
		// for (int k = 0; k < diff; k++) {
		//
		// cal.set(modifiedYear + k, 0, 1);
		// Date startDate = cal.getTime();
		//
		// cal.set(modifiedYear + k, 11, 31);
		// Date endDate = cal.getTime();
		//
		// FiscalYear fs = new FiscalYear(startDate, endDate,
		// FiscalYear.STATUS_OPEN, Boolean.FALSE);
		// session.save(fs);
		// }
		//
		// } else if (modifiedStartDate.after(existingHighestEndDate)) {
		// int diff = modifiedYear - existingHighestYear;
		// for (int k = 1; k <= diff; k++) {
		// cal.set(existingHighestYear + k, 0, 1);
		// Date startDate = cal.getTime();
		//
		// cal.set(existingHighestYear + k, 11, 31);
		// Date endDate = cal.getTime();
		// FiscalYear fs = new FiscalYear(startDate, endDate,
		// FiscalYear.STATUS_OPEN, Boolean.FALSE);
		// session.save(fs);
		// }
		// }
		//
		// }
		// this.setStartDate(this.getPreviousStartDate());
		// session.saveOrUpdate(this);
		//
		// Company company = Company.getCompany();
		// company.getPreferences().setStartDate(this.startDate);
		// company.getPreferences().setPreventPostingBeforeDate(
		// this.startDate);
		// session.saveOrUpdate(company);
		// }
		// }
		this.setPreviousStartDate(this.getStartDate());
		Utility.updateCurrentFiscalYear(getCompany());
		ChangeTracker.put(this);
		return false;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean canDelete(FiscalYear object) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		List list = session.getNamedQuery("getTransaction.by.Transactiondates")
				.setParameter("startDate", object.getStartDate())
				.setParameter("endDate", object.getEndDate())
				.setEntity("company", getCompany()).list();
		if (list != null && list.size() != 0)
			throw new AccounterException(AccounterException.ERROR_OBJECT_IN_USE);
		// "You already created some transaction in this period, You can't delete");
		return true;
	}

	private void addOrUpdateFiscalYears(FiscalYear presentFiscalYear) {
		// modifyFiscalYears(presentFiscalYear);
		Session session = HibernateUtil.getCurrentSession();
		List transactionDates = session
				.getNamedQuery("get.TransactionDate.from.TransactionbyDate")
				.setParameter("date", this.getStartDate())
				.setEntity("company", presentFiscalYear.getCompany()).list();
		Iterator it = transactionDates.iterator();
		while (it.hasNext()) {
			FinanceDate date = (FinanceDate) it.next();
			createFiscalYearForExistingTransactions(date);
		}
	}

	private FinanceDate getEndDateForStartDate(long startDate) {
		FinanceDate date = new FinanceDate(startDate);
		date.setYear(date.getYear() + 1);
		Calendar startDateCal = Calendar.getInstance();
		startDateCal.setTime(date.getAsDateObject());

		Calendar endDateCal = Calendar.getInstance();
		endDateCal.setTime(startDateCal.getTime());
		endDateCal.set(Calendar.DAY_OF_MONTH,
				startDateCal.get(Calendar.DAY_OF_MONTH) - 1);

		return new FinanceDate(endDateCal.getTime());
	}

	private void createFiscalYearForExistingTransactions(
			FinanceDate transactionDate) {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getFisacalyear.by.Startdate")
				.setEntity("company", getCompany());
		List list = query.list();
		// Object[] object = (Object[]) list.get(0);

		FinanceDate existingLeastStartDate = ((FiscalYear) list.get(0))
				.getStartDate();
		FinanceDate existingHighestEndDate = ((FiscalYear) list
				.get(list.size() - 1)).getEndDate();

		Calendar leastStartDateCal = Calendar.getInstance();
		leastStartDateCal.setTime(existingLeastStartDate.getAsDateObject());

		Calendar highestEndDateCal = Calendar.getInstance();
		highestEndDateCal.setTime(existingHighestEndDate.getAsDateObject());

		Calendar modifiedDateCal = Calendar.getInstance();
		modifiedDateCal.setTime(transactionDate.getAsDateObject());

		if (transactionDate.getDate() >= existingLeastStartDate.getDate()
				&& transactionDate.getDate() <= existingHighestEndDate
						.getDate()) {
			return;
		} else if (transactionDate.getDate() < existingLeastStartDate.getDate()) {

			Calendar tempCal = Calendar.getInstance();
			tempCal.setTime(leastStartDateCal.getTime());

			FinanceDate tempDate = existingLeastStartDate;

			while (tempDate.getDate() > transactionDate.getDate()) {

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
				checkIsCurrentFY(fs);
				session.save(fs);
				ChangeTracker.put(fs);

				tempDate = startDate;

				tempCal.setTime(startDate.getAsDateObject());

			}

		} else if (transactionDate.getDate() > existingHighestEndDate.getDate()) {

			Calendar tempCal = Calendar.getInstance();
			tempCal.setTime(highestEndDateCal.getTime());

			FinanceDate tempDate = existingHighestEndDate;

			while (tempDate.getDate() < transactionDate.getDate()) {

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
				checkIsCurrentFY(fs);
				session.save(fs);
				ChangeTracker.put(fs);

				tempDate = endDate;

				tempCal.setTime(endDate.getAsDateObject());
			}
		}
	}

	public void checkIsCurrentFY(FiscalYear fiscalYear) {
		FinanceDate presentDate = new FinanceDate();
		if (presentDate.getDate() >= fiscalYear.getStartDate().getDate()
				&& presentDate.getDate() <= fiscalYear.getEndDate().getDate())
			fiscalYear.setIsCurrentFiscalYear(true);
		else
			fiscalYear.setIsCurrentFiscalYear(false);
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.fiscalYear()).gap().gap();
		w.put(messages.status(), this.status);
	}

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub

	}
}
