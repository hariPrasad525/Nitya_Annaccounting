package com.vimukti.accounter.web.client.core;


public class ClientUserPreferences implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int version;

	public ClientUserPreferences() {

	}

	/**
	 * 
	 * @param dashBoardPreferences
	 */

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return AccounterCoreType.USER_PREFERENCES;
	}

	@Override
	public long getID() {
		return 0;
	}

	@Override
	public void setID(long id) {
		// this.id=id;

	}


	public ClientUserPreferences clone() {
		ClientUserPreferences clientUserPreferencesClone = (ClientUserPreferences) this
				.clone();
		return clientUserPreferencesClone;
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
