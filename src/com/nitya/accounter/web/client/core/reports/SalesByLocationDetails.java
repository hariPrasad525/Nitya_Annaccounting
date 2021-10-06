package com.nitya.accounter.web.client.core.reports;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.nitya.accounter.web.client.core.ClientFinanceDate;

public class SalesByLocationDetails extends BaseReport implements
		IsSerializable, Serializable {

	private static final long serialVersionUID = 1L;

	ClientFinanceDate dateOfCreation;

	private int type;

	private String number;

	private String prouductOrService;

	private String memoOrDescription;

	private long qty;

	private long rate;

	private double amount;

	private double balance;

	private String account;

	private long transactionid;

	private long parent;

	private long classId;

	private List<String> parents;

	private int depth;

	private Map<String, Integer> depthsByName;

	public long getTransactionid() {
		return transactionid;
	}

	public void setTransactionid(long transactionid) {
		this.transactionid = transactionid;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	private long date;

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	private String locationName;

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public ClientFinanceDate getDateOfCreation() {
		return dateOfCreation;
	}

	public void setDateOfCreation(ClientFinanceDate dateOfCreation) {
		this.dateOfCreation = dateOfCreation;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getProuductOrService() {
		return prouductOrService;
	}

	public void setProuductOrService(String prouductOrService) {
		this.prouductOrService = prouductOrService;
	}

	public String getMemoOrDescription() {
		return memoOrDescription;
	}

	public void setMemoOrDescription(String memoOrDescription) {
		this.memoOrDescription = memoOrDescription;
	}

	public long getQty() {
		return qty;
	}

	public void setQty(long qty) {
		this.qty = qty;
	}

	public long getRate() {
		return rate;
	}

	public void setRate(long rate) {
		this.rate = rate;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public long getParent() {
		return parent;
	}

	public void setParent(long parent) {
		this.parent = parent;
	}

	public long getClassId() {
		return classId;
	}

	public void setClassId(long classId) {
		this.classId = classId;
	}

	public List<String> getParents() {
		return parents;
	}

	public void setParents(List<String> parents) {
		this.parents = parents;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public Map<String, Integer> getDepthsByName() {
		return depthsByName;
	}

	public void setDepthsByName(Map<String, Integer> depthsByName) {
		this.depthsByName = depthsByName;
	}

}
