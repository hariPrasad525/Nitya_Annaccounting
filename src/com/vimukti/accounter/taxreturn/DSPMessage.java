package com.vimukti.accounter.taxreturn;

import com.vimukti.accounter.core.TAXReturn;

public class DSPMessage {
	private TAXReturn taxReturn;
	private String emailId;
	private String senderId;
	private String vatRegistrationNumber;

	public TAXReturn getTaxReturn() {
		return taxReturn;
	}

	public void setTaxReturn(TAXReturn taxReturn) {
		this.taxReturn = taxReturn;
	}

	public String getVatRegistrationNumber() {
		return vatRegistrationNumber;
	}

	public void setVatRegistrationNumber(String vatRegistrationNumber) {
		this.vatRegistrationNumber = vatRegistrationNumber;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

}
