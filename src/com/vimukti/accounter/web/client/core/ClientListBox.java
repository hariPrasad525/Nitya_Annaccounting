package com.vimukti.accounter.web.client.core;

public class ClientListBox implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String value;

	private Integer key;

	public ClientListBox() {
	}
	
	public ClientListBox(String value, Integer key) {
		 this.value = value;
		 this.key = key;
	}

	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public void setVersion(int version) {
	}

	@Override
	public String getName() {
		return value;
	}

	@Override
	public String getDisplayName() {
		return this.value;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return null;
	}

	@Override
	public void setID(long id) {
		key = (int) id;
	}

	@Override
	public long getID() {
		return key;
	}

}
