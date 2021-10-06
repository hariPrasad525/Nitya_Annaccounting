package com.nitya.accounter.core.migration;

import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.Payee;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;

public class Migrator24 extends AbstractMigrator {

	@Override
	public void migrate(Company company) throws AccounterException {
		log.info("Started Migrator24");
		for (Payee payee : company.getPayees()) {

			Double pBalance = (Double) getSession()
					.getNamedQuery("get.Payee.Balance")
					.setParameter("payeeId", payee.getID()).uniqueResult();
			if (pBalance == null || DecimalUtil.isEquals(pBalance, 0.00D)
					|| DecimalUtil.isEquals(pBalance, -0.00D)) {
				pBalance = 0.00D;
			}
			if (payee.isCustomer() && !DecimalUtil.isEquals(pBalance, 0.00D)) {
				pBalance = -pBalance;
			}
			if (DecimalUtil.isEquals(payee.getBalance(), pBalance)) {
				continue;
			}
			log.info("***Updating Payee Balance: " + payee.getID() + " Name: "
					+ payee.getName() + " with " + pBalance);
			payee.setBalance(pBalance);
			getSession().saveOrUpdate(payee);
		}
		log.info("Finished Migrator24");
	}

	@Override
	public int getVersion() {
		return 24;
	}

}
