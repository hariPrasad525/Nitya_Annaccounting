package com.nitya.accounter.web.server.translate;

import java.io.Serializable;

import com.nitya.accounter.core.Client;

public class Vote implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int id;

	LocalMessage localMessage;

	Client client;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalMessage getLocalMessage() {
		return localMessage;
	}

	public void setLocalMessage(LocalMessage localMessage) {
		this.localMessage = localMessage;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client cliet) {
		this.client = cliet;
	}
}
