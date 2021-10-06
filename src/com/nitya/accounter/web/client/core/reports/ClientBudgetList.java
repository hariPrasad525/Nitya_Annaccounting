package com.nitya.accounter.web.client.core.reports;

import java.io.Serializable;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.util.DayAndMonthUtil;

public class ClientBudgetList extends BaseReport implements IsSerializable,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String name = "";

	String accountName = "";

	private int transactionType;

	private long transactionId;

	ClientAccount account;

	private HashMap<Integer, Double> amountMap = new HashMap<Integer, Double>();
	private HashMap<String, Double> monthNamesMap = new HashMap<String, Double>();

	double januaryAmount = 0.0D;
	double febrauaryAmount = 0.0D;
	double marchAmount = 0.0D;
	double aprilAmount = 0.0D;
	double mayAmount = 0.0D;
	double juneAmount = 0.0D;
	double julyAmount = 0.0D;
	double augustAmount = 0.0D;
	double septemberAmount = 0.0D;
	double octoberAmount = 0.0D;
	double novemberAmount = 0.0D;
	double decemberAmount = 0.0D;
	double totalAmount = 0.0D;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public ClientAccount getAccount() {
		return account;
	}

	public void setAccount(ClientAccount account) {
		this.account = account;
	}

	public double getJanuaryAmount() {
		return januaryAmount;
	}

	public void setJanuaryAmount(double januaryAmount) {
		this.januaryAmount = januaryAmount;
	}

	public double getFebrauaryAmount() {
		return febrauaryAmount;
	}

	public void setFebrauaryAmount(double febrauaryAmount) {
		this.febrauaryAmount = febrauaryAmount;
	}

	public double getMarchAmount() {
		return marchAmount;
	}

	public void setMarchAmount(double marchAmount) {
		this.marchAmount = marchAmount;
	}

	public double getAprilAmount() {
		return aprilAmount;
	}

	public void setAprilAmount(double aprilAmount) {
		this.aprilAmount = aprilAmount;
	}

	public double getMayAmount() {
		return mayAmount;
	}

	public void setMayAmount(double mayAmount) {
		this.mayAmount = mayAmount;
	}

	public double getJuneAmount() {
		return juneAmount;
	}

	public void setJuneAmount(double juneAmount) {
		this.juneAmount = juneAmount;
	}

	public double getJulyAmount() {
		return julyAmount;
	}

	public void setJulyAmount(double julyAmount) {
		this.julyAmount = julyAmount;
	}

	public double getAugustAmount() {
		return augustAmount;
	}

	public void setAugustAmount(double augustAmount) {
		this.augustAmount = augustAmount;
	}

	public double getSeptemberAmount() {
		return septemberAmount;
	}

	public void setSeptemberAmount(double septemberAmount) {
		this.septemberAmount = septemberAmount;
	}

	public double getOctoberAmount() {
		return octoberAmount;
	}

	public void setOctoberAmount(double octoberAmount) {
		this.octoberAmount = octoberAmount;
	}

	public double getNovemberAmount() {
		return novemberAmount;
	}

	public void setNovemberAmount(double novemberAmount) {
		this.novemberAmount = novemberAmount;
	}

	public double getDecemberAmount() {
		return decemberAmount;
	}

	public void setDecemberAmount(double decemberAmount) {
		this.decemberAmount = decemberAmount;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public int getTransactionType() {
		return transactionType = IAccounterCore.BUDGET;
	}

	public void setTransactionType(int transactionType) {
		this.transactionType = transactionType;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public void preparMonthNamesList(int firstMonthOfFiscalYear) {
		firstMonthOfFiscalYear++;
		for (int jj = 1; jj <= 12; jj++) {
			if (firstMonthOfFiscalYear > 12) {
				firstMonthOfFiscalYear = 1;
			}
			String monthName = DayAndMonthUtil.monthNames
					.get(firstMonthOfFiscalYear);
			if (monthName != null && !(monthName.isEmpty())) {
				amountMap.put(jj, monthNamesMap.get(monthName));
			}
			firstMonthOfFiscalYear++;
		}
	}

	public HashMap<String, Double> getMonthNamesMap() {
		return monthNamesMap;
	}

	public void setMonthNamesMap(HashMap<String, Double> monthNamesMap) {
		this.monthNamesMap = monthNamesMap;
	}

	public HashMap<Integer, Double> getAmountMap() {
		return amountMap;
	}

	public void setAmountMap(HashMap<Integer, Double> amountMap) {
		this.amountMap = amountMap;
	}

}
