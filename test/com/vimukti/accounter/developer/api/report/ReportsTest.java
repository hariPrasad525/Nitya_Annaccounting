package com.vimukti.accounter.developer.api.report;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.methods.GetMethod;

import com.nitya.accounter.developer.api.core.ApiResult;
import com.vimukti.accounter.developer.api.core.AbstractTest;

public class ReportsTest extends AbstractTest {
	private String path;

	@Override
	public void test() throws Exception {
		path = "/company/api/xml/reports";
		salesbycustomersummary();
		agedcreditors();
	}

	private void salesbycustomersummary() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("reporttype", "salesbycustomersummary");
		String queryStr = getQueryString(map);
		GetMethod prepareMethod = prepareGetMethod(path, queryStr);
		ApiResult result = executeMethod(prepareMethod);
		eq(result.getStatus(), 200);
		System.out.println(result.getResult());
	}

	private void agedcreditors() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("reporttype", "agedcreditors");
		String queryStr = getQueryString(map);
		GetMethod prepareMethod = prepareGetMethod(path, queryStr);
		ApiResult result = executeMethod(prepareMethod);
		eq(result.getStatus(), 200);
		System.out.println(result.getResult());
	}

	@Override
	protected String getQueryString(Map<String, String> params) {
		params.put("StartDate", "2012.02.24 AD at 11:13:56 IST");
		params.put("EndDate", "2013.03.30 AD at 12:13:56 IST");
		return super.getQueryString(params);
	}
}
