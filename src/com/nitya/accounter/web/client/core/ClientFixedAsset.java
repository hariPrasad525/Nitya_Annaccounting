/**
 * 
 */
package com.nitya.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vimukti16
 * 
 */
public class ClientFixedAsset implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int METHOD_STRAIGHT_LINE = 1;
	public static final int METHOD_REDUCING_BALANCE = 2;

	public static final int STATUS_PENDING = 1;
	public static final int STATUS_REGISTERED = 2;
	public static final int STATUS_SOLD_OR_DISPOSED = 3;

	/**
	 * This is automatically generated by Hibernate which is Unique accross the
	 * Finance.
	 * 
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
	 */
	private long assetAccount;

	private long linkedAccumulatedDepreciationAccount;

	/**
	 * Date of Purchase
	 */
	private long purchaseDate;

	/**
	 * Purchase Price
	 */
	private double purchasePrice;

	/**
	 * Fixed Asset Description
	 */
	private String description;

	/**
	 * Asset Type
	 */
	private String assetType;

	private double depreciationRate;

	private int depreciationMethod;

	/**
	 * Depreciation Expense Account
	 * 
	 */
	private long depreciationExpenseAccount;

	private double accumulatedDepreciationAmount = 0.0;

	private int status;

	private double bookValue;

	private boolean isSoldOrDisposed;
	private List<ClientFixedAssetHistory> fixedAssetsHistory = new ArrayList<ClientFixedAssetHistory>();

	/**
	 * Selling OR Disposing Fixed Asset
	 * 
	 */
	private long accumulatedDepreciationAccount;
	private long soldOrDisposedDate;

	private long accountForSale;

	private double salePrice = 0.0;

	private boolean noDepreciation;

	private long depreciationTillDate;

	private String notes;

	private long lossOrGainOnDisposalAccount;

	private long totalCapitalGain;
	private double totalCapitalGainAmount;

	private double lossOrGain;

	private int version;

	// private String sellingOrDisposingFixedAsset;

	public ClientFixedAsset() {
	}

	public long getID() {
		return id;
	}

	public void setID(long id) {
		this.id = id;
	}

	public long getLinkedAccumulatedDepreciationAccount() {
		return linkedAccumulatedDepreciationAccount;
	}

	public void setLinkedAccumulatedDepreciationAccount(
			long linkedAccumulatedDepreciationAccount) {
		this.linkedAccumulatedDepreciationAccount = linkedAccumulatedDepreciationAccount;
	}

	public String getAssetNumber() {
		return assetNumber;
	}

	public void setAssetNumber(String assetNumber) {
		this.assetNumber = assetNumber;
	}

	public long getAssetAccount() {
		return assetAccount;
	}

	public void setAssetAccount(long assetAccount) {
		this.assetAccount = assetAccount;
	}

	public long getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(long purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public double getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public double getDepreciationRate() {
		return depreciationRate;
	}

	public void setDepreciationRate(double depreciationRate) {
		this.depreciationRate = depreciationRate;
	}

	public int getDepreciationMethod() {
		return depreciationMethod;
	}

	public void setDepreciationMethod(int depreciationMethod) {
		this.depreciationMethod = depreciationMethod;
	}

	public long getDepreciationExpenseAccount() {
		return depreciationExpenseAccount;
	}

	public void setDepreciationExpenseAccount(long depreciationExpenseAccount) {
		this.depreciationExpenseAccount = depreciationExpenseAccount;
	}

	public double getAccumulatedDepreciationAmount() {
		return accumulatedDepreciationAmount;
	}

	public void setAccumulatedDepreciationAmount(
			double accumulatedDepreciationAmount) {
		this.accumulatedDepreciationAmount = accumulatedDepreciationAmount;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public double getBookValue() {
		return bookValue;
	}

	public void setBookValue(double bookValue) {
		this.bookValue = bookValue;
	}

	public boolean isSoldOrDisposed() {
		return isSoldOrDisposed;
	}

	public void setSoldOrDisposed(boolean isSoldOrDisposed) {
		this.isSoldOrDisposed = isSoldOrDisposed;
	}

	// public String getSellingOrDisposingFixedAsset() {
	// return sellingOrDisposingFixedAsset;
	// }
	//
	// public void setSellingOrDisposingFixedAsset(
	// String sellingOrDisposingFixedAsset) {
	// this.sellingOrDisposingFixedAsset = sellingOrDisposingFixedAsset;
	// }

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDisplayName() {
		return this.name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.FIXEDASSET;
	}

	public List<ClientFixedAssetHistory> getFixedAssetsHistory() {
		return fixedAssetsHistory;
	}

	public void setFixedAssetsHistory(
			List<ClientFixedAssetHistory> fixedAssetsHistory) {
		this.fixedAssetsHistory = fixedAssetsHistory;
	}

	public void setSoldOrDisposedDate(long soldOrDisposedDate) {
		this.soldOrDisposedDate = soldOrDisposedDate;
	}

	public long getSoldOrDisposedDate() {
		return soldOrDisposedDate;
	}

	public void setAccountForSale(long accountForSale) {
		this.accountForSale = accountForSale;
	}

	public long getAccountForSale() {
		return accountForSale;
	}

	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}

	public double getSalePrice() {
		return salePrice;
	}

	public void setNoDepreciation(boolean noDepreciation) {
		this.noDepreciation = noDepreciation;
	}

	public boolean isNoDepreciation() {
		return noDepreciation;
	}

	public void setDepreciationTillDate(long depreciationTillDate) {
		this.depreciationTillDate = depreciationTillDate;
	}

	public long getDepreciationTillDate() {
		return depreciationTillDate;
	}

	/**
	 * @param notes
	 *            the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @param lossOrGainOnDisposalAccount
	 *            the lossOrGainOnDisposalAccount to set
	 */
	public void setLossOrGainOnDisposalAccount(long lossOrGainOnDisposalAccount) {
		this.lossOrGainOnDisposalAccount = lossOrGainOnDisposalAccount;
	}

	/**
	 * @return the lossOrGainOnDisposalAccount
	 */
	public long getLossOrGainOnDisposalAccount() {
		return lossOrGainOnDisposalAccount;
	}

	/**
	 * @param totalCapitalGain
	 *            the totalCapitalGain to set
	 */
	public void setTotalCapitalGain(long totalCapitalGain) {
		this.totalCapitalGain = totalCapitalGain;
	}

	/**
	 * @return the totalCapitalGain
	 */
	public long getTotalCapitalGain() {
		return totalCapitalGain;
	}

	/**
	 * @param lossOrGain
	 *            the lossOrGain to set
	 */
	public void setLossOrGain(double lossOrGain) {
		this.lossOrGain = lossOrGain;
	}

	/**
	 * @return the lossOrGain
	 */
	public double getLossOrGain() {
		return lossOrGain;
	}

	/**
	 * @param totalCapitalGainAmount
	 *            the totalCapitalGainAmount to set
	 */
	public void setTotalCapitalGainAmount(double totalCapitalGainAmount) {
		this.totalCapitalGainAmount = totalCapitalGainAmount;
	}

	/**
	 * @return the totalCapitalGainAmount
	 */
	public double getTotalCapitalGainAmount() {
		return totalCapitalGainAmount;
	}

	public ClientFixedAsset clone() {
		ClientFixedAsset fixedAsset = (ClientFixedAsset) this.clone();
		fixedAsset.accountForSale = this.accountForSale;

		List<ClientFixedAssetHistory> fixedAssetHistories = new ArrayList<ClientFixedAssetHistory>();
		for (ClientFixedAssetHistory clientFixedAssetHistory : this.fixedAssetsHistory) {
			fixedAssetHistories.add(clientFixedAssetHistory.clone());
		}
		fixedAsset.fixedAssetsHistory = fixedAssetHistories;

		return fixedAsset;

	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof ClientFixedAsset) {
			ClientFixedAsset fixedAsset = (ClientFixedAsset) obj;
			return this.getID() == fixedAsset.getID() ? true : false;
		}
		return false;
	}

	public long getAccumulatedDepreciationAccount() {
		return accumulatedDepreciationAccount;
	}

	public void setAccumulatedDepreciationAccount(
			long accumulatedDepreciationAccount) {
		this.accumulatedDepreciationAccount = accumulatedDepreciationAccount;
	}

}
