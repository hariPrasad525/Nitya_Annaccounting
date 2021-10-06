package com.nitya.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

public class ClientBudget implements IAccounterCore {

	private static final long serialVersionUID = 1L;

	private long id;

	private String budgetName;

	private String financialYear;

	private int version;

	List<ClientBudgetItem> budgetItems = new ArrayList<ClientBudgetItem>();

	public long getID() {
		return id;
	}

	public void setID(long id) {
		this.id = id;
	}

	public String getBudgetName() {
		return budgetName;
	}

	public void setBudgetName(String budgetname) {
		this.budgetName = budgetname;
	}

	public List<ClientBudgetItem> getBudgetItem() {
		return budgetItems;
	}

	public void setBudgetItem(List<ClientBudgetItem> budgetitems) {
		this.budgetItems = budgetitems;
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
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayName() {
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.BUDGET;
	}


	public String getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear(String financialYear) {
		this.financialYear = financialYear;
	}

}
