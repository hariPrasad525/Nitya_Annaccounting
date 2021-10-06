package com.nitya.accounter.core.migration;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.AccounterServerConstants;
import com.nitya.accounter.core.AccounterThreadLocal;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.Item;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.server.ItemUtils;

/**
 * This Migration will Create 'Cost of Goods Sold' account for Company.
 * 
 * Updates Inventory Items that don't have Assets/Expense accounts with
 * new/default Assets/Expense accounts
 * 
 * @author Prasanna Kumar G
 * 
 */
public class Migrator1 extends AbstractMigrator {

	@Override
	public void migrate(Company company) throws AccounterException {
		log.info("Started Migrator1");
		Session session = getSession();

		// CREATING COST OF GOODS SOLD ACCOUNT
		if (company.getCostOfGoodsSold() == null) {
			AccounterThreadLocal.set(company.getOpeningBalancesAccount()
					.getCreatedBy());
			String accountNumber = getNextAccountNumber(company.getID(),
					Account.TYPE_COST_OF_GOODS_SOLD);
			Account account = new Account(Account.TYPE_COST_OF_GOODS_SOLD,
					accountNumber, AccounterServerConstants.COST_OF_GOODS_SOLD,
					Account.CASH_FLOW_CATEGORY_OPERATING);
			account.setCompany(company);
			session.save(account);
			company.setCostOfGoodsSold(account);
		}

		// MIGRATING INVENTIRY
		Query query = session.getNamedQuery("get.All.InventoryItem")
				.setParameter("companyId", company.getId());
		List<Item> list = query.list();
		for (Item inventoryItem : list) {
			try {
				if (inventoryItem.getExpenseAccount() == null) {
					inventoryItem.setExpenseAccount(company
							.getCostOfGoodsSold());
				}

				Account assetsAccount = inventoryItem.getAssestsAccount();
				if (assetsAccount == null) {
					createAssetsAccount(inventoryItem);
					continue;
				}

				if (assetsAccount.getName().equals(
						AccounterServerConstants.ASSETS_INVENTORY)
						&& assetsAccount.getType() == Account.TYPE_OTHER_CURRENT_ASSET) {
					assetsAccount.setType(Account.TYPE_INVENTORY_ASSET);
					session.save(assetsAccount);
					continue;
				}

				if (assetsAccount.getType() != Account.TYPE_INVENTORY_ASSET) {
					query = session.getNamedQuery("get.AssetsAccountOfCompany")
							.setParameter("companyId", company.getId());
					List<Account> assetsAccounts = query.list();

					if (assetsAccounts == null || assetsAccounts.isEmpty()) {
						createAssetsAccount(inventoryItem);
					} else {
						inventoryItem.setAssestsAccount(assetsAccounts.get(0));
					}
				}
			} finally {
				session.saveOrUpdate(inventoryItem);
			}
		}

		ItemUtils.remapSalesPurchases(list);
		log.info("Finished Migrator1");
	}

	private void createAssetsAccount(Item inventoryItem) {
		AccounterThreadLocal.set(inventoryItem.getCreatedBy());
		Company company = inventoryItem.getCompany();

		String nextNum = getNextAccountNumber(company.getId(),
				Account.TYPE_INVENTORY_ASSET);
		Account account = new Account(Account.TYPE_INVENTORY_ASSET, nextNum,
				AccounterServerConstants.ASSETS_INVENTORY,
				Account.CASH_FLOW_CATEGORY_OPERATING);
		account.setCompany(company);
		getSession().save(account);
		inventoryItem.setAssestsAccount(account);
	}

	@Override
	public int getVersion() {
		return 1;
	}

}
