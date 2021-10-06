/**
 * 
 */
package com.vimukti.accounter.web.client.ui;

import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * @author Prasanna Kumar G
 * 
 */
public interface ISaveCallback {

	public void saveFailed(AccounterException exception);

	public void saveSuccess(IAccounterCore object);

}
