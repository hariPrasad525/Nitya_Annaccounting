package com.nitya.accounter.core.migration;

import java.util.List;

import org.hibernate.Query;

import com.nitya.accounter.core.AccountTransaction;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.Item;
import com.nitya.accounter.core.Transaction;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.server.ItemUtils;

public class Migrator3 extends AbstractMigrator {

	@Override
	public void migrate(Company company) throws AccounterException {
		Query query = getSession().getNamedQuery("get.All.InventoryItem")
				.setParameter("companyId", company.getId());
		List<Item> items = query.list();
		for (Item item : items) {
			int type = item.getType();
			if (type != Item.TYPE_INVENTORY_PART
					&& type != Item.TYPE_INVENTORY_ASSEMBLY) {
				continue;
			}

			query = getSession()
					.getNamedQuery("get.AccountTransaction.Of.Item")
					.setParameter("itemId", item.getID());
			List<AccountTransaction> list = query.list();
			for (AccountTransaction at : list) {
				Transaction transaction = at.getTransaction();
				transaction.getAccountTransactionEntriesList().remove(at);
				getSession().saveOrUpdate(transaction);
				getSession().saveOrUpdate(at);
			}
		}

		// Deleting Purchases
		getSession().getNamedQuery("delete.InventoryPurchases.Of.Company")
				.setParameter("companyId", company.getId()).executeUpdate();

		ItemUtils.remapSalesPurchases(items);
	}

	@Override
	public int getVersion() {
		return 3;
	}

}
