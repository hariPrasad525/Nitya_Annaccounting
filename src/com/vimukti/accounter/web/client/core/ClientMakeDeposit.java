package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

public class ClientMakeDeposit extends ClientTransaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long depositTo;

	private List<ClientTransactionDepositItem> transactionDepositItems = new ArrayList<ClientTransactionDepositItem>();

	public long getDepositTo() {
		return depositTo;
	}

	public void setDepositTo(long depositTo) {
		this.depositTo = depositTo;
	}

	public List<ClientTransactionDepositItem> getTransactionDepositItems() {
		return transactionDepositItems;
	}

	public void setTransactionDepositItems(
			List<ClientTransactionDepositItem> transactionDepositItems) {
		this.transactionDepositItems = transactionDepositItems;
		if (transactionDepositItems == null)
			return;

		for (ClientTransactionDepositItem transactionItem : transactionDepositItems) {
			transactionItem.setTransaction(this);
		}
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.MAKEDEPOSIT;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

}
