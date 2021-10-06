package com.vimukti.accounter.developer.api.process.lists;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.core.IAccounterCore;

public class BugetItemsProcessor extends ListProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		checkPermission(Features.BUDGET);
		List<? extends IAccounterCore> resultList = null;
		try {
			resultList = service.getBudgetList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		sendResult(resultList);
	}

}
