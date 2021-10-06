package com.vimukti.accounter.web.client.core.reports;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

/**
 * @author vimukti16
 * 
 */
public class AccountBalance implements IsSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String name;
	double balance;
	int type;

	public AccountBalance(String name, double balance, int type) {

		this.name = name;
		this.balance = balance;
		this.type = type;

	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the balance
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * @param balance
	 *            the balance to set
	 */
	public void setBalance(double balance) {
		this.balance = balance;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	public boolean equals(AccountBalance ab) {

		if (this.name.equals(ab.name)
				&& DecimalUtil.isEquals(this.balance, ab.balance)
				&& this.type == ab.type)
			return true;
		return false;

	}
}
