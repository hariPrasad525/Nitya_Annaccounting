package com.vimukti.accounter.developer.api.process.lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.ShippingMethod;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;

public class ShippingMethodsProcessor extends ListProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		ClientConvertUtil util = new ClientConvertUtil();
		List<ClientShippingMethod> list = new ArrayList<ClientShippingMethod>();
		Set<ShippingMethod> groups = getCompany().getShippingMethods();
		for (ShippingMethod customerGroup : groups) {
			ClientShippingMethod object = util.toClientObject(customerGroup,
					ClientShippingMethod.class);
			list.add(object);
		}
		sendResult(list);
	}

}