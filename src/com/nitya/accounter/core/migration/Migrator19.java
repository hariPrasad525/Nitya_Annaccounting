package com.nitya.accounter.core.migration;

import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.Item;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.server.ItemUtils;

public class Migrator19 extends AbstractMigrator {

	@Override
	public void migrate(Company company) throws AccounterException {
		log.info("Started Migrator19");

		for (Item item : company.getItems()) {
			int type = item.getType();
			if (type != Item.TYPE_INVENTORY_PART
					&& type != Item.TYPE_INVENTORY_ASSEMBLY) {
				continue;
			}
			double averageCost = ItemUtils.getAverageCost(item);
			if (DecimalUtil.isEquals(averageCost, item.getAverageCost())) {
				continue;
			}
			item.setAverageCost(averageCost);
			getSession().saveOrUpdate(item);
		}

		log.info("Finished Migrator19");
	}

	@Override
	public int getVersion() {
		return 19;
	}

}
