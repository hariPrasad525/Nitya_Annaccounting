package com.nitya.accounter.core;

import java.util.List;

import com.nitya.accounter.web.client.core.ClientEmployeePayHeadComponent;
import com.nitya.accounter.web.client.exception.AccounterException;

public class TaxComputationPayHead extends PayHead {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	LiveTaxRate liveTaxRate;

	public TaxComputationPayHead() {
		super(CALCULATION_TYPE_AS_TAX);
	}

	public LiveTaxRate getLiveTaxRate() {
		return liveTaxRate;
	}

	public void setLiveTaxRate(LiveTaxRate liveTaxRate) {
		this.liveTaxRate = liveTaxRate;
	}

	@Override
	public double calculatePayment(ClientEmployeePayHeadComponent component, PayStructureItem payStructureItem,
			double deductions, double earnings) {
		double calculatedAmount = earnings;
		double totalTax = 0.0;
		double remainingAmt = calculatedAmount;
		List<LiveTaxRateRange> ranges = this.liveTaxRate.getRates();
		if (this.liveTaxRate.getTaxType().equals(LiveTaxRate.TAX_TYPE_FEDERAL)
				|| this.liveTaxRate.getTaxType().equals(LiveTaxRate.TAX_TYPE_STATE)) {
			double potentialIncome = calculatePotentialIncome(payStructureItem, earnings);
			double incomPercent = (calculatedAmount / potentialIncome) * 100;
			//potentialIncome = potentialIncome - this.liveTaxRate.getStandardDeductions();
//			for (LiveTaxRateRange rate : ranges) {
//				if (rate == null)
//					continue;
//				if (potentialIncome >= rate.getStart() && potentialIncome <= rate.getEnd()
//						|| (rate.getStart() == 0 && rate.getEnd() == 0)) {
//					totalTax = (calculatedAmount * rate.getRate() / 100);
//					break;
//				}
//			}
			remainingAmt = potentialIncome;
			for (LiveTaxRateRange rate : ranges) {
				if (rate == null)
					continue;
				if (potentialIncome >= rate.getStart() && (potentialIncome <= rate.getEnd() || rate.isPlusMore())
						|| (potentialIncome >= rate.getStart() && potentialIncome >= rate.getEnd())) {
					if (remainingAmt > rate.getEnd()) {
						totalTax += rate.getEnd() * (rate.getRate() / 100);
					} else {
						totalTax += remainingAmt * (rate.getRate() / 100);
					}
					remainingAmt = remainingAmt - rate.getEnd();
					if (remainingAmt < 0) {
						break;
					}
				}
			}
			totalTax = (incomPercent / 100) * totalTax;
		} else {
			for (LiveTaxRateRange rate : ranges) {
				if (rate == null)
					continue;
				if (calculatedAmount >= rate.getStart() && calculatedAmount <= rate.getEnd()
						|| (rate.getStart() == 0 && rate.getEnd() == 0)) {
					totalTax = (calculatedAmount * rate.getRate() / 100);
					break;
				}
			}
		}

		if (this.isEmployeerDeduction()) {
			component.setEmployeerDeductions(Math.round(totalTax));
		}
		return Math.round(totalTax);
	}
	
	private double calculatePotentialIncome(PayStructureItem payStructureItem, double earnings) {
		Employee employee = payStructureItem.getPayStructure().getEmployee();
		int notOfDaysInYear = 2080/8;
		int payType = employee.getPayType();
		switch (payType) {
		case EmployeeCompsensation.PAY_TYPE_HOURLY:
			return notOfDaysInYear * 8 * employee.getSalary();
		case EmployeeCompsensation.PAY_TYPE_DAILY:
			return notOfDaysInYear * (employee.getSalary());
		case EmployeeCompsensation.PAY_TYPE_ANNUAL:
			return employee.getSalary();
		case EmployeeCompsensation.PAY_TYPE_PER_PERIOD:
			if (employee.getPayFrequency() == EmployeeCompsensation.PAY_FREQUENCY_BI_WEEKLY
					|| employee.getPayFrequency() == EmployeeCompsensation.PAY_FREQUENCY_SEMI_MONTHLY) {
				return employee.getSalary() * (notOfDaysInYear / 5) / 2;
			} else if (employee.getPayFrequency() == EmployeeCompsensation.PAY_FREQUENCY_MONTHLY) {
				return employee.getSalary() * (notOfDaysInYear / 12);
			} else if (employee.getPayFrequency() == EmployeeCompsensation.PAY_FREQUENCY_WEEKLY) {
				return employee.getSalary() * (notOfDaysInYear / 52);
			}
			return employee.getSalary();
		case EmployeeCompsensation.PAY_TYPE_MILES:
		case EmployeeCompsensation.PAY_TYPE_PER_WORK:
			return payStructureItem.getAttendance()[2] * employee.getSalary();
		default:
			return 0.0;
		}
	}

	@Override
	public void selfValidate() throws AccounterException {

	}

}
