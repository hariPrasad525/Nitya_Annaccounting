/**
 * 
 */
package com.nitya.accounter.web.client.ui;

import com.nitya.accounter.web.client.core.IAccounterCore;

/**
 * @author K.Sandeep Sagar
 * 
 */
public class HistoryTokenUtils {

	public String getTokenWithID(String historyToken, IAccounterCore object) {
		StringBuffer buffer = new StringBuffer(historyToken);
		if (object != null) {
			buffer.append('?');
			buffer.append(object.getObjectType().toString().toLowerCase());
			buffer.append(':');
			buffer.append(object.getID());
		}
		return buffer.toString();
	}

}
