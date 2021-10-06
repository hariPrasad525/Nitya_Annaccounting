package com.nitya.accounter.developer.api.process.lists;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.web.client.core.IAccounterCore;

public class WarehouseTransfersProcessor extends ListProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		List<? extends IAccounterCore> resultList = null;
		resultList = service.getWarehouseTransfersList();
		sendResult(resultList);
	}

}
