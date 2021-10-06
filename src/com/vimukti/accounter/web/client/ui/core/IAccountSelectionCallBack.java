/**
 * 
 */
package com.vimukti.accounter.web.client.ui.core;

import com.vimukti.accounter.web.client.core.ClientAccount;

/**
 * @author Fernandez
 * 
 */
public interface IAccountSelectionCallBack {

	void accountSelected(ClientAccount account, Double amount);

}
