package com.vimukti.accounter.ws;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.eclipse.jetty.io.ByteArrayBuffer;
import org.eclipse.jetty.server.LocalConnector;
import org.eclipse.jetty.websocket.WebSocket.OnTextMessage;

class AccounterWebSocket implements OnTextMessage {

	Logger log = Logger.getLogger(AccounterWebSocket.class);

	private static final int MAX_MESSAGE = 16000;

	private static final String MESSAGE_END = "#*";

	/**
	 * 
	 */
	private final WebSocketHandler webSocketHandler;

	private HashMap<Integer, String> message = new HashMap<Integer, String>();

	/**
	 * @param webSocketHandler
	 */
	AccounterWebSocket(WebSocketHandler webSocketHandler) {
		this.webSocketHandler = webSocketHandler;
	}

	private Connection connection;

	@Override
	public void onClose(int closeCode, String message) {
		this.webSocketHandler.users.remove(this);

	}

	@Override
	public void onOpen(Connection connection) {
		this.webSocketHandler.users.add(this);
		this.connection = connection;

	}

	@Override
	public void onMessage(String req) {
		LocalConnector connector = webSocketHandler.getConnector();
		try {
			int index = req.indexOf(':');
			if (index == -1) {
				return;
			}
			int id = Integer.parseInt(req.substring(0, index));

			req = req.substring(index + 1);

			String fullMessage = "";
			if (message.get(id) != null) {
				fullMessage = message.get(id);
			}

			if (req.length() > MAX_MESSAGE && req.endsWith(MESSAGE_END)) {
				fullMessage += req.substring(0, req.length() - 2);
				message.put(id, fullMessage);
			} else {
				fullMessage += req;
				log.info("Got message from WebSocket. Processing it...");
				ByteArrayBuffer responses = connector.getResponses(
						new ByteArrayBuffer(fullMessage, "UTF-8"), true);
				String resp = responses.toString("UTF-8");

				connection.sendMessage(id + ":" + resp);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}