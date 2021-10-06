/**
 * 
 */
package com.nitya.accounter.core;


/**
 * @author Prasanna Kumar G
 * 
 */
public class ServerMaintanance {

	private long id;

	private boolean isUnderMaintanance;

	/**
	 * @return the isUnderMaintanance
	 */
	public boolean isUnderMaintanance() {
		return isUnderMaintanance;
	}

	/**
	 * @param isUnderMaintanance
	 *            the isUnderMaintanance to set
	 */
	public void setUnderMaintanance(boolean isUnderMaintanance) {
		this.isUnderMaintanance = isUnderMaintanance;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
}
