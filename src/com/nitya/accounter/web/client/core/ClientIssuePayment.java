package com.nitya.accounter.web.client.core;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.nitya.accounter.web.client.ui.Accounter;

public class ClientIssuePayment extends ClientTransaction implements
		IsSerializable, Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long account;

	String checkNumber;

	/**
	 * @return the account
	 */
	public long getAccount() {
		return account;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(long account) {
		this.account = account;
	}

	/**
	 * @return the checkNumber
	 */
	public String getCheckNumber() {
		return checkNumber;
	}

	/**
	 * @param checkNumber
	 *            the checkNumber to set
	 */
	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	@Override
	public String getDisplayName() {
		return null;
	}

	@Override
	public String getName() {
		return Accounter.getCompany().getName();
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.ISSUEPAYMENT;
	}

	public ClientIssuePayment clone() {
		ClientIssuePayment clientIssuePaymentClone = (ClientIssuePayment) this
				.clone();
		return clientIssuePaymentClone;
	}
}
