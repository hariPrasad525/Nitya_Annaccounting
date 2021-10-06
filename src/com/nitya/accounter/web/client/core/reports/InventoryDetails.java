package com.nitya.accounter.web.client.core.reports;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class InventoryDetails extends BaseReport implements IsSerializable,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4071599266259837280L;

	private String itemName;
	private double qtyIn;
	private double cost;
	private double qtyOut;
	private double pricesold;
	private double onHandqty;
	private double costValuation;
	private long id;

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public double getQtyIn() {
		return qtyIn;
	}

	public void setQtyIn(double qtyIn) {
		this.qtyIn = qtyIn;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public double getQtyOut() {
		return qtyOut;
	}

	public void setQtyOut(double qtyOut) {
		this.qtyOut = qtyOut;
	}

	public double getPricesold() {
		return pricesold;
	}

	public void setPricesold(double pricesold) {
		this.pricesold = pricesold;
	}

	public double getOnHandqty() {
		return onHandqty;
	}

	public void setOnHandqty(double onHandqty) {
		this.onHandqty = onHandqty;
	}

	public double getCostValuation() {
		return costValuation;
	}

	public void setCostValuation(double costValuation) {
		this.costValuation = costValuation;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
