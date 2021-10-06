package com.nitya.accounter.web.client.core.Lists;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.nitya.accounter.web.client.core.ClientFinanceDate;

public class FixedAssetList implements IsSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This will hold a secure 40 digit random number.
	 */
	private long id;

	/**
	 * Unique Item ID, for which the
	 */
	private String name;

	private String assetNumber;

	/**
	 * Asset Account
	 * 
	 * @see
	 */
	private long assetAccount;

	/**
	 * Date of Purchase
	 */
	private ClientFinanceDate purchaseDate;

	/**
	 * Purchase Price
	 */
	private double purchasePrice;

	private double bookValue;

	/**
	 * @return the id
	 */
	public long getID(){
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setID(long id){
		this.id=id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the assetNumber
	 */
	public String getAssetNumber() {
		return assetNumber;
	}

	/**
	 * @param assetNumber
	 *            the assetNumber to set
	 */
	public void setAssetNumber(String assetNumber) {
		this.assetNumber = assetNumber;
	}

	/**
	 * @return the assetAccount
	 */
	public long getAssetAccount() {
		return assetAccount;
	}

	/**
	 * @param assetAccount
	 *            the assetAccount to set
	 */
	public void setAssetAccount(long assetAccount) {
		this.assetAccount = assetAccount;
	}

	/**
	 * @return the purchaseDate
	 */
	public ClientFinanceDate getPurchaseDate() {
		return purchaseDate;
	}

	/**
	 * @param purchaseDate
	 *            the purchaseDate to set
	 */
	public void setPurchaseDate(ClientFinanceDate purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	/**
	 * @return the purchasePrice
	 */
	public double getPurchasePrice() {
		return purchasePrice;
	}

	/**
	 * @param purchasePrice
	 *            the purchasePrice to set
	 */
	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	/**
	 * @return the bookValue
	 */
	public double getBookValue() {
		return bookValue;
	}

	/**
	 * @param bookValue
	 *            the bookValue to set
	 */
	public void setBookValue(double bookValue) {
		this.bookValue = bookValue;
	}

}
