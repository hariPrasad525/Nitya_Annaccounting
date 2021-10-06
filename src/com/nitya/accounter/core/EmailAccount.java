package com.nitya.accounter.core;

import org.json.JSONException;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.externalization.AccounterMessages;

public class EmailAccount extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3140130664346058589L;

	private String emailId;
	private String password;
	private String smtpMailServer;
	private int portNumber;
	private boolean isSSL;

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

	public String getSmtpMailServer() {
		return smtpMailServer;
	}

	public void setSmtpMailServer(String smtpMailServer) {
		this.smtpMailServer = smtpMailServer;
	}

	public int getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	public boolean isSSL() {
		return isSSL;
	}

	public void setSSL(boolean isSSL) {
		this.isSSL = isSSL;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();
		w.put(messages.email(), this.emailId);

		w.put(messages.portNumber(), this.portNumber);

		w.put(messages.smtpMailServer(), this.smtpMailServer);

	}

	@Override
	public String getName() {
		return getEmailId();
	}

	@Override
	public void setName(String name) {
	}

	@Override
	public int getObjType() {
		return IAccounterCore.EMAIL_ACCOUNT;
	}

	@Override
	public void selfValidate() throws AccounterException {
		if (this.emailId == null || this.emailId.trim().isEmpty()) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().email());
		}
		if (this.password == null || this.password.trim().isEmpty()) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().password());
		}
		if (this.smtpMailServer == null || this.smtpMailServer.trim().isEmpty()) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().smtpMailServer());
		}
		if (this.portNumber == 0) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().portNumber());
		}
	}

}
