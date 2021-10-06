package com.vimukti.accounter.core.migration;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;

import com.vimukti.accounter.core.AccountTransaction;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Transaction;

public class Migrator4 extends AbstractMigrator {

	Logger log = Logger.getLogger(Migrator4.class);

	@Override
	public void migrate(Company company) {
		log.info("Started Migrator4");

		for (Currency currency : company.getCurrencies()) {
			if (currency.getID() == company.getPrimaryCurrency().getID()) {
				currency.setAccountsReceivable(company
						.getAccountsReceivableAccount());
				currency.setAccountsPayable(company.getAccountsPayableAccount());
			} else {
				currency.createAccountsReveivablesAndPayables(getSession());
			}
			getSession().saveOrUpdate(currency);
		}

		Query query = getSession().getNamedQuery(
				"get.AccountTransactions.Of.AccountReceivables");
		query.setParameter("companyId", company.getID());
		List<AccountTransaction> list = query.list();
		for (AccountTransaction at : list) {
			Transaction transaction = at.getTransaction();
			Currency transactionCurrency = transaction.getCurrency();
			if (transactionCurrency.getID() != company.getPrimaryCurrency()
					.getID()) {
				at.getAccount().updateCurrentBalance(transaction,
						at.getAmount(), 1);
				transactionCurrency.getAccountsReceivable()
						.updateCurrentBalance(
								transaction,
								-(at.getAmount() / transaction
										.getCurrencyFactor()),
								transaction.getCurrencyFactor());
			}
		}

		query = getSession().getNamedQuery(
				"get.AccountTransactions.Of.AccountPayables");
		query.setParameter("companyId", company.getID());
		list = query.list();
		for (AccountTransaction at : list) {
			Transaction transaction = at.getTransaction();
			Currency transactionCurrency = transaction.getCurrency();
			if (transactionCurrency.getID() != company.getPrimaryCurrency()
					.getID()) {
				at.getAccount().updateCurrentBalance(transaction,
						-at.getAmount(), 1);
				transactionCurrency.getAccountsPayable().updateCurrentBalance(
						transaction,
						(at.getAmount() / transaction.getCurrencyFactor()),
						transaction.getCurrencyFactor());
			}
		}
		log.info("Fininshed Migrator4");
	}

	@Override
	public int getVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

}
