package com.nitya.accounter.listeners;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import com.nitya.accounter.core.AccounterThreadLocal;
import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.core.RecurringTransaction;
import com.nitya.accounter.core.Transaction;
import com.nitya.accounter.utils.HibernateUtil;

public class StartupScriptRunner {

	Logger log = Logger.getLogger(StartupScriptRunner.class);

	private static StartupScriptRunner instance;

	boolean isRunning = false;

	public static StartupScriptRunner getInstance() {
		if (instance == null) {
			instance = new StartupScriptRunner();
		}
		return instance;
	}

	public void run() {
		isRunning = true;
		deleteAllIncorrectAutoTransactions();
		rescheduleRecurringTransactions();
		isRunning = false;
	}

	@SuppressWarnings("unchecked")
	private void rescheduleRecurringTransactions() {
		log.info("Started  Rescheduling all recurring transactions.");
		Session getSession = HibernateUtil.openSession();
		List<Long> rts = new ArrayList<Long>();
		try {
			Query query = getSession.getNamedQuery("get.recurringTransactions");
			rts = query.list();
		} finally {
			getSession.close();
		}

		for (Long rtId : rts) {
			Session session = HibernateUtil.openSession();
			org.hibernate.Transaction hTransaction = session.beginTransaction();
			try {
				RecurringTransaction rTransaction = (RecurringTransaction) session
						.get(RecurringTransaction.class, rtId);

				boolean needToUpdate = false;
				if (rTransaction.getPrevScheduleOn() != null) {
					while (rTransaction.getPrevScheduleOn().compareTo(
							new FinanceDate(20120907)) >= 0) {
						needToUpdate = true;
						rTransaction.schedulePrevious();
					}
				}

				if (needToUpdate) {
					session.saveOrUpdate(rTransaction);
				}

				hTransaction.commit();
			} catch (Exception e) {
				log.error("Error while updating Recurring Transaction : "
						+ rtId, e);
				hTransaction.rollback();
			} finally {
				session.close();
			}
		}
		log.info("Completed Rescheduling all recurring transactions.");
	}

	@SuppressWarnings("unchecked")
	private void deleteAllIncorrectAutoTransactions() {
		log.info("Started Deleting all incorrect automatic transactions.");
		Session getSession = HibernateUtil.openSession();
		List<Long> transactions = new ArrayList<Long>();
		try {
			Query query = getSession
					.getNamedQuery("get.All.Incorrect.Auto.Transactions");
			transactions = query.list();
		} finally {
			getSession.close();

		}

		for (Long tId : transactions) {
			Session session = HibernateUtil.openSession();
			org.hibernate.Transaction hTransaction = session.beginTransaction();
			try {
				Transaction aTransaction = (Transaction) session.get(
						Transaction.class, tId);
				AccounterThreadLocal.set(aTransaction.getCreatedBy());
				session.delete(aTransaction);
				hTransaction.commit();
			} catch (Exception e) {
				log.error(
						"Error while deleting incorrect automatic transaction :"
								+ tId, e);
				hTransaction.rollback();
			} finally {
				session.close();
			}

		}
		log.info("Completed Deleting all incorrect automatic transactions.");
	}

	public boolean isRunning() {
		return isRunning;
	}

}
