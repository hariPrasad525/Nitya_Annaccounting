package com.nitya.accounter.core.migration;

import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.Transaction;
import com.nitya.accounter.web.client.exception.AccounterException;

public class Migrator16 extends AbstractMigrator {

	@Override
	public void migrate(Company company) throws AccounterException {
		log.info("Started Migrator16");
		for (Transaction t : company.getTransactions()) {
			// (201,202,204)
			int status = t.getSaveStatus();
			if (status == 201 || status == 202 || status == 204) {
				continue;
			}
			migrate(t);
		}
		log.info("Finished Migrator16");
	}

	@Override
	public int getVersion() {
		return 16;
	}

}
