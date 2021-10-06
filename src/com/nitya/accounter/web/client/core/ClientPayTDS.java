package com.nitya.accounter.web.client.core;

import com.nitya.accounter.web.client.Global;

public class ClientPayTDS extends ClientTransaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setID(long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		return Global.get().messages().payTDS();
	}

	@Override
	public String getDisplayName() {
		return getName();
	}

}
