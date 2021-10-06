package com.vimukti.accounter.developer.api.process.lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.VendorGroup;
import com.vimukti.accounter.web.client.core.ClientVendorGroup;

public class VendorGroupsProcessor extends ListProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		ClientConvertUtil util = new ClientConvertUtil();
		List<ClientVendorGroup> list = new ArrayList<ClientVendorGroup>();
		Set<VendorGroup> groups = getCompany().getVendorGroups();
		for (VendorGroup customerGroup : groups) {
			ClientVendorGroup object = util.toClientObject(customerGroup,
					ClientVendorGroup.class);
			list.add(object);

		}
		sendResult(list);
	}

}