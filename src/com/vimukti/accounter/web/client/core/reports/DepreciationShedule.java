package com.vimukti.accounter.web.client.core.reports;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;

/**
 * 
 * @author Devaraju.k
 * 
 * @description: This class shows Depreciation Schedule report based on the
 *               Accurate/Estimated.
 * 
 * 
 */
public class DepreciationShedule extends BaseReport implements IsSerializable,
		Serializable {

	long fixedAssetId;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Name of the Asset.
	 */

	private String assetName;

	/**
	 * Asset Number
	 */

	private String number;

	/**
	 * asset type
	 */
	private String type;

	/**
	 * Cost of Asset
	 */
	private double costOfAsset;

	/**
	 * Depreciation will be calculated per month depending on this depreciation
	 * rate. But here depreciation rate indicate the rate per Year.
	 */
	private double depreciationRate;

	/**
	 * Date of Purchase
	 */
	private ClientFinanceDate purchaseDate;

	/**
	 * Dispose date
	 */

	private ClientFinanceDate disposeDate;

	/**
	 * Start date of Financial Year or Fiscal year. FIXME :may be dont use...
	 */

	private ClientFinanceDate fiscalyear;

	/**
	 * Purchase cost of an asset.
	 */

	private double purchaseCost;

	/**
	 * Depreciation Amount.
	 */

	private double depreciationAmount;

	/**
	 * Selling or Disposals amount of an Asset.
	 */

	private double soldOrDisposalAmount;

	/**
	 * Accumulated Depreciation Amount.
	 */

	private double accumulatedDepreciationAmount;

	/**
	 * Net Amount of an Fixed Asset.
	 */

	private double netTotalOfAnFixedAssetAmount;

	private String assetAccountName;

	public long getFixedAssetId() {
		return fixedAssetId;
	}

	public void setFixedAssetId(long fixedAssetId) {
		this.fixedAssetId = fixedAssetId;
	}

	public String getAssetAccountName() {
		return assetAccountName;
	}

	public void setAssetAccountName(String assetAccountName) {
		this.assetAccountName = assetAccountName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public double getCostOfAsset() {
		return costOfAsset;
	}

	public void setCostOfAsset(double costOfAsset) {
		this.costOfAsset = costOfAsset;
	}

	public double getDepreciationRate() {
		return depreciationRate;
	}

	public void setDepreciationRate(double depreciationRate) {
		this.depreciationRate = depreciationRate;
	}

	public ClientFinanceDate getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(ClientFinanceDate purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public ClientFinanceDate getDisposeDate() {
		return disposeDate;
	}

	public void setDisposeDate(ClientFinanceDate disposeDate) {
		this.disposeDate = disposeDate;
	}

	public ClientFinanceDate getFiscalyear() {
		return fiscalyear;
	}

	public void setFiscalyear(ClientFinanceDate fiscalyear) {
		this.fiscalyear = fiscalyear;
	}

	public double getPurchaseCost() {
		return purchaseCost;
	}

	public void setPurchaseCost(double purchaseCost) {
		this.purchaseCost = purchaseCost;
	}

	public double getDepreciationAmount() {
		return depreciationAmount;
	}

	public void setDepreciationAmount(double depreciationAmount) {
		this.depreciationAmount = depreciationAmount;
	}

	public double getSoldOrDisposalAmount() {
		return soldOrDisposalAmount;
	}

	public void setSoldOrDisposalAmount(double soldOrDisposalAmount) {
		this.soldOrDisposalAmount = soldOrDisposalAmount;
	}

	public double getAccumulatedDepreciationAmount() {
		return accumulatedDepreciationAmount;
	}

	public void setAccumulatedDepreciationAmount(
			double accumulatedDepreciationAmount) {
		this.accumulatedDepreciationAmount = accumulatedDepreciationAmount;
	}

	public double getNetTotalOfAnFixedAssetAmount() {
		return netTotalOfAnFixedAssetAmount;
	}

	public void setNetTotalOfAnFixedAssetAmount(
			double netTotalOfAnFixedAssetAmount) {
		this.netTotalOfAnFixedAssetAmount = netTotalOfAnFixedAssetAmount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
