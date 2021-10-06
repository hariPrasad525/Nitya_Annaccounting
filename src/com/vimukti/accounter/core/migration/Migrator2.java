package com.vimukti.accounter.core.migration;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.utils.HibernateUtil;

public class Migrator2 extends AbstractMigrator {

	@Override
	public void migrate(Company company) {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session
				.getNamedQuery("get.Account.by.Name")
				.setParameter("companyId", company.getId())
				.setParameter("accountName",
						company.getCostOfGoodsSold().getName());
		List<Account> list = query.list();
		if (list.size() <= 1) {
			return;
		}

		Iterator<Account> iterator = list.iterator();

		while (iterator.hasNext()) {
			Account account = iterator.next();
			if (account.getID() != company.getCostOfGoodsSold().getID()) {
				query = session.getNamedQuery("getAccountTransactions")
						.setParameter("companyId", company.getId())
						.setParameter("accountId", account.getID());
				List ats = query.list();
				if (ats.isEmpty()) {
					session.delete(account);
				} else {
					Account costOfGoodsSold = company.getCostOfGoodsSold();
					company.setCostOfGoodsSold(account);
					session.delete(costOfGoodsSold);
				}
				break;
			}
		}

	}

	@Override
	public int getVersion() {
		return 2;
	}

}
