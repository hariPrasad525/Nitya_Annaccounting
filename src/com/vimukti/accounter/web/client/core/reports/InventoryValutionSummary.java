package com.vimukti.accounter.web.client.core.reports;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class InventoryValutionSummary extends BaseReport implements
		IsSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String itemDescription;

	private double onHand;

	private String unit;

	private Double avgCost;

	private double assetValue;

	private double salesPrice;

	private double retailValue;

	private Long itemId;

	private double perOfTotAsset;

	private double perOfTotRetail;

	private String itemName;

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public double getOnHand() {
		return onHand;
	}

	public void setOnHand(double onHand) {
		this.onHand = onHand;
	}

	public Double getAvgCost() {
		return avgCost;
	}

	public void setAvgCost(Double next) {
		this.avgCost = next;
	}

	public double getAssetValue() {
		return assetValue;
	}

	public void setAssetValue(double assetValue) {
		this.assetValue = assetValue;
	}

	public double getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(double salesPrice) {
		this.salesPrice = salesPrice;
	}

	public double getRetailValue() {
		return retailValue;
	}

	public void setRetailValue(double retailValue) {
		this.retailValue = retailValue;
	}

	public Long getId() {
		return getItemId();
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public double getPercentOfTotAsset() {
		return getPerOfTotAsset();
	}

	public double getPerOfTotAsset() {
		return perOfTotAsset;
	}

	public void setPerOfTotAsset(double perOfTotAsset) {
		this.perOfTotAsset = perOfTotAsset;
	}

	public double getPercentOfTotRetail() {
		return getPerOfTotRetail();
	}

	public double getPerOfTotRetail() {
		return perOfTotRetail;
	}

	public void setPerOfTotRetail(double perOfTotRetail) {
		this.perOfTotRetail = perOfTotRetail;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

}
