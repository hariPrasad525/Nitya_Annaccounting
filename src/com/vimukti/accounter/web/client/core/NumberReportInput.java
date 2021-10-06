package com.vimukti.accounter.web.client.core;

public class NumberReportInput implements ReportInput {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Number value;

	public NumberReportInput() {
		// TODO Auto-generated constructor stub
	}

	public NumberReportInput(Number input) {
		this.value = input;
	}

	/**
	 * @return the value
	 */
	public Number getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(Number value) {
		this.value = value;
	}

}
