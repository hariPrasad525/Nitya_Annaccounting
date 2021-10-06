package com.vimukti.accounter.admin.core;

import java.util.List;

import org.json.JSONException;

import com.vimukti.accounter.core.AuditWriter;
import com.vimukti.accounter.core.IAccounterServerCore;
import com.vimukti.accounter.core.INamedObject;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public class Admin implements IAccounterServerCore, INamedObject {

	private static final long serialVersionUID = 1L;

	private long id;

	private String name;

	private List<AdminUser> adminUsers;

	private String emailID;

	/**
	 * @return the id
	 */
	public void setID(long id) {
		this.id = id;
	}

	public long getID() {
		return id;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public void setVersion(int version) {

	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setAdminUsers(List<AdminUser> adminUsers) {
		this.adminUsers = adminUsers;
	}

	public List<AdminUser> getAdminUsers() {
		return adminUsers;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public String getEmailID() {
		return emailID;
	}

	@Override
	public int getObjType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.admin()).gap();

		w.put(messages.name(), this.name).gap();

		w.put(messages.email(), this.emailID);
	}

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub

	}

}
