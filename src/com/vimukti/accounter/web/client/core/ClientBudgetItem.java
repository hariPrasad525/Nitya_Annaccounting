package com.vimukti.accounter.web.client.core;


public class ClientBudgetItem implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int version;

	String name = "";

	String accountName = "";

	ClientAccount account;

	double januaryAmount = 0.00;
	double febrauaryAmount = 0.00;
	double marchAmount = 0.00;
	double aprilAmount = 0.00;
	double mayAmount = 0.00;
	double juneAmount = 0.00;
	double julyAmount = 0.00;
	double augustAmount = 0.00;
	double septemberAmount = 0.00;
	double octoberAmount = 0.00;
	double novemberAmount = 0.00;
	double decemberAmount = 0.00;
	double totalAmount = 0.00;

	@Override
	public int getVersion() {
		// TODO Auto-generated method stub
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;

	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDisplayName() {
		return name;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.BUDGETITEM;
	}

	@Override
	public void setID(long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public long getID() {
		// TODO Auto-generated method stub
		return 0;
	}


	public double getJanuaryAmount() {
		return januaryAmount;
	}

	public ClientAccount getAccount() {
		return account;
	}

	public void setAccount(ClientAccount account) {
		this.account = account;
	}

	/**
	 * 
	 * @param amount
	 */
	public void setJanuaryAmount(double amount) {
		this.januaryAmount = amount;
	}

	/**
	 * 
	 * @return
	 */
	public double getFebruaryAmount() {
		return febrauaryAmount;
	}

	/**
	 * 
	 * @param amount
	 */
	public void setFebruaryAmount(double amount) {
		this.febrauaryAmount = amount;
	}

	/**
	 * 
	 * @return
	 */
	public double getMarchAmount() {
		return marchAmount;
	}

	/**
	 * 
	 * @param amount
	 */
	public void setMarchAmount(double amount) {
		this.marchAmount = amount;
	}

	/**
	 * 
	 * @return
	 */
	public double getAprilAmount() {
		return aprilAmount;
	}

	/**
	 * 
	 * @param amount
	 */
	public void setAprilAmount(double amount) {
		this.aprilAmount = amount;
	}

	/**
	 * 
	 * @return
	 */
	public double getMayAmount() {
		return mayAmount;
	}

	/**
	 * 
	 * @param amount
	 */
	public void setMayAmount(double amount) {
		this.mayAmount = amount;
	}

	/**
	 * 
	 * @return
	 */
	public double getJuneAmount() {
		return juneAmount;
	}

	/**
	 * 
	 * @param amount
	 */
	public void setJuneAmount(double amount) {
		this.juneAmount = amount;
	}

	/**
	 * 
	 * @return
	 */
	public double getJulyAmount() {
		return julyAmount;
	}

	/**
	 * 
	 * @param amount
	 */
	public void setJulyAmount(double amount) {
		this.julyAmount = amount;
	}

	/**
	 * 
	 * @return
	 */
	public double getAugustAmount() {
		return augustAmount;
	}

	/**
	 * 
	 * @param amount
	 */
	public void setAugustAmount(double amount) {
		this.augustAmount = amount;
	}

	/**
	 * 
	 * @return
	 */
	public double getSpetemberAmount() {
		return septemberAmount;
	}

	/**
	 * 
	 * @param amount
	 */
	public void setSeptemberAmount(double amount) {
		this.septemberAmount = amount;
	}

	/**
	 * 
	 * @return
	 */
	public double getOctoberAmount() {
		return octoberAmount;
	}

	/**
	 * 
	 * @param amount
	 */
	public void setOctoberAmount(double amount) {
		this.octoberAmount = amount;
	}

	/**
	 * 
	 * @return
	 */
	public double getNovemberAmount() {
		return novemberAmount;
	}

	/**
	 * 
	 * @param amount
	 */
	public void setNovemberAmount(double amount) {
		this.novemberAmount = amount;
	}

	/**
	 * 
	 * @return
	 */
	public double getDecemberAmount() {
		return decemberAmount;
	}

	public void setDecemberAmount(double amount) {
		this.decemberAmount = amount;
	}

	public String getAccountsName() {

		return accountName;
	}

	public void setAccountsName(String accountnames) {
		this.accountName = accountnames;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double amount) {
		this.totalAmount = amount;
	}

	public ClientBudgetItem clone() {
		ClientBudgetItem budget = this.clone();
		return budget;

	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof ClientBudgetItem) {
			ClientBudgetItem clientBudget = (ClientBudgetItem) obj;

			return true;
		}
		return false;
	}

}
