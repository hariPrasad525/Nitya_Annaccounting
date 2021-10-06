package com.nitya.accounter.core;

import java.io.Serializable;

public class Property implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** IDs of Properties */
	public static final long SETUP_PAGE = 1001;

	public static final long SERVER_ID = 1002;

	private long id;

	private String value;

	public Property() {
		// TODO Auto-generated constructor stub
	}

	public Property(long id, String value) {
		this.id = id;
		this.value = value.trim();
	}

	public Property(long id, Number value) {
		this(id, value + "");
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
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
