/**
 * 
 */
package com.vimukti.accounter.mobile;

/**
 * Exception Throws in AccounterMobile
 */
public class AccounterMobileException extends Exception {

	public static final int ERROR_UNKNOWN_COMMAND = 1;
	public static final int ERROR_INTERNAL = 2;
	public static final int ERROR_INVALID_INPUTS = 3;
	private int errorCode;

	public AccounterMobileException(int errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * Creates new Instance
	 */
	public AccounterMobileException(int errorCode, Throwable t) {
		super(t);
		this.errorCode = errorCode;
	}

	/**
	 * Creates new Instance
	 */
	public AccounterMobileException(String message) {
		super(message);
	}

	/**
	 * Creates new Instance
	 */
	public AccounterMobileException(String message, Throwable t) {
		super(message, t);
	}

	/**
	 * Creates new Instance
	 */
	public AccounterMobileException(Throwable t) {
		super(t);
	}

	/**
	 * Creates new Instance
	 */
	public AccounterMobileException() {

	}

	/**
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}

}
