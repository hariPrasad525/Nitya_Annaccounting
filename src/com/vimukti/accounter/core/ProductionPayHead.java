/*package com.vimukti.accounter.core;

import com.vimukti.accounter.web.client.exception.AccounterException;

 *//**
 * On Production Calculation Type is used to calculate the pay value based on
 * the Production/Work down. The production data can be entered in Attendance
 * voucher against the Production type.
 * 
 * @author Prasanna Kumar G
 * 
 */
/*
public class ProductionPayHead extends PayHead {

 *//**
 * 
 */
/*
private static final long serialVersionUID = 1L;

AttendanceOrProductionType productionType;

public ProductionPayHead() {
super(CALCULATION_TYPE_ON_ATTENDANCE);
}

 *//**
 * @return the productionType
 */
/*
public AttendanceOrProductionType getProductionType() {
return productionType;
}

 *//**
 * @param productionType
 *            the productionType to set
 */
/*
 * public void setProductionType(AttendanceOrProductionType productionType) {
 * this.productionType = productionType; }
 * 
 * @Override public double calculatePayment(PayStructureItem payStructureItem,
 * double deductions, double earnings) { long workingDays =
 * payStructureItem.getAttendance()[2]; double rate =
 * payStructureItem.getRate(); double earningSalary = 0.0; if (workingDays != 0)
 * { earningSalary = rate * workingDays; } return earningSalary; }
 * 
 * @Override public void selfValidate() throws AccounterException { // TODO
 * Auto-generated method stub
 * 
 * }
 * 
 * }
 */