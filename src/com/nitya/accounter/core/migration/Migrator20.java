package com.nitya.accounter.core.migration;

import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.CompanyPreferences;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.server.ItemUtils;

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
