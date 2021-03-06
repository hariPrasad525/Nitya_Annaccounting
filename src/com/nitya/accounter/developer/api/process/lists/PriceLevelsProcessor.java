package com.nitya.accounter.developer.api.process.lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.core.ClientConvertUtil;
import com.nitya.accounter.core.PriceLevel;
import com.nitya.accounter.web.client.core.ClientPriceLevel;

public class PriceLevelsProcessor extends ListProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		ClientConvertUtil util = new ClientConvertUtil();
		List<ClientPriceLevel> list = new ArrayList<ClientPriceLevel>();
		Set<PriceLevel> groups = getCompany().getPriceLevels();
		for (PriceLevel customerGroup : groups) {
			ClientPriceLevel object = util.toClientObject(customerGroup,
					ClientPriceLevel.class);
			list.add(object);
		}
		sendResult(list);
	}

}