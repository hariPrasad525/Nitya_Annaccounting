package com.vimukti.accounter.core.migration;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccounterServerConstants;
import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.Company;

public class Migrator8 extends AbstractMigrator {

	@Override
	public void migrate(Company company) {
		log.info("Started Migrator8.");
		AccounterThreadLocal.set(company.getOpeningBalancesAccount()
				.getCreatedBy());
		String accountNumber = getNextAccountNumber(company.getID(),
				Account.TYPE_OTHER_CURRENT_LIABILITY);
		Account account = new Account(Account.TYPE_OTHER_CURRENT_LIABILITY,
				accountNumber, AccounterServerConstants.SALARIES_PAYABLE,
				Account.CASH_FLOW_CATEGORY_OPERATING);
		account.setCompany(company);
		getSession().save(account);
		company.setSalariesPaybleAccount(account);
		log.info("Finished Migrator8");
	}

	@Override
	public int getVersion() {
		return 8;
	}

}
