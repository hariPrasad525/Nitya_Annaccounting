package com.vimukti.accounter.web.client.core.Lists;

import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.IAccounterCore;

/**
 * 
 * @author sujana Bijjam
 * 
 *         PayeeList is for displaying the last six months transaction
 *         amount,Balance and YearToDate of Suppliers and customers.
 */
public class PayeeList implements IAccounterCore {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int type;
	public long id;
	String payeeName;
	double currentMonth = 0D;
	double previousMonth = 0D;
	double previousSecondMonth = 0D;
	double previousThirdMonth = 0D;
	double previousFourthMonth = 0D;
	double previousFifthMonth = 0D;
	double yearToDate = 0D;

	long currecny;

	double balance = 0D;

	boolean isActive = Boolean.TRUE;

	private int version;

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the id
	 */
	public long getID() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setID(long id) {
		this.id = id;
	}

	/**
	 * @return the payeeName
	 */
	public String getPayeeName() {
		return payeeName;
	}

	/**
	 * @param payeeName
	 *            the payeeName to set
	 */
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	/**
	 * @return the currentMonth
	 */
	public double getCurrentMonth() {
		return currentMonth;
	}

	/**
	 * @param currentMonth
	 *            the currentMonth to set
	 */
	public void setCurrentMonth(double currentMonth) {
		this.currentMonth = currentMonth;
	}

	/**
	 * @return the previousMonth
	 */
	public double getPreviousMonth() {
		return previousMonth;
	}

	/**
	 * @param previousMonth
	 *            the previousMonth to set
	 */
	public void setPreviousMonth(double previousMonth) {
		this.previousMonth = previousMonth;
	}

	/**
	 * @return the previoustSecondMonth
	 */
	public double getPreviousSecondMonth() {
		return previousSecondMonth;
	}

	/**
	 * @param previoustSecondMonth
	 *            the previoustSecondMonth to set
	 */
	public void setPreviousSecondMonth(double previousSecondMonth) {
		this.previousSecondMonth = previousSecondMonth;
	}

	/**
	 * @return the previousThirdMonth
	 */
	public double getPreviousThirdMonth() {
		return previousThirdMonth;
	}

	/**
	 * @param previousThirdMonth
	 *            the previousThirdMonth to set
	 */
	public void setPreviousThirdMonth(double previousThirdMonth) {
		this.previousThirdMonth = previousThirdMonth;
	}

	/**
	 * @return the previousFourthMonth
	 */
	public double getPreviousFourthMonth() {
		return previousFourthMonth;
	}

	/**
	 * @param previousFourthMonth
	 *            the previousFourthMonth to set
	 */
	public void setPreviousFourthMonth(double previousFourthMonth) {
		this.previousFourthMonth = previousFourthMonth;
	}

	/**
	 * @return the previousFifthMonth
	 */
	public double getPreviousFifthMonth() {
		return previousFifthMonth;
	}

	/**
	 * @param previousFifthMonth
	 *            the previousFifthMonth to set
	 */
	public void setPreviousFifthMonth(double previousFifthMonth) {
		this.previousFifthMonth = previousFifthMonth;
	}

	/**
	 * @return the balance
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * @param balance
	 *            the balance to set
	 */
	public void setBalance(double balance) {
		this.balance = balance;
	}

	/**
	 * @return the yearToDate
	 */
	public double getYearToDate() {
		return yearToDate;
	}

	/**
	 * @param yearToDate
	 *            the yearToDate to set
	 */
	public void setYearToDate(double yearToDate) {
		this.yearToDate = yearToDate;
	}


	@Override
	public String getDisplayName() {
		return this.payeeName;
	}

	@Override
	public String getName() {
		return this.payeeName;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return null;
	}

	public PayeeList clone() {
		PayeeList payeeListClone = (PayeeList) this.clone();
		return payeeListClone;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the currecny
	 */
	public long getCurrecny() {
		return currecny;
	}

	/**
	 * @param currecny
	 *            the currecny to set
	 */
	public void setCurrecny(long currecny) {
		this.currecny = currecny;
	}

}
