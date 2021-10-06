package com.vimukti.accounter.developer.api.process.lists;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.web.client.core.Features;

public class UserActivitiesProcessor extends ListProcessor {
	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		checkPermission(Features.USER_ACTIVITY);
		initTransactionList(req, resp);
		int value = readInt(req, "customise", 0);

		List<?> resultList = service.getUsersActivityLog(from, to, start,
				length, value);

		sendResult(resultList);
	}
}
