package com.vimukti.accounter.core.migration;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.ItemUtils;

public class Migrator12 extends AbstractMigrator {

	@Override
	public void migrate(Company company) throws AccounterException {
		log.info("Started Migrator12.");

		ItemUtils.remapAllInventory(company);

		log.info("Finished Migrator12.");
	}

	@Override
	public int getVersion() {
		return 12;
	}
}
