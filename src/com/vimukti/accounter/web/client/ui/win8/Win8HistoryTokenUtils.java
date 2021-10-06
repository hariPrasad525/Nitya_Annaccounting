package com.vimukti.accounter.web.client.ui.win8;

import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.HistoryTokenUtils;

public class Win8HistoryTokenUtils extends HistoryTokenUtils {

	public String getTokenWithID(String historyToken, IAccounterCore object) {
		StringBuffer buffer = new StringBuffer(historyToken);
		if (object != null) {
			buffer.append(':');
			buffer.append(object.getObjectType().toString().toLowerCase());
			buffer.append(':');
			buffer.append(object.getID());
		}
		return buffer.toString();
	}
}
