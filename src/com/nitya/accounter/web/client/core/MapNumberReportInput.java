package com.nitya.accounter.web.client.core;

import java.util.Map;

public class MapNumberReportInput implements ReportInput {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<? extends Number, ? extends Number> value;

	public MapNumberReportInput() {
		// TODO Auto-generated constructor stub
	}

	public MapNumberReportInput(Map<? extends Number, ? extends Number> map) {
		this.value = map;
	}

	/**
	 * @return the value
	 */
	public Map<? extends Number, ? extends Number> getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(Map<? extends Number, ? extends Number> value) {
		this.value = value;
	}

}
