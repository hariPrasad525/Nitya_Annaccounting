package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

public class ClientPayrollPayTax extends ClientTransaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private long taxAgency;
	private long payAccount;
	private List<ClientPayrollTransactionPayTax> transactionPayTax;

	

	public ClientPayrollPayTax() {
		super();
		setType(TYPE_PAY_TAX);
		setTransactionPayTax(new ArrayList<ClientPayrollTransactionPayTax>());
	}

	@Override
	public String getName() {
		return "PayTax";
	}

	@Override
	public String getDisplayName() {
		return "PayTax";
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.TYPE_PAYTAX;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return id;
	}
	
	public long getTaxAgency() {
		return taxAgency;
	}

	public void setTaxAgency(long taxAgency) {
		this.taxAgency = taxAgency;
	}


	public long getPayAccount() {
		return payAccount;
	}

	public void setPayAccount(long payAccount) {
		this.payAccount = payAccount;
	}

	public List<ClientPayrollTransactionPayTax> getTransactionPayTax() {
		return transactionPayTax;
	}

	public void setTransactionPayTax(
			List<ClientPayrollTransactionPayTax> transactionPayTax) {
		this.transactionPayTax = transactionPayTax;
	}
}



