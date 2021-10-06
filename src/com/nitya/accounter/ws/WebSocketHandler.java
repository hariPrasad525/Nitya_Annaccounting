package com.nitya.accounter.ws;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.LocalConnector;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

public class WebSocketHandler extends WebSocketServlet {

	private LocalConnector connector;

	
	@Override
	public void init(ServletConfig config) throws ServletException {
		ServletContext ct=config.getServletContext();
		this.connector = (LocalConnector) ct.getAttribute("wsConnector");
		super.init(config);
	}
	Set<AccounterWebSocket> users = new HashSet<AccounterWebSocket>();
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest request,
			String protocol) {
		return new AccounterWebSocket(this);
	}


	public LocalConnector getConnector() {
		return this.connector;
	}

}
