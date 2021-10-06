package com.vimukti.accounter.developer.api.process.lists;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.web.client.core.IAccounterCore;

public class WarehousesProcessor extends ListProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		List<? extends IAccounterCore> resultList = null;
		resultList = service.getWarehouses();
		sendResult(resultList);
	}

}
