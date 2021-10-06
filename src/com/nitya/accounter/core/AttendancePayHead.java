package com.nitya.accounter.core;

import java.util.Calendar;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientEmployeePayHeadComponent;
import com.nitya.accounter.web.client.exception.AccounterException;

/**
 * On Attendance type of Calculation Type is based on the attendance data where
 * the component will get pro-rated based on the actual days the Employee is
 * present in the office for the selected period. In Tally.ERP 9 the attendance
 * can be recorded as positive days (Present) or loss of pay (Absent) type.
 * 
 * @author Prasanna Kumar G
 * 
 */
public class AttendancePayHead extends PayHead {
	
	public static final String DEFAULT_MILE="Total of Miles";
	public static final String DEFAULT_HOUR="Total of Hours";
	public static final String DEFAULT_WORK="Total of Work";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * As per Calendar Period refers to the perpetual calendar month, i.e. if the
	 * payroll process is carried out for the month of March, then the attendance
	 * records will be entered for 31 days.
	 */
	public static final int PER_DAY_CALCULATION_AS_PER_CALANDAR_PERIOD = 1;

	/**
	 * User Defined refers to the consistent payroll period irrespective of calendar
	 * month, i.e. If an employee�s salary is based on a standard month of 30 days
	 * irrespective of the calendar month, then you can select User Defined as the
	 * Calculation Basis and define the periodicity of the specified period or
	 * month.
	 */
	public static final int PER_DAY_CALCULATION_30_DAYS = 2;

	/**
	 * User Defined Calendar Type option will provide the flexibility to the user to
	 * select user defined calendar days every Calculation Period. Once this option
	 * is selected the user can have different number of working days for every
	 * Calculation Period.
	 */
	public static final int PER_DAY_CALCULATION_USER_DEFINED_CALANDAR = 3;

	public static final int ATTENDANCE_ON_PAYHEAD = 1;
	public static final int ATTENDANCE_ON_EARNING_TOTAL = 2;
	public static final int ATTENDANCE_ON_SUBTOTAL = 3;
	public static final int ATTENDANCE_ON_RATE = 4;
	public static final int ATTENDANCE_ON_EMPLOYEE = 5;

	private int attendanceType;

	private PayHead payHead;

	private int calculationPeriod;

	private int perDayCalculationBasis;

	private AttendanceOrProductionType productionType;

	/**
	 * @return the calculationPeriod
	 */
	public int getCalculationPeriod() {
		return calculationPeriod;
	}

	/**
	 * @param calculationPeriod the calculationPeriod to set
	 */
	public void setCalculationPeriod(int calculationPeriod) {
		this.calculationPeriod = calculationPeriod;
	}

	/**
	 * @return the perDayCalculationBasis
	 */
	public int getPerDayCalculationBasis() {
		return perDayCalculationBasis;
	}

	/**
	 * @param perDayCalculationBasis the perDayCalculationBasis to set
	 */
	public void setPerDayCalculationBasis(int perDayCalculationBasis) {
		this.perDayCalculationBasis = perDayCalculationBasis;
	}

