package com.nitya.accounter.web.server.translate;

import java.io.Serializable;

public class Key implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	String key;

	private int usageCount;

	private int usageOrder;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the usageCount
	 */
	public int getUsageCount() {
		return usageCount;
	}

	/**
	 * @param usageCount
	 *            the usageCount to set
	 */
	public void setUsageCount(int usageCount) {
		this.usageCount = usageCount;
	}

	/**
	 * @return the usageOrder
	 */
	public int getUsageOrder() {
		return usageOrder;
	}

	/**
	 * @param usageOrder
	 *            the usageOrder to set
	 */
	public void setUsageOrder(int usageOrder) {
		this.usageOrder = usageOrder;
	}

	@Override
	public boolean equals(Object obj) {
		Key key = (Key) obj;
		if (key == null) {
			return false;
		}
		return this.key.equals(key.key);
	}

	@Override
	public String toString() {
		return key;
	}
}
