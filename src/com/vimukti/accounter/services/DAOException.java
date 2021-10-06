package com.vimukti.accounter.services;

/**
 * 
 * @author pineapple
 *
 */
public class DAOException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int INVALID_REQUEST_EXCEPTION = 1;

	public static final int DATABASE_EXCEPTION = 2;
	
	public static final int RANGE_EXCEED_EXCEPTION = 3;

	private int exceptionType;

	private Exception databaseException;

	private String message;

	public DAOException(int exceptionType, Exception databaseException) {

		this.exceptionType = exceptionType;
		this.databaseException = databaseException;
		if (this.databaseException != null) {
			this.databaseException.printStackTrace();
		}
	}

	public DAOException(int DAOExceptionType, Exception databaseException,
			String message) {

		this.exceptionType = DAOExceptionType;
		this.databaseException = databaseException;
		this.message = message;

	}

	public int getExceptionType() {
		return exceptionType;
	}

	public Exception getDatabaseException() {
		return databaseException;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
