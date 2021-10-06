package com.vimukti.accounter.web.client.core;

public class StringReportInput implements ReportInput {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String value;

	public StringReportInput() {
		// TODO Auto-generated constructor stub
	}

	public StringReportInput(String input) {
		this.value = input;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
