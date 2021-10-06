package com.nitya.accounter.core;

import java.util.Calendar;

import com.nitya.accounter.web.client.core.ClientEmployeePayHeadComponent;
import com.nitya.accounter.web.client.exception.AccounterException;

/**
 * Flat Rate is a Calculation Type is used where the value of the Pay head is a
 * fixed amount for a period. Pro-rata will not happen in this type of the
 * component (Pay Head). Examples of Flat Rate Calculation Type:
 * 
 * @author Prasanna Kumar G
 * 
 */
public class FlatRatePayHead extends PayHead {
	
	public static final int PAY_TYPE_HOURLY = 1;
	public static final int PAY_TYPE_PER_PERIOD = 2;
	public static final int PAY_TYPE_ANNUAL = 3;
	public static final int PAY_TYPE_DAILY = 4;
	public static final int PAY_TYPE_MILES = 5;
	public static final int PAY_TYPE_PER_WORK = 6;
	
	public static final int PAY_FREQUENCY_WEEKLY = 1;
	public static final int PAY_FREQUENCY_BI_WEEKLY = 2;
	public static final int PAY_FREQUENCY_SEMI_MONTHLY = 3;
	public static final int PAY_FREQUENCY_MONTHLY = 4;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int calculationPeriod;
	
	private int payType;
	private int payFrequency;

	public FlatRatePayHead() {
		super(CALCULATION_TYPE_FLAT_RATE);
	}
	
	public int getPayFrequency() {
		return payFrequency;
	}

	public void setPayFrequency(int payFrequency) {
		this.payFrequency = payFrequency;
	}
	
	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	/**
	 * @return the calculationPeriod
	 */
	public int getCalculationPeriod() {
		return calculationPeriod;
	}

	/**
	 * @param calculationPeriod
	 *            the calculationPeriod to set
	 */
	public void setCalculationPeriod(int calculationPeriod) {
		this.calculationPeriod = calculationPeriod;
	}

	@Override
	public double calculatePayment(ClientEmployeePayHeadComponent component,PayStructureItem payStructureItem,
			double deductions, double earnings) {
		return payStructureItem.getRate() * getWorkingDays(payStructureItem);
	}
	
	private double getWorkingDays(PayStructureItem payStructureItem) {
		double workingDays = 0;
		Calendar calendar1 = Calendar.getInstance();
		calendar1
				.setTime(payStructureItem.getStartDate().getAsDateObject());
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(payStructureItem.getEndDate().getAsDateObject());
		long diff = calendar2.getTimeInMillis()
				- calendar1.getTimeInMillis();
		long diffDays = diff / (24 * 60 * 60 * 1000);
		
		if (getCalculationPeriod() == CALCULATION_PERIOD_DAYS) {
			workingDays = diffDays;
		}
		
		if (getCalculationPeriod() == CALCULATION_PERIOD_WEEKS) {
			workingDays = diffDays/5;
		}
		
		if (getCalculationPeriod() == CALCULATION_PERIOD_MONTHS) {
			workingDays = diffDays/30;
		}
		
		if (getCalculationPeriod() == CALCULATION_PERIOD_FOR_NIGHTS) {
			workingDays = diffDays/30;
		} else if (getCalculationPeriod() == CALCULATION_PERIOD_SALARY) {
			workingDays = diffDays;
		}
		
		this.setAffectNetSalary(true);

		return workingDays;
	}


	@Override
	public void selfValidate() throws AccounterException {
		// TODO Auto-generated method stub

	}
}
