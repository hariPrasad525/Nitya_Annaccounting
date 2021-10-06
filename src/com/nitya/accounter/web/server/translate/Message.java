package com.nitya.accounter.web.server.translate;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long id;

	String value;

	String comment;

	Set<LocalMessage> localMessages = new HashSet<LocalMessage>();

	Set<Key> keys;

	boolean isNotUsed;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Set<LocalMessage> getLocalMessages() {
		return localMessages;
	}

	public void setLocalMessages(Set<LocalMessage> localMessages) {
		this.localMessages = localMessages;
	}

	public Set<Key> getKeys() {
		return keys;
	}

	public void setKeys(Set<Key> keys) {
		this.keys = keys;
	}

	public boolean isNotUsed() {
		return isNotUsed;
	}

	public void setNotUsed(boolean isNotUsed) {
		this.isNotUsed = isNotUsed;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}

	@Override
	public String toString() {
		return value;
	}
}
