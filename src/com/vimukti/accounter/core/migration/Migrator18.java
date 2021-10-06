package com.vimukti.accounter.core.migration;

import java.util.List;

import org.hibernate.Query;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class Migrator18 extends AbstractMigrator {

	@SuppressWarnings("unchecked")
	@Override
	public void migrate(Company company) throws AccounterException {
		log.info("Started Migrator18");
		Query query = getSession().getNamedQuery("get.Incorrect.Accounts")
				.setParameter("cid", company.getId());
		List<Object[]> list = query.list();
		if (list.isEmpty()) {
			return;
		}
		for (Object[] result : list) {
			long accountId = (Long) result[0];
			double atSum = (Double) result[1];
			double currentBalance = (Double) result[2];
			Account account = (Account) getSession().get(Account.class,
					accountId);
			if (account == null) {
				continue;
			}
			account.effectCurrentBalance(atSum - currentBalance, 1);
			getSession().saveOrUpdate(account);
		}
		log.info("Finished Migrator18");
	}

	@Override
	public int getVersion() {
		return 18;
	}

}
