package com.nitya.accounter.core;

import com.nitya.accounter.web.client.core.ClientEmployeePayHeadComponent;
import com.nitya.accounter.web.client.exception.AccounterException;

/**
 * As User defined Value allows you to manually enter the value at the time of
 * processing the salary. This Calculation Type is used the value of pay is not
 * fixed and does not depend upon any pay components.
 * 
 * Example: Incentives or Variable Salary.
 * 
 * @author Prasanna Kumar G
 * 
 */
public class UserDefinedPayHead extends PayHead {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserDefinedPayHead() {
		super(CALCULATION_TYPE_AS_USER_DEFINED);
	}

	@Override
	public double calculatePayment(ClientEmployeePayHeadComponent component,PayStructureItem payStructureItem,
			double deductions, double earnings) {
		return payStructureItem.getAttendance()[3];
	}

	@Override
	public void selfValidate() throws AccounterException {
		// TODO Auto-generated method stub

	}

}
