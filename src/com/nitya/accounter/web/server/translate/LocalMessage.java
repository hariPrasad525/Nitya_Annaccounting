package com.nitya.accounter.web.server.translate;

import java.io.Serializable;
import java.sql.Date;

import com.nitya.accounter.core.Client;

public class LocalMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long id;

	Message message;

	Language lang;

	String value;

	int votes;

	boolean isApproved;

	Client createdBy;

	Date createdDate;

	public long getId() {
		return id;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public Message getMessage() {
		return message;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Language getLang() {
		return lang;
	}

	public void setLang(Language lang) {
		this.lang = lang;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getUps() {
		return votes;
	}

	public void setUps(int ups) {
		this.votes = ups;
	}

	public boolean isApproved() {
		return isApproved;
	}

	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}

	public Client getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Client createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}
