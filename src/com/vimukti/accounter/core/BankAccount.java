package com.vimukti.accounter.core;

import org.hibernate.CallbackException;
import org.hibernate.Session;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class BankAccount extends Account {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Bank bank;

	private int bankAccountType;

	private String bankAccountNumber;

	private FinanceDate lastReconcilationDate;

	/**
	 * Creates new Instance
	 */
	public BankAccount() {
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
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

	public FinanceDate getLastReconcilationDate() {
		return lastReconcilationDate;
	}

	public void setLastReconcilationDate(FinanceDate lastReconcilationDate) {
		this.lastReconcilationDate = lastReconcilationDate;
	}

	@Override
	public int getObjType() {
		return IAccounterCore.BANK_ACCOUNT;
	}

	@Override
	protected void checkNullValues() throws AccounterException {
		super.checkNullValues();
		if (type == Account.TYPE_BANK) {
			if (bankAccountType <= 0) {
				throw new AccounterException(
						AccounterException.ERROR_OBJECT_NULL, Global.get()
								.messages().bankAccountType());
			}
		}
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		session.getNamedQuery("deleteBankChequeLayouts")
				.setParameter("account", this).executeUpdate();
		return super.onDelete(session);
	}

	@Override
	public void selfValidate() throws AccounterException {
		super.selfValidate();

		if (type == Account.TYPE_BANK) {
			if (bankAccountType <= 0) {
				throw new AccounterException("Bank type should not be null");
			}
			if (Integer.parseInt(getNumber()) < 1100
					&& Integer.parseInt(getNumber()) > 1179) {
				throw new AccounterException(
						"Bank accounter number should in between 1100 and 1179");
			}
		}

	}
}
