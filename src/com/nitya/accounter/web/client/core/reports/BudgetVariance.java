package com.nitya.accounter.web.client.core.reports;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BudgetVariance implements IsSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String accountName;

	double actual;

	double budget;

	double variance;

	double ytdActual;

	double ytdbudget;

	double ytdVariance;

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public double getActual() {
		return actual;
	}

	public void setActual(double actual) {
		this.actual = actual;
	}

	public double getBudget() {
		return budget;
	}

	public void setBudget(double budget) {
		this.budget = budget;
	}

	public double getVariance() {
		return variance;
	}

	public void setVariance(double variance) {
		this.variance = variance;
	}

	public double getYtdActual() {
		return ytdActual;
	}

	public void setYtdActual(double ytdActual) {
		this.ytdActual = ytdActual;
	}

	public double getYtdbudget() {
		return ytdbudget;
	}

	public void setYtdbudget(double ytdbudget) {
		this.ytdbudget = ytdbudget;
	}

	public double getYtdVariance() {
		return ytdVariance;
	}

	public void setYtdVariance(double ytdVariance) {
		this.ytdVariance = ytdVariance;
	}

}
