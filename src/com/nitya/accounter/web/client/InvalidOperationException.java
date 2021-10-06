/**
 * 
 */
package com.nitya.accounter.web.client;

import com.nitya.accounter.web.client.core.IAccounterCore;

/**
 * @author vimukti5
 * 
 */
public class InvalidOperationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidOperationException(String mesge) {
		this.detailedMessage = mesge;
	}

	public static final int CREATE_FAILED = 777;
	public static final int UPDATE_FAILED = 888;
	public static final int DELETE_FAILED = 999;

	public int status;

	private String detailedMessage;
	private long id;

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;

		switch (status) {
		case CREATE_FAILED:
			this.detailedMessage = "Create Failed!";
			break;

		case UPDATE_FAILED:
			this.detailedMessage = "Update Failed";
			break;
		case DELETE_FAILED:
			this.detailedMessage = "Delete Failed";
			break;
		default:
			this.detailedMessage = "Failed";
			break;
		}
	}

	/**
	 * @return the detailedMessage
	 */
	public String getDetailedMessage() {
		return detailedMessage;
	}

	/**
	 * @param detailedMessage
	 *            the detailedMessage to set
	 */
	public void setDetailedMessage(String detailedMessage) {
		if (this.detailedMessage == null)
			this.detailedMessage = new String(detailedMessage);
		else
			this.detailedMessage = this.detailedMessage.concat(" "
					+ detailedMessage);

	}

	public InvalidOperationException(int status, long id,
			IAccounterCore serverObject) {
		super();

		setStatus(status);

		this.id = id;

		if (serverObject != null) {
			this.detailedMessage = detailedMessage
					+ serverObject.getObjectType().getServerClassSimpleName();
		}

	}

	public InvalidOperationException() {
	}

}
