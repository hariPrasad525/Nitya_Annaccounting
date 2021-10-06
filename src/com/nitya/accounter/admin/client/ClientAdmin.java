package com.nitya.accounter.admin.client;

import java.util.List;

import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.IAccounterCore;

public class ClientAdmin implements IAccounterCore {

	private long id;

	private String name;

	private List<ClientAdminUser> clientAdminUsers;

	private String emailID;

	/**
	 * 
	 */
	private static final long serialVersionUID = 7573080843742411106L;


	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getID() {
		return id;
	}

	public void setID(long id) {
		this.id = id;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return null;
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

	public void setName(String name) {
		this.name = name;
	}

	public void setClientAdminUsers(List<ClientAdminUser> clientAdminUsers) {
		this.clientAdminUsers = clientAdminUsers;
	}

	public List<ClientAdminUser> getClientAdminUsers() {
		return clientAdminUsers;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public String getEmailID() {
		return emailID;
	}

}
