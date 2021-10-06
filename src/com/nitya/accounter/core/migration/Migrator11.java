package com.nitya.accounter.core.migration;

import java.util.List;

import org.hibernate.Session;

import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.Transaction;
import com.nitya.accounter.utils.HibernateUtil;

public class Migrator11 extends AbstractMigrator {

	@Override
	public void migrate(Company company) {

		log.info("Started Migrator11.");
		Session session = HibernateUtil.getCurrentSession();
		List<Long> transactionIds = getAllTransactions(company);
		for (Long id : transactionIds) {
			try {
				Transaction transaction = (Transaction) session.load(
						Transaction.class, id);
				transaction = HibernateUtil.initializeAndUnproxy(transaction);
				migrate(transaction);
			} catch (Exception e) {
				throw new RuntimeException();
			}
		}
		log.info("Finished Migrator11.");
	}

	// /**
	// * It will check the Suitable AccountTrannsaction for this Purchases and
	// * removes it from List
	// *
	// * @param oldATs
	// * @param purchases
	// * @param assetAccount
	// * @param missingAts
	// * @return
	// */
	// private double matchATandIP(Set<AccountTransaction> oldATs,
	// Set<InventoryPurchase> purchases, Account assetAccount,
	// Set<AccountTransaction> missingAts) {
	// if (purchases.isEmpty()) {
	// return 0.0D;
	// }
	// double assetChange = 0.00D;
	// for (InventoryPurchase purchase : purchases) {
	// double purchaseValue = purchase.getQuantity().calculatePrice(
	// purchase.getCost());
	// assetChange += purchaseValue;
	// Account effectingAccount = purchase.getEffectingAccount();
	// purchaseValue = (effectingAccount.isIncrease() ? 1 : -1)
	// * purchaseValue;
	// checkAndRemoveAT(oldATs, purchase.getEffectingAccount(),
	// -purchaseValue, missingAts);
	// }
	// assetChange = (assetAccount.isIncrease() ? 1 : -1) * assetChange;
	// checkAndRemoveAT(oldATs, assetAccount, assetChange, missingAts);
	// return assetChange;
	// }

	// /**
	// * It will check the AccountTransaction with same account and amount. If
	// * exists removes that from list and Make the VAR 'updateAccount' false;
	// *
	// * @param ats
	// * @param account
	// * @param amount
	// * @param missingAts
	// * @return
	// */
	// private AccountTransaction checkAndRemoveAT(Set<AccountTransaction> ats,
	// Account account, double amount, Set<AccountTransaction> missingAts) {
	// Iterator<AccountTransaction> iterator = ats.iterator();
	// while (iterator.hasNext()) {
	// AccountTransaction next = iterator.next();
	// if (next.getAccount().getID() == account.getID()
	// && DecimalUtil.isEquals(next.getAmount(), amount)) {
	// log.info("Found the AT match for Purchase : " + next);
	// iterator.remove();
	// next.setUpdateAccount(true);
	// getSession().saveOrUpdate(next);
	// return next;
	// }
	// }
	// return null;
	// }

	private List<Long> getAllTransactions(Company company) {
		Session session = HibernateUtil.getCurrentSession();
		List<Long> list = session
				.getNamedQuery("get.all.inventory.items.transactions")
				.setLong("companyId", company.getId()).list();
		return list;
	}

	@Override
	public int getVersion() {
		return 11;
	}

}
