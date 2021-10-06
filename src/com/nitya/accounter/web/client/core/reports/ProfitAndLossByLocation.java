package com.nitya.accounter.web.client.core.reports;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ProfitAndLossByLocation extends BaseReport implements
		IsSerializable, Serializable {

	private static final long serialVersionUID = 1L;
	private long parentAccount;
	private long accountId;
	private String accountName;
	private int accountType;
	private String accountNumber;
	private Map<Long, Double> map = new HashMap<Long, Double>();
	private long categoryId;
	private List<ProfitAndLossByClass> records = new ArrayList<ProfitAndLossByClass>();

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public int getAccountType() {
		return accountType;
	}

	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}

	public Map<Long, Double> getMap() {
		return map;
	}

	public void setMap(Map<Long, Double> map) {
		this.map = map;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public long getParentAccount() {
		return parentAccount;
	}

	public void setParentAccount(long parentAccount) {
		this.parentAccount = parentAccount;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public List<ProfitAndLossByClass> getRecords() {
		return records;
	}

	public void setRecords(List<ProfitAndLossByClass> records) {
		this.records = records;
	}
}
