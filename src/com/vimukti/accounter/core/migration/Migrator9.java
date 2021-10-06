package com.vimukti.accounter.core.migration;

import java.util.List;

import org.hibernate.Query;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.web.server.ItemUtils;

/**
 * Will Set the average cost to the Items which Zero average cost
 * 
 * @author vimutki35
 * 
 */
public class Migrator9 extends AbstractMigrator {

	@Override
	public void migrate(Company company) {
		log.info("Started Migrator9.");
		Query query = getSession().getNamedQuery("getItemsOfAverageCostZero")
				.setEntity("company", company);
		List<Item> items = query.list();
		for (Item item : items) {
			double averageCost = ItemUtils.getAverageCost(item);
			item.setAverageCost(averageCost);
			getSession().saveOrUpdate(item);
		}
		log.info("Finished Migrator9.");
	}

	@Override
	public int getVersion() {
		return 9;
	}

}
