package com.vimukti.accounter.core.migration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class Migrator22 extends AbstractMigrator {

	@SuppressWarnings("unchecked")
	@Override
	public void migrate(Company company) throws AccounterException {
		log.info("Started Migrator22");

		Query query = getSession().getNamedQuery("getDefaultAccounts")
				.setEntity("company", company);
		List<Account> list = query.list();

		List<Account> defaultAccounts = getDefaultAccounts(company);
		for (Account acc : list) {
			if (!defaultAccounts.contains(acc)) {
				acc.setDefault(false);
				getSession().saveOrUpdate(acc);
			}

		}

		log.info("Finished Migrator22");
	}

	@SuppressWarnings("unchecked")
	private List<Account> getDefaultAccounts(Company company) {
		Account[] defaults = { company.getAccountsPayableAccount(),
				company.getAccountsReceivableAccount(),
				company.getExchangeLossOrGainAccount(),
				company.getOpeningBalancesAccount(),
				company.getCostOfGoodsSold(), company.getCashDiscountsGiven(),
				company.getCashDiscountsGiven(),
				company.getSalariesPayableAccount(),
				company.getTAXFiledLiabilityAccount(),
				company.getTaxLiabilityAccount() };

		List<Account> defaultAccounts = new ArrayList<Account>(
				Arrays.asList(defaults));

		List<Account> list = getSession()
				.getNamedQuery("get.AssetsAccountOfCompany")
				.setParameter("companyId", company.getId()).list();

		if (!list.isEmpty()) {
			defaultAccounts.add(list.get(0));
		}

		return defaultAccounts;
	}

	@Override
	public int getVersion() {
		return 22;
	}

}
