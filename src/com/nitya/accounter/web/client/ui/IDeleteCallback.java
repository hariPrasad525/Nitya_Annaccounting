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
public interface IDeleteCallback {

	public void deleteFailed(AccounterException caught);

	public void deleteSuccess(IAccounterCore result);
}
