package com.nitya.accounter.core.migration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;

import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.PayeeUpdate;
import com.nitya.accounter.core.Transaction;
import com.nitya.accounter.core.TransactionEffectsImpl;
import com.nitya.accounter.web.client.exception.AccounterException;

public class Migrator23 extends AbstractMigrator {

	@Override
	public void migrate(Company company) throws AccounterException {
		log.info("Started Migrator23");

		for (Transaction transaction : company.getTransactions()) {
			int status = transaction.getSaveStatus();
			if (status == 201 || status == 202 || status == 204) {
				continue;
			}
			migratePayeeUpdates(company, transaction);
		}

		log.info("Finished Migrator23");
	}

	private void migratePayeeUpdates(Company company, Transaction transaction) {
		log.info("***Migrating Transaction ID: " + transaction.getID()
				+ " Number: " + transaction.getNumber() + " " + transaction);
		TransactionEffectsImpl tEffect = new TransactionEffectsImpl(transaction);
		transaction.getEffects(tEffect);

		Set<PayeeUpdate> oldPUs = new HashSet<PayeeUpdate>(transaction.getPayeeUpdates());

		List<PayeeUpdate> newPUs = tEffect.getNewPUs();

		findOutIntersectionPUs(oldPUs, newPUs);

		if (!oldPUs.isEmpty() || !newPUs.isEmpty()) {
			transaction.getPayeeUpdates().removeAll(oldPUs);
			insertPayeeUpdates(company, transaction, newPUs);
		}
		getSession().saveOrUpdate(transaction);

	}

	private void insertPayeeUpdates(Company company, Transaction transaction,
			List<PayeeUpdate> newPUs) {
		if (newPUs.isEmpty()) {
			return;
		}
		log.info("***Inserting PayeeUpdates Transaction ID: "
				+ transaction.getID() + " Number: " + transaction.getNumber()
				+ " " + transaction);
		for (PayeeUpdate pu : newPUs) {
			Query query = getSession()
					.getNamedQuery("insert.Payee.update")
					.setParameter("payeeId", pu.getPayee().getID())
					.setParameter("transactionId", pu.getTransaction().getID())
					.setParameter("amount", pu.getAmount())
					.setParameter("companyId", company.getId())
					.setParameter("createdBy",
							pu.getTransaction().getCreatedBy().getID());
			query.executeUpdate();
		}
	}

	@Override
	public int getVersion() {
		return 23;
	}

}
