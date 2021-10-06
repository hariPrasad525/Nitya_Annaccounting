package com.nitya.accounter.web.client.core;

public class ClientTransactionLog implements IAccounterCore {

	public static final int TYPE_NOTE = 4;
	public static final int TYPE_EDIT = 2;
	public static final int TYPE_CREATE = 1;
	public static final int TYPE_VOID = 3;

	private int type;
	private String description;

	private ClientUser user;
	private long time;
	private String userName;

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return "ClientTransactionLog";
	}

	@Override
	public long getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.TRANSACTION_LOG;
	}

	@Override
	public void setID(long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setVersion(int version) {
		// TODO Auto-generated method stub

	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setUser(ClientUser user) {
		this.user = user;
	}

	public ClientUser getUser() {
		return user;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getTime() {
		return time;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

}