	@Override
	public double calculatePayment(ClientEmployeePayHeadComponent component,PayStructureItem payStructureItem, double deductions, double earnings) {
		double rate = payStructureItem.getRate();
		double deductableSalary = rate;
		double workingDays = getWorkingDays(payStructureItem);
		if (attendanceType == ATTENDANCE_ON_RATE) {
			if (workingDays == 0) {
				deductableSalary = 0;
			} else {
				if (getCalculationType() == CALCULATION_TYPE_ON_PRODUCTION) {
					deductableSalary = rate * payStructureItem.getAttendance()[2];
				} else {
					deductableSalary = (rate / workingDays) * (getCalculationType() == CALCULATION_TYPE_ON_PRODUCTION
							? payStructureItem.getAttendance()[2]
							: payStructureItem.getAttendance()[0]);
				}
			}
		} else if (attendanceType == ATTENDANCE_ON_EARNING_TOTAL) {
			if (workingDays == 0) {
				deductableSalary = 0;
			} else {
				// Calculate Deduction for leaves taken
				double leavesTaken = payStructureItem.getAttendance()[1];
				double perDayEarning = (earnings / workingDays);
				deductableSalary = perDayEarning * leavesTaken;
			}
		} else if (attendanceType == ATTENDANCE_ON_SUBTOTAL) {
			deductableSalary = (rate + earnings) - deductions;
		} else if (attendanceType == ATTENDANCE_ON_PAYHEAD) {
			List<PayStructureItem> items = payStructureItem.getPayStructure().getItems();
			PayStructureItem payHeadStructureItem = null;
			for (PayStructureItem structureItem : items) {
				if (structureItem.getPayHead() != null && structureItem.getPayHead().getID() == this.payHead.getID()) {
					payHeadStructureItem = structureItem;
					break;
				}
			}
			if (payHeadStructureItem != null) {
				payHeadStructureItem.setStartDate(payStructureItem.getStartDate());
				payHeadStructureItem.setEndDate(payStructureItem.getEndDate());
				payHeadStructureItem.setAttendance(payStructureItem.getAttendance());
				deductableSalary = payHeadStructureItem.getRate();
				if (workingDays == 0) {
					deductableSalary = 0;
				} else {
					double perDayAmount = deductableSalary / workingDays;
					deductableSalary = perDayAmount
							* (isEarning() ? payStructureItem.getAttendance()[2] : payStructureItem.getAttendance()[1]);
				}
			} else {
				deductableSalary = 0.0;
			}
		} else if (attendanceType == ATTENDANCE_ON_EMPLOYEE) {
			Employee employee = payStructureItem.getPayStructure().getEmployee();
			int payType = employee.getPayType();
			switch (payType) {
			case EmployeeCompsensation.PAY_TYPE_HOURLY:
//				return workingDays * 8 * employee.getSalary();
			case EmployeeCompsensation.PAY_TYPE_DAILY:
//				return workingDays * (employee.getSalary());
			case EmployeeCompsensation.PAY_TYPE_ANNUAL:
//				return workingDays * (employee.getSalary() / 260);
			case EmployeeCompsensation.PAY_TYPE_MILES:
			case EmployeeCompsensation.PAY_TYPE_PER_WORK:
				return payStructureItem.getAttendance()[2] * employee.getSalary();
			default:
				return 0.0;
			}

		}

		return deductableSalary;
	}

	private double getWorkingDays(PayStructureItem payStructureItem) {
		double workingDays = 0;
		workingDays = payStructureItem.getAttendance()[0];
		if(workingDays > 0) {
			return workingDays;
		}
		if (getPerDayCalculationBasis() == PER_DAY_CALCULATION_AS_PER_CALANDAR_PERIOD
				|| attendanceType == ATTENDANCE_ON_EMPLOYEE) {
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(payStructureItem.getStartDate().getAsDateObject());
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(payStructureItem.getEndDate().getAsDateObject());
			long diff = calendar2.getTimeInMillis() - calendar1.getTimeInMillis();
			long diffDays = diff / (24 * 60 * 60 * 1000);
			workingDays = diffDays;
		}

		if (getPerDayCalculationBasis() == PER_DAY_CALCULATION_30_DAYS) {
			workingDays = 30;
		}

		if (getPerDayCalculationBasis() == PER_DAY_CALCULATION_USER_DEFINED_CALANDAR) {
			workingDays = payStructureItem.getAttendance()[0];
		}

		return workingDays;
	}

	public int getAttendanceType() {
		return attendanceType;
	}

	public void setAttendanceType(int attendanceType) {
		this.attendanceType = attendanceType;
	}

	public PayHead getPayhead() {
		return payHead;
	}

	public void setPayhead(PayHead payHead) {
		this.payHead = payHead;
	}

	@Override
	public void selfValidate() throws AccounterException {
		// TODO Auto-generated method stub

	}

	public AttendanceOrProductionType getProductionType() {
		return productionType;
	}

	public void setProductionType(AttendanceOrProductionType productionType) {
		this.productionType = productionType;
	}
}