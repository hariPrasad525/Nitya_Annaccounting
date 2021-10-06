package com.vimukti.accounter.developer.api.process.lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.core.AccounterClass;
import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.web.client.core.ClientAccounterClass;
import com.vimukti.accounter.web.client.core.Features;

public class ClassesProcessor extends ListProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		checkPermission(Features.CLASS);
		ClientConvertUtil util = new ClientConvertUtil();
		List<ClientAccounterClass> list = new ArrayList<ClientAccounterClass>();
		Set<AccounterClass> groups = getCompany().getAccounterClasses();
		for (AccounterClass customerGroup : groups) {
			ClientAccounterClass object = util.toClientObject(customerGroup,
					ClientAccounterClass.class);
			list.add(object);
		}
		sendResult(list);
	}

}
