package com.nitya.accounter.web.client.core;

public class AccounterCommand implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int CREATION_SUCCESS = 1;

	public static final int UPDATION_SUCCESS = 2;

	public static final int DELETION_SUCCESS = 3;

	public int command;

	IAccounterCore data;

	public IAccounterCore getData() {
		return data;
	}

	public void setData(IAccounterCore data) {
		this.data = data;
	}

	long id;
	private AccounterCoreType type;

	private int version;

	@Override
	public String getDisplayName() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return this.type;
	}

	public void setObjectType(AccounterCoreType type) {
		this.type = type;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	/**
	 * @return the command
	 */
	public int getCommand() {
		return command;
	}

	/**
	 * @param command
	 *            the command to set
	 */
	public void setCommand(int command) {
		this.command = command;
	}

	public AccounterCommand clone() {
		AccounterCommand command = (AccounterCommand) this.clone();
		return command;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

}
