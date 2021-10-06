package com.vimukti.accounter.developer.api.report;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.methods.GetMethod;

import com.vimukti.accounter.developer.api.core.AbstractTest;
import com.vimukti.accounter.developer.api.core.ApiResult;

public class ListsTest extends AbstractTest {
	private String path;

	@Override
	public void test() throws Exception {
		path = "/company/api/xml/lists";
		customers();
	}

	private void customers() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", "invoices");
		map.put("viewType", "open");
		map.put("dateType", "all");
		String queryStr = getQueryString(map);
		GetMethod method = prepareGetMethod(path, queryStr);
		ApiResult result = executeMethod(method);
		eq(result.getStatus(), 200);
		System.out.println(result.getResult());
	}

	@Override
	protected String getQueryString(Map<String, String> params) {
		params.put("start", "0");
		params.put("length", "10");
		return super.getQueryString(params);
	}
}
