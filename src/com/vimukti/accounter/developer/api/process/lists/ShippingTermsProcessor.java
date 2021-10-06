package com.vimukti.accounter.developer.api.process.lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.ShippingTerms;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;

public class ShippingTermsProcessor extends ListProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		ClientConvertUtil util = new ClientConvertUtil();
		List<ClientShippingTerms> list = new ArrayList<ClientShippingTerms>();
		Set<ShippingTerms> groups = getCompany().getShippingTerms();
		for (ShippingTerms customerGroup : groups) {
			ClientShippingTerms object = util.toClientObject(customerGroup,
					ClientShippingTerms.class);
			list.add(object);
		}
		sendResult(list);
	}

}