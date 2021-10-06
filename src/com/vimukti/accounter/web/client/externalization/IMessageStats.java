package com.vimukti.accounter.web.client.externalization;

import java.util.ArrayList;
import java.util.HashMap;

public interface IMessageStats {

	public ArrayList<String> getMessagesUsageOrder();

	public HashMap<String, Integer> getMessgaesUsageCount();
}
