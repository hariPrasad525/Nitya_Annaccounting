/**
 * 
 */
package com.nitya.accounter.web.client.ui.core;

/**
 * @author Fernandez
 * 
 */
public interface IAccounterList<T> {

	void updateInGrid(T objectTobeModified);

	void deleteFromGrid(T objectToBeRemoved);
	
	void addToGrid(T objectToBeAdded);
	
}
