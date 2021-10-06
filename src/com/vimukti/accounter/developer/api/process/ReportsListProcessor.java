package com.vimukti.accounter.developer.api.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.developer.api.core.ApiProcessor;

public class ReportsListProcessor extends ApiProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		Map<String, List<String>> reportsList = new HashMap<String, List<String>>();
		sendResult(reportsList);
	}
}