package com.nitya.accounter.web.client.data;

import com.google.gwt.user.client.rpc.IsSerializable;

public class InvalidSessionException extends Exception implements
		IsSerializable, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String error;

	public InvalidSessionException() {
		super();
	}

	public InvalidSessionException(String arg0) {
		super(arg0);
		this.error = arg0;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getError() {
		return error;
	}

}
