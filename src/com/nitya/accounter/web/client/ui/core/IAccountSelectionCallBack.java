/**
 * 
 */
package com.nitya.accounter.web.client.ui.core;

import com.nitya.accounter.web.client.core.ClientAccount;

/**
 * @author Fernandez
 * 
 */
public interface IAccountSelectionCallBack {

	void accountSelected(ClientAccount account, Double amount);

}
