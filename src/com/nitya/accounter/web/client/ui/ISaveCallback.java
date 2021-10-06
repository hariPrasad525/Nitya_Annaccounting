/**
 * 
 */
package com.nitya.accounter.web.client.ui;

import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;

/**
 * @author Prasanna Kumar G
 * 
 */
public interface ISaveCallback {

	public void saveFailed(AccounterException exception);

	public void saveSuccess(IAccounterCore object);

}
