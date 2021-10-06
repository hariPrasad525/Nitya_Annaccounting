package com.nitya.accounter.web.client.core;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PaginationList<T> extends ArrayList<T> implements IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int totalCount;
	private int start;

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}
}
