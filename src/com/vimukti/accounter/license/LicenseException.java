package com.vimukti.accounter.license;

public class LicenseException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LicenseException() {
	}

	public LicenseException(String message) {
		super(message);
	}

	public LicenseException(String message, Throwable cause) {
		super(message, cause);
	}

	public LicenseException(Throwable cause) {
		super(cause);
	}

}
