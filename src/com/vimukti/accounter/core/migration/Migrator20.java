package com.vimukti.accounter.core.migration;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CompanyPreferences;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.ItemUtils;

public class Migrator20 extends AbstractMigrator {

	@Override
	public void migrate(Company company) throws AccounterException {
		log.info("Started Migrator" + getVersion());

		if (company.getPreferences().getActiveInventoryScheme() != CompanyPreferences.INVENTORY_SCHME_AVERAGE) {
			ItemUtils.remapAllInventory(company);
		}

		log.info("Finished Migrator" + getVersion());

	}

	@Override
	public int getVersion() {
		return 20;
	}

}
