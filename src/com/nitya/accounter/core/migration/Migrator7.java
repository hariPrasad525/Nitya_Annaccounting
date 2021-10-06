package com.nitya.accounter.core.migration;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Query;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.AccountTransaction;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.InventoryPurchase;
import com.nitya.accounter.core.Transaction;
import com.nitya.accounter.core.TransactionEffectsImpl;
import com.nitya.accounter.core.TransactionItem;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;

public class Migrator7 extends AbstractMigrator {
	Logger log = Logger.getLogger(Migrator7.class);

	@Override
	public int getVersion() {
		return 7;
	}

	@Override
	public void migrate(Company company) {
		log.info("Started Migrator7.");

		Query query = getSession().getNamedQuery(
				"getAccountTransactionsOfVoidTransaction").setEntity("company",
				company);

		List<AccountTransaction> aTs = query.list();
		Iterator<AccountTransaction> iterator = aTs.iterator();
		while (iterator.hasNext()) {
			AccountTransaction next = iterator.next();
			getSession().delete(next);
			next.getTransaction().getAccountTransactionEntriesList()
					.remove(next);
		}

		query = getSession().getNamedQuery("getAllTransactionOfCompany")
				.setEntity("company", company);
		List<Transaction> list = query.list();
		for (Transaction transaction : list) {
			try {
				migrateTransaction(transaction);
			} catch (Exception e) {
				throw new RuntimeException();
			}
		}
		log.info("Finished Migrator7");
	}

	/**
	 * Migrates the Transaction
	 * 
	 * @param transaction
	 * @throws AccounterException
	 */
	private void migrateTransaction(Transaction transaction) throws AccounterException {
		int a = 0;
		log.info("***Migrating Transaction ID: " + transaction.getID()
				+ " Number: " + transaction.getNumber() + " " + transaction);
		Set<AccountTransaction> oldATs = new HashSet<AccountTransaction>(
				transaction.getAccountTransactionEntriesList());

		for (TransactionItem tItem : transaction.getTransactionItems()) {
			if (tItem.getType() == TransactionItem.TYPE_ITEM
					&& tItem.getItem() != null && tItem.getItem().isInventory()) {
				matchATandIP(oldATs, tItem.getPurchases(), tItem.getItem()
						.getAssestsAccount());
			}
		}

		TransactionEffectsImpl tEffects = new TransactionEffectsImpl(
				transaction);
		transaction.getEffects(tEffects);
		List<AccountTransaction> newATs = tEffects.getNewATs();
		tEffects.checkTrailBalance();

		// Finding Intersection
		findOutIntersectionAT(oldATs, newATs);

		if (!oldATs.isEmpty() || !newATs.isEmpty()) {
			log.warn("Removing ATs : " + oldATs);
			transaction.getAccountTransactionEntriesList().removeAll(oldATs);

			log.warn("Creating ATs : " + newATs);
			transaction.getAccountTransactionEntriesList().addAll(newATs);
		}
		getSession().save(transaction);
	}

	private void findOutIntersectionAT(Set<AccountTransaction> oldATs,
			List<AccountTransaction> newATs) {
		Iterator<AccountTransaction> oldAtsIterator = oldATs.iterator();
		while (oldAtsIterator.hasNext()) {
			AccountTransaction oldAT = oldAtsIterator.next();
			Iterator<AccountTransaction> newATsIterator = newATs.iterator();
			while (newATsIterator.hasNext()) {
				AccountTransaction newAT = newATsIterator.next();
				if (oldAT.getTransaction() == newAT.getTransaction()
						&& oldAT.getAccount() == newAT.getAccount()

						&& DecimalUtil.isEquals(oldAT.getAmount(),
								newAT.getAmount())) {
					newATsIterator.remove();
					oldAtsIterator.remove();
					break;
				}
			}
		}
	}

	/**
	 * It will check the Suitable AccountTrannsaction for this Purchases and
	 * removes it from List
	 * 
	 * @param oldATs
	 * @param purchases
	 * @param assetAccount
	 * @return
	 */
	private double matchATandIP(Set<AccountTransaction> oldATs,
			Set<InventoryPurchase> purchases, Account assetAccount) {
		double assetChange = 0.00D;
		for (InventoryPurchase purchase : purchases) {
			double purchaseValue = purchase.getQuantity().calculatePrice(
					purchase.getCost());
			assetChange += purchaseValue;
			Account effectingAccount = purchase.getEffectingAccount();
			purchaseValue = (effectingAccount.isIncrease() ? 1 : -1)
					* purchaseValue;
			checkAndRemoveAT(oldATs, purchase.getEffectingAccount(),
					-purchaseValue);
		}
		assetChange = (assetAccount.isIncrease() ? 1 : -1) * assetChange;
		checkAndRemoveAT(oldATs, assetAccount, assetChange);
		return assetChange;
	}

	/**
	 * It will check the AccountTransaction with same account and amount. If
	 * exists removes that from list and Make the VAR 'updateAccount' false;
	 * 
	 * @param ats
	 * @param account
	 * @param amount
	 * @return
	 */
	private AccountTransaction checkAndRemoveAT(Set<AccountTransaction> ats,
			Account account, double amount) {
		Iterator<AccountTransaction> iterator = ats.iterator();
		while (iterator.hasNext()) {
			AccountTransaction next = iterator.next();
			if (next.getAccount().getID() == account.getID()
					&& DecimalUtil.isEquals(next.getAmount(), amount)) {
				log.info("Found the AT match for Purchase : " + next);
				next.setUpdateAccount(false);
				getSession().saveOrUpdate(next);
				iterator.remove();
				return next;
			}
		}
		return null;
	}

}
