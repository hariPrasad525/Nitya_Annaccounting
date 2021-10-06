package com.vimukti.accounter.web.client.rpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.Header;

public class WebSocketConnection {

	public static final class Cookie {

		public String value;
		public String name;

	}

	static final WebSocketConnection instance = new WebSocketConnection();
	private static final int MAX_MESSAGE = 16000;
	private static final String MESSAGE_END = "#*";
	private String url;
	int requestCounter = 0;
	int sent = 0;

	private JavaScriptObject jsWebSocket;
	private HashMap<Integer, WebSocketRequest> requests = new HashMap<Integer, WebSocketRequest>();

	boolean isConnected = false;
	List<Cookie> cookiesList = new ArrayList<WebSocketConnection.Cookie>();

	WebSocketConnection() {
		String baseURL = "https://app.annaccounting.com:16443/test";
		int split = baseURL.indexOf('/', 8);
		String urlString = baseURL.substring(0, split).replace("https", "wss")
				.replace("http", "ws");
		this.url = urlString.concat("/main/ws");
		try {
			this.jsWebSocket = createJSWebSocket(url, this);
		} catch (Exception e) {
		}
	}

	public static WebSocketConnection getInstance() {
		return instance;
	}

	public void send(WebSocketRequest wsr) {
		requestCounter++;
		requests.put(requestCounter, wsr);
		if (isConnected) {
			String request = wsr.getRequest();

			while (request.length() > MAX_MESSAGE) {
				String substring = request.substring(0, MAX_MESSAGE);
				substring += MESSAGE_END;
				send(requestCounter + ":" + substring);
				request = request.substring(MAX_MESSAGE);
			}
			send(requestCounter + ":" + request);
			sent++;
		}
	}

	public native void send(String message) /*-{
		if (message == null)
			return;

		this.@com.vimukti.accounter.web.client.rpc.WebSocketConnection::jsWebSocket
				.send(message);
	}-*/;

	/**
	 * Creates the JavaScript WebSocket component and set's all callback
	 * handlers.
	 * 
	 * @param url
	 */
	private native JavaScriptObject createJSWebSocket(final String url,
			final WebSocketConnection webSocket) throws Exception /*-{
		var jsWebSocket = new WebSocket(url);

		jsWebSocket.onopen = function() {
			webSocket.@com.vimukti.accounter.web.client.rpc.WebSocketConnection::onOpen()();
		}

		jsWebSocket.onclose = function() {
			webSocket.@com.vimukti.accounter.web.client.rpc.WebSocketConnection::onClose()();
		}

		jsWebSocket.onerror = function() {
			webSocket.@com.vimukti.accounter.web.client.rpc.WebSocketConnection::onError()();
		}

		jsWebSocket.onmessage = function(socketResponse) {
			if (socketResponse.data) {
				webSocket.@com.vimukti.accounter.web.client.rpc.WebSocketConnection::onMessage(Ljava/lang/String;)(socketResponse.data);
			}
		}

		return jsWebSocket;
	}-*/;

	public native int getReadyState() /*-{
		return this.@com.vimukti.accounter.web.client.rpc.WebSocketConnection::jsWebSocket.readyState;
	}-*/;

	/**
	 * 
	 */
	private void onOpen() {
		for (int x = sent; x < requestCounter; x++) {
			WebSocketRequest request = requests.get(x + 1);
			send(requestCounter + ":" + request.getRequest());
			sent++;
		}
		isConnected = true;
	}

	/**
	 * @param message
	 */
	private void onMessage(String message) {
		int index = message.indexOf(':');
		try {
			int id = Integer.parseInt(message.substring(0, index));
			message = message.substring(index + 1);

			WebSocketRequest request = requests.get(id);
			if (request != null) {
				requests.remove(id);
				WebSocketResponse respone = new WebSocketResponse(message);

				processCookies(respone);
				request.getCallback().onResponseReceived(request, respone);
			}
		} catch (NumberFormatException e) {

		}
	}

	private void processCookies(WebSocketResponse respone) {
		Header[] headers = respone.getHeaders();

		for (Header header : headers) {
			if (header.getName().equals("Set-Cookie")) {
				String cookies = header.getValue();
				if (cookies == null || cookies.isEmpty()) {
					return;
				}
				String[] split = cookies.split(";");
				String[] keyValues = split[0].split("=");

				Cookie cookie = new Cookie();
				cookie.name = keyValues[0];
				cookie.value = keyValues[1];
				int contains = isContains(cookiesList, cookie.name);
				if (contains > 0) {
					cookiesList.remove(contains);
				}
				cookiesList.add(cookie);
			}
		}

	}

	private int isContains(List<Cookie> cookies, String contain) {
		for (Cookie cookie : cookies) {
			if (cookie.name.equals(contain)) {
				return cookies.indexOf(cookie);
			}
		}
		return -1;
	}

	private WebSocketRequest getRequest(int id) {
		return requests.get(id);
	}

	/**
	 * 
	 */
	private void onError() {
		onClose();
	}

	/**
	 * 
	 */
	private void onClose() {
		for (WebSocketRequest request : requests.values()) {
			request.getCallback().onError(request,
					new IOException("Connection lost"));
		}
		requests.clear();
		requestCounter = 0;
		sent = 0;
		// reconnect again
		try {
			this.jsWebSocket = createJSWebSocket(url, this);
		} catch (Exception e) {
		}
	}

	public List<Cookie> getCookies() {
		return cookiesList;
	}
}
