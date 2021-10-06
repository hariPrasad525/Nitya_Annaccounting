package com.nitya.accounter.web.client.rpc;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.nitya.accounter.web.client.rpc.WebSocketConnection.Cookie;

public class WebSocketRequestBuilder extends RequestBuilder {

	private Map<String, String> headers;

	public WebSocketRequestBuilder(Method httpMethod, String url) {
		super(httpMethod, url);
	}

	public void setHeader(String header, String value) {
		throwIfEmptyOrNull("header", header);
		throwIfEmptyOrNull("value", value);

		if (headers == null) {
			headers = new HashMap<String, String>();
		}

		headers.put(header, value);
	}

	public static void throwIfNull(String name, Object value) {
		if (null == value) {
			throw new NullPointerException(name + " cannot be null");
		}
	}

	public static void throwIfEmptyOrNull(String name, String value) {
		assert (name != null);
		assert (name.trim().length() != 0);

		throwIfNull(name, value);

		if (0 == value.trim().length()) {
			throw new IllegalArgumentException(name + " cannot be empty");
		}
	}

	@Override
	public Request send() throws RequestException {
		if (null == getCallback()) {
			throw new NullPointerException("callback" + " cannot be null");
		}
		return wsDoSend(getRequestData(), getCallback());
	}

	private Request wsDoSend(String requestData, RequestCallback callback) {
		addCookies();
		try {
			setHeader("Content-Length", ""
					+ requestData.getBytes("UTF-8").length);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String request = getHTTPMethod() + " " + getUrl() + " HTTP/1.0\r\n";
		if (headers != null && headers.size() > 0) {
			for (Map.Entry<String, String> header : headers.entrySet()) {
				request += header.getKey() + ": " + header.getValue() + "\r\n";
			}
		} else {
			request += "Content-Type: text/plain; charset=utf-8\r\n";
		}
		request += "\r\n" + requestData;
		WebSocketRequest wsr = new WebSocketRequest(request, callback);

		WebSocketConnection connection = WebSocketConnection.getInstance();
		connection.send(wsr);
		return wsr;
	}

	private void addCookies() {
		String cookies = "";
		for (Cookie cookie : WebSocketConnection.getInstance().getCookies()) {
			if (cookies.length() > 0) {
				cookies += "; ";
			}
			cookies += cookie.name + "=" + cookie.value;
		}
		if (cookies.length() > 0) {
			setHeader("Cookie", cookies);
		}
	}

	@Override
	public Request sendRequest(String requestData, RequestCallback callback)
			throws RequestException {
		if (null == callback) {
			throw new NullPointerException("callback" + " cannot be null");
		}
		return wsDoSend(requestData, callback);
	}

}
