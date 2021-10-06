/**
 * 
 */
package com.nitya.accounter.web.client.ui;

/**
 * @author Prasanna Kumar G
 * 
 */
public interface WidgetWithErrors {

	/**
	 * Clears All Errors
	 */
	public void clearAllErrors();

	/**
	 * Adds Error
	 * 
	 * @param item
	 * @param erroMsg
	 */
	public void addError(Object item, String erroMsg);

	/**
	 * Clears the given Error
	 * 
	 * @param obj
	 */
	public void clearError(Object obj);
}
