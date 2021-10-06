package com.nitya.accounter.web.client;

import java.io.Serializable;

public class ClientLocalMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long id;

	String value;

	String createBy;

	int votes;

	boolean isApproved;

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

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public int getVotes() {
		return votes;
	}

	public void setVotes(int ups) {
		this.votes = ups;
	}

	public boolean isApproved() {
		return isApproved;
	}

	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}

}
