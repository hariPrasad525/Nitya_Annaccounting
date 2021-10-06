package com.vimukti.accounter.developer.api.process.lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.ItemGroup;
import com.vimukti.accounter.web.client.core.ClientItemGroup;

public class ItemGroupsProcessor extends ListProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		ClientConvertUtil util = new ClientConvertUtil();
		List<ClientItemGroup> list = new ArrayList<ClientItemGroup>();
		Set<ItemGroup> groups = getCompany().getItemGroups();
		for (ItemGroup customerGroup : groups) {
			ClientItemGroup object = util.toClientObject(customerGroup,
					ClientItemGroup.class);
			list.add(object);
		}
		sendResult(list);

	}
}
