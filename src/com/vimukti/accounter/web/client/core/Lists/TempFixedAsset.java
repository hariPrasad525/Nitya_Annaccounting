package com.vimukti.accounter.web.client.core.Lists;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;

public class TempFixedAsset implements IsSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long fixedAssetID;
	private ClientFinanceDate purchaseDate;
	private boolean isNoDepreciation;
	private ClientFinanceDate soldOrDisposedDate;
	private ClientFinanceDate depreciationTillDate;
	private double purchasePrice;
	private double salesPrice;
	private double bookValue;
	private String assetAccountName;
	private String linkedAccumulatedDepreciatedAccountName;
	private String expenseAccountName;
	private String salesAccountName;
	private int depreciationMethod;
	private double depreciationRate;

	public long getFixedAssetID() {
		return fixedAssetID;
	}
	public void setFixedAssetID(long fixedAssetID) {
		this.fixedAssetID = fixedAssetID;
	}
	public ClientFinanceDate getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(ClientFinanceDate purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public boolean isNoDepreciation() {
		return isNoDepreciation;
	}
	public void setNoDepreciation(boolean isNoDepreciation) {
		this.isNoDepreciation = isNoDepreciation;
	}
	public ClientFinanceDate getSoldOrDisposedDate() {
		return soldOrDisposedDate;
	}
	public void setSoldOrDisposedDate(ClientFinanceDate soldOrDisposedDate) {
		this.soldOrDisposedDate = soldOrDisposedDate;
	}
	public ClientFinanceDate getDepreciationTillDate() {
		return depreciationTillDate;
	}
	public void setDepreciationTillDate(ClientFinanceDate depreciationTillDate) {
		this.depreciationTillDate = depreciationTillDate;
	}
	public double getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	public double getSalesPrice() {
		return salesPrice;
	}
	public void setSalesPrice(double salesPrice) {
		this.salesPrice = salesPrice;
	}

	public double getBookValue() {
		return bookValue;
	}
	public void setBookValue(double bookValue) {
		this.bookValue = bookValue;
	}
	public String getAssetAccountName() {
		return assetAccountName;
	}
	public void setAssetAccountName(String assetAccountName) {
		this.assetAccountName = assetAccountName;
	}

	public String getLinkedAccumulatedDepreciatedAccountName() {
		return linkedAccumulatedDepreciatedAccountName;
	}
	public void setLinkedAccumulatedDepreciatedAccountName(
			String linkedAccumulatedDepreciatedAccountName) {
		this.linkedAccumulatedDepreciatedAccountName = linkedAccumulatedDepreciatedAccountName;
	}
	public String getExpenseAccountName() {
		return expenseAccountName;
	}
	public void setExpenseAccountName(String expenseAccountName) {
		this.expenseAccountName = expenseAccountName;
	}
	public String getSalesAccountName() {
		return salesAccountName;
	}
	public void setSalesAccountName(String salesAccountName) {
		this.salesAccountName = salesAccountName;
	}
	public int getDepreciationMethod() {
		return depreciationMethod;
	}
	public void setDepreciationMethod(int depreciationMethod) {
		this.depreciationMethod = depreciationMethod;
	}
	public double getDepreciationRate() {
		return depreciationRate;
	}
	public void setDepreciationRate(double depreciationRate) {
		this.depreciationRate = depreciationRate;
	}

}
