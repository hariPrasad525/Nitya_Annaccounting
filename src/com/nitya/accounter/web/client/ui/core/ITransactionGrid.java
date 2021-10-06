/**
 * 
 */
package com.nitya.accounter.web.client.ui.core;

import java.util.List;

import com.nitya.accounter.web.client.core.IAccounterCore;

/**
 * @author Fernandez
 * 
 */
public interface ITransactionGrid {

	void disableUserEntry();

	void enableUserEntry();

	List<? extends IAccounterCore> getEntries();

	void setEntries(List<? extends IAccounterCore> entries);

}
