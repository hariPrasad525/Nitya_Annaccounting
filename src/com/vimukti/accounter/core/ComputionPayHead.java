package com.vimukti.accounter.core;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientEmployeePayHeadComponent;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * As Computed Value is used if the Pay Head value is based on a dependent
 * component. You will be able to define the dependent component by specifying
 * the formula or using the current sub-total or current earning or deduction
 * total. You can also define the slab either by percentage to value or the
 * combination in a slab.
 * 
 * @author Prasanna Kumar G
 * 
 */
public class ComputionPayHead extends PayHead {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Calculation Types */
	public static final int COMPUTATE_ON_DEDUCTION_TOTAL = 1;
	public static final int COMPUTATE_ON_EARNING_TOTAL = 2;
	public static final int COMPUTATE_ON_SUBTOTAL = 3;
	public static final int COMPUTATE_ON_SPECIFIED_FORMULA = 4;
	

	private int computationType;

	private List<ComputationSlab> slabs = new ArrayList<ComputationSlab>();

	/**
	 * It exists if ComputaionType is COMPUTATE_ON_SPECIFIED_FORMULA
	 */
	private List<ComputaionFormulaFunction> formulaFunctions;

	private int calculationPeriod;

	public ComputionPayHead() {
		super(CALCULATION_TYPE_AS_COMPUTED_VALUE);
	}

	/**
	 * @return the computationType
	 */
	public int getComputationType() {
		return computationType;
	}

	/**
	 * @param computationType
	 *            the computationType to set
	 */
	public void setComputationType(int computationType) {
		this.computationType = computationType;
	}

	/**
	 * @return the slabs
	 */
	public List<ComputationSlab> getSlabs() {
		return slabs;
	}

	/**
	 * @param slabs
	 *            the slabs to set
	 */
	public void setSlabs(List<ComputationSlab> slabs) {
		this.slabs = slabs;
	}

	/**
	 * @return the formulaFunctions
	 */
	public List<ComputaionFormulaFunction> getFormulaFunctions() {
		return formulaFunctions;
	}

	/**
	 * @param formulaFunctions
	 *            the formulaFunctions to set
	 */
	public void setFormulaFunctions(
			List<ComputaionFormulaFunction> formulaFunctions) {
		this.formulaFunctions = formulaFunctions;
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
	public double calculatePayment(ClientEmployeePayHeadComponent component, PayStructureItem payStructureItem,
			double deductions, double earnings) {
		double basicSal = payStructureItem.getRate();
		double calculatedAmount = 0.0D;
		if (computationType == COMPUTATE_ON_SPECIFIED_FORMULA) {
			for (ComputaionFormulaFunction function : getFormulaFunctions()) {
				double amount = function.calculatePayment(component, payStructureItem,
						deductions, earnings);
				if (function.getFunctionType() == ComputaionFormulaFunction.FUNCTION_ADD_PAY_HEAD) {
					calculatedAmount += amount;
				} else if (function.getFunctionType() == ComputaionFormulaFunction.FUNCTION_SUBSTRACT_PAY_HEAD) {
					calculatedAmount -= amount;
				} else if (function.getFunctionType() == ComputaionFormulaFunction.FUNCTION_MULTIPLY_ATTENDANCE) {
					calculatedAmount *= amount;
				} else if (function.getFunctionType() == ComputaionFormulaFunction.FUNCTION_DIVIDE_ATTENDANCE) {
					if (amount == 0) {
						// TODO
					} else {
						calculatedAmount /= amount;
					}
				}
			}
		} else if (computationType == COMPUTATE_ON_DEDUCTION_TOTAL) {
			calculatedAmount = deductions;
		} else if (computationType == COMPUTATE_ON_EARNING_TOTAL) {
			calculatedAmount = earnings;
		} else if (computationType == COMPUTATE_ON_SUBTOTAL) {
			calculatedAmount = (basicSal + earnings) - deductions;
		}
		double value = 0.0;
		for (ComputationSlab slab : getSlabs()) {
			if (calculatedAmount >= slab.getFromAmount()
					&& calculatedAmount <= slab.getToAmount()) {
				value = slab.getSlabType() == ComputationSlab.TYPE_PERCENTAGE ? (calculatedAmount * slab
						.getValue()) / 100 : slab.getValue();
			}
		}

		return value;
	}

	@Override
	public void selfValidate() throws AccounterException {
		// TODO Auto-generated method stub

	}
}
