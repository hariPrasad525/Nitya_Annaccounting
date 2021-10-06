package com.nitya.accounter.web.client.core.reports;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BudgetActuals extends BaseReport implements IsSerializable,
Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String accountName;
	
	private double atualAmount;
	
	private double budgetAmount;
	
	private int type;

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public double getAtualAmount() {
		return atualAmount;
	}

	public void setAtualAmount(double atualAmount) {
		this.atualAmount = atualAmount;
	}

	public double getBudgetAmount() {
		return budgetAmount;
	}

	public void setBudgetAmount(double budgetAmount) {
		this.budgetAmount = budgetAmount;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}
