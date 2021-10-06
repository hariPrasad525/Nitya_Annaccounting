package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;

public class ClientComputionPayHead extends ClientPayHead {

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

	private List<ClientComputationSlab> slabs = new ArrayList<ClientComputationSlab>();

	/**
	 * It exists if ComputaionType is COMPUTATE_ON_SPECIFIED_FORMULA
	 */
	private List<ClientComputaionFormulaFunction> formulaFunctions = new ArrayList<ClientComputaionFormulaFunction>();

	private int calculationPeriod;

	public int getComputationType() {
		return computationType;
	}

	public void setComputationType(int computationType) {
		this.computationType = computationType;
	}

	public List<ClientComputationSlab> getSlabs() {
		return slabs;
	}

	public void setSlabs(List<ClientComputationSlab> slabs) {
		this.slabs = slabs;
	}

	public List<ClientComputaionFormulaFunction> getFormulaFunctions() {
		return formulaFunctions;
	}

	public void setFormulaFunctions(
			List<ClientComputaionFormulaFunction> formulaFunctions) {
		this.formulaFunctions = formulaFunctions;
	}

	public int getCalculationPeriod() {
		return calculationPeriod;
	}

	public void setCalculationPeriod(int calculationPeriod) {
		this.calculationPeriod = calculationPeriod;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.COMPUTATION_PAY_HEAD;
	}

	public static String getComputationType(int computationType) {
		AccounterMessages messages = Accounter.getMessages();
		switch (computationType) {
		case COMPUTATE_ON_SUBTOTAL:
			return messages.onSubTotal();

		case COMPUTATE_ON_DEDUCTION_TOTAL:
			return messages.onDeductionTotal();

		case COMPUTATE_ON_EARNING_TOTAL:
			return messages.onEarningTotal();

		case COMPUTATE_ON_SPECIFIED_FORMULA:
			return messages.onSpecifiedFormula();

		default:
			break;
		}
		return null;
	}
}
