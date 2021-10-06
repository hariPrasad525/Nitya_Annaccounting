/**
 * 
 */
package com.nitya.accounter.web.client.ui.core;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.nitya.accounter.web.client.Global;

/**
 * @author Fernandez
 * 
 */
public class InvalidEntryException extends Exception {

	private static final long serialVersionUID = 1L;

	IsSerializable record;
	// ListGridRecord record;

	Throwable cause;

	private String message;

	/**
	 * 
	 */
	public InvalidEntryException() {
		cause = this;
	}

	/**
	 * @param message
	 */
	public InvalidEntryException(String message) {
		super(message);
		this.message = message;
		cause = this;

	}

	/**
	 * @param cause
	 */
	public InvalidEntryException(Throwable cause) {
		super(cause);
		this.cause = cause;

	}

	/**
	 * @param message
	 * @param cause
	 */
	public InvalidEntryException(String message, Throwable cause) {
		super(message, cause);
		this.message = message;
		this.cause = cause;

	}

	InvalidEntryException(IsSerializable record, String message) {
		super(message);
		this.record = record;
		this.message = message;
		this.cause = this;
	}

	// InvalidEntryException(ListGridRecord record, String message) {
	// super(message);
	// this.record = record;
	// this.message = message;
	// this.cause = this;
	// }

	@Override
	public String getMessage() {
		if (this.message != null)
			return super.getMessage();
		else
			return Global.get().messages().invalidEntry() + this.message;
	}
}
