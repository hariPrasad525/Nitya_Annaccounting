package com.vimukti.accounter.core;


public class DeleteReason extends CreatableObject {

	private String emailID;
	private String reason;
	private FinanceDate deletedDate;

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public FinanceDate getDeletedDate() {
		return deletedDate;
	}

	public void setDeletedDate(FinanceDate deletedDate) {
		this.deletedDate = deletedDate;
	}

}
