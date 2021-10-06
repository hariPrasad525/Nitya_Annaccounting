/**
 * 
 */
package com.nitya.accounter.web.client.ui.core;

/**
 * @author Fernandez
 * 
 */
public class InvalidTransactionEntryException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private Throwable cause;

	private String message;

	/**
	 * 
	 */
	public InvalidTransactionEntryException() {
		cause = this;
	}

	/**
	 * @param arg0
	 */
	public InvalidTransactionEntryException(String arg0) {
		super(arg0);
		cause = this;
		message = arg0;

	}

	/**
	 * @param arg0
	 */
	public InvalidTransactionEntryException(Throwable arg0) {
		super(arg0);
		cause = arg0;
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public InvalidTransactionEntryException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		message = arg0;
		cause = arg1;
	}

	@Override
	public String getMessage() {
		if (message == null)
			return super.getMessage();
		else
			return String.valueOf(message);
	}

}
