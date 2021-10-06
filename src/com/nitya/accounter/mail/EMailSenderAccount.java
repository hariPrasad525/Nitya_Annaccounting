package com.nitya.accounter.mail;

import java.io.Serializable;

public class EMailSenderAccount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String senderEmailID = "***REMOVED***";

	private String senderPassword = "";
	private String outGoingMailServer = "localhost";
	private int portNumber = 25;
	private String protocol = "smtp";
	private boolean smtpAuthentication = false;
	private boolean sslAutheticationRequired = false;
	private boolean isTtlsEnabled = false;
	private String accountName = "";

	public EMailSenderAccount() {

	}

	public EMailSenderAccount(String senderEmailID, String accountName,
			String senderPassword, String outGoingMailServer, int portNumber,
			String protocol, boolean smtpAuthentication,
			boolean sslAutheticationRequired, boolean startTtlsEnables) {
		this.senderEmailID = senderEmailID;
		this.senderPassword = senderPassword;
		this.outGoingMailServer = outGoingMailServer;
		this.portNumber = portNumber;
		this.protocol = protocol;
		this.smtpAuthentication = smtpAuthentication;
		this.sslAutheticationRequired = sslAutheticationRequired;
		this.isTtlsEnabled = startTtlsEnables;
		this.accountName = accountName;
	}

	public String getSenderEmailID() {
		return senderEmailID;
	}

	public void setSenderEmailID(String senderEmailID) {
		this.senderEmailID = senderEmailID;
	}

	public String getSenderPassword() {
		return senderPassword;
	}

	public void setSenderPassword(String senderPassword) {
		this.senderPassword = senderPassword;
	}

	public String getOutGoingMailServer() {
		return outGoingMailServer;
	}

	public void setOutGoingMailServer(String outGoingMailServer) {
		this.outGoingMailServer = outGoingMailServer;
	}

	public int getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public boolean isSmtpAuthentication() {
		return smtpAuthentication;
	}

	public void setSmtpAuthentication(boolean smtpAuthentication) {
		this.smtpAuthentication = smtpAuthentication;
	}

	public boolean isAutheticationRequired() {
		return sslAutheticationRequired;
	}

	public void setSslAutheticationRequired(boolean sslAutheticationRequired) {
		this.sslAutheticationRequired = sslAutheticationRequired;
	}

	public boolean isEnabledTls() {
		return isTtlsEnabled;
	}

	public void setEnableTls(boolean startTtlsEnables) {
		this.isTtlsEnabled = startTtlsEnables;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccoutName() {
		return this.accountName;
	}

}
