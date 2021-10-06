package com.vimukti.accounter.developer.api.report;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.tools.ant.filters.StringInputStream;

import com.nitya.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.developer.api.core.AbstractTest;

public class CrudTest extends AbstractTest {
	private String path;

	@Override
	public void test() throws Exception {
		path = "/company/api/xml/crud";

		// Create Customer
		createCustomer();

		// get Customer
		getCustomer();

		// Update Customer name
		updateCustomer();

		// Check Customer
		getCustomer();

		// Delete Customer
		deleteCustomer();
	}

	private void deleteCustomer() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", "customer");
		map.put("id",
				Long.toString(((ClientCustomer) result.getResult()).getID()));
		String queryStr = getQueryString(map);
		DeleteMethod prepareMethod = prepareDeleteMethod(path, queryStr);

		result = executeMethod(prepareMethod);
		eq(result.getStatus(), 200);
	}

	@SuppressWarnings("deprecation")
	private void updateCustomer() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", "customer");
		map.put("id",
				Long.toString(((ClientCustomer) result.getResult()).getID()));
		String queryStr = getQueryString(map);
		String customerXml = "	<Customer><name>API TEST CUSTOMER UPDATED</name></Customer>";
		PostMethod prepareMethod = preparePostMethod(path, queryStr);
		prepareMethod.setRequestBody(new StringInputStream(customerXml));
		result = executeMethod(prepareMethod);
		eq(result.getStatus(), 200);
		notNull(result.getResult());
	}

	private void getCustomer() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", "customer");
		map.put("id", result.getResult().toString());
		String queryStr = getQueryString(map);
		GetMethod getMethod = prepareGetMethod(path, queryStr);
		result = executeMethod(getMethod);
		eq(result.getStatus(), 200);
		notNull(result.getResult());
		System.out.println(((ClientCustomer) result.getResult()).getName());
	}

	@SuppressWarnings("deprecation")
	private void createCustomer() throws Exception {
		String customerXml = "	<Customer><name>API TEST CUSTOMER</name><currencyFactor>1.0</currencyFactor></Customer>";
		Map<String, String> map = new HashMap<String, String>();
		String queryStr = getQueryString(map);
		PutMethod putMethod = preparePutMethod(path, queryStr);
		putMethod.setRequestBody(new StringInputStream(customerXml));
		result = executeMethod(putMethod);
		eq(result.getStatus(), 200);
	}
}
