package com.vimukti.accounter.core.migration;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class Migrator21 extends AbstractMigrator {

	@Override
	public void migrate(Company company) throws AccounterException {
		log.info("Started Migrator" + getVersion());
		for (Transaction t : company.getTransactions()) {
			// (201,202,204)
			int status = t.getSaveStatus();
			if (status == 201 || status == 202 || status == 204) {
				continue;
			}
			migrate(t);
		}
		log.info("Finished Migrator" + getVersion());
	}

	@Override
	public int getVersion() {
		return 21;
	}

}
