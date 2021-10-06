package com.vimukti.api.process;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.LinkedBlockingQueue;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.FileRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.client.methods.HttpPost;
import org.apache.tools.ant.filters.StringInputStream;
import org.mortbay.util.UrlEncoded;

import com.vimukti.accounter.developer.api.ApiSerializationFactory;
import com.vimukti.accounter.developer.api.core.ApiResult;
import com.vimukti.api.core.ApiCallback;
import com.vimukti.api.core.ApiRequest;
import com.vimukti.api.crud.RequestUtil;

public class RequestPeocesser extends Thread {
	private static final String SIGNATURE = "Signature";
	private static final String ALGORITHM = "hmacSHA256";
	private static final String HOST = "http://local.annaccounting.com";

	private static RequestPeocesser instance;
	private LinkedBlockingQueue<ApiRequest<?>> requests = new LinkedBlockingQueue<ApiRequest<?>>();
	private HttpClient client;

	private RequestPeocesser() {
	}

	@Override
	public void run() {
		createConnection();

		while (true) {
			try {
				ApiRequest<?> request = requests.take();
				process(request);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void createConnection() {
		client = new HttpClient();
		client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
	}

	private void process(ApiRequest<?> request) throws HttpException,
			IOException {
		HttpMethod method = prepareMethpd(request);
		ApiResult result = null;
		try {
			result = executeMethod(method, request.getSerializationType());
		} catch (Exception e) {
			e.printStackTrace();
			result = new ApiResult();
			result.setStatus(ApiResult.FAIL);
			result.setResult(e.getMessage());
		}

		sendResult(result, request.getCallback());
	}

	@SuppressWarnings("unchecked")
	private <T> void sendResult(ApiResult result, ApiCallback<T> callback) {
		if (callback == null) {
			return;
		}
		if (result.getStatus() == ApiResult.FAIL) {
			callback.onFail(result.getResult() == null ? "" : result
					.getResult().toString());
		} else {
			callback.onSuccess((T) result.getResult());
		}

	}

	protected ApiResult executeMethod(HttpMethod method, int type)
			throws Exception {
		ApiResult apiResult = null;
		int statusCode = client.executeMethod(method);
		if (statusCode != HttpStatus.SC_OK) {
			apiResult = new ApiResult();
			apiResult.setStatus(ApiResult.FAIL);
			apiResult.setResult("Exception in request. status code is "
					+ statusCode);
			return apiResult;
		}
		ApiSerializationFactory apiSerializationFactory = RequestUtil
				.getSerializer(type);
		apiResult = apiSerializationFactory.deserializeApiResult(method
				.getResponseBodyAsStream());
		return apiResult;
	}

	@SuppressWarnings("deprecation")
	private HttpMethod prepareMethpd(ApiRequest<?> request) {
		String path = HOST + request.getPath();
		HttpMethod method = null;
		switch (request.getMethod()) {
		case ApiRequest.METHOD_GET:
			method = new GetMethod(path);
			break;
		case ApiRequest.METHOD_POST:
			PostMethod postMethod = new PostMethod(path);
			File f = request.getFile();
			if (f != null) {
				StringRequestEntity entity = new StringRequestEntity(
						request.getXml());
				postMethod.setRequestEntity(new FileRequestEntity(f,
						"multipart/form-data;boundary=7d41e5b3904fc"));
				postMethod.addRequestHeader("Content-length", "" + f.length());
			} else {
				postMethod.setRequestBody(getInputStream(request.getXml()));
			}
			method = postMethod;
			break;
		case ApiRequest.METHOD_PUT:
			PutMethod putMethod = new PutMethod(path);
			putMethod.setRequestBody(getInputStream(request.getXml()));
			method = putMethod;
			break;
		case ApiRequest.METHOD_DELETE:
			method = new DeleteMethod(path);
			break;
		default:
			break;
		}

		String encodeString = new UrlEncoded(request.getQueryString()).encode();
		String signature = doSigning(encodeString, request.getSecretKey());
		method.setQueryString(signature);
		return method;
	}

	private InputStream getInputStream(String xml) {
		return new StringInputStream(xml);
	}

	private String doSigning(String url, String secretKey) {
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

	protected String getURLEncode(String string) {
		try {
			return URLEncoder.encode(string, "utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return string;
	}

	public static RequestPeocesser getInstance() {
		if (instance == null) {
			instance = new RequestPeocesser();
		}
		return instance;
	}

	public static void add(ApiRequest<?> request) {
		getInstance().addRequest(request);
	}

	public void addRequest(ApiRequest<?> request) {
		requests.add(request);
	}
}
