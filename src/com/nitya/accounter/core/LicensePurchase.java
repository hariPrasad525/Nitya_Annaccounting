package com.nitya.accounter.core;

import java.io.Serializable;
import java.util.Date;

public class LicensePurchase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int TYPE_ONE_USER = 1;
	public static final int TYPE_TWO_USER = 2;
	public static final int TYPE_FIVE_USER = 3;
	public static final int TYPE_UNLIMITED_USER = 4;

	private long id;

	private int type;

	private String paypalSubscriptionID;

	private Client client;

	private Date purchaseDate;

	private Date expiredDate;

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the paypalSubscriptionID
	 */
	public String getPaypalSubscriptionID() {
		return paypalSubscriptionID;
	}

	/**
	 * @param paypalSubscriptionID
	 *            the paypalSubscriptionID to set
	 */
	public void setPaypalSubscriptionID(String paypalSubscriptionID) {
		this.paypalSubscriptionID = paypalSubscriptionID;
	}

	/**
	 * @return the client
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * @param client
	 *            the client to set
	 */
	public void setClient(Client client) {
		this.client = client;
	}

	/**
	 * @return the purchaseDate
	 */
	public Date getPurchaseDate() {
		return purchaseDate;
	}

	/**
	 * @param purchaseDate
	 *            the purchaseDate to set
	 */
	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	/**
	 * @return the expiredDate
	 */
	public Date getExpiredDate() {
		return expiredDate;
	}

	/**
	 * @param expiredDate
	 *            the expiredDate to set
	 */
	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

}
