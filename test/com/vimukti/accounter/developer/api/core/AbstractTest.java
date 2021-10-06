package com.vimukti.accounter.developer.api.core;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.mortbay.util.UrlEncoded;

import com.nitya.accounter.developer.api.ApiSerializationFactory;
import com.nitya.accounter.developer.api.core.ApiResult;

public abstract class AbstractTest implements ITest {
	private static final String SIGNATURE = "Signature";
	private static final String ALGORITHM = "hmacSHA256";
	private static final String secretKey = "x4c2jjr7jgruqvkh";
	protected static final String apikey = "t5zkba6c";
	protected static String HOST = "http://local.annaccounting.com";
	private static final String DATE_FORMAT = "yyyy.MM.dd G 'at' HH:mm:ss z";

	protected SimpleDateFormat simpleDateFormat;
	private HttpClient client;
	protected long companyId = 1;
	public ApiResult result;

	public AbstractTest() {
		simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
	}

	@Override
	public void before() throws Exception {
		client = new HttpClient();
		client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		companyId = getCompanyId();
	}

	protected void isNull(Object o) throws Exception {
		if (o != null) {
			throw new Exception("is Null?	o=" + o);
		}
	}

	protected void notNull(Object o) throws Exception {
		if (o == null) {
			throw new Exception("is Not Null?	o=" + o);
		}
	}

	protected void isTrue(boolean b) throws Exception {
		if (!b) {
			throw new Exception("is True?	b=" + b);
		}
	}

	protected void isTrue(Object b) throws Exception {
		isTrue(Boolean.parseBoolean(b.toString()));
	}

	protected void isFalse(Object b) throws Exception {
		isTrue(Boolean.parseBoolean(b.toString()));
	}

	protected void isFalse(boolean b) throws Exception {
		isTrue(!b);
	}

	protected void eq(Object o1, Object o2) throws Exception {
		if (o1 != o2) {
			if (o1 == null || !o1.equals(o2)) {
				throw new Exception("is Equal?	o1=" + o1 + ";o2=" + o2);
			}
		}
	}

	protected void notEq(Object o1, Object o2) throws Exception {
		if (o1 == o2) {
			throw new Exception("is Not Equal?	o1=" + o1 + ";o2=" + o2);
		}
		if (o1 == null) {
			return;
		}
		if (o1.equals(o2)) {
			throw new Exception("is Not Equal?	 o1=" + o1 + ";o2=" + o2);
		}
	}

	protected String getURLEncode(String string) {
		try {
			return URLEncoder.encode(string, "utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return string;
	}

	private String doSigning(String url) {
		byte[] secretKeyBytes = secretKey.getBytes();
		SecretKeySpec keySpec = new SecretKeySpec(secretKeyBytes, ALGORITHM);
		try {
			Mac mac = Mac.getInstance(ALGORITHM);
			mac.init(keySpec);
			byte[] doFinal = mac.doFinal(url.getBytes());
			String string = new String(doFinal);
			String urlEncode = getURLEncode(string);
			url += "&" + SIGNATURE + "=" + urlEncode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}

	protected GetMethod prepareGetMethod(String path, String queryStr) {
		GetMethod method = new GetMethod(HOST + path);
		prepareMethod(method, queryStr);
		return method;
	}

	protected PostMethod preparePostMethod(String path, String queryStr) {
		PostMethod method = new PostMethod(HOST + path);
		prepareMethod(method, queryStr);
		return method;
	}

	protected PutMethod preparePutMethod(String path, String queryStr) {
		PutMethod method = new PutMethod(HOST + path);
		prepareMethod(method, queryStr);
		return method;
	}

	protected DeleteMethod prepareDeleteMethod(String path, String queryStr) {
		DeleteMethod method = new DeleteMethod(HOST + path);
		prepareMethod(method, queryStr);
		return method;
	}

	private void prepareMethod(HttpMethod method, String queryStr) {
		String encodeString = new UrlEncoded(queryStr).encode();
		String signature = doSigning(encodeString);
		method.setQueryString(signature);
	}

	protected ApiResult executeMethod(HttpMethod method) throws Exception {
		int statusCode = client.executeMethod(method);
		eq(HttpStatus.SC_OK, statusCode);
		ApiSerializationFactory apiSerializationFactory = new ApiSerializationFactory(
				false);
		ApiResult apiResult = apiSerializationFactory
				.deserializeApiResult(method.getResponseBodyAsStream());
		return apiResult;
	}

	protected String getQueryString(Map<String, String> params) {
		params.put("ApiKey", apikey);
		params.put("CompanyId", Long.toString(companyId));
		params.put("Expire", simpleDateFormat.format(new Date()));

		// if (params.isEmpty()) {
		// return "";
		// }
		Set<Entry<String, String>> entrySet = params.entrySet();
		StringBuilder builder = new StringBuilder();
		for (Entry<String, String> entry : entrySet) {
			builder.append(entry.getKey());
			builder.append("=");
			builder.append(getURLEncode(entry.getValue()));
			builder.append("&");
		}
		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}

	protected long getCompanyId() throws Exception {
		// Map<String, String> params = new HashMap<String, String>();
		// params.put("ApiKey", apikey);
		// params.put("Expire", simpleDateFormat.format(new Date()));
		// String queryStr = getQueryString(params);
		// GetMethod prepareMethod = prepareGetMethod("" + "companyids",
		// queryStr);
		// int statusCode = executeMethod(prepareMethod);
		// eq(HttpStatus.SC_OK, statusCode);
		// System.out.println(prepareMethod.getResponseBodyAsString());
		return 1L;
	}

	@Override
	public ApiResult getResult() {
		return result;
	}
}
