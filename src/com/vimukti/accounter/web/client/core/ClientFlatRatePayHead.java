package com.vimukti.accounter.web.client.core;

public class ClientFlatRatePayHead extends ClientPayHead {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int calculationPeriod;
	
	private int payType;
	private int payFrequency;

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

	public int getCalculationPeriod() {
		return calculationPeriod;
	}

	public void setCalculationPeriod(int calculationPeriod) {
		this.calculationPeriod = calculationPeriod;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.FLATRATE_PAY_HEAD;
	}
}
