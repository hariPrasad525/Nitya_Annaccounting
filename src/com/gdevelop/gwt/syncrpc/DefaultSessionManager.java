package com.gdevelop.gwt.syncrpc;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;

/**
 * Implements a default session manager that handles storing its own private
 * sesssion state such as cookies and and login information.
 */
public class DefaultSessionManager implements SessionManager {

	private CookieManager cookieManager = new CookieManager();
	private HashSet<String> loggedIn = new HashSet<String>();

	@Override
	public HttpURLConnection openConnection(URL url) throws Exception {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.addRequestProperty("emailId", "email@example.com");
		cookieManager.setCookies(connection);
		return connection;
	}

	@Override
	public void handleResponseHeaders(HttpURLConnection connection)
			throws IOException {
		cookieManager.storeCookies(connection);
	}

	public CookieManager getCookieManager() {
		return cookieManager;
	}

}
