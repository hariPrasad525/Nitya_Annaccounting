package com.nitya.accounter.core.migration;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.AccounterServerConstants;
import com.nitya.accounter.core.AccounterThreadLocal;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.Item;
import com.nitya.accounter.core.NumberUtils;
import com.nitya.accounter.utils.HibernateUtil;

public class InventoryMigration {

	Logger log = Logger.getLogger(InventoryMigration.class);

	@SuppressWarnings("unchecked")
	public void start() {
		Session session = HibernateUtil.getCurrentSession();

		// CREATING COST OF GOODS SOLD ACCOUNT
		List<Company> companies = session.getNamedQuery("getAllCompanies")
				.list();

		for (Company company : companies) {
			AccounterThreadLocal.set(company.getOpeningBalancesAccount()
					.getCreatedBy());
			log.info("Updating Company - " + company.getTradingName());
			String accountNumber = getNextAccountNumber(company.getID(),
					Account.SUBBASETYPE_COST_OF_GOODS_SOLD);
			Account account = new Account(Account.TYPE_COST_OF_GOODS_SOLD,
					accountNumber, AccounterServerConstants.COST_OF_GOODS_SOLD,
					Account.CASH_FLOW_CATEGORY_OPERATING);
			account.setCompany(company);
			session.save(account);
			company.setCostOfGoodsSold(account);
			session.saveOrUpdate(company);
		}

		// MIGRATING INVENTIRY
		Query query = session
				.getNamedQuery("get.InventoryItem.donnot.have.assetAccount");
		List<Item> list = query.list();
		for (Item inventoryItem : list) {
			try {
				if (inventoryItem.getExpenseAccount() == null) {
					inventoryItem.setExpenseAccount(inventoryItem.getCompany()
							.getCostOfGoodsSold());
				}

				Account assetsAccount = inventoryItem.getAssestsAccount();
				if (assetsAccount == null) {
					createAssetsAccount(session, inventoryItem);
					continue;
				}

				if (assetsAccount.getName().equals(
						AccounterServerConstants.ASSETS_INVENTORY)
						&& assetsAccount.getType() == Account.TYPE_OTHER_CURRENT_ASSET) {
					assetsAccount.setType(Account.TYPE_INVENTORY_ASSET);
					session.save(assetsAccount);
					continue;
				}

				query = session.getNamedQuery("get.AssetsAccountOfCompany")
						.setEntity("company", inventoryItem.getCompany());
				List<Account> assetsAccounts = query.list();

				if (assetsAccounts == null || assetsAccounts.isEmpty()) {
					createAssetsAccount(session, inventoryItem);
				} else {
					inventoryItem.setAssestsAccount(assetsAccounts.get(0));
				}
			} finally {
				session.saveOrUpdate(inventoryItem);
			}
		}

		// UPDATING INCOME EXPENSE ACCOUNTS
		query = session
				.getNamedQuery("get.InventoryItem.donnot.have.expenseAccount");
		List<Item> items = query.list();
		for (Item item : items) {
			try {
				if (item.getExpenseAccount() == null) {
					item.setExpenseAccount(item.getCompany()
							.getCostOfGoodsSold());
				}
			} finally {
				session.saveOrUpdate(item);
			}
		}

	}

	private void createAssetsAccount(Session session, Item inventoryItem) {
		AccounterThreadLocal.set(inventoryItem.getCreatedBy());
		Company company = inventoryItem.getCompany();

		String nextNum = getNextAccountNumber(company.getId(),
				Account.SUBBASETYPE_CURRENT_ASSET);
		log.info("Creating Account with number " + nextNum + " for Item '"
				+ inventoryItem.getName() + "' of Company '"
				+ company.getTradingName() + "'.");
		Account account = new Account(Account.TYPE_INVENTORY_ASSET, nextNum,
				AccounterServerConstants.ASSETS_INVENTORY,
				Account.CASH_FLOW_CATEGORY_OPERATING);
		account.setCompany(company);
		session.save(account);
		inventoryItem.setAssestsAccount(account);
	}

	private String getNextAccountNumber(long companyId, int subbaseType) {
		Session session = HibernateUtil.getCurrentSession();
		FlushMode flushMode = session.getFlushMode();
		session.setFlushMode(FlushMode.COMMIT);
		try {
			Query query = session.getNamedQuery("getNextAccountNumber")
					.setParameter("companyId", companyId)
					.setParameter("subbaseType", subbaseType);
			String maxNumber = (String) query.uniqueResult();
			return NumberUtils.getStringwithIncreamentedDigit(maxNumber);
		} finally {
			session.setFlushMode(flushMode);
		}
	}
}
