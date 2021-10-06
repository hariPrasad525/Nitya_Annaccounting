package com.nitya.accounter.web.client.core;

import java.io.Serializable;
import java.util.HashSet;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SubscriptionDetails implements Serializable, IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long clientId;

	private String emailId;

	private int subscriptionType;

	private String expiresOn;

	private int durationType;

	/**
	 * This will Exists if subscriptionYype 2 or 3
	 */
	private HashSet<String> allowedEmails;

	private boolean isPaidUser;

	private boolean isFreeTrailDone;

	/**
	 * @return the clientId
	 */
	public long getClientId() {
		return clientId;
	}

	/**
	 * @param clientId
	 *            the clientId to set
	 */
	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	/**
	 * @return the emailId
	 */
	public String getEmailId() {
		return emailId;
	}

	/**
	 * @param emailId
	 *            the emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	/**
	 * @return the subscriptionType
	 */
	public int getSubscriptionType() {
		return subscriptionType;
	}

	/**
	 * @param subscriptionType
	 *            the subscriptionType to set
	 */
	public void setSubscriptionType(int subscriptionType) {
		this.subscriptionType = subscriptionType;
	}

	/**
	 * @return the expiresOn
	 */
	public String getExpiresOn() {
		return expiresOn;
	}

	/**
	 * @param expiresOn
	 *            the expiresOn to set
	 */
	public void setExpiresOn(String expiresOn) {
		this.expiresOn = expiresOn;
	}

	/**
	 * @return the durationType
	 */
	public int getDurationType() {
		return durationType;
	}

	/**
	 * @param durationType
	 *            the durationType to set
	 */
	public void setDurationType(int durationType) {
		this.durationType = durationType;
	}

	/**
	 * @return the allowedEmails
	 */
	public HashSet<String> getAllowedEmails() {
		return allowedEmails;
	}

	/**
	 * @param allowedEmails
	 *            the allowedEmails to set
	 */
	public void setAllowedEmails(HashSet<String> allowedEmails) {
		this.allowedEmails = allowedEmails;
	}

	/**
	 * @return the isPaidUser
	 */
	public boolean isPaidUser() {
		return isPaidUser;
	}

	/**
	 * @param isPaidUser
	 *            the isPaidUser to set
	 */
	public void setPaidUser(boolean isPaidUser) {
		this.isPaidUser = isPaidUser;
	}

	/**
	 * @return the isFreeTailDone
	 */
	public boolean isFreeTrailDone() {
		return isFreeTrailDone;
	}

	/**
	 * @param isFreeTailDone
	 *            the isFreeTailDone to set
	 */
	public void setFreeTrailDone(boolean isFreeTailDone) {
		this.isFreeTrailDone = isFreeTailDone;
	}

}
