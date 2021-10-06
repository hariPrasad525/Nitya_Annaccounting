/**
 * 
 */
package com.vimukti.accounter.core;

/**
 * @author Prasanna Kumar G
 * 
 */
public class RememberMeKey extends CreatableObject {

	private String key;

	private String emailID;

	private String clientKey;// d2
	private byte[] serverKey;// s2

	/**
	 * Creates new Instance
	 */
	public RememberMeKey() {
	}

	/**
	 * Creates new Instance
	 */
	public RememberMeKey(String emailId, String key) {
		this.emailID = emailId;
		this.key = key;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the emailID
	 */
	public String getEmailID() {
		return emailID;
	}

	/**
	 * @param emailID
	 *            the emailID to set
	 */
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public byte[] getServerKey() {
		return serverKey;
	}

	public void setServerKey(byte[] serverKey) {
		this.serverKey = serverKey;
	}

	public String getClientKey() {
		return clientKey;
	}

	public void setClientKey(String clientKey) {
		this.clientKey = clientKey;
	}

}
