package com.nitya.accounter.web.client.core.Lists;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.nitya.accounter.web.client.core.ClientFinanceDate;

public class SellingOrDisposingFixedAssetList implements IsSerializable,
		Serializable {

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

	private ClientFinanceDate soldOrDisposedDate;

	private double salePrice = 0.0;

	double lossOrGain;

	/**
	 * @return the id
	 */
	public long getID() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setID(long id) {
		this.id = id;
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
	 * @return the soldOrDisposedDate
	 */
	public ClientFinanceDate getSoldOrDisposedDate() {
		return soldOrDisposedDate;
	}

	/**
	 * @param soldOrDisposedDate
	 *            the soldOrDisposedDate to set
	 */
	public void setSoldOrDisposedDate(ClientFinanceDate soldOrDisposedDate) {
		this.soldOrDisposedDate = soldOrDisposedDate;
	}

	/**
	 * @return the salePrice
	 */
	public double getSalePrice() {
		return salePrice;
	}

	/**
	 * @param salePrice
	 *            the salePrice to set
	 */
	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}

	/**
	 * @return the lossOrGain
	 */
	public double getLossOrGain() {
		return lossOrGain;
	}

	/**
	 * @param lossOrGain
	 *            the lossOrGain to set
	 */
	public void setLossOrGain(double lossOrGain) {
		this.lossOrGain = lossOrGain;
	}
}
