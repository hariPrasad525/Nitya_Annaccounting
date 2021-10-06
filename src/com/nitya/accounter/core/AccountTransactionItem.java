package com.nitya.accounter.core;


public class AccountTransactionItem {

	long id;
	Transaction transaction;
	Account account;
	double amount;

	public AccountTransactionItem(Account account, Transaction transaction,
			double amount) {

		this.account = account;
		this.transaction = transaction;
		this.amount = amount;

	}

}
