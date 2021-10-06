package com.nitya.accounter.web.client.core;

public class BooleanReportInput implements ReportInput {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean value;

	public BooleanReportInput() {
		// TODO Auto-generated constructor stub
	}

	public BooleanReportInput(boolean input) {
		this.value = input;
	}

	/**
	 * @return the value
	 */
	public boolean getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(boolean value) {
		this.value = value;
	}

}
