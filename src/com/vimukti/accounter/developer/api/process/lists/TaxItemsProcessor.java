package com.vimukti.accounter.developer.api.process.lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.web.client.core.ClientTAXItem;

public class TaxItemsProcessor extends ListProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		isActive = readBoolean(req, "active");

		ClientConvertUtil util = new ClientConvertUtil();
		List<ClientTAXItem> list = new ArrayList<ClientTAXItem>();
		Set<TAXItem> items = getCompany().getTaxItems();
		for (TAXItem item : items) {
			if (isActive == null || item.isActive() != isActive) {
				continue;
			}
			ClientTAXItem object = util.toClientObject(item,
					ClientTAXItem.class);
			list.add(object);

		}
		sendResult(list);
	}

}
