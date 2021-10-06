package com.vimukti.accounter.core.migration;

import java.util.List;

import org.hibernate.Query;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.Quantity;

public class Migrator10 extends AbstractMigrator {

	@Override
	public void migrate(Company company) {
		Query query = getSession().getNamedQuery(
				"getInventoryOfAverageCostNegative").setParameter("company",
				company);
		List<Item> list = query.list();
		for (Item item : list) {
			double averageCost = item.getAverageCost();
			Quantity onhandQty = item.getOnhandQty();
			double onHandCost = onhandQty.calculatePrice(averageCost);
			if (!onhandQty.isEmpty()) {
				item.setAverageCost(Math.abs(onHandCost / onhandQty.getValue()));
			}
			getSession().saveOrUpdate(item);
		}
	}

	@Override
	public int getVersion() {
		return 10;
	}

}
