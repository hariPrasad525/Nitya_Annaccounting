package com.nitya.accounter.web.client.core;

public class ClientBankAccount extends ClientAccount {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long bank;

	private int bankAccountType;

	private String bankAccountNumber;

	private long lastReconcilationDate;

	/**
	 * Creates new Instance
	 */
	public ClientBankAccount() {
		this.type = TYPE_BANK;
	}

	public ClientBankAccount(long currency) {
		super(currency);
		this.type = TYPE_BANK;
	}

	public long getBank() {
		return bank;
	}

	public void setBank(long bank) {
		this.bank = bank;
	}

	public int getBankAccountType() {
		return bankAccountType;
	}

	public void setBankAccountType(int bankAccountType) {
		this.bankAccountType = bankAccountType;
	}

	public String getBankAccountNumber() {
		return bankAccountNumber;
	}

	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}

	public long getLastReconcilationDate() {
		return lastReconcilationDate;
	}

	public void setLastReconcilationDate(long lastReconcilationDate) {
		this.lastReconcilationDate = lastReconcilationDate;
	}

	@Override
	public ClientBankAccount clone() {
		ClientBankAccount clientBankAccount = this.clone();
		return clientBankAccount;

	}

	public AccounterCoreType getObjectType() {
		return AccounterCoreType.BANK_ACCOUNT;
	};
}
