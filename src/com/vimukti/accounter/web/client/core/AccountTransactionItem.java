package com.vimukti.accounter.web.client.core;

public class AccountTransactionItem {

	long id;
	ClientTransaction transaction;
	ClientAccount account;
	double amount;

	public AccountTransactionItem(ClientAccount account, ClientTransaction transaction,
			double amount) {

		this.account = account;
		this.transaction = transaction;
		this.amount = amount;

	}

}
