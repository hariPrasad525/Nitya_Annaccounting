package com.vimukti.accounter.web.client.core;

/**
 * 
 * @author vimukti16 <br/>
 * <br/>
 * 
 *         This can store the Back up of each Item and it's internal linked
 *         Accounts which are participated in any Transaction.
 * 
 */

public class ClientItemBackUp implements IAccounterCore {

	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;

	public long id;

	long item;

	long incomeAccount;

	long expenseAccount;

	private int version;

	public ClientItemBackUp() {
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	public long getItem() {
		return item;
	}

	public void setItem(long item) {
		this.item = item;
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
		return AccounterCoreType.ITEMBACKUP;
	}

	public ClientItemBackUp clone() {
		ClientItemBackUp itemBackUp = (ClientItemBackUp) this.clone();
		return itemBackUp;

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
