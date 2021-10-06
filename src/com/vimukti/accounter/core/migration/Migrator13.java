package com.vimukti.accounter.core.migration;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class Migrator13 extends AbstractMigrator {

	@Override
	public void migrate(Company company) throws AccounterException {
		log.info("Started Migrator13.");
		long cid = company.getID();

		getSession().getNamedQuery("set.current.balance")
				.setParameter("cid", cid).executeUpdate();
		getSession().getNamedQuery("set.total.balance.normal")
				.setParameter("cid", cid).executeUpdate();
		getSession().getNamedQuery("set.total.balance.2nd.stage")
				.setParameter("cid", cid).executeUpdate();
		getSession().getNamedQuery("set.total.balance.1st.stage")
				.setParameter("cid", cid).executeUpdate();
		getSession()
				.getNamedQuery("set.total.balance.in.account_currency.primary")
				.setParameter("cid", cid).executeUpdate();
		getSession()
				.getNamedQuery("set.total.balance.in.account_currency.other")
				.setParameter("cid", cid).executeUpdate();

		log.info("Finished Migrator13.");
	}

	@Override
	public int getVersion() {
		return 13;
	}

}
