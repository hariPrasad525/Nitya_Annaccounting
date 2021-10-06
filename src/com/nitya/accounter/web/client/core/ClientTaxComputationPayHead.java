package com.nitya.accounter.web.client.core;

public class ClientTaxComputationPayHead extends ClientPayHead {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	ClientLiveTaxRate liveTaxRate;

	public ClientLiveTaxRate getLiveTaxRate() {
		return liveTaxRate;
	}

	public void setLiveTaxRate(ClientLiveTaxRate liveTaxRate) {
		this.liveTaxRate = liveTaxRate;
	}

}
