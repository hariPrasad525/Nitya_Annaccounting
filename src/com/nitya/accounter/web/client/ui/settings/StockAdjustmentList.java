package com.nitya.accounter.web.client.ui.settings;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.nitya.accounter.web.client.core.ClientQuantity;

public class StockAdjustmentList implements IsSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String wareHouse;

	private String item;

	private ClientQuantity quantity;

	private long stockAdjustment;

	public String getWareHouse() {
		return wareHouse;
	}

	public void setWareHouse(String wareHouse) {
		this.wareHouse = wareHouse;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public ClientQuantity getQuantity() {
		return quantity;
	}

	public void setQuantity(ClientQuantity quantity) {
		this.quantity = quantity;
	}

	public long getStockAdjustment() {
		return stockAdjustment;
	}

	public void setStockAdjustment(long stockAdjustment) {
		this.stockAdjustment = stockAdjustment;
	}

}
