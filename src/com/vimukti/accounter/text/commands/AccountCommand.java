package com.vimukti.accounter.text.commands;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.BankAccount;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.utils.HibernateUtil;

/**
 * This is only for BankAccount. name, openingBalance, asOf
 * 
 * @author Umasree
 * 
 */
public class AccountCommand extends CreateOrUpdateCommand {

	private String name;
	private Double openingBal;
	private FinanceDate asOf;

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {
		name = data.nextString("");
		if (!data.isDouble()) {
			respnse.addError("Invalid Double for Opening Balance");
			return false;
		}
		openingBal = data.nextDouble(0);
		if (!data.isDate()) {
			respnse.addError("Invalid Date format for As Of Date");
			return false;
		}
		asOf = data.nextDate(new FinanceDate());

		return true;
	}

	@Override
	public void process(ITextResponse respnse) {
		Session session = HibernateUtil.getCurrentSession();
		Criteria query = session.createCriteria(BankAccount.class);
		query.add(Restrictions.eq("company", getCompany()));
		query.add(Restrictions.eq("name", name));
		BankAccount account = (BankAccount) query.uniqueResult();
		if (account == null) {
			account = new BankAccount();
		}
		account.setBankAccountType(Account.TYPE_BANK);
		account.setName(name);
		if (openingBal != null) {
			account.setOpeningBalance(openingBal);
		}
		if (asOf != null) {
			account.setAsOf(asOf);
		}

		session.save(account);
	}

}
