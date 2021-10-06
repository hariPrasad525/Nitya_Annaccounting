package com.vimukti.accounter.admin.client;

import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.IAccounterCore;

public class ClientAdminUser implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5387196197710854416L;

	private String name;

	private String emailId;

	private String password;

	private int typeOfUser;

	private String status;

	boolean isPermissionsGiven;

	long id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getTypeOfUser() {
		return typeOfUser;
	}

	public void setTypeOfUser(int typeOfUser) {
		this.typeOfUser = typeOfUser;
	}

	public boolean isPermission() {
		return isPermissionsGiven;
	}

	public void setPermission(boolean permission) {
		this.isPermissionsGiven = permission;
	}


	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.ADMIN_USER;
	}

	@Override
	public void setID(long id) {
		this.id = id;

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

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

}
