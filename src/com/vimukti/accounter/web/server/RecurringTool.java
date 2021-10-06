package com.vimukti.accounter.web.server;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.MessageOrTask;
import com.vimukti.accounter.core.RecurringTransaction;
import com.vimukti.accounter.core.Reminder;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.listeners.StartupScriptRunner;
import com.vimukti.accounter.main.ServerLocal;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.UIUtils;

public class RecurringTool extends Thread {

	Logger log = Logger.getLogger(RecurringTool.class);

	@Override
	public void run() {

		File startupScript = new File("StartupScript");

		if (startupScript.exists()) {
			StartupScriptRunner instance = StartupScriptRunner.getInstance();
			if (instance.isRunning()) {
				return;
			}
			instance.run();
			startupScript.delete();
		}

		log.info("Recurring schedule Started.");
		if (ServerLocal.get() == null) {
			ServerLocal.set(Locale.ENGLISH);
		}
		List<Long> recurrings = new ArrayList<Long>();
		Session session = HibernateUtil.openSession();
		try {
			Query query = session
					.getNamedQuery("getPresentRecurringTransactions");
			recurrings = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		executeRecurringSchedules(recurrings);
		log.info("Recurring schedule completed.");
	}

	public void executeRecurringSchedules(List<Long> recurrings) {

		Map<Long, FinanceDate> companyTransactions = new HashMap<Long, FinanceDate>();
		Map<Long, FinanceDate> companyReminders = new HashMap<Long, FinanceDate>();

		for (Long rtId : recurrings) {

			boolean recurringFailed = false;

			Session session = HibernateUtil.openSession();
			org.hibernate.Transaction hTransaction = session.beginTransaction();
			try {

				RecurringTransaction rTransaction = (RecurringTransaction) session
						.get(RecurringTransaction.class, rtId);

				Set<String> features = rTransaction.getCompany().getCreatedBy()
						.getClient().getClientSubscription().getSubscription()
						.getFeatures();
				if (!features.contains(Features.RECURRING_TRANSACTIONS)) {
					continue;
				}

				executeRecurring(rTransaction, companyTransactions,
						companyReminders);

				hTransaction.commit();
			} catch (Exception e) {
				log.error("Unable to Create Recurring Transaction for : "
						+ rtId, e);
				if (hTransaction != null) {
					hTransaction.rollback();
				}

				recurringFailed = true;

			} finally {
				session.close();
			}

			if (recurringFailed) {
				// MAKE IT AS FAILED RECURRING
				makeRecurringFail(rtId);
			}
		}

		for (Entry<Long, FinanceDate> entry : companyTransactions.entrySet()) {
			createAutomaticTransactionMessage(entry.getValue(), entry.getKey());
		}
		for (Entry<Long, FinanceDate> entry : companyReminders.entrySet()) {
			createRecurringReminderTask(entry.getValue(), entry.getKey());
		}
	}

	private void makeRecurringFail(Long rtId) {
		Session session = HibernateUtil.openSession();
		org.hibernate.Transaction hTransaction = session.beginTransaction();
		try {
			Query query = session.getNamedQuery(
					"make.RecurringTranscation.Fail")
					.setParameter("rtId", rtId);
			query.executeUpdate();
			hTransaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			hTransaction.rollback();
		} finally {
			session.close();
		}
	}

	private void executeRecurring(RecurringTransaction rTransaction,
			Map<Long, FinanceDate> companyTransactions,
			Map<Long, FinanceDate> companyReminders) throws AccounterException,
			CloneNotSupportedException {
		Session session = HibernateUtil.getCurrentSession();

		FinanceDate nextScheduleOn = rTransaction.getNextScheduleOn();
		AccounterThreadLocal.set(rTransaction.getCreatedBy());
		Calendar cal = Calendar.getInstance();
		cal.setTime(nextScheduleOn.getAsDateObject());
		cal.add(Calendar.DAY_OF_MONTH, -rTransaction.getDaysBeforeToRemind());
		FinanceDate remindDate = new FinanceDate(cal.getTime());
		if (rTransaction.getType() == RecurringTransaction.RECURRING_SCHEDULED) {
			Transaction transaction = FinanceTool
					.createDuplicateTransaction(rTransaction);
			transaction.setSaveStatus(Transaction.STATUS_APPROVE);
			transaction.setAutomaticTransaction(true);
			session.saveOrUpdate(transaction);
			if (rTransaction.isNotifyCreatedTransaction()) {
				FinanceDate transactionDate = companyTransactions
						.get(rTransaction.getCompany().getID());
				if (transactionDate == null) {
					transactionDate = remindDate;
				} else if (remindDate.before(transactionDate)) {
					transactionDate = remindDate;
				}
				companyTransactions.put(rTransaction.getCompany().getID(),
						transactionDate);
			}
		} else if (rTransaction.getType() == RecurringTransaction.RECURRING_REMINDER) {
			Reminder reminder = new Reminder(rTransaction);
			session.saveOrUpdate(reminder);
			FinanceDate remind = companyReminders.get(rTransaction.getCompany()
					.getID());
			if (remind == null) {
				remind = remindDate;
			} else if (remindDate.before(remind)) {
				remind = remindDate;
			}
			companyReminders.put(rTransaction.getCompany().getID(), remind);
		}

		// Schedule again for next recurring
		rTransaction.scheduleAgain();

		// Stop the recurring according to end date type.
		if (rTransaction.getEndDateType() == RecurringTransaction.END_DATE_DATE) {
			if (rTransaction.getNextScheduleOn() == null
					|| !rTransaction.getEndDate().after(
							rTransaction.getNextScheduleOn())) {
				rTransaction.setStopped(true);
			}
		} else if (rTransaction.getEndDateType() == RecurringTransaction.END_DATE_OCCURRENCES) {
			if (rTransaction.getOccurencesCount() <= rTransaction
					.getOccurencesCompleted()) {
				rTransaction.setStopped(true);
			}
		}
		if (rTransaction.isStopped() && rTransaction.isAlertWhenEnded()) {
			MessageOrTask message = new MessageOrTask();
			message.setType(MessageOrTask.TYPE_MESSAGE);
			message.setDate(remindDate == null ? nextScheduleOn : remindDate);
			message.setSystemCreated(true);
			message.setContent(Global.get().messages()
					.recurringTemplateHasEnded(rTransaction.getName()));
			int transactionType = rTransaction.getTransaction().getType();
			AccounterCoreType type = UIUtils
					.getAccounterCoreType(transactionType);
			StringBuffer token = new StringBuffer(
					getActionToken(transactionType));
			token.append("?");
			token.append(type.toString().toLowerCase());
			token.append(":");
			token.append(rTransaction.getTransaction().getID());
			message.setActionToken(token.toString());
			message.setCompany(rTransaction.getCompany());
			session.saveOrUpdate(message);

		}
		rTransaction.setStatus(RecurringTransaction.STATUS_SUCCESS);
		session.saveOrUpdate(rTransaction);
	}

	private void createRecurringReminderTask(FinanceDate remindDate,
			long companyId) {
		Session session = HibernateUtil.openSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		try {

			Company company = (Company) session.get(Company.class, companyId);

			MessageOrTask task = (MessageOrTask) session
					.getNamedQuery("getRecurringReminderTask")
					.setParameter("companyId", companyId).uniqueResult();

			if (task == null) {
				task = new MessageOrTask();
				task.setType(MessageOrTask.TYPE_TASK);
				task.setSystemCreated(true);
				task.setContent(Global.get().messages()
						.respondToYourRemindersAboutRecurringTemplates());
				task.setContentType(MessageOrTask.CONTENT_TYPE_RECURRING_REMINDER);
			}
			task.setActionToken(HistoryTokens.RECURRINGREMINDERS);
			if (task.getDate() == null || remindDate.before(task.getDate())) {
				task.setDate(remindDate);
			}
			task.setCompany(company);
			session.saveOrUpdate(task);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
		} finally {
			session.close();
		}
	}

	private void createAutomaticTransactionMessage(FinanceDate transactionDate,
			Long companyId) {
		Session session = HibernateUtil.openSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		try {
			Company company = (Company) session.get(Company.class, companyId);
			MessageOrTask message = (MessageOrTask) session
					.getNamedQuery("getAutomaticTransactionsMessage")
					.setParameter("companyId", companyId).uniqueResult();

			if (message == null) {
				message = new MessageOrTask();
				message.setType(MessageOrTask.TYPE_MESSAGE);
				message.setSystemCreated(true);
				message.setContent(Global.get().messages()
						.transactionsWereAutomaticallyCreated());
				message.setContentType(MessageOrTask.CONTENT_TYPE_AUTOMATIC_TRANSACTIONS);
			}
			message.setActionToken(HistoryTokens.AUTOMATICTRANSACTIONS);
			if (message.getDate() == null
					|| transactionDate.before(message.getDate())) {
				message.setDate(transactionDate);
			}
			message.setCompany(company);
			session.saveOrUpdate(message);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
		} finally {
			session.close();
		}
	}

	private String getActionToken(int type) {
		String token = "";
		switch (type) {

		case ClientTransaction.TYPE_TRANSFER_FUND:
			token = HistoryTokens.DEPOSITETRANSFERFUNDS;
			break;
		case ClientTransaction.TYPE_ENTER_BILL:
			token = HistoryTokens.ENTERBILL;
			break;
		case ClientTransaction.TYPE_CASH_PURCHASE:
			token = HistoryTokens.NEWCASHPURCHASE;
			break;
		case ClientTransaction.TYPE_CASH_SALES:
			token = HistoryTokens.NEWCASHSALE;
			break;
		case ClientTransaction.TYPE_WRITE_CHECK:
			token = HistoryTokens.WRITECHECK;
			break;
		case ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO:
			token = HistoryTokens.NRECREDITNOTE;
			break;
		case ClientTransaction.TYPE_INVOICE:
			token = HistoryTokens.NEWINVOICE;
			break;
		case ClientTransaction.TYPE_ESTIMATE:
			token = HistoryTokens.NEWQUOTE;
			break;
		case ClientTransaction.TYPE_VENDOR_CREDIT_MEMO:
			token = HistoryTokens.VENDORCREDIT;
			break;

		case ClientTransaction.TYPE_CASH_EXPENSE:
			token = "cashExpense";
			break;

		case ClientTransaction.TYPE_CREDIT_CARD_EXPENSE:
			token = "creditCardExpense";
			break;
		case ClientTransaction.TYPE_PAY_RUN:
			token = HistoryTokens.NEW_PAYRUN;
			break;
		case ClientTransaction.TYPE_PAY_EMPLOYEE:
			token = HistoryTokens.PAY_EMPLOYEE;
			break;
		}
		return token;
	}
}