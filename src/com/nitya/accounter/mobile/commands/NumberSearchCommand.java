package com.nitya.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.Customer;
import com.nitya.accounter.core.Transaction;
import com.nitya.accounter.core.Vendor;
import com.nitya.accounter.mobile.Command;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;

public class NumberSearchCommand extends Command {

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		// TODO Auto-generated method stub

	}

	@Override
	public Result run(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<Transaction> getTransactionsWithNumber(Session session,
			String number) {
		Query query = session.getNamedQuery("getTransaction.with.number");
		query.setParameter("number", number);
		return query.list();

	}

	private List<Customer> getCustomersWithNumber(Session session, String number) {
		// TODO
		return null;

	}

	private List<Vendor> getVendorsWithNumber(Session session, String number) {
		// TODO
		return null;

	}

	private List<Account> getAccountsWithNumber(Session session, String number) {
		// TODO
		return null;

	}

}
