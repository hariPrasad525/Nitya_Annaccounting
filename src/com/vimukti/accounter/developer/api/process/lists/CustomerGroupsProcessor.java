package com.vimukti.accounter.developer.api.process.lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.CustomerGroup;
import com.vimukti.accounter.web.client.core.ClientCustomerGroup;

public class CustomerGroupsProcessor extends ListProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		ClientConvertUtil util = new ClientConvertUtil();
		List<ClientCustomerGroup> list = new ArrayList<ClientCustomerGroup>();
		Set<CustomerGroup> groups = getCompany().getCustomerGroups();
		for (CustomerGroup customerGroup : groups) {
			ClientCustomerGroup object = util.toClientObject(customerGroup,
					ClientCustomerGroup.class);
			list.add(object);

		}
		sendResult(list);
	}

}
