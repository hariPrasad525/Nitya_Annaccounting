/**
 * 
 */
package com.vimukti.accounter.web.client.ui.core;


/**
 * @author Fernandez
 * 
 */
public class AccounterListGridRecord<T>  {

	public AccounterListGridRecord(T element) {
		this.element = element;
	}

	T element;

	public AccounterListGridRecord<T> getRecord() {
		return this;
	}

	public T getElement() {
		return element;
	}



}
