package com.nitya.accounter.web.client.core;

import com.google.gwt.user.client.rpc.IsSerializable;

public class StartupException extends Exception implements IsSerializable {

	public StartupException(String message) {
		super(message);
	}

	public StartupException() {
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
