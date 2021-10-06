package com.vimukti.accounter.core.migration;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.server.ItemUtils;

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
