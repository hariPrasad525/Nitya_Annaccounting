package com.nitya.accounter.core.migration;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.AccounterServerConstants;
import com.nitya.accounter.core.AccounterThreadLocal;
import com.nitya.accounter.core.Company;

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
