package com.vimukti.accounter.core;

import java.io.Serializable;

import org.json.JSONException;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.IVersionable;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

/**
 * 
 * A Interface Implemented by All Accounter Server Core classes,
 * 
 * @author Fernandez
 * 
 */

public interface IAccounterServerCore extends Serializable, Cloneable,
		IVersionable {

	AccounterMessages accounterMessages = Global.get().messages();

	long getID();

	boolean canEdit(IAccounterServerCore clientObject, boolean goingToBeEdit)
			throws AccounterException;

	void writeAudit(AuditWriter w) throws JSONException;

	/**
	 * This method should validate this class. It throws exception when it
	 * fails.
	 * 
	 * @return
	 * @throws AccounterException
	 */
	public void selfValidate() throws AccounterException;
}
