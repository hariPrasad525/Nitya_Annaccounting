package com.vimukti.accounter.web.client.core;

public class ClientTransactionMakeDepositEntries implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int TYPE_FINANCIAL_ACCOUNT = 1;
	public static final int TYPE_VENDOR = 2;
	public static final int TYPE_CUSTOMER = 3;

	public long id;

	/**
	 * In this variable we store the type of the entry that was being created
	 * for this Class.
	 */
	int type;

	/**
	 * This reference to Transaction class is maintained to know what
	 * MakeDeposit results this TransactionMakeDepositEntries.
	 */
	private ClientTransaction transaction;

	// private PaymentMethod paymentMethod;
	//
	/**
	 * This Account reference is used to indicate the Bank account to where the
	 * Make Deposit total has to be stored.
	 */
	private ClientAccount account;
	//
	// private Vendor vendor;
	//
	// private Customer customer;
	//
	// private String reference;

	/**
	 * The amount with which this object to be created.
	 */
	private double amount;

	/**
	 * The amount which has to be paid still is maintained in this variable.
	 */
	private double balance;

	private int version;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public ClientTransaction getTransaction() {
		return transaction;
	}

	public void setTransaction(ClientTransaction transaction) {
		this.transaction = transaction;
	}

	public ClientAccount getAccount() {
		return account;
	}

	public void setAccount(ClientAccount account) {
		this.account = account;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}


	@Override
	public String getDisplayName() {
		// its not using any where
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.TRANSACTIONMAKEDEPOSITENTRIES;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	public ClientTransactionMakeDepositEntries clone() {
		ClientTransactionMakeDepositEntries clientTransactionMakeDepositClone = (ClientTransactionMakeDepositEntries) this
				.clone();

		return clientTransactionMakeDepositClone;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version=version;
	}
}
